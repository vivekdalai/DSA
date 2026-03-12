# Dynamic Programming Notes

## 19 - Subset Sum Equals K (0/1 Knapsack Feasibility)

**Generated on:** 2026-02-24 20:42:56 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `arr[]` and an integer `K`, determine whether there exists a subset of `arr` whose sum equals `K`.  
Each element can be used at most once (0/1 choice).

This is the boolean-feasibility version of 0/1 Knapsack.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[i][s]` = true if we can form sum `s` using the first `i` items (indices `0..i-1`), else false.

Answer:
- `dp[n][K]` (where `n = arr.length`)

Top-down equivalent:
- `f(idx, target)` = true if we can form `target` using items `0..idx`

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

At item `i-1` (value `arr[i-1]`), for sum `s`:
- Exclude current: `dp[i-1][s]`
- Include current (only if `arr[i-1] <= s`): `dp[i-1][s - arr[i-1]]`

Therefore:
- `dp[i][s] = dp[i-1][s] || dp[i-1][s - arr[i-1]]` (when `arr[i-1] <= s`)
- else `dp[i][s] = dp[i-1][s]`

Base:
- `dp[i][0] = true` for all `i` (empty subset achieves sum 0)
- `dp[0][s] = false` for `s > 0` (no items cannot make positive sum)

------------------------------------------------------------------------

## 🧱 4. Base Cases

- Empty set can make sum 0: true.
- With no items, positive sums are false.
- In memoized recursion, use `Boolean` (nullable) to distinguish “uncomputed” from false.

------------------------------------------------------------------------

## 💻 5A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class SubsetSumMemo {
    private Boolean[][] dp;

    public boolean subsetSumToK(int[] arr, int K) {
        int n = arr.length;
        dp = new Boolean[n + 1][K + 1];
        return solve(arr, n, K);
    }

    private boolean solve(int[] arr, int i, int target) {
        // i = number of items available from prefix [0..i-1]
        if (target == 0) return true;
        if (i == 0) return false;

        if (dp[i][target] != null) return dp[i][target];

        boolean exclude = solve(arr, i - 1, target);
        boolean include = false;
        if (arr[i - 1] <= target) {
            include = solve(arr, i - 1, target - arr[i - 1]);
        }

        return dp[i][target] = (include || exclude);
    }
}
```

Complexity:
- Time: O(n · K)
- Space: O(n · K) memo + O(n) recursion

------------------------------------------------------------------------

## 💻 5B. Bottom-Up Tabulation

```java
class SubsetSumTab {
    public boolean subsetSumToK(int[] arr, int K) {
        int n = arr.length;
        boolean[][] dp = new boolean[n + 1][K + 1];

        // base: sum 0 is always possible (empty subset)
        for (int i = 0; i <= n; i++) dp[i][0] = true;

        for (int i = 1; i <= n; i++) {
            int val = arr[i - 1];
            for (int s = 1; s <= K; s++) {
                boolean exclude = dp[i - 1][s];
                boolean include = (val <= s) ? dp[i - 1][s - val] : false;
                dp[i][s] = exclude || include;
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

## 💻 5C. Space-Optimized (1D boolean DP)

Iterate sum backward to prevent reusing the same element multiple times (0/1 behavior).

```java
class SubsetSumSpace {
    public boolean subsetSumToK(int[] arr, int K) {
        boolean[] dp = new boolean[K + 1];
        dp[0] = true;

        for (int val : arr) {
            for (int s = K; s >= val; s--) {
                dp[s] = dp[s] || dp[s - val];
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
arr = [4, 3, 2, 1], K = 5
```

- dp[0] = [true, false, false, false, false, false]
- Process 4:
  - s: 5..4 → dp[4] = true (4), dp[5] stays false
- Process 3:
  - s: 5..3 → dp[5] = dp[5] || dp[2] = false || false = false
               dp[4] = true || dp[1]=false → true
               dp[3] = true
- Process 2:
  - s: 5..2 → dp[5] = dp[5] || dp[3] = false || true = true
Answer: true (subset {3,2})

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: 0/1 Knapsack Feasibility (Boolean DP)
- Family: DP over (index, target)
- Triggers:
  - “Is there a subset that sums to K?”
  - Each element used at most once
- Variants:
  - Equal Sum Partition (check subset sum to total/2)
  - Min Subset Sum Difference (scan last row up to total/2)
  - Count of subsets with given diff / Target Sum (counting DP)

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- Be careful using `Boolean` vs `boolean` for memoization (null vs false).
- Negative numbers are not supported by the standard formulation (adjust if needed).
- K = 0 → always true.
- Large K → prefer space-optimized O(K) DP.

------------------------------------------------------------------------

## 🔁 9. Useful Extensions

- To reconstruct one valid subset, backtrack over the 2D table from `(n, K)`.
- To count number of ways, switch to integer DP with `dp[s] += dp[s - val]` (order matters based on loop nesting).

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Subset Sum is the boolean version of 0/1 knapsack.
- Use 1D backward DP for optimal space.
- This template powers several partition/difference/counting problems.

------------------------------------------------------------------------

# End of Notes
