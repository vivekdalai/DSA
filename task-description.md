# Task Description: Dynamic Programming Notes Playbook

Purpose
- Create a consistent, interview-focused set of Dynamic Programming (DP) notes in Markdown.
- Each note explains the problem, derives the state and recurrence, and provides clean Java implementations (top-down/bottom-up/space-optimized where applicable), plus dry run, edge cases, and patterns.
- The goal is quick recall and reliable templates for coding interviews and practice.

Audience
- Interview prep (SDE roles)
- Competitive programming refresher
- Engineers wanting canonical DP templates and pitfalls

Repository Conventions

1) File naming and numbering
- Use the prefix DP_XX_ with a 2-digit sequence number, then a brief kebab/pascal case title.
  - Example: DP_40_Palindrome_Partitioning_II_Min_Cuts.md
- For related problems, use suffixed letters:
  - Example: DP_04_Coin_Change_Min_Coins.md and DP_04_B_Coin_Change_Number_of_Ways.md
- Keep one problem/topic per file.

2) Section layout for every note (required)
Use this exact outline and emojis for fast scanning:
- Title (H2): 0-based sequence and short name
- Generated on: timestamp with timezone (IST)
- 1. Problem Understanding (🧠)
- 2. State Definition (🪜)
- 3. Recurrence Relation (🔁)
- 4. Implementation(s) in Java (💻)
  - Prefer: 4A. Top-Down Memo + 4B. Bottom-Up + 4C. Space-Optimized (when applicable)
- 5. Dry Run / Example (🔎)
- 6. Pattern Recognition (🏷)
- 7. Edge Cases and Pitfalls (🔄)
- 8. Takeaway (✅)

3) Code style and language
- Language: Java (JDK 8+ compatible)
- Class naming: concise and descriptive per variant (e.g., LCSMemo, LCSTab, StockIII_Tab)
- Use clear variable names: i, j, n, m, dp, prev/curr, buy/sell, etc.
- Include minimal main for sample I/O only when clarifying usage.
- Complexity must be present: Time O(...), Space O(...). Mention space optimization if possible.

4) Content standards
- Explain the intuition before equations.
- Derive the state carefully; prefer prefix-based dp[i][j] for strings; interval [i..j] for segments; parity/buy/sell for stocks.
- Present the recurrence exactly, with base cases.
- Dry run: with small numbers that expose transitions.
- Include pitfalls: common off-by-ones, loop order (e.g., combinations vs permutations), overflow considerations.
- Variants/relations: e.g., LCS ↔ SCS, LPS ↔ LCS(s, reverse(s)), Edit Distance ↔ LCS costs, Weighted Interval Scheduling ↔ binary search + dp.

