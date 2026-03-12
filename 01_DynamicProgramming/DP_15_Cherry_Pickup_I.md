# Dynamic Programming Notes

## 15 - Cherry Pickup I (Two Persons from (0,0) to (n-1,n-1))

**Generated on:** 2026-02-24 19:27:43 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given an `n x n` grid with:
- `1` representing a cherry,
- `0` representing an empty cell,
- `-1` representing a thorn/obstacle (not passable).

Two persons start at `(0,0)` and both need to reach `(n-1,n-1)` by moving only:
- Right: `(r, c) → (r, c+1)`
- Down:  `(r, c) → (r+1, c)`

They move simultaneously (step-by-step).  
If both step on the same cell in a step, the cherry in that cell can be collected only once.

Goal: Maximize the total number of cherries collected by coordinating both persons’ paths.

Key Reduction:
- This is equivalent to sending two persons from `(0,0)` to `(n-1,n-1)` at the same time.  
- At step `t`, both persons have taken exactly `t` moves, hence:
  - If person1 at `(r1, c1)` and person2 at `(r2, c2)`, then `r1 + c1 = r2 + c2 = t`.
  - Therefore, `c2 = (r1 + c1) - r2`.

We can represent the joint state as `(r1, c1, r2)` since `c2` is derived.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[r1][c1][r2]` = maximum cherries collectible when
  - Person1 is at `(r1, c1)`,
  - Person2 is at `(r2, c2)` with `c2 = r1 + c1 - r2`,
  - and both move to `(n-1, n-1)` optimally from here.

Answer:
- `max(0, dp[0][0][0])` (non-negative; if no valid path, return 0)

Grid constraints:
- If any position is out-of-bounds or is a thorn (`-1`), that branch is invalid.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

From `(r1, c1, r2)` with `c2 = r1 + c1 - r2`, the next joint step is one of four move combinations:
- `(r1+1, c1,   r2+1)`   → (down, down)
- `(r1+1, c1,   r2)`     → (down, right)
- `(r1,   c1+1, r2+1)`   → (right, down)
- `(r1,   c1+1, r2)`     → (right, right)

Collect at current cells:
- If `(r1, c1) == (r2, c2)`: add `grid[r1][c1]` once.
- Else: add `grid[r1][c1] + grid[r2][c2]`.

Transition:
- `dp[r1][c1][r2] = cherriesHere + max( nextStates )`
- Invalid next states contribute `-INF` (a large negative sentinel) so they don’t get picked.

Base case:
- If both at destination `(n-1, n-1)`, return `grid[n-1][n-1]` (1 or 0).

------------------------------------------------------------------------

## 🧱 4. Base and Invalid Cases

- Out of bounds → invalid → return `-INF`
- Thorn at any of the two positions → invalid → return `-INF`
- Destination reached for both → return cell value (1/0)

Finally clamp with `max(0, result)`.

------------------------------------------------------------------------

## 📦 5. Why 3D → 2D Reduction?

Naively thinking, two independent paths (go and return) need visited-state handling.  
The simultaneous two-person model ensures:
- Each time step is synchronized → same `t = r + c`
- Avoids modifying the grid and avoids double-counting when both land on the same cell by checking `(r1,c1)==(r2,c2)`

This converts the problem into a 3D DP over `(r1, c1, r2)` (with `c2` derived).

------------------------------------------------------------------------

## 💻 6A. Clean Interview Version — Memoized DFS (Top-Down)

```java
import java.util.*;

class CherryPickup {
    private int[][] grid;
    private Integer[][][] dp;
    private int n;
    private static final int NEG_INF = -1_000_000_000;

    public int cherryPickup(int[][] grid) {
        this.grid = grid;
        this.n = grid.length;
        this.dp = new Integer[n][n][n]; // r1, c1, r2 (c2 inferred)
        int ans = dfs(0, 0, 0);
        return Math.max(0, ans);
    }

