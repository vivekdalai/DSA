# Dynamic Programming Notes

## 03 - Delete and Earn | House Robber Pattern

**Generated on:** 2026-02-24 17:11:52 (IST)

------------------------------------------------------------------------

## 1. Problem Understanding

Part A: Delete and Earn (LC 740)
- Given an array of integers `nums`.
- If you pick a number `x`, you earn `x` points, but you must delete every occurrence of `x - 1` and `x + 1` from the array.
- Goal: maximize total points.

Key insight:
- Group equal values together. Picking value `v` gives you `v * count(v)` points.
- After grouping, choosing `v` blocks only `v - 1` and `v + 1`.
- This becomes the House Robber problem on a line of values indexed by `v`.

Part B: House Robber I (LC 198)
- Given non-negative integers representing money in each house.
- You can't rob two adjacent houses.
- Goal: maximize total amount.

Delete and Earn reduces to House Robber by converting values into a frequency-sum array.

------------------------------------------------------------------------

## 2. State Definition

Delete and Earn reduction:
- Let `maxVal = max(nums)`.
- Build `points[v] = v * count(v)`.
- Now solve standard House Robber on `points[0..maxVal]`.

House Robber (linear):
- `dp[i]` = maximum amount from positions `0..i`, with the rule that no two adjacent positions can both be chosen.

Space-optimized form:
- `prev2 = dp[i - 2]`, `prev1 = dp[i - 1]`
- `curr = max(prev1, prev2 + value[i])`

------------------------------------------------------------------------

## 3. Recurrence Relation

House Robber:
- `dp[i] = max(dp[i - 1], dp[i - 2] + val[i])`

Delete and Earn:
- First build `val[i] = points[i]`
- Then apply the exact same House Robber recurrence

------------------------------------------------------------------------

## 4. Base Cases

House Robber:
- If `n == 0` -> `0`
- If `n == 1` -> `val[0]`

Delete and Earn:
- If `nums` is empty -> `0`
- `points[0]` may be zero, which is fine

------------------------------------------------------------------------

## 5. Recursive Intuition First

Before the iterative DP, think recursively for House Robber.

At index `i`:
- Take `val[i]`, then move to `i - 2`
- Skip `val[i]`, then move to `i - 1`

So:

```java
solve(i) = max(val[i] + solve(i - 2), solve(i - 1))
```

Recursive version:

```java
private int solve(int i, int[] val) {
    if (i < 0) return 0;
    if (i == 0) return val[0];

    int take = val[i] + solve(i - 2, val);
    int skip = solve(i - 1, val);
    return Math.max(take, skip);
}
```

Delete and Earn uses this exact recurrence after converting the input into the `points[]` array.

------------------------------------------------------------------------

## 6. Space Optimization Insight

- Only the previous two DP states are needed for House Robber, so we can reduce space to `O(1)`.
- For Delete and Earn, if `maxVal` is reasonably small, use an `int[]` of size `maxVal + 1`.
- If values are very large but sparse, use a sorted map approach to avoid a huge array.

------------------------------------------------------------------------

## 7A. Clean Interview Version: Delete and Earn (Array Compression)

```java
import java.util.*;

class Solution {
    public int deleteAndEarn(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        int maxVal = 0;
        for (int x : nums) maxVal = Math.max(maxVal, x);

        int[] points = new int[maxVal + 1];
        for (int x : nums) points[x] += x;

        return robLinear(points);
    }

    private int robLinear(int[] val) {
        int prev2 = 0;
        int prev1 = 0;

        for (int x : val) {
            int curr = Math.max(prev1, prev2 + x);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

Complexity:
- Time: `O(N + K)`, where `K = max(nums)`
- Space: `O(K)`

------------------------------------------------------------------------

## 7B. Clean Interview Version: Delete and Earn (Sorted Map, Sparse-Friendly)

```java
import java.util.*;

class SolutionSparse {
    public int deleteAndEarn(int[] nums) {
        if (nums == null || nums.length == 0) return 0;

        Map<Integer, Integer> total = new HashMap<>();
        for (int x : nums) total.put(x, total.getOrDefault(x, 0) + x);

        List<Integer> vals = new ArrayList<>(total.keySet());
        Collections.sort(vals);

        int prev2 = 0;
        int prev1 = 0;
        Integer prevValue = null;

        for (int v : vals) {
            int gain = total.get(v);

            if (prevValue != null && v == prevValue + 1) {
                int curr = prev2 + gain;
                prev2 = Math.max(prev2, prev1);
                prev1 = curr;
            } else {
                int best = Math.max(prev2, prev1);
                prev1 = best + gain;
                prev2 = best;
            }
            prevValue = v;
        }

        return Math.max(prev1, prev2);
    }
}
```

Complexity:
- Time: `O(N log U)` for sorting `U` unique values
- Space: `O(U)`

Use this when `max(nums)` is very large compared to `N`.

------------------------------------------------------------------------

## 7C. House Robber I (Reference Template)

Recursive intuition version:

```java
class HouseRobberRecursive {
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
}
```

Iterative optimized version:

```java
class HouseRobber {
    public int rob(int[] nums) {
        int prev2 = 0;
        int prev1 = 0;

        for (int money : nums) {
            int curr = Math.max(prev1, prev2 + money);
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n) recursive, O(1) iterative

------------------------------------------------------------------------

## 7D. House Robber II (Circular Street Variant)

- First and last houses are adjacent, so you can't take both.
- Solve two cases and take the maximum:
  - Rob houses `0..n-2`
  - Rob houses `1..n-1`

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

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 8. Full Dry Run Example (Delete and Earn)

Input:
- `nums = [2, 2, 3, 3, 3, 4]`

Build `points`:
- `count(2) = 2` -> `points[2] = 4`
- `count(3) = 3` -> `points[3] = 9`
- `count(4) = 1` -> `points[4] = 4`
- `points = [0, 0, 4, 9, 4]`

Apply House Robber on `[0, 0, 4, 9, 4]`:
- `i = 0`: best = `0`
- `i = 1`: best = `0`
- `i = 2`: best = `4`
- `i = 3`: best = `9`
- `i = 4`: best = `9`

Answer = `9`, by taking all `3`s.

------------------------------------------------------------------------

## 9. Pattern Recognition

- Name: "Reduce-to-House-Robber"
- Family: 1D DP with adjacency conflict
- Triggers:
  - Choosing value `v` invalidates neighbors `v - 1` and `v + 1`
  - Group equal values, then run non-adjacent DP

Mental steps:
1. Compress values into `points[v] = v * count(v)`
2. Run House Robber on `points`

------------------------------------------------------------------------

## 10. Edge Cases and Pitfalls

Edge cases:
- Empty array -> `0`
- All same numbers -> take all, because only one value bucket is used
- Large values but sparse -> prefer map-based approach
- `nums` may contain `0` -> harmless

Pitfalls:
- Forgetting to multiply by the value: `points[v]` must be `v * count(v)`
- Using 2D DP unnecessarily when 1D DP is enough
- Ignoring sparse inputs and building a huge array when `maxVal` is very large
- For House Robber II, forgetting the `n == 1` guard

------------------------------------------------------------------------

## 11. Takeaway

- Delete and Earn is a direct reduction to House Robber.
- If the conflict is "pick one value, lose neighboring values", try grouping first and then applying House Robber logic.

------------------------------------------------------------------------

# End of Notes
