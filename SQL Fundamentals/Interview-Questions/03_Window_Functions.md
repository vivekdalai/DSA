# Window Functions Interview Questions

*All examples use standard ANSI SQL syntax (window functions are part of the SQL
standard, ISO/IEC 9075, and behave the same way across PostgreSQL, SQL Server, Oracle,
and MySQL 8.0+). Any engine-specific caveat is called out explicitly — see Q7.*

Questions are organized by difficulty: **Easy** (definitions/basic syntax), **Medium**
(applied gotchas, comparisons, common mistakes), **Hard** (SDE2-level scenarios that
combine multiple concepts and require a judgment call).

---

## Easy

### Core Concept: Window Functions vs. Aggregate Functions
1. ***What fundamentally distinguishes a window function from a regular aggregate function?***
    - A plain aggregate (`AVG`, `SUM`, `COUNT`, ... used with `GROUP BY`) **collapses**
      every row in a group down into a single output row per group — the individual rows
      that went into the calculation are gone from the result set.
    - A **window function** (any function used with an `OVER(...)` clause) computes its
      value across a set of rows *related to the current row* — the "window" — but does
      **not** collapse anything. Every original row is preserved in the output, and the
      computed value is attached alongside it as an extra column. In effect, a window
      function lets you ask "what's the department average?" while still seeing every
      individual employee row, instead of forcing a choice between per-row detail and
      per-group aggregation.
    - Mechanically: for each row, the database looks at that row's window (determined by
      `PARTITION BY` and, if present, `ORDER BY` + a frame — see Q9), computes the
      aggregate/ranking value over just that window, and writes the result on that one
      row — repeated independently for every row in the result set.

2. ***Worked example — `GROUP BY` vs. window function, side by side.***
    - Sample `employees` table:

      | emp_id | name | department | salary |
      |---|---|---|---|
      | 1 | Alice | Engineering | 90000 |
      | 2 | Bob | Engineering | 85000 |
      | 3 | Carol | Engineering | 95000 |
      | 4 | Dave | Sales | 60000 |
      | 5 | Eve | Sales | 65000 |
      | 6 | Frank | Marketing | 70000 |

    - **Plain aggregate (`GROUP BY`)** — one row per department, individual employees are gone:
      ```sql
      SELECT department, AVG(salary) AS avg_salary
      FROM employees
      GROUP BY department;
      ```
      Result (3 rows):

      | department | avg_salary |
      |---|---|
      | Engineering | 90000 |
      | Sales | 62500 |
      | Marketing | 70000 |

    - **Equivalent window function** — one row per employee, each annotated with their
      own department's average:
      ```sql
      SELECT emp_id, name, department, salary,
             AVG(salary) OVER (PARTITION BY department) AS dept_avg_salary
      FROM employees;
      ```
      Result (6 rows — nothing collapsed):

      | emp_id | name | department | salary | dept_avg_salary |
      |---|---|---|---|---|
      | 1 | Alice | Engineering | 90000 | 90000 |
      | 2 | Bob | Engineering | 85000 | 90000 |
      | 3 | Carol | Engineering | 95000 | 90000 |
      | 4 | Dave | Sales | 60000 | 62500 |
      | 5 | Eve | Sales | 65000 | 62500 |
      | 6 | Frank | Marketing | 70000 | 70000 |
    - This is exactly why window functions are the right tool whenever you need both the
      individual row *and* some group-level context in the same result — e.g. "show each
      employee's salary next to their department's average" cannot be expressed with plain
      `GROUP BY` at all, since `GROUP BY` fundamentally cannot return per-employee detail.

3. ***What are the components of the `OVER(...)` clause?***
    - `PARTITION BY` — (optional) splits rows into independent groups; the window
      calculation restarts for each partition (see Q8).
    - `ORDER BY` — (optional) defines the order rows are processed in within a partition;
      required for functions like `ROW_NUMBER()`, `LAG()`/`LEAD()` to be meaningful, and it
      also changes the *default frame* for aggregate functions (see Q9, Q10).
    - **Frame clause** (`ROWS BETWEEN ... AND ...` / `RANGE BETWEEN ... AND ...`) —
      (optional) further restricts the window to a sub-range of the partition relative to
      the current row, e.g. "the 2 preceding rows and the current row" for a moving
      average (see Q11). Only meaningful for aggregate window functions — ranking
      functions ignore the frame clause entirely.

