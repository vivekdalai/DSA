# Monotonic Stack Notes

## 08 - Largest Rectangle in Histogram (LC 84)

Problem: Given an array `heights` representing the heights of histogram bars (each of width `1`), find the area of the largest rectangle that can be formed within the histogram.

---

## 1. Problem Understanding

Example:

```text
heights = [2,1,5,6,2,3]
answer = 10
```

The rectangle of area `10` comes from bars `[5,6]` (width `2`, using the smaller height `5`) — actually the maximal one here is height `5` spanning indices `2..3` giving area `10`, or height `2` spanning indices `2..5` giving area `8`. The largest is `10`.

For **each bar**, if we imagine it as the *shortest* bar in some rectangle, that rectangle can extend left and right until it hits a bar shorter than it. So for every bar `i`:

```text
maxRectangleUsingHeight[i] = heights[i] * (distance it can extend left and right before hitting a shorter bar)
```

The answer is the max of this over all `i`.

---

## 2. Core Intuition

For bar `i`, we need:

```text
NSL[i] = index of Nearest Smaller bar to the Left
NSR[i] = index of Nearest Smaller bar to the Right
```

The rectangle using `heights[i]` as its height can span from just after `NSL[i]` to just before `NSR[i]`:

```text
width = NSR[i] - NSL[i] - 1
area  = width * heights[i]
```

---

## 3. Why Monotonic Stack?

To build `NSL`, maintain a stack that is **non-decreasing from bottom to top** — whenever the current bar is shorter than or equal to the stack's top, the top can never be the "nearest smaller" for anything further right, so pop it:

```text
for i in 0..n-1:
    while stack not empty and heights[stack.top()] >= heights[i]:
        pop()
    NSL[i] = stack.empty() ? -1 : stack.top()
    push(i)
```

`NSR` is the mirror, scanning right to left with the same pop condition.

---

## 4. Java Implementation

```java
import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public int largestRectangleArea(int[] heights) {
        int size = heights.length;
        int[] NSEL = new int[size]; // Nearest Smaller Element Left (index)
        int[] NSER = new int[size]; // Nearest Smaller Element Right (index)

        Deque<Integer> st = new ArrayDeque<>();

        // NSEL
        for (int i = 0; i < size; i++) {
            while (!st.isEmpty() && heights[st.peekFirst()] >= heights[i]) {
                st.pollFirst();
            }
            NSEL[i] = st.isEmpty() ? -1 : st.peekFirst();
            st.addFirst(i);
        }

        st.clear();

        // NSER
        for (int i = size - 1; i >= 0; i--) {
            while (!st.isEmpty() && heights[st.peekFirst()] >= heights[i]) {
                st.pollFirst();
            }
            NSER[i] = st.isEmpty() ? size : st.peekFirst();
            st.addFirst(i);
        }

        int maxArea = 0;
        for (int i = 0; i < size; i++) {
            int left = i - NSEL[i];
            int right = NSER[i] - i;
            int area = (left + right - 1) * heights[i];
            maxArea = Math.max(maxArea, area);
        }

        return maxArea;
    }
}
```

Note: `left + right - 1` is algebraically the same as `NSER[i] - NSEL[i] - 1` (the `heights[i]` bar itself is counted once, not twice, since it contributes to both `left` and `right`).

### Single-Pass Stack Version (no NSL/NSR arrays)

A more common interview-ready version computes the area while popping, in one pass:

```java
class Solution {
    public int largestRectangleArea(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>(); // indices, increasing height
        int maxArea = 0;
        int n = heights.length;

        for (int i = 0; i <= n; i++) {
            int currHeight = (i == n) ? 0 : heights[i]; // sentinel to flush remaining bars

            while (!stack.isEmpty() && heights[stack.peekFirst()] >= currHeight) {
                int height = heights[stack.pollFirst()];
                int width = stack.isEmpty() ? i : i - stack.peekFirst() - 1;
                maxArea = Math.max(maxArea, height * width);
            }

            stack.addFirst(i);
        }

        return maxArea;
    }
}
```

---

## 5. Dry Run

Input:

```text
heights = [2, 1, 5, 6, 2, 3]
```

NSEL (pop while `>=`):

