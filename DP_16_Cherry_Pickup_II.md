# Dynamic Programming Notes

## 16 - Cherry Pickup II (Two Robots from Top Row to Bottom Row)

**Generated on:** 2026-02-24 20:37:59 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given an `N x M` grid:
- Non-negative numbers represent the number of cherries in that cell.
- Some platforms may use `-1` as an obstacle (blocked cell). If obstacles are present, robots cannot step on them.

Two robots start at the top row:
- Robot A at `(0, 0)`
- Robot B at `(0, M - 1)`

At each step (i.e., moving to the next row), both robots move simultaneously from row `r` to `r + 1` with possible column moves:
- For each robot independently: `col' ∈ {col - 1, col, col + 1}` (stay, left, or right) while moving down one row.

If both robots land on the same cell in a step, the cherries are counted once.

Goal: Maximize the total number of cherries collected when both robots reach the bottom row (row `N - 1`).

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[row][y1][y2]` = maximum cherries collectible starting from
  - current row = `row`,
  - robot A at column `y1`,
  - robot B at column `y2`,
  - moving both robots row by row down to `N - 1`.

Answer:
- `dp[0][0][M - 1]` (or the memoized DFS result from `(0, 0, M-1)`)

Base intuition:
- At any row, collect `grid[row][y1] + (y1 == y2 ? 0 : grid[row][y2])`
- Then pick the best of the 9 transitions to the next row.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For a given `(row, y1, y2)`:
- If out-of-bounds or any blocked cell (if obstacles exist) → invalid branch (use `-INF` sentinel)
- If `row == N - 1` (last row):
  - Gain = `grid[row][y1] + (y1 == y2 ? 0 : grid[row][y2])`
- Otherwise:
  - Try all moves `d1 ∈ {-1, 0, +1}`, `d2 ∈ {-1, 0, +1}`:
    - `ny1 = y1 + d1`, `ny2 = y2 + d2` (both move to `row + 1`)
    - Candidate = `gainHere + dp[row + 1][ny1][ny2]`
  - `dp[row][y1][y2] = max over 9 candidates`

Where:
- `gainHere = grid[row][y1] + (y1 == y2 ? 0 : grid[row][y2])`

------------------------------------------------------------------------

## 🧱 4. Base and Invalid Cases

- If `y1 < 0 || y1 >= M || y2 < 0 || y2 >= M` → invalid
- If obstacles are represented (e.g., `-1`) and `grid[row][y1] == -1` or `grid[row][y2] == -1` → invalid
- At `row == N - 1` → return `gainHere`

Use a large negative sentinel `NEG_INF` for invalid branches and ensure you never add raw `Integer.MIN_VALUE`.

------------------------------------------------------------------------

## 💻 5A. Clean Interview Version — Memoized DFS (Top-Down)

```java
import java.util.*;

class CherryPickupII_Memo {
    private int[][] grid;
    private Integer[][][] dp; // dp[row][y1][y2]
    private int N, M;
    private static final int NEG_INF = -1_000_000_000;

    public int cherryPickup(int[][] grid) {
        this.grid = grid;
        this.N = grid.length;
        this.M = grid[0].length;
        this.dp = new Integer[N][M][M];

        int ans = dfs(0, 0, M - 1);
        return Math.max(0, ans);
    }

