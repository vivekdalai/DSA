# Indexes & Query Optimization Interview Questions

## Easy

1. ***What actually is a database index?***
    - A **separate, auxiliary data structure** — not just a flag or a hint — that stores a
      sorted (or otherwise searchable) mapping from column value(s) to the physical
      location of the corresponding row(s) in the table. It is typically implemented as a
      **B-tree** (balanced tree, not binary tree — each node holds many keys and many
      children, tuned to minimize disk-page reads), though hash indexes and other
      structures exist for specific engines/use cases (see Q9).
    - Conceptually it's the same idea as an index at the back of a textbook: instead of
      reading every page to find every mention of "polymorphism," you scan a short sorted
      list of terms, find the entry, and jump straight to the listed page numbers. The
      table itself is the book; the index is the alphabetized lookup list plus page
      references.
    - Without an index, the only way to find rows matching a condition is a **full table
      scan** — read every single row, in storage order, and check each one against the
      `WHERE` clause. An index lets the database instead traverse a shallow tree toward the
      matching value(s) directly.

2. ***Why does an index speed up lookups — what's the actual complexity argument?***
    - A full table scan is `O(n)` — every one of the `n` rows must be read and checked,
      regardless of how many rows actually match.
    - A B-tree index lookup is `O(log n)` — a B-tree is height-balanced and has a high
      **fanout** (each node commonly holds hundreds of keys, because nodes are sized to
      match a disk page), so even a table with millions of rows typically has a tree only
      3-4 levels deep. Finding a value means comparing against a handful of keys per level
      and descending one level at a time — a constant number of page reads that grows only
      logarithmically with table size, not linearly.
    - Concretely: for a 1,000,000-row table, a full scan touches ~1,000,000 rows; a B-tree
      lookup touches roughly `log(1,000,000)` ≈ 20 comparisons (and in practice far fewer
      actual disk-page reads, since high fanout means each tree level covers a huge range
      of values). That gap is *why* "add an index" is often the single highest-leverage
      fix for a slow query.

3. ***What is a clustered index?***
    - A clustered index determines the **actual physical storage order of the table's
      rows on disk** — the row data itself is stored sorted by the clustered index key,
      as leaves of the B-tree *are* the actual rows (not pointers to them elsewhere).
    - Because physical storage can only be ordered one way, **a table can have at most one
      clustered index**. In many engines (e.g. SQL Server, MySQL/InnoDB), the primary key
      is the clustered index by default unless declared otherwise.
    - Because the leaf level *is* the row, looking up by the clustered key (or scanning a
      range of it) is extremely fast — once you find the leaf, you already have the full
      row, no extra hop needed.

4. ***What is a non-clustered index?***
    - A **separate structure** from the table's physical row storage. Its leaves store
      the indexed column value plus a **pointer/row locator** back to where the actual row
      lives (in SQL Server, that's the clustered key value if a clustered index exists, or
      a direct row identifier otherwise; in MySQL/InnoDB, non-clustered — "secondary" —
      indexes always store the primary key value as the pointer).
    - Since it's a separate structure, **a table can have many non-clustered indexes**
      (one per column or column-combination you need fast lookups on).
    - Looking up via a non-clustered index is a two-step process: (1) traverse the
      non-clustered B-tree to find the matching entry and its row pointer, then (2) follow
      that pointer to fetch the actual row from the table (or from the clustered index, if
      one exists) — commonly called a **key lookup** or **bookmark lookup**. This is why a
      query that needs many columns not present in the non-clustered index itself can still
      be slower than one served entirely by a clustered index, even though both are
      "using an index."

