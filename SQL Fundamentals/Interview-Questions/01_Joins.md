# SQL Joins Interview Questions

*Uses two small sample tables throughout — `employees` and `departments` — so every join
type can be shown against the same concrete data instead of described abstractly.*

**`employees`**

| emp_id | name  | dept_id | salary | manager_id |
|--------|-------|---------|--------|------------|
| 1      | Alice | 10      | 90000  | NULL       |
| 2      | Bob   | 10      | 95000  | 1          |
| 3      | Carol | 20      | 85000  | 1          |
| 4      | Dave  | 20      | 60000  | 3          |
| 5      | Eve   | NULL    | 75000  | 1          |

**`departments`**

| dept_id | dept_name   |
|---------|-------------|
| 10      | Engineering |
| 20      | Sales       |
| 30      | Marketing   |

Note the deliberate mismatches: `Eve` has `dept_id = NULL` (no department), and department
`30` (Marketing) has no employees. These are what make the OUTER join examples below
non-trivial — an example where every row matches on both sides can't actually show what
makes each join different.

## Easy

1. ***What does an `INNER JOIN` return?***
    - Only rows where the join condition matches on **both** sides. Any row from either
      table with no matching counterpart is dropped entirely.
    ```sql
    SELECT e.name, d.dept_name
    FROM employees e
    INNER JOIN departments d ON e.dept_id = d.dept_id;
    ```
    - Result:

      | name  | dept_name   |
      |-------|-------------|
      | Alice | Engineering |
      | Bob   | Engineering |
      | Carol | Sales       |
      | Dave  | Sales       |
    - Eve is dropped (her `dept_id` is `NULL`, matches nothing), and Marketing is dropped
      (no employee has `dept_id = 30`). This is the key intuition for INNER JOIN: it's a
      *set intersection* on the join key, not a "combine everything" operation.

2. ***What does a `LEFT [OUTER] JOIN` return?***
    - Every row from the left table, plus matching right-table columns where a match
      exists — and `NULL` for every right-table column where it doesn't. The left table is
      never allowed to lose rows.
    ```sql
    SELECT e.name, d.dept_name
    FROM employees e
    LEFT JOIN departments d ON e.dept_id = d.dept_id;
    ```
    - Result:

      | name  | dept_name   |
      |-------|-------------|
      | Alice | Engineering |
      | Bob   | Engineering |
      | Carol | Sales       |
      | Dave  | Sales       |
      | Eve   | NULL        |
    - All 5 employees survive; Eve gets a `NULL` `dept_name` because nothing matched.
      Marketing still doesn't appear — LEFT JOIN protects rows on the left, not the right.

