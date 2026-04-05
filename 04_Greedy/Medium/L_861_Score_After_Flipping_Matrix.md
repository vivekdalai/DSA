# Greedy Notes

## Score After Flipping Matrix

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given a binary matrix `grid`.

You may flip any row or column any number of times. Flipping changes `0` to `1` and `1` to `0`.

Each row is interpreted as a binary number.

Return the maximum possible total score of all rows.

**Example 1**

    Input: grid = [[0,0,1,1],[1,0,1,0],[1,1,0,0]]
    Output: 39

**Example 2**

    Input: grid = [[0]]
    Output: 1

------------------------------------------------------------------------

## 2. Most Important Bit First

The first column has the largest binary weight.

So in any optimal solution, every first-column bit should be `1`.

The file uses this fact implicitly:

- it assumes each row is flipped if needed so column `0` becomes all `1`s

------------------------------------------------------------------------

## 3. Column-by-Column Greedy

After fixing the first column logically, handle each remaining column independently.

For column `j`:

- count how many `1`s it would have after the implied row flips
- decide whether flipping that column gives more `1`s

If a column can have `maxOnes` ones, its contribution is:

    maxOnes * 2^(m-1-j)

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    grid = [[0,0,1,1],[1,0,1,0],[1,1,0,0]]

Force first column to all `1`s by flipping the first row.

Then check each later column and keep whichever orientation gives more `1`s.

The final maximum score becomes:

    39

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int matrixScore(int[][] grid) {
    int n = grid.length;
    int m = grid[0].length;
    int score = n * (1 << (m - 1));

    for (int j = 1; j < m; j++) {
        int countOnes = 0;

        for (int i = 0; i < n; i++) {
            if (grid[i][0] == 1) countOnes += grid[i][j];
            else countOnes += 1 - grid[i][j];
        }

        int maxOnes = Math.max(countOnes, n - countOnes);
        score += maxOnes * (1 << (m - 1 - j));
    }

    return score;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(nm)`
- **Space:** `O(1)`

Pattern:

- maximize the most significant bit first
- then optimize each column independently

------------------------------------------------------------------------

## End of Notes