5. ***What is `EXPLAIN` (or `EXPLAIN ANALYZE`) for?***
    - `EXPLAIN` asks the database's query optimizer to show the **execution plan** it
      would use (or did use) to run a query — which tables are accessed, in what order,
      via which access method (full scan vs. some form of index access), how tables are
      joined, and (for `EXPLAIN ANALYZE`) the actual row counts and timing measured by
      really running the query, rather than just the optimizer's pre-execution estimate.
    - It's the primary diagnostic tool for answering "why is this query slow?" — instead of
      guessing, you read the plan to see exactly which step is expensive (e.g. a full scan
      on a huge table where an index seek was expected) and address that specific step.

## Medium

6. ***If indexes are so much faster, why not index every column?***
    - Indexes are not free — they trade **write speed and storage** for **read speed**:
      - **Slower writes.** Every `INSERT`, `UPDATE` (on an indexed column), or `DELETE`
        must also update every index that covers the affected column(s) — rebalancing
        B-tree nodes, splitting/merging pages as needed — not just modify the underlying
        table. A table with five indexes pays that update cost five times over on every
        write, not once.
      - **Extra storage.** Each index is a full additional data structure that duplicates
        (a copy of) the indexed column values plus pointers back to the row, so storage
        grows with every index added — this can be substantial on large tables or with
        many indexes.
      - **Diminishing/negative returns on read speed** for columns that don't actually
        benefit (see Q14 on low-cardinality columns) — an unused or rarely-useful index is
        pure write/storage overhead with no offsetting read benefit.
    - The practical rule: index columns that are actually used in `WHERE`, `JOIN ON`,
      `ORDER BY`, or `GROUP BY` clauses on tables where reads meaningfully outnumber
      writes, and be deliberate rather than indexing everything reflexively.

7. ***Clustered vs. non-clustered — summary table.***

    | | Clustered | Non-Clustered |
    |---|---|---|
    | Determines physical row order | Yes | No |
    | Max per table | 1 | Many |
    | Leaf level contains | The actual row data | Indexed column(s) + pointer to row |
    | Lookup by indexed value | Direct — leaf *is* the row | Two-step — find pointer, then fetch row |
    | Typical default | Primary key (engine-dependent) | Any column explicitly indexed with `CREATE INDEX` |

8. ***What is a composite index, and what is the "leftmost prefix" rule?***
    - A composite (multi-column) index is built on more than one column, e.g.:
      ```sql
      CREATE INDEX idx_a_b_c ON some_table (a, b, c);
      ```
    - Internally, this is still a single B-tree, but the sort key is the **concatenation**
      of `(a, b, c)` — sorted first by `a`, then within equal values of `a` sorted by `b`,
      then within equal `(a, b)` sorted by `c`. This is exactly like sorting a phone book
      by (last name, first name, middle name): entries are alphabetized by last name first,
      and only *within* a group of identical last names does first name become the
      tiebreaker.
    - Because of this sort order, the index can only be searched efficiently starting from
      its **leftmost column** and moving right without skipping a column — this is the
      "leftmost prefix" rule. A composite index on `(a, b, c)` can efficiently serve:
      - a filter on `a` alone,
      - a filter on `a AND b`,
      - a filter on `a AND b AND c`.
    - It generally **cannot** efficiently serve a filter on `b` alone, or `c` alone, or
      `b AND c` without `a` — because without knowing `a`, the index has no way to jump to
      the relevant range of `b` values; `b` is only sorted *within* each distinct `a`
      value, not globally.