    private int dfs(int r1, int c1, int r2) {
        int c2 = r1 + c1 - r2; // invariant: r1 + c1 == r2 + c2

        // bounds / obstacle checks
        if (r1 < 0 || c1 < 0 || r2 < 0 || c2 < 0 ||
            r1 >= n || c1 >= n || r2 >= n || c2 >= n) {
            return NEG_INF;
        }
        if (grid[r1][c1] == -1 || grid[r2][c2] == -1) {
            return NEG_INF;
        }

        // destination
        if (r1 == n - 1 && c1 == n - 1) {
            // (r2,c2) must also be (n-1,n-1) due to invariant
            return grid[r1][c1];
        }

        if (dp[r1][c1][r2] != null) return dp[r1][c1][r2];

        int cherriesHere;
        if (r1 == r2 && c1 == c2) {
            cherriesHere = grid[r1][c1]; // same cell → count once
        } else {
            cherriesHere = grid[r1][c1] + grid[r2][c2];
        }

        // 4 move combinations
        int dd = dfs(r1 + 1, c1,     r2 + 1); // down, down
        int dr = dfs(r1 + 1, c1,     r2    ); // down, right
        int rd = dfs(r1,     c1 + 1, r2 + 1); // right, down
        int rr = dfs(r1,     c1 + 1, r2    ); // right, right

        int bestNext = Math.max(Math.max(dd, dr), Math.max(rd, rr));
        return dp[r1][c1][r2] = cherriesHere + bestNext;
    }
}
```

Complexity:
- States: `O(n^3)` (r1 ∈ [0..n-1], c1 ∈ [0..n-1], r2 ∈ [0..n-1])
- Transitions: 4 per state
- Time: `O(4 * n^3)` in worst case (memo reduces it effectively)
- Space: `O(n^3)` for dp + recursion depth `O(n+n)` → `O(n)`

------------------------------------------------------------------------

## 💻 6B. PDF-Style Variant (Same Logic, Comment Emphasis)

```java
class CherryPickupPDF {
    private Integer[][][] dp;
    private int[][] grid;
    private int n;
    private static final int NEG_INF = Integer.MIN_VALUE / 4; // safe sentinel

    public int cherryPickup(int[][] grid) {
        this.grid = grid;
        this.n = grid.length;
        this.dp = new Integer[n][n][n];
        int ans = dfs(0, 0, 0);
        return Math.max(0, ans); // ensure non-negative
    }

    private int dfs(int r1, int c1, int r2) {
        int c2 = r1 + c1 - r2; // r1 + c1 == r2 + c2 always

        // invalid bounds
        if (r1 >= n || c1 >= n || r2 >= n || c2 >= n) return NEG_INF;
        // obstacle check
        if (grid[r1][c1] == -1 || grid[r2][c2] == -1) return NEG_INF;

        // destination
        if (r1 == n - 1 && c1 == n - 1) {
            // by invariant, r2,c2 also == n-1,n-1
            return grid[r1][c1];
        }

        if (dp[r1][c1][r2] != null) return dp[r1][c1][r2];

        int cherries = grid[r1][c1];
        if (!(r1 == r2 && c1 == c2)) {
            cherries += grid[r2][c2]; // avoid double count
        }

        // explore 4 possibilities
        int res = Math.max(
            Math.max(dfs(r1 + 1, c1,     r2 + 1), dfs(r1 + 1, c1,     r2    )),
            Math.max(dfs(r1,     c1 + 1, r2 + 1), dfs(r1,     c1 + 1, r2    ))
        );

        if (res == NEG_INF) return dp[r1][c1][r2] = NEG_INF;
        return dp[r1][c1][r2] = cherries + res;
    }
}
```

Notes:
- Same as 6A; highlights the invariant and the need to avoid double counting.

------------------------------------------------------------------------

## 🔎 7. Dry Run Example

Grid:
```
[
  [0, 1, -1],
  [1, 0, -1],
  [1, 1,  1]
]
```

Intuition:
- Optimal is to collect as many 1s as possible without hitting `-1`.
- The best coordinated routes yield `5`.

Why:
- One path can go (0,0)→(1,0)→(2,0)→(2,1)→(2,2)
- Other path can go (0,0)→(0,1)→(1,1)→(2,1)→(2,2)
- Cells overlapped are counted once; total cherries = 5.

The DP ensures invalid branches (touching -1 or going out-of-bounds) are discarded via `NEG_INF`.

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Two-Agents Synchronized DP (3D DP)
- Family: Grid DP with combined state and invariant
- Triggers:
  - Two agents moving in a grid with shared resources (cherries)
  - Movement constraints in lockstep (same number of moves)
  - Avoiding double-count of shared cells

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- Start or End blocked by `-1` → immediate 0
- Ensure the invariant `r1 + c1 == r2 + c2` is used to derive `c2`
- Do not double-count when `(r1,c1)==(r2,c2)`
- Use a safe negative sentinel (`NEG_INF`) and not raw `Integer.MIN_VALUE` in additions
- Return `max(0, result)` as per problem spec when all paths invalid

------------------------------------------------------------------------

## 🔁 10. Variants

- Cherry Pickup II (two robots start at `(0,0)` and `(0, m-1)` and move downwards) → different state `(row, col1, col2)`
- Weighted cells (values other than 0/1) → same DP works
- Count number of optimal paths in addition to max cherries → maintain an extra dp for counts (tie-handling)

------------------------------------------------------------------------

## ✅ 11. Takeaway

- Convert the “go and return” idea into a simultaneous two-person DP with a 3D state.
- Key invariant: `r1 + c1 = r2 + c2`.
- Memoized DFS is the most succinct and commonly-accepted approach.

------------------------------------------------------------------------

# End of Notes
