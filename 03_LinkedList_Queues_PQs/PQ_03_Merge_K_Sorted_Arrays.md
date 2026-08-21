# Priority Queue Notes

## 03 - Merge K Sorted Arrays

Classic pattern problem (GfG / interview staple). LeetCode's version of this idea is [23 - Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/), same trick, linked-list nodes instead of array indices.

---

## 1. Problem, In One Line

You're given `k` arrays, each already sorted ascending. Produce one fully sorted array containing all their elements.

**Example**

    Input:  arrays = [[1,4,7], [2,5], [3,6,8,9]]
    Output: [1,2,3,4,5,6,7,8,9]

---

## 2. Segment 1 — The Core Idea

At any point, the next-smallest overall element must be the smallest *front* element among the `k` arrays — because every array is sorted, nothing behind an array's front pointer can be smaller than it.

So: repeatedly find the smallest of the `k` current front elements, output it, advance that one array's pointer, repeat.

A **min-heap of size `k`** does "find the smallest of k things" in `O(log k)` instead of `O(k)` per step.

---

## 3. Segment 2 — What Goes In The Heap

Not the value alone — you also need to know *which array* it came from and *where*, so you can advance to that array's next element after popping it.

Each heap entry is a 3-tuple:

```java
{ value, arrayIndex, elementIndex }
```

- `value` — what the heap compares on (min-heap by value).
- `arrayIndex` — which of the `k` arrays this came from.
- `elementIndex` — position inside that array, so we know what to push next.

---

## 4. Segment 3 — The Algorithm, Step By Step

1. Push each array's **first** element onto the heap (skip empty arrays). Heap size starts at `<= k`.
2. Pop the smallest entry — it is guaranteed to be the next-smallest value overall.
3. Append its `value` to the result.
4. If that array has a next element, push `{nextValue, sameArrayIndex, elementIndex + 1}`.
5. Repeat steps 2-4 until the heap is empty.

The heap never holds more than `k` entries at once — one per array still in play.

---

## 5. Segment 4 — Code

```java
import java.util.*;

public class Solution {
    public int[] mergeKSortedArrays(int[][] arrays) {
        int k = arrays.length;

        // heap entry: {value, arrayIndex, elementIndex}
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));

        int total = 0;
        for (int i = 0; i < k; i++) {
            if (arrays[i].length > 0) {
                heap.offer(new int[]{arrays[i][0], i, 0});
                total += arrays[i].length;
            }
        }

        int[] result = new int[total];
        int idx = 0;

        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            int value = top[0], arrIdx = top[1], elemIdx = top[2];

            result[idx++] = value;

            if (elemIdx + 1 < arrays[arrIdx].length) {
                heap.offer(new int[]{arrays[arrIdx][elemIdx + 1], arrIdx, elemIdx + 1});
            }
        }

        return result;
    }
}
```

---

## 6. Segment 5 — Dry Run

Input:

    arrays = [ [1,4,7],      // array 0
               [2,5],        // array 1
               [3,6,8,9] ]   // array 2

Initial heap (one entry per array): `(1,0,0)`, `(2,1,0)`, `(3,2,0)`.

| Pop `(value, arr, idx)` | Result so far | Pushed next |
|---|---|---|
| `(1,0,0)` | `[1]` | `(4,0,1)` |
| `(2,1,0)` | `[1,2]` | `(5,1,1)` |
| `(3,2,0)` | `[1,2,3]` | `(6,2,1)` |
| `(4,0,1)` | `[1,2,3,4]` | `(7,0,2)` |
| `(5,1,1)` | `[1,2,3,4,5]` | array 1 exhausted |
| `(6,2,1)` | `[1,2,3,4,5,6]` | `(8,2,2)` |
| `(7,0,2)` | `[1,2,3,4,5,6,7]` | array 0 exhausted |
| `(8,2,2)` | `[1,2,3,4,5,6,7,8]` | `(9,2,3)` |
| `(9,2,3)` | `[1,2,3,4,5,6,7,8,9]` | array 2 exhausted |