9. ***Worked example — which of these queries actually use `idx_a_b_c ON (a, b, c)`?***
    ```sql
    -- (1) Uses the index efficiently — filters on leftmost column `a`.
    SELECT * FROM some_table WHERE a = 5;

    -- (2) Uses the index efficiently — leftmost prefix (a, b), in order.
    SELECT * FROM some_table WHERE a = 5 AND b = 10;

    -- (3) Uses the index efficiently — full prefix (a, b, c), in order.
    SELECT * FROM some_table WHERE a = 5 AND b = 10 AND c = 20;

    -- (4) Does NOT use the index efficiently (typically a full scan) —
    --     filters only on `b`, which is not the leftmost column.
    SELECT * FROM some_table WHERE b = 10;

    -- (5) Does NOT use the index efficiently — filters only on `c`.
    SELECT * FROM some_table WHERE c = 20;

    -- (6) Partially usable — the optimizer can use the index to satisfy the `a`
    --     predicate (jump straight to a = 5), but then must scan/filter within that
    --     range for c = 20, since c isn't reachable without b also being fixed.
    --     Still generally faster than a full table scan, just not as tight a seek
    --     as query (3).
    SELECT * FROM some_table WHERE a = 5 AND c = 20;

    -- (7) Order of conditions in the WHERE clause does NOT matter — the optimizer
    --     reorders predicates; this uses the index exactly like query (2) does.
    SELECT * FROM some_table WHERE b = 10 AND a = 5;
    ```
    - The practical takeaway: **column order in a composite index should match the most
      common/most selective leading filter columns your queries actually use** — an index
      on `(a, b, c)` is not interchangeable with one on `(b, a, c)` or `(c, b, a)`, even
      though they cover the same three columns.

10. ***If you need to query on `b` alone frequently, does the `(a, b, c)` index help?***
    - Not meaningfully — you would need a separate index either on `b` alone, or with `b`
      as the leftmost column (e.g. `(b, a)` if queries also sometimes add `a`). A single
      composite index cannot efficiently serve arbitrary leftmost columns; it's common for
      real schemas to carry a few different indexes covering the same columns in different
      leading orders, matching each hot query pattern's leading filter.

11. ***Why would the database ignore an index even though one exists on the filtered column?***
    - The general theme: the optimizer can only use an index if it can determine the set of
      matching values (or a matching range) *before* evaluating each row individually.
      Anything that obscures the indexed column's raw value from the index — wrapping it
      in a function, comparing it with a leading wildcard, or making the index simply not
      worth the extra hop — defeats that.

12. ***Applying a function to the indexed column — why does this break index usage?***
    ```sql
    -- Index-unfriendly: the index stores raw order_date values, not YEAR(order_date)
    -- results, so the engine cannot binary-search the tree for "YEAR = 2024" without
    -- computing YEAR() for every single row first — which requires reading every row,
    -- i.e. a full table scan.
    SELECT * FROM orders WHERE YEAR(order_date) = 2024;

    -- Index-friendly (sargable): order_date itself is compared directly, unmodified,
    -- against literal bounds — the engine can seek to '2024-01-01' in the B-tree and
    -- scan forward until it passes '2025-01-01', touching only matching rows/pages.
    SELECT * FROM orders
    WHERE order_date >= '2024-01-01' AND order_date < '2025-01-01';
    ```
    - A predicate the optimizer *can* translate into an index seek is called **sargable**
      (Search ARGument ABLE). `YEAR(order_date) = 2024` is non-sargable: the index is
      sorted by the raw date value, not by the output of `YEAR()` applied to it, so the
      only way to know which rows satisfy the condition is to compute `YEAR()` for every
      row and check — exactly what an index exists to avoid. Rewriting the condition as an
      equivalent range over the *raw, unmodified* column restores sargability.
    - The same logic applies to any function wrapping the column: `WHERE UPPER(name) =
      'ALICE'`, `WHERE price * 1.1 > 100`, `WHERE SUBSTRING(email, 1, 5) = 'admin'`, etc.
      all defeat a plain index on that column for the same reason.

