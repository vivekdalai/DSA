# Greedy Notes

## Best Time to Buy and Sell Stock with Transaction Fee

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given an array `prices` and an integer `fee`.

You may complete as many transactions as you like, but each sale costs the given transaction fee.

Return the maximum profit.

**Example 1**

    Input: prices = [1,3,2,8,4,9], fee = 2
    Output: 8

**Example 2**

    Input: prices = [1,3,7,5,10,3], fee = 3
    Output: 6

------------------------------------------------------------------------

## 2. File Logic vs. Optimized State View

The file uses memoization with two states:

- `buy = 1` means we may buy
- `buy = 0` means we hold a stock and may sell

That is correct.

The optimized iterative version compresses the same idea into:

- `hold` = best profit while holding a stock
- `sell` = best profit while not holding a stock

------------------------------------------------------------------------

## 3. Transition

If we hold a stock today, either:

- we were already holding
- or we buy today from the previous `sell` state

If we do not hold a stock today, either:

- we were already not holding
- or we sell today and pay the fee

So:

    hold = max(hold, sell - price)
    sell = max(sell, hold + price - fee)

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    prices = [1,3,2,8,4,9], fee = 2

Best trades are:

- buy at `1`, sell at `8` -> profit `5`
- buy at `4`, sell at `9` -> profit `3`

Total:

    8

------------------------------------------------------------------------

## 5. Optimized Interview Version

```java
public int maxProfit(int[] prices, int fee) {
    int hold = -prices[0];
    int sell = 0;

    for (int i = 1; i < prices.length; i++) {
        int newHold = Math.max(hold, sell - prices[i]);
        int newSell = Math.max(sell, hold + prices[i] - fee);
        hold = newHold;
        sell = newSell;
    }

    return sell;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)` for the optimized state version

Pattern:

- buy/sell state DP
- compress states when each day depends only on the previous day

------------------------------------------------------------------------

## End of Notes
