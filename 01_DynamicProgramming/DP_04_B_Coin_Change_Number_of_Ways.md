# Dynamic Programming Notes

## 04_B - Coin Change (Number of Ways to Make Amount) — Unbounded Knapsack Counting

**Generated on:** 2026-02-24 22:45:28 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given coin denominations `coins[]` (infinite supply of each) and a target `amount`, return the number of distinct combinations to form exactly `amount`.

Key points:
- Infinite supply → Unbounded Knapsack
- Combinations (order-independent), not permutations (order-dependent)
- If no combination can form `amount`, return 0
- Some platforms ask to return answer modulo 1e9+7

Example:
- coins = [1,2,5], amount = 5 → ways = 4
  - [5], [2,2,1], [2,1,1,1], [1,1,1,1,1]

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- dp[v] = number of combinations to make value `v`

Unbounded nature + Combinations:
- Iterate coins outer, values inner (forward), i.e.,
  - for each coin c:
    - for v from c..amount:
      - dp[v] += dp[v - c]

Initialize:
- dp[0] = 1  (one way to make 0 sum: choose nothing)
- dp[v>0] = 0 initially

Why coin-outer?
- Ensures each combination is counted once (order-independent).
- If you reverse the loop nesting (value outer, coin inner), you count permutations.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For each coin c ∈ coins, for v from c..amount:

    dp[v] = dp[v] + dp[v - c]

Meaning:
- To form sum v, append coin c to every combination that forms (v - c).

------------------------------------------------------------------------

## 🧱 4. Base Cases and Initialization

- dp[0] = 1
- For all v > 0: dp[v] = 0
- For empty coin set:
  - dp[0] = 1, dp[v>0] = 0

Pitfall:
- Don’t initialize dp[v] with 1 for v>0; only dp[0] starts at 1.

------------------------------------------------------------------------

## 💻 5A. Bottom-Up (1D, Canonical)

```java
import java.util.*;

class CoinChangeWays_1D {
    public long countWaysToMakeChange(int[] coins, int amount) {
        long[] dp = new long[amount + 1];
        dp[0] = 1; // base

        for (int c : coins) {
            for (int v = c; v <= amount; v++) {
                dp[v] += dp[v - c];
            }
        }
        return dp[amount];
    }
}
```

Complexity:
- Time: O(N * amount), where N = number of coins
- Space: O(amount)

If required by platform, apply modulo:
- Replace `dp[v] = (dp[v] + dp[v - c]) % MOD`

------------------------------------------------------------------------

## 💻 5B. Bottom-Up (2D, Educational Form)

dp[i][v] = number of ways to make sum v using first i coins (coins[0..i-1])

Transitions:
- Exclude current coin: dp[i-1][v]
- Include current coin (unbounded): dp[i][v - coin] (note i stays same)

```java
import java.util.*;

class CoinChangeWays_2D {
    public long countWaysToMakeChange(int[] coins, int amount) {
        int n = coins.length;
        long[][] dp = new long[n + 1][amount + 1];

        // base: with any number of coins, ways to make 0 is 1 (empty set)
        for (int i = 0; i <= n; i++) dp[i][0] = 1;

        for (int i = 1; i <= n; i++) {
            int c = coins[i - 1];
            for (int v = 0; v <= amount; v++) {
                long exclude = dp[i - 1][v];
                long include = (v >= c) ? dp[i][v - c] : 0;
                dp[i][v] = exclude + include;
            }
        }
        return dp[n][amount];
    }
}
```

Complexity:
- Time: O(N * amount), Space: O(N * amount)

------------------------------------------------------------------------

## 💻 5C. Top-Down (Memoized DFS)

```java
import java.util.*;

class CoinChangeWays_TopDown {
    public long countWaysToMakeChange(int[] coins, int amount) {
        Long[][] memo = new Long[coins.length + 1][amount + 1];
        return dfs(coins, coins.length, amount, memo);
    }

    private long dfs(int[] coins, int i, int v, Long[][] memo) {
        if (v == 0) return 1;        // one way to make 0
        if (i == 0) return 0;        // no coins left to use
        if (memo[i][v] != null) return memo[i][v];

        long ways = 0;
        int c = coins[i - 1];

        // exclude current coin type
        ways += dfs(coins, i - 1, v, memo);

        // include current coin type (unbounded)
        if (v >= c) {
            ways += dfs(coins, i, v - c, memo);
        }

        return memo[i][v] = ways;
    }
}
```

Complexity:
- Time: O(N * amount) with memoization
- Space: O(N * amount) + recursion stack

------------------------------------------------------------------------

## 🔎 6. Full Dry Run Example

coins = [1, 2, 5], amount = 5

Initialization:
- dp = [1, 0, 0, 0, 0, 0]

Process coin=1 (dp[v] += dp[v-1], updated in place so each new v sees the just-updated dp[v-1]):
- v=1: dp[1]+=dp[0] → 0+1=1
- v=2: dp[2]+=dp[1] → 0+1=1
- v=3: dp[3]+=dp[2] → 0+1=1
- v=4: dp[4]+=dp[3] → 0+1=1
- v=5: dp[5]+=dp[4] → 0+1=1
→ dp = [1, 1, 1, 1, 1, 1]

Process coin=2 (dp[v] += dp[v-2]):
- v=2: dp[2]+=dp[0] → 1+1=2
- v=3: dp[3]+=dp[1] → 1+1=2
- v=4: dp[4]+=dp[2] → 1+2=3
- v=5: dp[5]+=dp[3] → 1+2=3
→ dp = [1, 1, 2, 2, 3, 3]

Process coin=5 (dp[v] += dp[v-5]):
- v=5: dp[5]+=dp[0] → 3+1=4
→ dp = [1, 1, 2, 2, 3, 4]

Answer: dp[5] = 4, matching the 4 combinations:
- {5}
- {2,2,1}
- {2,1,1,1}
- {1,1,1,1,1}

Key takeaway from this trace: because the update is in-place with an ascending `v` loop, `dp[v-coin]` already reflects any contribution from the *current* coin at smaller values — that is exactly what allows unlimited reuse of the same coin (unbounded knapsack) while the coin-outer loop still avoids counting order permutations.

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: Unbounded Knapsack (Counting Combinations)
- Triggers:
  - Infinite supply
  - Order-independent counting (coin-outer loop)
- Related:
  - Minimum coins problem (minimization)
  - Counting permutations (swap loop order to value-outer/coin-inner — not desired here)

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- amount = 0 → 1 way (empty set)
- Empty coins and amount > 0 → 0 ways
- Ordering:
  - Coin-outer → combinations
  - Value-outer then coin-inner → permutations (wrong for this problem)
- Modulo if required by platform constraints

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Use 1D dp with coin-outer, value-inner forward loop to count combinations.
- dp[0] = 1 is the cornerstone for counting paths/ways in additive DP.

------------------------------------------------------------------------

