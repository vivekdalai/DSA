# DP Notes

## 198 - House Robber

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/house-robber/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them is that adjacent houses have security systems connected and it will automatically contact the police if two adjacent houses were broken into on the same night.

Given an integer array `nums` representing the amount of money of each house, return the maximum amount of money you can rob tonight without alerting the police.

**Example 1:**

```text
Input: nums = [1,2,3,1]
Output: 4
Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
Total amount you can rob = 1 + 3 = 4.
```

**Example 2:**

```text
Input: nums = [2,7,9,3,1]
Output: 12
Explanation: Rob house 1 (money = 2), rob house 3 (money = 9) and rob house 5 (money = 1).
Total amount you can rob = 2 + 9 + 1 = 12.
```

**Constraints:**

- `1 <= nums.length <= 100`

- `0 <= nums[i] <= 400`

------------------------------------------------------------------------

## 2. First Intuition

At each house there are only two useful choices: rob it and then you must skip the previous house, or skip it and keep the best answer so far. The whole problem is the best non-adjacent sum.

------------------------------------------------------------------------

## 3. Build the Intuition with Recursion First

Think from the last house at index `i`.

- If we rob house `i`, we must skip `i - 1`, so profit becomes `nums[i] + solve(i - 2)`.
- If we skip house `i`, profit becomes `solve(i - 1)`.
- So the recurrence is:

```java
solve(i) = max(nums[i] + solve(i - 2), solve(i - 1))
```

Base cases:

- `solve(i) = 0` when `i < 0`
- `solve(0) = nums[0]`

Plain recursive version:

```java
public int rob(int[] nums) {
    return solve(nums.length - 1, nums);
}

private int solve(int i, int[] nums) {
    if (i < 0) return 0;
    if (i == 0) return nums[0];

    int take = nums[i] + solve(i - 2, nums);
    int skip = solve(i - 1, nums);
    return Math.max(take, skip);
}
```

This gives the right intuition first, but it repeats subproblems, so we then convert the same recurrence into DP.

------------------------------------------------------------------------

## 4. Take-or-Skip DP Used in the File

- `rob` delegates to the tabulation method in the file.
- `dp[i]` means the maximum money possible from houses `0..i`.
- For every index, compute `take = nums[i] + dp[i - 2]` and `skip = dp[i - 1]`, then keep the larger value.
- Memoization, tabulation, and space optimization all come from the same recurrence above.

------------------------------------------------------------------------

## 5. Short Dry Run

For `nums = [2,7,9,3,1]`: best values become `2, 7, 11, 11, 12`. The answer is `12`, using houses with values `2 + 9 + 1`.

------------------------------------------------------------------------

## 6. Clean Interview Version

```java
public int rob(int[] nums) {
    int prev2 = 0;
    int prev1 = 0;

    for (int money : nums) {
        int take = money + prev2;
        int skip = prev1;
        int curr = Math.max(take, skip);
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

------------------------------------------------------------------------

## 7. Complexity

- Recursive intuition version: `O(2^n)` time, `O(n)` stack space
- Iterative clean version: `O(n)` time and `O(1)` extra space
- The file's `rob_dp` method uses `O(n)` space.

------------------------------------------------------------------------

## 8. Pattern Recognition and Revision Notes

- This is the standard non-adjacent choice DP.
- If choosing index `i` invalidates index `i - 1`, think `take/skip`.

------------------------------------------------------------------------

## End of Notes
