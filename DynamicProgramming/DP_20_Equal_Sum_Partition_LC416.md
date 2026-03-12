# Dynamic Programming Notes

## 20 - Equal Sum Partition (LC 416) — Reduce to Subset Sum

**Generated on:** 2026-02-24 20:44:35 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums`, determine if it can be partitioned into two subsets such that the sum of elements in both subsets is equal.

Observation:
- Let total sum be `S`. We need two subsets `S1` and `S2` with `S1 = S2 = S/2`.
- If `S` is odd → impossible immediately.
- Otherwise, the problem reduces to “Is there a subset that sums to `S/2`?” (Subset Sum equals K).

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `K = S/2` (target half-sum)
- `dp[i][t]` = true if we can form sum `t` using first `i` elements (`0..i-1`), else false.

Answer:
- `dp[n][K]` (where `n = nums.length`)

Top-down equivalent:
- `f(idx, target)` = true if we can form `target` from items `0..idx`

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

At item `i-1` with value `nums[i-1]`:

- Exclude: `dp[i-1][t]`
- Include (only if `nums[i-1] <= t`): `dp[i-1][t - nums[i-1]]`

Thus:
- `dp[i][t] = dp[i-1][t] || (t >= nums[i-1] ? dp[i-1][t - nums[i-1]] : false)`

Base:
- `dp[i][0] = true` for all `i` (empty set makes 0)
- `dp[0][t] = false` for `t > 0` (no items cannot make positive sum)

------------------------------------------------------------------------

## 🧱 4. Base Cases and Early Exits

- If `S` is odd → return `false`
- If `K == 0` → always `true` (both subsets sum to 0 → all zeros)
- Optional pruning:
  - If max element > K → exclude automatically (still continue)

------------------------------------------------------------------------

## 💻 5A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class EqualSumPartitionMemo {
    private Boolean[][] dp;

    public boolean canPartition(int[] nums) {
        int total = 0;
        for (int x : nums) total += x;
        if ((total & 1) == 1) return false; // odd sum

        int K = total / 2;
        int n = nums.length;
        dp = new Boolean[n + 1][K + 1];
        return solve(nums, n, K);
    }

    private boolean solve(int[] nums, int i, int target) {
        if (target == 0) return true;
        if (i == 0) return false;

        if (dp[i][target] != null) return dp[i][target];

        boolean exclude = solve(nums, i - 1, target);
        boolean include = false;
        if (nums[i - 1] <= target) {
            include = solve(nums, i - 1, target - nums[i - 1]);
        }
        return dp[i][target] = (exclude || include);
    }
}
```

Complexity:
- Time: O(n · K)
- Space: O(n · K) memo + O(n) recursion

------------------------------------------------------------------------

## 💻 5B. Bottom-Up Tabulation

```java
class EqualSumPartitionTab {
    public boolean canPartition(int[] nums) {
        int total = 0;
        for (int x : nums) total += x;
        if ((total & 1) == 1) return false;

        int K = total / 2;
        int n = nums.length;

        boolean[][] dp = new boolean[n + 1][K + 1];
        // base: sum 0 always possible
        for (int i = 0; i <= n; i++) dp[i][0] = true;

        for (int i = 1; i <= n; i++) {
            int val = nums[i - 1];
            for (int t = 1; t <= K; t++) {
                boolean exclude = dp[i - 1][t];
                boolean include = (val <= t) ? dp[i - 1][t - val] : false;
                dp[i][t] = exclude || include;
            }
        }
        return dp[n][K];
    }
}
```

Complexity:
- Time: O(n · K)
- Space: O(n · K)

------------------------------------------------------------------------

## 💻 5C. Space-Optimized (1D DP)

Iterate target backward to ensure 0/1 usage per element.

```java
class EqualSumPartitionSpace {
    public boolean canPartition(int[] nums) {
        int total = 0;
        for (int x : nums) total += x;
        if ((total & 1) == 1) return false;

        int K = total / 2;
        boolean[] dp = new boolean[K + 1];
        dp[0] = true;

        for (int val : nums) {
            for (int t = K; t >= val; t--) {
                dp[t] = dp[t] || dp[t - val];
            }
        }
        return dp[K];
    }
}
```

Complexity:
- Time: O(n · K)
- Space: O(K)

------------------------------------------------------------------------

## 🔎 6. Dry Run Example

Input:
```
nums = [1, 5, 11, 5]
total = 22, K = 11
```

1D DP:
- dp[0] = true
- Process 1: dp[1] = true
- Process 5: dp[6] = dp[6] || dp[1] = true; dp[5] = true
- Process 11: dp[11] = dp[11] || dp[0] = true (taking 11 from {11})
  OR dp[11] also reachable via 6 + 5 from prior steps
Answer: true

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: Equal Sum Partition via Subset Sum
- Family: 0/1 Knapsack (feasibility)
- Triggers:
  - “Split array into two equal-sum subsets”
  - Reduce to “subset with sum = total/2”

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- Odd total sum → false immediately
- Zeros: harmless; dp handles them naturally (multiple subsets, but feasibility unaffected)
- Negative numbers: standard approach assumes non-negative; adjust if negatives allowed
- Large sums: prefer 1D DP to save space

------------------------------------------------------------------------

## 🔁 9. Variants

- Minimum subset sum difference → compute all reachable sums up to K = total/2 and minimize `total - 2*s`
- Count partitions with given difference → transforms to counting subsets with sum `(total + diff)/2`
- Target Sum (LC 494) → same counting DP with zeros-handling

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Check parity; then solve subset sum to total/2.
- Use 1D backward DP for optimal space and clean reasoning.
- This serves as a base for several partition-related problems.

------------------------------------------------------------------------

# End of Notes
