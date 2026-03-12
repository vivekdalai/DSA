# Dynamic Programming Notes

## 52 - Best Time to Buy and Sell Stock IV (At Most K Transactions) — LC 188

**Generated on:** 2026-02-25 23:27:37 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `prices[]` and an integer `K`, return the maximum profit you can achieve with at most `K` transactions.  
Each transaction is a buy followed by a sell; you cannot hold more than one share at a time.

Special case:
- If `K >= n/2`, the constraint is effectively unlimited transactions → reduce to Stock II (sum all positive deltas).

------------------------------------------------------------------------

## 🪜 2. Approaches

A) Transactions-Left DP (buy/sell state + remaining sells)  
- `dp[i][buy][t]`: max profit from day `i` onward, where:
  - `buy ∈ {0,1}` (1=can buy next, 0=must sell next)
  - `t ∈ {0..K}` = number of sells remaining
- Transitions:
  - If buy==1: `max(-p[i] + dp[i+1][0][t], 0 + dp[i+1][1][t])`
  - If buy==0: `max(+p[i] + dp[i+1][1][t-1], 0 + dp[i+1][0][t])`
- Answer: `dp[0][1][K]`

B) Operation-Count DP (0..2K-1 ops, even=buy, odd=sell)  
- `dp[i][op]`: max profit from day `i` onward with operation index `op` (0=first buy, 1=first sell, 2=second buy, …)
- Transition:
  - If `op` even (buy): `max(-p[i] + dp[i+1][op+1], dp[i+1][op])`
  - If `op` odd (sell): `max(+p[i] + dp[i+1][op+1], dp[i+1][op])`
- Stop when `op == 2K`
- Answer: `dp[0][0]`

Both are O(n*K) time and O(K) space with rolling arrays.

Greedy shortcut:
- If `K >= n/2`, return Stock II result: sum of `max(0, prices[i]-prices[i-1])`.

------------------------------------------------------------------------

## 💻 3A. Optimized Solution (Operation-Count, O(n*K) time, O(K) space)

```java
import java.util.*;

public class StockIV_OpCount {
    public int maxProfit(int K, int[] prices) {
        int n = prices.length;
        if (n <= 1 || K == 0) return 0;

        // Greedy shortcut: effectively unlimited transactions
        if (K >= n / 2) {
            int profit = 0;
            for (int i = 1; i < n; i++) {
                if (prices[i] > prices[i - 1]) {
                    profit += prices[i] - prices[i - 1];
                }
            }
            return profit;
        }

        // dpNext/opCount rolling: dpNext[op] represents dp[i+1][op]
        int ops = 2 * K;
        int[] next = new int[ops + 1]; // dp at i+1
        int[] curr = new int[ops + 1]; // dp at i

        for (int i = n - 1; i >= 0; i--) {
            Arrays.fill(curr, 0);
            for (int op = 0; op < ops; op++) {
                if ((op & 1) == 0) {
                    // buy op
                    int buy = -prices[i] + next[op + 1];
                    int skip = next[op];
                    curr[op] = Math.max(buy, skip);
                } else {
                    // sell op
                    int sell = prices[i] + next[op + 1];
                    int hold = next[op];
                    curr[op] = Math.max(sell, hold);
                }
            }
            // swap
            int[] tmp = next; next = curr; curr = tmp;
        }
        return next[0];
    }

    public static void main(String[] args) {
        StockIV_OpCount solver = new StockIV_OpCount();
        System.out.println(solver.maxProfit(2, new int[]{3,2,6,5,0,3})); // 7
        System.out.println(solver.maxProfit(2, new int[]{2,4,1}));       // 2
    }
}
```

------------------------------------------------------------------------

## 💻 3B. Transactions-Left DP (Memoized Top-Down)

```java
import java.util.*;

class StockIV_Memo {
    private Integer[][][] memo;
    private int[] prices;

    public int maxProfit(int K, int[] prices) {
        int n = prices.length;
        if (n <= 1 || K == 0) return 0;

        // Greedy shortcut
        if (K >= n / 2) {
            int profit = 0;
            for (int i = 1; i < n; i++) {
                if (prices[i] > prices[i - 1]) profit += prices[i] - prices[i - 1];
            }
            return profit;
        }

        this.prices = prices;
        memo = new Integer[n + 1][2][K + 1];
        return dfs(0, 1, K);
    }

    private int dfs(int i, int buy, int t) {
        if (i == prices.length || t == 0) return 0;
        if (memo[i][buy][t] != null) return memo[i][buy][t];

        int best;
        if (buy == 1) {
            int take = -prices[i] + dfs(i + 1, 0, t);
            int skip = dfs(i + 1, 1, t);
            best = Math.max(take, skip);
        } else {
            int sell = prices[i] + dfs(i + 1, 1, t - 1);
            int hold = dfs(i + 1, 0, t);
            best = Math.max(sell, hold);
        }
        return memo[i][buy][t] = best;
    }
}
```

------------------------------------------------------------------------

## 🔎 4. Dry Run Sketch

prices = [3,2,6,5,0,3], K=2  
- Operation-count:
  - ops = 4 (buy,sell,buy,sell)
  - Working backward, dp accumulates optimal picks:
    - First transaction: buy at 2, sell at 6 → +4
    - Second transaction: buy at 0, sell at 3 → +3
  - Total = 7

------------------------------------------------------------------------

## 🏷 5. Pattern Recognition

- Name: Stocks IV — K transactions
- Family: Stock DP generalized from II/III:
  - Operation index (0..2K) or transactions-left state
- Triggers:
  - At most K transactions
  - If K large relative to n, reduce to unlimited (Stock II)

------------------------------------------------------------------------

## 🔄 6. Edge Cases and Pitfalls

- K=0 or n<=1 → 0
- K >= n/2 → reduce to Stock II (greedy)
- Ensure sell reduces transactions (in transactions-left DP)
- Watch array bounds for op index (0..2K) in op-count DP

------------------------------------------------------------------------

## ✅ 7. Takeaway

- Two clean formulations:
  - Operation-count dp[i][op] (space O(K))
  - Transactions-left dp[i][buy][t] (space O(K))
- Time O(n*K); handle K>=n/2 shortcut for performance.

------------------------------------------------------------------------

# End of Notes
