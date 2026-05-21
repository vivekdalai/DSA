# Binary Search Notes

## 02 - Search in Rotated Sorted Array

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/search-in-rotated-sorted-array/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given a sorted array that has been rotated at some pivot, find the index of `target`.

If `target` is not present, return `-1`.

Example:

```text
Input: nums = [4,5,6,7,0,1,2], target = 0
Output: 4
```

------------------------------------------------------------------------

## 2. First Intuition

In a rotated sorted array, the whole array is not sorted, but at every `mid`, at least one half is sorted.

The key is:

- identify the sorted half
- check if `target` lies inside that sorted half
- eliminate the other half

------------------------------------------------------------------------

## 3. Methodology

At each binary search step:

1. If `nums[mid] == target`, return `mid`.
2. If left half is sorted: `nums[low] <= nums[mid]`
   - If `target` lies between `nums[low]` and `nums[mid]`, move left.
   - Else, move right.
3. Else right half is sorted.
   - If `target` lies between `nums[mid]` and `nums[high]`, move right.
   - Else, move left.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int search(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                return mid;
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

        return -1;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `nums = [4,5,6,7,0,1,2]`, `target = 0`:

- `mid = 3`, value `7`
- left half `[4,5,6,7]` is sorted
- `0` is not in that sorted half, so search right
- next search space is `[0,1,2]`
- `target` is found at index `4`

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(log n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Rotation breaks global sorting but keeps one side sorted at every step.
- Always identify the sorted side first.
- Eliminate only after checking whether `target` lies in the sorted side.

------------------------------------------------------------------------

## End of Notes
