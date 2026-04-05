# Greedy Notes

## 01 - Maximize Sum Of Array After K Negations

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/maximize-sum-of-array-after-k-negations/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given an integer array `nums` and an integer `k`.

In one operation, you may choose an index and replace `nums[i]` with `-nums[i]`.

Return the largest possible array sum after exactly `k` negations.

**Example 1**

    Input: nums = [4, 2, 3], k = 1
    Output: 5

Explanation:

- We must use exactly one flip
- Flipping `2` to `-2` gives sum `4 - 2 + 3 = 5`
- Flipping `4` or `3` would give a smaller result

**Example 2**

    Input: nums = [3, -1, 0, 2], k = 3
    Output: 6

One optimal sequence is:

- Flip `-1` to `1`
- Use the remaining flips on `0`

------------------------------------------------------------------------

## 2. Main Intuition

Every time we flip a negative number to positive, the total sum increases.

Every time we flip a positive number to negative, the total sum decreases.

So the greedy priority is:

- Convert the most useful negative numbers first
- If flips remain after all negatives are handled, only parity matters

That is why sorting helps.

------------------------------------------------------------------------

## 3. Greedy Logic Used Here

The method sorts `nums` in ascending order.

After sorting:

- negative numbers appear first
- we flip them from left to right while `k > 0`

Then:

- compute the sum
- find the minimum value in the updated array
- if `k` is still odd, subtract `2 * min`

Why subtract `2 * min`?

Because one extra flip changes `x` into `-x`, which reduces the sum by `2x`.

So if an odd flip is forced at the end, we should apply it to the smallest absolute value.

------------------------------------------------------------------------

## 4. Quick Walkthrough

**Input:**

    nums = [-4, -2, -3], k = 4

After sorting:

    [-4, -3, -2]

Flip negatives greedily:

- flip `-4` -> `4`, `k = 3`
- flip `-3` -> `3`, `k = 2`
- flip `-2` -> `2`, `k = 1`

Array becomes:

    [4, 3, 2]

Current sum:

    9

One flip is still left, so we must flip the smallest value `2`.

Final sum:

    9 - 2 * 2 = 5

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static int largestSumAfterKNegations(int[] nums, int k) {
    Arrays.sort(nums);
    int ans = 0;

    for (int i = 0; i < nums.length; i++) {
        if (nums[i] < 0 && k > 0) {
            nums[i] *= -1;
            k--;
        }
        ans += nums[i];
    }

    int min = Integer.MAX_VALUE;
    for (int x : nums) {
        min = Math.min(min, x);
    }

    if (k % 2 == 1) ans -= 2 * min;
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort
- fix harmful values first
- if an odd operation remains, apply it to the cheapest place

------------------------------------------------------------------------

## End of Notes
