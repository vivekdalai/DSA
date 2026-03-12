# Dynamic Programming Notes

## 18 - 0/1 Knapsack — Recursion, Memoization, Tabulation, Space-Optimized

**Generated on:** 2026-02-24 20:41:53 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given:
- `wt[i]` = weight of the i-th item
- `val[i]` = value of the i-th item
- `W` = maximum capacity of the knapsack

Each item can be picked at most once (0 or 1 times).
Goal: Maximize total value without exceeding capacity `W`.

This is the canonical 0/1 DP problem with a binary choice per item.

------------------------------------------------------------------------

## 🪜 2. State Definition

Two equivalent formulations:

A) Index and Capacity (top-down):
- `f(idx, cap)` = max value using items from `0..idx` with capacity `cap`
- Answer: `f(n-1, W)`

B) Item Count and Capacity (bottom-up):
- `dp[i][cap]` = max value using first `i` items with capacity `cap`
- Answer: `dp[n][W]`

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

At item `i`, capacity `cap`:
- If we do not take item `i`:
  - value = `f(i-1, cap)`
- If we take item `i` (only if `wt[i] <= cap`):
  - value = `val[i] + f(i-1, cap - wt[i])`

Therefore:
- `f(i, cap) = max( f(i-1, cap), val[i] + f(i-1, cap - wt[i]) )` (when `wt[i] <= cap`)
- else `f(i, cap) = f(i-1, cap)`

Base:
- If `i < 0` or `cap == 0`: return 0

------------------------------------------------------------------------

## 🧱 4. Base Cases

- No items or zero capacity → 0
- If using bottom-up with `dp[n+1][W+1]`:
  - `dp[0][cap] = 0` for all `cap`
  - `dp[i][0] = 0` for all `i`

------------------------------------------------------------------------

## 💻 5A. Recursive (Exponential) — For Intuition

```java
class KnapsackRecursive {
    public int knapSack(int W, int[] wt, int[] val) {
        return dfs(wt.length - 1, W, wt, val);
    }

    private int dfs(int idx, int cap, int[] wt, int[] val) {
        if (idx < 0 || cap == 0) return 0;

        // skip
        int notTake = dfs(idx - 1, cap, wt, val);

        // take if fits
        int take = 0;
        if (wt[idx] <= cap) {
            take = val[idx] + dfs(idx - 1, cap - wt[idx], wt, val);
        }

        return Math.max(take, notTake);
    }
}
```

Complexity:
- Time: O(2^n)
- Space: O(n) recursion depth

------------------------------------------------------------------------

## 💻 5B. Memoization (Top-Down)

```java
import java.util.Arrays;

class KnapsackMemo {
    private int[][] dp;

    public int knapSack(int W, int[] wt, int[] val) {
        int n = wt.length;
        dp = new int[n][W + 1];
        for (int[] row : dp) Arrays.fill(row, -1);
        return dfs(n - 1, W, wt, val);
    }

    private int dfs(int idx, int cap, int[] wt, int[] val) {
        if (idx < 0 || cap == 0) return 0;
        if (dp[idx][cap] != -1) return dp[idx][cap];

        int notTake = dfs(idx - 1, cap, wt, val);
        int take = 0;
        if (wt[idx] <= cap) {
            take = val[idx] + dfs(idx - 1, cap - wt[idx], wt, val);
        }

        return dp[idx][cap] = Math.max(take, notTake);
    }
}
```

Complexity:
- Time: O(n · W)
- Space: O(n · W) memo + O(n) recursion

------------------------------------------------------------------------

## 💻 5C. Tabulation (Bottom-Up)

```java
import java.util.Arrays;

class KnapsackTab {
    public int knapSack(int W, int[] wt, int[] val) {
        int n = wt.length;
        int[][] dp = new int[n + 1][W + 1];

        // dp[0][*] = 0, dp[*][0] = 0 (default)

        for (int i = 1; i <= n; i++) {
            int weight = wt[i - 1];
            int value  = val[i - 1];

            for (int cap = 0; cap <= W; cap++) {
                int notTake = dp[i - 1][cap];
                int take = 0;
                if (weight <= cap) {
                    take = value + dp[i - 1][cap - weight];
                }
                dp[i][cap] = Math.max(take, notTake);
            }
        }
        return dp[n][W];
    }
}
```

Complexity:
- Time: O(n · W)
- Space: O(n · W)

------------------------------------------------------------------------

## 💻 5D. Space-Optimized (O(W)) — Backward Capacity Loop

0/1 means an item can be used once. Use a single 1D array and iterate `cap` backward to avoid reusing the same item more than once.

```java
class KnapsackSpaceOptimized {
    public int knapSack(int W, int[] wt, int[] val) {
        int n = wt.length;
        int[] dp = new int[W + 1]; // dp[cap] = max value with capacity 'cap'

        for (int i = 0; i < n; i++) {
            int weight = wt[i], value = val[i];
            for (int cap = W; cap >= weight; cap--) {
                dp[cap] = Math.max(dp[cap], value + dp[cap - weight]);
            }
        }
        return dp[W];
    }
}
```

Why backward?
- If you go forward, you’d allow the same item to be counted multiple times (turns into unbounded knapsack behavior).
- Backward ensures each item contributes at most once.

Complexity:
- Time: O(n · W)
- Space: O(W)

------------------------------------------------------------------------

## 🔎 6. Dry Run (Backward 1D)

Items:
```
val = [2, 4, 5], wt = [2, 3, 4], W = 5
```

Initially:
```
dp = [0, 0, 0, 0, 0, 0]
```

Item 0: (w=2, v=2), cap = 5..2
- cap=5: dp[5] = max(0, 2 + dp[3]=0) = 2
- cap=4: dp[4] = max(0, 2 + dp[2]=0) = 2
- cap=3: dp[3] = max(0, 2 + dp[1]=0) = 2
- cap=2: dp[2] = max(0, 2 + dp[0]=0) = 2

Item 1: (w=3, v=4), cap = 5..3
- cap=5: dp[5] = max(2, 4 + dp[2]=2) = 6
- cap=4: dp[4] = max(2, 4 + dp[1]=0) = 4
- cap=3: dp[3] = max(2, 4 + dp[0]=0) = 4

Item 2: (w=4, v=5), cap = 5..4
- cap=5: dp[5] = max(6, 5 + dp[1]=0) = 6
- cap=4: dp[4] = max(4, 5 + dp[0]=0) = 5

Final `dp[5] = 6` (pick items with weights 2 and 3).

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: 0/1 Knapsack (Binary Choice per Item)
- Family: DP over (index, capacity)
- Triggers:
  - Each item can be used at most once
  - Maximize/minimize over capacity constraint
- Variants:
  - Subset sum (boolean feasibility)
  - Equal sum partition, min subset sum difference
  - Target sum (count of ways)
  - Bounded knapsack (limited copies)
  - Unbounded knapsack (infinite copies) → forward capacity loop

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- Ensure backward capacity loop in the 1D optimization for 0/1 variant.
- Watch for `W = 0` or `n = 0` cases → result is 0.
- Arrays length mismatch; validate `wt.length == val.length`.
- Large `W` or `n` → prefer 1D space-optimized solution.

------------------------------------------------------------------------

## 🔁 9. Useful Extensions

- Recover chosen items:
  - Use the 2D `dp` table and backtrack from `(n, W)` to find which items were included.
- Time/Value constraints:
  - Same structure with different resource interpretations.

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Master all four implementations: recursion, memoization, tabulation, and 1D backward optimization.
- The backward 1D DP is the standard interview-ready approach for 0/1 knapsack.

------------------------------------------------------------------------

# End of Notes
