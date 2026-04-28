# DP Notes

## 70 - Climbing Stairs

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/climbing-stairs/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are climbing a staircase. It takes `n` steps to reach the top.

Each time you can either climb `1` or `2` steps. In how many distinct ways can you climb to the top?

**Example 1:**

```text
Input: n = 2
Output: 2
Explanation: There are two ways to climb to the top.
1. 1 step + 1 step
2. 2 steps
```

**Example 2:**

```text
Input: n = 3
Output: 3
Explanation: There are three ways to climb to the top.
1. 1 step + 1 step + 1 step
2. 1 step + 2 steps
3. 2 steps + 1 step
```

**Constraints:**

- `1 <= n <= 45`

------------------------------------------------------------------------

## 2. First Intuition

At step `i`, the final move must have come from either `i - 1` by taking one step or `i - 2` by taking two steps. That makes the count of ways Fibonacci-like: every state is built from the two previous states.

------------------------------------------------------------------------

## 3. DP Idea Used in the Code

- The Java file handles `n < 3` directly because `n = 1` has one way and `n = 2` has two ways.
- It creates `dp[i]` as the number of ways to reach step `i`.
- For every `i >= 3`, it stores `dp[i - 1] + dp[i - 2]` and returns `dp[n]`.

------------------------------------------------------------------------

## 4. Short Dry Run

For `n = 5`: `dp[1] = 1`, `dp[2] = 2`, `dp[3] = 3`, `dp[4] = 5`, `dp[5] = 8`. The answer is `8`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int climbStairs(int n) {
    if (n <= 2) return n;

    int[] dp = new int[n + 1];
    dp[1] = 1;
    dp[2] = 2;

    for (int i = 3; i <= n; i++) {
        dp[i] = dp[i - 1] + dp[i - 2];
    }
    return dp[n];
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(n)` for the DP array. This can be reduced to `O(1)` with two variables.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Recognize this when a path/counting state can be reached from the previous one or two states.
- Base cases matter: the recurrence only becomes valid after `n = 1` and `n = 2`.

------------------------------------------------------------------------

## End of Notes