13. ***Leading wildcard vs. trailing wildcard in `LIKE` — why does one break the index and not the other?***
    ```sql
    -- Index-friendly: '%' is at the end. The engine can seek directly to the point in
    -- the sorted index where entries start with 'John', then scan forward only as long
    -- as that prefix continues to match — a bounded range scan.
    SELECT * FROM customers WHERE name LIKE 'John%';

    -- Index-unfriendly: '%' is at the start. A match could occur anywhere inside the
    -- string, so there's no fixed prefix to seek to in a structure that's sorted
    -- left-to-right by leading characters — the engine must check every value,
    -- i.e. a full scan (or full index scan, which is barely better than a table scan).
    SELECT * FROM customers WHERE name LIKE '%smith';
    ```
    - A B-tree index on a text column is sorted the way a dictionary is — by the string's
      **leading characters first**. A trailing wildcard (`'John%'`) preserves a known,
      fixed prefix, so the tree can be seeked to that prefix directly. A leading wildcard
      (`'%smith'`) means the match could start at any position, so no fixed prefix exists
      to seek to — the sort order the index provides gives no advantage, forcing a scan of
      every row. (Some engines offer specialized structures — e.g. trigram/full-text
      indexes — specifically to make leading-wildcard or substring search fast when
      needed; a plain B-tree index cannot.)

