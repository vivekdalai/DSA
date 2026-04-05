# Greedy Notes

## Best Time to Buy and Sell Stock II

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given an array `prices`, where `prices[i]` is the stock price on day `i`.

You may complete as many transactions as you like, but you cannot hold more than one stock at a time.

Return the maximum profit.

**Example 1**

    Input: prices = [7,1,5,3,6,4]
    Output: 7

One optimal plan is:

- buy at `1`, sell at `5` -> profit `4`
- buy at `3`, sell at `6` -> profit `3`

**Example 2**

    Input: prices = [1,2,3,4,5]
    Output: 4

------------------------------------------------------------------------

## 2. File Logic vs. Greedy Shortcut

The file solves the problem with a `buy / sell` state DP and memoization.

That is correct, but this problem has a simpler greedy collapse:

- every upward step can be taken as profit

So:

    profit += max(0, prices[i] - prices[i - 1])

Both views lead to the same answer.

------------------------------------------------------------------------

## 3. Why The Greedy Shortcut Works

Suppose prices rise like:

    1 -> 3 -> 5

Taking one transaction:

    buy at 1, sell at 5 = 4

Taking every positive rise:

    (3 - 1) + (5 - 3) = 4

So capturing all local rises is equivalent to capturing the whole rising segment.

------------------------------------------------------------------------

## 4. DP View In The File

State:

- `idx` = current day
- `buy = 1` means we may buy now
- `buy = 0` means we currently hold a stock and may sell

Transition:

- if `buy == 1`, choose between buying or skipping
- if `buy == 0`, choose between selling or skipping

Memoization stores the best profit from each state.

------------------------------------------------------------------------

## 5. Greedy Interview Version

```java
public int maxProfit(int[] prices) {
    int profit = 0;

    for (int i = 1; i < prices.length; i++) {
        if (prices[i] > prices[i - 1]) {
            profit += prices[i] - prices[i - 1];
        }
    }

    return profit;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)` for the greedy version

Pattern:

- unlimited transactions
- no fee
- sum every positive slope

------------------------------------------------------------------------

## End of Notes
