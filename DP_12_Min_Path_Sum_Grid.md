# Dynamic Programming Notes

## 12 - Minimum Path Sum in Grid (Top-Left to Bottom-Right)

**Generated on:** 2026-02-24 19:20:11 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an `m x n` grid `grid[i][j]` of non-negative integers,  
find a path from top-left `(0,0)` to bottom-right `(m-1,n-1)` which minimizes the sum of the numbers along its path.

Allowed moves:
- Right `(i, j) → (i, j+1)`
- Down  `(i, j) → (i+1, j)`

Goal: Return the minimum possible path sum.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[i][j]` = minimum path sum to reach `(i, j)` from `(0, 0)`.

Goal: `dp[m-1][n-1]`.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For `i, j ≥ 0`:
- If `(i, j) == (0, 0)`:
  - `dp[0][0] = grid[0][0]`
- Otherwise:
  - `dp[i][j] = grid[i][j] + min( i > 0 ? dp[i-1][j] : INF, j > 0 ? dp[i][j-1] : INF )`

Interpretation:
- To reach `(i, j)`, we must come either from top or from left. Choose the cheaper.

------------------------------------------------------------------------

## 🧱 4. Base Cases

- `dp[0][0] = grid[0][0]`
- First row: `dp[0][j] = grid[0][j] + dp[0][j-1]`
- First column: `dp[i][0] = grid[i][0] + dp[i-1][0]`

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

Each state depends only on:
- Same column in previous row (up)
- Previous column in current row (left)

We can compress to a 1D array over columns:
- `curr[j] = grid[i][j] + min(prev[j], curr[j-1])`

Space reduces to O(n).

------------------------------------------------------------------------

## 💻 6A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class MinPathSumMemo {
    private int[][] grid;
    private Integer[][] memo;
    private static final int INF = 1_000_000_000;

    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        this.grid = grid;
        this.memo = new Integer[m][n];
        return dfs(m - 1, n - 1);
    }

    private int dfs(int i, int j) {
        if (i < 0 || j < 0) return INF;               // invalid path
        if (i == 0 && j == 0) return grid[0][0];      // base
        if (memo[i][j] != null) return memo[i][j];

        int top = dfs(i - 1, j);
        int left = dfs(i, j - 1);
        return memo[i][j] = grid[i][j] + Math.min(top, left);
    }
}
```

Complexity:
- Time: O(m·n)
- Space: O(m·n) for memo + recursion stack

------------------------------------------------------------------------

## 💻 6B. Iterative Tabulation (Bottom-Up)

```java
class MinPathSumTab {
    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];

        dp[0][0] = grid[0][0];

        // first row
        for (int j = 1; j < n; j++) {
            dp[0][j] = grid[0][j] + dp[0][j - 1];
        }
        // first column
        for (int i = 1; i < m; i++) {
            dp[i][0] = grid[i][0] + dp[i - 1][0];
        }

        // rest cells
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = grid[i][j] + Math.min(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[m - 1][n - 1];
    }
}
```

Complexity:
- Time: O(m·n)
- Space: O(m·n)

------------------------------------------------------------------------

## 💻 6C. Space-Optimized (1D over columns)

```java
class MinPathSumSpace {
    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;

        int[] prev = new int[n];

        // row 0
        prev[0] = grid[0][0];
        for (int j = 1; j < n; j++) {
            prev[j] = grid[0][j] + prev[j - 1];
        }

        for (int i = 1; i < m; i++) {
            int[] curr = new int[n];
            // col 0 for this row
            curr[0] = grid[i][0] + prev[0];

            for (int j = 1; j < n; j++) {
                curr[j] = grid[i][j] + Math.min(prev[j], curr[j - 1]);
            }
            prev = curr;
        }
        return prev[n - 1];
    }
}
```

Complexity:
- Time: O(m·n)
- Space: O(n)

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input:
```
grid = [
  [1, 3, 1],
  [1, 5, 1],
  [4, 2, 1]
]
```

Tabulation:
- dp[0] = [1, 4, 5]
- dp[1][0] = 1 + 1 = 2
- dp[1][1] = 5 + min(4, 2) = 7
- dp[1][2] = 1 + min(5, 7) = 6
- dp[2][0] = 4 + 2 = 6
- dp[2][1] = 2 + min(7, 6) = 8
- dp[2][2] = 1 + min(6, 8) = 7

Answer: 7 (one optimal path: 1→3→1→1→1)

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Grid DP (Min Path Sum)
- Family: 2D DP with additive costs
- Triggers:
  - Move right/down minimizing cumulative weight
  - Transition from top and left only

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- Single cell grid → answer is grid[0][0]
- Large values: keep sums within int range (under typical constraints it’s fine)
- Don’t mix INF with modulo; for pure minimization INF is a large sentinel only

------------------------------------------------------------------------

## 🔁 10. Variants

- With obstacles: treat blocked cells as INF (or skip them) → paths that pass them become invalid
- With negative values (rare in this problem): standard DP still works if no negative cycles (none here due to DAG)
- Diagonal moves allowed: add third candidate `dp[i-1][j-1]`

------------------------------------------------------------------------

## ✅ 11. Takeaway

- Define dp as minimum cost to reach each cell.
- Transition is “current cell cost + min(top, left).”
- Prefer O(n) space rolling-array solution in interviews.

------------------------------------------------------------------------

# End of Notes
