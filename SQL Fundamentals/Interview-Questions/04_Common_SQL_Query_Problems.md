# Common SQL Query Problems Interview Questions

Classic "write this query" interview problems. Each entry gives the problem statement,
sample data, the query, and why it works (or why a tempting naive version doesn't).

Questions are organized by difficulty: **Easy** (single-clause, no real gotcha),
**Medium** (the standard write-this-query problems — ties, dedup, per-group
comparisons, pivoting), **Hard** (SDE2-level problems: gaps-and-islands, sessionization,
medians, recursive hierarchies). This file leans Medium/Hard by nature, since almost
every classic "write this query" problem has at least one non-obvious edge case baked
into it — don't read the short Easy section as the whole picture.

---

## Easy

### Find Duplicate Rows
1. ***Problem: find rows that are duplicates on some column (e.g. email).***
    - Sample `Users`:

      | ID | Email |
      |---|---|
      | 1 | a@x.com |
      | 2 | b@x.com |
      | 3 | a@x.com |
      | 4 | c@x.com |
      | 5 | a@x.com |

    ```sql
    SELECT Email, COUNT(*) AS Occurrences
    FROM Users
    GROUP BY Email
    HAVING COUNT(*) > 1;
    ```
    - Result: `a@x.com, 3`. `GROUP BY Email` collapses rows sharing the same email into
      one group per distinct value; `HAVING COUNT(*) > 1` filters to groups with more
      than one member *after* aggregation (a plain `WHERE` can't reference an aggregate
      like `COUNT(*)`, since `WHERE` is evaluated before grouping happens — that's
      exactly why `HAVING` exists as a separate post-aggregation filter clause).

### Running Total / Cumulative Sum by Date
2. ***Problem: compute a running total of daily sales.***
    - Sample `DailySales`:

      | SaleDate | Amount |
      |---|---|
      | 2024-01-01 | 100 |
      | 2024-01-02 | 150 |
      | 2024-01-03 | 200 |
      | 2024-01-04 | 50 |

    ```sql
    SELECT SaleDate, Amount,
           SUM(Amount) OVER (ORDER BY SaleDate) AS RunningTotal
    FROM DailySales;
    ```
    - Result: `100, 250, 450, 500` — each row's `RunningTotal` is the sum of `Amount`
      from the first row through the current one. `SUM(...) OVER (ORDER BY ...)` with no
      explicit frame defaults to `RANGE BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW`,
      i.e. "everything up to and including rows that tie with the current `ORDER BY`
      value." See [`03_Window_Functions.md`](03_Window_Functions.md) for frame clause
      (`ROWS` vs `RANGE`) semantics in depth — and see Q14 below for what breaks when
      `SaleDate` isn't unique.

---

## Medium

### Nth Highest Salary
3. ***Problem: find the Nth highest salary — parameterized, not hardcoded to "2nd".***
    - Sample `Employees`:

      | EmployeeID | Name | Salary |
      |---|---|---|
      | 1 | Alice | 90000 |
      | 2 | Bob | 85000 |
      | 3 | Carol | 90000 |
      | 4 | Dave | 80000 |
      | 5 | Eve | 85000 |

    - Distinct salary values, descending: 90000, 85000, 80000 -> the 2nd highest is
      **85000**.

4. ***Why does a naive `LIMIT`/`OFFSET` without `DISTINCT` silently break on ties?***
    ```sql
    -- BROKEN for "2nd highest" when there are duplicate top salaries:
    SELECT Salary FROM Employees ORDER BY Salary DESC LIMIT 1 OFFSET 1;
    ```
    - Sorted descending by row (not by distinct value): `90000` (Alice), `90000`
      (Carol), `85000` (Bob), `85000` (Eve), `80000` (Dave). `OFFSET 1 LIMIT 1` skips
      exactly one *row* and returns the next one — which is Carol's `90000`, not
      `85000`. The bug: `LIMIT`/`OFFSET` counts physical rows, but "2nd highest salary"
      means the 2nd *distinct* value — a tie at the top silently shifts everything below
      it out of position.

5. ***Portable, correct `OFFSET`/`FETCH` version (ANSI SQL:2008 standard).***
    ```sql
    SELECT DISTINCT Salary
    FROM Employees
    ORDER BY Salary DESC
    OFFSET (2 - 1) ROWS FETCH NEXT 1 ROWS ONLY;  -- N = 2 here
    ```
    - `DISTINCT` collapses the tied `90000` rows into one value first, so the ordered
      distinct list is `90000, 85000, 80000` — offsetting past 1 distinct value and
      taking the next 1 correctly lands on `85000`. Supported by PostgreSQL, SQL Server
      2012+, Oracle 12c+, DB2.

6. ***`LIMIT`/`OFFSET` equivalent.***
    ```sql
    -- MySQL / PostgreSQL / SQLite:
    SELECT DISTINCT Salary
    FROM Employees
    ORDER BY Salary DESC
    LIMIT 1 OFFSET (2 - 1);  -- N = 2 here
    ```
    - Same fix as Q5 (the `DISTINCT` is what actually matters), just with the
      non-standard but widely-supported `LIMIT n OFFSET m` syntax instead of the ANSI
      `OFFSET ... FETCH ...` form. Not supported by SQL Server (pre-2012 used `TOP`,
      which has its own tie-handling caveats).

7. ***`DENSE_RANK()` version — and why it handles ties correctly by construction.***
    ```sql
    SELECT Salary
    FROM (
        SELECT Salary,
               DENSE_RANK() OVER (ORDER BY Salary DESC) AS SalaryRank
        FROM Employees
    ) ranked
    WHERE SalaryRank = 2;  -- N = 2 here
    ```
    - `DENSE_RANK()` assigns the **same rank** to tied rows and increments the rank by
      exactly 1 for the next *distinct* value (unlike `RANK()`, which skips ranks after a
      tie, e.g. `1, 1, 3, 3, 5`). Walking the sample data: Alice and Carol (90000) both
      get rank 1; Bob and Eve (85000) both get rank 2; Dave (80000) gets rank 3.
      `WHERE SalaryRank = 2` returns Bob and Eve — both correctly identified as "the 2nd
      highest salary," and unlike the `LIMIT`/`OFFSET` approaches, this naturally returns
      *every* employee at that salary, not just one row. See
      [`03_Window_Functions.md`](03_Window_Functions.md) for the full `RANK()` vs
      `DENSE_RANK()` vs `ROW_NUMBER()` comparison.

### Delete Duplicate Rows, Keeping One Copy
8. ***Problem: delete duplicate rows on `Email`, keeping exactly one (the lowest `ID`).***
    - Same `Users` table as Q1. Want to keep `ID=1` for `a@x.com` and delete `ID=3` and
      `ID=5`.
    ```sql
    WITH ranked AS (
        SELECT ID, Email,
               ROW_NUMBER() OVER (PARTITION BY Email ORDER BY ID) AS rn
        FROM Users
    )
    DELETE FROM Users
    WHERE ID IN (SELECT ID FROM ranked WHERE rn > 1);
    ```
    - `ROW_NUMBER() OVER (PARTITION BY Email ORDER BY ID)` numbers rows *within each
      email group* starting at 1, in ascending `ID` order: `a@x.com` -> ID 1 gets
      `rn=1`, ID 3 gets `rn=2`, ID 5 gets `rn=3`; `b@x.com` -> ID 2 gets `rn=1`;
      `c@x.com` -> ID 4 gets `rn=1`. Deleting every row where `rn > 1` removes IDs 3 and
      5 (the "extra" copies of `a@x.com`) and leaves exactly one row per distinct email.
      See [`03_Window_Functions.md`](03_Window_Functions.md) for `ROW_NUMBER()`/
      `PARTITION BY` mechanics in more depth.

9. ***Why can't you just write `DELETE ... GROUP BY` directly?***
    - Standard SQL's `DELETE` statement doesn't support a `GROUP BY`/`HAVING` clause at
      all — `DELETE` operates on individual rows matched by a `WHERE` predicate, while
      `GROUP BY`/`HAVING` is a `SELECT`-only construct for producing an *aggregated
      result set*. There's no direct syntax to express "delete all but one row per
      group" as a single grouped statement — you have to first turn "keep one per group"
      into a per-row, filterable condition (which is exactly what `ROW_NUMBER()` does in
      Q8: it converts a group-level rule into a row-level `rn` value you can filter on),
      and only then delete based on that.

10. ***MySQL gotcha: "You can't specify target table for update in FROM clause".***
    ```sql
    -- MySQL: wrap the ranked subquery in an extra derived table to work around it:
    DELETE FROM Users
    WHERE ID IN (
        SELECT ID FROM (
            SELECT ID, ROW_NUMBER() OVER (PARTITION BY Email ORDER BY ID) AS rn
            FROM Users
        ) AS t
        WHERE t.rn > 1
    );
    ```
    - Some MySQL versions reject a `DELETE` whose subquery selects directly from the
      same table being deleted from. Wrapping the subquery in an extra derived table
      (`AS t`) forces MySQL to materialize it first, sidestepping the restriction.
      PostgreSQL and SQL Server don't need this workaround for the CTE version in Q8.

### Employees Earning More Than Their Department's Average
11. ***Problem: find employees whose salary exceeds their own department's average.***
    - Sample `Employees`:

      | ID | Name | DeptID | Salary |
      |---|---|---|---|
      | 1 | Alice | D1 | 90000 |
      | 2 | Bob | D1 | 70000 |
      | 3 | Carol | D1 | 80000 |
      | 4 | Dave | D2 | 60000 |
      | 5 | Eve | D2 | 40000 |

    - D1 average = (90000+70000+80000)/3 = 80000 -> only Alice (90000) exceeds it.
      D2 average = (60000+40000)/2 = 50000 -> only Dave (60000) exceeds it. Expected
      result: **Alice, Dave**.

12. ***Correlated subquery approach.***
    ```sql
    SELECT e.Name, e.DeptID, e.Salary
    FROM Employees e
    WHERE e.Salary > (
        SELECT AVG(e2.Salary)
        FROM Employees e2
        WHERE e2.DeptID = e.DeptID
    );
    ```
    - The inner query is *correlated* — it references `e.DeptID` from the outer row, so
      conceptually it re-evaluates once per outer row, computing that row's own
      department's average fresh each time. Alice: dept average 80000, 90000 > 80000 ->
      kept. Carol: 80000 > 80000 is false (not strictly greater) -> excluded. Dave: dept
      average 50000, 60000 > 50000 -> kept.

13. ***Window function approach — and how it compares.***
    ```sql
      WITH EmployeeAvgs AS (
        SELECT Name, DeptID, Salary,
              AVG(Salary) OVER (PARTITION BY DeptID) AS DeptAvg
        FROM Employees
        )
        SELECT Name, DeptID, Salary
        FROM EmployeeAvgs
        WHERE Salary > DeptAvg;
    ```
    - `AVG(Salary) OVER (PARTITION BY DeptID)` computes the average *within each
      department* but, unlike `GROUP BY`, doesn't collapse the rows — every employee row
      keeps its own identity and simply gets its department's average attached as an
      extra column. Same result: Alice (90000 > 80000) and Dave (60000 > 50000).
    - **Comparison:** the correlated subquery re-runs (conceptually) once per outer row,
      which many optimizers can rewrite but isn't guaranteed to; the window function
      computes each partition's average in a single pass over the data and broadcasts it
      to every row in that partition, which is generally the more efficient and more
      idiomatic choice when you want the aggregate visible *alongside* each row rather
      than only used as a filter threshold. Note: window functions need MySQL 8.0+
      (unavailable on 5.7 and earlier, where the correlated subquery is the only
      option).

### Running Total Pitfall
14. ***Pitfall: what happens if `SaleDate` (from Q2's running total) isn't unique?***
    - With the default `RANGE` frame, *all* rows sharing the same `ORDER BY` value are
      treated as one peer group and get the **same** cumulative total (the frame
      boundary is defined by value, not by physical row position) — two sales on the
      same date would both show the total *including* each other, not a strict
      row-by-row accumulation. Fix: either add a tiebreaker to `ORDER BY` (e.g.
      `ORDER BY SaleDate, ID`) or be explicit about wanting row-by-row behavior with
      `ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW` instead of the default `RANGE`
      frame.

### Pivot Rows into Columns
15. ***Problem: turn per-quarter rows into one row per employee with a column per quarter.***
    - Sample `Sales`:

      | EmployeeName | Quarter | Amount |
      |---|---|---|
      | Alice | Q1 | 100 |
      | Alice | Q2 | 150 |
      | Bob | Q1 | 200 |
      | Bob | Q2 | 250 |

    ```sql
    SELECT
        EmployeeName,
        SUM(CASE WHEN Quarter = 'Q1' THEN Amount ELSE 0 END) AS Q1,
        SUM(CASE WHEN Quarter = 'Q2' THEN Amount ELSE 0 END) AS Q2
    FROM Sales
    GROUP BY EmployeeName;
    ```
    - Result: `Alice, 100, 150` and `Bob, 200, 250`. For each `EmployeeName` group, the
      `CASE` expression evaluates per row — it contributes `Amount` to the column
      matching that row's `Quarter` and `0` to every other quarter's column — then
      `SUM()` collapses each group down to a single row, with each `CASE`/`SUM` pair
      acting as one manually-built pivoted column. Use `MAX(...)` instead of `SUM(...)`
      when there's at most one matching row per group per category (both give the same
      result in that case; `SUM` is the safer default when duplicates are possible and
      should be added together). This manual pattern is the portable choice since not
      every SQL dialect has a native `PIVOT` operator (SQL Server and Oracle do;
      PostgreSQL and MySQL don't).

---

## Hard

### Gaps and Islands: Consecutive Date Ranges of Activity
16. ***Problem: for each user, collapse consecutive daily logins into date ranges ("islands") — e.g. logins on Jan 1-3 and Jan 5-6 should produce two separate ranges, not one.***
    - Sample `Logins` (one row per user per day they logged in):

      | UserID | LoginDate |
      |---|---|
      | 1 | 2024-01-01 |
      | 1 | 2024-01-02 |
      | 1 | 2024-01-03 |
      | 1 | 2024-01-05 |
      | 1 | 2024-01-06 |
      | 2 | 2024-01-01 |
      | 2 | 2024-01-03 |
      | 2 | 2024-01-04 |

    ```sql
    WITH ranked AS (
        SELECT UserID, LoginDate,
               ROW_NUMBER() OVER (PARTITION BY UserID ORDER BY LoginDate) AS rn
        FROM Logins
    ),
    grouped AS (
        SELECT UserID, LoginDate,
               -- Postgres: LoginDate - rn * INTERVAL '1 day'
               -- MySQL:    DATE_SUB(LoginDate, INTERVAL rn DAY)
               -- SQL Srv:  DATEADD(day, -rn, LoginDate)
               DATE_SUB(LoginDate, INTERVAL rn DAY) AS IslandGroup
        FROM ranked
    )
    SELECT UserID,
           MIN(LoginDate) AS IslandStart,
           MAX(LoginDate) AS IslandEnd,
           COUNT(*) AS DaysActive
    FROM grouped
    GROUP BY UserID, IslandGroup
    ORDER BY UserID, IslandStart;
    ```
    - **The trick:** within one unbroken run of consecutive dates, `LoginDate` and `rn`
      (the row's position in that user's date-ordered sequence) both increase by exactly
      1 per row — so `LoginDate - rn` is *constant* across the whole run, and changes the
      moment a day is skipped. That constant becomes a free grouping key for "which
      island does this row belong to," with no explicit gap-detection logic needed.
    - Trace for User 1 (dates ordered 01-01, 01-02, 01-03, 01-05, 01-06 -> rn 1,2,3,4,5):
      `01-01 - 1 day = 12-31`, `01-02 - 2 days = 12-31`, `01-03 - 3 days = 12-31` (all
      three share `IslandGroup = 12-31` -> one island, 01-01 to 01-03). Then
      `01-05 - 4 days = 01-01`, `01-06 - 5 days = 01-01` (a *different* group -> second
      island, 01-05 to 01-06 — the group value jumped because the skipped `01-04` broke
      the run).
    - Trace for User 2 (dates 01-01, 01-03, 01-04 -> rn 1,2,3): `01-01 - 1 = 12-31`
      (island of 1 day, alone in its group). `01-03 - 2 = 01-01`, `01-04 - 3 = 01-01`
      (second island, 01-03 to 01-04).
    - Result:

      | UserID | IslandStart | IslandEnd | DaysActive |
      |---|---|---|---|
      | 1 | 2024-01-01 | 2024-01-03 | 3 |
      | 1 | 2024-01-05 | 2024-01-06 | 2 |
      | 2 | 2024-01-01 | 2024-01-01 | 1 |
      | 2 | 2024-01-03 | 2024-01-04 | 2 |

### Gaps and Islands: Longest Consecutive Login Streak
17. ***Problem: extending Q16, find just each user's single longest consecutive-day login streak (start date, end date, and length).***
    - Sample `Logins` — reuse User 1 and User 2 from Q16, plus a third user:

      | UserID | LoginDate |
      |---|---|
      | 3 | 2024-01-01 |
      | 3 | 2024-01-02 |
      | 3 | 2024-01-03 |
      | 3 | 2024-01-04 |
      | 3 | 2024-01-05 |

      (User 1's islands from Q16 are length 3 and length 2; User 2's are length 1 and
      length 2; User 3 is one unbroken island of length 5.)

    ```sql
    WITH ranked AS (
        SELECT UserID, LoginDate,
               ROW_NUMBER() OVER (PARTITION BY UserID ORDER BY LoginDate) AS rn
        FROM Logins
    ),
    grouped AS (
        SELECT UserID, LoginDate,
               DATE_SUB(LoginDate, INTERVAL rn DAY) AS IslandGroup
        FROM ranked
    ),
    islands AS (
        SELECT UserID, IslandGroup,
               MIN(LoginDate) AS StreakStart,
               MAX(LoginDate) AS StreakEnd,
               COUNT(*) AS StreakLength
        FROM grouped
        GROUP BY UserID, IslandGroup
    ),
    islands_ranked AS (
        SELECT UserID, StreakStart, StreakEnd, StreakLength,
               ROW_NUMBER() OVER (PARTITION BY UserID ORDER BY StreakLength DESC) AS rn
        FROM islands
    )
    SELECT UserID, StreakStart, StreakEnd, StreakLength
    FROM islands_ranked
    WHERE rn = 1;
    ```
    - **Two-stage window function use:** the first `ROW_NUMBER()` (in `ranked`) does the
      island-detection trick from Q16; the CTE then aggregates each island down to one
      row with its length; a *second*, independent `ROW_NUMBER()` (in `islands_ranked`)
      ranks each user's islands by length and keeps only the longest one — the standard
      "top-1-per-group" pattern from
      [`03_Window_Functions.md`](03_Window_Functions.md) Q17, just applied on top of
      already-derived island rows instead of raw data.
    - Trace: User 1's islands are length 3 (01-01 to 01-03) and length 2 (01-05 to
      01-06) -> longest is 3. User 2's islands are length 1 and length 2 -> longest is 2.
      User 3 has a single island of length 5 -> trivially the longest.
    - Result:

      | UserID | StreakStart | StreakEnd | StreakLength |
      |---|---|---|---|
      | 1 | 2024-01-01 | 2024-01-03 | 3 |
      | 2 | 2024-01-03 | 2024-01-04 | 2 |
      | 3 | 2024-01-01 | 2024-01-05 | 5 |

### Gaps and Islands: Finding Missing IDs in a Sequence
18. ***Problem: `OrderID` is supposed to be a gapless sequence but isn't — find every missing range ("gap," the mirror-image problem of Q16-17's "islands").***
    - Sample `Orders`:

      | OrderID |
      |---|
      | 1 |
      | 2 |
      | 3 |
      | 5 |
      | 6 |
      | 9 |
      | 10 |
      | 11 |
      | 15 |

    ```sql
    WITH ordered AS (
        SELECT OrderID,
               LEAD(OrderID) OVER (ORDER BY OrderID) AS NextOrderID
        FROM Orders
    )
    SELECT OrderID + 1 AS GapStart, NextOrderID - 1 AS GapEnd
    FROM ordered
    WHERE NextOrderID - OrderID > 1;
    ```
    - **The trick:** `LEAD()` pulls each row's immediate successor in `OrderID` order
      onto the same row, with no self-join. If the sequence were gapless, every row's
      successor would be exactly `OrderID + 1`; whenever the gap between a row and its
      successor is more than 1, everything strictly between them is missing.
    - Trace: `1 -> 2` (diff 1, no gap), `2 -> 3` (diff 1, no gap), `3 -> 5` (diff 2 ->
      gap `4-4`), `5 -> 6` (diff 1, no gap), `6 -> 9` (diff 3 -> gap `7-8`), `9 -> 10`
      (diff 1, no gap), `10 -> 11` (diff 1, no gap), `11 -> 15` (diff 4 -> gap `12-14`).
      The last row (`15`) has `NextOrderID = NULL` (no successor) — `NULL - 15` is
      `NULL`, and `NULL > 1` is neither true nor false, so `WHERE` correctly drops that
      row rather than reporting a false trailing gap.
    - Result:

      | GapStart | GapEnd |
      |---|---|
      | 4 | 4 |
      | 7 | 8 |
      | 12 | 14 |

