# Binary Search Notes

## 09 - Median of Row-Wise Sorted Matrix

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

## 1. Problem Understanding

Given a row-wise sorted matrix with odd total elements, return the median.

Each row is sorted, but the whole matrix is not necessarily sorted globally.

Example:

```text
Input:
matrix = [
  [1, 3, 5],
  [2, 6, 9],
  [3, 6, 9]
]

Output: 5
```

------------------------------------------------------------------------

## 2. First Intuition

The median is the smallest value such that at least half of the matrix elements are less than or equal to it.

Since rows are sorted, for any guessed value `x`, we can count how many elements are `<= x` using binary search in each row.

That count increases monotonically as `x` increases, so binary search on value works.

------------------------------------------------------------------------

## 3. Methodology

Search range:

- `low = minimum first element among all rows`
- `high = maximum last element among all rows`

Required count:

```text
required = (rows * cols) / 2 + 1
```

For a value `mid`:

- count how many elements are `<= mid`
- if count is smaller than required, median is larger
- else `mid` may be the answer, move left

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int matrixMedian(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int low = Integer.MAX_VALUE;
        int high = Integer.MIN_VALUE;

        for (int r = 0; r < rows; r++) {
            low = Math.min(low, matrix[r][0]);
            high = Math.max(high, matrix[r][cols - 1]);
        }

        int required = (rows * cols) / 2 + 1;

        while (low < high) {
            int mid = low + (high - low) / 2;
            int count = countLessEqual(matrix, mid);

            if (count < required) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }

    private int countLessEqual(int[][] matrix, int target) {
        int count = 0;

        for (int[] row : matrix) {
            count += upperBound(row, target);
        }

        return count;
    }

    private int upperBound(int[] row, int target) {
        int low = 0;
        int high = row.length;

        while (low < high) {
            int mid = low + (high - low) / 2;

            if (row[mid] <= target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For:

```text
[
  [1, 3, 5],
  [2, 6, 9],
  [3, 6, 9]
]
```

There are 9 elements, so median is the 5th smallest.

- Count elements `<= 5`: `5`, so `5` can be the answer.
- Smaller values do not have enough elements before them.
- Answer is `5`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(rows * log(cols) * log(valueRange))`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This is binary search on value, not index.
- The helper is upper bound: count of elements `<= target`.
- Same idea appears in kth-smallest and median problems.

------------------------------------------------------------------------

## End of Notes
