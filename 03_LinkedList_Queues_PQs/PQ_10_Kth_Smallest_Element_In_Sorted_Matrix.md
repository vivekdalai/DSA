# Priority Queue Notes

## 10 - Kth Smallest Element in a Sorted Matrix

Problem: [LeetCode 378 - Kth Smallest Element in a Sorted Matrix](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/description/)

---

## 1. Problem, In One Line

Given an `n x n` matrix where every row and every column is sorted ascending, return the `k`-th smallest element overall (1-indexed).

**Example**

    Input:  matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8
    Output: 13

    Input:  matrix = [[-5]], k = 1
    Output: -5

---

## 2. Core Idea

Same k-way merge shape as `PQ_03_Merge_K_Sorted_Arrays.md` — treat each **row** as one of the `k` sorted lists being merged. Because both rows and columns are sorted, the next-smallest element overall is always the smallest of the current "front" elements across all rows, so a min-heap of row-fronts does the job in `O(log n)` per step instead of scanning all rows.

Pop the smallest `k` times; the `k`-th pop is the answer. No need to fully merge the matrix — stop as soon as `k` pops have happened.

---

## 3. What Goes In The Heap

A min-heap of `{value, row, col}` triples, ordered by `value` ascending — same 3-tuple shape as `PQ_03`'s array-merge heap entry, just with `row`/`col` standing in for `arrayIndex`/`elementIndex`.

Seed it with the **first column** — one entry per row, since that's every row's smallest element.

---

## 4. The Algorithm, Step By Step

1. Push `{matrix[i][0], i, 0}` for every row `i` — `n` initial entries.
2. Repeat `k` times:
   - Pop the smallest entry; that popped `value` becomes the running answer.
   - If that row has a next column (`col + 1` is still in bounds), push `{matrix[row][col+1], row, col+1}`.
3. After the `k`-th pop, return the running answer.

---

## 5. Code

```java
import java.util.*;

class KthSmallest {
    public static int kthSmallestElement(int[][] matrix, int k) {
        int n = matrix.length;
        int m = matrix[0].length;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0])); // {value, row, col}

        for (int i = 0; i < n; i++) {
            pq.offer(new int[]{matrix[i][0], i, 0});
        }

        int result = 0;
        for (int i = 0; i < k; i++) {
            int[] top = pq.poll();
            result = top[0];
            int row = top[1];
            int col = top[2];
            if (col < m - 1) {
                pq.offer(new int[]{matrix[row][col + 1], row, col + 1});
            }
        }
        return result;
    }
}
```

Complexity:
- Time: `O((n + k) log n)` — `n` initial pushes, then `k` pop/push rounds, each `O(log n)` since the heap never holds more than `n` entries (one per row).
- Space: `O(n)` for the heap.

Verified against a brute-force reference (flatten + sort + index) across 20,000 randomized square matrices, plus both official examples — zero mismatches.

---

## 6. Dry Run

Input: `matrix = [[1,5,9],[10,11,13],[12,13,15]], k = 8`

Initial heap (one per row): `(1,0,0)`, `(10,1,0)`, `(12,2,0)`

| Pop | `result` | Pushed next |
|---|---|---|
| `(1,0,0)` | `1` | `(5,0,1)` |
| `(5,0,1)` | `5` | `(9,0,2)` |
| `(9,0,2)` | `9` | row 0 exhausted |
| `(10,1,0)` | `10` | `(11,1,1)` |
| `(11,1,1)` | `11` | `(13,1,2)` |
| `(12,2,0)` | `12` | `(13,2,1)` |
| `(13,1,2)` | `13` | row 1 exhausted |
| `(13,2,1)` | `13` | `(15,2,2)` |

8th pop: `13` — matches expected output.

---

## 7. Edge Cases and Pitfalls

- **The submitted code originally bounded column advancement with `col < (n - 1)` instead of `col < (m - 1)`** — using the *row* count (`n`) to decide whether a *column* index (`col`) can advance. For LeetCode 378 specifically this happens to be harmless, because the problem guarantees the matrix is always `n x n` (square), so `n - 1` and `m - 1` are the same number. Verified this is a real latent bug, not a style nitpick: feeding the exact same logic a non-square sorted matrix (3 rows × 4 columns) crashes with `NullPointerException` — some rows stop pushing new columns too early, the heap runs dry before `k` pops complete, and `pq.poll()` returns `null`. If you ever reuse this k-way-merge-over-a-matrix pattern for a variant that isn't guaranteed square (e.g. "kth smallest in a sorted `m x n` matrix"), this bug reappears and won't be caught by testing against LC378's own examples, since those are all square. Always bound `col` by the actual column count, not the row count, even when they currently happen to be equal.
- `k` is guaranteed `1 <= k <= n*n` by the problem, so the loop always completes without needing an empty-heap guard — but that guarantee is exactly what the bug above silently violates once the matrix isn't square.
- `08_BinarySearch/BS_10_Kth_Smallest_Element_In_Sorted_Matrix.md` covers the same problem with a different pattern: binary search over the *value range* using a per-guess counting step, `O(n log(max-min))`, no heap at all. This note's heap approach is the more natural fit for the "k-way merge" mental model and generalizes better to "kth smallest across k sorted lists" style variants; the binary-search version generalizes better when `k` is huge relative to `n` (its cost doesn't depend on `k` at all).

---

## 8. Complexity And Pattern

Pattern:

- k-way merge over a matrix's rows — identical shape to `PQ_03_Merge_K_Sorted_Arrays.md`, with rows playing the role of the `k` sorted arrays.
- Early-stop merge: no need to fully merge all `n^2` elements, since only the first `k` pops are ever needed — this is what keeps it `O((n+k) log n)` instead of `O(n^2 log n)`.
- General lesson (see Edge Cases): whenever a bound is expressed as "one dimension's size," double-check it's actually bounding *that* dimension, not a different one that currently happens to share the same value.

Trigger words:
- "sorted matrix" combined with "kth smallest" or "kth largest"
- Rows and columns both sorted ascending (the property that makes the k-way-merge framing valid at all)

---

## End of Notes
