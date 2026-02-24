# Dynamic Programming Notes

## 09 - Geek's Training (Max Points Without Repeating Tasks Consecutively)

**Generated on:** 2026-02-24 19:13:11 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given a matrix `arr[N][3]` where:
- `arr[day][task]` = points gained by doing `task ∈ {0,1,2}` on that `day`
- Constraint: You cannot perform the same task on two consecutive days

Goal: Maximize total points over `N` days.

This is a classic 2D DP where the choice on a day depends on the previous day's choice.

------------------------------------------------------------------------

## 🪜 2. State Definition

Two equivalent state designs:

A) Recurrence (day, lastTask):
- `dp[day][last]` = maximum points up to `day` if yesterday’s task was `last`
- `last ∈ {0,1,2,3}` where `3` means “no restriction” (i.e., at the start)
- Answer: `dp[N-1][3]`

B) Simpler tabulation (day, task):
- `dp[day][task]` = max points up to `day` if today you perform `task`
- Answer: `max(dp[N-1][0], dp[N-1][1], dp[N-1][2])`

We’ll use (A) as in the PDF for clarity and easy space optimization.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For day `d`, and “yesterday’s task” = `last`:
- `dp[d][last] = max over task in {0,1,2}, task != last of ( arr[d][task] + dp[d-1][task] )`

Base (day = 0):
- `dp[0][last] = max over task != last of arr[0][task]`
- Particularly, `dp[0][3] = max(arr[0][0], arr[0][1], arr[0][2])`

Answer: `dp[N-1][3]`

------------------------------------------------------------------------

## 🧱 4. Base Cases

- If `N == 1` → answer is `max(arr[0])`
- As above, initialize `dp[0][last]` for `last ∈ {0,1,2,3}`

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

The transition for `day` only depends on `day-1`.  
Use two 4-length arrays:
- `prev[4]` holds `dp[day-1][*]`
- `curr[4]` computes `dp[day][*]`
Space reduces to O(1) extra.

------------------------------------------------------------------------

## 💻 6A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class GeeksTrainingMemo {
    // dp[day][last], last in {0,1,2,3}, 3 = no restriction
    Integer[][] dp;

    public int maximumPoints(int[][] arr) {
        int n = arr.length;
        dp = new Integer[n][4];
        return solve(arr, n - 1, 3);
    }

    private int solve(int[][] arr, int day, int last) {
        if (day == 0) {
            int best = 0;
            for (int task = 0; task < 3; task++) {
                if (task != last) best = Math.max(best, arr[0][task]);
            }
            return best;
        }

        if (dp[day][last] != null) return dp[day][last];

        int best = 0;
        for (int task = 0; task < 3; task++) {
            if (task != last) {
                int gain = arr[day][task] + solve(arr, day - 1, task);
                best = Math.max(best, gain);
            }
        }
        return dp[day][last] = best;
    }
}
```

Complexity:
- Time: O(N * 4 * 3) ≈ O(N)
- Space: O(N) recursion + memo

------------------------------------------------------------------------

## 💻 6B. Iterative Tabulation (Bottom-Up, dp[day][4])

```java
import java.util.*;

class GeeksTrainingTab {
    public int maximumPoints(int[][] arr) {
        int n = arr.length;
        int[][] dp = new int[n][4];

        // Base for day 0
        dp[0][0] = Math.max(arr[0][1], arr[0][2]);                  // last=0 → today can't pick 0
        dp[0][1] = Math.max(arr[0][0], arr[0][2]);                  // last=1 → today can't pick 1
        dp[0][2] = Math.max(arr[0][0], arr[0][1]);                  // last=2 → today can't pick 2
        dp[0][3] = Math.max(arr[0][0], Math.max(arr[0][1], arr[0][2])); // last=3 (no restriction)

        for (int day = 1; day < n; day++) {
            for (int last = 0; last < 4; last++) {
                dp[day][last] = 0;
                for (int task = 0; task < 3; task++) {
                    if (task != last) {
                        int points = arr[day][task] + dp[day - 1][task];
                        dp[day][last] = Math.max(dp[day][last], points);
                    }
                }
            }
        }
        return dp[n - 1][3];
    }
}
```

Complexity:
- Time: O(N * 4 * 3)
- Space: O(N * 4)

------------------------------------------------------------------------

## 💻 6C. Space-Optimized (Rolling 4-length arrays)

```java
class GeeksTrainingSpace {
    public int maximumPoints(int[][] arr) {
        int n = arr.length;

        int[] prev = new int[4];
        // Base for day 0
        prev[0] = Math.max(arr[0][1], arr[0][2]);
        prev[1] = Math.max(arr[0][0], arr[0][2]);
        prev[2] = Math.max(arr[0][0], arr[0][1]);
        prev[3] = Math.max(arr[0][0], Math.max(arr[0][1], arr[0][2]));

        for (int day = 1; day < n; day++) {
            int[] curr = new int[4];
            for (int last = 0; last < 4; last++) {
                int best = 0;
                for (int task = 0; task < 3; task++) {
                    if (task != last) {
                        best = Math.max(best, arr[day][task] + prev[task]);
                    }
                }
                curr[last] = best;
            }
            prev = curr;
        }
        return prev[3];
    }
}
```

Complexity:
- Time: O(N * 4 * 3)
- Space: O(1) extra

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input:
```
arr = [
  [1, 2, 5],
  [3, 1, 1],
  [3, 3, 3]
]
```

Day 0:
- last=0 → max(2,5)=5
- last=1 → max(1,5)=5
- last=2 → max(1,2)=2
- last=3 → max(1,2,5)=5

Day 1 (using prev = [5,5,2,5]):
- last=0 → max(task 1 or 2):
  - t=1: 1 + prev[1]=1+5=6
  - t=2: 1 + prev[2]=1+2=3 → dp=6
- last=1 → max(task 0 or 2):
  - t=0: 3 + prev[0]=3+5=8
  - t=2: 1 + prev[2]=1+2=3 → dp=8
- last=2 → max(task 0 or 1):
  - t=0: 3 + prev[0]=3+5=8
  - t=1: 1 + prev[1]=1+5=6 → dp=8
- last=3 → max(all tasks):
  - max(3+5, 1+5, 1+2) = max(8,6,3)=8

Day 2 (compute similarly) → final answer prev[3] = 11

One optimal schedule:
- Day 0: task 2 (5)
- Day 1: task 0 (3)
- Day 2: task 1 (3)
Total = 11

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: “DP with previous choice restriction”
- Family: 2D DP with “last taken” dimension
- Triggers:
  - At each step choose among K options
  - Cannot repeat the same option consecutively

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- N = 1 → return max of the first row
- Ensure base `dp[0][last]` is set correctly (exclude `last`)
- Use the correct index for carry-over: when you pick `task` today, transition from `dp[day-1][task]`
- If values can be large, keep type as `int` unless overflow risk is explicit

------------------------------------------------------------------------

## 🔁 10. Variants

- K tasks instead of 3: use arrays of size `K+1` for `last` (with `K` = “no restriction”)
- Add cooldown on specific tasks: extend state with cooldown counters
- Weighted penalties for repeating tasks: modify recurrence to subtract penalty if `task == last`

------------------------------------------------------------------------

## ✅ 11. Takeaway

- Store the “last picked” dimension to enforce “no two consecutive same tasks.”
- Tabulation with a rolling 4-length array is clean and optimal.
- This template generalizes to K options and other “no-repeat” scheduling problems.

------------------------------------------------------------------------

# End of Notes
