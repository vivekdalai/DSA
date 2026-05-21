# Binary Search Notes

## 11 - Single Element in a Sorted Array

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/single-element-in-a-sorted-array/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given a sorted array where every element appears exactly twice except one element that appears once, return the single element.

Example:

```text
Input: nums = [1,1,2,3,3,4,4,8,8]
Output: 2
```

------------------------------------------------------------------------

## 2. First Intuition

Before the single element, pairs start at even indices:

```text
0,1  2,3  4,5
```

After the single element, this pairing pattern breaks and pairs start at odd indices.

So we binary search for the first place where the pair pattern breaks.

------------------------------------------------------------------------

## 3. Methodology

Use binary search and force `mid` to be even.

- If `nums[mid] == nums[mid + 1]`, the single element is on the right.
- Else the single element is at `mid` or on the left.

When `low == high`, that index is the answer.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int singleNonDuplicate(int[] nums) {
        int low = 0;
        int high = nums.length - 1;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (mid % 2 == 1) {
                mid--;
            }

            if (nums[mid] == nums[mid + 1]) {
                low = mid + 2;
            } else {
                high = mid;
            }
        }

        return nums[low];
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `nums = [1,1,2,3,3,4,4,8,8]`:

- `mid = 4`, make it even, pair at `4,5` is `3,4`, broken.
- So answer is at `4` or left.
- Continue until search lands on index `2`.
- Answer is `2`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(log n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This is binary search on index parity.
- Look for where a structural pattern changes.
- The sorted array is useful because equal elements stay adjacent.

------------------------------------------------------------------------

## End of Notes
