# Dynamic Programming Notes

## 06 - Frog Jump (Min Energy with 1/2 Jumps)

**Generated on:** 2026-02-24 19:10:09 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `height[]` where `height[i]` is the height of the i-th stone.  
A frog starts at index 0 and wants to reach index `n-1`.  
At each move, it can jump:
- from `i` to `i+1` (cost = `|height[i] - height[i+1]|`) or
- from `i` to `i+2` (cost = `|height[i] - height[i+2]|`)

Goal: Minimize the total energy (sum of jump costs) to reach `n-1`.

This is a 1D DP minimization with local transitions.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- dp[i] = minimum energy to reach index i

Goal: dp[n - 1]

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For i ≥ 1:
- Option 1 (jump from i-1): `cost1 = dp[i-1] + |height[i] - height[i-1]|`
- Option 2 (jump from i-2), only if i ≥ 2: `cost2 = dp[i-2] + |height[i] - height[i-2]|`

Then:
- `dp[i] = min(cost1, cost2)` (with cost2 considered only when i ≥ 2)

------------------------------------------------------------------------

## 🧱 4. Base Cases

- dp[0] = 0 (already at start, no energy)
- If n == 1 → answer is 0
- If n == 2 → answer is `|height[1] - height[0]|`

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

We only need the last two states:
- prev2 = dp[i-2]
- prev1 = dp[i-1]
- curr  = min(prev1 + |h[i]-h[i-1]|, prev2 + |h[i]-h[i-2]|)

Space reduces to O(1).

------------------------------------------------------------------------

## 💻 6A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class FrogJumpMemo {
    public int minCost(int[] height) {
        int n = height.length;
        int[] dp = new int[n];
        Arrays.fill(dp, -1);
        return solve(n - 1, height, dp);
    }

    private int solve(int i, int[] h, int[] dp) {
        if (i == 0) return 0;
        if (dp[i] != -1) return dp[i];

        int cost1 = solve(i - 1, h, dp) + Math.abs(h[i] - h[i - 1]);
        int cost2 = Integer.MAX_VALUE;
        if (i > 1) {
            cost2 = solve(i - 2, h, dp) + Math.abs(h[i] - h[i - 2]);
        }
        return dp[i] = Math.min(cost1, cost2);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n) (recursion + memo)

------------------------------------------------------------------------

## 💻 6B. Iterative Tabulation (Bottom-Up)

```java
import java.util.*;

class FrogJumpTab {
    public int minCost(int[] height) {
        int n = height.length;
        if (n <= 1) return 0;

        int[] dp = new int[n];
        dp[0] = 0;
        dp[1] = Math.abs(height[1] - height[0]);

        for (int i = 2; i < n; i++) {
            int cost1 = dp[i - 1] + Math.abs(height[i] - height[i - 1]);
            int cost2 = dp[i - 2] + Math.abs(height[i] - height[i - 2]);
            dp[i] = Math.min(cost1, cost2);
        }
        return dp[n - 1];
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n)

------------------------------------------------------------------------

## 💻 6C. Space-Optimized (O(1) extra)

```java
class FrogJumpSpace {
    public int minCost(int[] height) {
        int n = height.length;
        if (n <= 1) return 0;
        if (n == 2) return Math.abs(height[1] - height[0]);

        int prev2 = 0; // dp[0]
        int prev1 = Math.abs(height[1] - height[0]); // dp[1]

        for (int i = 2; i < n; i++) {
            int cost1 = prev1 + Math.abs(height[i] - height[i - 1]);
            int cost2 = prev2 + Math.abs(height[i] - height[i - 2]);
            int curr = Math.min(cost1, cost2);
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

## 🔎 7. Full Dry Run Example

Input:
- height = [10, 20, 30, 10]

dp:
- dp[0] = 0
- dp[1] = |20 - 10| = 10
- dp[2] = min(
    dp[1] + |30 - 20| = 10 + 10 = 20,
    dp[0] + |30 - 10| = 0 + 20 = 20
  ) = 20
- dp[3] = min(
    dp[2] + |10 - 30| = 20 + 20 = 40,
    dp[1] + |10 - 20| = 10 + 10 = 20
  ) = 20

Answer: 20

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Local Transition Linear DP (Minimization)
- Family: 1D DP (similar to House Robber structure but with cost function)
- Triggers:
  - Move along a line with limited step sizes (1 or 2)
  - Cost to reach i depends on i-1 and i-2 only

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- n = 0 or n = 1 → 0
- Equal adjacent heights yield zero-cost jumps
- Don’t forget to guard i-2 when i = 1
- Using MAX_VALUE + something can overflow; compute candidates separately, then min

------------------------------------------------------------------------

## 🔁 10. Variants

- K-Frog Jump: can jump up to k steps; recurrence generalizes to:
  - dp[i] = min for j in [1..k, j ≤ i] of dp[i-j] + |h[i] - h[i-j]|
  - Time O(nk), Space O(1) if only last k used via window optimizations (careful)
- Paths reconstruction: keep `parent[i]` storing the best previous index

------------------------------------------------------------------------

## ✅ 11. Takeaway

- Define dp[i] as min energy to reach i.
- Transition comes from i-1 and i-2 (or 1..k).
- Prefer O(1) space version in interviews; memo/tab versions are good for clarity.

------------------------------------------------------------------------

# End of Notes
