# Stack Notes

## 04 - Sort a Stack Using Recursion

Problem: Sort a stack in ascending order (smallest on top, largest at bottom, or as specified) using only recursion — no extra array, queue, or explicit second stack.

---

## 1. Problem Understanding

Example:

```text
stack (top -> bottom) = [3, 1, 4, 1, 5, 9, 2, 6]
sorted (top -> bottom) = [1, 1, 2, 3, 4, 5, 6, 9]
```

This is essentially **recursive insertion sort**, adapted to a stack's pop/push interface instead of array indices.

---

## 2. Core Intuition

Two nested recursive routines, mirroring [[ST_03_Reverse_Stack_Using_Recursion]]:

1. **`sortHelper(stack)`** — pop the top, recursively sort what remains, then insert the popped element back at its **correct sorted position** (not necessarily the bottom).
2. **`placeElementAtCorrectPosition(stack, element)`** — while the current top of the (already sorted) stack is `<= element`, that's the right spot to push `element` directly on top. Otherwise, pop the top, recurse to place `element` deeper, then push the popped top back on.

---

## 3. Why Recursion?

Exactly as before: the call stack holds the elements temporarily removed while we search for the correct insertion point, standing in for the auxiliary storage we're not allowed to declare.

`sortHelper` assumes (inductively) that once it has sorted everything below the current top, `placeElementAtCorrectPosition` can correctly insert one more element into that already-sorted stack — same logic as insertion sort inserting one element into an already-sorted prefix.

---

## 4. Java Implementation

```java
import java.util.Stack;

class Solution {
    public void sortStack(Stack<Integer> st) {
        if (st.isEmpty()) {
            return;
        }

        int top = st.peek();
        st.pop();

        sortStack(st);

        placeElementAtCorrectPosition(st, top);
    }

    private void placeElementAtCorrectPosition(Stack<Integer> st, int element) {
        if (st.isEmpty()) {
            st.push(element);
            return;
        }

        if (st.peek() <= element) {
            // everything below is already sorted and smaller-or-equal, so this is the spot
            st.push(element);
            return;
        }

        int top = st.peek();
        st.pop();

        placeElementAtCorrectPosition(st, element);

        st.push(top);
    }
}
```

Result: the stack ends up with the **largest** element on top and the **smallest** at the bottom (see the dry run below for why). Flip the comparison in `placeElementAtCorrectPosition` from `<=` to `>=` if the opposite order (smallest on top) is required.

---

## 5. Dry Run

Input:

```text
stack (top -> bottom) = [3, 1, 2]
```

`sortStack([3,1,2])`:
- pop `3`, recurse on `[1,2]`
  - pop `1`, recurse on `[2]`
    - pop `2`, recurse on `[]` → empty, return.
    - `placeElementAtCorrectPosition([], 2)`: empty → push `2` → stack `[2]`
  - `placeElementAtCorrectPosition([2], 1)`: top `2 <= 1`? No → pop `2`, recurse `placeElementAtCorrectPosition([], 1)` → push `1` → `[1]`; push `2` back → `[2, 1]`
- Now stack (top->bottom) is `[2, 1]`.
- `placeElementAtCorrectPosition([2,1], 3)`: top `2 <= 3`? Yes → push `3` directly → `[3, 2, 1]`

Final stack (top -> bottom): `[3, 2, 1]`, i.e. reading bottom-to-top: `1, 2, 3`.

The condition `st.peek() <= element` pushes `element` above anything smaller-or-equal, so **larger elements end up closer to the top** — this implementation sorts with the largest on top and smallest at the bottom (descending top-to-bottom). If "smallest on top" (ascending top-to-bottom) is required instead, flip the comparison to `st.peek() >= element`. Always confirm which convention the problem statement wants before submitting.

---

## 6. Edge Cases

- Empty stack: `sortStack` returns immediately, no-op.
- Single element: trivially sorted, `sortHelper` pops it, recurses on empty (returns), then places it back via the base case of `placeElementAtCorrectPosition`.
- Already sorted stack: still runs the full recursive process; every `placeElementAtCorrectPosition` call resolves in its base case or first comparison (best-case-like behavior similar to insertion sort on sorted input, but the algorithm doesn't detect this in advance).
- Duplicate values: the `<=` (or `>=`) comparison determines whether equal elements are placed above or below existing equal elements — either is a valid stable-ish placement, but be consistent.
- Reverse-sorted stack: worst case for both time and recursion depth, mirroring insertion sort's worst case.

---

## 7. Common Mistakes

- Choosing the wrong comparison direction (`<=` vs `>=`) for the desired sort order (largest-on-top vs smallest-on-top) — always verify with a small dry run which end ends up sorted which way.
- Forgetting the base case in `placeElementAtCorrectPosition` — infinite recursion or exceptions on an empty stack.
- Swapping the order of "recurse then place" — `sortHelper` must fully sort the remainder **before** attempting to place the popped element; placing first would insert into an unsorted stack, breaking the invariant.
- Assuming this is O(n log n) because it's "like sorting" — it's actually O(n²), same complexity class as insertion sort, because each placement can walk through the entire (already sorted) remainder.

---

## 8. Complexity

```text
Time:  O(n^2)   (n calls to sortStack, each doing up to O(n) work in placeElementAtCorrectPosition)
Space: O(n)     (recursion call stack depth)
```

---

## 9. Pattern Recognition

Same family as [[ST_02_Find_Middle_Element_Of_Stack]] and [[ST_03_Reverse_Stack_Using_Recursion]] — recursion substituting for disallowed auxiliary storage. This specific problem is recursive **insertion sort**, restated in terms of stack pop/push instead of array shifting.

---

## 10. Takeaway

"Sort a stack with only recursion" is insertion sort wearing a stack costume: pop one element, recursively sort the rest, then walk back down (via recursion) to insert that element in its correct place. Always double-check the comparison direction against the exact "top should be smallest/largest" requirement before trusting the result.

---

# End of Notes
