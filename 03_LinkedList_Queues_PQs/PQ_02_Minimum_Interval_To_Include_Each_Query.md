# Priority Queue Notes

## 02 - Minimum Interval to Include Each Query

Problem: LeetCode 1851 - Minimum Interval to Include Each Query

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/minimum-interval-to-include-each-query/description/
<!-- leetcode-link-end -->

---

## 1. LeetCode Question Statement

You are given a 2D array `intervals`, where `intervals[i] = [left_i, right_i]` describes the `i`th interval starting at `left_i` and ending at `right_i` (inclusive). The size of an interval is `right_i - left_i + 1`.

You are also given an integer array `queries`. The answer to the `j`th query is the size of the **smallest** interval `i` such that `left_i <= queries[j] <= right_i`. If no such interval exists, the answer is `-1`.

Return an array containing the answers to the queries.

**Example**

    Input: intervals = [[1,4],[2,4],[3,6],[4,4]], queries = [2,3,4,5]
    Output: [3,3,1,4]

Explanation:
- Query `2` -> smallest containing interval is `[2,4]`, size `3`
- Query `3` -> smallest containing interval is `[2,4]`, size `3`
- Query `4` -> smallest containing interval is `[4,4]`, size `1`
- Query `5` -> smallest containing interval is `[3,6]`, size `4`

---

## 2. Approach

Brute force checks every interval per query -> `O(n * q)`, too slow for large inputs.

Instead, use **sort + sweep + min-heap**:

1. Sort `intervals` by start (`left`) ascending.
2. Sort the **indices** of `queries` by their value ascending (not the values themselves, since the answer must be placed back at the original query position).
3. Sweep through queries in increasing order. For each query `q`:
   - Push every interval whose `left <= q` into a min-heap, keyed by `(size, end)`. Once an interval's start is small enough for the current (sorted, increasing) query, it stays a valid candidate for all remaining queries too, so each interval is pushed at most once — the `i` pointer only moves forward.
   - Pop from the heap any interval whose `end < q` — it can never contain this query or any larger future query, so it is safe to discard permanently.
   - The top of the heap (if any) is now the smallest interval that contains `q`.
4. Because indices were sorted separately, write each answer back to `result[originalIndex]`.

This works because both the interval pointer and the heap-eviction check only move forward as `q` increases — classic two-pointer + heap sweep.

---

## 3. Code

```java
import java.util.*;

public class Solution {
    public int[] minInterval(int[][] intervals, int[] query) {
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0])); // sort by start time

        int qLen = query.length;
        Integer[] indices = new Integer[qLen];
        for (int i = 0; i < qLen; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, (a, b) -> Integer.compare(query[a], query[b])); // sort query indices by value asc

        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        int[] result = new int[qLen];
        int i = 0; // pointer into sorted intervals

        for (int idx : indices) {
            int q = query[idx];

            // add all intervals that can contain the query
            while (i < intervals.length && intervals[i][0] <= q) {
                int left = intervals[i][0], right = intervals[i][1];
                heap.offer(new int[]{right - left + 1, right}); // insert (size_of_interval, end_of_interval) to heap
                i++;
            }

            // remove intervals that end before this query
            while (!heap.isEmpty() && heap.peek()[1] < q) {
                heap.poll(); //evict already passed intervals which cannot be the answer!
            }

            result[idx] = heap.isEmpty() ? -1 : heap.peek()[0];
        }

        return result;
    }
}
```

---

## 4. Dry Run

Input:

    intervals = [[1,4],[2,4],[3,6],[4,4]]
    queries   = [2,3,4,5]

Sorted intervals by start (already sorted here):

    [1,4], [2,4], [3,6], [4,4]

Sorted query indices by value (values already ascending, so indices stay `[0,1,2,3]`):

    query[0]=2, query[1]=3, query[2]=4, query[3]=5

Heap entries are shown as `(size, end)`.

| q | Pushed (start <= q) | Heap after push | Evicted (end < q) | Heap after evict | Top -> answer |
|---|---|---|---|---|---|
| 2 | `[1,4]`->(4,4), `[2,4]`->(3,4) | `(3,4),(4,4)` | none | `(3,4),(4,4)` | `(3,4)` -> **3** |
| 3 | `[3,6]`->(4,6) | `(3,4),(4,4),(4,6)` | none | `(3,4),(4,4),(4,6)` | `(3,4)` -> **3** |
| 4 | `[4,4]`->(1,4) | `(1,4),(3,4),(4,4),(4,6)` | none | `(1,4),(3,4),(4,4),(4,6)` | `(1,4)` -> **1** |
| 5 | none (all intervals already pushed) | `(1,4),(3,4),(4,4),(4,6)` | `(1,4)`, `(3,4)`, `(4,4)` all have `end=4 < 5` | `(4,6)` | `(4,6)` -> **4** |

Final result, written back via `result[idx]`:

    [3, 3, 1, 4]

which matches the expected output.

Note how at `q=5` nothing new gets pushed (the interval pointer `i` already reached the end of the sorted intervals), and three stale entries get popped in one shot before the true top `(4,6)` surfaces — this is the "monotonic sweep" doing its job.

---

## 5. Why Sort Query Indices (Not Values)

The answer array must line up with the **original** query order, but the sweep needs queries processed in **increasing value** order so the interval pointer `i` and heap-eviction only move forward.

Sorting `Integer[] indices` by `query[a]` keeps a mapping back to the original position (`idx`), so `result[idx] = ...` writes the answer to the right slot even though queries were visited out of order.

---

## 6. Complexity

- **Time:** `O((n + q) log n)` — sorting intervals and queries is `O(n log n)` and `O(q log q)`; each interval is pushed/popped from the heap at most once, `O(n log n)`; each query does at most one heap peek plus amortized pops, `O(q log n)`.
- **Space:** `O(n + q)` for the heap and the indices array.

Pattern:

- offline queries: sort queries, answer in sorted order, map back via original indices
- min-heap keyed by `(size, end)` to always expose the smallest *currently valid* interval
- monotonic sweep: pointer into sorted intervals and heap evictions both move forward only

---

## End of Notes