14. ***Low-cardinality columns — why might the optimizer skip the index even for a simple equality filter?***
    ```sql
    -- is_active is a boolean: only 2 distinct values across possibly millions of rows.
    SELECT * FROM users WHERE is_active = TRUE;
    ```
    - **Cardinality** = the number of distinct values a column can take, relative to the
      table's row count. A boolean flag has cardinality 2, so an index on it can (at best)
      narrow the search to roughly half the table — if 50% of rows have `is_active = TRUE`,
      the index doesn't meaningfully reduce the work.
    - Concretely, if `is_active = TRUE` matches 40% of a million-row table, using the
      non-clustered index means: traverse the tree to find matching entries, then perform
      ~400,000 separate row-pointer lookups back to the actual table (Q4) — likely
      scattered randomly across disk pages. A full table scan instead reads the table
      **sequentially**, which is dramatically cheaper per row on spinning disk and still
      generally cheaper on SSD due to read-ahead and fewer random I/O operations.
    - The query optimizer estimates this trade-off using column statistics (roughly, "what
      fraction of rows will this predicate match") and **chooses whichever plan it
      estimates is cheaper** — for a low-selectivity predicate (matches a large fraction of
      rows), a full scan often wins even with a usable index sitting right there. This is
      normal, correct optimizer behavior, not a sign the index is broken — it's why "just
      add an index" doesn't always yield a speedup, and it's a common interview trap
      question.

15. ***How can composite indexes indirectly fix a low-cardinality problem?***
    - Pairing a low-cardinality column with a higher-cardinality one in a composite index
      (e.g. `(is_active, created_at)`) can still be useful *if* queries commonly filter on
      both together — the combination `(is_active = TRUE AND created_at > '2024-01-01')`
      can have much higher selectivity than either predicate alone, making the composite
      index worth using even though `is_active` alone wouldn't be.

16. ***What does "full table scan" vs. "index seek/scan" mean in an `EXPLAIN` plan?***
    - **Full table scan** (`type: ALL` in MySQL, `Seq Scan` in PostgreSQL): every row in
      the table is read and checked against the `WHERE` clause, regardless of how many
      actually match. Expected/fine for small tables or queries that legitimately need most
      rows; a red flag on a large table for a query that should only touch a handful of
      rows.
    - **Index scan**: the engine reads through some or all of an index structure (rather
      than the table) — better than a full table scan since index entries are smaller and
      often more of them fit per page, but it still isn't a targeted lookup if it has to
      traverse a large portion of the index.
    - **Index seek** (`type: ref`/`range`/`const` in MySQL, `Index Scan` / `Index Only
      Scan` with a start/stop condition in PostgreSQL): the engine navigates the B-tree
      directly to the matching value(s) and reads only the relevant range — this is the
      `O(log n)`-to-get-there, then-read-only-what-matches behavior from Q2, and generally
      what you want to see for a selective, indexed lookup.
    - **Index-only scan / covering index**: the best case — every column the query needs
      is present in the index itself, so the engine never needs the extra hop back to the
      table (Q4's "key lookup") at all.
    - Interview-relevant summary: seeing `ALL`/`Seq Scan` on a large table for a highly
      selective query is the signature of a missing or unusable index; seeing a scan whose
      estimated/actual row count is much larger than expected is the signature of a
      non-sargable predicate (Q12) quietly defeating an index that does exist.

## Hard

17. ***A query against a large table is slow, and `EXPLAIN` shows a full table scan even though an index exists on the filtered column. What are the possible reasons, and how would you confirm each one?***
    - This is a diagnostic checklist question — the point is to work through candidate
      causes systematically rather than guess-and-check:
      1. **Non-sargable predicate (Q12).** The column is wrapped in a function, arithmetic,
         or implicit type cast (`WHERE CAST(user_id AS VARCHAR) = '123'` against an
         integer column, or comparing a `VARCHAR` column to a numeric literal that forces
         an implicit conversion). *Confirm:* inspect the `WHERE` clause for anything
         wrapping the column; rewrite the predicate to compare the raw column against a
         literal of the exact same type and re-run `EXPLAIN`.
      2. **Leading wildcard or a pattern the index can't seek (Q13).** *Confirm:* check for
         `LIKE '%...'` or a regex predicate on the column.
      3. **Low selectivity (Q14).** The predicate matches a large fraction of the table, so
         the optimizer's cost model genuinely prefers a sequential scan. *Confirm:* run
         `SELECT COUNT(*) FROM t WHERE <predicate>` against `SELECT COUNT(*) FROM t` — if
         the predicate matches, say, 30%+ of rows, a full scan may legitimately be the
         faster plan and the index isn't "broken."
      4. **Stale statistics.** The optimizer's row-count estimates come from statistics
         gathered at the last `ANALYZE`/auto-analyze run; if the table's data distribution
         changed drastically since then (a bulk load, a data migration, a seasonal shift),
         the optimizer may be working from a stale picture and misjudge selectivity.
         *Confirm:* compare the plan's *estimated* row count (from `EXPLAIN`) against the
         *actual* row count (from `EXPLAIN ANALYZE`) — a large gap between the two is the
         signature of stale statistics. Fix by manually running `ANALYZE`/`UPDATE
         STATISTICS` and re-checking the plan.
      5. **Wrong index chosen among several candidates**, or the optimizer's cost model
         simply estimates the scan as cheaper for this specific data distribution (e.g. a
         tiny table, or a table that fits entirely in one or two disk pages — for small
         tables a full scan can genuinely be faster than the overhead of a tree traversal).
         *Confirm:* check table size/row count; on a table with only a few hundred rows,
         a full scan is expected and not a problem worth chasing.
      6. **The index isn't the one being asked for at all** — e.g. the query filters on
         `email` but the only index is a composite `(tenant_id, email)`, and the query
         never filters on `tenant_id`, so the leftmost-prefix rule (Q8) blocks its use.
         *Confirm:* check the exact column(s) and order of the existing index definition
         against the query's actual filter columns.
    - The general method, regardless of which cause it turns out to be: never guess — pull
      the exact index definition, run `EXPLAIN ANALYZE` to compare estimated vs. actual
      rows, and check the predicate's literal shape against the column's literal type.
      Each of the above has a distinct, checkable fingerprint.

18. ***A table is write-heavy (thousands of inserts/sec) but also needs to serve a handful of specific read queries fast. How do you decide which indexes are actually worth the write-cost tradeoff, and how do you verify a candidate index is even being used?***
    - **Deciding what's worth it:** start from the *read* side, not the schema — enumerate
      the actual hot queries the application runs (not hypothetical future ones) and their
      `WHERE`/`JOIN`/`ORDER BY` columns. For each candidate index, weigh:
      - How selective is the predicate it serves (Q14)? An index that only narrows a scan
        from 100% to 60% of rows isn't worth its write cost; one that narrows to 0.1% is.
      - How *frequently* is the query that would use it actually run, versus how frequently
        the table is written to? An index serving a query run once an hour on a table
        written to thousands of times a second is paying continuous cost for occasional
        benefit — it may be better served by a read replica or a periodically-refreshed
        summary table instead of an index on the hot write path.
      - Can one composite index serve *multiple* of the hot queries (via shared leftmost
        prefixes, Q8), rather than adding a separate single-column index per query? Fewer,
        well-chosen composite indexes usually beat many narrow ones.
    - **Verifying a candidate index is actually used**, before and after adding it:
      1. Run `EXPLAIN ANALYZE` on the target query and confirm the plan reports an index
         seek/index-only scan against the new index, not a scan of a different index or a
         full table scan.
      2. Check engine-level usage counters over a real traffic window rather than one
         query: PostgreSQL's `pg_stat_user_indexes` (`idx_scan` column — zero or near-zero
         after a representative period means the index isn't earning its write cost),
         MySQL's `sys.schema_unused_indexes` / `performance_schema`, or SQL Server's
         `sys.dm_db_index_usage_stats`.
      3. Deploy the index to a staging environment with production-scale data volume (not
         a small dev dataset — cardinality and page counts on a tiny table give misleading
         plan choices, Q14/Q17) and measure both query latency *and* write throughput —
         confirm the intended read query speeds up and that insert latency doesn't regress
         past an acceptable threshold before rolling out to production. If possible, roll
         out to a subset of production traffic first and watch write-latency dashboards
         rather than assuming the staging measurement transfers exactly.
    - The underlying judgment call: an index is only worth adding if a *specific, measured*
      read benefit exceeds its *measured* write cost on this workload — never add one
      speculatively "because the column might get queried someday."

19. ***You have `idx_a_b_c ON (a, b, c)`. A new hot query pattern emerges: `WHERE a = ? AND c = ? ORDER BY c`. Does the existing index serve this well, and how would you redesign the indexing to fix it?***
    - Walk the existing index against this query: the engine can seek to the `a = ?`
      subtree (leftmost prefix satisfied), but within that subtree, rows are sorted by `b`
      first and only *then* by `c` — since `b` isn't part of the filter, the entries for a
      fixed `a` are effectively in an order that's arbitrary with respect to `c`. The
      engine must scan every row in the `a` range and check `c = ?` individually (same
      partial-usability case as query (6) in Q9), *and* because the surviving rows aren't
      sorted by `c`, the database still needs a separate sort step to satisfy `ORDER BY c`
      — the index helps narrow the `a` range but does nothing for the `c` filter or the
      ordering.
    - Fix: add a second composite index with `c` immediately after `a` and with `b`
      dropped entirely — `CREATE INDEX idx_a_c ON some_table (a, c);`. Now for a fixed `a`,
      entries are sorted by `c`, so the engine can seek directly to `c = ?` *and* the
      matching rows are already returned in `c` order, satisfying `ORDER BY c` with no
      extra sort.
    - The tradeoff this introduces: you now maintain two composite indexes that both start
      with `a` and largely overlap in purpose (`(a, b, c)` and `(a, c)`) — every write to
      the table pays the maintenance cost of both (Q6). Before adding the second index,
      confirm (per Q18) that the `(a, c) + ORDER BY c` query pattern is frequent/important
      enough to justify that extra write cost, and check whether `b` is still needed by any
      other query pattern served by the original `(a, b, c)` index — if nothing still needs
      `b`, it may be cheaper to redesign `(a, b, c)` into `(a, c, b)` instead of carrying
      two separate indexes, provided no other query relies on `(a, b)` being a usable
      leftmost prefix.

20. ***The optimizer chooses a full table scan for a query whose predicate used to be highly selective, but the data distribution has since shifted (e.g. a status column that was 90% `'pending'` is now 90% `'shipped'`). Why does this happen, and how do you fix it?***
    - The optimizer's plan choice is driven by **column statistics** — a histogram/estimate
      of value distribution captured the last time statistics were gathered (automatically,
      on a schedule or threshold of changed rows, or manually via `ANALYZE`/`UPDATE
      STATISTICS`). If a large, fast shift in the data (a status flag flipping en masse
      after a batch job, a seasonal traffic pattern, a bulk backfill) happens *between*
      statistics refreshes, the optimizer is planning against a picture of the data that no
      longer matches reality — e.g. it may still believe `status = 'pending'` matches only
      10% of rows and choose an index seek, when it actually now matches 90% and a full
      scan would truly be cheaper (or the reverse: it still assumes a predicate is
      unselective and skips a now much-more-useful index).
    - *Confirming it's a statistics problem specifically* (vs. one of the other causes in
      Q17): compare the plan's estimated row count against `EXPLAIN ANALYZE`'s actual row
      count — a large mismatch, on a query whose predicate shape is otherwise sargable and
      selective, is the fingerprint of stale statistics rather than a structurally
      unusable index.
    - *Fix:* manually trigger a statistics refresh (`ANALYZE table_name` in PostgreSQL/
      MySQL, `UPDATE STATISTICS table_name` in SQL Server) immediately after any bulk
      data change, rather than waiting for the engine's automatic threshold-based trigger
      to catch up — and for tables that undergo frequent large swings, consider tuning the
      auto-analyze threshold to run more often. As a short-term/emergency mitigation while
      statistics catch up, some engines support a plan/index hint to force the intended
      access path, but this should be treated as temporary — hardcoding a plan fights the
      optimizer permanently and can itself become wrong once statistics are current again.

21. ***A `DELETE` on a parent table (or a join between parent and child tables) is unexpectedly slow, and the child table's foreign-key column has no index. Why does this happen, and does every engine handle it the same way?***
    - Two related mechanisms both degrade without an index on the child's FK column:
      - **Referential integrity checks / cascades.** When you delete a row from the parent
        (or update its key), the engine must find every child row referencing it — either
        to block the delete (`RESTRICT`/default FK behavior, if any child rows still
        exist) or to cascade the delete (`ON DELETE CASCADE`). Without an index on the
        child's FK column, finding those rows means a full scan of the child table, *for
        every single parent row deleted* — turning a batch delete into an
        `O(parent_rows × child_table_size)` operation instead of an indexed lookup per
        parent row.
      - **Joins.** `SELECT * FROM customers c JOIN orders o ON o.customer_id = c.id` needs
        to find, for each `customers` row, the matching `orders` rows — without an index on
        `orders.customer_id`, the engine must scan the entirety of `orders` per outer row
        (or fall back to a full hash/merge join that at least avoids the repeated scan, but
        still costs a full read of `orders` where an indexed nested-loop could have touched
        only the matching rows).
      ```sql
      CREATE TABLE customers (id INT PRIMARY KEY);
      CREATE TABLE orders (
          id INT PRIMARY KEY,
          customer_id INT REFERENCES customers(id)   -- FK column, unindexed
      );

      DELETE FROM customers WHERE id = 42;
      -- Without an index on orders.customer_id, checking/cascading this delete
      -- requires scanning all of `orders` to find rows where customer_id = 42.
      ```
    - **Engine-specific nuance — this is the interview trap:** MySQL/InnoDB automatically
      creates an index on a foreign-key column when the constraint is created (because
      InnoDB requires one to enforce the constraint efficiently), so this problem is less
      likely to appear silently in MySQL. PostgreSQL and SQL Server do **not** automatically
      index the referencing (child) column — only the referenced (parent) column is
      guaranteed indexed (it's the primary/unique key being referenced) — so it's entirely
      possible to define a valid, constraint-enforced foreign key in Postgres/SQL Server
      and still have this exact slow-delete/slow-join problem until you explicitly run
      `CREATE INDEX idx_orders_customer_id ON orders(customer_id);`. Always check this
      explicitly rather than assuming a FK constraint implies an index on both sides.
