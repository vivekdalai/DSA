# Dynamic Programming Notes

## 10 - Unique Paths (Grid m x n)

**Generated on:** 2026-02-24 19:15:55 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given a grid with `m` rows and `n` columns.  
A robot starts at the top-left cell `(0,0)` and wants to reach the bottom-right cell `(m-1, n-1)`.  
At any step, it may move only:
- Right `(i, j) → (i, j+1)`, or
- Down `(i, j) → (i+1, j)`

Question: How many unique paths exist from start to end?

This is a classic 2D DP counting problem.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[i][j]` = number of unique paths to reach cell `(i, j)` from `(0, 0)`.

Goal: `dp[m-1][n-1]`.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For `i, j ≥ 0`:
- If `(i, j) == (0, 0)`: `dp[0][0] = 1`
- Otherwise:
  - `dp[i][j] = (i > 0 ? dp[i-1][j] : 0) + (j > 0 ? dp[i][j-1] : 0)`

Interpretation:  
To reach `(i, j)`, you must have come from either the top `(i-1, j)` or from the left `(i, j-1)`.

------------------------------------------------------------------------

## 🧱 4. Base Cases

- `dp[0][0] = 1`
- First row: only right moves → `dp[0][j] = 1` for all `j`
- First column: only down moves → `dp[i][0] = 1` for all `i`

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

Transition uses only:
- current row’s left neighbor, and
- previous row’s same column

Therefore, a single 1D array of size `n` (columns) suffices:
- `curr[j] = curr[j] (left) + prev[j] (up)`  
If we roll arrays row by row, we can even reuse one array with in-place update:
- `row[j] += row[j-1]`

------------------------------------------------------------------------

## 💻 6A. Top-Down (Recursive with Memoization)

```java
import java.util.*;

class UniquePathsMemo {
    private Integer[][] memo;

    public int uniquePaths(int m, int n) {
        memo = new Integer[m][n];
        return dfs(m - 1, n - 1);
    }

    private int dfs(int i, int j) {
        if (i < 0 || j < 0) return 0;
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

```java
class UniquePathsTab {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];

        // base cases
        for (int i = 0; i < m; i++) dp[i][0] = 1;
        for (int j = 0; j < n; j++) dp[0][j] = 1;

        // fill table
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                int up = dp[i - 1][j];
                int left = dp[i][j - 1];
                dp[i][j] = up + left;
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

## 💻 6C. Space-Optimized (1D DP)

```java
class UniquePathsSpace {
    public int uniquePaths(int m, int n) {
        int[] row = new int[n];
        // base for first row: only 1 way to go straight right
        for (int j = 0; j < n; j++) row[j] = 1;

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                row[j] = row[j] + row[j - 1]; // up (row[j]) + left (row[j-1])
            }
        }
        return row[n - 1];
    }
}
```

Complexity:
- Time: O(m·n)
- Space: O(n)

------------------------------------------------------------------------

## 💻 6D. Combinatorics (Most Optimal for Large m, n)

To reach `(m-1, n-1)` from `(0,0)`, you must make exactly:
- `(m-1)` down moves and `(n-1)` right moves
- In total `N = (m-1) + (n-1)` moves
- Choose positions of the `(m-1)` down moves (or `(n-1)` right moves):
  - `C(N, m-1) = N! / ((m-1)! (n-1)!)`

Use multiplicative formula to avoid overflow and factorials.

```java
class UniquePathsComb {
    public int uniquePaths(int m, int n) {
        int down = m - 1, right = n - 1;
        int N = down + right;
        // compute C(N, down) using double (or BigInteger for very large)
        double res = 1.0;
        for (int i = 1; i <= down; i++) {
            res = res * (N - down + i) / i;
        }
        return (int) Math.round(res);
    }
}
```

Notes:
- For huge m, n, use BigInteger to avoid precision loss.
- On coding platforms with modulo, compute nCr under modulo using modular inverses.

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input:
- m = 3, n = 3

Tabulation:
- dp initial:
  - row 0: [1, 1, 1]
  - row 1: [1, ?, ?]
  - row 2: [1, ?, ?]

Fill:
- dp[1][1] = dp[0][1] + dp[1][0] = 1 + 1 = 2
- dp[1][2] = dp[0][2] + dp[1][1] = 1 + 2 = 3
- dp[2][1] = dp[1][1] + dp[2][0] = 2 + 1 = 3
- dp[2][2] = dp[1][2] + dp[2][1] = 3 + 3 = 6

Answer: 6

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Grid Path Counting (Right/Down only)
- Family: 2D DP with simple additive transitions (top + left)
- Triggers:
  - Count number of ways from top-left to bottom-right with only right/down moves
  - Equivalent to lattice path counting (combinatorics)

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- m == 0 or n == 0: typically invalid; if allowed, answer is 0
- m == 1 or n == 1: exactly 1 path (straight line)
- Don’t reset dp[0][0] to 0 in tabulation loops; set it to 1 before filling
- When using 1D DP, ensure left dependency (`row[j-1]`) is updated after current j-1

------------------------------------------------------------------------

## 🔁 10. Variants

- Unique Paths with Obstacles (cells blocked) → treat obstacles as dp[i][j] = 0 and skip adding
- With diagonal moves → add third transition
- Count paths modulo MOD → compute dp under modulo
- Minimum path sum → change operation from “+ counting” to “min + weight”

------------------------------------------------------------------------

## ✅ 11. Takeaway

- dp[i][j] = dp[i-1][j] + dp[i][j-1] with dp[0][0] = 1 is the canonical solution.
- 1D rolling array yields O(n) space.
- Combinatorics gives O(1) space and O(min(m, n)) time, ideal for large dimensions.

------------------------------------------------------------------------

# End of Notes