    private int dfs(int row, int y1, int y2) {
        // out-of-bounds
        if (y1 < 0 || y1 >= M || y2 < 0 || y2 >= M) return NEG_INF;
        // obstacles (if your platform uses -1 for blocked cells)
        if (grid[row][y1] == -1 || grid[row][y2] == -1) return NEG_INF;

        if (dp[row][y1][y2] != null) return dp[row][y1][y2];

        // current gain
        int gain = grid[row][y1];
        if (y1 != y2) gain += grid[row][y2];

        // base: last row
        if (row == N - 1) return dp[row][y1][y2] = gain;

        int best = NEG_INF;
        for (int d1 = -1; d1 <= 1; d1++) {
            for (int d2 = -1; d2 <= 1; d2++) {
                int ny1 = y1 + d1;
                int ny2 = y2 + d2;
                int next = dfs(row + 1, ny1, ny2);
                if (next != NEG_INF) {
                    best = Math.max(best, next);
                }
            }
        }
        if (best == NEG_INF) return dp[row][y1][y2] = NEG_INF;
        return dp[row][y1][y2] = gain + best;
    }
}
```

Complexity:
- Time: `O(N * M * M * 9)` in worst-case → `O(N * M^2)`
- Space: `O(N * M^2)` for memo + recursion depth `O(N)`

------------------------------------------------------------------------

## 💻 5B. Bottom-Up Tabulation (Full 3D)

```java
class CherryPickupII_Tab {
    public int cherryPickup(int[][] grid) {
        int N = grid.length, M = grid[0].length;
        int NEG_INF = -1_000_000_000;

        int[][][] dp = new int[N][M][M];

        // Initialize last row
        for (int y1 = 0; y1 < M; y1++) {
            for (int y2 = 0; y2 < M; y2++) {
                if (grid[N - 1][y1] == -1 || grid[N - 1][y2] == -1) {
                    dp[N - 1][y1][y2] = NEG_INF;
                } else {
                    int gain = grid[N - 1][y1];
                    if (y1 != y2) gain += grid[N - 1][y2];
                    dp[N - 1][y1][y2] = gain;
                }
            }
        }

        // Fill from second-last row up to row 0
        for (int row = N - 2; row >= 0; row--) {
            for (int y1 = 0; y1 < M; y1++) {
                for (int y2 = 0; y2 < M; y2++) {
                    if (grid[row][y1] == -1 || grid[row][y2] == -1) {
                        dp[row][y1][y2] = NEG_INF;
                        continue;
                    }
                    int gain = grid[row][y1];
                    if (y1 != y2) gain += grid[row][y2];

                    int best = NEG_INF;
                    for (int d1 = -1; d1 <= 1; d1++) {
                        for (int d2 = -1; d2 <= 1; d2++) {
                            int ny1 = y1 + d1, ny2 = y2 + d2;
                            if (ny1 >= 0 && ny1 < M && ny2 >= 0 && ny2 < M) {
                                best = Math.max(best, dp[row + 1][ny1][ny2]);
                            }
                        }
                    }
                    dp[row][y1][y2] = (best == NEG_INF) ? NEG_INF : gain + best;
                }
            }
        }

        int ans = dp[0][0][M - 1];
        return Math.max(0, ans);
    }
}
```

Complexity:
- Time: `O(N * M * M * 9)`
- Space: `O(N * M * M)`

------------------------------------------------------------------------

## 💻 5C. Space-Optimized Tabulation (2 Layers over Rows)

```java
class CherryPickupII_Space {
    public int cherryPickup(int[][] grid) {
        int N = grid.length, M = grid[0].length;
        int NEG_INF = -1_000_000_000;

        int[][] prev = new int[M][M];
        // init last row
        for (int y1 = 0; y1 < M; y1++) {
            for (int y2 = 0; y2 < M; y2++) {
                if (grid[N - 1][y1] == -1 || grid[N - 1][y2] == -1) {
                    prev[y1][y2] = NEG_INF;
                } else {
                    int gain = grid[N - 1][y1];
                    if (y1 != y2) gain += grid[N - 1][y2];
                    prev[y1][y2] = gain;
                }
            }
        }

        for (int row = N - 2; row >= 0; row--) {
            int[][] curr = new int[M][M];
            for (int y1 = 0; y1 < M; y1++) {
                for (int y2 = 0; y2 < M; y2++) {
                    if (grid[row][y1] == -1 || grid[row][y2] == -1) {
                        curr[y1][y2] = NEG_INF;
                        continue;
                    }
                    int gain = grid[row][y1];
                    if (y1 != y2) gain += grid[row][y2];

                    int best = NEG_INF;
                    for (int d1 = -1; d1 <= 1; d1++) {
                        for (int d2 = -1; d2 <= 1; d2++) {
                            int ny1 = y1 + d1, ny2 = y2 + d2;
                            if (ny1 >= 0 && ny1 < M && ny2 >= 0 && ny2 < M) {
                                best = Math.max(best, prev[ny1][ny2]);
                            }
                        }
                    }
                    curr[y1][y2] = (best == NEG_INF) ? NEG_INF : gain + best;
                }
            }
            prev = curr;
        }

        int ans = prev[0][M - 1];
        return Math.max(0, ans);
    }
}
```

Complexity:
- Time: `O(N * M * M * 9)`
- Space: `O(M * M)` (rolling layers)

------------------------------------------------------------------------

## 🔎 6. Dry Run Example

Grid (no obstacles):
```
grid = [
  [3, 1, 1],
  [2, 5, 1],
  [1, 5, 5],
  [2, 1, 1]
]
```
N = 4, M = 3

Intuition:
- Robot A starts at (0,0), Robot B at (0,2)
- Optimal total = 24
One optimal set of moves:
- Row 0: A→0, B→2 → gain 3 + 1 = 4
- Row 1: A→1 (0→1), B→1 (2→1) → gain 5 (count once) + 2 (A at col 0 previous doesn't matter now) → effectively best path merges over high cells
- The DP enumerates all pairs (y1,y2) and picks the maximum.

The tabulation or memoized version computes this systematically and returns 24.

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: Two-Agents Row-by-Row DP (3D state)
- Family: Grid DP with synchronized transitions
- Triggers:
  - Two agents moving down the grid
  - At each row, both choose among 3 horizontal offsets
  - Joint state is `(row, y1, y2)`; 9 transitions per state

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- Obstacles: return invalid for any cell with `-1`
- Same cell overlap: add once only
- Beware of `Integer.MIN_VALUE` overflow; use safer sentinel like `-1e9`
- Clamp final result with `max(0, ans)` if your platform expects non-negative answer when no valid path exists
- M can be as small as 1; handle y1==y2 naturally

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Use `dp[row][y1][y2]` with 9 transitions per state.
- Memoized DFS is concise and interview-friendly.
- For performance and memory, prefer space-optimized bottom-up using two `M x M` layers.

------------------------------------------------------------------------

# End of Notes