### `LAG()` and `LEAD()`
4. ***What do `LAG()` and `LEAD()` do?***
    - `LAG(column, offset, default)` reads a column's value from a row **before** the
      current row (within its partition, per the `ORDER BY`) — `offset` defaults to `1`
      (the immediately preceding row), and `default` (optional) is returned instead of
      `NULL` when there is no such row (e.g. the very first row in the partition).
    - `LEAD(column, offset, default)` is the mirror image — it reads a column's value from
      a row **after** the current row, with the same `offset`/`default` semantics.
    - Both let you compare a row to a neighboring row **without a self-join** — the
      classic use case is computing period-over-period change (day-over-day,
      month-over-month, etc.).

5. ***Worked example — day-over-day revenue change.***
    - Sample `sales_daily` table:

      | sale_date | revenue |
      |---|---|
      | 2024-01-01 | 1000 |
      | 2024-01-02 | 1200 |
      | 2024-01-03 | 1100 |
      | 2024-01-04 | 1300 |

      ```sql
      SELECT sale_date, revenue,
             LAG(revenue) OVER (ORDER BY sale_date) AS prev_day_revenue,
             revenue - LAG(revenue) OVER (ORDER BY sale_date) AS day_over_day_change
      FROM sales_daily;
      ```
      Result:

      | sale_date | revenue | prev_day_revenue | day_over_day_change |
      |---|---|---|---|
      | 2024-01-01 | 1000 | NULL | NULL |
      | 2024-01-02 | 1200 | 1000 | 200 |
      | 2024-01-03 | 1100 | 1200 | -100 |
      | 2024-01-04 | 1300 | 1100 | 200 |
    - The first row has no prior day, so `LAG()` returns `NULL` (and the arithmetic
      expression involving it also evaluates to `NULL`) — pass a third argument, e.g.
      `LAG(revenue, 1, 0)`, if you'd rather default to `0` than `NULL` for that edge row.
    - `LEAD()` gives the symmetric "next day" view:
      ```sql
      SELECT sale_date, revenue,
             LEAD(revenue) OVER (ORDER BY sale_date) AS next_day_revenue
      FROM sales_daily;
      ```

      | sale_date | revenue | next_day_revenue |
      |---|---|---|
      | 2024-01-01 | 1000 | 1200 |
      | 2024-01-02 | 1200 | 1100 |
      | 2024-01-03 | 1100 | 1300 |
      | 2024-01-04 | 1300 | NULL |

### Other Common Window Functions (Quick Reference)
6. ***What other window functions come up besides ranking and `LAG`/`LEAD`?***
    - **`FIRST_VALUE(expr)` / `LAST_VALUE(expr)`** — returns the value of `expr` from the
      first/last row of the window frame (careful: `LAST_VALUE` is a frequent gotcha
      because, per Q10, the *default* frame for an ordered window ends at the current row,
      so `LAST_VALUE` often just returns the current row's own value unless the frame is
      explicitly widened to the full partition — see Q21 for a full worked diagnosis).
    - **`NTILE(n)`** — divides the partition's rows as evenly as possible into `n` numbered
      buckets (e.g. `NTILE(4)` for quartiles) — useful for percentile-style bucketing, but
      see Q22 for why it can misbehave on small or unevenly-sized partitions.
    - **Any aggregate as a window function** — `SUM()`, `COUNT()`, `MIN()`, `MAX()` all
      work with `OVER(...)` exactly like `AVG()` does in Q2/Q9 — e.g. `SUM(amount) OVER
      (PARTITION BY customer_id)` for a per-customer running or full total, `COUNT(*) OVER
      (PARTITION BY department)` for a per-employee "how many people are in my
      department" column.

### Portability / Engine Support
7. ***Are window functions standard SQL, or engine-specific?***
    - Window functions are part of the ANSI SQL standard (introduced in SQL:2003) and all
      examples above use portable, standard syntax that behaves identically across major
      engines.
    - The one practical caveat worth knowing for interviews: **MySQL did not support
      window functions until version 8.0** (released 2018) — any MySQL instance on 5.7 or
      earlier has no `OVER()` support at all, and the same "top N per group" or "running
      total" problems had to be solved with self-joins or session variables instead.
      PostgreSQL (since 8.4), SQL Server (since 2012), and Oracle (since 8i/9i) have
      supported the standard syntax for considerably longer.

      | Engine | Window function support since |
      |---|---|
      | PostgreSQL | 8.4 (2009) |
      | SQL Server | 2012 (basic ranking support since 2005) |
      | Oracle | 8i/9i |
      | MySQL | 8.0 (2018) — no support in 5.7 and earlier |

---

## Medium

