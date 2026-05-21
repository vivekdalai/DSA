# Binary Search Notes

## 08 - Median of Two Sorted Arrays

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/median-of-two-sorted-arrays/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given two sorted arrays `a` and `b`, return the median of the combined sorted array.

The required time complexity is `O(log(min(n, m)))`.

Example:

```text
Input: a = [1,3], b = [2]
Output: 2.0
```

------------------------------------------------------------------------

## 2. First Intuition

Median divides the combined sorted data into two halves:

- left half contains the smaller elements
- right half contains the larger elements

We do not need to merge arrays. We only need to find a valid partition.

------------------------------------------------------------------------

## 3. Methodology

Binary search on the smaller array.

For a partition:

- `cut1` elements are taken from `a` into the left half
- `cut2` elements are taken from `b` into the left half
- `cut1 + cut2 = (n + m + 1) / 2`

Define:

```text
l1 = element just left of cut1 in a
r1 = element just right of cut1 in a
l2 = element just left of cut2 in b
r2 = element just right of cut2 in b
```

The partition is valid when:

```text
l1 <= r2 && l2 <= r1
```

Then:

- odd total length: median is `max(l1, l2)`
- even total length: median is average of `max(l1, l2)` and `min(r1, r2)`

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return findMedianSortedArrays(nums2, nums1);
        }

        int n = nums1.length;
        int m = nums2.length;
        int low = 0;
        int high = n;

        while (low <= high) {
            int cut1 = low + (high - low) / 2;
            int cut2 = (n + m + 1) / 2 - cut1;

            int l1 = cut1 == 0 ? Integer.MIN_VALUE : nums1[cut1 - 1];
            int r1 = cut1 == n ? Integer.MAX_VALUE : nums1[cut1];

            int l2 = cut2 == 0 ? Integer.MIN_VALUE : nums2[cut2 - 1];
            int r2 = cut2 == m ? Integer.MAX_VALUE : nums2[cut2];

            if (l1 <= r2 && l2 <= r1) {
                if ((n + m) % 2 == 0) {
                    return (Math.max(l1, l2) + Math.min(r1, r2)) / 2.0;
                }
                return Math.max(l1, l2);
            }

            if (l1 > r2) {
                high = cut1 - 1;
            } else {
                low = cut1 + 1;
            }
        }

        return 0.0;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `a = [1,3]`, `b = [2]`:

- Binary search on smaller array `[2]`.
- Valid partition puts `[1,2]` on the left and `[3]` on the right.
- Total length is odd, so median is max of left side: `2`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(log(min(n, m)))`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This is binary search on partition, not on value.
- Always binary search on the smaller array.
- Sentinels `Integer.MIN_VALUE` and `Integer.MAX_VALUE` avoid edge-case-heavy code.

------------------------------------------------------------------------

## End of Notes
