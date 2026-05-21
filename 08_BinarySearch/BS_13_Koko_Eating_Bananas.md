# Binary Search Notes

## 13 - Koko Eating Bananas

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/koko-eating-bananas/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given piles of bananas and `h` hours, find the minimum eating speed `k` such that Koko can finish all bananas within `h` hours.

Example:

```text
Input: piles = [3,6,7,11], h = 8
Output: 4
```

------------------------------------------------------------------------

## 2. First Intuition

If Koko can finish at speed `k`, she can also finish at any speed greater than `k`.

So the condition is monotonic:

```text
too slow -> possible answer boundary -> fast enough
```

We binary search the minimum valid speed.

------------------------------------------------------------------------

## 3. Methodology

Search space:

- `low = 1`
- `high = max(piles)`

For a speed `k`, time needed for one pile is:

```text
ceil(pile / k)
```

If total hours `<= h`, speed works, so try smaller.
Else speed is too slow, so try bigger.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        int low = 1;
        int high = 0;

        for (int pile : piles) {
            high = Math.max(high, pile);
        }

        int ans = high;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (canFinish(piles, h, mid)) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return ans;
    }

    private boolean canFinish(int[] piles, int h, int speed) {
        long hours = 0;

        for (int pile : piles) {
            hours += (pile + speed - 1) / speed;
        }

        return hours <= h;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `piles = [3,6,7,11]`, `h = 8`:

- speed `4`: hours are `1 + 2 + 2 + 3 = 8`, possible.
- speed `3`: hours are `1 + 2 + 3 + 4 = 10`, not possible.
- Minimum valid speed is `4`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n log maxPile)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This is binary search on answer.
- Trigger words: minimum speed, finish within H hours.
- The check function computes whether a candidate speed is enough.

------------------------------------------------------------------------

## End of Notes
