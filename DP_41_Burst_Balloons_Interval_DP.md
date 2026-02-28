# Dynamic Programming Notes

## 41 - Burst Balloons (LC 312) — Interval DP (Pick Last Strategy)

**Generated on:** 2026-02-25 23:06:55 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given `nums` of length n where each balloon `i` has a number `nums[i]`.  
When you burst balloon `i`, you gain coins equal to `left * nums[i] * right` where:
- `left` is the value of the nearest unburst balloon to the left of i (or 1 if none),
- `right` is the value of the nearest unburst balloon to the right of i (or 1 if none).

After bursting `i`, it disappears. You can burst in any order.  
Goal: Maximize total coins collected.

Example:
- nums = [3,1,5,8] → Answer = 167  
  Optimal order gives 167 (e.g., burst 1, then 5, then 3, then 8 last; details vary)

------------------------------------------------------------------------

## 🪜 2. Key Insight — “Pick Last in Interval”

Trying to pick the “first” balloon in an interval is complicated because neighbors change as balloons are removed.  
Instead:
- Consider an interval (l, r) (exclusive), meaning balloons strictly between l and r are available.
- If k is the last balloon to burst in (l, r), then:
  - The gain from bursting k last is: `val[l] * val[k] * val[r]`, where val is nums padded with 1 at both ends.
  - The subproblems become disjoint: (l, k) and (k, r).

Define:
- Pad nums: `val = [1] + nums + [1]` (length n+2)
- Let `dp[l][r]` = max coins from bursting all balloons strictly in interval (l, r), i.e., l+1..r-1.

Recurrence:
- `dp[l][r] = max over k in (l+1..r-1) of ( dp[l][k] + val[l]*val[k]*val[r] + dp[k][r] )`
- Base: `dp[l][l+1] = 0` (empty interval)

Answer: `dp[0][n + 1]`.

Complexity: O(n^3) time, O(n^2) space.

------------------------------------------------------------------------

## 💻 3. Bottom-Up Tabulation (Java)

```java
import java.util.*;

public class BurstBalloons {
    public int maxCoins(int[] nums) {
        int n = nums.length;
        int[] val = new int[n + 2];
        val[0] = 1; val[n + 1] = 1;
        for (int i = 0; i < n; i++) val[i + 1] = nums[i];

        int[][] dp = new int[n + 2][n + 2];

        // len is the interval length in terms of indices distance (r - l)
        for (int len = 2; len <= n + 1; len++) { // at least one balloon between l and r
            for (int l = 0; l + len <= n + 1; l++) {
                int r = l + len;
                // compute dp[l][r]
                int best = 0;
                for (int k = l + 1; k <= r - 1; k++) {
                    int gain = val[l] * val[k] * val[r];
                    best = Math.max(best, dp[l][k] + gain + dp[k][r]);
                }
                dp[l][r] = best;
            }
        }
        return dp[0][n + 1];
    }

    // Example
    public static void main(String[] args) {
        BurstBalloons solver = new BurstBalloons();
        System.out.println(solver.maxCoins(new int[]{3,1,5,8})); // 167
    }
}
```

Notes:
- `len` starts from 2 because with interval endpoints (l, r), we need at least one k (so len ≥ 2).
- dp[l][l+1] = 0 as there are no balloons between l and l+1.

------------------------------------------------------------------------

## 💡 4. Top-Down Memoized DFS (Equivalent)

```java
import java.util.*;

class BurstBalloonsMemo {
    private int[][] memo;
    private int[] val;

    public int maxCoins(int[] nums) {
        int n = nums.length;
        val = new int[n + 2];
        val[0] = 1; val[n + 1] = 1;
        for (int i = 0; i < n; i++) val[i + 1] = nums[i];

        memo = new int[n + 2][n + 2];
        // default zeros indicate “uncomputed”; answers are non-negative
        return dfs(0, n + 1);
    }

    // return max coins by bursting balloons in (l, r), exclusive
    private int dfs(int l, int r) {
        if (l + 1 >= r) return 0; // empty interval
        if (memo[l][r] != 0) return memo[l][r];

        int best = 0;
        for (int k = l + 1; k <= r - 1; k++) {
            int gain = val[l] * val[k] * val[r];
            best = Math.max(best, dfs(l, k) + gain + dfs(k, r));
        }
        return memo[l][r] = best;
    }
}
```

------------------------------------------------------------------------

## 🔎 5. Dry Run Sketch

nums = [3,1,5,8], val = [1,3,1,5,8,1]
- dp[0][5] depends on choosing k ∈ {1,2,3,4} last:
  - If k=3 (5 is last in that segment): gain = val[0]*val[3]*val[5] = 1*5*1 = 5
    - plus dp[0][3] (burst among 3,1) and dp[3][5] (burst among 8)
  - Evaluate all ks and choose maximum.

The structure ensures neighbors for last-burst k are exactly l and r, which are fixed for that interval.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Interval DP / “Pick last” strategy
- Family: DP on intervals (similar to Matrix Chain Multiplication, Boolean Parenthesization)
- Triggers:
  - Removing/adding items changes neighbors dynamically
  - “Pick last in sub-interval” linearizes dependencies

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty array → 0
- Single element → nums[0] (because val becomes [1, nums[0], 1] → gain 1*nums[0]*1)
- Ensure to pad with 1 at both ends
- O(n^3) may be heavy for n ~ 500; typical constraints are n ≤ ~300 (solutions still pass in optimized languages)

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Pad with 1s, solve dp over intervals using “burst last” choice.
- Both top-down (memo) and bottom-up (tabulation) are clear and equivalent.
- Canonical interval DP problem frequently asked in interviews.

------------------------------------------------------------------------

# End of Notes
