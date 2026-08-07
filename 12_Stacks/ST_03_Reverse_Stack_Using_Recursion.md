# Stack Notes

## 03 - Reverse a Stack Using Recursion

Problem: Reverse the order of elements in a stack using only recursion — no extra array, queue, or second explicit stack allowed.

---

## 1. Problem Understanding

Example:

```text
stack (top -> bottom) = [1, 2, 3, 4, 5, 6]
reversed (top -> bottom) = [6, 5, 4, 3, 2, 1]
```

The element that was at the **bottom** must become the new **top**, and vice versa.

---

## 2. Core Intuition

Two nested recursive ideas are needed:

1. **`reverseStackHelper(stack)`** — pop the top element, recursively reverse what remains, then insert the popped element at the very **bottom** of the now-reversed remainder.
2. **`insertAtBottom(stack, element)`** — pop everything off, recursively insert `element` at the bottom of an empty stack (trivial base case), then push everything back on top as the recursion unwinds.

The key mental model: "top elements get pushed down to the bottom, recursively," which is exactly what happens each time `insertAtBottom` runs.

---

## 3. Why Recursion?

Just like [[ST_02_Find_Middle_Element_Of_Stack]], the call stack itself holds the elements that have been temporarily popped, acting as the "extra storage" we're not allowed to declare explicitly.

`reverseStackHelper` recurses down to an empty (or single-element) stack, then, as each frame returns, it takes the element it had set aside and inserts it at the bottom via `insertAtBottom` — effectively building the reversed order from the bottom up.

---

## 4. Java Implementation

```java
import java.util.Stack;

class Solution {
    public void reverseStack(Stack<Integer> st) {
        if (st.size() <= 1) {
            return;
        }

        int top = st.peek();
        st.pop();

        reverseStack(st);

        insertAtBottom(st, top);
    }

    private void insertAtBottom(Stack<Integer> st, int element) {
        if (st.isEmpty()) {
            st.push(element);
            return;
        }

        int top = st.peek();
        st.pop();

        insertAtBottom(st, element);

        st.push(top);
    }
}
```

---

## 5. Dry Run

Input:

```text
stack (top -> bottom) = [1, 2, 3]
```

`reverseStack([1,2,3])`:
- pop `1`, recurse on `[2,3]`
  - `reverseStack([2,3])`: pop `2`, recurse on `[3]`
    - `reverseStack([3])`: size `<= 1`, return immediately. Stack is `[3]`.
  - `insertAtBottom([3], 2)`:
    - pop `3`, recurse `insertAtBottom([], 2)` → push `2` → stack `[2]`
    - push `3` back → stack `[3, 2]`
  - Now stack (top->bottom) is `[3, 2]`.
- `insertAtBottom([3,2], 1)`:
  - pop `3`, recurse `insertAtBottom([2], 1)`:
    - pop `2`, recurse `insertAtBottom([], 1)` → push `1` → stack `[1]`
    - push `2` back → stack `[2, 1]`
  - push `3` back → stack `[3, 2, 1]`

Final: `[3, 2, 1]` (top -> bottom), which is `[1,2,3]` reversed. ✓

---

## 6. Edge Cases

- Empty stack: `reverseStack` on an empty stack does nothing (size `0 <= 1`), correctly a no-op.
- Single element: size `1 <= 1`, returns immediately — a single-element stack is trivially its own reverse.
- Already palindromic content (e.g. `[1,2,1]`): still goes through the full recursive process; the result happens to look unchanged, but the algorithm doesn't special-case this.
- Duplicate values: no special handling needed — the algorithm only cares about position, not uniqueness of values.

---

## 7. Common Mistakes

- Confusing `reverseStack` (reverses order top-to-bottom) with `insertAtBottom` (inserts one specific element at the bottom, keeping everything else's relative order) — they solve different sub-problems and one calls the other.
- Forgetting the base case in `insertAtBottom` (`if empty, push and return`) — without it, infinite recursion or a `NoSuchElementException` from `peek()`/`pop()` on empty.
- Pushing before recursing instead of after in `insertAtBottom` — the recursive call must happen **before** `st.push(top)` so that `element` is inserted below `top`, not above it.
- Trying to solve this iteratively "cleverly" without recursion or extra storage — it's not possible without one of those two (the problem is specifically designed to test recursion-as-storage).

---

## 8. Complexity

```text
Time:  O(n^2)   (reverseStack makes n calls; each calls insertAtBottom, which itself does up to n work)
Space: O(n)     (recursion call stack depth, across both nested recursions)
```

---

## 9. Pattern Recognition

Same family as [[ST_02_Find_Middle_Element_Of_Stack]] and [[ST_04_Sort_Stack_Using_Recursion]]: "modify a stack in place using only recursion." The reusable sub-technique is `insertAtBottom` (or a variant of it) — inserting an element at a specific position using recursion instead of an explicit second structure.

---

## 10. Takeaway

Reversing a stack recursively is really two problems in one: a helper that inserts a single element at the bottom (using recursion to "get past" every other element), and an outer routine that repeatedly extracts the top and reinserts it via that helper as the recursion unwinds.

---

# End of Notes
