# Dynamic Programming Notes

## 58 - Dungeon Game (LC 174) — Backward Grid DP

**Generated on:** 2026-08-03 01:35:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

A knight starts at the top-left of an `m x n` dungeon grid and must rescue a
princess at the bottom-right, moving only right or down. Each cell has an integer
value: negative means health lost (demon), positive means health gained (potion),
zero means empty. The knight's health must **stay above 0 at every step of the
path**, not just at the end. Find the minimum initial health required to guarantee
a path exists that keeps health positive throughout.

This is the classic "reverse" grid DP — the twist that makes it non-obvious is that
you cannot simply compute "best cumulative sum so far" forward from the top-left,
because a locally great prefix sum can still dip below 1 mid-path in a way that a
forward-only greedy or DP can't detect cleanly.

------------------------------------------------------------------------

## 🪜 2. State Definition

- `dp[i][j]` = the **minimum health required upon entering** cell `(i, j)` such
  that the knight can survive (health `>= 1` at every cell) all the way to the
  princess from `(i, j)` onward.

Goal: `dp[0][0]` — the minimum health needed at the very start.

Key mental shift: `dp[i][j]` is not "best achievable health after visiting up to
`(i,j)`" (that's the forward framing that fails). It's "requirement to survive the
*rest* of the journey from here" — which is why the table must be filled starting
from the destination, backward.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

Fill from the bottom-right corner backward to the top-left:

    needFromNext = min(dp[i+1][j], dp[i][j+1])   // best of the two paths onward
    dp[i][j] = max(1, needFromNext - dungeon[i][j])

Meaning: whatever health is required entering the *next* cell, subtract what this
cell gives/costs (`dungeon[i][j]`) to find what's required entering *this* cell —
but health can never be allowed to be computed as `<= 0`, so floor it at `1`.

------------------------------------------------------------------------

## 🧱 4. Base Cases and Initialization

- `dp[m-1][n-1] = max(1, 1 - dungeon[m-1][n-1])` — surviving the princess's cell
  itself requires ending that step with at least 1 health.
- Cells beyond the grid boundary (used implicitly by the last row/column, which only
  have one direction to look) are treated as `+infinity` so `min(...)` correctly
  picks the one valid neighbor.

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

Each `dp[i][j]` only depends on `dp[i+1][j]` and `dp[i][j+1]` — the row below and
the column to the right. A single rolling 1D array (processed right-to-left,
bottom-to-top) is enough, dropping space from O(m*n) to O(n).

------------------------------------------------------------------------

## 💻 6A. Bottom-Up 2D (Canonical, Fill Backward)

```java
class Solution {
    public int calculateMinimumHP(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        int[][] dp = new int[m + 1][n + 1];
        for (int[] row : dp) java.util.Arrays.fill(row, Integer.MAX_VALUE);

        // sentinels so the two cells adjacent to the princess resolve correctly
        dp[m][n - 1] = 1;
        dp[m - 1][n] = 1;

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int needFromNext = Math.min(dp[i + 1][j], dp[i][j + 1]);
                dp[i][j] = Math.max(1, needFromNext - dungeon[i][j]);
            }
        }
        return dp[0][0];
    }
}
```

Complexity: Time O(m*n), Space O(m*n)

------------------------------------------------------------------------

## 💻 6B. Space-Optimized 1D Rolling Row

```java
import java.util.*;

class SolutionOptimized {
    public int calculateMinimumHP(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        int[] dp = new int[n + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[n - 1] = 1;

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int down = dp[j];             // dp[i+1][j] before this row overwrites it
                int right = (j + 1 <= n - 1) ? dp[j + 1] : Integer.MAX_VALUE;
                int needFromNext = Math.min(down, right);
                dp[j] = Math.max(1, (needFromNext == Integer.MAX_VALUE)
                        ? 1 - dungeon[i][j] : needFromNext - dungeon[i][j]);
            }
        }
        return dp[0];
    }
}
```

Complexity: Time O(m*n), Space O(n)

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input:
```
dungeon = [
  [-2, -3,  3],
  [-5, -10, 1],
  [10, 30, -5]
]
```

Fill backward from `(2,2)`:
- `dp[2][2] = max(1, 1 - (-5)) = 6`
- `dp[2][1] = max(1, dp[2][2] - 30) = max(1, 6-30) = 1`
- `dp[2][0] = max(1, dp[2][1] - 10) = max(1, 1-10) = 1`
- `dp[1][2] = max(1, dp[2][2] - 1) = max(1, 5) = 5`
- `dp[1][1] = max(1, min(dp[2][1], dp[1][2]) - (-10)) = max(1, min(1,5)+10) = 11`
- `dp[1][0] = max(1, min(dp[2][0], dp[1][1]) - (-5)) = max(1, min(1,11)+5) = 6`
- `dp[0][2] = max(1, dp[1][2] - 3) = max(1, 5-3) = 2`
- `dp[0][1] = max(1, min(dp[1][1], dp[0][2]) - (-3)) = max(1, min(11,2)+3) = 5`
- `dp[0][0] = max(1, min(dp[1][0], dp[0][1]) - (-2)) = max(1, min(6,5)+2) = 7`

Answer: `dp[0][0] = 7` (matches the well-known LC174 sample output).

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Backward/Reverse Grid DP — "survivability threshold" DP
- Family: grid path DP, but with the fill direction flipped relative to
  [Min Path Sum](DP_12_Min_Path_Sum_Grid.md) / [Unique Paths](DP_10_Unique_Paths.md)
  because the constraint ("never drop below a threshold *during* the path, not just
  at the end") makes forward accumulation unsound.
- Trigger to recognize this pattern: whenever a problem says a running quantity
  "must never drop below X at any point," define the state as "requirement to enter
  this cell/step," and fill from the end of the path backward — not "best value
  achieved so far" filled forward.

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

Edge Cases:
- Single-cell grid (`1x1`) → `dp[0][0] = max(1, 1 - dungeon[0][0])`
- All-positive dungeon → still need at least `1` health entering the final cell;
  answer is never `0` or negative
- Last row / last column → only one direction (`right` or `down`) to look back from;
  handle via the `+infinity` sentinel so `min()` picks the valid neighbor

Pitfalls:
- Trying to solve this forward with "max cumulative health so far" — this is the
  single most common wrong first instinct; walk through why it fails (a path can
  have a great total but dip to 0 mid-way) before coding
- Forgetting the `max(1, ...)` floor at every cell, not just the base case
- Sentinel initialization mistakes: only `dp[m][n-1]` and `dp[m-1][n]` should be `1`
  (adjacent to the princess cell); everything else needs `+infinity` so it never
  wins a `min()` incorrectly

------------------------------------------------------------------------

## ✅ 10. Takeaway

- State = "minimum health required to **enter** this cell," not "best health
  achieved up to this cell" — that inversion is the entire problem.
- Because of that inversion, the table must be filled backward from the destination.
- `dp[i][j] = max(1, min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j])` — memorize this
  one line; everything else is boundary bookkeeping.

------------------------------------------------------------------------

# End of Notes
