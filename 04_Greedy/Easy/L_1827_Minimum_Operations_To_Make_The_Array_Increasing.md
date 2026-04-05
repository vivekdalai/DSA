# Greedy Notes

## 08 - Minimum Operations To Make The Array Increasing

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/minimum-operations-to-make-the-array-increasing/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given an integer array `nums`.

In one operation, you may increment any element by `1`.

Return the minimum number of operations needed to make the array strictly increasing.

**Example 1**

    Input: nums = [1, 1, 1]
    Output: 3

Explanation:

One optimal transformation is:

    [1, 1, 1] -> [1, 2, 3]

**Example 2**

    Input: nums = [1, 5, 2, 4, 1]
    Output: 14

------------------------------------------------------------------------

## 2. Greedy View

At index `i`, the only value that matters is the previous final value.

To keep the array strictly increasing:

    nums[i] must be at least previous + 1

If `nums[i]` is already large enough, do nothing.

If not, push it up to exactly `previous + 1`.

Going any higher would waste operations.

------------------------------------------------------------------------

## 3. One-Pass Rule

For each index from left to right:

- if `nums[i] > nums[i - 1]`, keep it
- otherwise we need:

    needed = nums[i - 1] + 1 - nums[i]

- add `needed` to the answer
- update `nums[i]` to `nums[i - 1] + 1`

This greedy is minimal because every element is fixed at the smallest legal value.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [1, 1, 1]

Process:

- index `1`: needs to become `2`, add `1`
- array becomes `[1, 2, 1]`
- index `2`: needs to become `3`, add `2`
- array becomes `[1, 2, 3]`

Total operations:

    1 + 2 = 3

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int minOperations(int[] nums) {
    int ans = 0;

    for (int i = 1; i < nums.length; i++) {
        if (nums[i] <= nums[i - 1]) {
            int needed = nums[i - 1] + 1 - nums[i];
            ans += needed;
            nums[i] = nums[i - 1] + 1;
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- maintain the minimum valid prefix
- fix each next value just enough to preserve the invariant

------------------------------------------------------------------------

## End of Notes
