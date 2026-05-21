# Binary Search Notes

## 04 - Find Minimum in Rotated Sorted Array

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given a sorted array rotated at an unknown pivot, return the minimum element.

Example:

```text
Input: nums = [4,5,6,7,0,1,2]
Output: 0
```

------------------------------------------------------------------------

## 2. First Intuition

In a rotated sorted array, one half is sorted. The minimum of a sorted half is its first element.

So whenever we identify a sorted half:

- keep its smallest value as a possible answer
- continue searching the unsorted half, because the real minimum may be there

------------------------------------------------------------------------

## 3. Methodology

At each step:

- If `nums[low] <= nums[mid]`, left half is sorted.
  - Candidate answer: `nums[low]`
  - Search right half.
- Else right half is sorted.
  - Candidate answer: `nums[mid]`
  - Search left half.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int findMin(int[] nums) {
        int low = 0;
        int high = nums.length - 1;
        int ans = nums[0];

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[low] <= nums[mid]) {
                ans = Math.min(ans, nums[low]);
                low = mid + 1;
            } else {
                ans = Math.min(ans, nums[mid]);
                high = mid - 1;
            }
        }

        return ans;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `nums = [4,5,6,7,0,1,2]`:

- left half `[4,5,6,7]` is sorted, candidate `4`, search right
- now search space contains `[0,1,2]`
- candidate becomes `0`
- answer is `0`

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(log n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- For rotated arrays, sorted half gives a trustworthy candidate.
- Search the unsorted half because the pivot and minimum live there.
- If the current range is already sorted, `nums[low]` is the minimum for that range.

------------------------------------------------------------------------

## End of Notes
