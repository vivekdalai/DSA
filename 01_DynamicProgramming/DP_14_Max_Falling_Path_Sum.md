# Dynamic Programming Notes

## 14 - Maximum Falling Path Sum (Down, Down-Left, Down-Right)

**Generated on:** 2026-02-24 19:23:01 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an `N x M` matrix `matrix[i][j]` (can be negative), find the maximum path sum from any cell in the first row to any cell in the last row, where from `(i, j)` you may move to:
- Down: `(i+1, j)`
- Down-left: `(i+1, j-1)`
- Down-right: `(i+1, j+1)`

Goal: Maximize the sum along the path.

This is a 2D DP with transitions coming from the previous row.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[i][j]` = maximum path sum to reach cell `(i, j)` from some cell in row `0`.

Answer:
- `max_j dp[N-1][j]` (best among last row)

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

Base (first row):
- `dp[0][j] = matrix[0][j]` for all `j`

Transition (for `i >= 1`):
- `dp[i][j] = matrix[i][j] + max( up, upLeft, upRight )` where
  - `up = dp[i-1][j]`
  - `upLeft = (j > 0 ? dp[i-1][j-1] : -INF)`
  - `upRight = (j < M-1 ? dp[i-1][j+1] : -INF)`

Use a large negative sentinel (e.g., `-1_000_000_000`) for invalid directions.

------------------------------------------------------------------------

## 🧱 4. Base Cases

- If `N == 0` or `M == 0` → result is 0 (or invalid by problem statement)
- Initialize first row directly from `matrix[0][*]`

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

Each row depends only on the previous row.  
Use a rolling 1D array:
- `prev[j]` holds dp for row `i-1`
- `curr[j]` computes dp for row `i`
- After each row, set `prev = curr`

Space reduces to O(M).

------------------------------------------------------------------------

## 💻 6A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class MaxFallingPathMemo {
    private int[][] a;
    private Integer[][] dp;
    private int n, m;
    private static final int NEG_INF = -1_000_000_000;

    public int getMaxPathSum(int[][] matrix) {
        this.a = matrix;
        this.n = a.length;
        this.m = a[0].length;

        dp = new Integer[n][m];

        int best = NEG_INF;
        for (int col = 0; col < m; col++) {
            best = Math.max(best, dfs(n - 1, col));
        }
        return best;
    }

    // Max path sum ending at (i, j) coming from row 0
    private int dfs(int i, int j) {
        if (j < 0 || j >= m) return NEG_INF;
        if (i == 0) return a[0][j];
        if (dp[i][j] != null) return dp[i][j];

        int up      = dfs(i - 1, j);
        int upLeft  = dfs(i - 1, j - 1);
        int upRight = dfs(i - 1, j + 1);

        return dp[i][j] = a[i][j] + Math.max(up, Math.max(upLeft, upRight));
    }
}
```

Complexity:
- Time: O(N·M) after memoization
- Space: O(N·M) memo + O(N) recursion depth

------------------------------------------------------------------------

## 💻 6B. Iterative Tabulation (Bottom-Up)

```java
class MaxFallingPathTab {
    public int getMaxPathSum(int[][] matrix) {
        int n = matrix.length, m = matrix[0].length;
        int NEG_INF = -1_000_000_000;

        int[][] dp = new int[n][m];

        // base: first row
        for (int j = 0; j < m; j++) {
            dp[0][j] = matrix[0][j];
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int up = dp[i - 1][j];
                int upLeft = (j > 0) ? dp[i - 1][j - 1] : NEG_INF;
                int upRight = (j < m - 1) ? dp[i - 1][j + 1] : NEG_INF;

                dp[i][j] = matrix[i][j] + Math.max(up, Math.max(upLeft, upRight));
            }
        }

        int best = dp[n - 1][0];
        for (int j = 1; j < m; j++) best = Math.max(best, dp[n - 1][j]);
        return best;
    }
}
```

Complexity:
- Time: O(N·M)
- Space: O(N·M)

------------------------------------------------------------------------

## 💻 6C. Space-Optimized (O(M))

```java
class MaxFallingPathSpace {
    public int getMaxPathSum(int[][] matrix) {
        int n = matrix.length, m = matrix[0].length;
        int NEG_INF = -1_000_000_000;

        int[] prev = new int[m];
        // base row
        for (int j = 0; j < m; j++) prev[j] = matrix[0][j];

        for (int i = 1; i < n; i++) {
            int[] curr = new int[m];
            for (int j = 0; j < m; j++) {
                int up = prev[j];
                int upLeft = (j > 0) ? prev[j - 1] : NEG_INF;
                int upRight = (j < m - 1) ? prev[j + 1] : NEG_INF;
                curr[j] = matrix[i][j] + Math.max(up, Math.max(upLeft, upRight));
            }
            prev = curr;
        }

        int best = prev[0];
        for (int j = 1; j < m; j++) best = Math.max(best, prev[j]);
        return best;
    }
}
```

Complexity:
- Time: O(N·M)
- Space: O(M)

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input:
```
matrix = [
  [10,  2,  3],
  [ 3,  4,  5],
  [ 6,  7,  8]
]
```

dp (tabulation):
- Row 0: [10, 2, 3]
- Row 1:
  - j=0: 3 + max(10, NEG_INF, 2) = 3 + 10 = 13
  - j=1: 4 + max(2, 10, 3) = 4 + 10 = 14
  - j=2: 5 + max(3, 2, NEG_INF) = 5 + 3  = 8
  → [13, 14, 8]
- Row 2:
  - j=0: 6 + max(13, NEG_INF, 14) = 6 + 14 = 20
  - j=1: 7 + max(14, 13, 8) = 7 + 14 = 21
  - j=2: 8 + max(8, 14, NEG_INF) = 8 + 14 = 22
  → [20, 21, 22]

Answer: 22

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Falling Path DP with Diagonals
- Family: 2D DP; each cell depends on up to three cells from previous row
- Triggers:
  - Move to next row with adjacent columns (j, j-1, j+1)
  - Optimize (max/min) aggregate along a path

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- Negative values: handled by using a large negative sentinel for invalid directions
- Single row: answer is max element in that row (or sum if start must be specific cell)
- Single column: path is forced straight down (sum of column)
- Don’t access out-of-bounds for `j-1` or `j+1`

------------------------------------------------------------------------

## 🔁 10. Variants

- Minimum falling path sum: replace `max` with `min`
- Start and end at fixed columns: restrict initialization and final selection
- Count number of maximum-sum paths: keep another dp for counts with tie-handling
- Weighted constraints (e.g., penalties for direction changes): augment state with last move

------------------------------------------------------------------------

## ✅ 11. Takeaway

- Define dp over rows using transitions from the previous row (up/diagonals).
- Prefer O(M) rolling array in interviews.
- Works for both max and min variants with simple operator change.

------------------------------------------------------------------------

# End of Notes
