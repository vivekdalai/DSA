# Dynamic Programming Notes

## 07 - Maximum Sum Without Adjacent Elements

**Generated on:** 2026-02-24 19:11:05 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `arr[]`, pick elements to maximize the total sum such that you never pick two adjacent elements.

This is the core pattern behind House Robber I (linear street).  
It’s a 1D DP maximization with an adjacency conflict.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- dp[i] = maximum sum we can get from subarray arr[0..i] with the “no-adjacent” constraint

Goal: dp[n - 1]

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

At index i:
- Include arr[i] → we must skip i-1 → sum = arr[i] + dp[i-2]
- Exclude arr[i] → take best up to i-1 → sum = dp[i-1]

Therefore:
- dp[i] = max(dp[i-1], arr[i] + dp[i-2])

------------------------------------------------------------------------

## 🧱 4. Base Cases

- If n == 0 → 0
- If n == 1 → arr[0]
- Initialize:
  - dp[0] = arr[0]
  - dp[1] = max(arr[0], arr[1])   (when n ≥ 2)

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

Only the last two states are required:
- prev2 = dp[i-2]
- prev1 = dp[i-1]
- curr  = max(prev1, prev2 + arr[i])

Space reduces to O(1).

------------------------------------------------------------------------

## 💻 6A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class MaxSumNoAdjMemo {
    public int findMaxSum(int[] arr) {
        int n = arr.length;
        if (n == 0) return 0;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MIN_VALUE);
        return solve(n - 1, arr, dp);
    }

    private int solve(int i, int[] arr, int[] dp) {
        if (i < 0) return 0;              // nothing to pick
        if (i == 0) return arr[0];        // only first element
        if (dp[i] != Integer.MIN_VALUE) return dp[i];

        int include = arr[i] + solve(i - 2, arr, dp);
        int exclude = solve(i - 1, arr, dp);

        return dp[i] = Math.max(include, exclude);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n) (memo + recursion)

------------------------------------------------------------------------

## 💻 6B. Iterative Tabulation (Bottom-Up)

```java
import java.util.*;

class MaxSumNoAdjTab {
    public int findMaxSum(int[] arr) {
        int n = arr.length;
        if (n == 0) return 0;
        if (n == 1) return arr[0];

        int[] dp = new int[n];
        dp[0] = arr[0];
        dp[1] = Math.max(arr[0], arr[1]);

        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(dp[i - 1], arr[i] + dp[i - 2]);
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
class MaxSumNoAdjSpace {
    public int findMaxSum(int[] arr) {
        int n = arr.length;
        if (n == 0) return 0;
        if (n == 1) return arr[0];

        int prev2 = arr[0];
        int prev1 = Math.max(arr[0], arr[1]);

        for (int i = 2; i < n; i++) {
            int curr = Math.max(prev1, prev2 + arr[i]);
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
- arr = [3, 2, 7, 10]

dp:
- dp[0] = 3
- dp[1] = max(3, 2) = 3
- dp[2] = max(dp[1], arr[2] + dp[0]) = max(3, 7 + 3) = 10
- dp[3] = max(dp[2], arr[3] + dp[1]) = max(10, 10 + 3) = 13

Answer: 13 (pick 3 and 10)

Another:
- arr = [5, 5, 10, 100, 10, 5] → Answer = 110 (pick 5, 100, 5)

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: “Skip Adjacent” / House Robber (Linear)
- Triggers:
  - Items on a line, cannot pick neighbors simultaneously
- Relatives:
  - House Robber I (exact same)
  - Delete and Earn after value-compression to indices
  - House Robber II (circular street variant: split into [0..n-2] and [1..n-1])

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- n = 0 → 0
- n = 1 → arr[0]
- Negative values: the recurrence still works; dp[1] must be max(arr[0], arr[1])
- Large arrays: O(1) space version is preferred
- Don’t double-count adjacent picks

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Canonical DP where each choice conflicts with immediate neighbor.
- Template reappears in many problems (House Robber family, value-index reductions).
- Favor the O(1) rolling-variables solution in interviews.

------------------------------------------------------------------------

# End of Notes
