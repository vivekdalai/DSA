# SQL Fundamentals — Interview Questions

SDE2-level SQL interview prep, organized by topic. Every file is split into **Easy → Medium →
Hard** sections — Easy/Medium cover core mechanics and common gotchas, Hard is scenario/tradeoff
questions (diagnose this production issue, justify this design decision) rather than definitions.

## Suggested Reading Order

1. **[01_Joins](01_Joins.md)** — multi-table querying, the foundation everything else builds on.
2. **[02_Query_Execution_Order_GroupBy_Having](02_Query_Execution_Order_GroupBy_Having.md)** —
   how a query actually executes clause by clause; explains *why* joins/filters/aggregates
   interact the way they do.
3. **[03_Window_Functions](03_Window_Functions.md)** — ranking, running totals, and per-row
   calculations without collapsing rows the way `GROUP BY` does.
4. **[04_Common_SQL_Query_Problems](04_Common_SQL_Query_Problems.md)** — checkpoint: classic
   "write this query" problems (Nth highest salary, duplicates, gaps-and-islands, sessionization)
   that lean directly on 01–03.
5. **[05_Indexes_And_Query_Optimization](05_Indexes_And_Query_Optimization.md)** — now that you
   can write correct queries, learn to make them fast: B-tree indexes, composite index prefix
   rules, reading `EXPLAIN`.
6. **[06_Transactions_ACID_And_Isolation_Levels](06_Transactions_ACID_And_Isolation_Levels.md)** —
   correctness under concurrency: ACID, isolation levels, locking, write skew.
7. **[07_Normalization](07_Normalization.md)** — schema design: normal forms, and when to
   deliberately denormalize.

Files 1–4 are about writing correct, expressive queries. Files 5–7 shift to systems-level
concerns — performance, concurrency, and schema design — the kind of judgment-call questions an
SDE2 round is more likely to probe once basic query-writing is a given.