### Sessionization: Grouping Events by a Time-Gap Threshold
19. ***Problem: given raw page-view timestamps, split each user's events into "sessions," starting a new session whenever more than 30 minutes elapse since the previous event. How many sessions did each user have, and when did each start/end?***
    - Sample `PageViews`:

      | UserID | ViewTime |
      |---|---|
      | 1 | 09:00 |
      | 1 | 09:10 |
      | 1 | 09:45 |
      | 1 | 09:50 |
      | 1 | 11:00 |
      | 2 | 10:00 |
      | 2 | 10:05 |
      | 2 | 10:08 |

    ```sql
    WITH gaps AS (
        SELECT UserID, ViewTime,
               TIMESTAMPDIFF(
                   MINUTE,
                   LAG(ViewTime) OVER (PARTITION BY UserID ORDER BY ViewTime),
                   ViewTime
               ) AS GapMinutes
        FROM PageViews
    ),
    flags AS (
        SELECT UserID, ViewTime,
               CASE WHEN GapMinutes IS NULL OR GapMinutes > 30 THEN 1 ELSE 0 END AS IsNewSession
        FROM gaps
    ),
    sessions AS (
        SELECT UserID, ViewTime,
               SUM(IsNewSession) OVER (
                   PARTITION BY UserID ORDER BY ViewTime
                   ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
               ) AS SessionID
        FROM flags
    )
    SELECT UserID, SessionID,
           MIN(ViewTime) AS SessionStart,
           MAX(ViewTime) AS SessionEnd,
           COUNT(*) AS ViewsInSession
    FROM sessions
    GROUP BY UserID, SessionID
    ORDER BY UserID, SessionID;
    ```
    - **A different island-detection technique than Q16-17:** instead of the
      `value - ROW_NUMBER()` constant trick (which only works cleanly for evenly-spaced
      sequences like calendar days), this uses `LAG()` to compare each event directly to
      its *immediate predecessor's timestamp*, flags a "session boundary" whenever the
      gap exceeds the threshold (or there is no predecessor — the first row is always a
      new session), and then turns those 0/1 flags into a running session counter with a
      cumulative `SUM() OVER (... ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW)`.
      Every row keeps accumulating the same `SessionID` until the next boundary flag
      bumps it. (`TIMESTAMPDIFF` is MySQL syntax; Postgres equivalent:
      `EXTRACT(EPOCH FROM (ViewTime - LAG(ViewTime) OVER (...))) / 60`.)
    - Trace for User 1 (chronological order 09:00, 09:10, 09:45, 09:50, 11:00):
      09:00 has no predecessor -> `IsNewSession=1`, running `SessionID=1`. 09:10: gap
      from 09:00 is 10 min (<=30) -> `IsNewSession=0`, stays `SessionID=1`. 09:45: gap
      from 09:10 is 35 min (>30) -> `IsNewSession=1`, `SessionID` becomes 2. 09:50: gap
      from 09:45 is 5 min -> stays `SessionID=2`. 11:00: gap from 09:50 is 70 min (>30)
      -> `IsNewSession=1`, `SessionID` becomes 3. Three sessions: `{09:00,09:10}`,
      `{09:45,09:50}`, `{11:00}`.
    - Trace for User 2 (10:00, 10:05, 10:08): gaps are 5 min and 3 min, both well under
      30 -> all three rows stay in `SessionID=1`. One session covering all three views.
    - Result:

      | UserID | SessionID | SessionStart | SessionEnd | ViewsInSession |
      |---|---|---|---|---|
      | 1 | 1 | 09:00 | 09:10 | 2 |
      | 1 | 2 | 09:45 | 09:50 | 2 |
      | 1 | 3 | 11:00 | 11:00 | 1 |
      | 2 | 1 | 10:00 | 10:08 | 3 |

