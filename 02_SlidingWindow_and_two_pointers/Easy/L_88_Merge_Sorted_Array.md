# Two Pointers and Sliding Window Notes

## 88 - Merge Sorted Array

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/merge-sorted-array/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given two integer arrays `nums1` and `nums2`, sorted in **non-decreasing order**, and two integers `m` and `n`, representing the number of elements in `nums1` and `nums2` respectively.

**Merge** `nums1` and `nums2` into a single array sorted in **non-decreasing order**.

The final sorted array should not be returned by the function, but instead be stored inside the array `nums1`. To accommodate this, `nums1` has a length of `m + n`, where the first `m` elements denote the elements that should be merged, and the last `n` elements are set to `0` and should be ignored. `nums2` has a length of `n`.

**Example 1:**

```text
Input: nums1 = [1,2,3,0,0,0], m = 3, nums2 = [2,5,6], n = 3
Output: [1,2,2,3,5,6]
Explanation: The arrays we are merging are [1,2,3] and [2,5,6].
The result of the merge is [1,2,2,3,5,6] with the underlined elements coming from nums1.
```

**Example 2:**

```text
Input: nums1 = [1], m = 1, nums2 = [], n = 0
Output: [1]
Explanation: The arrays we are merging are [1] and [].
The result of the merge is [1].
```

**Example 3:**

```text
Input: nums1 = [0], m = 0, nums2 = [1], n = 1
Output: [1]
Explanation: The arrays we are merging are [] and [1].
The result of the merge is [1].
Note that because m = 0, there are no elements in nums1. The 0 is only there to ensure the merge result can fit in nums1.
```

**Constraints:**

- `nums1.length == m + n`

- `nums2.length == n`

- `0 <= m, n <= 200`

- `1 <= m + n <= 200`

- `-109 <= nums1[i], nums2[j] <= 109`

**Follow up:**Can you come up with an algorithm that runs in `O(m + n)` time?

------------------------------------------------------------------------

## 2. First Intuition

`nums1` has empty space at the end. Filling from the back avoids overwriting unmerged values at the front.

------------------------------------------------------------------------

## 3. Merge from the Back

- `l` starts at the last real element of `nums1`.
- `r` starts at the last element of `nums2`.
- `p` writes into the last available slot of `nums1`.
- Place the larger of `nums1[l]` and `nums2[r]`, then move that pointer backward.

------------------------------------------------------------------------

## 4. Short Dry Run

For `nums1 = [1,2,3,0,0,0]`, `m = 3`, `nums2 = [2,5,6]`: write `6`, then `5`, then `3`, then `2`; the final array is `[1,2,2,3,5,6]`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public void merge(int[] nums1, int m, int[] nums2, int n) {
    int i = m - 1;
    int j = n - 1;
    int write = m + n - 1;

    while (j >= 0) {
        if (i >= 0 && nums1[i] > nums2[j]) {
            nums1[write--] = nums1[i--];
        } else {
            nums1[write--] = nums2[j--];
        }
    }
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(m + n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- When one array has trailing capacity, merge backward.
- The loop only needs to run while `nums2` still has values.

------------------------------------------------------------------------

## End of Notes
