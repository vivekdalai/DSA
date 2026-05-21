# Binary Search Notes

## 05 - Find Peak Element

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/find-peak-element/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

A peak element is an element that is strictly greater than its neighbors.

Given `nums`, return the index of any peak element. Assume:

```text
nums[-1] = -infinity
nums[n] = -infinity
```

Example:

```text
Input: nums = [1,2,3,1]
Output: 2
```

------------------------------------------------------------------------

## 2. First Intuition

If `nums[mid] < nums[mid + 1]`, the array is rising to the right, so a peak must exist on the right side.

Why? Because either it keeps rising until the end, where the last element is a peak, or it eventually falls and creates a peak.

Similarly, if `nums[mid] > nums[mid + 1]`, a peak exists on the left side including `mid`.

------------------------------------------------------------------------

## 3. Methodology

There are two common versions.

The cleanest version compares `mid` with `mid + 1`:

- If `nums[mid] < nums[mid + 1]`, move right.
- Else move left, keeping `mid` as a possible peak.

When `low == high`, that index is a peak.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int findPeakElement(int[] nums) {
        int low = 0;
        int high = nums.length - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] < nums[mid + 1]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `nums = [1,2,3,1]`:

- `mid = 1`, `nums[mid] = 2`, `nums[mid + 1] = 3`
- rising to the right, so move `low = 2`
- search ends at index `2`
- `nums[2] = 3` is a peak

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(log n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This is binary search on slope direction, not on exact sorted values.
- Move toward the side that guarantees a peak.
- We only need any peak, so we can safely discard half the array.

------------------------------------------------------------------------

## End of Notes