### Median Without a Native `MEDIAN()`/`PERCENTILE_CONT()` Function
20. ***Problem: compute the median salary per department. Most engines (MySQL, older Postgres) have no built-in `MEDIAN()`, and `PERCENTILE_CONT` isn't universally available either — do it with `ROW_NUMBER()`/`COUNT()` so it's portable.***
    - Sample `Employees`:

      | ID | Name | DeptID | Salary |
      |---|---|---|---|
      | 1 | Alice | D1 | 90000 |
      | 2 | Bob | D1 | 70000 |
      | 3 | Carol | D1 | 80000 |
      | 4 | Dan | D1 | 60000 |
      | 5 | Eve | D2 | 50000 |
      | 6 | Frank | D2 | 55000 |
      | 7 | Grace | D2 | 65000 |

    - D1 has 4 employees (even count) -> median is the average of the 2 middle values.
      D2 has 3 employees (odd count) -> median is the single middle value.

    ```sql
    WITH ranked AS (
        SELECT DeptID, Salary,
               ROW_NUMBER() OVER (PARTITION BY DeptID ORDER BY Salary) AS AscRn,
               COUNT(*) OVER (PARTITION BY DeptID) AS Cnt
        FROM Employees
    )
    SELECT DeptID, AVG(Salary * 1.0) AS MedianSalary
    FROM ranked
    WHERE AscRn IN (FLOOR((Cnt + 1) / 2.0), CEILING((Cnt + 1) / 2.0))
    GROUP BY DeptID;
    ```
    - **Why `FLOOR((Cnt+1)/2.0)` / `CEILING((Cnt+1)/2.0)`:** for an odd count these two
      expressions land on the *same* integer (the single middle position); for an even
      count they land on the two adjacent middle positions — one formula handles both
      parities without an `IF`/`CASE` branch. Casting to `2.0` (not `2`) forces
      floating-point division so the formula isn't silently truncated by integer
      division on engines (like Postgres) where `int / int` truncates.
    - Trace D1 (4 rows, sorted ascending by salary: Dan 60000 `rn=1`, Bob 70000 `rn=2`,
      Carol 80000 `rn=3`, Alice 90000 `rn=4`; `Cnt=4`): `FLOOR(5/2.0)=FLOOR(2.5)=2`,
      `CEILING(5/2.0)=3` -> keep `rn IN (2,3)` -> Bob (70000) and Carol (80000) ->
      `AVG = 75000`.
    - Trace D2 (3 rows: Eve 50000 `rn=1`, Frank 55000 `rn=2`, Grace 65000 `rn=3`;
      `Cnt=3`): `FLOOR(4/2.0)=FLOOR(2.0)=2`, `CEILING(4/2.0)=2` -> keep `rn=2` only ->
      Frank (55000) -> `AVG = 55000`.
    - Result:

      | DeptID | MedianSalary |
      |---|---|
      | D1 | 75000 |
      | D2 | 55000 |

