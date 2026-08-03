# Dynamic Programming Notes

## 57 - Perfect Squares (LC 279) — Unbounded Knapsack Minimization

**Generated on:** 2026-08-03 01:30:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer `n`, return the least number of perfect square numbers
(`1, 4, 9, 16, ...`) that sum to exactly `n`.

Example: `n = 12` → `4 + 4 + 4 = 12` → answer `3`.

This is structurally identical to [Coin Change — Min Coins](DP_04_Coin_Change_Min_Coins.md):
the "coin denominations" are simply every perfect square `<= n`, generated on the
fly, with infinite supply of each.

------------------------------------------------------------------------

## 🪜 2. State Definition

- `dp[i]` = minimum number of perfect squares that sum to `i`.

Goal: `dp[n]`.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For each `i` from `1..n`, try every square `j*j <= i`:

    dp[i] = min over j where j*j <= i  of  ( dp[i - j*j] + 1 )

Meaning: the last square used is `j*j`, so the answer for `i` is one more than the
best answer for the remainder `i - j*j`.

------------------------------------------------------------------------

## 🧱 4. Base Cases and Initialization

- `dp[0] = 0` (zero squares needed to sum to 0).
- `dp[i]` initialized to a large sentinel (or `Integer.MAX_VALUE`) for `i > 0`,
  since unlike Coin Change, this problem always has a solution (every `n` is
  reachable using `n` copies of `1*1`), so `-1` never needs to be returned.

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

Same as Coin Change: a 1D `dp` array of size `n + 1` is already the minimal
representation — no further collapsing is possible since `dp[i]` can depend on any
`dp[i - j*j]` for varying `j`, not just a fixed nearby index.

Beyond-DP note (good to mention if the interviewer pushes for more): Lagrange's
four-square theorem guarantees every number is expressible with at most 4 squares,
which enables an O(sqrt(n)) number-theoretic check (is it 1, 2, 3, or 4?) — but the
DP is the expected default answer.

------------------------------------------------------------------------

## 💻 6A. Top-Down (Memoized Recursion)

```java
class SolutionTopDown {
    public int numSquares(int n) {
        Integer[] memo = new Integer[n + 1];
        return solve(n, memo);
    }

    private int solve(int remaining, Integer[] memo) {
        if (remaining == 0) return 0;
        if (memo[remaining] != null) return memo[remaining];

        int best = Integer.MAX_VALUE;
        for (int j = 1; j * j <= remaining; j++) {
            int sub = solve(remaining - j * j, memo);
            if (sub != Integer.MAX_VALUE) best = Math.min(best, sub + 1);
        }
        return memo[remaining] = best;
    }
}
```

Complexity: Time O(n * sqrt(n)), Space O(n) (recursion + memo)

------------------------------------------------------------------------

## 💻 6B. Bottom-Up (1D Tabulation) — Interview-Preferred

```java
import java.util.*;

class Solution {
    public int numSquares(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j * j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i - j * j] + 1);
            }
        }
        return dp[n];
    }
}
```

Complexity: Time O(n * sqrt(n)), Space O(n)

**Equivalent Coin-Change-style form.** Precompute the perfect squares `<= n` into an
array once, then run the exact coin-outer / value-inner-ascending loop from
[Coin Change — Min Coins, 6B](DP_04_Coin_Change_Min_Coins.md). This isn't just
similar — it's the same unbounded-knapsack-minimization algorithm, with `squares[]`
standing in for `coins[]`. Loop order doesn't affect correctness here because this is
a *minimization*, not a count of combinations (contrast with
[Coin Change II](DP_04_B_Coin_Change_Number_of_Ways.md), where coin-outer order is
required specifically to avoid double-counting permutations):

```java
import java.util.*;

class SolutionCoinChangeStyle {
    public int numSquares(int n) {
        List<Integer> squares = new ArrayList<>();
        for (int j = 1; j * j <= n; j++) squares.add(j * j);

        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int sq : squares) {
            for (int v = sq; v <= n; v++) {
                if (dp[v - sq] != Integer.MAX_VALUE) {
                    dp[v] = Math.min(dp[v], dp[v - sq] + 1);
                }
            }
        }
        return dp[n];
    }
}
```

Complexity: Time O(n * sqrt(n)) (same as above — precomputing `squares[]` costs only
O(sqrt(n))), Space O(n + sqrt(n))

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input: `n = 12`

- `dp[0] = 0`
- `dp[1] = dp[0] + 1 = 1` (using `1*1`)
- `dp[4] = min(dp[3]+1, dp[0]+1) = 1` (using `2*2` directly)
- `dp[8] = min(dp[7]+1, dp[4]+1) = 2` (`4 + 4`)
- `dp[12] = min(dp[11]+1, dp[8]+1, dp[3]+1) = min(_, 3, _) = 3` (`4 + 4 + 4`)

Answer: `dp[12] = 3`

Second example: `n = 13` → `dp[13] = 2` (`4 + 9`), showing the answer isn't always
"greedily use the largest square first" in an obvious way without the DP scan.

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Unbounded Knapsack Minimization (dynamically generated denominations)
- Family: identical shape to [Coin Change — Min Coins](DP_04_Coin_Change_Min_Coins.md);
  "coins" here are `1, 4, 9, 16, ...` up to `n` instead of a fixed input array
- Triggers: "minimum count of reusable building blocks summing to a target," where
  the blocks follow a generatable numeric pattern (squares, cubes, Fibonacci, etc.)

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

Edge Cases:
- `n` is itself a perfect square → answer `1`
- `n = 0` → `dp[0] = 0` (not typically asked directly, but the base case matters)
- Large `n` → O(n * sqrt(n)) must stay within time limits; this is the accepted
  complexity for LC constraints (`n <= 10^4`)

Pitfalls:
- Forgetting perfect squares always guarantee a solution — no `-1`/"impossible"
  branch is needed here, unlike Coin Change
- Off-by-one in the square-generation loop condition (`j * j <= i`, not `<`)
- Recomputing `j * j` inside the inner loop without caching is fine at this scale,
  but avoid recomputing `Math.sqrt` repeatedly — plain multiplication is simpler and
  avoids floating-point edge cases

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Recognize this immediately as Coin Change with square numbers as the coin set —
  don't re-derive the pattern from scratch.
- `dp[0] = 0`, then minimize `dp[i - j*j] + 1` over all valid squares `j*j <= i`.
- Mention Lagrange's four-square theorem as a bonus fact if asked to push beyond
  the DP, but implement the DP as the primary answer.

------------------------------------------------------------------------

# End of Notes
