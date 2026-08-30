# SQL Transactions, ACID, and Isolation Levels Interview Questions

## Easy

1. ***What does ACID stand for, and why does a database need all four?***
    - Atomicity, Consistency, Isolation, Durability — the four guarantees a transaction
      manager provides so that a group of statements executed as one logical unit behaves
      predictably even in the presence of crashes, concurrent access, and constraint
      violations. Drop any one of them and a specific, well-known failure mode reappears
      (covered per-property below) — they aren't redundant with each other, each protects
      against a different failure class.

2. ***Atomicity — what does it guarantee, and what breaks without it?***
    - Guarantee: a transaction's statements either **all** take effect or **none** do —
      there is no partially-applied state visible to anyone, even across a crash mid-transaction.
    - Concrete break: a bank transfer moving $100 from account A to account B.
    ```sql
    BEGIN TRANSACTION;
    UPDATE accounts SET balance = balance - 100 WHERE id = 'A'; -- debit succeeds
    -- server crashes / connection drops right here, before the next statement runs
    UPDATE accounts SET balance = balance + 100 WHERE id = 'B'; -- never executes
    COMMIT;
    ```
    - Without atomicity, A's $100 is gone (already applied) but B never received it — the
      money has vanished from the system entirely, with no record explaining where it
      went. With atomicity, the database recognizes the transaction never reached
      `COMMIT` and rolls back the debit on A during recovery — either both updates exist,
      or neither does.

