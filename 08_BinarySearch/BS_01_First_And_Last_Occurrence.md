# Binary Search Notes

## 01 - First and Last Occurrence of Target

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given a sorted integer array `nums` and a value `target`, return the first and last index where `target` appears.

If `target` is not present, return `[-1, -1]`.

Example:

```text
Input: nums = [5,7,7,8,8,10], target = 8
Output: [3,4]
```

------------------------------------------------------------------------

## 2. First Intuition

Normal binary search stops as soon as it finds `target`. Here that is not enough because there may be more occurrences on both sides.

So when we find `target`, we save it as a possible answer and keep searching:

- for first occurrence: move left
- for last occurrence: move right

------------------------------------------------------------------------

## 3. Methodology

Use binary search twice.

First binary search:

- If `nums[mid] == target`, save `mid` and move `high = mid - 1`.
- This asks: "Can I find the same target even earlier?"

Second binary search:

- If `nums[mid] == target`, save `mid` and move `low = mid + 1`.
- This asks: "Can I find the same target even later?"

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int[] searchRange(int[] nums, int target) {
        return new int[] {
            firstOccurrence(nums, target),
            lastOccurrence(nums, target)
        };
    }

    private int firstOccurrence(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                ans = mid;
                high = mid - 1;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return ans;
    }

    private int lastOccurrence(int[] nums, int target) {
        int low = 0;
        int high = nums.length - 1;
        int ans = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (nums[mid] == target) {
                ans = mid;
                low = mid + 1;
            } else if (nums[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return ans;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `nums = [5,7,7,8,8,10]`, `target = 8`:

First occurrence:

- find `8` at index `4`, save `4`, search left
- find `8` at index `3`, save `3`, search left
- answer becomes `3`

Last occurrence:

- find `8` at index `4`, save `4`, search right
- no later `8`
- answer becomes `4`

Final answer: `[3, 4]`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(log n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Use this when a sorted array asks for boundary positions.
- Finding first or last occurrence is the same as normal binary search, except we continue after finding the answer.
- This is a lower-bound / upper-bound style problem.

------------------------------------------------------------------------

## End of Notes
