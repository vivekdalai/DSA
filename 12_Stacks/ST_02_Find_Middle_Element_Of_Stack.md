# Stack Notes

## 02 - Find the Middle Element of a Stack (Recursion)

Problem: Given a stack, return its middle element without using any other data structure (array, list, etc.) for storage — only the call stack itself may be used as auxiliary space.

---

## 1. Problem Understanding

Example:

```text
stack (top -> bottom) = [1, 2, 3, 4, 5]
size = 5
middle = 3   (the 3rd element counting from the top, i.e., index 2 if top is index 0)
```

If the size is even, e.g. `size = 6`, "middle" is conventionally the element at position `size/2` counting from the top (index `3` if 0-indexed from the top, i.e., the *lower* of the two middle elements).

The catch: a `Stack`/`Deque` only exposes `peek()` and `pop()` at the top — there's no direct indexing. We must temporarily remove elements to reach the middle, then restore the stack exactly as it was.

---

## 2. Core Intuition

We already know the stack's `size` before starting (`stack.size()` is `O(1)`).

Recursively pop from the top, counting how many elements we've removed (`count`). When `count == size / 2`, the element currently on top **is** the middle — return it directly via `peek()`, without popping it.

As the recursion unwinds, push every previously-popped element back on, in the reverse order they were removed — restoring the original stack.

---

## 3. Why Recursion (Not a Loop)?

A simple loop that pops elements to find the middle would need somewhere to *store* the popped elements so they can be pushed back afterward — normally an array or another stack. Since we're not allowed extra storage, we exploit the **call stack** itself: each recursive call's local variable `top` holds exactly one popped element, "storing" it for free until the call returns and pushes it back.

```text
recursive call stack (implicit) == the auxiliary storage we're not allowed to declare explicitly
```

---

## 4. Java Implementation

```java
class Solution {
    public int getMiddle(java.util.Stack<Integer> st) {
        return getMidValue(st, st.size(), 0);
    }

    private int getMidValue(java.util.Stack<Integer> st, int size, int count) {
        if (count == size / 2) {
            return st.peek();
        }

        int top = st.peek();
        st.pop();

        int answer = getMidValue(st, size, count + 1);

        st.push(top); // restore the stack on the way back up

        return answer;
    }
}
```

---

## 5. Dry Run

Input:

```text
stack (top -> bottom) = [5, 4, 3, 2, 1]   (size = 5, so size/2 = 2)
```

| call | count | stack top before | action |
|---|---:|---:|---|
| getMidValue(size=5,count=0) | 0 | 5 | not middle yet, pop 5, recurse |
| getMidValue(size=5,count=1) | 1 | 4 | not middle yet, pop 4, recurse |
| getMidValue(size=5,count=2) | 2 | 3 | count==size/2 → return 3 (peek, no pop) |
| unwind | 1 | - | push 4 back |
| unwind | 0 | - | push 5 back |

Result: `3`. Stack restored to `[5,4,3,2,1]`.

---

## 6. Edge Cases

- Empty stack: `st.size() == 0`, `size/2 == 0`, and the very first call has `count == 0 == size/2`, but `st.peek()` on an empty stack throws — guard for this explicitly (`if (st.isEmpty()) throw ...` or return a sentinel) before calling the recursive helper.
- Single element: `size = 1`, `size/2 = 0`, so the base case triggers immediately on the first call and the single element is returned without ever being popped.
- Even size (e.g. `size = 4`): `size/2 = 2` — the middle returned is the "lower-middle" (3rd from top when counting top as 1st). Confirm with the interviewer/problem statement which of the two middle elements is wanted for even sizes.
- Very large stacks: recursion depth is `O(size/2)` — for extremely large stacks this risks a `StackOverflowError` (a real Java call-stack overflow, distinct from the logical "stack" data structure). This is an inherent trade-off of the "recursion as storage" trick.

---

## 7. Common Mistakes

- Popping the middle element itself instead of just `peek()`-ing it — the middle element must remain in the stack after the operation (only *finding* it is required, not removing it).
- Forgetting to push elements back on the way back up the recursion — this permanently corrupts the stack's original order.
- Off-by-one on the middle condition for even-sized stacks — clarify whether "middle" means the upper-middle or lower-middle element.
- Computing `size` incorrectly by calling `st.size()` again inside the recursive calls (it must be computed **once**, before any elements are popped, and passed down unchanged).

---

## 8. Complexity

```text
Time:  O(n)     (n/2 pops on the way down, n/2 pushes on the way back up)
Space: O(n)     (recursion call stack depth, since no explicit auxiliary structure is used)
```

---

## 9. Pattern Recognition

Any "manipulate a stack without extra explicit storage" problem (this one, [[ST_03_Reverse_Stack_Using_Recursion]], [[ST_04_Sort_Stack_Using_Recursion]]) uses the same trick: recursion's call stack substitutes for the disallowed auxiliary data structure. The general shape is:

```text
pop top, save in a local variable
recurse on the smaller stack
use the saved local variable to restore or reposition state on the way back up
```

---

## 10. Takeaway

When a data-structure-manipulation problem forbids extra storage, ask whether the recursive call stack can play that role instead — each stack frame's local variables act as one "slot" of hidden storage, freed automatically when the frame returns.

---

# End of Notes
