# Dynamic Programming Notes

## 35 - Best Time to Buy and Sell Stock III (At Most Two Transactions)

**Generated on:** 2026-02-24 21:31:33 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given `prices[i]` for day `i`, maximize profit with at most two complete transactions (each transaction = buy then sell, cannot overlap).

Examples:
- prices = [3,3,5,0,0,3,1,4] → Answer = 6  
  buy at 0 → sell at 3 (profit 3), buy at 1 → sell at 4 (profit 3)

------------------------------------------------------------------------

## 🪜 2. State Designs

Two common DP designs:

A) Transactions-left DP (clean and scalable)  
- `dp[i][buy][t]` = max profit from day `i` onward, where:
  - `buy ∈ {0,1}` → 1: we can buy next, 0: we must sell next (i.e., holding)
  - `t` = number of transactions remaining (sell actions left). For at most two transactions, `t ∈ {0,1,2}`
- Answer: `dp[0][1][2]`

B) “Operation count” DP with 0..3 ops (buy, sell, buy, sell)  
- `op ∈ {0..3}`: even op = buy state, odd op = sell state; when `op == 4` stop
- Answer: `dp[0][0]`

This note uses (A) as it generalizes nicely.

------------------------------------------------------------------------

## 🔁 3. Recurrence (Transactions-left DP)

Base:
- If `i == N` or `t == 0` → 0

Transition:
- If `buy == 1` (allowed to buy):
  - `dp[i][1][t] = max(-prices[i] + dp[i+1][0][t], dp[i+1][1][t])`
- If `buy == 0` (holding; allowed to sell):
  - `dp[i][0][t] = max(+prices[i] + dp[i+1][1][t-1], dp[i+1][0][t])`

------------------------------------------------------------------------

## 💻 4A. Memoized (Top-Down)

```java
import java.util.*;

class StockIII_Memo {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        Integer[][][] dp = new Integer[n + 1][2][3]; // [i][buy][t]
        return dfs(0, 1, 2, prices, dp);
    }

    private int dfs(int i, int buy, int t, int[] prices, Integer[][][] dp) {
        if (i == prices.length || t == 0) return 0;
        if (dp[i][buy][t] != null) return dp[i][buy][t];

        if (buy == 1) {
            int take = -prices[i] + dfs(i + 1, 0, t, prices, dp); // buy
            int skip = 0 + dfs(i + 1, 1, t, prices, dp);          // wait
            return dp[i][buy][t] = Math.max(take, skip);
        } else {
            int sell = prices[i] + dfs(i + 1, 1, t - 1, prices, dp); // sell
            int hold = 0 + dfs(i + 1, 0, t, prices, dp);             // wait
            return dp[i][buy][t] = Math.max(sell, hold);
        }
    }
}
```

Complexity:
- Time: O(N * 2 * 3) = O(N)
- Space: O(N * 2 * 3) memo + O(N) recursion

------------------------------------------------------------------------

## 💻 4B. Tabulation (Bottom-Up)

```java
class StockIII_Tab {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int[][][] dp = new int[n + 1][2][3]; // initialized to 0

        for (int i = n - 1; i >= 0; i--) {
            for (int buy = 0; buy <= 1; buy++) {
                for (int t = 1; t <= 2; t++) { // t=0 stays 0
                    if (buy == 1) {
                        dp[i][1][t] = Math.max(
                            -prices[i] + dp[i + 1][0][t],
                             0 + dp[i + 1][1][t]
                        );
                    } else {
                        dp[i][0][t] = Math.max(
                            +prices[i] + dp[i + 1][1][t - 1],
                             0 + dp[i + 1][0][t]
                        );
                    }
                }
            }
        }
        return dp[0][1][2];
    }
}
```

Space optimization:
- Keep only `next[2][3]` and `curr[2][3]` for O(1) extra space.

------------------------------------------------------------------------

## 💻 4C. Space-Optimized (O(1) extra)

```java
class StockIII_Space {
    public int maxProfit(int[] prices) {
        int[][] next = new int[2][3];
        int[][] curr = new int[2][3];

        for (int i = prices.length - 1; i >= 0; i--) {
            for (int buy = 0; buy <= 1; buy++) {
                for (int t = 1; t <= 2; t++) {
                    if (buy == 1) {
                        curr[1][t] = Math.max(-prices[i] + next[0][t], next[1][t]);
                    } else {
                        curr[0][t] = Math.max(+prices[i] + next[1][t - 1], next[0][t]);
                    }
                }
            }
            // swap
            int[][] tmp = next;
            next = curr;
            curr = tmp;
        }
        return next[1][2];
    }
}
```

------------------------------------------------------------------------

## 🔎 5. “4-ops” Alternative (Buy/Sell steps 0..3)

- `op ∈ {0..3}`, at even op → can buy, odd op → can sell
- When `op == 4`, return 0
- Transition:
  - If even: `max(-p[i] + dp[i+1][op+1], dp[i+1][op])`
  - If odd:  `max(+p[i] + dp[i+1][op+1], dp[i+1][op])`

Equivalent results; some find this form intuitive.

------------------------------------------------------------------------

## 🔎 6. Dry Run (Brief)

prices = [3,3,5,0,0,3,1,4]
- Two profitable segments:
  - 0 → 3 (profit 3)
  - 1 → 4 (profit 3)
- Sum = 6

The DP captures the best combination of two disjoint buy/sell pairs.

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: Stocks III — at most two transactions
- Family: Stock DP, transactions-limited
- Triggers:
  - Small upper bound on transactions (use t or op)
- Scales naturally to K transactions (add dimension t ∈ {0..K})

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- If array is short or no rise → return 0
- Ensure transactions count reduces on sell, not on buy
- Be careful with indices and base conditions (t=0)

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Use `dp[i][buy][t]`, decreasing `t` on sell.
- Works in O(N) time, O(1) extra space with rolling arrays.
- “4-ops” variant is an equivalent, often used in tutorials.

------------------------------------------------------------------------

# End of Notes