| i | h[i] | pops | NSEL[i] |
|---:|---:|---|---:|
| 0 | 2 | - | -1 |
| 1 | 1 | pop idx0 | -1 |
| 2 | 5 | - | 1 |
| 3 | 6 | - | 2 |
| 4 | 2 | pop idx3, pop idx2 | 1 |
| 5 | 3 | - | 4 |

NSER (pop while `>=`, scanning right to left, order is i=5,4,3,2,1,0):

- i=5 (h=3): stack empty → NSER[5]=6 (size); push 5
- i=4 (h=2): top=5(h=3), 3>=2 → pop; stack empty → NSER[4]=6; push 4
- i=3 (h=6): top=4(h=2), 2>=6? No → NSER[3]=4; push 3
- i=2 (h=5): top=3(h=6), 6>=5 → pop; top=4(h=2), 2>=5? No → NSER[2]=4; push 2
- i=1 (h=1): top=2(h=5)->pop, top=4(h=2)->pop; stack empty → NSER[1]=6; push 1
- i=0 (h=2): top=1(h=1), 1>=2? No → NSER[0]=1; push 0

Final `NSER = [1, 6, 4, 4, 6, 6]`, `NSEL = [-1, -1, 1, 2, 1, 4]`.

Areas, using `area = (left + right - 1) * heights[i]` where `left = i - NSEL[i]` and `right = NSER[i] - i`:

| i | h[i] | left | right | area |
|---:|---:|---:|---:|---:|
| 0 | 2 | 1 | 1 | (1+1-1)*2=2 |
| 1 | 1 | 2 | 5 | (2+5-1)*1=6 |
| 2 | 5 | 1 | 2 | (1+2-1)*5=10 |
| 3 | 6 | 1 | 1 | (1+1-1)*6=6 |
| 4 | 2 | 3 | 2 | (3+2-1)*2=8 |
| 5 | 3 | 1 | 1 | (1+1-1)*3=3 |

Max area: `10` ✓

---

## 6. Edge Cases

- Single bar: answer is `heights[0] * 1`.
- All bars equal height (e.g. `[3,3,3,3]`): the answer is `height * n` — the whole histogram is one rectangle. Because the pop condition is `>=` and applied consistently in both directions, care is needed not to double count, but since we only ever read `NSEL`/`NSER` (not push twice), this resolves correctly to width `n`.
- Strictly increasing bars (e.g. `[1,2,3,4]`): the best rectangle might be the last bar alone, or a combination — the algorithm checks all `i`, so it's found automatically.
- Strictly decreasing bars: symmetric to the above.
- Empty input: no bars, answer is `0` (guard for `size == 0`, or let the loop simply not execute and `maxArea` stay `0`).
- Bars containing height `0`: contributes an area of `0` for that index, never affects the max unless all bars are `0`.

---

## 7. Common Mistakes

- Unlike [[MS_06_Sum_Of_Subarray_Minimums_LC907]] (which needs a strict/non-strict split to avoid double-counting a `sum`), this problem takes a `max`, so using `>=` consistently on both NSL and NSR is safe — even if two equal-height bars each compute an overlapping width, both arrive at the same correct maximal area, and `Math.max` absorbs the redundancy.
- Off-by-one in width calculation — always double check with `(left + right - 1)` or equivalently `NSER[i] - NSEL[i] - 1`.
- Forgetting the sentinel `size` for "no smaller element to the right" (must be `size`, not `size - 1` or `-1`).
- In the single-pass version, forgetting the sentinel height `0` appended after the array to flush any bars still on the stack at the end.

---

## 8. Complexity

```text
Time:  O(n)
Space: O(n)
```

---

## 9. Pattern Recognition

"Largest rectangle" problems (histogram, and later "maximal rectangle in a binary matrix" which reduces to this per-row) always reduce to **nearest smaller element on both sides**, exactly the same monotonic-stack template as [[MS_02_Next_Greater_Element_LC496]] and [[MS_06_Sum_Of_Subarray_Minimums_LC907]] — just optimizing a `max` instead of a `sum`.

---

## 10. Takeaway

Treat every bar as a candidate "shortest bar" of some rectangle, and use nearest-smaller-left / nearest-smaller-right to find how wide that rectangle can be. When hand-tracing, always double check the width formula (`left + right - 1`, not `left + right`) since it's the single easiest place to introduce an off-by-one.

---

# End of Notes