Heap empty -> done.

    result = [1,2,3,4,5,6,7,8,9]

Notice the heap size never exceeds `k = 3` at any point — that's the whole benefit over dumping everything into one list and sorting it.

---

## 7. Complexity

- **Time:** `O(n log k)` — `n` = total elements across all arrays, each pushed and popped once from a heap of size at most `k`.
- **Space:** `O(k)` for the heap, `O(n)` for the result.

Compare to the naive approach (flatten all `n` elements, then `Arrays.sort`): that's `O(n log n)`. The heap approach wins whenever `k` (number of arrays) is much smaller than `n` (total elements) — same trade-off documented in `L_759_Employee_Free_Time`'s Approach 2 and used again in `PQ_02_Minimum_Interval_To_Include_Each_Query`.

---

## 8. Variant — Merge K Sorted Linked Lists (LeetCode 23)

Same problem, different container: you're given `k` sorted **linked lists** instead of `k` sorted arrays, and must merge them into one sorted list.

    Input:  lists = [1->4->5, 1->3->4, 2->6]
    Output: 1->1->2->3->4->4->5->6

---

## 9. Segment 1 — What's Different (And What Isn't)

The core idea from Segment 1 above is unchanged: the next-smallest overall value is always the smallest of the `k` current front nodes, so a min-heap of size `k` still does the job.

What changes is what the heap needs to track. With arrays, a node has no way to point to "the next element," so the heap entry had to be the artificial 3-tuple `{value, arrayIndex, elementIndex}`.

A linked-list node **already carries `next`** — it's its own "pointer to what comes after." So the heap can just store the `ListNode` itself, ordered by `.val`. No manual index bookkeeping needed. This is actually simpler than the array version.

---

## 10. Segment 2 — Code

```java
import java.util.*;

class ListNode {
    int val;
    ListNode next;
    ListNode(int val) { this.val = val; }
}

public class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        PriorityQueue<ListNode> heap = new PriorityQueue<>((a, b) -> Integer.compare(a.val, b.val));

        for (ListNode node : lists) {
            if (node != null) {
                heap.offer(node);
            }
        }

        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;

        while (!heap.isEmpty()) {
            ListNode smallest = heap.poll();
            tail.next = smallest;
            tail = tail.next;

            if (smallest.next != null) {
                heap.offer(smallest.next);
            }
        }

        return dummy.next;
    }
}
```

`dummy` is a throwaway head node so the merged list can be built without special-casing "what's the first node" — a standard linked-list-building trick.

---

## 11. Segment 3 — Dry Run

Input:

    lists = [ 1->4->5,   // list 0
              1->3->4,   // list 1
              2->6 ]     // list 2

Initial heap (one node per list): `1(list0)`, `1(list1)`, `2(list2)`.

| Popped node | Merged so far | Pushed next |
|---|---|---|
| `1` (list 0) | `1` | `4` (list 0) |
| `1` (list 1) | `1,1` | `3` (list 1) |
| `2` (list 2) | `1,1,2` | `6` (list 2) |
| `3` (list 1) | `1,1,2,3` | `4` (list 1) |
| `4` (list 0) | `1,1,2,3,4` | `5` (list 0) |
| `4` (list 1) | `1,1,2,3,4,4` | list 1 exhausted |
| `5` (list 0) | `1,1,2,3,4,4,5` | list 0 exhausted |
| `6` (list 2) | `1,1,2,3,4,4,5,6` | list 2 exhausted |

Heap empty -> done.

    result = 1->1->2->3->4->4->5->6

---

## 12. Complexity

- **Time:** `O(n log k)` — same as the array version, `n` total nodes, heap size at most `k`.
- **Space:** `O(k)` for the heap. Unlike the array version, no `O(n)` result array is needed — existing nodes are re-linked in place via `tail.next`, so extra space is just the heap.

---

## End of Notes
