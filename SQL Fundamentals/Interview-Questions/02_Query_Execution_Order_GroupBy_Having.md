# SQL Query Execution Order, GROUP BY & HAVING Interview Questions

## Easy

1. ***What is the actual logical execution order of a SQL query's clauses? Why does it
   matter that this differs from the order they're written in?***
    - A query is *written* as:
    ```sql
    SELECT ... FROM ... JOIN ... WHERE ... GROUP BY ... HAVING ... ORDER BY ... LIMIT ...
    ```
    - But it is *logically executed* in this order:
    1. `FROM` — identify the base table(s).
    2. `JOIN` (+ `ON`) — combine rows from multiple tables into one working row set.
    3. `WHERE` — filter individual rows, before any grouping happens.
    4. `GROUP BY` — collapse the remaining rows into groups by the specified column(s), then
       compute every aggregate function (`COUNT`, `SUM`, `AVG`, `MIN`, `MAX`, ...) once per
       group. This aggregation happens as part of this step, immediately after grouping —
       it's *not* a separate numbered clause, but it's worth naming explicitly because every
       later step (`HAVING`, `SELECT`, `ORDER BY`) can freely reference the resulting
       aggregate values, while everything before this step (`WHERE`) cannot (Q3). If there's
       no `GROUP BY` at all but the query still has an aggregate (e.g. `SELECT COUNT(*) FROM
       employees`), the whole table is treated as a single implicit group.
    5. `HAVING` — filter entire *groups*, using the aggregate values computed in step 4.
    6. `SELECT` — compute the actual output columns/expressions (including aggregates and
       aliases).
    7. `DISTINCT` — remove duplicate rows from the `SELECT` output.
    8. `ORDER BY` — sort the final result set. Accepts multiple keys, applied left to right —
       later keys only break ties left unresolved by earlier ones, they don't re-sort
       everything:
       ```sql
       SELECT first_name FROM patients
       ORDER BY LENGTH(first_name), first_name;
       -- groups by name length first (shortest first), then alphabetizes
       -- only within each length group, e.g.: Al, Bo, Amy, Ann, Zoe
       ```
    9. `LIMIT` / `OFFSET` — trim the sorted result to a page/window.
    - This matters because SQL is a *declarative* language — you describe the result you
      want, not the steps to compute it — but the engine still has to execute the request
      as a well-defined sequence of steps internally, and that sequence is fixed and does
      **not** follow the left-to-right reading order of the clauses as written. Several
      "gotcha" rules that otherwise look arbitrary (Q2–Q3 below) fall directly out of this
      one fact: a clause can only reference things that a *prior* step in the logical order
      has already produced.
    - (Real database engines are free to physically execute/optimize a query differently —
      e.g. pushing a `WHERE` predicate down before a join, or short-circuiting a `LIMIT` —
      as long as the *result* is identical to running the logical order above. The logical
      order is a contract about correctness/scoping, not a literal execution plan.)

## Medium

2. ***Why can't you reference a `SELECT`-aliased column in a `WHERE` clause, but you can in
   `ORDER BY`?***
    - Per the order above, `WHERE` (step 3) runs *before* `SELECT` (step 6) — the alias
      simply doesn't exist yet at the point `WHERE` is evaluated, because the engine hasn't
      computed the `SELECT` list yet. `ORDER BY` (step 8) runs *after* `SELECT`, so the
      alias is already a known, computed column by the time sorting happens.
    ```sql
    -- FAILS: `high_earner` doesn't exist yet when WHERE is evaluated
    SELECT salary, (salary > 80000) AS high_earner
    FROM employees
    WHERE high_earner = true;
    -- Error: column "high_earner" does not exist
    ```
    ```sql
    -- WORKS: repeat the expression in WHERE instead of the alias
    SELECT salary, (salary > 80000) AS high_earner
    FROM employees
    WHERE (salary > 80000) = true;
    ```
    ```sql
    -- WORKS: ORDER BY runs after SELECT, so the alias is already computed
    SELECT salary, (salary > 80000) AS high_earner
    FROM employees
    ORDER BY high_earner DESC;
    ```
    - (MySQL is a notable, deliberate exception: it allows referencing a `SELECT` alias in
      `GROUP BY` and `HAVING` as a convenience extension, even though standard/ANSI SQL and
      most other engines — PostgreSQL, SQL Server — do not extend that same convenience to
      `WHERE`, since `WHERE` conceptually can't know about per-group aggregates at all,
      independent of alias timing.)

