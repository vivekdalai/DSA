# DP Notes

## 213 - House Robber II

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/house-robber-ii/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed. All houses at this place are **arranged in a circle.** That means the first house is the neighbor of the last one. Meanwhile, adjacent houses have a security system connected, and it will automatically contact the police if two adjacent houses were broken into on the same night.

Given an integer array `nums` representing the amount of money of each house, return the maximum amount of money you can rob tonight **without alerting the police**.

**Example 1:**

```text
Input: nums = [2,3,2]
Output: 3
Explanation: You cannot rob house 1 (money = 2) and then rob house 3 (money = 2), because they are adjacent houses.
```

**Example 2:**

```text
Input: nums = [1,2,3,1]
Output: 4
Explanation: Rob house 1 (money = 1) and then rob house 3 (money = 3).
Total amount you can rob = 1 + 3 = 4.
```

**Example 3:**

```text
Input: nums = [1,2,3]
Output: 3
```

**Constraints:**

- `1 <= nums.length <= 100`

- `0 <= nums[i] <= 1000`

------------------------------------------------------------------------

## 2. First Intuition

The first and last houses are adjacent because the street is circular. A valid plan cannot include both, so the circle is converted into two normal House Robber ranges.

------------------------------------------------------------------------

## 3. Circular DP Split

- If there is only one house, return it directly.
- Run the normal House Robber DP on `0..n-2`, which excludes the last house.
- Run it again on `1..n-1`, which excludes the first house.
- The answer is the larger of those two independent linear cases.

------------------------------------------------------------------------

## 4. Short Dry Run

For `nums = [2,3,2]`: excluding the last gives `[2,3] -> 3`; excluding the first gives `[3,2] -> 3`; answer is `3`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int rob(int[] nums) {
    int n = nums.length;
    if (n == 1) return nums[0];
    return Math.max(robRange(nums, 0, n - 2), robRange(nums, 1, n - 1));
}

private int robRange(int[] nums, int start, int end) {
    int prev2 = 0;
    int prev1 = 0;

    for (int i = start; i <= end; i++) {
        int curr = Math.max(nums[i] + prev2, prev1);
        prev2 = prev1;
        prev1 = curr;
    }
    return prev1;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Circular adjacency often becomes two linear cases.
- For circular arrays, ask whether index `0` and index `n - 1` can both be chosen.

------------------------------------------------------------------------

## End of Notes