3. ***Consistency — what does it guarantee, and what breaks without it?***
    - Guarantee: a transaction moves the database from one **valid** state to another
      valid state — every constraint (`CHECK`, `FOREIGN KEY`, `UNIQUE`, application-defined
      invariants enforced via triggers) that held before the transaction still holds
      after it.
    - Concrete break: suppose `accounts` has `CHECK (balance >= 0)`. A transfer that would
      leave account A at -$20:
    ```sql
    BEGIN TRANSACTION;
    UPDATE accounts SET balance = balance - 100 WHERE id = 'A'; -- would take A to -20
    UPDATE accounts SET balance = balance + 100 WHERE id = 'B';
    COMMIT;
    ```
    - Without consistency enforcement, this commits and leaves the database in a state
      that violates a fundamental business rule (an account can't be overdrawn) — every
      piece of downstream code now has to defensively re-check something the schema was
      supposed to guarantee. With consistency, the `CHECK` violation aborts the whole
      transaction (atomicity then rolls back the debit too) — the database never contains
      an invalid state, even transiently.
    - Consistency is the one ACID property that's partly the *application's*
      responsibility too — the database only enforces the invariants you actually
      declared as constraints.

4. ***Isolation — what does it guarantee, and what breaks without it?***
    - Guarantee: concurrently running transactions don't see each other's uncommitted, or
      inconsistently-committed, intermediate state — each transaction behaves (to a degree
      controlled by the isolation level, see below) as if it were running alone.
    - Concrete break (a classic **lost update**): two concurrent transfers both read
      account A's balance before either writes it back.
    ```text
    T1: SELECT balance FROM accounts WHERE id='A';   -- reads 500
    T2: SELECT balance FROM accounts WHERE id='A';   -- also reads 500
    T1: UPDATE accounts SET balance = 500 - 100 WHERE id='A'; COMMIT;  -- writes 400
    T2: UPDATE accounts SET balance = 500 - 50  WHERE id='A'; COMMIT;  -- writes 450
    ```
    - T2's write is computed from a stale read (500) and overwrites T1's $100 debit
      entirely — the database ends up at 450 instead of the correct 350
      (500 - 100 - 50). T1's debit is silently lost even though both transactions
      individually committed successfully. This is exactly what isolation levels and
      locking strategies (below) exist to prevent.

5. ***Durability — what does it guarantee, and what breaks without it?***
    - Guarantee: once a transaction is acknowledged as committed, its effects survive any
      subsequent crash — power loss, OS crash, process kill — permanently.
    - Concrete break: a client calls `COMMIT`, the database returns success, and the
      client tells a user "payment confirmed." A power failure hits the server one second
      later, before the committed data was actually flushed to non-volatile storage. On
      restart, the database's in-memory state is gone and the write reverts to its
      pre-commit value — the user was told their payment succeeded, but it didn't.
    - This is why durable engines use **write-ahead logging (WAL)**: the log record for a
      change is `fsync`'d to disk *before* `COMMIT` returns to the client, so recovery can
      always replay it even if the in-memory data pages were never flushed.

6. ***What are the four standard SQL isolation levels, from weakest to strongest?***
    - Read Uncommitted -> Read Committed -> Repeatable Read -> Serializable. Each level up
      the ladder prevents one additional category of anomaly that the level below it still
      allows, at the cost of more locking/blocking (or more abort-and-retry traffic in an
      optimistic/MVCC implementation).

7. ***Read Uncommitted — what does it allow?***
    - The weakest level: a transaction can read rows that another transaction has
      modified but not yet committed. Allows dirty reads, non-repeatable reads, *and*
      phantom reads. Rarely used in practice — mainly for approximate analytics queries
      where a slightly-wrong read is an acceptable trade for zero read-locking overhead.

8. ***Read Committed — what does it add, and what's still missing?***
    - Guarantees a transaction only ever reads data that has been committed by some other
      transaction — dirty reads become impossible. It does this by taking a fresh
      read/snapshot for each individual *statement*, not for the whole transaction, so
      two `SELECT`s of the same row in the same transaction can still see two different
      (both legitimately committed) values if another transaction committed a change to
      that row in between — a non-repeatable read is still possible. This is the default
      isolation level in PostgreSQL, Oracle, and SQL Server.

9. ***Serializable — what does it add?***
    - The strongest level: transactions execute with an effect equivalent to *some*
      serial (one-at-a-time) order, even though they physically run concurrently. Adds
      protection against phantom reads on top of Repeatable Read — a repeated range query
      is guaranteed to return the exact same *set* of rows every time within the
      transaction. Implemented either via full range/predicate locking (locking-based
      engines) or via conflict detection at commit time that aborts one of the
      conflicting transactions (Serializable Snapshot Isolation, e.g. PostgreSQL) —
      either way, this level has the highest blocking/abort-and-retry cost.

10. ***What exactly is a dirty read? Walk through an example.***
    - Reading data written by another transaction that **has not committed yet** — and
      might still roll back, in which case you read a value that never officially
      existed.
    ```text
    T1: UPDATE accounts SET balance = 400 WHERE id='A';   -- not committed yet
    T2: SELECT balance FROM accounts WHERE id='A';         -- reads 400 (dirty!)
    T1: ROLLBACK;                                          -- balance reverts to 500
    ```
    - T2 acted on a balance of 400 that, from the database's final, official history,
      never existed — the account was 500 the whole time as far as any committed record
      is concerned. Only possible under Read Uncommitted.

11. ***What exactly is a non-repeatable read? Walk through an example.***
    - Re-reading the **same row** twice within one transaction and getting two different
      (but both legitimately committed) *values*, because another transaction committed a
      change to that row in between the two reads.
    ```text
    T1: SELECT balance FROM accounts WHERE id='A';        -- reads 500
    T2: UPDATE accounts SET balance = 400 WHERE id='A'; COMMIT;
    T1: SELECT balance FROM accounts WHERE id='A';        -- reads 400 -- different!
    ```
    - Both 500 and 400 were real, committed values — the anomaly is that T1's *own*
      logic saw the row change mid-transaction, which can silently break any logic that
      assumes "I already read this row once, so I know its value for the rest of my
      transaction." Prevented starting at Repeatable Read.

12. ***What exactly is a phantom read? Walk through an example.***
    - Re-running the **same range/set query** (not a single-row read) twice within one
      transaction and getting a different set of *rows* — new rows matching the predicate
      appear (or previously-matching rows disappear), because another transaction
      inserted or deleted qualifying rows in between.
    ```text
    T1: SELECT * FROM orders WHERE amount > 100;    -- returns 5 rows
    T2: INSERT INTO orders (amount) VALUES (150); COMMIT;
    T1: SELECT * FROM orders WHERE amount > 100;    -- returns 6 rows -- a "phantom" appeared
    ```
    - The difference from a non-repeatable read is the *shape* of the change: no
      existing row's value changed under T1 — a brand-new row simply started (or
      stopped) qualifying for the predicate. Prevented only at Serializable (per the ANSI
      standard — see Q13's InnoDB caveat).

## Medium

13. ***Repeatable Read — what does it add, and what's still missing?***
    - Guarantees that once a transaction reads a row, re-reading that same row later in
      the same transaction always returns the same value — non-repeatable reads become
      impossible. Mechanically: either the transaction holds its read locks on
      already-read rows until it ends (locking implementations), or it pins a single
      consistent snapshot taken at the transaction's start and reuses it for every
      statement (MVCC implementations). Per the ANSI standard, this still doesn't protect
      a *range* — a repeated range query can return newly-inserted rows that didn't exist
      on the first read, i.e. a phantom read is still possible in the standard's
      definition.
    - **Engine-specific note (MySQL/InnoDB):** InnoDB's `REPEATABLE READ` deviates from
      the ANSI standard in practice — its next-key locking (row locks plus gap locks)
      also blocks phantom inserts into a locked range for locking reads, so InnoDB's
      `REPEATABLE READ` prevents phantoms for the common case, unlike the
      standard-compliant definition. This is a frequent interview trivia point — always
      clarify "per the ANSI standard" vs. "on this specific engine" when discussing it.

14. ***Isolation level vs. anomaly matrix — summarize.***

    | Isolation Level | Dirty Read | Non-Repeatable Read | Phantom Read |
    |---|---|---|---|
    | Read Uncommitted | Possible | Possible | Possible |
    | Read Committed | Prevented | Possible | Possible |
    | Repeatable Read | Prevented | Prevented | Possible (ANSI standard)\* |
    | Serializable | Prevented | Prevented | Prevented |

    \* MySQL/InnoDB's `REPEATABLE READ` prevents phantoms in practice for locking reads
    (Q13) — the table reflects the ANSI SQL standard's definitions, which is what
    interviewers usually mean unless they name a specific engine.

15. ***If Serializable prevents every anomaly, why isn't it the default everywhere?***
    - Cost. Serializable requires either wide-reaching locks (blocking far more
      concurrent transactions than strictly necessary, hurting throughput under
      contention) or optimistic conflict-checking that aborts and forces a retry of one
      of two transactions that *did* actually conflict (wasted work, and the application
      must be written to retry). Most workloads don't need every anomaly prevented for
      every table — e.g. a dashboard read tolerating a slightly stale row is fine — so
      engines default to Read Committed as the pragmatic middle ground and let individual
      transactions opt into a stronger level (`SET TRANSACTION ISOLATION LEVEL
      SERIALIZABLE`) only where correctness truly requires it (e.g. enforcing a
      uniqueness invariant that spans multiple rows, like "at most one active booking per
      seat per showtime" — see Q22).

16. ***How do MVCC engines (PostgreSQL, InnoDB) achieve isolation without pure locking?***
    - Multi-Version Concurrency Control: instead of a writer blocking every reader, the
      engine keeps multiple physical versions of a row (each tagged with the transaction
      that created it). A reader is handed a consistent **snapshot** — "the state of the
      data as of some point in time" — and reads the version of each row that was
      committed as of that snapshot, ignoring newer uncommitted or later-committed
      versions entirely. This means readers never block writers and writers never block
      readers (a `SELECT` never waits on a concurrent `UPDATE`), which is why MVCC engines
      can offer Read Committed / Repeatable Read cheaply. Writers can still conflict with
      other **writers** on the same row — that's resolved via row-level write locks (or
      an abort-and-retry on conflict at commit time for Serializable Snapshot Isolation,
      Q9).

17. ***Non-repeatable read vs. phantom read — what's the actual distinction?***
    - Both stem from "the same query run twice in one transaction gives inconsistent
      results because of another transaction's commit," but:
    - Non-repeatable read: the **same, already-identified row** now has a **different
      column value**. A row-level lock held on that specific row (as Repeatable Read
      does) is enough to prevent it.
    - Phantom read: the **set of rows matching a predicate** changes — the query returns
      rows that weren't locked (and couldn't have been, since they didn't exist yet) on
      the first pass. Preventing it requires locking the *range/predicate itself* (e.g.
      gap locks, predicate locks), not just the rows that happened to exist at read time
      — a fundamentally different, more expensive mechanism, which is why it needs a
      stronger isolation level.

18. ***Optimistic vs. pessimistic locking — what's the core difference in philosophy?***
    - **Pessimistic locking** assumes conflicts are likely: acquire a lock *before*
      reading/modifying data, blocking every other transaction that wants the same lock
      until you're done.
    ```sql
    BEGIN TRANSACTION;
    SELECT balance FROM accounts WHERE id = 'A' FOR UPDATE;  -- locks the row now
    UPDATE accounts SET balance = balance - 100 WHERE id = 'A';
    COMMIT;  -- lock released
    ```
    - **Optimistic locking** assumes conflicts are rare: don't lock anything upfront;
      read normally, then at write time check whether the row changed since you read it
      (typically via a `version` column or timestamp), and reject/retry if it did.
    ```sql
    -- earlier read: SELECT balance, version FROM accounts WHERE id = 'A'; -- got version=5
    UPDATE accounts
    SET balance = 400, version = version + 1
    WHERE id = 'A' AND version = 5;
    -- if 0 rows affected: someone else updated first -> re-read and retry
    ```

19. ***When would you choose optimistic over pessimistic locking (and vice versa)?***
    - Optimistic: low-contention workloads (most rows are touched by at most one
      transaction at a time — e.g. a user editing their own profile), or workloads where
      holding a lock for a long time (e.g. while waiting on user input, or an external API
      call, between read and write) would be far more costly than an occasional retry.
    - Pessimistic: high-contention workloads where conflicts are common and re-doing work
      after a failed optimistic check would be expensive (e.g. many transactions racing
      to decrement the same limited-inventory counter — better to serialize them via a
      lock than have most of them repeatedly fail and retry).

## Hard

20. ***Two services concurrently update the same row's counter using read-then-write (not an atomic `UPDATE`) — walk through the race condition that occurs under Read Committed, and give two different SQL-level fixes.***
    - The race (a lost update, same shape as Q4 but on an app-level counter instead of a
      balance):
    ```text
    -- counters.value starts at 10
    T1: SELECT value FROM counters WHERE id='visits';         -- reads 10
    T2: SELECT value FROM counters WHERE id='visits';         -- also reads 10
    T1: UPDATE counters SET value = 11 WHERE id='visits'; COMMIT;  -- writes 10+1=11
    T2: UPDATE counters SET value = 11 WHERE id='visits'; COMMIT;  -- writes 10+1=11 (!!)
    -- final value is 11, but two increments happened -- should be 12
    ```
    - Read Committed doesn't help here: each individual `SELECT` and `UPDATE` sees only
      committed data (no dirty read), but there's nothing stopping T2 from reading the
      *same* pre-T1-commit value of 10 and computing its own "new" value from that same
      stale base — the anomaly lives in the gap between the application's read and its
      write, not within either statement alone.
    - **Fix 1 — pessimistic locking (`SELECT ... FOR UPDATE`):**
      ```sql
      BEGIN TRANSACTION;
      SELECT value FROM counters WHERE id = 'visits' FOR UPDATE;  -- T2 blocks here
                                                                    -- until T1 commits
      UPDATE counters SET value = value + 1 WHERE id = 'visits';
      COMMIT;
      ```
      T2's `SELECT ... FOR UPDATE` blocks until T1 releases its lock at commit, then reads
      the *already-updated* value (11), so its own increment correctly produces 12.
    - **Fix 2 — atomic single-statement update:**
      ```sql
      UPDATE counters SET value = value + 1 WHERE id = 'visits';
      ```
      Collapsing the read and write into one statement removes the race entirely — the
      engine reads and writes the row under a single row-level lock as one atomic
      operation, so there's no window for another transaction to read a stale value in
      between. This works correctly under Read Committed and needs no explicit locking
      syntax at all.
    - (A third option, optimistic concurrency via a `version` column as in Q18, also
      fixes this — reject and retry T2's write if `version` no longer matches what it
      read.)
    - **Tradeoff:** the atomic `UPDATE x = x + 1` is simplest and fastest whenever the
      whole operation can be expressed as one set-based statement — prefer it by default.
      `SELECT ... FOR UPDATE` is needed when there's real application logic between the
      read and the write that can't collapse into one statement (e.g. validating the new
      value against business rules before writing it) — but it holds a lock for the
      duration of that logic, so it throttles throughput under contention more than the
      single-statement form. Optimistic/version-based concurrency avoids holding any lock
      at all, which is best when conflicts are rare and/or the gap between read and write
      is long (e.g. waiting on user input), at the cost of needing retry logic in the
      application for the (hopefully rare) conflict case.

