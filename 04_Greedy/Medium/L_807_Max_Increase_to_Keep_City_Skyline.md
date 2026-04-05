# Greedy Notes

## Max Increase to Keep City Skyline

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/max-increase-to-keep-city-skyline/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given an `n x n` grid of building heights.

You may increase the height of any building, but the city skyline viewed from the top, bottom, left, and right must remain unchanged.

Return the maximum total sum by which the buildings can be increased.

**Example 1**

    Input: grid = [[3,0,8,4],[2,4,5,7],[9,2,6,3],[0,3,1,0]]
    Output: 35

**Example 2**

    Input: grid = [[1,2],[3,4]]
    Output: 1


<!-- leetcode-images-start -->
**Official LeetCode Image**

![LeetCode image 1](https://assets.leetcode.com/uploads/2021/06/21/807-ex1.png)

<!-- leetcode-images-end -->
------------------------------------------------------------------------

## 2. Row/Column Cap Intuition

For cell `(i, j)`, the tallest it can become is limited by:

- the maximum in row `i`
- the maximum in column `j`

So its new value can be at most:

    min(rowMax[i], colMax[j])

Anything larger would change at least one skyline.

------------------------------------------------------------------------

## 3. Two Passes

First pass:

- compute every row maximum
- compute every column maximum

Second pass:

- for each cell, add:

    min(rowMax[i], colMax[j]) - grid[i][j]

That total is the maximum safe increase.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    grid = [[3,0,8,4],[2,4,5,7],[9,2,6,3],[0,3,1,0]]

Example cell `(0,1)`:

- row max = `8`
- col max = `4`

So the highest safe value is:

    min(8, 4) = 4

Current value is `0`, so it can increase by `4`.

Do this for every cell and sum the increases.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maxIncreaseKeepingSkyline(int[][] grid) {
    int n = grid.length;
    int[] row = new int[n];
    int[] col = new int[n];

    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            row[i] = Math.max(row[i], grid[i][j]);
            col[j] = Math.max(col[j], grid[i][j]);
        }
    }

    int ans = 0;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            ans += Math.min(row[i], col[j]) - grid[i][j];
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n^2)`
- **Space:** `O(n)`

Pattern:

- precompute row and column caps
- each cell is independently bounded by the tighter cap

------------------------------------------------------------------------

## End of Notes
