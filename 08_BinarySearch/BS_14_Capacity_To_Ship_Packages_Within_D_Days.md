# Binary Search Notes

## 14 - Capacity to Ship Packages Within D Days

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given package weights in order and `days`, find the minimum ship capacity needed to ship all packages within `days`.

The order of packages cannot be changed.

Example:

```text
Input: weights = [1,2,3,4,5,6,7,8,9,10], days = 5
Output: 15
```

------------------------------------------------------------------------

## 2. First Intuition

This is similar to allocate pages.

If capacity `C` can ship all packages within `days`, then any larger capacity also works.

So we binary search for the smallest valid capacity.

------------------------------------------------------------------------

## 3. Methodology

Search space:

- `low = max(weights)` because the ship must carry the heaviest package
- `high = sum(weights)` because one day can carry all packages

Check function:

- greedily load packages in order
- when adding a package exceeds capacity, start a new day
- count required days

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int shipWithinDays(int[] weights, int days) {
        int low = 0;
        int high = 0;

        for (int weight : weights) {
            low = Math.max(low, weight);
            high += weight;
        }

        int ans = high;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (requiredDays(weights, mid) <= days) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return ans;
    }

    private int requiredDays(int[] weights, int capacity) {
        int days = 1;
        int currentLoad = 0;

        for (int weight : weights) {
            if (currentLoad + weight > capacity) {
                days++;
                currentLoad = weight;
            } else {
                currentLoad += weight;
            }
        }

        return days;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `weights = [1,2,3,4,5,6,7,8,9,10]`, `days = 5`:

- Capacity `15` works in exactly 5 days.
- Capacity `14` needs more than 5 days.
- Minimum valid capacity is `15`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n log sum)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This is a classic minimize-maximum problem.
- Same family: allocate pages, split array largest sum, painter partition.
- Order cannot be changed, so greedy grouping is valid for the check.

------------------------------------------------------------------------

## End of Notes
