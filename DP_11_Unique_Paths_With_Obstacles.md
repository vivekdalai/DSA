# Dynamic Programming Notes

## 11 - Unique Paths With Obstacles (Grid m x n)

**Generated on:** 2026-02-24 19:18:31 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a grid `m x n` with obstacles, count the number of unique paths from top-left `(0,0)` to bottom-right `(m-1,n-1)` if you can move only:
- Right `(i, j) → (i, j+1)`
- Down `(i, j) → (i+1, j)`

Obstacle conventions:
- LeetCode uses `1` to denote obstacle, `0` free.
- Some platforms (as in your PDF) use `-1` as obstacle, `0/positive` as free.

Goal: Count all valid paths that avoid obstacles.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[i][j]` = number of unique paths to reach cell `(i, j)` from `(0, 0)` without stepping on obstacles.

Goal: `dp[m-1][n-1]`.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

If `(i, j)` is an obstacle → `dp[i][j] = 0`.

Otherwise:
- If `(i, j) == (0, 0)` and not obstacle → `dp[0][0] = 1`
- For general cell:
  - `dp[i][j] = (i > 0 ? dp[i-1][j] : 0) + (j > 0 ? dp[i][j-1] : 0)`

------------------------------------------------------------------------

## 🧱 4. Base Cases

- If start `(0,0)` is blocked → answer is `0`
- If end `(m-1,n-1)` is blocked → answer is `0`
- First row/column:
  - If a cell is blocked, all cells to the right/below in that row/col become `0` (cannot pass through)

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

Each `dp[i][j]` depends only on:
- `dp[i-1][j]` (up) from previous row
- `dp[i][j-1]` (left) from current row

Thus we can use:
- 1D array of size `n` with in-place update: `row[j] = (obstacle ? 0 : row[j] + row[j-1])`

------------------------------------------------------------------------

## 💻 6A. Top-Down (Recursive with Memoization)

Obstacle value expected: LeetCode-style (`1` = obstacle). Adjust `isBlocked` as needed.

```java
import java.util.*;

class UniquePathsObstaclesMemo {
    private Integer[][] memo;
    private int[][] grid;

    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        this.grid = obstacleGrid;
        int m = grid.length, n = grid[0].length;

        if (isBlocked(0, 0) || isBlocked(m - 1, n - 1)) return 0;

        memo = new Integer[m][n];
        return dfs(m - 1, n - 1);
    }

    private boolean isBlocked(int i, int j) {
        // LeetCode: 1 is obstacle. If your platform uses -1, change to: grid[i][j] == -1
        return grid[i][j] == 1;
    }

    private int dfs(int i, int j) {
        if (i < 0 || j < 0) return 0;
        if (isBlocked(i, j)) return 0;
        if (i == 0 && j == 0) return 1;

        if (memo[i][j] != null) return memo[i][j];

        int up = dfs(i - 1, j);
        int left = dfs(i, j - 1);

        return memo[i][j] = up + left;
    }
}
```

Complexity:
- Time: O(m·n)
- Space: O(m·n) for memo + recursion stack

------------------------------------------------------------------------

## 💻 6B. Bottom-Up (Tabulation)

Version A: LeetCode style (1 = obstacle)

```java
class UniquePathsObstaclesTab {
    public int uniquePathsWithObstacles(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        if (grid[0][0] == 1 || grid[m - 1][n - 1] == 1) return 0;

        int[][] dp = new int[m][n];
        dp[0][0] = 1;

        // first row
        for (int j = 1; j < n; j++) {
            dp[0][j] = (grid[0][j] == 1) ? 0 : dp[0][j - 1];
        }

        // first column
        for (int i = 1; i < m; i++) {
            dp[i][0] = (grid[i][0] == 1) ? 0 : dp[i - 1][0];
        }

        // rest cells
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (grid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }
        return dp[m - 1][n - 1];
    }
}
```

Version B: PDF style (−1 = obstacle) with modulo

```java
class UniquePathsObstaclesTabMod {
    private static final int MOD = 1_000_000_007;

    public int mazeObstacles(int n, int m, int[][] mat) {
        if (mat[0][0] == -1 || mat[n - 1][m - 1] == -1) return 0;

        int[][] dp = new int[n][m];

        dp[0][0] = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == -1) {
                    dp[i][j] = 0;
                } else if (i == 0 && j == 0) {
                    // already set
                } else {
                    long up = (i > 0) ? dp[i - 1][j] : 0;
                    long left = (j > 0) ? dp[i][j - 1] : 0;
                    dp[i][j] = (int) ((up + left) % MOD);
                }
            }
        }
        return dp[n - 1][m - 1];
    }
}
```

------------------------------------------------------------------------

## 💻 6C. Space-Optimized (1D DP)

LeetCode style (1 = obstacle):

```java
class UniquePathsObstaclesSpace {
    public int uniquePathsWithObstacles(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] row = new int[n];

        // start
        row[0] = (grid[0][0] == 1) ? 0 : 1;

        // first row
        for (int j = 1; j < n; j++) {
            row[j] = (grid[0][j] == 1) ? 0 : row[j - 1];
        }

        for (int i = 1; i < m; i++) {
            // first col of this row
            row[0] = (grid[i][0] == 1) ? 0 : row[0];
            for (int j = 1; j < n; j++) {
                if (grid[i][j] == 1) {
                    row[j] = 0;
                } else {
                    row[j] = row[j] + row[j - 1]; // up (row[j]) + left (row[j-1])
                }
            }
        }
        return row[n - 1];
    }
}
```

PDF style (−1 = obstacle) with modulo:

```java
class UniquePathsObstaclesSpaceMod {
    private static final int MOD = 1_000_000_007;

    public int mazeObstacles(int n, int m, int[][] mat) {
        if (mat[0][0] == -1 || mat[n - 1][m - 1] == -1) return 0;

        int[] prev = new int[m];

        for (int i = 0; i < n; i++) {
            int[] curr = new int[m];
            for (int j = 0; j < m; j++) {
                if (mat[i][j] == -1) {
                    curr[j] = 0;
                } else if (i == 0 && j == 0) {
                    curr[j] = 1;
                } else {
                    long up = (i > 0) ? prev[j] : 0;
                    long left = (j > 0) ? curr[j - 1] : 0;
                    curr[j] = (int) ((up + left) % MOD);
                }
            }
            prev = curr;
        }
        return prev[m - 1];
    }
}
```

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Grid (1 = obstacle):
```
grid = [
  [0, 0, 0],
  [0, 1, 0],
  [0, 0, 0]
]
```

dp:
- Row 0: [1, 1, 1]
- Row 1:
  - dp[1][0] = 1
  - dp[1][1] = 0 (obstacle)
  - dp[1][2] = dp[0][2] + dp[1][1] = 1 + 0 = 1
- Row 2:
  - dp[2][0] = 1
  - dp[2][1] = dp[1][1] + dp[2][0] = 0 + 1 = 1
  - dp[2][2] = dp[1][2] + dp[2][1] = 1 + 1 = 2

Answer: 2

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Grid Path Counting with Blocked Cells
- Family: 2D DP with obstacles
- Triggers:
  - Count paths with only right/down moves
  - Some cells contribute 0 (blocked)

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- Start or End blocked → immediate 0
- Entire first row/column blocked early → zeros propagate
- Use modulo if required by platform
- When using 1D DP, ensure correct update order: left-to-right within a row

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Same additive DP as Unique Paths, but set blocked cells to 0.
- 1D rolling DP is optimal in space.
- Align obstacle convention with the platform: 1 or −1 for blocked.

------------------------------------------------------------------------

# End of Notes