21. ***How do deadlocks arise between two transactions each locking rows in a different order, and how do databases typically detect/resolve them?***
    - The setup — each transaction acquires its *first* lock successfully, then blocks
      waiting for a lock the *other* transaction is already holding:
    ```text
    T1: UPDATE accounts SET balance = balance - 50 WHERE id = 'A';  -- T1 locks row A
    T2: UPDATE accounts SET balance = balance - 50 WHERE id = 'B';  -- T2 locks row B
    T1: UPDATE accounts SET balance = balance + 50 WHERE id = 'B';  -- T1 waits for
                                                                      -- T2's lock on B
    T2: UPDATE accounts SET balance = balance + 50 WHERE id = 'A';  -- T2 waits for
                                                                      -- T1's lock on A
    -- T1 is waiting on T2, and T2 is waiting on T1 -- neither can ever proceed
    ```
    - Neither transaction can make progress: T1 holds A and wants B, T2 holds B and wants
      A — a circular wait. Without intervention both would block forever.
    - **Detection:** the database maintains a "waits-for" graph among transactions
      currently blocked on locks — an edge from T1 to T2 whenever T1 is waiting on a lock
      T2 holds. Periodically (or whenever a new wait edge is added), the engine checks
      this graph for a cycle; finding one is the definition of a deadlock.
    - **Resolution:** once a cycle is detected, the engine picks one transaction as the
      **victim** (heuristics vary by engine — commonly the one that has done the least
      work / holds the fewest locks / would be cheapest to roll back) and forcibly aborts
      it, releasing its locks so the other transaction(s) in the cycle can proceed. The
      aborted transaction receives a deadlock error and is expected to be retried by the
      application (this is why application code around transactions should generally be
      written to catch a deadlock error and retry, not treat it as a fatal failure).
    - **Mitigation/avoidance:** the standard fix is to make all transactions acquire locks
      on shared resources in a **consistent global order** (e.g., always lock accounts in
      ascending `id` order, regardless of which account is the "source" or "destination"
      in the business logic) — this eliminates the circular-wait condition structurally,
      since no transaction can ever hold a "later" lock while waiting on an "earlier" one.

