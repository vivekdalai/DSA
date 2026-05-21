# Binary Search Notes

## 10 - Kth Smallest Element in Sorted Matrix

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given an `n x n` matrix where every row and every column is sorted in ascending order, return the `k`th smallest element.

Example:

```text
Input:
matrix = [
  [1,  5,  9],
  [10,11,13],
  [12,13,15]
]
k = 8

Output: 13
```

------------------------------------------------------------------------

## 2. First Intuition

The answer must lie between:

- smallest value: `matrix[0][0]`
- largest value: `matrix[n - 1][n - 1]`

For any guessed value `x`, count how many matrix elements are `<= x`.

If count is less than `k`, the answer is bigger.
Otherwise, `x` may be the answer or the answer may be smaller.

------------------------------------------------------------------------

## 3. Methodology

Binary search on value range.

For counting elements `<= target`, use the sorted row and column property:

- start from bottom-left
- if current value is `<= target`, all values above it in that column are also `<= target`
- add `row + 1` to count and move right
- else move up

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int kthSmallest(int[][] matrix, int k) {
        int n = matrix.length;
        int low = matrix[0][0];
        int high = matrix[n - 1][n - 1];

        while (low < high) {
            int mid = low + (high - low) / 2;
            int count = countLessEqual(matrix, mid);

            if (count < k) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        return low;
    }

    private int countLessEqual(int[][] matrix, int target) {
        int n = matrix.length;
        int row = n - 1;
        int col = 0;
        int count = 0;

        while (row >= 0 && col < n) {
            if (matrix[row][col] <= target) {
                count += row + 1;
                col++;
            } else {
                row--;
            }
        }

        return count;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For:

```text
[
  [1,  5,  9],
  [10,11,13],
  [12,13,15]
]
k = 8
```

The 8th smallest value is `13`.

When guessed value is too small, fewer than 8 elements are `<= guess`, so move right.
When guessed value is at least `13`, count reaches 8 or more, so move left.

Final answer is `13`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n log(valueRange))`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This is binary search on value.
- The matrix is sorted by rows and columns, so bottom-left counting is `O(n)`.
- Same count-based idea is useful for median of sorted matrix.

------------------------------------------------------------------------

## End of Notes
