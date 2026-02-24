# Dynamic Programming Notes

## 08 - House Robber I and II (Linear and Circular)

**Generated on:** 2026-02-24 19:12:06 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

House Robber I (LC 198):
- You are given an array `nums[]` where `nums[i]` is the money in the i-th house on a street.
- Constraint: You cannot rob two adjacent houses.
- Goal: Maximize the total amount you can rob.

House Robber II (LC 213):
- Same as above, but houses are arranged in a circle.
- First and last houses are adjacent, so you cannot take both.
- Goal: Maximize total amount.

Relation:
- House Robber I is the “maximum sum without picking adjacent elements” problem.
- House Robber II reduces to two runs of House Robber I on linear ranges and take the maximum.

------------------------------------------------------------------------

## 🪜 2. State Definition

Linear street (Robber I):
- dp[i] = maximum amount you can rob from houses in range [0..i] with adjacency constraint.

Circular street (Robber II):
- Evaluate two linear scenarios:
  - Case A: rob houses [0..n-2] (exclude last)
  - Case B: rob houses [1..n-1] (exclude first)
- Answer = max(robLinear([0..n-2]), robLinear([1..n-1]))

Space-optimized:
- prev2 = dp[i-2], prev1 = dp[i-1], curr = max(prev1, prev2 + nums[i])

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

Robber I (linear):
- dp[i] = max(dp[i-1], nums[i] + dp[i-2])

Robber II (circular):
- max(robLinear(0..n-2), robLinear(1..n-1))

------------------------------------------------------------------------

## 🧱 4. Base Cases

Robber I:
- If n == 0 → 0
- If n == 1 → nums[0]
- Initialize:
  - dp[0] = nums[0]
  - dp[1] = max(nums[0], nums[1]) (when n ≥ 2)

Robber II:
- If n == 1 → nums[0] (only one house, can take it)

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

- Only two previous states are needed. Use rolling variables:
  - prev2 = answer at i-2
  - prev1 = answer at i-1
  - curr  = max(prev1, prev2 + nums[i])
- O(1) extra space, O(n) time.

------------------------------------------------------------------------

## 💻 6A. House Robber I — Clean Interview Version (O(1) space)

```java
class HouseRobberI {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];

        int prev2 = nums[0];
        int prev1 = Math.max(nums[0], nums[1]);

        for (int i = 2; i < n; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 💻 6B. House Robber I — Recursive + Memoization (Top-Down)

```java
import java.util.*;

class HouseRobberIMemo {
    public int rob(int[] nums) {
        int n = nums.length;
        Integer[] dp = new Integer[n];
        return solve(n - 1, nums, dp);
    }

    private int solve(int i, int[] nums, Integer[] dp) {
        if (i < 0) return 0;
        if (i == 0) return nums[0];
        if (dp[i] != null) return dp[i];

        int include = nums[i] + solve(i - 2, nums, dp);
        int exclude = solve(i - 1, nums, dp);
        return dp[i] = Math.max(include, exclude);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n) (recursion + memo)

------------------------------------------------------------------------

## 💻 6C. House Robber II — Circular Street

- We can’t take both nums[0] and nums[n-1]. So split and take max of two linear cases.

```java
class HouseRobberII {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        return Math.max(robRange(nums, 0, n - 2),
                        robRange(nums, 1, n - 1));
    }

    private int robRange(int[] nums, int l, int r) {
        int prev2 = 0;
        int prev1 = 0;
        for (int i = l; i <= r; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example (Robber I)

Input:
- nums = [2, 7, 9, 3, 1]

dp (space-optimized thought):
- i=0: prev2 = 2, prev1 = max(2, 7) = 7
- i=2: curr = max(7, 2 + 9) = 11 → prev2 = 7, prev1 = 11
- i=3: curr = max(11, 7 + 3) = 11 → prev2 = 11, prev1 = 11
- i=4: curr = max(11, 11 + 1) = 12 → prev2 = 11, prev1 = 12

Answer = 12 (rob houses 2 and 4 → 2-indexed view; values 2, 9, 1 with constraint)

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: “Skip Adjacent” DP / House Robber
- Family: 1D DP with local adjacency conflict
- Relatives:
  - Maximum Sum Without Adjacent Elements (same structure)
  - Delete and Earn (reduce values to indices and apply robber)
  - Circular conflict → split into two linear runs

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- n == 0 → 0
- n == 1 → nums[0]
- Negative values: typically problem gives non-negatives; if allowed, recurrence still valid
- House Robber II: do not forget the n == 1 guard
- Off-by-one errors in robRange (inclusive bounds)

------------------------------------------------------------------------

## ✅ 10. Takeaway

- House Robber I is the canonical “no two adjacent” maximization DP.
- House Robber II reduces circular adjacency to two linear passes.
- Prefer the O(1) space rolling solution in interviews.

------------------------------------------------------------------------

# End of Notes
