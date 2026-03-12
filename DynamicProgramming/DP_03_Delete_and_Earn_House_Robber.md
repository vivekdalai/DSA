# Dynamic Programming Notes

## 03 - Delete and Earn | House Robber Pattern

**Generated on:** 2026-02-24 17:11:52 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Part A: Delete and Earn (LC 740)
- Given an array of integers nums.
- If you pick a number x, you earn x points, but you must delete every occurrence of x-1 and x+1 from the array.
- Goal: Maximize total points.

Key Insight:
- Group equal values together. Picking value v gives you v * count(v) points.
- After grouping, choosing v blocks only (v-1) and (v+1).
- This is exactly the House Robber problem on a “line” of values by index v.

Part B: House Robber I (LC 198)
- Given non-negative integers representing money of each house.
- You can’t rob two adjacent houses.
- Goal: Maximize total amount.

Delete and Earn reduces to House Robber by converting values to a frequency-sum array.

------------------------------------------------------------------------

## 🪜 2. State Definition

Delete and Earn reduction:
- Let maxVal = max(nums).
- Build points[v] = v * count of v in nums.
- Now solve standard House Robber on points[0..maxVal].

House Robber (linear):
- dp[i] = maximum amount from first i+1 positions (0..i), with constraint no two adjacent chosen.

Space-optimized:
- prev2 = dp[i-2], prev1 = dp[i-1]
- curr = max(prev1, prev2 + value[i])

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

House Robber:
- dp[i] = max(dp[i-1], dp[i-2] + val[i])

Delete and Earn:
- Build val[i] = points[i] as above, then apply House Robber recurrence.

------------------------------------------------------------------------

## 🧱 4. Base Cases

House Robber:
- If n == 0 → 0
- If n == 1 → val[0]

Delete and Earn:
- If nums is empty → 0
- points[0] may be zero (safe)

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

- Only previous two DP states are needed for House Robber (O(1) space).
- For Delete and Earn, if maxVal is small (LC constraints), use an int[] of size maxVal+1.
- If values are very large but sparse, use a TreeMap/ordered map approach (skip gaps).

------------------------------------------------------------------------

## 💻 6A. Clean Interview Version: Delete and Earn (Array Compression)

```java
import java.util.*;

class Solution {
    public int deleteAndEarn(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int maxVal = 0;
        for (int x : nums) maxVal = Math.max(maxVal, x);

        int[] points = new int[maxVal + 1];
        for (int x : nums) points[x] += x; // accumulate v * count(v)

        return robLinear(points);
    }

    // Standard House Robber (linear) on an array of non-negative values
    private int robLinear(int[] val) {
        int n = val.length;
        if (n == 0) return 0;
        if (n == 1) return val[0];

        int prev2 = val[0];
        int prev1 = Math.max(val[0], val[1]);

        for (int i = 2; i < n; i++) {
            int curr = Math.max(prev1, prev2 + val[i]);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

Complexity:
- Time: O(N + K) where K = max(nums). For LC constraints, OK.
- Space: O(K)

------------------------------------------------------------------------

## 💻 6B. Clean Interview Version: Delete and Earn (Sorted Map, Sparse-Friendly)

```java
import java.util.*;

class SolutionSparse {
    public int deleteAndEarn(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        // total[v] = v * count(v)
        Map<Integer, Integer> total = new HashMap<>();
        for (int x : nums) total.put(x, total.getOrDefault(x, 0) + x);

        List<Integer> vals = new ArrayList<>(total.keySet());
        Collections.sort(vals);

        int take = 0; // take current value
        int skip = 0; // skip current value

        Integer prev = null;
        for (int v : vals) {
            int gain = total.get(v);
            if (prev != null && v == prev + 1) {
                // Adjacent to previous: standard take/skip transition
                int newTake = skip + gain;     // must skip previous
                int newSkip = Math.max(skip, take);
                take = newTake;
                skip = newSkip;
            } else {
                // Not adjacent: both take and skip can inherit max(skip, take)
                int best = Math.max(skip, take);
                take = best + gain;
                skip = best;
            }
            prev = v;
        }
        return Math.max(take, skip);
    }
}
```

Complexity:
- Time: O(N log U) for sorting U unique values.
- Space: O(U)

Use this when max(nums) is large vs N (sparse values).

------------------------------------------------------------------------

## 💻 6C. House Robber I (Reference Template)

```java
class HouseRobber {
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

------------------------------------------------------------------------

## 💻 6D. House Robber II (Circular Street Variant)

- First and last are adjacent; you can’t take both.
- Solve two cases and take the max:
  - Rob houses 0..n-2
  - Rob houses 1..n-1

```java
class HouseRobberII {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];
        return Math.max(robRange(nums, 0, n - 2), robRange(nums, 1, n - 1));
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

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example (Delete and Earn)

Input:
- nums = [2, 2, 3, 3, 3, 4]

Build points:
- count(2)=2 → points[2]=4
- count(3)=3 → points[3]=9
- count(4)=1 → points[4]=4
- points = [0, 0, 4, 9, 4]

Apply House Robber on [0, 0, 4, 9, 4]:
- i=0: prev2=0, prev1=0
- i=1: prev2=0, prev1=0
- i=2: curr=max(0, 0+4)=4 → prev2=0, prev1=4
- i=3: curr=max(4, 0+9)=9 → prev2=4, prev1=9
- i=4: curr=max(9, 4+4)=9 → prev2=9, prev1=9

Answer = 9 (pick all 3’s)

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: “Reduce-to-House-Robber”
- Family: 1D DP with adjacency conflict
- Triggers:
  - Choosing value v invalidates neighbors v-1 and v+1
  - Group-equal-values-then-rob

Mental steps:
1) Compress values → points[v] = v * count(v)
2) Run House Robber on points

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

Edge Cases:
- Empty array → 0
- All same numbers → take all (since only one index v is used)
- Large values but sparse → prefer map-based approach to avoid huge arrays
- nums may contain 0 → harmless; contributes nothing

Pitfalls:
- Forgetting to multiply by value: points[v] must be v * count(v), not just count
- Using 2D DP unnecessarily; 1D O(1) space is enough
- Integer overflow (rare under LC constraints). If values can grow large, use long for accumulations
- For House Robber II, don’t forget n==1 guard

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Delete and Earn is a direct reduction to House Robber.
- Master the House Robber template; many problems map to it after the right transformation.

------------------------------------------------------------------------

# End of Notes
