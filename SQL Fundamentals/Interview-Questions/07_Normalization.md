# Normalization Interview Questions

## Easy

1. ***What is database normalization trying to achieve?***
    - Organize columns and tables to minimize **redundant data**, and by doing so,
      eliminate the **update, insert, and delete anomalies** that redundancy causes.
      Normalization is a series of rules (normal forms) applied progressively — each one
      fixes a specific category of anomaly by splitting a table so that every attribute
      depends only on its own table's key, not on some other fact that happens to be
      duplicated alongside it.

2. ***Concrete "before" example — what goes wrong with one big denormalized table?***
    - A single `Orders` table that stuffs customer info and a multi-item product list
      directly into every order row:

      | OrderID | CustomerID | CustomerName | CustomerAddress | Products |
      |---|---|---|---|---|
      | 1 | C1 | Alice | 123 Main St | Widget:9.99:3, Gadget:19.99:1 |
      | 2 | C1 | Alice | 123 Main St | Chair:49.99:2 |
      | 3 | C2 | Bob | 456 Oak Ave | Widget:9.99:2 |

    - This single table already exhibits every classic anomaly:
    - **Update anomaly:** Alice's two orders (rows 1 and 2) both carry her address. If
      she moves, the address has to be updated in *every one of her order rows* — miss
      row 2, and the database now claims Alice has two different addresses at once, with
      no way to tell which is correct.
    - **Insert anomaly:** there's no way to store a new customer's contact info until
      they place an order — `CustomerID`/`CustomerName`/`CustomerAddress` only exist
      attached to an `OrderID`, so "add a customer with no orders yet" has nowhere to go.
    - **Delete anomaly:** if Bob's only order (row 3) is deleted, Bob's address is
      deleted along with it — there is no other place in the schema recording "Bob lives
      at 456 Oak Ave."
    - **1NF violation:** the `Products` column crams multiple products (each with its own
      price and quantity) into one text field. You can't write a plain
      `WHERE Products = 'Widget'` to find orders containing a Widget, can't enforce a
      foreign key from a product to a `Products` catalog table, and can't sum quantities
      per product with a simple `GROUP BY`.

3. ***What rule does 1NF add?***
    - Every column must hold a single, atomic value (no comma-separated lists, no
      repeating groups of columns like `Product1, Product2, Product3`), and the table
      must have a well-defined key that uniquely identifies each row.

4. ***Apply 1NF to the running example.***
    - Split the multi-valued `Products` field into one row per (order, product) pair:

      | OrderID | CustomerID | CustomerName | CustomerAddress | Product | ProductPrice | Quantity |
      |---|---|---|---|---|---|---|
      | 1 | C1 | Alice | 123 Main St | Widget | 9.99 | 3 |
      | 1 | C1 | Alice | 123 Main St | Gadget | 19.99 | 1 |
      | 2 | C1 | Alice | 123 Main St | Chair | 49.99 | 2 |
      | 3 | C2 | Bob | 456 Oak Ave | Widget | 9.99 | 2 |

    - Now every cell is atomic, and `(OrderID, Product)` is a valid composite key (no two
      rows share both). But redundancy actually got *worse* — Alice's name/address is now
      repeated across three rows instead of two, and Widget's price is repeated across
      two rows. The update/insert/delete anomalies from Q2 are all still present; 1NF
      alone doesn't fix them, it just makes the data shape queryable. That's what 2NF and
      3NF are for.

5. ***What rule does 2NF add?***
    - Must already be in 1NF, **and** every non-key attribute must depend on the *entire*
      candidate key, not just part of it. This rule is only meaningful when the key is
      composite — a single-column key can't have a "partial" dependency on itself.

6. ***What rule does 3NF add?***
    - Must already be in 2NF, **and** no non-key attribute may depend on another
      **non-key** attribute (a *transitive* dependency) — every non-key attribute must
      depend directly on the key, and only on the key.

7. ***Is 3NF the end of the line — what's beyond it?***
    - 3NF is the practical target for most schemas and is what "normalized" usually means
      in an interview answer. Stricter forms exist for edge cases 3NF doesn't fully close
      — **BCNF** (Boyce-Codd Normal Form) handles anomalies from overlapping composite
      candidate keys, and **4NF/5NF** handle multi-valued and join dependencies. These
      come up rarely outside academic contexts; worth naming if asked "is there anything
      beyond 3NF," but 3NF (or a deliberately-denormalized variant of it) is what real
      schemas target. (Q13 works through a concrete case where 3NF and BCNF actually
      diverge.)