### `PARTITION BY` vs. `GROUP BY`, and the Default-Frame Gotcha
8. ***How does `PARTITION BY` differ from `GROUP BY`?***
    - `GROUP BY` physically collapses rows into one row per group.
    - `PARTITION BY` (used inside `OVER(...)`) resets/restarts the window calculation
      independently for each group of rows sharing the same partition key, but **does not
      collapse rows** — every row stays in the output, tagged with the value computed over
      its own partition. This is exactly the `dept_avg_salary` column in Q2 — the
      calculation is scoped per department (like `GROUP BY department` would scope it),
      but each employee row survives.
    - A query can even use both in different columns simultaneously: a window function's
      `PARTITION BY` can coexist with a separate `GROUP BY` elsewhere in more complex
      queries, since they operate at different stages of logical query processing.

9. ***Gotcha: does adding `ORDER BY` inside `OVER()` change the result of an aggregate window function?***
    - Yes — and this trips people up constantly. Adding `ORDER BY` to `OVER(...)` changes
      the **default frame** for an aggregate function from "the whole partition" to "from
      the start of the partition up through the current row" (a running/cumulative
      calculation) — see Q10 for the full rule.
    - Using the Engineering rows from Q2 (Bob 85000, Alice 90000, Carol 95000), sorted
      ascending by salary:
      ```sql
      SELECT name, salary,
             AVG(salary) OVER (PARTITION BY department ORDER BY salary) AS running_avg
      FROM employees
      WHERE department = 'Engineering';
      ```
      Result — a **running** average, not the flat department average from Q2:

      | name | salary | running_avg |
      |---|---|---|
      | Bob | 85000 | 85000 |
      | Alice | 90000 | 87500 |
      | Carol | 95000 | 90000 |
    - Trace: Bob is first in salary order, so his running window is just himself:
      `85000`. Alice's window is Bob + Alice: `(85000 + 90000) / 2 = 87500`. Carol's
      window is all three: `(85000 + 90000 + 95000) / 3 = 90000`. Only the *last* row in
      order-by sequence ends up matching the plain department average from Q2 — this is a
      common source of "why is my window function giving weird numbers" bugs when someone
      adds an `ORDER BY` out of habit (e.g. to make output look sorted) without realizing
      it silently changes the frame.

10. ***What is the default window frame, and how does `ORDER BY` change it?***
    - Per the ANSI SQL standard, when an aggregate window function has an `ORDER BY`
      inside `OVER(...)` but no explicit frame clause, the default frame is `RANGE BETWEEN
      UNBOUNDED PRECEDING AND CURRENT ROW` — a **running/cumulative** calculation from the
      start of the partition through the current row. This is exactly what produced the
      running average in Q9.
    - When `OVER(...)` has **no** `ORDER BY` at all, the default frame is the **entire
      partition** (`UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING`) — every row in the
      partition contributes to every other row's value, which is why Q2's `AVG(salary)
      OVER (PARTITION BY department)` (no `ORDER BY`) correctly reproduced the flat
      per-department average.
    - The frame clause is meaningless for ranking functions (`ROW_NUMBER`, `RANK`,
      `DENSE_RANK`) and for `LAG`/`LEAD` — they always operate on the full ordered
      partition regardless of any `ROWS`/`RANGE` clause; framing only affects aggregate
      window functions.

11. ***Explicit frame example — a 3-day moving average.***
    - Using the `sales_daily` table from Q5:
      ```sql
      SELECT sale_date, revenue,
             AVG(revenue) OVER (
               ORDER BY sale_date
               ROWS BETWEEN 2 PRECEDING AND CURRENT ROW
             ) AS moving_avg_3day
      FROM sales_daily;
      ```
    - Trace: the frame is "the current row plus the 2 rows before it" (fewer if not
      enough rows exist yet). Row 1 (`01-01`) has no rows before it, so its frame is just
      itself: `1000`. Row 2 (`01-02`) only has 1 prior row available: `(1000 + 1200) / 2 =
      1100`. Row 3 (`01-03`) has a full 3-row frame: `(1000 + 1200 + 1100) / 3 = 1100`.
      Row 4 (`01-04`) has a full 3-row frame that has now slid forward, dropping `01-01`:
      `(1200 + 1100 + 1300) / 3 = 1200`.

      | sale_date | revenue | moving_avg_3day |
      |---|---|---|
      | 2024-01-01 | 1000 | 1000 |
      | 2024-01-02 | 1200 | 1100 |
      | 2024-01-03 | 1100 | 1100 |
      | 2024-01-04 | 1300 | 1200 |
    - `ROWS` counts physical rows (used above); `RANGE` counts logical value ranges
      (relevant mainly when the `ORDER BY` column has ties or you want a value-based
      window, e.g. "all rows within 7 days of the current row's date") — `ROWS` is the more
      commonly asked-about variant in interviews.

