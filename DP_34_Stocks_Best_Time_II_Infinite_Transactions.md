# Dynamic Programming Notes

## 34 - Best Time to Buy and Sell Stock II (Infinite Transactions)

**Generated on:** 2026-02-24 21:18:09 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given `prices[i]` for day `i`, you may complete as many transactions as you like:
- You must sell the stock before you buy again (cannot hold more than one share at a time).

Goal: Maximize total profit.

Examples:
- prices = [7,1,5,3,6,4] → Answer = 7  
  Transactions: buy at 1 → sell at 5 (profit 4), buy at 3 → sell at 6 (profit 3), total = 7
- prices = [1,2,3,4,5] → Answer = 4 (buy at 1 → sell at 5)

------------------------------------------------------------------------

## 🪜 2. Approaches

A) Greedy (optimal and simplest)
- Sum all positive day-to-day increases:  
  `profit += max(0, prices[i] - prices[i-1])`

B) DP on “buy/sell” states (template for stock DP series)
- `dp[i][buy]` = max profit from day `i` to end:
  - `buy ∈ {0,1}`:
    - `buy == 1` → we can buy or skip
    - `buy == 0` → we can sell or skip

------------------------------------------------------------------------

## 🔁 3. DP Recurrence

Let N = prices.length.

- If `buy == 1` (allowed to buy):
  - `dp[i][1] = max(-prices[i] + dp[i+1][0], 0 + dp[i+1][1])`
- If `buy == 0` (holding a stock; allowed to sell):
  - `dp[i][0] = max(+prices[i] + dp[i+1][1], 0 + dp[i+1][0])`

Base:
- `dp[N][*] = 0` (past the last day → no profit)

Answer:
- `dp[0][1]` (start on day 0 with permission to buy)

Space-optimized tabulation uses two scalars for next-day states or a pair of arrays of size 2.

------------------------------------------------------------------------

## 💻 4A. Greedy (O(n), O(1))

```java
class StockII_Greedy {
    public int maxProfit(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                profit += prices[i] - prices[i - 1];
            }
        }
        return profit;
    }
}
```

Rationale:
- Every ascending segment is fully captured by summing daily gains.

------------------------------------------------------------------------

## 💻 4B. DP — Memoized (Top-Down)

```java
import java.util.*;

class StockII_Memo {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        Integer[][] dp = new Integer[n + 1][2];
        return dfs(0, 1, prices, dp);
    }

    private int dfs(int i, int buy, int[] prices, Integer[][] dp) {
        if (i == prices.length) return 0;
        if (dp[i][buy] != null) return dp[i][buy];

        if (buy == 1) {
            int take = -prices[i] + dfs(i + 1, 0, prices, dp); // buy
            int skip = 0 + dfs(i + 1, 1, prices, dp);          // do nothing
            return dp[i][buy] = Math.max(take, skip);
        } else {
            int sell =  prices[i] + dfs(i + 1, 1, prices, dp); // sell
            int hold =  0 + dfs(i + 1, 0, prices, dp);         // do nothing
            return dp[i][buy] = Math.max(sell, hold);
        }
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n) recursion + memo

------------------------------------------------------------------------

## 💻 4C. DP — Space-Optimized Tabulation (O(1) space)

```java
class StockII_Tab {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int nextBuy = 0, nextSell = 0; // dp[i+1][1], dp[i+1][0]

        for (int i = n - 1; i >= 0; i--) {
            int currBuy  = Math.max(-prices[i] + nextSell, nextBuy);
            int currSell = Math.max(+prices[i] + nextBuy, nextSell);
            nextBuy = currBuy;
            nextSell = currSell;
        }
        return nextBuy; // dp[0][1]
    }
}
```

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

prices = [7,1,5,3,6,4]
- Greedy adds: (1→5)=4, (3→6)=3 → total 7
- DP yields the same. The greedy is a condensed form of taking every profitable edge.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: “Stocks II — Unlimited Transactions”
- Family: Stock DP (buy/sell state machine)
- Triggers:
  - Unlimited transactions allowed
  - No cooldown or fee

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Flat or decreasing arrays → profit 0
- Very large arrays → prefer greedy for constant space and speed
- Don’t confuse with constraints:
  - Cooldown day between sell and next buy
  - Transaction fee on sells
  - Limit on number of transactions

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Greedy summing positive differences is optimal and simplest.
- Buy/sell DP template generalizes to more complex constraints (cooldown, fee, at most K transactions).

------------------------------------------------------------------------

# End of Notes