3. ***Why can't `WHERE` reference the result of an aggregate function like `COUNT()` or
   `SUM()`, but `HAVING` can?***
    - `WHERE` (step 3) runs before `GROUP BY` (step 4), which is the step that actually
      *forms* the groups an aggregate function computes over. There are no groups yet, and
      therefore nothing for `COUNT()`/`SUM()`/`AVG()` to aggregate, at the point `WHERE` is
      evaluated — it operates strictly row-by-row on the pre-grouped data. `HAVING` (step
      5) runs after both `GROUP BY` and the aggregation step, so it can freely reference
      aggregate results because they already exist by then. This is really the same
      underlying rule as Q2, applied to aggregates instead of aliases.

4. ***What's the practical difference between `WHERE` and `HAVING`? Show a query that's
   wrong because it uses `WHERE` with an aggregate, and the corrected version.***
    - `WHERE` filters individual rows *before* grouping — cheap, applied early, cannot see
      aggregate results (Q3). `HAVING` filters whole groups *after* aggregation — can
      reference `COUNT()`, `SUM()`, `AVG()`, etc., because grouping and aggregation have
      already happened by the time `HAVING` runs.
    - Sample data — an `orders` table:

      | order_id | customer_id | amount |
      |----------|-------------|--------|
      | 1        | 101         | 50     |
      | 2        | 101         | 30     |
      | 3        | 102         | 200    |
      | 4        | 103         | 10     |
      | 5        | 103         | 15     |
      | 6        | 103         | 5      |
    - Goal: "find customers whose total order amount exceeds 40."
    ```sql
    -- WRONG: SUM() doesn't exist yet when WHERE is evaluated (fails per Q3)
    SELECT customer_id, SUM(amount) AS total
    FROM orders
    WHERE SUM(amount) > 40
    GROUP BY customer_id;
    -- Error: aggregate functions are not allowed in WHERE
    ```
    ```sql
    -- CORRECT: filter the aggregated groups with HAVING instead
    SELECT customer_id, SUM(amount) AS total
    FROM orders
    GROUP BY customer_id
    HAVING SUM(amount) > 40;
    ```
    - Tracing the corrected query against the sample data: grouping by `customer_id` gives
      totals of `101 -> 80`, `102 -> 200`, `103 -> 30`. `HAVING SUM(amount) > 40` keeps only
      groups where that's true, so the result is `101, 80` and `102, 200` — `103` (total 30)
      is excluded.

      | customer_id | total |
      |-------------|-------|
      | 101         | 80    |
      | 102         | 200   |
    - `WHERE` and `HAVING` can (and often should) both appear in the same query, each doing
      its own job — `WHERE` narrows down the raw rows cheaply before the expensive grouping
      step, and `HAVING` then filters on the aggregate result:
    ```sql
    -- e.g. "total order amount per customer, counting only orders >= 10, for customers
    -- whose filtered total still exceeds 20"
    SELECT customer_id, SUM(amount) AS total
    FROM orders
    WHERE amount >= 10
    GROUP BY customer_id
    HAVING SUM(amount) > 20;
    -- Row 2 (customer 101, amount 30) survives WHERE; row 1 (amount 50) does too, giving
    -- 101 -> 80. Row 3 (customer 102, amount 200) -> 102 -> 200. For customer 103: rows 4
    -- (10) and 5 (15) survive WHERE (>= 10), row 6 (5) is filtered out by WHERE before
    -- grouping even happens; grouped total for 103 is 10 + 15 = 25, which does pass
    -- HAVING SUM(amount) > 20, so 103 IS included here (unlike the first example) because
    -- WHERE removed the low outlier (5) before the sum was computed.
    -- Result: 101 -> 80, 102 -> 200, 103 -> 25
    ```

