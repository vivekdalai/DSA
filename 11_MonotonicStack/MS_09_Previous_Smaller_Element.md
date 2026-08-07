# Monotonic Stack Notes

## 09 - Previous (Nearest) Smaller Element to the Left

Problem: For every element of an array, find the nearest element to its **left** that is strictly smaller than it. If none exists, the answer is `-1`.

---

## 1. Problem Understanding

Example:

```text
arr    = [4, 5, 2, 10, 8]
answer = [-1, 4, -1, 2, 2]
```

For `arr[1] = 5`: the nearest smaller element to its left is `4`.

For `arr[3] = 10`: scanning left, `2` is the nearest smaller element (we stop at the first one found going backward, we don't need the *smallest overall*, just the *nearest*).

For `arr[4] = 8`: nearest smaller to the left is `2` (index 2); `10` is not smaller, so it's skipped.

---

## 2. Approach A — Monotonic Stack (the standard O(n) approach)

Scan left to right, maintaining a stack that is increasing from bottom to top. Before processing `arr[i]`, pop everything on the stack that is `>= arr[i]` — those values can never be the "previous smaller" for `arr[i]` or for anything after it, since `arr[i]` itself is smaller-or-equal and stands in front of them.

```java
import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public int[] previousSmaller(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>(); // increasing values

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && stack.peekFirst() >= arr[i]) {
                stack.pollFirst();
            }
            result[i] = stack.isEmpty() ? -1 : stack.peekFirst();
            stack.addFirst(arr[i]);
        }

        return result;
    }
}
```

```text
Time:  O(n)
Space: O(n)
```

This is the same template as [[MS_02_Next_Greater_Element_LC496]], just scanning left-to-right (because "previous" looks backward) with the opposite comparison (smaller instead of greater).

---

## 3. Approach B — TreeSet (alternative, useful when values must stay sorted for other reasons)

A `TreeSet` keeps its elements in sorted order (backed by a red-black tree), giving `O(log n)` insertion, deletion, and "closest value" queries:

- `treeSet.lower(x)` → the greatest element strictly less than `x`, or `null` if none exists.
- `treeSet.higher(x)` → the smallest element strictly greater than `x`, or `null` if none exists.

Since we only ever need "the greatest element smaller than the current one, among everything seen so far," `lower()` directly answers the question — **without needing "nearest," because there is at most one candidate value that matters: the largest one below `curr`**.

```java
import java.util.Arrays;
import java.util.TreeSet;

public class NextGreaterLeft {
    public static void main(String[] args) {
        int[] arr = {2, 5, 7, 3, 4, 9, 6, 3};
        int n = arr.length;
        int[] result = new int[n];
        TreeSet<Integer> treeSet = new TreeSet<>();

        for (int i = 0; i < n; i++) {
            int curr = arr[i];
            Integer lower = treeSet.lower(curr);
            result[i] = (lower == null) ? -1 : lower;
            treeSet.add(curr);
        }

        System.out.println("previous smaller element: " + Arrays.toString(result));
    }
}
```

```text
Time:  O(n log n)
Space: O(n)
```

### Why This Works Even Though a TreeSet Isn't "Nearest In Position"

This is a subtle and important point: `treeSet.lower(curr)` gives the **largest value smaller than `curr`** among all elements seen so far — not necessarily the *closest by index*. For the "previous smaller element" problem, this is actually fine, because:

```text
if v is the largest value < curr seen so far,
then v is guaranteed to be closer in index than any other value < curr,
because any smaller value w < v would have been "blocked" by v
(v sits between w and curr in value, so v is a tighter/more useful bound)
```

More precisely: we don't actually need the index-nearest smaller element for *this* problem — we only need *the value* of the previous smaller element, and the largest such value seen so far is always a **valid answer** because nothing between it and `curr` (in the *array*, not just in value) could be `>= curr` without invalidating the "smaller" property for `v`. In fact this equivalence (largest-value-below == nearest-by-position among "still-standing" candidates) is exactly the monotonic-stack invariant in disguise — the stack in Approach A only ever contains a strictly increasing sequence of values, so its top-most element `<= curr` **is** the largest value below `curr` that has survived, matching what `TreeSet.lower()` returns.

---

## 4. Dry Run (Approach A — Stack)

Input:

```text
arr = [4, 5, 2, 10, 8]
```

| i | arr[i] | pops | stack after push | result[i] |
|---:|---:|---|---|---:|
| 0 | 4 | - | [4] | -1 |
| 1 | 5 | - | [5,4] | 4 |
| 2 | 2 | pop 5, pop 4 | [2] | -1 |
| 3 | 10 | - | [10,2] | 2 |
| 4 | 8 | pop 10 | [8,2] | 2 |

Final:

```text
[-1, 4, -1, 2, 2]
```

---

## 5. Edge Cases

- Empty array: return an empty array.
- Single element: no element to its left, answer is `[-1]`.
- Strictly increasing array (e.g. `[1,2,3,4]`): every answer (except the first) is the immediately preceding element.
- Strictly decreasing array (e.g. `[9,7,5,3]`): every answer is `-1`, since nothing to the left is ever smaller.
- Duplicate values (e.g. `[3,3,3]`): must use `>=` in the stack's pop condition (or, for the TreeSet, note that `lower()` is strict by definition) so that an equal value is correctly treated as "not smaller" — every answer is `-1`.
- All negative numbers: works unchanged since only relative order matters.

---

## 6. Common Mistakes

- Using `>` instead of `>=` in the stack pop condition — this makes duplicate values incorrectly "block" each other inconsistently. Always pop while `stack.top() >= curr` to correctly stop at the first strictly smaller value.
- Confusing `TreeSet.lower()` (strictly less) with `TreeSet.floor()` (less than **or equal**) — using `floor()` would incorrectly report a duplicate value as the "previous smaller" element.
- Forgetting that `TreeSet` stores each **value** once — if the array has duplicate values, adding a duplicate to a `TreeSet<Integer>` is a no-op (it doesn't multi-count), which is actually fine for this specific problem since we only care about the *value* of the nearest smaller element, not how many times it occurred. This assumption breaks if the problem instead required *index* tracking (in which case, use `TreeMap<Integer, Integer>` mapping value → most recent index, or fall back to the stack approach entirely).

---

## 7. Complexity Summary

| Approach | Time | Space | When to prefer |
|---|---|---|---|
| Monotonic stack | O(n) | O(n) | Default choice — simplest and fastest |
| TreeSet | O(n log n) | O(n) | If you already need sorted-order queries for other reasons in the same problem |

---

## 8. Pattern Recognition

This is the mirror of [[MS_02_Next_Greater_Element_LC496]] (direction: left instead of right; comparison: smaller instead of greater), and it underlies the boundary computations in [[MS_06_Sum_Of_Subarray_Minimums_LC907]], [[MS_07_Sum_Of_Subarray_Ranges_LC2104]], and [[MS_08_Largest_Rectangle_In_Histogram_LC84]].

---

## 9. Takeaway

The monotonic stack is the standard O(n) tool for "nearest greater/smaller, left/right" questions. A `TreeSet` can answer a *weaker* version of the same question (largest-value-below, not nearest-by-position) in O(n log n) — and for many problems those two notions coincide, but it's worth recognizing precisely why, rather than assuming it always holds.

---

# End of Notes