5) Formatting rules
- Break sections with a horizontal rule (------------------------------------------------------------------------).
- Use bullet lists for examples and pitfalls.
- Use fenced code blocks with language hint ```java.
- Keep lines ≤ 100–120 characters where possible.
- Avoid extraneous prose; be crisp and technical.

6) When to include extras
- Reconstruction: if printing the sequence/solution is a common variant (e.g., Print LCS, SCS print, LIS reconstruction).
- Space optimization: when trivial and interview-useful (rolling arrays, 1D DP).
- Alternate approach: only if it illuminates a pattern (e.g., Set-DP for Frog 403, greedy proof for Substring vs Subsequence, op-count DP for Stocks IV).

Checklist for Adding a New Note

1) Identify category
- Arrays/1D: Kadane, House Robber, Delete and Earn, Candy
- Strings/2D: LCS, LPS, Edit Distance, Regex, Distinct Subsequences, Interleaving, SCS
- Grid/2D: Unique Paths, Obstacles, Min Path Sum, Triangle/Matrix rolling DP
- Interval: MCM, Palindrome Partitioning II, Burst Balloons, Boolean Parenthesization
- Scheduling: Weighted Interval Scheduling (Job Scheduling)
- Stocks: I/II/III/Cooldown/Fee/IV (K transactions)
- Trees: House Robber III, Binary Tree Max Path Sum
- Special: Frog problems (403 and round-trip no-revisit), Coin Change (min/ways)

2) Write the note using the standard outline
- Problem Understanding: define constraints, classic examples; disambiguate similar problems.
- State Definition: dp meaning in one sentence; dimensions and index semantics.
- Recurrence: base cases first, then transitions. If interval DP, specify fill order (length→i→j).
- Implementation(s): Java code, readable and minimal; include memo/tabulation; show O(1) space where natural.
- Dry Run: pick small but representative inputs.
- Pattern Recognition: name, family, triggers (“if you see X, reach for Y”).
- Pitfalls: include loop directions (coin-outer vs value-outer), boundary handling, overflows.
- Takeaway: bullet the key memory hooks.

3) Validate
- Manually test with 1–2 trivial and 1–2 non-trivial inputs from the example section.
- Re-check base cases: dp[0][*], dp[*][0], empty strings, single element.
- Confirm complexity claims; consider optimization note if easy.

Templates (Copy/Paste)

Title and header
## NN - Descriptive Name (Optional Tag/LC ID)

Generated on: YYYY-MM-DD HH:MM:SS (IST)

------------------------------------------------------------------------

🧠 1. Problem Understanding
- One- to three-sentence summary.
- 1–2 example IO pairs with notes on why.

------------------------------------------------------------------------

🪜 2. State Definition
- dp[...] meaning in one sentence.
- Dimensions and index conventions (0/1-based).

------------------------------------------------------------------------

🔁 3. Recurrence Relation
- Base cases (explicit).
- Transitions (clear and exact).
- Fill order if needed (interval length or outer coin vs inner value).

------------------------------------------------------------------------

💻 4. Implementation(s) (Java)
- 4A. Top-Down + memo (if helpful)
- 4B. Bottom-Up Tabulation (canonical)
- 4C. Space-Optimized (if natural)

Complexity:
- Time: O(...)
- Space: O(...)

------------------------------------------------------------------------

🔎 5. Dry Run / Example
- Small input with step highlights.

------------------------------------------------------------------------

🏷 6. Pattern Recognition
- Name, family, triggers, related problems.

------------------------------------------------------------------------

🔄 7. Edge Cases and Pitfalls
- Bullet points with sharp warnings.

------------------------------------------------------------------------

✅ 8. Takeaway
- 3–5 bullets that encode the solution’s “shape” to memory.

------------------------------------------------------------------------

Coding Conventions and Best Practices
- Memo arrays: prefer Integer/Boolean wrappers for tri-state (null/uncomputed).
- Rolling arrays: document what prev/curr indexes represent (e.g., dp[i-1] vs dp[i]).
- 1D compression: walk j backwards for counts (Distinct Subsequences) to avoid contamination.
- Interval DP: always specify l..r exclusive/inclusive semantics; ensure base intervals are 0.
- Strongly prefer deterministic iteration orders (no HashSet iteration dependency unless per-problem).
- Use long for counting DP that can exceed int (e.g., Distinct Subsequences intermediate sums).
- Use binary search for predecessor lookup in scheduling.
- For greedy justifications (Longest Common Substring vs LCS; Frog II round-trip), include the high-level proof idea.

Common Patterns Catalog (non-exhaustive)
- Prefix DP on two strings: LCS, LPS via LCS(s,rev), Edit Distance, Interleaving, SCS, Distinct Subsequences, Regex.
- Interval “pick last/split” DP: MCM, Burst Balloons, Boolean Parenthesization, Palindrome Partitioning II.
- 1D linear DP: Kadane, House Robber family, Delete and Earn, Candy.
- Grid DP: Unique Paths (with/without obstacles), Min Path Sum, Triangle DP, Falling Path Sum.
- Tree DP: House Robber III (take/skip), Binary Tree Max Path Sum (global+return), Diameter.
- Stocks DP: I/II/III/Cooldown/Fee/IV, operation-index or transaction-left templates.
- Scheduling DP: Weighted Interval Scheduling (LC 1235) with binary search.
- Set/Bitmask DP: Partition to K Equal Sum Subsets, Assignment variants (optional).
- Graph + memo on DAGs: Longest Increasing Path in Matrix (LC 329).

Anti-patterns to avoid
- Mixing 0-based and 1-based dp without stating it.
- Using value-outer then coin-inner when counting combinations (counts permutations).
- Returning partial code without complexity or base cases.
- Overuse of recursion without memo (exponential).
- Writing reconstruction only in comments—include working code if reconstruction is a common ask.

Versioning and Edits
- When updating an existing file:
  - Maintain the same outline.
  - If changing a recurrence, adjust dry run and complexity.
  - Keep the “Generated on” only for first creation; add a “Last updated” line if useful.
- Cross-reference related notes in “Pattern Recognition”.

Examples of Quick Hooks
- SCS length = n + m − LCS.
- LPS(s) = LCS(s, reverse(s)).
- Coin combinations = coin-outer → value-inner forward loops with dp[0]=1.
- Stocks IV: if K ≥ n/2, reduce to Stock II greedy.
- Frog 403: state = (index, last jump), transitions on {k−1,k,k+1} > 0.
- Burst Balloons: dp[l][r] = max over k (dp[l][k] + val[l]*val[k]*val[r] + dp[k][r]) with val padded by 1.

How to Propose/Scope a New Note
- Confirm it’s a canonical interview problem or a variant that introduces a new DP idea.
- Map it into an existing pattern family (above).
- Add it with the next DP_XX_… number, filling the standard outline.

This file is the authoritative style guide for creating and maintaining DP notes in this repository. Follow it to keep the content consistent, high-signal, and interview-ready.
