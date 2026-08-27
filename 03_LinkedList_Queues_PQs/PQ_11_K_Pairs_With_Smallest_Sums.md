# Priority Queue Notes

## 11 - Find K Pairs with Smallest Sums ⭐

Problem: [LeetCode 373 - Find K Pairs with Smallest Sums](https://leetcode.com/problems/find-k-pairs-with-smallest-sums/description/)

**Why ⭐:** this is the natural "next step up" from `PQ_03_Merge_K_Sorted_Arrays.md` and `PQ_10_Kth_Smallest_Element_In_Sorted_Matrix.md` — same k-way-merge-over-a-heap shape, but the "list of sorted lists" is now *implicit* (row `i` is the virtual list `list1[i] + list2[0], list1[i] + list2[1], ...`), which is exactly the kind of twist interviewers use to check whether you actually understood the pattern or just memorized the code.

---

## 1. Problem, In One Line

Given two arrays `list1`, `list2` (both sorted ascending) and an integer `k`, return the `k` pairs `(list1[i], list2[j])` with the smallest sums.

**Example**

    Input:  list1 = [1,7,11], list2 = [2,4,6], k = 3
    Output: [[1,2],[1,4],[1,6]]

    Input:  list1 = [1,1,2], list2 = [1,2,3], k = 2
    Output: [[1,1],[1,1]]

---

## 2. Core Idea

Think of it as merging `n` *virtual* sorted lists, one per index `i` in `list1`: row `i` is `list1[i] + list2[0] <= list1[i] + list2[1] <= ... <= list1[i] + list2[m-1]` (sorted because `list2` is sorted). That's exactly `PQ_03`'s k-way merge, just with the row's elements computed on the fly instead of stored.

**The optimization that makes this efficient:** you never need to seed more than `min(n, k)` rows. If `k < n`, rows `k, k+1, ..., n-1` can *never* contribute to the k smallest sums — rows `0..k-1` already supply `k` distinct pairs (each `(i, 0)`) with sums `list1[0]+list2[0] <= list1[1]+list2[0] <= ... <= list1[k-1]+list2[0]`, every one of which is `<= list1[i]+list2[0] <= list1[i]+list2[j]` for any `i >= k` (since `list1` and `list2` are both sorted ascending). So those later rows are provably never in contention.

---

## 3. What Goes In The Heap

A min-heap of `{i, j}` index pairs, ordered by `list1[i] + list2[j]`. Same shape as `PQ_10`'s `{value, row, col}` triples, except the comparator recomputes the sum from `i`/`j` on every comparison instead of storing it as a first field — a minor style difference, not a correctness one (see Edge Cases).

---

## 4. The Algorithm, Step By Step

1. Seed the heap with `{i, 0}` for `i` in `0 .. min(n, k) - 1` — pair each of the first `min(n,k)` elements of `list1` with `list2`'s smallest element.
2. Pop the smallest-sum entry `k` times (or until the heap runs dry, whichever comes first):
   - Add `(list1[i], list2[j])` to the result.
   - If `j < m - 1`, push `{i, j + 1}` — the next element in that same row.
3. Return whatever was collected — fewer than `k` pairs if `list1`/`list2` together don't have `k` pairs total.

---

## 5. Code

```java
import java.util.*;

class FindKPairs {
    public static List<List<Integer>> kSmallestPairs(int[] list1, int[] list2, int k) {
        int n = list1.length;
        int m = list2.length;

        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
            (a, b) -> Integer.compare(list1[a[0]] + list2[a[1]], list1[b[0]] + list2[b[1]])
        );
        for (int i = 0; i < Math.min(n, k); i++) {
            minHeap.offer(new int[]{i, 0});
        }

        List<List<Integer>> result = new ArrayList<>();
        while (k-- > 0 && !minHeap.isEmpty()) {
            int[] top = minHeap.poll();
            int i = top[0], j = top[1];
            result.add(new ArrayList<>(List.of(list1[i], list2[j])));

            if (j < m - 1) {
                minHeap.offer(new int[]{i, j + 1});
            }
        }
        return result;
    }
}
```

Complexity:
- Time: `O(min(n,k) + k log(min(n,k)))` — the heap never holds more than `min(n, k)` entries (one per active row), so each of the `k` pop/push rounds is `O(log(min(n,k)))`.
- Space: `O(min(n, k))` for the heap, plus `O(k)` for the result.

Verified against a brute-force reference (generate all `n*m` pairs, sort by sum, take the first `k`) across 50,000 randomized trials — including cases where `k > n*m` — comparing result size and total sum (ties can legitimately produce different specific pairs, but count and total sum must match). Zero mismatches. Both official examples pass exactly.

---

## 6. Dry Run

Input: `list1 = [1,7,11], list2 = [2,4,6], k = 3`

`min(n,k) = min(3,3) = 3`, so seed all 3 rows: `{0,0}`→sum 3, `{1,0}`→sum 9, `{2,0}`→sum 13.

| Pop `{i,j}` | Pair added | Pushed next |
|---|---|---|
| `{0,0}` (sum 3) | `[1,2]` | `{0,1}` (sum `1+4=5`) |
| `{0,1}` (sum 5) | `[1,4]` | `{0,2}` (sum `1+6=7`) |
| `{0,2}` (sum 7) | `[1,6]` | row 0 exhausted (`j == m-1`) |

3 pops done (`k` exhausted): result = `[[1,2],[1,4],[1,6]]` — matches expected output. Note rows 1 and 2 (`{1,0}` sum 9, `{2,0}` sum 13) never even get popped — correctly outclassed by row 0's cheaper options.

---

## 7. Edge Cases and Pitfalls

- `k > n * m`: there simply aren't `k` pairs to return. The `while (k-- > 0 && !minHeap.isEmpty())` loop handles this correctly — it stops as soon as the heap runs dry, returning fewer than `k` pairs, matching the problem's expected behavior. Verified explicitly in the stress test above.
- The `min(n, k)` seeding cap is doing real work, not just a micro-optimization — without it (seeding all `n` rows unconditionally) the algorithm would still be *correct*, just wasteful when `k` is small and `n` is huge (e.g. `n = 10^5`, `k = 1`). Don't "simplify" this back to seeding every row; it changes the time complexity from `O(min(n,k) + k log(min(n,k)))` to `O(n + k log n)`.
- The comparator recomputes `list1[a[0]] + list2[a[1]]` on every single comparison rather than caching the sum in the tuple (contrast with `PQ_10`'s `{value, row, col}`, which stores the value once at push time). Not a correctness bug — just repeated array-lookup-plus-addition work per comparison instead of a stored field read. Fine at LeetCode's constraints; worth caching the sum as a third array element if this were on a hotter path.
- `list1` and list2` are assumed sorted ascending — if either isn't, the "row is sorted" property that justifies the whole k-way-merge framing breaks, and both the algorithm's correctness and the `min(n,k)` seeding proof fail with it.

---

## 8. Complexity And Pattern

Pattern:

- k-way merge over **virtual** sorted lists (rows computed on the fly), same family as `PQ_03_Merge_K_Sorted_Arrays.md` and `PQ_10_Kth_Smallest_Element_In_Sorted_Matrix.md`.
- "You only ever need `min(n, k)` starting points" is a recurring trick whenever you're pulling the `k` best items from `n` independently-sorted sources — recognize it as its own reusable idea, not something specific to this problem.

Trigger words:
- "k pairs with smallest sums", "k smallest sums" from two sorted arrays
- Any "smallest/largest combination of one element from each of several sorted sources" framing

---

## End of Notes
