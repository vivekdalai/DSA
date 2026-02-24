# Dynamic Programming Notes

## 13 - Triangle Minimum Path Sum

**Generated on:** 2026-02-24 19:21:15 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a triangle (jagged) array `triangle` of size `n`, where row `i` has `i+1` entries:  
Start at the top `(0,0)`. At each step, you may move to:
- directly below `(i+1, j)`, or
- down-right `(i+1, j+1)`

Goal: Find the minimum path sum from top to any element in the last row, i.e., to reach row `n-1`.

This is a classic DP on a triangular grid.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[i][j]` = minimum path sum from cell `(i, j)` to the bottom.

Answer:
- `dp[0][0]`

Alternate (top-down):
- `dp[i][j]` = minimum path sum to reach `(i, j)` from top.  
Answer: `min_j dp[n-1][j]` (but bottom-up from last row is more natural for triangle).

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

From position `(i, j)` you can go to:
- `(i+1, j)` or `(i+1, j+1)`

Thus:
- `dp[i][j] = triangle[i][j] + min(dp[i+1][j], dp[i+1][j+1])`

Boundary:
- Last row is the base: `dp[n-1][j] = triangle[n-1][j]`

------------------------------------------------------------------------

## 🧱 4. Base Cases

- For bottom-up formulation:
  - Initialize `dp[n-1][j] = triangle[n-1][j]` for all `j ∈ [0..n-1]`
- For top-down recursion:
  - When `i == n-1`, return `triangle[i][j]`

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

We only need the next row to compute the current row.  
Use a 1D array `prev` representing `dp[i+1][*]`, and compute `curr` for row `i`.  
Finally `prev[0]` is the answer.

Space reduces to O(n).

------------------------------------------------------------------------

## 💻 6A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class TriangleMinPathMemo {
    private int[][] tri;
    private Integer[][] memo;
    private int n;

    public int minimumTotal(List<List<Integer>> triangle) {
        n = triangle.size();
        // convert to array for faster index access
        tri = new int[n][n];
        for (int i = 0; i < n; i++) {
            List<Integer> row = triangle.get(i);
            for (int j = 0; j <= i; j++) tri[i][j] = row.get(j);
        }
        memo = new Integer[n][n];
        return dfs(0, 0);
    }

    private int dfs(int i, int j) {
        if (i == n - 1) return tri[i][j];
        if (memo[i][j] != null) return memo[i][j];

        int down = dfs(i + 1, j);
        int diag = dfs(i + 1, j + 1);
        return memo[i][j] = tri[i][j] + Math.min(down, diag);
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(n^2) memo + O(n) recursion depth

------------------------------------------------------------------------

## 💻 6B. Iterative Tabulation (Bottom-Up)

```java
class TriangleMinPathTab {
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] dp = new int[n][n];

        // base: last row
        for (int j = 0; j < n; j++) {
            dp[n - 1][j] = triangle.get(n - 1).get(j);
        }

        // from second-last row up to top
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                int down = dp[i + 1][j];
                int diag = dp[i + 1][j + 1];
                dp[i][j] = triangle.get(i).get(j) + Math.min(down, diag);
            }
        }
        return dp[0][0];
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(n^2)

------------------------------------------------------------------------

## 💻 6C. Space-Optimized Bottom-Up (O(n))

```java
import java.util.*;

class TriangleMinPathSpace {
    public int minimumTotal(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[] prev = new int[n];

        // initialize with last row
        for (int j = 0; j < n; j++) {
            prev[j] = triangle.get(n - 1).get(j);
        }

        // build upwards
        for (int i = n - 2; i >= 0; i--) {
            int[] curr = new int[i + 1];
            for (int j = 0; j <= i; j++) {
                int down = prev[j];
                int diag = prev[j + 1];
                curr[j] = triangle.get(i).get(j) + Math.min(down, diag);
            }
            prev = curr;
        }
        return prev[0];
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(n)

------------------------------------------------------------------------

## 💡 6D. (Optional) Reconstruct One Minimum Path

```java
import java.util.*;

class TriangleMinPathWithPath {
    public List<Integer> minimumPath(List<List<Integer>> triangle) {
        int n = triangle.size();
        int[][] dp = new int[n][n];
        int[][] nextIdx = new int[n][n]; // store choice: j or j+1

        // base
        for (int j = 0; j < n; j++) dp[n - 1][j] = triangle.get(n - 1).get(j);

        // fill
        for (int i = n - 2; i >= 0; i--) {
            for (int j = 0; j <= i; j++) {
                int down = dp[i + 1][j];
                int diag = dp[i + 1][j + 1];
                if (down <= diag) {
                    dp[i][j] = triangle.get(i).get(j) + down;
                    nextIdx[i][j] = j;
                } else {
                    dp[i][j] = triangle.get(i).get(j) + diag;
                    nextIdx[i][j] = j + 1;
                }
            }
        }

        // reconstruct path from (0,0)
        List<Integer> path = new ArrayList<>();
        int i = 0, j = 0;
        path.add(triangle.get(0).get(0));
        while (i < n - 1) {
            j = nextIdx[i][j];
            i++;
            path.add(triangle.get(i).get(j));
        }
        return path;
    }
}
```

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Triangle:
```
[
  [2],
  [3, 4],
  [6, 5, 7],
  [4, 1, 8, 3]
]
```

Bottom row → prev = [4, 1, 8, 3]

Row 2:
- j=0: 6 + min(4,1) = 7
- j=1: 5 + min(1,8) = 6
- j=2: 7 + min(8,3) = 10
prev = [7, 6, 10]

Row 1:
- j=0: 3 + min(7,6) = 9
- j=1: 4 + min(6,10)= 10
prev = [9, 10]

Row 0:
- j=0: 2 + min(9,10) = 11

Answer: 11

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Triangle DP (Down/Down-Right transitions)
- Family: 2D DP on triangular grid
- Triggers:
  - Each row has one more element than the previous
  - From (i,j) you can go to (i+1,j) and (i+1,j+1)
  - Min or Max aggregation along the path

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- Triangle with a single row → answer is that single value
- Don’t return 0 for out-of-bounds in recursion; use proper base at last row (or INF if you structure differently)
- Prefer bottom-up with O(n) space for clean, non-recursive solution
- If values can be large, ensure no overflow (int is fine for typical constraints)

------------------------------------------------------------------------

## 🔁 10. Variants

- Maximum path sum in a triangle → replace `min` with `max`
- Count number of minimum-sum paths → maintain an extra count dp with tie-handling
- Path reconstruction (shown above)

------------------------------------------------------------------------

## ✅ 11. Takeaway

- The most succinct approach is bottom-up from the last row with O(n) space.
- For interviews: present the space-optimized DP; mention memoized recursion as an alternative.

------------------------------------------------------------------------

# End of Notes