22. ***How would you enforce "at most one active booking per seat per showtime" correctly under concurrent requests, and why is this harder than the lost-update case in Q20?***
    - The naive approach — check-then-insert — races the same way a lost update does, but
      is *not* fixed by Repeatable Read/Snapshot Isolation the way Q20's counter problem
      would be, because each transaction's own read really was accurate at the time it ran:
    ```sql
    -- Both T1 and T2 run this concurrently for the same seat_id/showtime_id:
    SELECT 1 FROM bookings
    WHERE seat_id = 5 AND showtime_id = 100 AND status = 'active';
    -- both see 0 rows -- no existing booking, so both proceed to insert
    INSERT INTO bookings (seat_id, showtime_id, status) VALUES (5, 100, 'active');
    -- both inserts succeed -> the seat is now double-booked
    ```
    - This is a **write skew** anomaly: T1 and T2 each read a *disjoint* fact (their own
      read genuinely returned zero matching rows, and stayed true for their own
      transaction) and each wrote based on that individually-true fact, but the
      *combination* of both writes violates an invariant that spans both transactions.
      Because neither transaction's own read set was invalidated by the other's write
      (they each inserted a *new* row rather than modifying a row the other one had
      read), even Repeatable Read/Snapshot Isolation does not catch this — it's a
      genuinely different anomaly class from the lost update in Q20, not just a
      relabeling of it.
    - **Fix — push the invariant into a constraint the storage engine enforces
      atomically**, rather than relying on any amount of application-level
      check-then-act logic:
      ```sql
      CREATE UNIQUE INDEX uq_active_booking
          ON bookings (seat_id, showtime_id)
          WHERE status = 'active';   -- partial unique index (PostgreSQL syntax)
      ```
      Now the second `INSERT` fails outright with a unique-violation error at the moment
      it's executed, regardless of timing or isolation level — the constraint check and
      the write happen atomically inside the storage engine, so there's no window for two
      transactions to both "successfully" pass a check that's since gone stale.
    - **Alternative:** running both transactions under `SERIALIZABLE` would also prevent
      this — a serializable engine using conflict detection (e.g. PostgreSQL's
      Serializable Snapshot Isolation) is specifically designed to detect write-skew
      patterns like this one and abort one of the two transactions at commit time. But
      this is more expensive and more general-purpose than necessary here; a targeted
      unique constraint is cheaper, simpler to reason about, and should be preferred
      whenever the invariant can be expressed as one — reserve `SERIALIZABLE` for
      invariants too complex to encode as a single constraint.

