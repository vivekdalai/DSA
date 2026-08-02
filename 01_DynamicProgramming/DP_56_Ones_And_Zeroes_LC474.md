# Dynamic Programming Notes

## 56 - Ones and Zeroes (LC 474) — 2D 0/1 Knapsack

**Generated on:** 2026-08-03 01:25:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array of binary strings `strs` and integers `m` and `n`, find the size of
the largest subset of `strs` such that the subset contains **at most `m` 0's and at
most `n` 1's** in total. Each string can be used at most once.

This is 0/1 Knapsack with two independent weight dimensions (zero-budget and
one-budget) instead of one — the framing Amazon research describes as "resource
allocation" (e.g. capacity-constrained selection under two simultaneous limits).

------------------------------------------------------------------------

## 🪜 2. State Definition

- `dp[i][j]` = maximum number of strings selectable using **at most `i` zeros and
  `j` ones**, considering strings processed so far.

Goal: `dp[m][n]` after processing every string in `strs`.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For each string with `zeros` zero-count and `ones` one-count, updating the existing
`dp` table (0/1 knapsack — each string used at most once):

    dp[i][j] = max(dp[i][j], dp[i - zeros][j - ones] + 1)   for i >= zeros, j >= ones

Iterate `i` from `m` down to `zeros` and `j` from `n` down to `ones` (descending) so
each string's `dp[i-zeros][j-ones]` read still reflects the *previous* string's
state — the same in-place 0/1 knapsack trick as [DP_18](DP_18_0_1_Knapsack.md).

------------------------------------------------------------------------

## 🧱 4. Base Cases and Initialization

- `dp[i][j] = 0` for all `i, j` before processing any string (0 strings picked → 0
  size subset, always achievable within any budget).

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

A naive formulation would add a third dimension for "which string index we're on,"
i.e. `dp[k][i][j]`. Because each string is only ever used once and processed in a
single pass, the classic 0/1 knapsack in-place trick collapses that away: reuse one
`(m+1) x (n+1)` table across all strings, updating it **in descending order** of `i`
and `j` per string. This is the entire space optimization — there's no further
reduction possible since both budgets (`m` and `n`) are needed simultaneously.

------------------------------------------------------------------------

## 💻 6A. Bottom-Up 2D In-Place Knapsack — Interview-Preferred

```java
class Solution {
    public int findMaxForm(String[] strs, int m, int n) {
        int[][] dp = new int[m + 1][n + 1];

        for (String s : strs) {
            int zeros = 0, ones = 0;
            for (char c : s.toCharArray()) {
                if (c == '0') zeros++;
                else ones++;
            }

            // descending order: treat each string as usable at most once
            for (int i = m; i >= zeros; i--) {
                for (int j = n; j >= ones; j--) {
                    dp[i][j] = Math.max(dp[i][j], dp[i - zeros][j - ones] + 1);
                }
            }
        }
        return dp[m][n];
    }
}
```

Complexity:
- Time: O(k * m * n) where `k = strs.length`
- Space: O(m * n)

------------------------------------------------------------------------

## 💻 6B. Top-Down (Memoized, 3D) — For Comparison

```java
import java.util.*;

class SolutionTopDown {
    public int findMaxForm(String[] strs, int m, int n) {
        int[][] counts = new int[strs.length][2];
        for (int k = 0; k < strs.length; k++) {
            for (char c : strs[k].toCharArray()) {
                counts[k][c - '0']++;
            }
        }
        Integer[][][] memo = new Integer[strs.length + 1][m + 1][n + 1];
        return solve(0, m, n, counts, memo);
    }

    private int solve(int idx, int i, int j, int[][] counts, Integer[][][] memo) {
        if (idx == counts.length) return 0;
        if (memo[idx][i][j] != null) return memo[idx][i][j];

        int skip = solve(idx + 1, i, j, counts, memo);
        int take = 0;
        int zeros = counts[idx][0], ones = counts[idx][1];
        if (i >= zeros && j >= ones) {
            take = 1 + solve(idx + 1, i - zeros, j - ones, counts, memo);
        }
        return memo[idx][i][j] = Math.max(skip, take);
    }
}
```

Complexity:
- Time: O(k * m * n)
- Space: O(k * m * n) memo + recursion depth O(k)

Note: the bottom-up in-place version (6A) is what's expected in an interview — same
time complexity, far less space.

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input: `strs = ["10", "0001", "111001", "1", "0"]`, `m = 5`, `n = 3`

- `"10"` → zeros=1, ones=1
- `"0001"` → zeros=3, ones=1
- `"111001"` → zeros=2, ones=4 (too many ones ever to fully afford alone with n=3)
- `"1"` → zeros=0, ones=1
- `"0"` → zeros=1, ones=0

Best subset: `{"10", "0001", "1", "0"}` → uses zeros = 1+3+0+1 = 5 (≤5),
ones = 1+1+1+0 = 3 (≤3) → size **4**, which matches `dp[5][3] = 4`.
(`"111001"` is excluded — its 4 ones alone would blow the `n=3` budget.)

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: 2D 0/1 Knapsack (Multi-Constraint Knapsack)
- Family: same in-place descending-iteration trick as
  [0/1 Knapsack](DP_18_0_1_Knapsack.md) and
  [Subset Sum](DP_19_Subset_Sum_Equals_K.md), extended to two simultaneous budgets
- Triggers: "at most X of type A and Y of type B," each item usable once, maximize
  count/value under two independent capacity constraints

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

Edge Cases:
- `m = 0` or `n = 0` — only strings whose corresponding count is exactly 0 for that
  dimension are usable
- A string whose `zeros > m` or `ones > n` individually can never be selected — it's
  simply skipped by the `i >= zeros && j >= ones` guard, no special-casing needed
- `strs` empty → answer 0
- Strings with identical content are still independent list entries (each is a
  distinct "item," not merged)

Pitfalls:
- Iterating `i`/`j` in **ascending** order (like unbounded knapsack) instead of
  descending — this would let a string be reused multiple times, silently breaking
  the 0/1 constraint
- Forgetting to reset per-string `zeros`/`ones` counts each iteration
- Off-by-one on loop bounds (`i >= zeros`, not `i > zeros`)

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Two independent capacities → two dimensions in the dp table; everything else is
  standard 0/1 knapsack.
- Iterate each string's budgets in **descending** order to preserve the "use at most
  once" guarantee — this is the one detail that differentiates it from unbounded
  knapsack / Coin Change II style problems.
- In-place 2D table reuse across items is the expected space optimization.

------------------------------------------------------------------------

# End of Notes
