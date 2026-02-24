# Dynamic Programming Notes

## 21 - Minimum Subset Sum Difference — Reduce to Subset Sum Frontier

**Generated on:** 2026-02-24 20:45:37 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `arr[]` of positive integers, partition it into two subsets `S1` and `S2` such that the absolute difference of their sums is minimized.

Let:
- Total sum = `T = sum(arr)`
- `S1 + S2 = T`
- We want to minimize `|S2 - S1| = |T - 2*S1|`

Therefore, the problem reduces to finding a subset sum `S1` (≤ T/2) that is as close as possible to `T/2`.  
The minimal difference is: `minDiff = T - 2*S1best`.

------------------------------------------------------------------------

## 🪜 2. State Definition

Classic reduction to boolean subset sum:
- `dp[i][s]` = true if using first `i` items (`0..i-1`) we can form sum `s`.

We only need the last row (or a 1D array) to know which sums up to `T/2` are feasible.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

At item `i-1` with value `arr[i-1]`:
- Exclude: `dp[i-1][s]`
- Include (if `arr[i-1] <= s`): `dp[i-1][s - arr[i-1]]`
- `dp[i][s] = dp[i-1][s] || dp[i-1][s - arr[i-1]]`

Base:
- `dp[i][0] = true` (empty set → sum 0)
- `dp[0][s > 0] = false`

After filling, scan `s` from `0..T/2`, pick any `s` with `dp[n][s] = true` minimizing `T - 2*s`.

------------------------------------------------------------------------

## 🧱 4. Base Cases

- If `arr` is empty → answer is 0 (both sums 0).
- All positive ints assumed; if negatives appear, this template needs adaptation.

------------------------------------------------------------------------

## 💻 5A. Bottom-Up Tabulation (2D) + Scan Frontier

```java
class MinSubsetSumDiffTab {
    public int minSubsetSumDifference(int[] arr) {
        int n = arr.length;
        int total = 0;
        for (int x : arr) total += x;

        boolean[][] dp = new boolean[n + 1][total + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = true;

        for (int i = 1; i <= n; i++) {
            int val = arr[i - 1];
            for (int s = 1; s <= total; s++) {
                boolean exclude = dp[i - 1][s];
                boolean include = (val <= s) ? dp[i - 1][s - val] : false;
                dp[i][s] = exclude || include;
            }
        }

        int half = total / 2;
        int best = 0;
        for (int s = 0; s <= half; s++) {
            if (dp[n][s]) best = s; // latest true s closest to half
        }

        return total - 2 * best;
    }
}
```

Complexity:
- Time: O(n · T)
- Space: O(n · T)

------------------------------------------------------------------------

## 💻 5B. Space-Optimized (1D boolean DP)

```java
class MinSubsetSumDiffSpace {
    public int minSubsetSumDifference(int[] arr) {
        int n = arr.length;
        int total = 0;
        for (int x : arr) total += x;

        boolean[] dp = new boolean[total + 1];
        dp[0] = true;

        for (int val : arr) {
            for (int s = total; s >= val; s--) {
                dp[s] = dp[s] || dp[s - val];
            }
        }

        int half = total / 2;
        int best = 0;
        for (int s = 0; s <= half; s++) {
            if (dp[s]) best = s;
        }

        return total - 2 * best;
    }
}
```

Complexity:
- Time: O(n · T)
- Space: O(T)

------------------------------------------------------------------------

## 🔎 6. Full Dry Run Example

Input:
```
arr = [2, 4, 3]
T = 9, half = 4
```

Feasible sums up to 4 (via 1D DP):
- Start: dp[0] = true
- After 2: dp[2] = true
- After 4: dp[4] = true (also dp[2] remains true)
- After 3: dp[3] = true, dp[1] = (false), dp[5] possible but > half not scanned

Scan s=0..4 where dp[s]=true → s ∈ {0, 2, 3, 4}
Best s = 4 (closest to half)
Answer = T - 2*s = 9 - 8 = 1

One optimal partition: {4} and {2,3} → sums 4 and 5 → difference 1.

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: Min Difference Partition via Subset Frontier
- Family: 0/1 Knapsack feasibility with frontier scan
- Triggers:
  - Minimize |S2 - S1|
  - Equivalent to minimizing |T - 2*S1| with S1 ≤ T/2

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- If all numbers are large, T can be large → pseudo-polynomial runtime (depends on sum, not just count).
- With zeros, multiple equal differences may exist; dp handles naturally.
- Negative numbers require offset-based DP (not covered here).

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Reduce to subset sum; scan feasible sums up to T/2.
- Result = T - 2*best, where best is the feasible sum closest to T/2.
- Use 1D boolean DP for optimal space.

------------------------------------------------------------------------

# End of Notes
