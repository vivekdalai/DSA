# Two Pointers and Sliding Window Notes

## 31 - Next Permutation

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/next-permutation/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

A **permutation** of an array of integers is an arrangement of its members into a sequence or linear order.

- For example, for `arr = [1,2,3]`, the following are all the permutations of `arr`: `[1,2,3], [1,3,2], [2, 1, 3], [2, 3, 1], [3,1,2], [3,2,1]`.

The **next permutation** of an array of integers is the next lexicographically greater permutation of its integer. More formally, if all the permutations of the array are sorted in one container according to their lexicographical order, then the **next permutation** of that array is the permutation that follows it in the sorted container. If such arrangement is not possible, the array must be rearranged as the lowest possible order (i.e., sorted in ascending order).

- For example, the next permutation of `arr = [1,2,3]` is `[1,3,2]`.

- Similarly, the next permutation of `arr = [2,3,1]` is `[3,1,2]`.

- While the next permutation of `arr = [3,2,1]` is `[1,2,3]` because `[3,2,1]` does not have a lexicographical larger rearrangement.

Given an array of integers `nums`, find the next permutation of `nums`.

The replacement must be **in place** and use only constant extra memory.

**Example 1:**

```text
Input: nums = [1,2,3]
Output: [1,3,2]
```

**Example 2:**

```text
Input: nums = [3,2,1]
Output: [1,2,3]
```

**Example 3:**

```text
Input: nums = [1,1,5]
Output: [1,5,1]
```

**Constraints:**

- `1 <= nums.length <= 100`

- `0 <= nums[i] <= 100`

------------------------------------------------------------------------

## 2. First Intuition

To get the next larger permutation, change the array as far to the right as possible. The suffix after the pivot is descending, so reversing it gives the smallest suffix after the swap.

------------------------------------------------------------------------

## 3. Pivot, Swap, Reverse

- Scan from the right to find the first index `pivot` where `nums[pivot] < nums[pivot + 1]`.
- If no pivot exists, the array is the largest permutation; reverse all of it.
- Otherwise, find the rightmost value greater than `nums[pivot]` and swap.
- Reverse the suffix after the pivot to make it as small as possible.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[1,2,3]`: pivot is `2`, swap it with `3`, and the suffix is already size one, so result is `[1,3,2]`. For `[3,2,1]`, no pivot exists, so reverse to `[1,2,3]`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public void nextPermutation(int[] nums) {
    int n = nums.length;
    int pivot = n - 2;

    while (pivot >= 0 && nums[pivot] >= nums[pivot + 1]) {
        pivot--;
    }

    if (pivot >= 0) {
        int j = n - 1;
        while (nums[j] <= nums[pivot]) {
            j--;
        }
        swap(nums, pivot, j);
    }

    reverse(nums, pivot + 1, n - 1);
}

private void reverse(int[] nums, int left, int right) {
    while (left < right) {
        swap(nums, left++, right--);
    }
}

private void swap(int[] nums, int i, int j) {
    int temp = nums[i];
    nums[i] = nums[j];
    nums[j] = temp;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Next permutation is not sorting the whole array; only reverse the descending suffix.
- The pivot is the first place from the right where the array can be increased.

------------------------------------------------------------------------

## End of Notes
