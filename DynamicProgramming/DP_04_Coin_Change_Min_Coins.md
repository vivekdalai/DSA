# Dynamic Programming Notes

## 04 - Coin Change (Minimum Coins to Amount) — Unbounded Knapsack Pattern

**Generated on:** 2026-02-24 19:07:46 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given coin denominations `coins[]` (infinite supply of each) and a target `amount`, find the minimum number of coins needed to make up the amount. If it’s not possible, return `-1`.

This is an Unbounded Knapsack variant:
- You can take any coin multiple times.
- Minimize the count of picked items (coins).

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- dp[v] = minimum number of coins needed to make value `v`.

Goal: dp[amount]

Unbounded nature:
- When we pick a coin `c`, we stay on the same “row” (we can pick `c` again): dp[v] can depend on dp[v - c].

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For each value v and coin c (where v ≥ c):

    dp[v] = min(dp[v], 1 + dp[v - c])

Meaning: last coin is c, so we add 1 to the solution for (v - c).

------------------------------------------------------------------------

## 🧱 4. Base Cases and Initialization

- dp[0] = 0 (0 coins to form 0)
- For all v > 0, initialize dp[v] = INF (a large sentinel, e.g., 1_000_000_000)

Pitfalls:
- Do not initialize dp[v] with 0 for v > 0.
- Ensure dp[v - c] != INF before using it (or rely on min with INF).

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

The 1D dp array already gives O(1) extra space (beyond the dp buffer).
Unbounded knapsack uses a forward inner loop over values (from coin to amount).

------------------------------------------------------------------------

## 💻 6A. Clean Interview Version — Top-Down (Memoized DFS)

```java
import java.util.*;

class SolutionTopDown {
    public int coinChange(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (coins == null || coins.length == 0) return -1;

        Integer[] memo = new Integer[amount + 1];
        int INF = 1_000_000_000;
        int res = dfs(coins, amount, memo, INF);
        return (res >= INF) ? -1 : res;
    }

    private int dfs(int[] coins, int rem, Integer[] memo, final int INF) {
        if (rem == 0) return 0;
        if (rem < 0) return INF;
        if (memo[rem] != null) return memo[rem];

        int best = INF;
        for (int c : coins) {
            int sub = dfs(coins, rem - c, memo, INF);
            if (sub != INF) best = Math.min(best, 1 + sub);
        }
        return memo[rem] = best;
    }
}
```

Complexity:
- Time: O(N * amount) in practice with memoization (N = number of coins)
- Space: O(amount) recursion stack + memo

------------------------------------------------------------------------

## 💻 6B. Clean Interview Version — Bottom-Up (1D Tabulation)

```java
import java.util.*;

class Solution {
    public int coinChange(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (coins == null || coins.length == 0) return -1;

        int INF = 1_000_000_000;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        // Unbounded knapsack pattern: coin outer, value inner (forward)
        for (int coin : coins) {
            for (int v = coin; v <= amount; v++) {
                if (dp[v - coin] != INF) {
                    dp[v] = Math.min(dp[v], dp[v - coin] + 1);
                }
            }
        }
        return dp[amount] == INF ? -1 : dp[amount];
    }
}
```

Complexity:
- Time: O(N * amount)
- Space: O(amount)

Why coin-outer?
- Reflects “unbounded” nature (we can reuse current coin in the same iteration).
- Works cleanly and avoids permutation-vs-combination confusion in other problems.

------------------------------------------------------------------------

## 💻 6C. Reconstruct One Optimal Combination (Optional)

To return one set of coins forming the optimal minimum:

```java
import java.util.*;

class SolutionWithPath {
    public List<Integer> minCoinCombination(int[] coins, int amount) {
        int INF = 1_000_000_000;
        int[] dp = new int[amount + 1];
        int[] pick = new int[amount + 1];
        Arrays.fill(dp, INF);
        Arrays.fill(pick, -1);
        dp[0] = 0;

        for (int coin : coins) {
            for (int v = coin; v <= amount; v++) {
                if (dp[v - coin] != INF && dp[v - coin] + 1 < dp[v]) {
                    dp[v] = dp[v - coin] + 1;
                    pick[v] = coin;
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        if (dp[amount] == INF) return path; // empty → no solution

        int cur = amount;
        while (cur > 0) {
            int coin = pick[cur];
            path.add(coin);
            cur -= coin;
        }
        // 'path' contains one optimal multiset (order not important)
        return path;
    }
}
```

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input:
- coins = [1, 2, 5], amount = 11

Initialize:
- dp = [0, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF, INF]

Process coin = 1:
- dp[v] = v (using only 1s)

Process coin = 2:
- dp[2] = min(2, dp[0]+1=1) = 1
- dp[3] = min(3, dp[1]+1=3) = 3
- dp[4] = min(4, dp[2]+1=2) = 2
- ...

Process coin = 5:
- dp[5] = min(5, dp[0]+1=1) = 1
- dp[10] = min(10, dp[5]+1=2) = 2
- dp[11] = min(11, dp[6]+1=3) = 3

Answer: dp[11] = 3 (11 = 5 + 5 + 1)

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Unbounded Knapsack (Minimization)
- Triggers:
  - Infinite supply of items
  - Optimize count/sum with additive transitions
  - Transition uses state at (value - weight) on the same row

Mental template:
- dp[0] = 0, dp[others] = INF
- For each coin:
  - for v = coin..amount:
    - dp[v] = min(dp[v], 1 + dp[v - coin])

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

Edge Cases:
- amount = 0 → 0
- coins empty and amount > 0 → -1
- No combination possible → -1
- Duplicate coin denominations → harmless
- Large amount but small coin set → ensure INF safe

Pitfalls:
- Wrong initialization (dp[v]=0 for v>0) will break answers
- Using backward loop for unbounded problems (forward is canonical)
- Overflow: keep INF within int range; avoid adding to INF directly
- Mixing “count ways” DP with “min coins” DP (different transitions)

------------------------------------------------------------------------

## 🔁 10. Variants

- Coin Change II (Count number of combinations to form amount)
  - dp[v] += dp[v - coin] with coin-outer, value-inner (forward)
- Minimum coins with additional constraints (limited supply → 0/1 or bounded knapsack)
- Path reconstruction for lexicographically smallest set (tie-breaking during pick)

------------------------------------------------------------------------

## ✅ 11. Takeaway

- Treat Coin Change (min coins) as an Unbounded Knapsack with minimization.
- Correct initialization and loop ordering are key.
- 1D DP is optimal and interview-friendly; memoized DFS is also acceptable.

------------------------------------------------------------------------

# End of Notes
