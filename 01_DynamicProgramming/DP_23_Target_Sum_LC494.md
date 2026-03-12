# Dynamic Programming Notes

## 23 - Target Sum (LC 494) — Count Expressions With +/- Signs

**Generated on:** 2026-02-24 20:47:51 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums` and an integer `target`, you assign either `+` or `-` in front of each number and evaluate the expression.  
Count the number of different expressions that evaluate to `target`.

Example:
- nums = [1,1,2,3], target = 1
- Ways: (+1 -1 -2 +3), (-1 +1 -2 +3), ...

Key Reduction:
- Partition nums into two subsets: P (positive) and N (negative)
- sum(P) - sum(N) = target
- sum(P) + sum(N) = total
⇒ 2·sum(P) = total + target
⇒ sum(P) = (total + target) / 2

Therefore, count ways to choose a subset with sum S = (total + target) / 2.

Invalid cases:
- If |target| > total → 0
- If total + target is odd → 0

Zero handling:
- Zeros contribute multiplicatively: including zero or excluding zero keeps sum same → doubles ways at that stage.  
The DP transitions naturally handle it; in explicit 2D versions you can also special-case base row.

------------------------------------------------------------------------

## 🪜 2. State Definition (Reduction to Subset Sum Count)

Let:
- S = (total + target) / 2
- dp[s] = number of ways to form sum s using the first i elements (1D rolling)

Answer:
- dp[S]

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

Counting ways (0/1):
- For each value v in nums:
  - For s from S down to v:
    - dp[s] += dp[s - v]

Base:
- dp[0] = 1 (one way to form 0 – take none)

------------------------------------------------------------------------

## 🧱 4. Validity and Edge Cases

- If |target| > total → 0
- If (total + target) is odd → 0
- Zeros: handled by recurrence; they can double the count
- Negative numbers: standard reduction assumes non-negative nums; if negatives present, prefer direct memoization (next section)

------------------------------------------------------------------------

## 💻 5A. Reduction to Subset Sum — 1D DP (Optimal)

```java
import java.util.*;

class TargetSumReduce1D {
    private static final int MOD = Integer.MAX_VALUE; // no mod required on LC; keep natural int

    public int findTargetSumWays(int[] nums, int target) {
        int total = 0;
        for (int x : nums) total += x;

        // invalid quick checks
        if (Math.abs(target) > total) return 0;
        int sumPlusTarget = total + target;
        if ((sumPlusTarget & 1) == 1) return 0;

        int S = sumPlusTarget / 2;
        int[] dp = new int[S + 1];
        dp[0] = 1;

        for (int v : nums) {
            for (int s = S; s >= v; s--) {
                dp[s] += dp[s - v];
                // no modulo typically needed; if very large counts possible, consider long
            }
        }
        return dp[S];
    }
}
```

Complexity:
- Time: O(n · S)
- Space: O(S)

Notes:
- Use long if overflow might occur in extreme inputs.

------------------------------------------------------------------------

## 💻 5B. Direct Memoization on (index, currentSum)

When values include negatives or for clarity, use DFS with memo over (i, sum). Offset map sum or use HashMap.

```java
import java.util.*;

class TargetSumMemo {
    public int findTargetSumWays(int[] nums, int target) {
        Map<String, Integer> memo = new HashMap<>();
        return dfs(nums, 0, 0, target, memo);
    }

    private int dfs(int[] nums, int i, int curr, int target, Map<String, Integer> memo) {
        if (i == nums.length) {
            return curr == target ? 1 : 0;
        }
        String key = i + "#" + curr;
        if (memo.containsKey(key)) return memo.get(key);

        int add = dfs(nums, i + 1, curr + nums[i], target, memo);
        int sub = dfs(nums, i + 1, curr - nums[i], target, memo);

        int ways = add + sub;
        memo.put(key, ways);
        return ways;
    }
}
```

Complexity:
- Time: O(n · rangeOfSums) → pseudo-polynomial; memo bounds it
- Space: O(n · rangeOfSums) for memo + O(n) recursion

Tip:
- If sums can be large, consider shifting with offset array (need bounds) or stick with HashMap.

------------------------------------------------------------------------

## 🔎 6. Dry Run Example

nums = [1, 1, 2, 3], target = 1  
total = 7 → S = (7 + 1)/2 = 4  
Count subsets with sum 4: {1,3}, {1,1,2} → 2 ways  
Answer = 2

Zero example:
- nums = [0,0,1], target = 1
- total = 1 → S = (1 + 1)/2 = 1
- dp evolves: zeros double dp[0] multiple times; then 1 can be formed accordingly → 4 ways

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: Target Sum via Subset Sum Counting
- Family: 0/1 Knapsack (count ways)
- Triggers:
  - Assign +/− signs to reach target
  - Convert to partition with difference D = target
  - S = (total + target) / 2

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- (total + target) odd or |target| > total → 0
- Handling zeros: the 1D counting DP naturally accounts for doubling
- Large counts may exceed int in contrived cases → consider long
- Negative numbers in input: prefer memoized DFS over reduction

------------------------------------------------------------------------

## ✅ 9. Takeaway

- The fastest approach reduces to counting subsets with sum S = (total + target)/2 using 1D DP.
- A direct memoization over (index, sum) is a robust alternative, especially with negatives.

------------------------------------------------------------------------

# End of Notes
