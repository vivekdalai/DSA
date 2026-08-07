# Monotonic Stack Notes

## 03 - Next Greater Element II / Circular Array (LC 503)

Problem: Given a **circular** array `nums`, find the next greater element for every element, where "next" wraps around from the end of the array back to the beginning.

---

## 1. Problem Understanding

Example:

```text
nums = [1,2,1]
answer = [2,-1,2]
```

For index `2` (value `1`), the array wraps around: after index `2` comes index `0` again (value `1`), then index `1` (value `2`). So the next greater element for the last `1` is `2`.

This is the same problem as [[MS_02_Next_Greater_Element_LC496]], except the "right side" of an element can wrap around to the start of the array.

---

## 2. Core Intuition

We cannot physically loop the array, but we can **simulate** looping it by iterating over `2 * n` virtual indices and mapping each virtual index back to a real index with `i % n`.

```text
real index = i % size
```

Walking from `2n - 1` down to `0` means every real element gets visited twice: once as its own "true" position, and once as if it were shifted one full lap earlier. By the time we reach the second (real, leftmost) visit for index `i`, the stack already holds every relevant candidate found while scanning through the "wrapped" second half.

---

## 3. Why Monotonic Stack?

Exactly the same rule as plain Next Greater Element, just applied over `2n` virtual steps instead of `n`:

```text
for i from 2n-1 down to 0:
    curr = nums[i % n]
    while stack not empty and stack.top() <= curr:
        pop()
    ans[i % n] = stack.empty() ? -1 : stack.top()
    push(curr)
```

Because we only ever *write* `ans[i % n]` — never read it — writing it twice for the same index is safe; the second (final) write, which happens on the earlier iteration of `i`, is the one that sticks... actually the **first** write in iteration order (at the larger `i`, i.e., the "later lap") gets overwritten by the second write (smaller `i`, the "real" position) — and that second write is the correct final answer, because by then the stack has already absorbed the wrap-around elements.

---

## 4. Java Implementation

```java
import java.util.Arrays;
import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public int[] nextGreaterElements(int[] nums) {
        int size = nums.length;
        int[] ans = new int[size];
        Arrays.fill(ans, -1);

        Deque<Integer> st = new ArrayDeque<>();

        for (int i = 2 * size - 1; i >= 0; i--) {
            int curr = nums[i % size];

            while (!st.isEmpty() && st.peekFirst() <= curr) {
                st.pollFirst();
            }

            ans[i % size] = st.isEmpty() ? -1 : st.peekFirst();
            st.addFirst(curr);
        }

        return ans;
    }
}
```

---

## 5. Dry Run

Input:

```text
nums = [1, 2, 1]   (size = 3, virtual range i = 5..0)
```

| i | i % 3 | curr | pops | stack after push | ans[i % 3] |
|---:|---:|---:|---|---|---:|
| 5 | 2 | 1 | - | [1] | -1 |
| 4 | 1 | 2 | pop 1 | [2] | -1 |
| 3 | 0 | 1 | - | [1,2] | 2 |
| 2 | 2 | 1 | pop 1 | [1,2] | 2 |
| 1 | 1 | 2 | pop 1, pop 2 | [2] | -1 |
| 0 | 0 | 1 | - | [1,2] | 2 |

At `i = 2` (the "real", final visit to index `2`), the stack already holds `[1, 2]` from the wrapped lap (`i = 3, 4`), so popping the `1` on top exposes the `2` underneath — that `2` is the wrap-around answer.

The last write to each index (the smallest `i` with that `i % n`) is the one that stands:

```text
ans = [2, -1, 2]
```

This matches the expected output for `nums = [1,2,1]`.

---

## 6. Edge Cases

- Single element (`[5]`): no element other than itself exists, and it can't be its own "next", so answer is `[-1]`.
- All elements equal (`[3,3,3]`): every answer is `-1` (nothing is *strictly* greater, ever, even after wrapping fully around).
- Strictly increasing then wraps (`[1,2,3]`): last element's next-greater wraps to index `0` but `1 < 3`, so its answer is `-1`, matching a normal (non-circular) run since nothing wraps successfully here.
- Already using `2 * size` virtual length: for very large arrays, this doubles memory/iteration but stays `O(n)` overall.

---

## 7. Common Mistakes

- Forgetting the modulo (`i % size`) and going out of bounds.
- Iterating only `n` steps instead of `2n`, which misses the wrap-around case entirely (this degenerates to plain Next Greater Element).
- Clearing the stack between the two "laps" — don't; the whole point is that the stack carries state from the second (wrapped) lap into the first (real) lap.
- Off-by-one on the virtual range: it must be `i = 2*n - 1` down to `0`, inclusive.

---

## 8. Complexity

```text
Time:  O(n)      (2n total iterations, each element pushed/popped at most twice)
Space: O(n)
```

---

## 9. Pattern Recognition

Whenever an array is described as **circular** and needs a "next greater/smaller" style answer:

```text
simulate wrap-around by iterating 2n virtual indices
map back with i % n
keep the monotonic stack logic identical to the non-circular version
```

Builds directly on [[MS_02_Next_Greater_Element_LC496]].

---

## 10. Takeaway

Circular array problems rarely need real array rotation — doubling the iteration range and using modulo indexing is enough to let a monotonic stack "see" the wrap-around, while keeping the same `O(n)` guarantees.

---

# End of Notes
