# Priority Queue Notes

## 07 - Find Median From Data Stream

Problem: [LeetCode 295 - Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/description/)

---

## 1. Problem, In One Line

Design a structure that supports adding numbers one at a time (`insertNum`) and, at any point, reporting the median of every number seen so far (`findMedian`) — without re-sorting from scratch on every query.

**Example**

    insertNum(5)  -> findMedian() = 5.0
    insertNum(15) -> findMedian() = 10.0   // (5+15)/2
    insertNum(1)  -> findMedian() = 5.0    // sorted: 1,5,15
    insertNum(3)  -> findMedian() = 4.0    // sorted: 1,3,5,15 -> (3+5)/2

---

## 2. Core Idea

Keep the numbers seen so far split into two halves:

- `maxHeap` — the **smaller** half, ordered so the largest of the small half is on top.
- `minHeap` — the **larger** half, ordered so the smallest of the large half is on top.

If both halves are kept the same size (or `maxHeap` has exactly one extra element), the median is always sitting at the top of one or both heaps — no scan, no sort, `O(1)` to read.

- Odd total count → `maxHeap` carries the extra element → median = `maxHeap.peek()`.
- Even total count → median = average of `maxHeap.peek()` and `minHeap.peek()`.

This is the running-median counterpart to `PQ_03_Merge_K_Sorted_Arrays.md`'s "heap gives you the next-best value in `O(log k)`" idea — here the heap gives you the **middle** value instead of the smallest.

---

## 3. What Goes In The Heaps

- `maxHeap` (`PriorityQueue<Integer>` with `Collections.reverseOrder()`) — holds every number that belongs to the lower half, largest on top.
- `minHeap` (plain `PriorityQueue<Integer>`) — holds every number that belongs to the upper half, smallest on top.

Invariant maintained after every insert:
1. Every element in `maxHeap` is `<=` every element in `minHeap`.
2. `maxHeap.size() == minHeap.size()` or `maxHeap.size() == minHeap.size() + 1` (never the other way around).

---

## 4. The Algorithm, Step By Step

**`insertNum(num)`**
1. Decide which half `num` belongs in: if `maxHeap` is empty or `num <= maxHeap.peek()`, it belongs in the smaller half → push to `maxHeap`. Otherwise push to `minHeap`.
2. Rebalance so the size invariant holds:
   - If `maxHeap` now has more than one extra element, move its top to `minHeap`.
   - If `minHeap` somehow ends up larger than `maxHeap`, move its top to `maxHeap`.

**`findMedian()`**
1. If nothing's been inserted, `maxHeap` is empty — return `0.0` (or handle as undefined, depending on the spec).
2. If `maxHeap` is strictly larger, return its top as a `double`.
3. Otherwise the two heaps are equal size — return the average of both tops.

---

## 5. Code

```java
import java.util.*;

class MedianOfStream {
    PriorityQueue<Integer> minHeap; // larger half, min at top
    PriorityQueue<Integer> maxHeap; // smaller half, max at top

    public MedianOfStream() {
        minHeap = new PriorityQueue<>();
        maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    }

    public void insertNum(int num) {
        if (maxHeap.isEmpty() || num <= maxHeap.peek()) {
            maxHeap.offer(num);
        } else {
            minHeap.offer(num);
        }
        
        //rebalance
        if (maxHeap.size() > minHeap.size() + 1) {
            minHeap.offer(maxHeap.poll());
        } else if (minHeap.size() > maxHeap.size()) {
            maxHeap.offer(minHeap.poll());
        }
    }

    public double findMedian() {
        if (maxHeap.isEmpty()) {
            return 0.0;
        }
        if (maxHeap.size() > minHeap.size()) {
            return 1.0 * maxHeap.peek();
        } else {
            return (maxHeap.peek() + minHeap.peek()) / 2.0;
        }
    }
}
```

Complexity:
- Time: `O(log n)` per `insertNum` (heap push/pop), `O(1)` per `findMedian`.
- Space: `O(n)` total, across both heaps.

---

## 6. Dry Run

Insert `5, 15, 1, 3` in order.

| Insert | Goes to | Rebalance | `maxHeap` (small half) | `minHeap` (large half) | `findMedian()` |
|---|---|---|---|---|---|
| 5 | `maxHeap` empty → maxHeap | sizes 1/0, no rebalance needed | `[5]` | `[]` | `5.0` |
| 15 | `15 > maxHeap.peek()(5)` → minHeap | sizes 1/1, ok | `[5]` | `[15]` | `(5+15)/2 = 10.0` |
| 1 | `1 <= 5` → maxHeap | maxHeap now size 2, minHeap 1 → within +1, ok | `[5,1]` | `[15]` | maxHeap bigger → `5.0` |
| 3 | `3 <= maxHeap.peek()(5)` → maxHeap | maxHeap size 3 > minHeap(1)+1 → move maxHeap top (5) to minHeap | `[3,1]` | `[5,15]` | equal sizes → `(3+5)/2 = 4.0` |

Matches the worked example in Section 1 exactly.

---

## 7. Edge Cases and Pitfalls

- `num <= maxHeap.peek()`, not `<` — ties must route to `maxHeap` for the size-invariant math in `findMedian` to stay consistent; routing ties to `minHeap` instead still works but only if you're consistent about which comparison direction you picked.
- The rebalance step is an `if / else if`, not two independent `if`s — a single insert can only ever violate the balance by at most one element, so at most one of the two branches should ever fire per call.
- `maxHeap` is deliberately the one allowed to hold the "extra" element on odd counts — this determines which heap `findMedian` reads from first; swapping the convention is fine as long as insert and read agree with each other.
- `findMedian()` on an empty stream: this implementation returns `0.0` rather than throwing — know your interviewer's expected contract here (some variants expect `NaN`, an exception, or a documented precondition that `findMedian` is only called after at least one insert).
- Integer overflow: `maxHeap.peek() + minHeap.peek()` is safe for normal `int` ranges here since the sum is immediately promoted to `double` via `/ 2.0`, but if the values could be near `Integer.MAX_VALUE`, sum as `long` first to be safe.

---

## 8. Complexity And Pattern

Pattern:

- two-heap "split the stream down the middle" — max-heap for the lower half, min-heap for the upper half, kept balanced within one element of each other.
- `O(log n)` insert / `O(1)` query is the whole point — the naive "insert into a sorted list" approach is `O(n)` per insert, which this beats decisively for large streams.
- Same "keep the boundary between two ordered halves under a size constraint" flavor as k-th largest/smallest streaming problems, just centered on the middle instead of an edge.

Trigger words:
- "running median", "median of a data stream", "design a data structure that supports adding numbers and finding the median"

---

## End of Notes