## Medium

8. ***Apply 2NF to the running example.***
    - The 1NF table's key is the composite `(OrderID, Product)`. Check each non-key
      column against it:
    - `CustomerID`, `CustomerName`, `CustomerAddress` depend only on `OrderID` (the
      product ordered has nothing to do with who the customer is) — a **partial
      dependency**.
    - `ProductPrice` depends only on `Product` (which order it's attached to doesn't
      change the item's price) — also a **partial dependency**.
    - `Quantity` genuinely depends on the *combination* of `OrderID` and `Product` (how
      many of that specific product were in that specific order) — fully dependent, this
      one is fine.
    - Fix: split off each partially-dependent group into its own table, keyed by just the
      part of the key it actually depends on:
    ```sql
    CREATE TABLE Orders (
        OrderID INT PRIMARY KEY,
        CustomerID INT,
        CustomerName VARCHAR(100),
        CustomerAddress VARCHAR(200)
    );

    CREATE TABLE Products (
        Product VARCHAR(50) PRIMARY KEY,
        ProductPrice DECIMAL(10,2)
    );

    CREATE TABLE OrderItems (
        OrderID INT,
        Product VARCHAR(50),
        Quantity INT,
        PRIMARY KEY (OrderID, Product)
    );
    ```
    - Now `ProductPrice` is stored exactly once per product (not once per order line) —
      `Orders` and `Products` each have a single-column key, so 2NF is trivially
      satisfied for them, and `OrderItems`'s only non-key column (`Quantity`) depends on
      its full composite key.

9. ***Apply 3NF to the running example.***
    - Look at the `Orders` table from Q8: `OrderID` (the key) determines `CustomerID`
      directly — fine. But `CustomerName` and `CustomerAddress` don't really depend on
      `OrderID` at all; they depend on **`CustomerID`**, which is itself just another
      non-key column of the same table (`OrderID -> CustomerID -> CustomerName,
      CustomerAddress`) — a transitive dependency. Symptom: Alice's address is still
      redundantly stored once per order row (rows for OrderID 1 and 2 both say "Alice,
      123 Main St") — the exact same update anomaly from Q2, just now confined to one
      smaller table instead of the whole flat schema.
    - Fix: pull the transitively-dependent attributes into their own table, keyed by the
      attribute they actually depend on:
    ```sql
    CREATE TABLE Customers (
        CustomerID INT PRIMARY KEY,
        CustomerName VARCHAR(100),
        CustomerAddress VARCHAR(200)
    );

    CREATE TABLE Orders (
        OrderID INT PRIMARY KEY,
        CustomerID INT REFERENCES Customers(CustomerID)
    );
    ```
    - Now Alice's address exists in exactly **one** row, in `Customers`. Moving her is a
      single-row `UPDATE Customers SET CustomerAddress = ... WHERE CustomerID = 'C1'` —
      it's now structurally impossible for her two orders to disagree about her address,
      because they no longer store it at all; they just reference `C1`.
    - Final 3NF schema for the whole example: `Customers`, `Orders`, `Products`,
      `OrderItems` — four tables, each with no redundant, independently-updatable copies
      of the same fact.

10. ***Why isn't every production schema fully normalized?***
    - Every join at query time costs something — normalized data split across many small
      tables means a "give me everything about this order" query has to join `Orders`,
      `Customers`, `OrderItems`, and `Products` back together every single time it's
      read. For read-heavy workloads where the same joined shape is queried constantly,
      that join cost, paid on every read, can outweigh the benefit of not duplicating a
      few columns.

11. ***What does denormalization actually trade away?***
    - You deliberately reintroduce redundancy (e.g. storing `CustomerName` directly on
      the `Orders` row again) to make reads cheaper (no join needed), at the cost of
      needing more careful writes — every place that redundant value lives has to be kept
      in sync (via application logic, triggers, or an async process), and the simple
      "single row, single source of truth" guarantee a normalized schema gives you for
      free is gone.

12. ***When does denormalization tend to make sense, and when doesn't it?***
    - **Tends to make sense:** read-heavy analytical/reporting workloads (OLAP, data
      warehouses) — e.g. a star schema deliberately denormalizes dimension data into wide
      fact tables because the workload is overwhelmingly "scan and aggregate," writes
      happen in controlled batch/ETL loads rather than many concurrent small
      transactions, and eventual/periodic consistency between the source of truth and the
      denormalized copy is acceptable.
    - **Tends not to make sense:** write-heavy transactional systems (OLTP) — e.g. the
      bank-account example used throughout the ACID note — where strong consistency
      matters immediately (every write must be correct *now*, not reconciled later) and
      duplicated data multiplies the number of places a bug or a missed update can leave
      the system inconsistent. There, the anomaly-prevention normalization buys is worth
      the join cost.

## Hard

13. ***You've normalized a schema to 3NF, but a critical dashboard query now requires 6 joins and is too slow. How do you decide what to selectively denormalize, and what do you now have to worry about on the write path that you didn't before?***
    - **Don't denormalize blindly — profile first.** Run `EXPLAIN ANALYZE` on the actual
      slow query; six joins are not automatically six expensive joins — a join against a
      tiny reference/lookup table (e.g. a 5-row `order_status` table) costs almost
      nothing, while one or two joins against large tables usually account for nearly all
      the cost. Target the specific expensive join(s), not the schema in general.
    - **Two different fixes, with different tradeoffs:**
      1. **Denormalize the source-of-truth tables directly** — e.g. copy `CustomerName`
         onto the `Orders` row itself so the dashboard query no longer needs to join
         `Customers` at all. Fastest reads, but the redundancy is now permanent and lives
         in the transactional schema itself.
      2. **Build a separate precomputed reporting table (or materialized view)** that
         already contains the 6-way join's result, refreshed on a schedule, via triggers,
         or via change-data-capture — the OLTP schema stays normalized (correctness/write
         simplicity preserved) while the dashboard reads a flat, pre-joined copy.
         Generally preferable when the fast-read requirement is specific to one reporting
         use case rather than a core, latency-sensitive part of the write-serving system.
    - **What changes on the write path once you pick option 1 (direct denormalization):**
      every write that changes the duplicated source value (e.g. a customer renaming
      themselves) must now also update every place that value was copied to — via
      application code, a database trigger, or an async job — and until that propagation
      happens, the copies are stale/inconsistent. You've traded "correct by construction"
      (a single source of truth enforced by the schema itself) for "correct only if every
      write path remembered to propagate the update," which means you now need
      monitoring/reconciliation jobs to catch drift, and every future engineer touching
      the `Customers` table has to remember the duplicate exists. This is exactly the
      class of anomaly normalization exists to prevent (Q2) — you are deliberately
      reintroducing it in one narrow, measured place, not undoing the schema's design
      wholesale.

14. ***Give a concrete relation that's in 3NF but not in BCNF — where do 3NF and BCNF actually diverge, and why does it matter?***
    - Consider `StudentSubjectInstructor(Student, Subject, Instructor)` with two business
      rules: (a) a student studying a given subject has exactly one instructor for it —
      `(Student, Subject) -> Instructor` — and (b) each instructor teaches only one
      subject — `Instructor -> Subject`.
    - **Candidate keys:** `(Student, Subject)` determines `Instructor` (rule a), so its
      closure covers all three attributes — it's a candidate key. `(Student, Instructor)`
      determines `Subject` (via rule b), so its closure also covers all three attributes —
      it's *also* a candidate key. `Subject` is therefore a **prime attribute** (it
      appears in the candidate key `(Student, Subject)`), even though it's also the
      target of the `Instructor -> Subject` dependency.
    - **Why this is 3NF:** 3NF allows a dependency `X -> A` as long as *either* `X` is a
      superkey *or* `A` is a prime attribute. For `Instructor -> Subject`: `Instructor`
      alone is not a superkey, but `Subject` is prime (shown above) — so the exception
      clause is satisfied and the relation passes 3NF.
    - **Why this is not BCNF:** BCNF has no prime-attribute exception — it requires that
      for *every* dependency `X -> A`, `X` must be a superkey, full stop. `Instructor ->
      Subject` violates that (`Instructor` alone is not a superkey), so the relation fails
      BCNF even though it passes 3NF.
    - **The anomaly this allows to slip through:** since every instructor teaches only one
      subject, that subject value is redundantly repeated once per student that
      instructor teaches:

      | Student | Subject | Instructor |
      |---|---|---|
      | Alice | Math | Smith |
      | Bob | Math | Smith |
      | Carol | Math | Smith |

      If Smith switches from teaching Math to Physics, every row containing Smith must be
      updated — the same update-anomaly shape 3NF was supposed to eliminate (Q2), just one
      that happens to slip past 3NF's specific prime-attribute exception.
    - **Fix — decompose into BCNF:**
      ```sql
      CREATE TABLE InstructorSubject (
          Instructor VARCHAR(50) PRIMARY KEY,
          Subject VARCHAR(50)
      );
      CREATE TABLE StudentInstructor (
          Student VARCHAR(50),
          Instructor VARCHAR(50) REFERENCES InstructorSubject(Instructor),
          PRIMARY KEY (Student, Instructor)
      );
      ```
      Now Smith's subject is stored exactly once. **Why this is usually not pursued in
      practice:** the decomposition is lossless but not always dependency-preserving —
      the original rule "(Student, Subject) determines exactly one Instructor" can no
      longer be checked as a simple key constraint on either table alone; verifying it
      requires reconstructing the join. This loss of easily-enforceable constraints is
      exactly why BCNF is treated as an academic/interview-trivia refinement rather than a
      practical target — real schemas almost always stop at 3NF (Q7) and accept this
      narrow class of anomaly as a known, rare tradeoff.

15. ***A product catalog needs to store wildly different attributes per category (books have author/ISBN/pages; electronics have voltage/warranty; clothing has size/color). How do you normalize this without either an unmaintainable wide table or a schema migration for every new category?***
    - Strict normalization pulls in two directions here, and neither pure option is great:
      a single wide `Products` table with a nullable column per possible attribute across
      *all* categories is sparse, requires an `ALTER TABLE` for every new category, and
      can't express "Books must have an author" as a real constraint; one table per
      category is clean per-category but makes any cross-category query ("list all
      products under $20") an awkward `UNION` across many tables.
    - **Realistic options, in order of how "normalized" they are:**
      1. **Class-table inheritance:** a shared `Products(id, name, price, category)` base
         table plus one side table per category (`BookDetails(product_id, author, isbn,
         pages)`, `ElectronicsDetails(product_id, voltage, warranty_months)`), joined by
         `product_id`. Fully normalized, each side table only has relevant columns and can
         enforce real constraints (`NOT NULL author`), but a generic "list all products
         with some attribute over X" query needs conditional joins per category, and a
         brand-new category still needs a new table (though not a migration of existing
         data).
      2. **EAV (Entity-Attribute-Value):** a generic
         `ProductAttributes(product_id, attribute_name, attribute_value)` table. Maximally
         flexible — a new attribute or category needs zero schema change — but loses type
         safety (`attribute_value` is usually text, so numeric filtering/sorting needs
         casts), loses the ability to put a normal index or constraint on a specific
         attribute, and "show me all of a product's attributes as one row" requires
         pivoting many rows into columns, which is awkward in plain SQL.
      3. **Fixed relational columns + a JSON/JSONB column for the variable part:** keep
         universal columns (`id, name, price, category`) relational, and put
         category-specific attributes in one `attributes JSONB` column. Similar
         flexibility to EAV without splitting one product's attributes across many rows;
         modern engines (PostgreSQL `JSONB`, MySQL `JSON`) support indexing into a
         specific key (e.g. a Postgres GIN index on `attributes`) when a particular
         attribute needs efficient filtering, without requiring one for every possible key
         up front.
    - **Decision framework:** weigh how often you need to *query/filter/sort by* a
      specific category-specific attribute across the catalog (favors option 1, or
      option 3 with a targeted index on that one key) against how often new
      attributes/categories are added and how costly a schema migration is for your
      release process (favors option 2 or 3). In practice, option 3 (relational core +
      JSONB extension) is the common pragmatic middle ground in modern Postgres/MySQL
      catalogs — it avoids full EAV's query and type-safety pain while not requiring a
      migration for every new product category.

16. ***A normalized `Customers` table is written to by very different traffic patterns — `last_login_at` updates on every login (extremely high frequency), `name`/`email`/`address` change rarely, and a batch job periodically bulk-updates `loyalty_points`. How would you decide whether to split this table, and what's the tradeoff?***
    - The problem isn't normalization in the classical (redundancy) sense — it's that one
      physical table is bundling columns with very different write frequencies and write
      sources. Every index on *any* column of this table pays a write cost (per the
      indexing note's index-tradeoff discussion) on *every* one of these updates,
      including the high-frequency `last_login_at` ones, and frequent small writes to the
      same row/page can contend with concurrent writes from the unrelated batch job.
    - **Decision approach — verify before restructuring, don't do this preemptively:**
      measure whether the high-frequency column's updates are actually contending with
      (blocking, or meaningfully adding index-maintenance overhead to) the low-frequency
      columns' updates in practice — e.g. via lock-wait statistics and index write-overhead
      metrics — rather than assuming a mismatch in update *frequency* alone implies a real
      performance problem.
    - **Fix, if warranted — vertical partitioning:** split the frequently-and-independently
      updated columns into their own table keyed by the same id, e.g.
      `CustomerActivity(customer_id, last_login_at)` separate from
      `CustomerProfile(customer_id, name, email, address, loyalty_points)`. A login-timestamp
      update now only touches the small, narrow `CustomerActivity` row — fewer indexes to
      maintain per write, and it no longer contends with unrelated profile/loyalty writes
      on the same physical rows.
    - **Tradeoff:** any query that needs both profile and activity data together now needs
      a join — the same read-cost-vs-write-cost tradeoff as normalization/denormalization
      in general (Q10-Q12), just applied along a "write frequency" axis instead of a
      "logical entity" axis. Only worth doing where the measured contention/overhead is
      real; splitting a table preemptively "because the columns feel different" adds join
      complexity for no measured benefit.

17. ***How would you migrate a live production table from a denormalized shape to a normalized schema with zero downtime, without a single risky cutover?***
    - The core principle: never make the schema change and the traffic cutover the same
      event — separate "the new structure exists and is being kept correct" from "readers
      now depend on it" into distinct, individually-reversible steps.
      1. **Design and create the new normalized tables** (e.g. extract a `Customers`
         table out of a legacy flat `Orders` table) without touching or removing anything
         on the existing `Orders` table — nothing that currently reads `Orders` is
         affected yet.
      2. **Backfill:** a batched, throttled script reads existing `Orders` rows and
         populates `Customers` with deduplicated data, handling the fact that the same
         customer appears on many order rows (e.g. `INSERT ... ON CONFLICT DO NOTHING`
         keyed by a natural or newly-generated customer identifier) — run in small batches
         to avoid long locks or replication-lag spikes on a live table.
      3. **Dual-write:** change the application's write path so every new/updated order
         also writes to `Customers`, while *still* writing the old flat columns on
         `Orders` too — for a transition window, both shapes are kept in sync, so nothing
         reading the old shape breaks and nothing has cut over to the new shape yet either.
      4. **Verify:** run a reconciliation job comparing the old denormalized columns
         against the new normalized tables (a full comparison, or a statistically
         significant sample) and fix any drift before trusting the new tables as a source
         of truth.
      5. **Cut over reads incrementally:** migrate one reader (one query, one service) at
         a time from the old flat columns to the new normalized tables, monitoring for
         regressions after each move — rather than flipping every reader simultaneously,
         so a single problematic reader is easy to identify and roll back independently.
      6. **Retire the old columns last, as its own separate step:** only once every known
         reader has moved off the old columns do you stop dual-writing, and only after
         that (with a further observation window) do you drop the now-redundant columns
         from `Orders` — bundling this with the cutover removes your ability to roll back
         cheaply if a missed reader turns up later.
    - Throughout, prefer non-locking schema operations where the engine supports them —
      e.g. PostgreSQL's `ALTER TABLE ... ADD CONSTRAINT ... NOT VALID` followed by a
      separate `VALIDATE CONSTRAINT`, so that adding a new foreign key doesn't take a
      long table-wide lock scanning every existing row as part of the same statement that
      also changes the schema.

## Related Notes in This Repo
- [`06_Transactions_ACID_And_Isolation_Levels.md`](06_Transactions_ACID_And_Isolation_Levels.md)
  — the concurrency/consistency side of relational databases.
- [`Common_SQL_Query_Problems.md`](Common_SQL_Query_Problems.md) — classic
  write-this-query interview problems.
