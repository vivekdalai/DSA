# Binary Search Notes

## 16 - Search a 2D Matrix

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/search-a-2d-matrix/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given an `m x n` matrix where:

- each row is sorted
- the first element of each row is greater than the last element of the previous row

Return whether `target` exists.

Example:

```text
Input:
matrix = [
  [1,3,5,7],
  [10,11,16,20],
  [23,30,34,60]
]
target = 3

Output: true
```

------------------------------------------------------------------------

## 2. First Intuition

Because every row starts after the previous row ends, the entire matrix behaves like one sorted 1D array.

So we can binary search from index `0` to `m * n - 1`.

------------------------------------------------------------------------

## 3. Methodology

Convert a virtual 1D index into matrix coordinates:

```text
row = index / cols
col = index % cols
```

Then run normal binary search.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public boolean searchMatrix(int[][] matrix, int target) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int low = 0;
        int high = rows * cols - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int row = mid / cols;
            int col = mid % cols;
            int value = matrix[row][col];

            if (value == target) {
                return true;
            } else if (value < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return false;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `target = 16`:

Virtual sorted array is:

```text
[1,3,5,7,10,11,16,20,23,30,34,60]
```

Binary search finds `16` at virtual index `6`.

```text
row = 6 / 4 = 1
col = 6 % 4 = 2
```

So it maps to `matrix[1][2]`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(log(m * n))`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Use virtual indexing when a 2D structure is globally sorted.
- This differs from row-wise and column-wise sorted matrix problems.
- The clean trick is `row = mid / cols`, `col = mid % cols`.

------------------------------------------------------------------------

## End of Notes
