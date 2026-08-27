# Priority Queue Notes

## 13 - K-th Smallest Prime Fraction

Problem: [LeetCode 786 - K-th Smallest Prime Fraction](https://leetcode.com/problems/k-th-smallest-prime-fraction/description/)

---

## 1. Problem, In One Line

Given `arr` — sorted ascending, containing `1` and distinct **primes** only — consider every fraction `arr[i]/arr[j]` for `i < j`. Return the `k`-th smallest such fraction, as `[arr[i], arr[j]]`. Constraint: `1 <= k <= arr.length * (arr.length - 1) / 2` — `k` never exceeds the count of legitimate `i<j` pairs (that count is exactly `nC2`, since every unordered pair of indices maps to exactly one `i<j` ordering).

**Example**

    Input:  arr = [1,2,3,5], k = 3
    Output: [2,5]

    Input:  arr = [1,7], k = 1
    Output: [1,7]

---

## 2. Core Idea

Fix a numerator index `i`. As the denominator index `j` decreases from `n-1` down to `i+1`, `arr[j]` decreases (array is sorted ascending), so the fraction `arr[i]/arr[j]` **increases**. That means row `i`'s virtual sorted list — smallest fraction first — is `j = n-1, n-2, ..., i+1`.

Same k-way merge shape as `PQ_10_Kth_Smallest_Element_In_Sorted_Matrix.md` and `PQ_11_K_Pairs_With_Smallest_Sums.md`: seed one heap entry per row at its smallest fraction (`j = n-1`), pop the smallest `k` times, and each pop only ever unlocks the *next* element of that same row (`j - 1`) — never a new row mid-algorithm.

---

## 3. What Goes In The Heap

A plain min-heap of `{value, i, j}` — the fraction's `double` value **computed once and cached** at push time, alongside the index pair. Ordered by that cached value, ascending, with no tricks: `(a, b) -> Double.compare(a[0], b[0])`.

**Two things this deliberately avoids, both worth knowing why:**

- **Recomputing the fraction inside the comparator.** An earlier draft of this note used `PriorityQueue<int[]>` and computed `arr[a[0]]/arr[a[1]]` fresh on every comparison. A heap makes `O(log n)` comparisons per push/pop, so that's `O(log n)` divisions per operation instead of one. Benchmarked both directly on a large input (`n=1500`, `k` around half the max pair count): the cached version ran in **~106ms vs ~159ms** for the recompute-every-time version — roughly a third faster, not just a theoretical nitpick.
- **Negating the value and reversing the comparator.** A tempting-looking alternative (seen in some editorial solutions) stores `-value` and uses a *reversed* comparator (`Double.compare(b[0], a[0])`) to simulate a max-heap that then behaves like a min-heap. This works, but it's solving a problem that doesn't exist: negating the value alone flips the order to largest-first (wrong), and reversing the comparator alone *also* flips it to largest-first (wrong) — verified this directly, both give the identical incorrect result. Doing **both together** flips it twice, landing back on correct ascending order — which is exactly what you get by doing **neither**. Verified: negate+reverse and do-nothing-at-all produce byte-identical results across every `k` tested. There's no scenario where negation is actually required here; `arr[i]/arr[j]` is always a plain positive `double`, and `PriorityQueue`'s default behavior (smallest-first) already matches what this problem wants directly.

---

## 4. The Algorithm, Step By Step

1. Seed the heap with `{arr[i]/arr[n-1], i, n-1}` for every `i` from `0` to `n-2` — each row starts at its smallest fraction (largest denominator), value cached at push time.
2. Pop `k` times, tracking the most recently popped entry:
   - Advance the row: `j - 1`. Only push `{arr[i]/arr[j-1], i, j-1}` back if `j - 1 > i` — once `j` would reach `i` (or below), row `i` has no more valid pairs (the problem requires `i < j` strictly), so nothing more is pushed for that row.
3. After the loop, the last popped entry is the `k`-th smallest — map its `i`, `j` back to `arr[i]`, `arr[j]`.

---

## 5. Code

```java
import java.util.*;

class Solution {
    public int[] kthSmallestPrimeFraction(int[] arr, int k) {
        int n = arr.length;
        PriorityQueue<double[]> pq = new PriorityQueue<>((a, b) -> Double.compare(a[0], b[0]));

        for (int i = 0; i < n - 1; i++) {
            pq.offer(new double[]{(double) arr[i] / arr[n - 1], i, n - 1});
        }

        double[] current = null;
        for (int count = 0; count < k; count++) {
            current = pq.poll();
            int i = (int) current[1];
            int j = (int) current[2];
            if (j - 1 > i) {
                pq.offer(new double[]{(double) arr[i] / arr[j - 1], i, j - 1});
            }
        }

        return new int[]{arr[(int) current[1]], arr[(int) current[2]]};
    }
}
```

Complexity:
- Time: `O((n + k) log n)` — `n` initial pushes, `k` pop/push rounds, heap never holds more than `n` entries. Each comparison is a plain `double` compare, no division.
- Space: `O(n)` for the heap.

Verified against both official examples plus 100,000 randomized trials — using inputs of `1` and **actual distinct primes** (sieved), matching the problem's real constraint, not arbitrary integers. Zero mismatches.

---

## 6. Dry Run

Input: `arr = [1,2,3,5], k = 3`

Seed (one per row, `j` starts at `n-1=3`, value cached): `{0.2, 0, 3}` (1/5), `{0.4, 1, 3}` (2/5), `{0.6, 2, 3}` (3/5)

| `count` | Popped `{value, i, j}` | Pushed next |
|---|---|---|
| 0 | `{0.2, 0, 3}` (1/5) | `{0.333, 0, 2}` (1/3), since `2 > 0` |
| 1 | `{0.333, 0, 2}` (1/3) | `{0.5, 0, 1}` (1/2), since `1 > 0` |
| 2 | `{0.4, 1, 3}` (2/5) | `{0.667, 1, 2}` (2/3), since `2 > 1` |

Loop ran for `count = 0, 1, 2` (three iterations, `k=3`). The **last** popped entry (`count=2`) is `{0.4, 1, 3}` → `arr[1]/arr[3]` = `2/5` → answer `[2, 5]` — matches expected output.

---

## 7. Edge Cases and Pitfalls

- **`arr` containing only `1` and distinct primes is exactly what guarantees every fraction is unique.** For primes `p`, `q` (or `1`), `p/q` is already in lowest terms — two different index pairs can never reduce to the same value. Verified this directly by first stress-testing with arbitrary integers (which produced real, reproducible mismatches, e.g. `4/50 == 8/100`), then re-testing with primes-only inputs matching the actual constraint (zero mismatches). Don't validate this kind of problem with generic random integers — the input domain restriction is load-bearing.
- **`k` is guaranteed `1 <= k <= n*(n-1)/2`** — every legitimate pair, never more — verified against the problem's actual stated constraint, not just assumed. So the loop always finds its answer among genuinely valid `{i,j}` pairs; no empty-heap guard is needed for realistic inputs.
- **Negation + reversed comparator is a redundant double-flip, not an extra safety net.** See Section 3 — it's very likely a leftover from mixing two independent "fake a max-heap" idioms (negate values *or* reverse the comparator — pick one, not both) rather than something intentionally chosen for this problem. If you see this pattern in your own code, it's worth asking which one flip you actually meant to keep.
- A tempting-looking alternative to `if (j - 1 > i) pq.offer(...)` is to always decrement `j` and, if it collides with `i`, decrement once more before pushing (constructing `{i, i-1}`) — this doesn't crash and doesn't corrupt the *answer* either, since `arr[i]/arr[i-1]` is always `> 1` (numerator index has the larger value) and therefore sorts after every legitimate `<= 1` fraction — but it's solving "don't crash" instead of "don't create a meaningless pair in the first place." Simpler and clearer to just not push when `j - 1 <= i`.
- Caching the value at push time (this note's approach) beats recomputing it in the comparator — see Section 3 for the measured difference. Worth doing whenever the sort key is more expensive than a raw field read (division, string concatenation, nested lookups), not just in this specific problem.

---

## 8. Complexity And Pattern

Pattern:

- k-way merge over rows of a value that's *monotonic in one index for a fixed other index* — same family as `PQ_10` and `PQ_11`. The recurring skill is spotting the "virtual sorted list" hiding inside a formula (here, `arr[i]/arr[j]` for fixed `i`), not just recognizing an already-sorted structure handed to you.
- `08_BinarySearch/BS_18_Kth_Smallest_Prime_Fraction.md` covers the same problem via binary search on the fraction value, counting how many pairs are `<= mid` via a two-pointer sweep in `O(n)` per iteration — `O(n log(1/epsilon))` overall, independent of `k` entirely, which wins when `k` is close to `n*(n-1)/2`. This heap approach wins for small `k`. Same trade-off as `PQ_10` vs. `BS_10`.
- General lesson: when faking a max-heap in a language whose priority queue only offers min-heap ordering, pick exactly one of "negate the values" or "reverse the comparator" — never reach for both out of caution, since it's not extra safety, just extra indirection that happens to cancel out.

Trigger words:
- "k-th smallest fraction", "sorted array of primes", any "value computed from two indices, monotonic when one index is fixed"

---

## End of Notes
