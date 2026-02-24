# Dynamic Programming Notes

## 22 - Count Subsets With Given Difference — Reduce to Target Sum (Counting)

**Generated on:** 2026-02-24 20:47:02 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `arr[]` of non-negative integers and an integer `D`, count the number of ways to partition the array into two subsets `S1` and `S2` such that:
- `S2 - S1 = D`
- Each element must belong to exactly one of the subsets.

Observation:
- Let total sum `T = sum(arr)`.
- We have `S1 + S2 = T` and `S2 - S1 = D`.
- Solving gives: `S2 = (T + D) / 2`.

Therefore, the problem reduces to:
- Count the number of subsets with sum `S2 = (T + D) / 2`.

Invalid cases:
- If `(T + D)` is odd or `S2 < 0`, answer is `0`.

Optional: Return result modulo `1e9+7` (common on coding platforms).

------------------------------------------------------------------------

## 🪜 2. State Definition

Counting DP:
- `dp[i][s]` = number of ways to form sum `s` using first `i` elements (`0..i-1`).

Answer:
- `dp[n][S2]` (where `n = arr.length`)

Space-optimized:
- `dp[s]` over `s=0..S2`, update backward for 0/1 counting.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

At item `i-1` with value `val = arr[i-1]`:
- Exclude: `dp[i-1][s]`
- Include (if `val <= s`): `dp[i-1][s - val]`
- Count is additive:
  - `dp[i][s] = dp[i-1][s] + (val <= s ? dp[i-1][s - val] : 0)`

Base:
- `dp[i][0] = 1` for all `i` (one way to form 0 sum → take nothing)
- `dp[0][s>0] = 0`

Zero handling:
- If `arr[i-1] == 0`, including or excluding zero produces the same sum, effectively doubling the ways at that stage (handled naturally by recurrence).

------------------------------------------------------------------------

## 🧱 4. Base Cases and Validity Checks

- Compute `T = sum(arr)`, `S2 = (T + D) / 2`.
- If `(T + D)` is odd or `S2 < 0`, return `0`.
- Initialize `dp[0][0] = 1`.

------------------------------------------------------------------------

## 💻 5A. Bottom-Up 2D DP (with MOD)

```java
import java.util.*;

class CountSubsetsDiff2D {
    private static final int MOD = 1_000_000_007;

    public int countPartitions(int[] arr, int D) {
        long total = 0;
        for (int x : arr) total += x;

        // Invalid if (T + D) is odd or negative
        long target2L = total + D;
        if (target2L < 0 || (target2L & 1L) == 1L) return 0;
        int S2 = (int)(target2L / 2);

        int n = arr.length;
        int[][] dp = new int[n + 1][S2 + 1];
        dp[0][0] = 1;

        for (int i = 1; i <= n; i++) {
            int val = arr[i - 1];
            for (int s = 0; s <= S2; s++) {
                int exclude = dp[i - 1][s];
                int include = (val <= s) ? dp[i - 1][s - val] : 0;
                dp[i][s] = (int)((exclude + (long)include) % MOD);
            }
        }
        return dp[n][S2];
    }
}
```

Complexity:
- Time: O(n · S2)
- Space: O(n · S2)

------------------------------------------------------------------------

## 💻 5B. Space-Optimized 1D DP (Backward update)

```java
import java.util.*;

class CountSubsetsDiff1D {
    private static final int MOD = 1_000_000_007;

    public int countPartitions(int[] arr, int D) {
        long total = 0;
        for (int x : arr) total += x;

        long target2L = total + D;
        if (target2L < 0 || (target2L & 1L) == 1L) return 0;
        int S2 = (int)(target2L / 2);

        int[] dp = new int[S2 + 1];
        dp[0] = 1;

        for (int val : arr) {
            for (int s = S2; s >= val; s--) {
                dp[s] = (int)((dp[s] + dp[s - val]) % MOD);
            }
        }
        return dp[S2];
    }
}
```

Complexity:
- Time: O(n · S2)
- Space: O(S2)

------------------------------------------------------------------------

## 🔎 6. Dry Run Example

Input:
```
arr = [1, 1, 2, 3], D = 1
T = 7 → (T + D) = 8 → S2 = 4
Count subsets with sum 4
```
Subsets summing to 4: `{1, 3}`, `{1, 1, 2}` → 2 ways  
Answer: 2

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: Count Partitions With Given Difference
- Family: 0/1 Knapsack (counting ways)
- Reduction: `S2 = (T + D)/2` and count subsets to sum `S2`
- Related:
  - Target Sum (LC 494) → identical counting DP with target `(T + target)/2` and zero handling nuance

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- `(T + D)` odd or negative → 0 ways
- Zeros in array: recurrence already doubles ways appropriately
- Large sums: use 1D DP to save space; be mindful of MOD
- Negative elements are not supported in this standard reduction

------------------------------------------------------------------------

## 🔁 9. Variants

- Count subsets to exact sum `K`: same DP with `S2 = K`
- Count partitions into K groups with equal sum: different DP/bitset techniques
- Include constraints (e.g., limit frequency per number) → bounded knapsack

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Convert difference constraint to a single target sum and use classic counting DP.
- Use 1D backward DP for O(S2) space.
- Remember invalidity checks before DP to avoid wasted work.

------------------------------------------------------------------------

# End of Notes