23. ***A long-running (or "idle in transaction") session is causing table bloat and slowing down unrelated queries elsewhere in the database. What's the mechanism, and how do you fix it?***
    - This is an MVCC-specific operational failure mode (most visible in PostgreSQL). Per
      Q16, MVCC engines keep multiple physical versions of a row so that concurrent
      readers never block on writers. Once **no transaction's snapshot can possibly need
      to see an old row version anymore**, that version is "dead" and can be physically
      reclaimed (by autovacuum in PostgreSQL, or purged from the undo/history list in
      other MVCC engines) — but the engine determines "no transaction needs it" using the
      oldest still-open transaction's snapshot horizon across the *entire* database, not
      just the tables that transaction touches.
    - A single transaction left open for a long time (a forgotten `BEGIN` with no
      `COMMIT`/`ROLLBACK`, an ORM holding a connection "idle in transaction" while it
      waits on an unrelated external API call, a debugging session left paused) pins that
      old snapshot horizon in place — so dead row versions produced by completely
      unrelated, actively-running transactions on other tables also can't be reclaimed
      until the long-running transaction finally ends. Symptoms: growing table/index
      bloat, degrading query performance database-wide even though "nothing changed" in
      the affected tables' own workload, and rising disk usage.
    - **Fix:** identify long-running/idle-in-transaction sessions (PostgreSQL:
      `pg_stat_activity`, filtering for `state = 'idle in transaction'` and a long
      `xact_start`), and kill or fix the offending session. Proactively, set
      `idle_in_transaction_session_timeout` (and a reasonable `statement_timeout`) so a
      forgotten or stuck transaction is automatically terminated rather than left open
      indefinitely, and audit application code for any pattern that holds a DB transaction
      open across a network call to another service — that call's latency (and any
      retries/hangs) directly becomes how long the transaction, and the bloat it causes,
      lasts. After resolving the immediate cause, a manual `VACUUM` can reclaim the
      backlog of dead tuples that piled up while blocked.

