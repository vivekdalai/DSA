# Dynamic Programming Notes

## 36 - Best Time to Buy and Sell Stock With Cooldown

**Generated on:** 2026-02-24 21:32:34 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `prices` where `prices[i]` is the price of a stock on day `i`, find the maximum profit with the constraint:
- After you sell a stock, you cannot buy stock on the next day (cooldown 1 day).

You may complete as many transactions as you like subject to cooldown. You cannot hold more than one share at a time.

Example:
- prices = [1,2,3,0,2] → Answer = 3  
  Transactions: buy at 1 → sell at 3 (profit 2), cooldown at day 3, buy at 0 → sell at 2 (profit 2), total = 4?  
  But optimal per LC is 3 with sequence: buy(1), sell(2), cooldown, buy(0), sell(2).

------------------------------------------------------------------------

## 🪜 2. State Definition (Buy/Sell with cooldown on sell)

Let:
- `dp[i][buy]` = max profit starting from day `i`, where:
  - `buy == 1` → we are allowed to buy next
  - `buy == 0` → we are holding; we are allowed to sell next

Cooldown:
- When we sell on day `i`, we must skip day `i+1`, i.e., next state jumps to `i+2` with `buy=1`.

Answer:
- `dp[0][1]`

------------------------------------------------------------------------

## 🔁 3. Recurrence

Base cases:
- If `i >= N` → 0 (past the last day)

Transitions:
- If `buy == 1` (allowed to buy):
  - `dp[i][1] = max(-prices[i] + dp[i+1][0], 0 + dp[i+1][1])`
- If `buy == 0` (holding; allowed to sell):
  - `dp[i][0] = max(+prices[i] + dp[i+2][1], 0 + dp[i+1][0])`  // cooldown jump

------------------------------------------------------------------------

## 💻 4A. Memoized (Top-Down)

```java
import java.util.*;

class StockCooldown_Memo {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        Integer[][] dp = new Integer[n + 2][2]; // +2 to handle i+2 index safely
        return dfs(0, 1, prices, dp);
    }

    private int dfs(int i, int buy, int[] prices, Integer[][] dp) {
        if (i >= prices.length) return 0;
        if (dp[i][buy] != null) return dp[i][buy];

        if (buy == 1) {
            int take = -prices[i] + dfs(i + 1, 0, prices, dp); // buy
            int skip = 0 + dfs(i + 1, 1, prices, dp);          // wait
            return dp[i][buy] = Math.max(take, skip);
        } else {
            // sell then cooldown: jump to i+2
            int sell = prices[i] + dfs(i + 2, 1, prices, dp);  // sell
            int hold = 0 + dfs(i + 1, 0, prices, dp);          // wait
            return dp[i][buy] = Math.max(sell, hold);
        }
    }
}
```

Complexity:
- Time: O(N)
- Space: O(N) memo + recursion

------------------------------------------------------------------------

## 💻 4B. Tabulation (Bottom-Up)

```java
class StockCooldown_Tab {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n + 2][2]; // dp[i][buy], prefilled with 0

        for (int i = n - 1; i >= 0; i--) {
            // buy == 1
            dp[i][1] = Math.max(-prices[i] + dp[i + 1][0],
                                 0 + dp[i + 1][1]);
            // buy == 0 (holding)
            dp[i][0] = Math.max(+prices[i] + dp[i + 2][1],
                                 0 + dp[i + 1][0]);
        }
        return dp[0][1];
    }
}
```

------------------------------------------------------------------------

## 💻 4C. Space-Optimized (O(1) extra)

We need next day (i+1) and the day after (i+2). Track:
- `next1[2]` = dp[i+1][*]
- `next2[2]` = dp[i+2][*]
- `curr[2]`  = dp[i][*]

```java
class StockCooldown_Space {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int[] next1 = new int[2]; // dp[i+1]
        int[] next2 = new int[2]; // dp[i+2]
        int[] curr  = new int[2];

        for (int i = n - 1; i >= 0; i--) {
            // buy == 1
            curr[1] = Math.max(-prices[i] + next1[0], next1[1]);
            // buy == 0
            curr[0] = Math.max(+prices[i] + next2[1], next1[0]);

            // shift windows: next2 <- next1, next1 <- curr
            next2[0] = next1[0]; next2[1] = next1[1];
            next1[0] = curr[0];  next1[1] = curr[1];
        }
        return next1[1];
    }
}
```

------------------------------------------------------------------------

## 🔎 5. Dry Run (Brief)

prices = [1,2,3,0,2]
- Optimal: buy(1), sell(3), cooldown, buy(0), sell(2) → profit 3
- DP captures sell → jump (cooldown) flow; greedy fails for cooldown constraints, so DP is preferred.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Stocks with Cooldown
- Family: Stock DP with “skip-ahead” on action
- Triggers:
  - Must skip next day after selling (cooldown)
- Generalizes neatly in buy/sell DP framework

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- When indexing dp[i+2], ensure array size ≥ n+2
- Flat or decreasing arrays → 0
- Large N → prefer O(1) space version

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Cooldown is modeled by jumping to `i+2` after selling.
- O(N) time with O(1) extra space via rolling arrays.

------------------------------------------------------------------------

# End of Notes