5. ***What's the difference between `COUNT(*)` and `COUNT(column)`?***
    - `COUNT(*)` counts **every row** in the group, regardless of whether any particular
      column in that row is `NULL`. `COUNT(column)` counts only rows where that specific
      `column` is **non-`NULL`** — `NULL` values are skipped entirely (this is true of every
      aggregate function — `SUM`, `AVG`, `MAX`, `MIN` — they all silently ignore `NULL`s in
      the aggregated column, not just `COUNT`).
    - Sample data — an `employees_ext` table:

      | emp_id | name  | bonus |
      |--------|-------|-------|
      | 1      | Alice | 500   |
      | 2      | Bob   | NULL  |
      | 3      | Carol | 300   |
      | 4      | Dave  | NULL  |
    ```sql
    SELECT COUNT(*)      AS total_rows,     -- 4  (every row counted)
           COUNT(bonus)  AS rows_with_bonus -- 2  (NULL bonus rows for Bob, Dave skipped)
    FROM employees_ext;
    ```
    - A related gotcha: `COUNT(column)` and `COUNT(DISTINCT column)` are also different —
      the latter additionally collapses duplicate non-null values before counting, e.g.
      `COUNT(DISTINCT dept_id)` counts how many *distinct* non-null departments appear, not
      how many rows have a non-null `dept_id`.
    - Also worth knowing: `AVG(column)` divides by the count of **non-null** rows, not the
      total row count — `AVG(bonus)` above is `(500 + 300) / 2 = 400`, not `(500 + 300) / 4
      = 200`. This trips people up when they expect `AVG` to implicitly treat missing values
      as `0`.

6. ***Why does `GROUP BY` require every non-aggregated selected column to also appear in
   the `GROUP BY` clause?***
    - `GROUP BY` collapses many rows into one output row per group. For any column that is
      **not** wrapped in an aggregate function (`SUM`, `COUNT`, etc.) and **not** part of the
      `GROUP BY` key, the engine has no well-defined single value to return for that column
      — a group can (and usually does) contain multiple rows with different values for that
      column, so which one would `SELECT` return? Standard SQL refuses to guess, and
      requires every selected non-aggregated column to be part of the grouping key (or
      "functionally dependent" on it — see below) specifically so the query has one
      unambiguous value per group for every output column.
    ```sql
    -- FAILS in standard SQL / PostgreSQL / SQL Server:
    -- 'salary' isn't in GROUP BY and isn't wrapped in an aggregate —
    -- which employee's salary would it even return for a department with several employees?
    SELECT dept_id, salary, COUNT(*)
    FROM employees
    GROUP BY dept_id;
    -- Error: column "employees.salary" must appear in the GROUP BY clause
    --        or be used in an aggregate function
    ```
    ```sql
    -- CORRECT: either aggregate salary...
    SELECT dept_id, AVG(salary) AS avg_salary, COUNT(*) AS headcount
    FROM employees
    GROUP BY dept_id;

    -- ...or add it to the grouping key (changes the meaning: now one row per
    -- dept_id+salary combination, not one row per dept_id)
    SELECT dept_id, salary, COUNT(*)
    FROM employees
    GROUP BY dept_id, salary;
    ```
    - **The "functional dependency" exception (MySQL-specific looseness):** MySQL (5.7+,
      with `ONLY_FULL_GROUP_BY` — which is the default since 5.7.5) permits a non-aggregated
      column in `SELECT` if it is *functionally dependent* on the `GROUP BY` columns — most
      commonly, if you group by a table's primary key, every other column in that table is
      guaranteed to have exactly one value per group (since the primary key already
      uniquely identifies the row), so MySQL allows selecting them without an aggregate or
      without listing them all in `GROUP BY`:
    ```sql
    -- MySQL: allowed, because emp_id is the primary key of employees — grouping by it
    -- already guarantees `name` and `salary` have exactly one value per group.
    SELECT emp_id, name, salary, COUNT(*)
    FROM employees
    GROUP BY emp_id;
    ```
    - Relying on this is a portability trap: the same query throws an error on PostgreSQL or
      SQL Server (neither performs this functional-dependency analysis for arbitrary
      queries), and even in MySQL it silently produces **unspecified** (not necessarily
      wrong, just engine-chosen and non-portable) results if `ONLY_FULL_GROUP_BY` happens to
      be disabled and the dependency doesn't actually hold. The safe, portable habit is to
      always aggregate or explicitly group by every selected column.