24. ***A service needs to update its own database row and publish a message to a queue as part of the "same" logical operation (e.g., mark an order paid, then publish an `OrderPaid` event). Why doesn't a normal ACID transaction cover this, and how would you make it reliable?***
    - A database transaction's atomicity guarantee only extends to that one database — it
      says nothing about a separate system like a message queue. Two naive orderings both
      break:
      - **Publish before commit:** if the transaction then rolls back (a later statement
        fails, a constraint violation, a crash), you've already announced an event for
        something that never actually happened in the database of record.
      - **Commit, then publish:** if the process crashes, or the publish call itself fails,
        *after* the commit succeeds but *before* the publish completes, the database
        permanently says "paid" while no event was ever published — and there is no ACID
        mechanism spanning both systems to catch or roll back that gap, since the DB
        transaction already committed successfully on its own terms.
    - **Fix — the transactional outbox pattern:** write the fact "this event needs to be
      published" as a row in an `outbox` table, in the *same* database transaction as the
      business update. Since both writes now target the same database, ordinary atomicity
      guarantees they either both commit or neither does:
      ```sql
      BEGIN TRANSACTION;
      UPDATE orders SET status = 'paid' WHERE id = 123;
      INSERT INTO outbox (event_type, payload, published)
      VALUES ('OrderPaid', '{"order_id":123}', FALSE);
      COMMIT;
      ```
      A separate, asynchronous worker (a polling job, or a change-data-capture tool like
      Debezium reading the database's write-ahead log directly) then reads unpublished
      outbox rows, publishes each to the queue, and marks it published — retrying on
      failure until it succeeds. This decouples "durably record that this happened"
      (synchronous, atomic, immediate) from "notify the outside world" (asynchronous,
      retried, eventually-consistent).
    - **Tradeoff:** the worker can crash or retry *after* publishing but *before* marking
      the outbox row published, causing the same event to be published twice — so this
      pattern only guarantees **at-least-once** delivery, and every downstream consumer of
      the event must be written to be idempotent (safe to process the same event more than
      once, e.g. keyed by an event ID it has already seen).
    - **Why not two-phase commit (2PC) across both systems instead?** 2PC can in principle
      give true atomicity across a database and a queue, but it requires a distributed
      transaction coordinator, blocks *all* participants if the coordinator crashes
      mid-protocol (a stuck "in-doubt" transaction holding locks until manually resolved),
      and most modern message brokers either don't support the XA protocol well or
      deliberately avoid it for throughput reasons. The outbox pattern is generally
      preferred in practice because it needs no cross-system coordinator and degrades
      gracefully (worst case: a delayed or duplicated publish, not a stuck distributed
      lock).

## Related Notes in This Repo
- [`07_Normalization.md`](07_Normalization.md) — schema design side of relational databases.
- [`Common_SQL_Query_Problems.md`](Common_SQL_Query_Problems.md) — classic
  write-this-query interview problems.