### Ranking Functions: `ROW_NUMBER()` vs. `RANK()` vs. `DENSE_RANK()`
12. ***What's the exact difference between `ROW_NUMBER()`, `RANK()`, and `DENSE_RANK()`?***
    - All three assign an integer to each row within a partition, ordered by `ORDER BY`,
      starting at 1 — they differ **only in how they handle ties** (rows with equal
      `ORDER BY` values):
      - **`ROW_NUMBER()`** — always assigns a strictly sequential, unique number to every
        row, even if values are tied. Ties are broken arbitrarily/non-deterministically
        unless the `ORDER BY` itself is unique (see Q14).
      - **`RANK()`** — tied rows receive the *same* rank. The next rank after a tie group
        **skips** ahead by the number of tied rows (leaving a gap) — e.g. two rows tied
        for rank 2 means the next distinct value gets rank 4, not 3.
      - **`DENSE_RANK()`** — tied rows also receive the *same* rank, but the next rank
        after a tie group is always the very next consecutive integer — **no gaps**.

13. ***Worked example — all three side by side on tied scores.***
    - Sample `scores` table, ordered by `score` descending:

      | student | score |
      |---|---|
      | A | 95 |
      | B | 90 |
      | C | 90 |
      | D | 85 |
      | E | 85 |
      | F | 85 |
      | G | 80 |

      ```sql
      SELECT student, score,
             ROW_NUMBER() OVER (ORDER BY score DESC) AS row_num,
             RANK()       OVER (ORDER BY score DESC) AS rnk,
             DENSE_RANK() OVER (ORDER BY score DESC) AS dense_rnk
      FROM scores;
      ```
      Result:

      | student | score | row_num | rnk | dense_rnk |
      |---|---|---|---|---|
      | A | 95 | 1 | 1 | 1 |
      | B | 90 | 2 | 2 | 2 |
      | C | 90 | 3 | 2 | 2 |
      | D | 85 | 4 | 4 | 3 |
      | E | 85 | 5 | 4 | 3 |
      | F | 85 | 6 | 4 | 3 |
      | G | 80 | 7 | 7 | 4 |
    - Trace of the interesting rows: B and C are tied at 90, so both get `rnk = 2` and
      `dense_rnk = 2`; `row_num` still increments to 2 then 3 regardless. After that
      2-row tie, `RANK()` jumps to `4` (skipping 3, because 3 rows — A, B, C — now rank
      ahead of D), while `DENSE_RANK()` moves to the next consecutive value, `3`. The same
      pattern repeats for the 3-way tie at 85 (D, E, F): `RANK()` jumps from 4 straight to
      7 for G (6 rows rank ahead of G), while `DENSE_RANK()` only moves to 4.

14. ***Is `ROW_NUMBER()` deterministic when there are ties in the `ORDER BY` column?***
    - No — if the `ORDER BY` expression doesn't uniquely determine row order (e.g. two
      students tied at score 90, as in Q13), the database is free to break the tie in any
      order, and that order is **not guaranteed to be stable** across runs or query plan
      changes. If a specific tiebreak matters (e.g. always prefer the lexicographically
      first name, or the earliest-inserted row), add a deterministic secondary column to
      `ORDER BY`, e.g. `ORDER BY score DESC, student ASC`.