## Hard

7. ***A report needs "total revenue per region, but only regions with more than 2
   non-refunded orders, counting only non-refunded orders." Walk through structuring this
   query, and explain why clause order/placement matters for getting it right — including a
   plausible-looking wrong version.***
    - Sample data — `regional_orders`:

      | order_id | region | amount | is_refunded |
      |----------|--------|--------|-------------|
      | 1        | East   | 100    | false       |
      | 2        | East   | 150    | false       |
      | 3        | East   | 200    | true        |
      | 4        | West   | 300    | false       |
      | 5        | West   | 50     | true        |
      | 6        | North  | 500    | false       |
      | 7        | North  | 400    | false       |
      | 8        | North  | 100    | false       |
      | 9        | North  | 75     | true        |
    - There are two independent conditions here at *different grains*: "non-refunded"
      is a per-**row** condition (belongs in `WHERE`, evaluated before grouping, so
      refunded orders never contribute to the sum or the count at all), and "more than 2
      orders" is a per-**group** condition on an aggregate (belongs in `HAVING`, evaluated
      after grouping). Getting the grain right — not just "where is this syntactically
      legal" — is the actual skill being tested.
    ```sql
    -- CORRECT
    SELECT region, SUM(amount) AS total_revenue, COUNT(*) AS non_refunded_orders
    FROM regional_orders
    WHERE is_refunded = false
    GROUP BY region
    HAVING COUNT(*) > 2;
    ```
    - Trace: `WHERE is_refunded = false` first drops rows 3, 5, 9 (the refunded ones),
      leaving rows 1, 2, 4, 6, 7, 8. Grouping what's left: East = rows 1,2 ->
      `sum=250, count=2`; West = row 4 -> `sum=300, count=1`; North = rows 6,7,8 ->
      `sum=1000, count=3`. `HAVING COUNT(*) > 2` keeps only North.

      | region | total_revenue | non_refunded_orders |
      |--------|----------------|-----------------------|
      | North  | 1000           | 3                     |
    - Plausible wrong version: someone tries to fold *both* conditions into `HAVING`
      instead, reasoning "HAVING can filter on aggregates, so I can just aggregate the
      refund flag too". This wrong version reaches for `CASE WHEN` — worth pinning down
      what that actually is before using it: `CASE WHEN <condition> THEN <value> [WHEN ...]
      ELSE <default> END` is an *expression*, not a statement — it evaluates to a single
      value and can go anywhere a value is expected (`SELECT`, `WHERE`, `HAVING`,
      `ORDER BY`, inside an aggregate like `SUM(...)`/`COUNT(...)`). It checks each `WHEN`
      top to bottom and returns the *first* match's `THEN` value (no fall-through between
      branches, unlike some languages' `switch`); if nothing matches and there's no `ELSE`,
      it evaluates to `NULL`. (There's also a "simple" form, `CASE column WHEN val1 THEN
      ... WHEN val2 THEN ... END`, for plain equality checks against one column — the
      "searched" form used here, with a full boolean condition per `WHEN`, is more general
      and the one used throughout this note.) `SUM(CASE WHEN is_refunded THEN 1 ELSE 0
      END)` below reads as: for each row, contribute `1` if refunded else `0`, then add
      those up per group — a row-level condition turned into a per-group count.
    - `CASE` isn't limited to aggregates — it's just as useful directly inside `ORDER BY`,
      for a **custom sort priority** that plain alphabetical/numeric order can't express.
      This uses the "simple" form (comparing one column against literal values) rather than
      the "searched" form above:
    ```sql
    SELECT Name, Salary
    FROM Employees
    ORDER BY
        CASE Department
            WHEN 'IT' THEN 1
            WHEN 'HR' THEN 2
            ELSE 3
        END;
    ```
    - Trace, given `(Alice, IT, 90000)`, `(Bob, HR, 70000)`, `(Carol, Finance, 80000)`,
      `(Dave, IT, 85000)`: the `CASE` maps each row to a sort key — Alice -> 1, Bob -> 2,
      Carol -> 3, Dave -> 1 — and `ORDER BY` sorts on that computed key, so every `IT` row
      sorts before every `HR` row, which sorts before everything else (`Finance` included,
      via the `ELSE`). Result order: `Alice, Dave` (both key `1`, relative order between
      them undefined without a second sort key), then `Bob` (key `2`), then `Carol` (key
      `3`). Add a second key (e.g. `ORDER BY CASE Department WHEN ... END, Name`) to make
      the within-group order deterministic — the same "later keys only break ties" rule
      from Q1's `LENGTH(first_name), first_name` example applies here too.
    ```sql
    -- WRONG: tries to exclude "regions with any refund at all" instead of excluding
    -- refunded rows before summing
    SELECT region, SUM(amount) AS total_revenue, COUNT(*) AS total_orders
    FROM regional_orders
    GROUP BY region
    HAVING COUNT(*) > 2 AND SUM(CASE WHEN is_refunded THEN 1 ELSE 0 END) = 0;
    ```
    - Trace: with no `WHERE`, grouping runs over **all 9** rows. East = rows 1,2,3 ->
      `count=3`, refund_count = 1 (row 3). West = rows 4,5 -> `count=2`, refund_count = 1.
      North = rows 6,7,8,9 -> `count=4`, refund_count = 1 (row 9). Checking `HAVING`: East
      has `count=3 > 2` but `refund_count=1 <> 0` -> fails. West has `count=2`, not `> 2`
      -> fails. North has `count=4 > 2` but `refund_count=1 <> 0` -> fails. **Every region
      fails — the query returns zero rows**, even though North clearly has 3 qualifying
      non-refunded orders totaling 1000. The bug: this version excludes an entire region
      the moment *any* row in it is refunded, instead of excluding just that one refunded
      row's amount from the sum/count — a materially different question answered by
      accident, not a performance issue or style choice.
    - The lesson generalizes beyond this example: before deciding whether a condition goes
      in `WHERE` or `HAVING`, ask "does this condition apply to individual rows, or to the
      group as a whole?" — not "where is SQL willing to let me write it?"

8. ***Why can two semantically-similar-looking queries — one filtering rows against an
   aggregate via a correlated subquery in `WHERE`, one filtering groups against an
   aggregate via `HAVING` — actually be answering different questions, not just different
   styles of the same one?***
    - Sample data — `products`:

      | product_id | category    | price |
      |------------|-------------|-------|
      | 1          | Electronics | 100   |
      | 2          | Electronics | 300   |
      | 3          | Electronics | 200   |
      | 4          | Furniture   | 50    |
      | 5          | Furniture   | 150   |
    - Question A: "which individual products are priced above their own category's
      average?" This needs a result at **row** grain (one row per qualifying product) —
      `HAVING` can't produce this at all, because `GROUP BY`/`HAVING` collapse to one row
      per group, and by the time `HAVING` runs, individual product prices no longer exist
      as separate rows. The only way to compare a row to its group's aggregate while
      keeping row grain is `WHERE` with a correlated subquery (or a window function):
    ```sql
    SELECT p.product_id, p.category, p.price
    FROM products p
    WHERE p.price > (
        SELECT AVG(p2.price) FROM products p2 WHERE p2.category = p.category
    );
    ```
    - Trace: category averages — Electronics `(100+300+200)/3 = 200`, Furniture
      `(50+150)/2 = 100`. Row by row: product 1 (100 > 200? no), product 2 (300 > 200?
      yes), product 3 (200 > 200? no, not strictly greater), product 4 (50 > 100? no),
      product 5 (150 > 100? yes).

      | product_id | category    | price |
      |------------|-------------|-------|
      | 2          | Electronics | 300   |
      | 5          | Furniture   | 150   |
    - Alternative for Question A using `WITH` (a CTE): precompute each category's average
      once, then join products against it instead of correlating a subquery per row. Same
      result, same row grain — just a different way to get the group-grain value in scope
      for a row-level comparison:
    ```sql
    WITH category_avg AS (
        SELECT category, AVG(price) AS avg_price
        FROM products
        GROUP BY category
    )
    SELECT p.product_id, p.category, p.price
    FROM products p
    JOIN category_avg ca ON ca.category = p.category
    WHERE p.price > ca.avg_price;
    ```
    - The CTE computes each category's average **once per category** (2 rows: Electronics
      200, Furniture 100), then an ordinary join attaches the right average back onto every
      product row for the `WHERE` comparison — versus the correlated subquery form above,
      which recomputes `AVG(p2.price)` **once per outer row** (5 times here) unless the
      optimizer rewrites it. Functionally identical output; the CTE form is generally the
      more efficient and more readable choice once the correlation is this simple, though a
      smart optimizer may produce the same plan for both.
    - Question B: "which *categories'* average price is above the overall average price?"
      This is a genuinely different, **group**-grain question, and `HAVING` is the natural
      (and only sensible) tool:
    ```sql
    SELECT category, AVG(price) AS avg_price
    FROM products
    GROUP BY category
    HAVING AVG(price) > (SELECT AVG(price) FROM products);
    ```
    - Trace: overall average = `(100+300+200+50+150)/5 = 160`. Electronics average `200 >
      160` -> passes. Furniture average `100 > 160` -> fails.

      | category    | avg_price |
      |-------------|-----------|
      | Electronics | 200       |
    - The point: these aren't two styles for writing "the same logic" — the `WHERE`
      version operates at row grain, the `HAVING` version at group grain, and trying to
      write Question A's logic as a `HAVING` clause (e.g. `HAVING AVG(price) >
      AVG(price)`) is a category error, not a rewrite, because the per-row `price` value
      Question A needs no longer exists once `HAVING` runs. When two forms genuinely are
      interchangeable, it's because the question itself was reframed to match the new
      grain, not because the SQL was mechanically transformed.
    - Performance angle, when a genuine row-vs-group choice exists (e.g. an `EXISTS`
      correlated subquery could sometimes be reframed as a `HAVING` on a join): the
      correlated subquery in `WHERE` re-evaluates its inner aggregate **once per outer
      row** (5 times here, N times at scale) unless the optimizer rewrites it into a join
      against a pre-aggregated derived table (some engines do this automatically for
      simple cases; complex correlated subqueries often force a literal per-row
      re-execution). The `HAVING`/group-grain version computes each aggregate exactly
      **once per group** in a single pass — fundamentally more set-based and cheaper when
      the question can legitimately be asked at group grain.

9. ***How would you get the top 2 highest-paid employees per department — and why can't
   `GROUP BY`/`HAVING` do this directly?***
    - Sample data — `dept_salaries`:

      | emp_id | dept | salary |
      |--------|------|--------|
      | 1      | Eng  | 90000  |
      | 2      | Eng  | 95000  |
      | 3      | Eng  | 80000  |
      | 4      | Sales| 85000  |
      | 5      | Sales| 60000  |
    - `GROUP BY dept` collapses to exactly **one row per department** — it can give you
      `MAX(salary)` or a single aggregated summary, but it fundamentally cannot return "the
      top 2 full rows" per group, because that means keeping *multiple, distinct, un-collapsed
      rows per group with all their original columns* — the opposite of what grouping does.
      `HAVING` only makes this worse: it filters which *groups* survive, not which rows
      within a group survive. Neither clause operates at "N rows retained per group" grain.
    - The right tool is a window function, which ranks rows **without** collapsing them,
      wrapped in an outer query/CTE so the ranking can then be filtered on:
    ```sql
    SELECT emp_id, dept, salary
    FROM (
        SELECT emp_id, dept, salary,
               ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC) AS rn
        FROM dept_salaries
    ) ranked
    WHERE rn <= 2;
    ```
    - Trace: partition by `dept`, order by `salary DESC` within each partition. Eng sorted:
      95000 (emp 2, rn=1), 90000 (emp 1, rn=2), 80000 (emp 3, rn=3). Sales sorted: 85000
      (emp 4, rn=1), 60000 (emp 5, rn=2). Outer `WHERE rn <= 2` keeps emp 2 and emp 1 from
      Eng (dropping emp 3), and keeps both Sales rows (only 2 exist there anyway).

      | emp_id | dept  | salary |
      |--------|-------|--------|
      | 2      | Eng   | 95000  |
      | 1      | Eng   | 90000  |
      | 4      | Sales | 85000  |
      | 5      | Sales | 60000  |
    - Same query written with `WITH` (a CTE) instead of an inline subquery — purely a
      naming/readability choice, identical execution and result:
    ```sql
    WITH ranked AS (
        SELECT emp_id, dept, salary,
               ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC) AS rn
        FROM dept_salaries
    )
    SELECT emp_id, dept, salary
    FROM ranked
    WHERE rn <= 2;
    ```
    - Why the outer wrapper is required, tying back to Q2/Q3: `WHERE rn <= 2` cannot be
      written directly in the same query block that defines `rn`, because window functions
      are computed as part of the `SELECT`-processing step — the same "not computed yet by
      the time `WHERE` runs" reasoning that blocks referencing a `SELECT` alias (Q2) or an
      aggregate (Q3) directly in `WHERE`. The subquery/CTE boundary exists specifically to
      let a later query block's `WHERE` see a value the inner block's `SELECT` already
      finished computing.

10. ***A paginated API uses `ORDER BY amount DESC LIMIT 2 OFFSET 2` for "page 2." Users
    report seeing the same row twice across pages, and another row never appears at all.
    What's actually going on, and how do you fix it?***
    - Sample data — `orders`:

      | order_id | amount |
      |----------|--------|
      | 1        | 50     |
      | 2        | 100    |
      | 3        | 100    |
      | 4        | 100    |
      | 5        | 30     |
    - `ORDER BY amount DESC` fully determines the order *between distinct amounts*, but
      says nothing about how to order rows that **tie** on `amount` — orders 2, 3, and 4 are
      all `100`. The SQL standard does not guarantee a stable, repeatable order for tied
      rows; the engine is free to return them in whatever order is convenient for the
      chosen execution plan (which can itself change between calls, e.g. after an index
      rebuild, a plan cache eviction, or simply because it's a parallel scan). This is the
      actual failure mode — not a specific wrong number, but the *absence* of any
      guarantee: "page 1" (`LIMIT 2 OFFSET 0`) might return orders 2 and 3, and "page 2"
      (`LIMIT 2 OFFSET 2`) might then return order 3 again followed by order 4 — order 3
      duplicated across pages, while a different run could just as validly skip order 4
      entirely. Since `ORDER BY` (step 8) genuinely does run before `LIMIT`/`OFFSET` (step
      9) in the logical order, the bug isn't in *when* the sort or trim happens — it's that
      the sort key itself doesn't fully determine a unique ordering, so which rows land in
      which page is undefined for the tied rows.
    - Fix: make the `ORDER BY` **fully deterministic** by appending a tiebreaker that is
      itself unique per row — typically the primary key:
    ```sql
    -- page 1
    SELECT * FROM orders ORDER BY amount DESC, order_id DESC LIMIT 2 OFFSET 0;
    -- page 2
    SELECT * FROM orders ORDER BY amount DESC, order_id DESC LIMIT 2 OFFSET 2;
    ```
    - Trace: with `order_id DESC` breaking ties, the full order is always exactly
      `4 (100), 3 (100), 2 (100), 1 (50), 5 (30)` on every single execution. Page 1
      (`OFFSET 0`) is always `[4, 3]`; page 2 (`OFFSET 2`) is always `[2, 1]` — no overlap,
      no gaps, and this is guaranteed to be the same on every call, not just likely.
      Whenever a sort key can contain duplicates and the query is used for pagination (or
      anything else where row identity across separate calls matters), the sort must be
      extended with a unique column to be safe.

11. ***A stakeholder asks for "regions where more than 3 customers ordered." Two engineers
    write `HAVING COUNT(DISTINCT customer_id) > 3` and `HAVING COUNT(customer_id) > 3`
    respectively for the same `GROUP BY region` query. Why can these return completely
    different results, and which one actually answers the question asked?***
    - Sample data — `order_events` (one row per order placed):

      | event_id | region | customer_id |
      |----------|--------|-------------|
      | 1        | East   | C1          |
      | 2        | East   | C1          |
      | 3        | East   | C1          |
      | 4        | East   | C2          |
      | 5        | East   | C3          |
      | 6        | West   | C4          |
      | 7        | West   | C4          |
      | 8        | West   | C4          |
      | 9        | West   | C4          |
    - `COUNT(customer_id)` (equivalent to `COUNT(*)` here, since `customer_id` is never
      `NULL`) counts every **order row** — order *volume*. `COUNT(DISTINCT customer_id)`
      collapses repeat customers first, then counts — customer *breadth*. These answer
      different business questions that a hurried spec ("more than 3 orders/customers per
      region") can easily conflate.
    ```sql
    -- "more than 3 orders" (volume)
    SELECT region, COUNT(customer_id) AS total_orders
    FROM order_events
    GROUP BY region
    HAVING COUNT(customer_id) > 3;
    ```
    - Trace: East has 5 rows (`count=5`), West has 4 rows (`count=4`). Both `> 3`.

      | region | total_orders |
      |--------|----------------|
      | East   | 5              |
      | West   | 4              |
    ```sql
    -- "more than 3 distinct customers" (breadth) — the question actually asked
    SELECT region, COUNT(DISTINCT customer_id) AS distinct_customers
    FROM order_events
    GROUP BY region
    HAVING COUNT(DISTINCT customer_id) > 3;
    ```
    - Trace: East's distinct customers are `{C1, C2, C3}` -> `3`. West's distinct customer
      is `{C4}` -> `1` (all 4 of West's orders are the same customer). Neither region has
      more than 3 *distinct* customers, so `HAVING COUNT(DISTINCT customer_id) > 3` keeps
      **no rows at all** — the result set is empty.
    - This is the real synthesis point: the volume query returns both regions, the breadth
      query returns nothing, from the *same underlying data* — a one-word difference
      (`DISTINCT`) completely changes which rows pass `HAVING`. "More than 3 customers
      ordered" is a statement about distinct entities, so `COUNT(DISTINCT customer_id) > 3`
      is the version that actually answers the stated question; `COUNT(customer_id) > 3`
      answers a different, easily-conflated question ("more than 3 orders happened") that
      West's single repeat customer satisfies on its own. Getting this right requires
      recognizing which real-world entity ("an order" vs "a customer") the requirement is
      actually counting — a judgment call the SQL syntax alone won't flag as wrong.

## Related Notes in This Repo
- [`01_Joins.md`](01_Joins.md) — Q8 there (the `ON` vs `WHERE` gotcha for outer joins)
  leans directly on the `JOIN` -> `WHERE` ordering established in Q1 here.
