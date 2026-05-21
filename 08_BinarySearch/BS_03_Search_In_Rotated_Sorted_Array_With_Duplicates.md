# Binary Search Notes

## 03 - Search in Rotated Sorted Array with Duplicates

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/search-in-rotated-sorted-array-ii/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given a rotated sorted array that may contain duplicates, return whether `target` exists.

Example:

```text
Input: nums = [2,5,6,0,0,1,2], target = 0
Output: true
```

------------------------------------------------------------------------

## 2. First Intuition

This is the same as searching in a rotated sorted array, but duplicates create one confusing case:

```text
nums[low] == nums[mid] == nums[high]
```

In that case, we cannot confidently say whether the left half or right half is sorted. So we shrink both ends by one and continue.

------------------------------------------------------------------------

## 3. Methodology

At each step:

1. If `nums[mid] == target`, return `true`.
2. If `nums[low] == nums[mid] == nums[high]`, do `low++` and `high--`.
3. Else use the normal rotated-array logic:
   - identify the sorted half
   - check if `target` lies inside it
   - eliminate the other half

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public boolean search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                return true;
            }

            if (nums[low] == nums[mid] && nums[mid] == nums[high]) {
                low++;
                high--;
                continue;
            }

            if (nums[low] <= nums[mid]) {
                if (nums[low] <= target && target < nums[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else {
                if (nums[mid] < target && target <= nums[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }

        return false;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `nums = [3,3,3,1,3,3,3]`, `target = 1`:

- Several steps may hit `nums[low] == nums[mid] == nums[high]`.
- We shrink both sides until the useful rotated structure becomes visible.
- Then the usual sorted-half logic finds `1`.

------------------------------------------------------------------------

## 6. Complexity

- Average Time: `O(log n)`
- Worst Time: `O(n)` because duplicates can force one-by-one shrinking
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Duplicates can destroy the guarantee that one half is clearly sorted.
- The confusing case is handled by shrinking both ends.
- Apart from that one case, the algorithm remains the same as rotated binary search.

------------------------------------------------------------------------

## End of Notes