3. ***What does a `RIGHT [OUTER] JOIN` return?***
    - The mirror image of LEFT JOIN: every row from the right table, with `NULL`s filled in
      for left-table columns where nothing matches. Rarely used in practice because you can
      always rewrite a RIGHT JOIN as a LEFT JOIN by swapping the table order — most style
      guides prefer doing that for readability (so a reader never has to hold "which side is
      protected" in their head while scanning `FROM ... RIGHT JOIN ...`).
    ```sql
    SELECT e.name, d.dept_name
    FROM employees e
    RIGHT JOIN departments d ON e.dept_id = d.dept_id;
    ```
    - Result:

      | name  | dept_name   |
      |-------|-------------|
      | Alice | Engineering |
      | Bob   | Engineering |
      | Carol | Sales       |
      | Dave  | Sales       |
      | NULL  | Marketing   |
    - All 3 departments survive, including Marketing (which has no employees, so `name` is
      `NULL`). Eve is dropped — RIGHT JOIN protects the right table, not the left.

4. ***What does a `FULL [OUTER] JOIN` return?***
    - The union of LEFT and RIGHT JOIN: every row from both tables, with `NULL`s filled in on
      whichever side didn't match. Nothing from either table is ever dropped.
    ```sql
    SELECT e.name, d.dept_name
    FROM employees e
    FULL OUTER JOIN departments d ON e.dept_id = d.dept_id;
    ```
    - Result:

      | name  | dept_name   |
      |-------|-------------|
      | Alice | Engineering |
      | Bob   | Engineering |
      | Carol | Sales       |
      | Dave  | Sales       |
      | Eve   | NULL        |
      | NULL  | Marketing   |
    - Note: MySQL has no native `FULL OUTER JOIN`. It's emulated with
      `LEFT JOIN ... UNION ... RIGHT JOIN` (or `LEFT JOIN UNION SELECT ... FROM b LEFT JOIN a
      ... WHERE a.key IS NULL`, to avoid duplicating the matched rows). PostgreSQL and SQL
      Server support `FULL OUTER JOIN` natively.
    - Why `UNION` is the right tool here: it stacks rows, it doesn't add columns. The
      emulation works only because the `LEFT JOIN` and `RIGHT JOIN` queries both `SELECT`
      the exact same columns — `UNION` just concatenates their row sets into one (and
      de-duplicates; use `UNION ALL` if you specifically don't want that). It's a common
      mix-up to reach for `UNION` when what's actually wanted is two values *side by side
      in one row* (e.g. a male count and a female count as two columns) — that needs
      conditional aggregation instead (`COUNT(CASE WHEN ...)`), not `UNION`; see the Pivot
      question in `04_Common_SQL_Query_Problems.md`.

5. ***What does a `CROSS JOIN` return?***
    - The Cartesian product: every row of the left table paired with every row of the right
      table, with **no join condition at all**. `5 employees × 3 departments = 15 rows`.
    ```sql
    SELECT e.name, d.dept_name
    FROM employees e
    CROSS JOIN departments d;
    -- 15 rows: Alice-Engineering, Alice-Sales, Alice-Marketing, Bob-Engineering, ...
    ```
    - Real uses: generating all combinations (e.g. every product x every size/color
      variant), or cross-joining against a small "calendar"/"numbers" table to generate a
      date series. Writing `FROM a, b` with no `WHERE` clause is an implicit CROSS JOIN — a
      classic accidental-Cartesian-product bug when a `WHERE`/`ON` condition is forgotten.

6. ***What does a `SELF JOIN` return? (see also Q11 for the canonical use case)***
    - Not a distinct join *type* — it's any join (usually INNER or LEFT) where a table is
      joined to itself, using table aliases to distinguish the two "roles" a row can play.
      Used whenever a table has a column that references another row in the *same* table
      (e.g. `manager_id` referencing another `emp_id`). See the manager/report example in
      Q11 below.

7. ***What is a self join used for? Give a concrete example.***
    - Any time a table has a column referencing another row of the *same* table — an
      org chart (`manager_id` -> another `emp_id`), a "friends of friends" graph, a
      "previous version of this row" chain, a bill-of-materials/parts-within-parts
      hierarchy. You alias the table twice to let each alias play a different "role" in the
      relationship.

## Medium

8. ***What's the difference between filtering in `ON` vs `WHERE` for a LEFT JOIN? Why does
   it matter?***
    - `ON` is evaluated **while the join is being formed** — it decides which rows are
      allowed to match. Rows on the protected side (left, for a LEFT JOIN) that fail the
      `ON` condition still appear in the result, just with `NULL`s for the other side's
      columns, because LEFT JOIN's guarantee ("keep every left row") applies regardless of
      the `ON` condition's outcome.
    - `WHERE` is evaluated **after** the join has already been produced (see the logical
      execution order in `02_Query_Execution_Order_GroupBy_Having.md`). By the time `WHERE`
      runs, the unmatched left rows already have `NULL` right-table columns. If the `WHERE`
      clause filters on a right-table column with a condition that `NULL` can never satisfy
      (e.g. `d.dept_name = 'Sales'`, or worse, anything that isn't an explicit
      `IS NULL`/`IS NOT NULL` check), those unmatched rows get **filtered out** — which
      silently converts the LEFT JOIN into the equivalent of an INNER JOIN. This is one of
      the most common outer-join bugs in real code: the query still "runs," returns
      plausible-looking rows, and the bug is a silent behavior change, not an error.
    - Concrete proof using the sample data — intent: "show every employee, and if their
      department is Engineering, show it; otherwise leave it blank" (i.e. the LEFT JOIN
      should still protect every employee row):
    ```sql
    -- (A) Filter in ON — preserves LEFT JOIN semantics, all 5 employees still appear:
    SELECT e.name, d.dept_name
    FROM employees e
    LEFT JOIN departments d ON e.dept_id = d.dept_id AND d.dept_name = 'Engineering';
    -- Alice, Engineering | Bob, Engineering | Carol, NULL | Dave, NULL | Eve, NULL
    -- (all 5 employees present; only Engineering rows got real dept_name, rest are NULL)

    -- (B) Filter in WHERE — silently degrades to an INNER JOIN, employees vanish:
    SELECT e.name, d.dept_name
    FROM employees e
    LEFT JOIN departments d ON e.dept_id = d.dept_id
    WHERE d.dept_name = 'Engineering';
    -- Alice, Engineering | Bob, Engineering
    -- (Carol, Dave, Eve are gone — WHERE discarded every row where d.dept_name wasn't
    --  exactly 'Engineering', and NULL is never equal to 'Engineering', so all the
    --  unmatched left rows got silently dropped too)
    ```
    - The takeaway asked in interviews: **conditions that restrict which rows are allowed
      to join belong in `ON`; conditions that filter the final result set belong in
      `WHERE`** — and for an outer join, putting a condition on the *nullable* side's
      columns into `WHERE` is almost always a bug unless you explicitly also handle the
      `NULL` case.

9. ***How do you find all departments with no employees?***
    - Pattern 1 — `LEFT JOIN ... WHERE right.key IS NULL` (the classic "anti-join"): join
      everything, keep only the rows where the right side failed to match at all.
    ```sql
    SELECT d.dept_name
    FROM departments d
    LEFT JOIN employees e ON d.dept_id = e.dept_id
    WHERE e.emp_id IS NULL;
    -- Marketing
    ```
    - Why `e.emp_id IS NULL` and not `e.dept_id IS NULL`: either works here since
      `emp_id` is never null, but `emp_id` is idiomatic because it's the row-identity
      column — checking a column that's guaranteed non-null when a match *did* occur
      makes the "no match happened" intent unambiguous, whereas checking the join column
      itself can be misread as "the join column happened to be null," which is a different
      condition (see Q10).
    - Pattern 2 — `NOT EXISTS` (a correlated subquery): usually the query planner's
      preferred/most efficient form, and arguably clearer intent ("a row for which no
      matching employee exists").
    ```sql
    SELECT d.dept_name
    FROM departments d
    WHERE NOT EXISTS (
        SELECT 1 FROM employees e WHERE e.dept_id = d.dept_id
    );
    -- Marketing
    ```

10. ***Why is `NOT IN` a footgun for this same query, compared to `NOT EXISTS`?***
    - The tempting-looking third version:
    ```sql
    -- DANGEROUS if employees.dept_id can contain NULL
    SELECT d.dept_name
    FROM departments d
    WHERE d.dept_id NOT IN (SELECT dept_id FROM employees);
    ```
    - `IN`/`NOT IN` against a subquery desugars to a chain of `OR`/`AND` equality checks:
      `dept_id NOT IN (10, 20, NULL)` is really
      `dept_id <> 10 AND dept_id <> 20 AND dept_id <> NULL`. SQL's three-valued logic means
      `dept_id <> NULL` never evaluates to `TRUE` — it evaluates to `UNKNOWN`, always
      (comparing anything to `NULL` with `=`/`<>` yields `UNKNOWN`, not `TRUE` or `FALSE`).
      Since `AND` with any `UNKNOWN` operand can never produce an overall `TRUE`, **the
      entire `NOT IN` expression evaluates to `UNKNOWN`/not-true for every single row**, the
      whole time the subquery's result set contains even one `NULL`. The query silently
      returns **zero rows** instead of the correct answer — and in this dataset, Eve's
      `dept_id` is exactly that `NULL`, so `NOT IN` here would (wrongly) return nothing at
      all instead of `Marketing`.
    - `NOT EXISTS` and the `LEFT JOIN ... IS NULL` pattern don't have this problem because
      neither relies on comparing a value against a set that might contain `NULL` — they
      test row existence/match directly, which is well-defined regardless of `NULL`s
      elsewhere in the joined column.
    - Practical rule: default to `NOT EXISTS` for anti-joins; only use `NOT IN` against a
      subquery when you can guarantee that column is `NOT NULL` (e.g. it's a primary key,
      or the subquery explicitly adds `WHERE dept_id IS NOT NULL`).

11. ***Find every employee who earns more than their manager.***
    - Using the `employees` sample table, `manager_id` self-references `emp_id`. Alias the
      table once as `e` (the employee) and once as `m` (their manager), joining
      `e.manager_id = m.emp_id`:
    ```sql
    SELECT e.name AS employee, e.salary AS employee_salary,
           m.name AS manager, m.salary AS manager_salary
    FROM employees e
    INNER JOIN employees m ON e.manager_id = m.emp_id
    WHERE e.salary > m.salary;
    ```
    - Tracing it against the sample data: `manager_id` pairs are Bob->Alice(1),
      Carol->Alice(1), Dave->Carol(3), Eve->Alice(1). Comparing salaries: Bob(95000) vs
      Alice(90000) — **yes, 95000 > 90000**; Carol(85000) vs Alice(90000) — no; Dave(60000)
      vs Carol(85000) — no; Eve(75000) vs Alice(90000) — no. So only one row qualifies:

      | employee | employee_salary | manager | manager_salary |
      |----------|------------------|---------|-----------------|
      | Bob      | 95000            | Alice   | 90000           |
    - Note `INNER JOIN` here deliberately excludes Alice, who has `manager_id = NULL` (the
      top of the hierarchy, no manager to compare against) — a `NULL` `manager_id` matches
      nothing, so Alice is correctly dropped from a "vs their manager" comparison without
      needing an explicit filter for it.

## Hard

12. ***A query joins one "one" table to two different "many" tables in the same `SELECT`
    (e.g. an employee joined to both their orders and their certifications) and an
    aggregate like `SUM()` comes back way too large. Walk through why, and how you'd fix
    it.***
    - This is **row fan-out**: when a single row on the "one" side is joined independently
      to two separate one-to-many relationships in the *same* query, the engine produces
      the **Cartesian product of the two "many" sides** for that row, before any
      aggregation happens. If a row has 2 matching orders and 3 matching certifications,
      the joined result has `2 x 3 = 6` rows for it — each order amount duplicated once per
      certification, and vice versa.
    - Sample data:

      **`orders`** (Alice only, Bob has none)

      | order_id | emp_id | amount |
      |----------|--------|--------|
      | 101      | 1      | 100    |
      | 102      | 1      | 200    |

      **`certifications`** (Alice only, Bob has none)

      | cert_id | emp_id | cert_name |
      |---------|--------|-----------|
      | 901     | 1      | AWS       |
      | 902     | 1      | PMP       |
      | 903     | 1      | Scrum     |
    ```sql
    -- BUGGY: joins orders and certifications independently off the same employee row
    SELECT e.name,
           SUM(o.amount)              AS total_amount,
           COUNT(DISTINCT c.cert_id)  AS total_certs
    FROM employees e
    LEFT JOIN orders o ON o.emp_id = e.emp_id
    LEFT JOIN certifications c ON c.emp_id = e.emp_id
    WHERE e.emp_id IN (1, 2)
    GROUP BY e.name;
    ```
    - Trace: before `GROUP BY`, Alice's 2 orders x 3 certs produce 6 intermediate rows —
      `(100,AWS) (100,PMP) (100,Scrum) (200,AWS) (200,PMP) (200,Scrum)`. `SUM(o.amount)`
      over those 6 rows adds `100` three times and `200` three times:
      `(100+200) x 3 = 900` — not the true total of `300`. `COUNT(DISTINCT c.cert_id)`
      happens to come out **correct** (`3`) only because `DISTINCT` collapses the
      duplicated cert IDs — which is exactly what makes this bug dangerous: one column in
      the output is silently wrong while a neighboring column looks fine, so a quick eyeball
      check of the results doesn't catch it. Bob has no orders/certs, so his single `LEFT
      JOIN`-preserved row gives `total_amount = NULL`, `total_certs = 0`.

      | name  | total_amount | total_certs |
      |-------|---------------|--------------|
      | Alice | 900           | 3            |
      | Bob   | NULL          | 0            |
    - Fix: aggregate each one-to-many relationship **independently in its own subquery
      first**, so no cross product is ever formed, then join the already-collapsed
      (one-row-per-employee) results together:
    ```sql
    SELECT e.name,
           o_agg.total_amount,
           COALESCE(c_agg.total_certs, 0) AS total_certs
    FROM employees e
    LEFT JOIN (
        SELECT emp_id, SUM(amount) AS total_amount
        FROM orders GROUP BY emp_id
    ) o_agg ON o_agg.emp_id = e.emp_id
    LEFT JOIN (
        SELECT emp_id, COUNT(*) AS total_certs
        FROM certifications GROUP BY emp_id
    ) c_agg ON c_agg.emp_id = e.emp_id
    WHERE e.emp_id IN (1, 2);
    ```
    - Trace: `o_agg` has one row, `(emp_id=1, total_amount=300)`; `c_agg` has one row,
      `(emp_id=1, total_certs=3)`. Joining those (already one-row-per-employee) results to
      `employees` can't fan out further.

      | name  | total_amount | total_certs |
      |-------|---------------|--------------|
      | Alice | 300           | 3            |
      | Bob   | NULL          | 0            |
    - General rule: the moment a query joins a "one" row to **two or more** independent
      "many" relationships and then aggregates, pre-aggregate each "many" side separately
      (subquery/CTE) before combining — never aggregate across a multi-way fan-out directly.

13. ***In a chain of joins, a `LEFT JOIN` is followed later by a plain `JOIN` — and rows the
    `LEFT JOIN` was supposed to protect disappear anyway. Why, and how do you fix it?***
    - `JOIN` (with no qualifier) means `INNER JOIN`. When it appears *after* a `LEFT JOIN`
      in the same chain, it re-filters the already-produced result set: any row that the
      `LEFT JOIN` kept alive with `NULL`s (because it had no match) will now fail the
      later `INNER JOIN`'s condition — `NULL` matches nothing — and gets silently dropped.
      The `LEFT JOIN`'s protection is real for exactly one step, and undone by the very
      next `INNER JOIN`.
    - Sample data — reusing `employees`/`departments`, plus:

      **`dept_projects`** (only Engineering has an active project)

      | dept_id | project_name       |
      |---------|---------------------|
      | 10      | Migrate to Cloud    |
    ```sql
    -- BUGGY: intent is "every employee, their dept if any, and any active project" —
    -- but the second JOIN is INNER
    SELECT e.name, d.dept_name, p.project_name
    FROM employees e
    LEFT JOIN departments d ON e.dept_id = d.dept_id
    JOIN dept_projects p ON d.dept_id = p.dept_id;
    ```
    - Trace: after the `LEFT JOIN`, all 5 employees are present (Alice/Bob ->
      Engineering, Carol/Dave -> Sales, Eve -> `NULL`). The `INNER JOIN` to
      `dept_projects` only has a row for `dept_id = 10`, so it only matches Alice and
      Bob's `d.dept_id = 10`. Carol/Dave (`dept_id = 20`, no project row) and Eve
      (`dept_id = NULL`, matches nothing) all get silently eliminated by the second join —
      even though the query *looks* like it should keep every employee, because the first
      join was `LEFT`.

      | name  | dept_name   | project_name      |
      |-------|-------------|--------------------|
      | Alice | Engineering | Migrate to Cloud   |
      | Bob   | Engineering | Migrate to Cloud   |
    - Fix: make every join in the chain that's meant to be "optional" a `LEFT JOIN`,
      including the later ones — protection has to be maintained at *every* step, not just
      the first:
    ```sql
    SELECT e.name, d.dept_name, p.project_name
    FROM employees e
    LEFT JOIN departments d ON e.dept_id = d.dept_id
    LEFT JOIN dept_projects p ON d.dept_id = p.dept_id;
    ```
    - Trace: now Carol/Dave get `project_name = NULL` instead of being dropped, and Eve
      gets both `dept_name` and `project_name` as `NULL` — all 5 employees are present,
      matching the original intent.

      | name  | dept_name   | project_name      |
      |-------|-------------|--------------------|
      | Alice | Engineering | Migrate to Cloud   |
      | Bob   | Engineering | Migrate to Cloud   |
      | Carol | Sales       | NULL               |
      | Dave  | Sales       | NULL               |
      | Eve   | NULL        | NULL               |
    - This is the same underlying rule as the `ON` vs `WHERE` gotcha (Q8) — an `INNER JOIN`
      is just a filter, wherever it appears in the chain — but here the trap is placement
      within a multi-join chain rather than clause choice within a single join.

14. ***A single self join only finds an employee's *direct* reports. How would you find
    every employee under a given manager at any depth — and why doesn't a bigger self join
    fix it?***
    - A self join (`e JOIN e2 ON e.manager_id = e2.emp_id`) is fundamentally a **fixed,
      one-hop** join — it can only walk the `manager_id -> emp_id` edge exactly once per
      join written in the query. Stacking a fixed number of self joins finds a fixed
      number of levels (two self joins = 2 levels down), but an org chart's depth is
      arbitrary and data-dependent, so no fixed number of self joins is correct in
      general — you'd need to know the maximum depth in advance and write that many joins.
      The right tool is a **recursive CTE**, which repeats the same join step until no new
      rows are produced.
    - Sample data — `employees` again: Alice(1, no manager), Bob(2, mgr 1), Carol(3, mgr
      1), Dave(4, mgr 3), Eve(5, mgr 1). Note Dave reports to Carol, who reports to Alice —
      Dave is *two* levels under Alice, not a direct report.
    ```sql
    -- Only finds DIRECT reports of Alice (emp_id 1) — misses Dave entirely
    SELECT e.name
    FROM employees e
    WHERE e.manager_id = 1;
    -- Bob, Carol, Eve  (Dave is missing — he reports to Carol, not Alice)
    ```
    ```sql
    -- Recursive CTE: walks the hierarchy to whatever depth actually exists
    WITH RECURSIVE org_chart AS (
        SELECT emp_id, name, manager_id, 1 AS depth
        FROM employees
        WHERE manager_id = 1                       -- anchor: Alice's direct reports
        UNION ALL
        SELECT e.emp_id, e.name, e.manager_id, oc.depth + 1
        FROM employees e
        INNER JOIN org_chart oc ON e.manager_id = oc.emp_id  -- recursive step
    )
    SELECT * FROM org_chart;
    ```
    - Trace: anchor member — employees with `manager_id = 1`: Bob, Carol, Eve, all
      `depth = 1`. Recursive step, round 1 — employees whose `manager_id` is one of
      `{2, 3, 5}` (the emp_ids just produced): Dave has `manager_id = 3` (Carol) -> matches,
      `depth = 2`. Round 2 — employees whose `manager_id` is `4` (Dave): none exist, so the
      recursion terminates.

      | emp_id | name  | manager_id | depth |
      |--------|-------|------------|-------|
      | 2      | Bob   | 1          | 1     |
      | 3      | Carol | 1          | 1     |
      | 5      | Eve   | 1          | 1     |
      | 4      | Dave  | 3          | 2     |
    - All 4 of Alice's transitive reports are found, including Dave at depth 2, which no
      single self join could have produced without also over-fitting to "exactly 2 levels"
      as a hardcoded assumption.

15. ***When would you rewrite a `JOIN` as a correlated subquery, or vice versa — and what's
    the actual performance reasoning, not just style preference?***
    - Sample data — `orders` with dates:

      | order_id | emp_id | amount | order_date |
      |----------|--------|--------|------------|
      | 101      | 1      | 100    | 2024-01-05 |
      | 102      | 1      | 200    | 2024-02-10 |
    - Goal: "each employee's most recent order amount." Version A — correlated subquery:
    ```sql
    SELECT e.name,
           (SELECT o.amount FROM orders o
            WHERE o.emp_id = e.emp_id
            ORDER BY o.order_date DESC LIMIT 1) AS latest_order_amount
    FROM employees e;
    ```
    - Version B — window function in a derived table, joined:
    ```sql
    SELECT e.name, o.amount AS latest_order_amount
    FROM employees e
    LEFT JOIN (
        SELECT emp_id, amount,
               ROW_NUMBER() OVER (PARTITION BY emp_id ORDER BY order_date DESC) AS rn
        FROM orders
    ) o ON o.emp_id = e.emp_id AND o.rn = 1;
    ```
    - Trace: for Alice, the latest order by date is `102` (`2024-02-10`, amount `200`),
      not `101`. Both versions correctly return `Alice -> 200`; for Bob (no orders), both
      correctly return `Bob -> NULL`.
    - The real performance difference: Version A's subquery is **re-evaluated once per
      outer row** (once per employee here). That's fine, even fast, when the outer table is
      small and the inner table has an index on `(emp_id, order_date)` — each execution is
      a cheap indexed seek. Version B computes the ranking **once, in a single pass** over
      the whole `orders` table (one sort/partition operation), then does one join — better
      when the outer table is large, because the per-row cost of re-seeking N times can
      exceed the cost of one bulk sort. So: correlated subquery favors "few outer rows,
      well-indexed inner lookup"; window-function/join favors "need this for most/all rows
      anyway, so do the whole table once."
    - The other direction matters too: `EXISTS`/`NOT EXISTS` correlated subqueries are
      often **better** than `JOIN ... GROUP BY`/`DISTINCT` for anti-join or existence
      checks, because the planner can treat them as a semi-join/anti-join and stop at the
      first match per outer row, without ever materializing or de-duplicating the full set
      of matching rows the way a `JOIN` followed by `DISTINCT` would have to.

16. ***Does the order you write `JOIN`s in matters for performance? What actually decides
    join order and join algorithm (nested loop / hash / merge)?***
    - In modern cost-based optimizers (PostgreSQL, SQL Server, MySQL 8+), the *textual*
      order of `JOIN` clauses generally does **not** dictate execution order — the planner
      uses table statistics (row counts, cardinality/selectivity estimates, available
      indexes) to choose both the join order and the join algorithm, and is free to
      reorder joins as long as the result is identical to the query as written (the same
      "logical vs physical execution" distinction that governs `WHERE`/`GROUP BY`/`HAVING`
      ordering).
    - What picks the algorithm: **nested loop join** — for each row of the (smaller, or
      more selectively filtered) outer table, probe the inner table via an index; good
      when one side is small or heavily filtered and the join column is indexed on the
      other side. **Hash join** — build an in-memory hash table on the smaller side's join
      key, then stream the larger side through it; good for large, roughly
      similarly-sized tables on an equality join with no useful index. **Merge join** —
      if both sides are already sorted (or cheaply sortable, e.g. via an index) on the
      join key, walk both sorted streams in lockstep; good when the sort order is already
      available "for free."
    - Concretely: joining a 10M-row `orders` table to a 500-row `customers` table on
      `customer_id`, with a B-tree index on `orders.customer_id` — a cost-based optimizer
      would typically pick `customers` as the outer/driving side (few rows to loop over)
      and do a nested-loop indexed lookup into `orders` per customer, rather than hashing
      the 10M-row table. This is a planner decision, not something the SQL text controls
      directly.
    - When *does* written order matter: engines/modes with weaker optimizers (older MySQL
      versions historically leaned heavily on nested-loop joins and were more sensitive to
      the order tables were listed in), or when statistics are stale/missing (e.g. right
      after a bulk load with no `ANALYZE` run), causing the optimizer to mis-estimate
      cardinalities and pick a bad plan — in which case join hints or query rewriting
      (not just clause reordering) are the real fix, and checking `EXPLAIN`/`EXPLAIN
      ANALYZE` output is how you'd confirm what the planner actually chose versus what was
      assumed.

## Related Notes in This Repo
- [`02_Query_Execution_Order_GroupBy_Having.md`](02_Query_Execution_Order_GroupBy_Having.md) —
  the logical execution order referenced in Q8 (`ON`/`WHERE` timing) is explained in full
  there, along with `GROUP BY`/`HAVING`/aggregate gotchas.
