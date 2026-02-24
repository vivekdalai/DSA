# Dynamic Programming Notes

## 32 - Best Time to Buy and Sell Stock I (Single Transaction)

**Generated on:** 2026-02-24 20:59:05 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `prices[]` where `prices[i]` is the price of a stock on day `i`, find the maximum profit you can achieve from exactly one buy and one sell (buy before sell).  
If no profit is possible, return `0`.

Example:
- prices = [7,1,5,3,6,4] → Answer = 5 (buy at 1, sell at 6)
- prices = [7,6,4,3,1] → Answer = 0 (no profitable trade)

------------------------------------------------------------------------

## 🪜 2. Key Insight (Greedy/1D DP)

The best sell on day `j` uses the minimum buy price on any day `< j`.

Track:
- `minPriceSoFar` as we scan left to right
- `maxProfit = max(maxProfit, prices[i] - minPriceSoFar)`

This is equivalent to a 1D DP where the subproblem at `i` is the best profit ending at `i`.

------------------------------------------------------------------------

## 🔁 3. Recurrence (Intuition)

Let:
- `minPrice[i]` = minimum price in `prices[0..i]`
- `best[i]` = max profit achievable by selling on/before `i` (one transaction)

Then:
- `minPrice[i] = min(minPrice[i-1], prices[i])`
- `best[i] = max(best[i-1], prices[i] - minPrice[i-1])`

Space can be O(1) by keeping only running minima and max profit.

------------------------------------------------------------------------

## 💻 4. Clean Interview Version (O(n), O(1))

```java
class BestTimeToBuySellI {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length == 0) return 0;

        int minPriceSoFar = prices[0];
        int maxProfit = 0;

        for (int i = 1; i < prices.length; i++) {
            int curr = prices[i];
            maxProfit = Math.max(maxProfit, curr - minPriceSoFar);
            minPriceSoFar = Math.min(minPriceSoFar, curr);
        }
        return maxProfit;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 💻 5. Optional DP Formulation (Buy/Sell States)

Although overkill for a single transaction, you can use the buy/sell state template:

- `hold`  = max profit while holding a stock after day i
- `sold`  = max profit while not holding a stock after day i (<= 1 sell)

For single transaction:
```
hold = max(hold, -price)        // buy or keep holding
sold = max(sold, hold + price)  // sell or keep not holding
```
Initialize `hold = -∞` (or `-prices[0]`), `sold = 0`.  
Final answer is `max(0, sold)`.

```java
class BestTimeToBuySellI_DP {
    public int maxProfit(int[] prices) {
        int hold = Integer.MIN_VALUE / 2;
        int sold = 0;

        for (int p : prices) {
            hold = Math.max(hold, -p);
            sold = Math.max(sold, hold + p);
        }
        return Math.max(0, sold);
    }
}
```

------------------------------------------------------------------------

## 🔎 6. Dry Run Example

prices = [7,1,5,3,6,4]
- minPriceSoFar = 7, maxProfit = 0
- i=1: price=1 → maxProfit=max(0, 1-7=-6)=0; min=1
- i=2: price=5 → maxProfit=max(0, 5-1=4)=4; min=1
- i=3: price=3 → maxProfit=max(4, 3-1=2)=4; min=1
- i=4: price=6 → maxProfit=max(4, 6-1=5)=5; min=1
- i=5: price=4 → maxProfit=max(5, 4-1=3)=5; min=1

Answer = 5.

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: “Track Best Buy So Far”
- Family: 1D DP / Greedy with running minima
- Triggers:
  - One buy and one sell, maximize (sell - buy)
  - “Best suffix/prefix after a local choice”

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- Monotonically decreasing prices → return 0
- Single day or empty array → 0
- Ensure buy before sell (tracking min-so-far ensures order)
- Don’t confuse with:
  - Multiple transactions allowed (Stock II)
  - At most two transactions (Stock III)
  - Cooldown constraint
  - Transaction fee

------------------------------------------------------------------------

## 🔁 9. Useful Variations

- Return the actual buy/sell days:
  - Track indices when updating `minPriceSoFar` and `maxProfit`
- If negative profit allowed (shorting): different problem
- If K transactions or other constraints: see subsequent notes:
  - Stock II (infinite transactions)
  - Stock III (at most two transactions)
  - Stock with cooldown
  - Stock with transaction fee

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Single pass with `minPriceSoFar` is optimal and simplest.
- State-DP (hold/sold) is a general pattern; for single transaction, the greedy reduces to it.

------------------------------------------------------------------------

# End of Notes