15. ***Practical gotcha: does `RANK() <= N` (or `DENSE_RANK() <= N`) reliably return exactly N rows per group?***
    - No — only `ROW_NUMBER() <= N` is guaranteed to return exactly `N` rows (per
      partition). `RANK()` and `DENSE_RANK()` can return **more** than `N` rows whenever
      there's a tie straddling the cutoff.
    - Using the `scores` table from Q13 with `<= 2`:
      - `ROW_NUMBER() <= 2` → exactly 2 rows: A, B.
      - `RANK() <= 2` → **3 rows**: A (rank 1), B and C (both rank 2) — because B and C
        are tied and both legitimately have rank 2.
      - `DENSE_RANK() <= 2` → also **3 rows**: A, B, C, same reasoning.
    - Whether that's a bug or the desired behavior depends on intent: use `RANK()`/
      `DENSE_RANK()` when ties should be included together ("give me everyone tied for
      top 2"), and use `ROW_NUMBER()` when you need an exact row count regardless of ties
      ("give me exactly 2 rows, tie broken arbitrarily or by an explicit secondary sort").

### Practical Applications
16. ***Standard technique: "top N rows per group."***
    - `ROW_NUMBER() OVER (PARTITION BY <group_col> ORDER BY <rank_col> DESC)` wrapped in a
      CTE (or subquery), then filtered with `WHERE rn <= N`, is the idiomatic way to get
      the top `N` rows of each group in a single query — no correlated subquery, no
      per-group `LIMIT`, works uniformly across engines.

17. ***Worked example — top 2 highest-paid employees per department.***
    - Using the `employees` table from Q2:
      ```sql
      WITH ranked AS (
        SELECT emp_id, name, department, salary,
               ROW_NUMBER() OVER (PARTITION BY department ORDER BY salary DESC) AS rn
        FROM employees
      )
      SELECT emp_id, name, department, salary
      FROM ranked
      WHERE rn <= 2;
      ```
    - Trace, partition by partition: Engineering ordered by salary desc is Carol (95000,
      rn=1), Alice (90000, rn=2), Bob (85000, rn=3) — Bob is excluded. Sales is Eve
      (65000, rn=1), Dave (60000, rn=2) — both kept. Marketing is just Frank (70000,
      rn=1) — kept.
    - Result (5 rows — Bob is the only employee dropped, since Engineering had 3 people
      and we only wanted 2):

      | emp_id | name | department | salary |
      |---|---|---|---|
      | 3 | Carol | Engineering | 95000 |
      | 1 | Alice | Engineering | 90000 |
      | 5 | Eve | Sales | 65000 |
      | 4 | Dave | Sales | 60000 |
      | 6 | Frank | Marketing | 70000 |

18. ***Standard technique: finding duplicate rows.***
    - The same `ROW_NUMBER() OVER (PARTITION BY <columns that define a duplicate> ORDER BY
      <tiebreak, e.g. primary key>)` pattern identifies duplicates directly: partition by
      whatever combination of columns should be unique, and any row with `rn > 1` is a
      duplicate of an earlier row in that partition.
    - Example — an `employees_raw` table with duplicate emails:

      | id | email |
      |---|---|
      | 1 | a@x.com |
      | 2 | b@x.com |
      | 3 | a@x.com |
      | 4 | c@x.com |
      | 5 | a@x.com |

      ```sql
      SELECT id, email,
             ROW_NUMBER() OVER (PARTITION BY email ORDER BY id) AS rn
      FROM employees_raw;
      ```
      Result (partitioned by `email`, ordered by `id` ascending within each):

      | id | email | rn |
      |---|---|---|
      | 1 | a@x.com | 1 |
      | 3 | a@x.com | 2 |
      | 5 | a@x.com | 3 |
      | 2 | b@x.com | 1 |
      | 4 | c@x.com | 1 |
    - Rows `id = 3` and `id = 5` are the duplicates (rn > 1) — `id = 1` is treated as the
      "original" because `ORDER BY id` puts the lowest id first in that partition. This
      pattern is also the standard way to actually **delete** duplicates, keeping one copy:
      ```sql
      DELETE FROM employees_raw
      WHERE id IN (
        SELECT id FROM (
          SELECT id, ROW_NUMBER() OVER (PARTITION BY email ORDER BY id) AS rn
          FROM employees_raw
        ) t
        WHERE rn > 1
      );
      ```

---

## Hard

19. ***For each user, determine whether their most recent purchase amount increased or decreased relative to their trailing 3-purchase average — using window functions only, no self-join.***
    - The trick is two window functions on the same partition: one to identify "most
      recent" (`ROW_NUMBER()` ordered by date descending), and one to compute a
      **trailing** average that explicitly excludes the current row via the frame clause
      (`ROWS BETWEEN 3 PRECEDING AND 1 PRECEDING` — "the up-to-3 rows before this one, not
      including this one").
    - Sample `purchases` table:

      | user_id | purchase_date | amount |
      |---|---|---|
      | 1 | 2024-01-01 | 100 |
      | 1 | 2024-01-05 | 120 |
      | 1 | 2024-01-10 | 90 |
      | 1 | 2024-01-15 | 200 |
      | 2 | 2024-01-02 | 300 |
      | 2 | 2024-01-06 | 250 |
      | 2 | 2024-01-11 | 220 |
      | 2 | 2024-01-16 | 150 |
      | 3 | 2024-01-03 | 50 |
      | 3 | 2024-01-08 | 80 |

      ```sql
      WITH ranked AS (
        SELECT user_id, purchase_date, amount,
               ROW_NUMBER() OVER (
                 PARTITION BY user_id ORDER BY purchase_date DESC
               ) AS rn_most_recent,
               AVG(amount) OVER (
                 PARTITION BY user_id ORDER BY purchase_date
                 ROWS BETWEEN 3 PRECEDING AND 1 PRECEDING
               ) AS trailing_3_avg
        FROM purchases
      )
      SELECT user_id, purchase_date, amount, trailing_3_avg,
             CASE
               WHEN amount > trailing_3_avg THEN 'increased'
               WHEN amount < trailing_3_avg THEN 'decreased'
               ELSE 'unchanged'
             END AS trend
      FROM ranked
      WHERE rn_most_recent = 1;
      ```
    - Trace, user by user (frame is computed in ascending-date order, so for the last
      chronological row it correctly looks *backward*):
      - User 1's most recent purchase is `2024-01-15` (200). Its trailing frame is the 3
        rows before it: 100, 120, 90 → avg = `310 / 3 = 103.33`. `200 > 103.33` →
        **increased**.
      - User 2's most recent is `2024-01-16` (150). Trailing frame: 300, 250, 220 → avg =
        `770 / 3 = 256.67`. `150 < 256.67` → **decreased**.
      - User 3 only has 2 total purchases, so the most recent (`2024-01-08`, 80) has just
        1 prior row available for its frame — `ROWS BETWEEN 3 PRECEDING AND 1 PRECEDING`
        gracefully uses whatever is available, giving avg = `50`. `80 > 50` →
        **increased**. This shows the frame doesn't need a full 3 rows to be valid —
        it silently adapts, which is exactly why it's the right tool here instead of
        manually checking row counts.
    - Result:

      | user_id | purchase_date | amount | trailing_3_avg | trend |
      |---|---|---|---|---|
      | 1 | 2024-01-15 | 200 | 103.33 | increased |
      | 2 | 2024-01-16 | 150 | 256.67 | decreased |
      | 3 | 2024-01-08 | 80 | 50.00 | increased |

20. ***You need "top 3 products per category by revenue, but only categories with at least 5 distinct products." Walk through combining a window function with a filtering step, and why you can't put the window function's result directly in `WHERE`.***
    - **Why `WHERE rn <= 3` fails in the same `SELECT`:** per SQL's logical query
      processing order, `WHERE` is evaluated *before* the `SELECT` list — and window
      functions are computed as part of the `SELECT` list, logically after `WHERE`,
      `GROUP BY`, and `HAVING` have already run. So a window function's alias (or even
      its raw expression) doesn't exist yet when `WHERE` is evaluated; every mainstream
      engine rejects `WHERE ROW_NUMBER() OVER (...) <= 3` outright. The fix is always the
      same shape: compute the window function in a subquery or CTE, then filter it in an
      **outer** `WHERE`, which now runs against already-materialized rows that include the
      window function's output as an ordinary column. (Some engines — Snowflake, BigQuery,
      DuckDB — offer a `QUALIFY` clause as a non-ANSI shortcut for exactly this, but it
      isn't portable.)
    - **The category-count gate is a second, independent problem:** "at least 5 distinct
      products" is a per-category aggregate condition, unrelated to the per-category
      revenue ranking — so it needs its own `GROUP BY ... HAVING` step (or an equivalent
      `COUNT(DISTINCT ...)` filter), computed separately and then used to restrict which
      categories the ranking step even looks at.
    - Sample `products` table:

      | product_id | category | revenue |
      |---|---|---|
      | P1 | A | 500 |
      | P2 | A | 450 |
      | P3 | A | 400 |
      | P4 | A | 380 |
      | P5 | A | 300 |
      | P6 | A | 100 |
      | P7 | B | 900 |
      | P8 | B | 800 |
      | P9 | B | 700 |
      | P10 | C | 200 |
      | P11 | C | 190 |
      | P12 | C | 180 |
      | P13 | C | 170 |
      | P14 | C | 160 |

      ```sql
      WITH category_counts AS (
        SELECT category, COUNT(DISTINCT product_id) AS distinct_products
        FROM products
        GROUP BY category
        HAVING COUNT(DISTINCT product_id) >= 5
      ),
      ranked AS (
        SELECT p.product_id, p.category, p.revenue,
               ROW_NUMBER() OVER (
                 PARTITION BY p.category ORDER BY p.revenue DESC
               ) AS rn
        FROM products p
        JOIN category_counts cc ON cc.category = p.category
      )
      SELECT product_id, category, revenue
      FROM ranked
      WHERE rn <= 3
      ORDER BY category, rn;
      ```
    - Trace: category A has 6 distinct products (`>= 5` — passes the gate), category B
      has only 3 (fails, dropped entirely — even though it has the *highest individual
      revenues* of any category, 900/800/700, none of it survives, because the gate is
      about product-count breadth, not revenue), category C has exactly 5 (passes). The
      `JOIN` to `category_counts` removes category B's rows *before* `ROW_NUMBER()` ever
      ranks them. Within A, ranked desc by revenue: P1=1, P2=2, P3=3, P4=4, ... → keep
      P1/P2/P3. Within C: P10=1, P11=2, P12=3, P13=4, P14=5 → keep P10/P11/P12.
    - Result:

      | product_id | category | revenue |
      |---|---|---|
      | P1 | A | 500 |
      | P2 | A | 450 |
      | P3 | A | 400 |
      | P10 | C | 200 |
      | P11 | C | 190 |
      | P12 | C | 180 |

21. ***Production bug: `LAST_VALUE(order_date) OVER (PARTITION BY customer_id ORDER BY order_date)` is supposed to show each order alongside the customer's most recent order date, but every row just shows its own `order_date` back. Diagnose why, and give two fixes.***
    - **Root cause:** an `ORDER BY` inside `OVER(...)` with no explicit frame defaults to
      `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` (Q10). `LAST_VALUE` returns the
      value from the *last row of the frame* — and for every row, the frame's last row
      (given the default) is the current row itself. So `LAST_VALUE` degenerates into "my
      own value," which is a well-known trap since the function name suggests it should
      reach across the whole partition.
    - Sample `orders` table (customer 1 only, for brevity):

      | customer_id | order_id | order_date |
      |---|---|---|
      | 1 | 101 | 2024-01-01 |
      | 1 | 102 | 2024-01-05 |
      | 1 | 103 | 2024-01-10 |

      Naive query and its (wrong) output:
      ```sql
      SELECT customer_id, order_id, order_date,
             LAST_VALUE(order_date) OVER (
               PARTITION BY customer_id ORDER BY order_date
             ) AS latest_order_date
      FROM orders;
      ```

      | order_id | order_date | latest_order_date (buggy) |
      |---|---|---|
      | 101 | 2024-01-01 | 2024-01-01 |
      | 102 | 2024-01-05 | 2024-01-05 |
      | 103 | 2024-01-10 | 2024-01-10 |

      Every row just echoes its own date — the bug reproduced. Row 101's frame is
      `[101..101]` (default frame ends at current row), so its "last row" is itself; same
      for 102 (`[101..102]`, last = 102) and 103 (`[101..103]`, last = 103).
    - **Fix 1 — widen the frame explicitly** to cover the whole partition regardless of
      order:
      ```sql
      SELECT customer_id, order_id, order_date,
             LAST_VALUE(order_date) OVER (
               PARTITION BY customer_id ORDER BY order_date
               ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING
             ) AS latest_order_date
      FROM orders;
      ```
      Now every row's frame is all 3 rows, so `LAST_VALUE` correctly returns
      `2024-01-10` for all three.
    - **Fix 2 — simpler and more idiomatic:** since order doesn't matter for a plain max,
      just drop `LAST_VALUE`/`ORDER BY` entirely and use `MAX()`:
      ```sql
      SELECT customer_id, order_id, order_date,
             MAX(order_date) OVER (PARTITION BY customer_id) AS latest_order_date
      FROM orders;
      ```
      With no `ORDER BY` in the `OVER(...)`, the default frame is already the entire
      partition (Q10), so this needs no explicit frame clause at all and is the version
      most reviewers would expect to see.

22. ***You need each customer's percentile rank of total spend within their region, then to flag customers in the "top 10%" region-wide. Why can `NTILE(10)` silently mis-tag boundary rows when a region's customer count isn't a clean multiple of 10, and what's a more precise alternative?***
    - **The `NTILE` problem:** `NTILE(n)` always produces exactly `min(n, row_count)`
      non-empty buckets, distributing rows as evenly as possible — it does **not**
      guarantee that bucket 1 represents exactly `1/n` of the partition. For small or
      awkwardly-sized partitions, "bucket 1 of 10" can represent a very different actual
      percentage than 10%.
    - Region X has 7 customers, spend descending: 1000, 900, 800, 700, 600, 500, 400.
      `NTILE(10)` requests 10 buckets but only has 7 rows, so it assigns exactly one row
      per bucket, buckets 1 through 7 (buckets 8–10 simply have no rows). "Bucket 1" is
      the single row with spend 1000 — that's `1/7 ≈ 14.3%` of the region, not 10%.
      Compare Region Y with 50 customers: `NTILE(10)` there divides evenly into 10
      buckets of 5 rows each, so bucket 1 (top 5) really is exactly 10%. Same query,
      same bucket number, two different actual percentages — because `NTILE`'s
      granularity is a function of partition size, not a fixed percentage.
    - **More precise alternative — `PERCENT_RANK()`:** defined as `(rank - 1) /
      (total_rows_in_partition - 1)`, a continuous value from `0` (best row) to `1`
      (worst row) that represents true relative standing rather than a discrete bucket.
      For Region X (n=7, unique values so no ties):
      - Rank 1 (spend 1000): `(1-1)/6 = 0.0`
      - Rank 2 (spend 900): `(2-1)/6 ≈ 0.167`
      - Rank 3 (spend 800): `(3-1)/6 ≈ 0.333`

      Filtering `WHERE percent_rank <= 0.10` (in an outer query, per Q20's rule that
      window function results can't be filtered directly in the same `WHERE`) keeps only
      rank 1 (`0.0 <= 0.10`), since rank 2's `0.167` already exceeds the threshold —
      exactly one customer, deterministically, regardless of how the region's size
      happens to divide against 10.
      ```sql
      WITH ranked AS (
        SELECT customer_id, region, total_spend,
               PERCENT_RANK() OVER (PARTITION BY region ORDER BY total_spend DESC) AS pct_rank
        FROM customer_spend
      )
      SELECT customer_id, region, total_spend
      FROM ranked
      WHERE pct_rank <= 0.10;
      ```
    - **Related but distinct — `CUME_DIST()`:** the cumulative-distribution cousin,
      defined as "fraction of rows at or ahead of the current row in the `ORDER BY`
      sequence" (so it always reaches exactly `1.0` on the last row, unlike
      `PERCENT_RANK` which reaches `1.0` only when there's no tie at the bottom). For
      Region X, the top row's `CUME_DIST` is `1/7 ≈ 0.143` — meaning `CUME_DIST <= 0.10`
      would actually exclude *everyone* in this 7-row region, since even the single best
      row represents 14.3% cumulative share. `PERCENT_RANK` (anchored at 0 for the best
      row) and `CUME_DIST` (anchored at `1/n` for the best row) answer subtly different
      questions — "relative standing" vs. "cumulative share from the top" — and mixing
      them up is an easy way to get an off-by-one-customer result in exactly this kind of
      small-partition edge case.

23. ***Two logically equivalent queries — one using a correlated subquery, one using a window function — both compute "employees earning more than their department's average." Why is the window function version typically far faster at scale, and what would you look for in `EXPLAIN` output to confirm it?***
    - **Correlated subquery's cost profile:** conceptually, the inner `AVG` subquery
      re-evaluates once per outer row, scoped to that row's department. Whether this
      actually costs `O(rows × avg_group_size)` in practice depends entirely on whether
      the optimizer can **decorrelate** it — rewrite it into a single grouped
      aggregation joined back to the outer rows. Mature optimizers (PostgreSQL, SQL
      Server, Oracle) often manage this for simple cases like a per-group `AVG`, but it
      isn't guaranteed for every query shape, and historically MySQL (pre-8.0) was
      notorious for executing correlated subqueries close to literally — once per outer
      row — with no decorrelation at all.
    - **Window function's cost profile:** guaranteed by the standard's execution model,
      not dependent on optimizer cleverness — one pass to compute the partition's
      average via a single sort/hash on the partition key, broadcast back to every row in
      that partition. Cost is predictably `O(n log n)` (dominated by the sort for
      `PARTITION BY`/`ORDER BY`) and doesn't get worse as group sizes grow, unlike an
      un-decorrelated correlated subquery.
    - **What to check in `EXPLAIN` (PostgreSQL terms):** the window function version
      should show a `WindowAgg` node sitting on top of a single `Sort` (or none at all, if
      an index already provides the partition/order columns pre-sorted). The correlated
      subquery version is the one to scrutinize — if you see a `SubPlan` or nested-loop
      structure where the inner query's cost is multiplied by the outer row count, that's
      the un-decorrelated, expensive case; if instead the planner rewrote it into a
      `HashAggregate` feeding a `Hash Join`, the optimizer already did the decorrelation
      for you and the two queries may perform comparably. The takeaway for an interview:
      don't assume either form is fast — the window function's cost is guaranteed by the
      execution model, while the correlated subquery's cost is a bet on the optimizer,
      which is exactly the kind of judgment call worth stating out loud rather than
      assuming.

---

## Related Notes in This Repo
- [`05_Indexes_And_Query_Optimization.md`](05_Indexes_And_Query_Optimization.md) — companion
  SQL interview-prep note on indexes, `EXPLAIN`, and query optimization internals.
- [`04_Common_SQL_Query_Problems.md`](04_Common_SQL_Query_Problems.md) — classic
  "write this query" problems (Nth highest salary, duplicates, running totals, pivoting,
  gaps-and-islands) that lean heavily on the ranking/frame mechanics covered here.
