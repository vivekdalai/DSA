# Dynamic Programming Notes

## 37 - Best Time to Buy and Sell Stock With Transaction Fee (LC 714)

**Generated on:** 2026-02-24 21:33:34 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `prices` where `prices[i]` is the price of a stock on day `i`, and an integer `fee` representing a transaction fee:
- You may complete as many transactions as you like.
- Each sell operation incurs the fee (or equivalently, you can apply the fee to buy — both are fine as long as you’re consistent).
- You cannot hold more than one share at a time.

Goal: Maximize total profit.

Example:
- prices = [1,3,2,8,4,9], fee = 2 → Answer = 8  
  One optimal sequence: buy(1), sell(8) → +7 - 2 = +5; buy(4), sell(9) → +5 - 2 = +3; total = 8.

------------------------------------------------------------------------

## 🪜 2. State Definition (Buy/Sell DP)

Let:
- `dp[i][buy]` = max profit from day `i` to end if
  - `buy == 1`: allowed to buy next (not holding)
  - `buy == 0`: currently holding (must sell next to realize profit)

Answer:
- `dp[0][1]`

Fee handling:
- Apply the fee when selling:
  - On sell at price `p`, realized profit = `p - fee`.

------------------------------------------------------------------------

## 🔁 3. Recurrence

Base:
- If `i == N` (past last day) → 0

Transitions:
- If `buy == 1` (allowed to buy):
  - `dp[i][1] = max(-prices[i] + dp[i+1][0], 0 + dp[i+1][1])`
- If `buy == 0` (holding; allowed to sell):
  - `dp[i][0] = max(+prices[i] - fee + dp[i+1][1], 0 + dp[i+1][0])`

------------------------------------------------------------------------

## 💻 4A. Memoized (Top-Down)

```java
import java.util.*;

class StockWithFee_Memo {
    public int maxProfit(int[] prices, int fee) {
        int n = prices.length;
        Integer[][] dp = new Integer[n + 1][2]; // dp[i][buy]
        return dfs(0, 1, prices, fee, dp);
    }

    private int dfs(int i, int buy, int[] prices, int fee, Integer[][] dp) {
        if (i == prices.length) return 0;
        if (dp[i][buy] != null) return dp[i][buy];

        if (buy == 1) {
            int take = -prices[i] + dfs(i + 1, 0, prices, fee, dp); // buy
            int skip = 0 + dfs(i + 1, 1, prices, fee, dp);          // wait
            return dp[i][buy] = Math.max(take, skip);
        } else {
            int sell = prices[i] - fee + dfs(i + 1, 1, prices, fee, dp); // sell with fee
            int hold = 0 + dfs(i + 1, 0, prices, fee, dp);               // wait
            return dp[i][buy] = Math.max(sell, hold);
        }
    }
}
```

Complexity:
- Time: O(N)
- Space: O(N) recursion + memo

------------------------------------------------------------------------

## 💻 4B. Tabulation (O(N))

```java
class StockWithFee_Tab {
    public int maxProfit(int[] prices, int fee) {
        int n = prices.length;
        int[][] dp = new int[n + 1][2]; // dp[i][buy] default 0

        for (int i = n - 1; i >= 0; i--) {
            // buy == 1
            dp[i][1] = Math.max(-prices[i] + dp[i + 1][0],
                                 0 + dp[i + 1][1]);
            // buy == 0
            dp[i][0] = Math.max(+prices[i] - fee + dp[i + 1][1],
                                 0 + dp[i + 1][0]);
        }
        return dp[0][1];
    }
}
```

------------------------------------------------------------------------

## 💻 4C. Space-Optimized (O(1) extra)

Use two scalars for next-day states:

```java
class StockWithFee_Space {
    public int maxProfit(int[] prices, int fee) {
        int nextBuy = 0, nextSell = 0; // dp[i+1][1], dp[i+1][0]

        for (int i = prices.length - 1; i >= 0; i--) {
            int currBuy  = Math.max(-prices[i] + nextSell, nextBuy);
            int currSell = Math.max(+prices[i] - fee + nextBuy, nextSell);

            nextBuy = currBuy;
            nextSell = currSell;
        }
        return nextBuy; // dp[0][1]
    }
}
```

Alternative iterative (hold/cash) form:
- `cash` = profit not holding a stock
- `hold` = profit holding a stock
- Transitions:
  - `cash = max(cash, hold + price - fee)`
  - `hold = max(hold, cash - price)`
Initialize `cash = 0`, `hold = -prices[0]`.

```java
class StockWithFee_HoldCash {
    public int maxProfit(int[] prices, int fee) {
        int cash = 0;
        int hold = -prices[0];
        for (int i = 1; i < prices.length; i++) {
            cash = Math.max(cash, hold + prices[i] - fee);
            hold = Math.max(hold, cash - prices[i]);
        }
        return cash;
    }
}
```

Note: In the hold/cash method, the order of updates uses previous `cash` for hold update; this in-place works because `cash` for the current day is computed from previous `hold`, and `hold` for the current day is then computed from the (updated) `cash` which is logically “current cash.” Alternatively, store prevCash/prevHold to be explicit.

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

prices = [1,3,2,8,4,9], fee = 2

Intuition:
- Take big gaps to amortize the fee:
  - buy 1 → sell 8: profit 7 - 2 = 5
  - buy 4 → sell 9: profit 5 - 2 = 3
  - total = 8 (optimal)

DP states choose to skip small gains when they don’t overcome the fee.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Stocks with Fee
- Family: Stock DP (buy/sell states)
- Triggers:
  - Each sell incurs a constant transaction fee
  - Unlimited transactions otherwise

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Apply the fee consistently (sell or buy, but not both)
- Flat or small fluctuations relative to fee → likely 0 profit
- Large inputs → use O(1) space iterative forms

------------------------------------------------------------------------

## ✅ 8. Takeaway

- The fee modifies the sell transition by `-fee`.
- O(N) time and O(1) space with buy/sell (or hold/cash) templates.
- Choose the iterative hold/cash form for concise production code.

------------------------------------------------------------------------

# End of Notes