### Recursive CTE: Team Size Under Each Manager
21. ***Problem: given a self-referencing `Employees(EmployeeID, Name, ManagerID)` table, find each manager's total team size — direct reports plus indirect reports at any depth.***
    - Sample `Employees`:

      | EmployeeID | Name | ManagerID |
      |---|---|---|
      | 1 | Alice | NULL |
      | 2 | Bob | 1 |
      | 3 | Carol | 1 |
      | 4 | Dave | 2 |
      | 5 | Eve | 2 |
      | 6 | Frank | 3 |

      (Alice is the top of the org; Bob and Carol report to Alice; Dave and Eve report
      to Bob; Frank reports to Carol.)

    ```sql
    WITH RECURSIVE OrgChain AS (
        -- Anchor: every direct manager -> report pair.
        SELECT ManagerID, EmployeeID AS ReportID
        FROM Employees
        WHERE ManagerID IS NOT NULL

        UNION ALL

        -- Recursive step: for every (manager, report) pair found so far, pull in that
        -- report's own direct reports, still attributed to the original manager.
        SELECT oc.ManagerID, e.EmployeeID AS ReportID
        FROM OrgChain oc
        JOIN Employees e ON e.ManagerID = oc.ReportID
    )
    SELECT ManagerID, COUNT(*) AS TotalTeamSize
    FROM OrgChain
    GROUP BY ManagerID
    ORDER BY ManagerID;
    ```
    - **Why recursion is needed:** a plain `JOIN` can only walk one level of the
      hierarchy at a time (e.g. "who reports directly to Bob"); "everyone under Alice at
      any depth" requires repeatedly re-joining until no new rows appear, which is
      exactly what `WITH RECURSIVE`'s anchor + recursive term + implicit fixpoint
      termination does. The recursive term only sees the *previous iteration's* output
      (not the full accumulated table), so each pass extends the chain by exactly one
      more level.
    - Trace: **Anchor** produces the 5 direct pairs: `(1,2) (1,3) (2,4) (2,5) (3,6)`.
      **Iteration 1** joins each of those on `ReportID` to find further reports: report
      2 (Bob) has reports 4 and 5 -> adds `(1,4) (1,5)`; report 3 (Carol) has report 6 ->
      adds `(1,6)`; reports 4, 5, 6 have no reports of their own -> nothing else. New
      rows this pass: `(1,4) (1,5) (1,6)`. **Iteration 2** joins those on `ReportID`
      (4, 5, 6) -> none of them manage anyone -> no new rows -> recursion terminates.
      Accumulated total: 8 rows — `(1,2) (1,3) (2,4) (2,5) (3,6) (1,4) (1,5) (1,6)`.
    - `GROUP BY ManagerID`: ManagerID=1 (Alice) appears in 5 rows — `(1,2) (1,3) (1,4)
      (1,5) (1,6)` — team size 5 (everyone else in the company). ManagerID=2 (Bob)
      appears in 2 rows — `(2,4) (2,5)` — team size 2. ManagerID=3 (Carol) appears in 1
      row — `(3,6)` — team size 1.
    - Result (Dave, Eve, and Frank manage nobody, so they correctly have **no row at
      all** here rather than a `0` — add a `LEFT JOIN` back to the full `Employees` list
      if explicit zeroes are required for leaf employees):

      | ManagerID | TotalTeamSize |
      |---|---|
      | 1 | 5 |
      | 2 | 2 |
      | 3 | 1 |

---

## Related Notes in This Repo
- [`03_Window_Functions.md`](03_Window_Functions.md) — deeper dive on `ROW_NUMBER()`,
  `RANK()`, `DENSE_RANK()`, and window frame (`ROWS`/`RANGE`) semantics used throughout
  this file, plus the "top N per group" pattern reused in Q17.
- [`06_Transactions_ACID_And_Isolation_Levels.md`](06_Transactions_ACID_And_Isolation_Levels.md)
  — concurrency/consistency side of relational databases.
- [`07_Normalization.md`](07_Normalization.md) — schema design side of relational
  databases.
