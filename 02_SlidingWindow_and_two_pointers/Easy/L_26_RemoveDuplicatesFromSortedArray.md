# Two Pointers and Sliding Window Notes

## 26 - Remove Duplicates from Sorted Array

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/remove-duplicates-from-sorted-array/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an integer array `nums` sorted in **non-decreasing order**, remove the duplicates **in-place** such that each unique element appears only **once**. The **relative order** of the elements should be kept the **same**.

Consider the number of unique elements in `nums` to be `k​​​​​​​`​​​​​​​. After removing duplicates, return the number of unique elements `k`.

The first `k` elements of `nums` should contain the unique numbers in **sorted order**. The remaining elements beyond index `k - 1` can be ignored.

**Custom Judge:**

The judge will test your solution with the following code:

```text
int[] nums = [...]; // Input array
int[] expectedNums = [...]; // The expected answer with correct length

int k = removeDuplicates(nums); // Calls your implementation

assert k == expectedNums.length;
for (int i = 0; i < k; i++) {
assert nums[i] == expectedNums[i];
}
```

If all assertions pass, then your solution will be **accepted**.

**Example 1:**

```text
Input: nums = [1,1,2]
Output: 2, nums = [1,2,_]
Explanation: Your function should return k = 2, with the first two elements of nums being 1 and 2 respectively.
It does not matter what you leave beyond the returned k (hence they are underscores).
```

**Example 2:**

```text
Input: nums = [0,0,1,1,1,2,2,3,3,4]
Output: 5, nums = [0,1,2,3,4,_,_,_,_,_]
Explanation: Your function should return k = 5, with the first five elements of nums being 0, 1, 2, 3, and 4 respectively.
It does not matter what you leave beyond the returned k (hence they are underscores).
```

**Constraints:**

- `1 <= nums.length <= 3 * 104`

- `-100 <= nums[i] <= 100`

- `nums` is sorted in **non-decreasing** order.

------------------------------------------------------------------------

## 2. First Intuition

Because the array is sorted, duplicates are adjacent. Keep one pointer at the last unique value and scan ahead until a new value appears.

------------------------------------------------------------------------

## 3. Slow Writer and Fast Reader

- `i` marks the position of the last unique value kept.
- `j` scans through the array.
- When `nums[j]` differs from `nums[i]`, copy it into `nums[i + 1]` and advance `i`.
- The new logical length is `i + 1`.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[0,0,1,1,1,2,2,3,3,4]`: the writer keeps `0,1,2,3,4` in the first five positions, so the method returns `5`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int removeDuplicates(int[] nums) {
    int write = 0;
    for (int read = 1; read < nums.length; read++) {
        if (nums[read] != nums[write]) {
            nums[++write] = nums[read];
        }
    }
    return write + 1;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Sorted array deduplication usually uses a write pointer.
- Return the logical length; values after that length do not matter.

------------------------------------------------------------------------

## End of Notes
