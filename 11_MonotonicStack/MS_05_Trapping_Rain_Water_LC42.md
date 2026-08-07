# Monotonic Stack Notes

## 05 - Trapping Rain Water (LC 42)

Problem: Given `n` non-negative integers representing an elevation map where the width of each bar is `1`, compute how much water it can trap after raining.

---

## 1. Problem Understanding

Example:

```text
height = [0,1,0,2,1,0,1,3,2,1,2,1]
answer = 6
```

Water can only sit above a bar if there is a taller (or equal) bar on **both** its left and its right. The amount of water sitting directly above position `i` is:

```text
water[i] = min(maxToLeft[i], maxToRight[i]) - height[i]
```

(only when this is positive; otherwise no water sits there).

---

## 2. Core Intuition

For every index, we need to know:

- the tallest bar anywhere to its left (`maxInLeft[i]`)
- the tallest bar anywhere to its right (`maxInRight[i]`)

The water level above index `i` is capped by whichever of those two is **smaller** — water always leaks out over the shorter wall.

---

## 3. Approach A — Prefix / Suffix Max Arrays

Precompute both arrays in two linear passes, then combine:

```java
class Solution {
    public int trap(int[] height) {
        int size = height.length;
        if (size == 0) return 0;

        int[] maxInLeft = new int[size];
        int[] maxInRight = new int[size];
        maxInLeft[0] = height[0];
        maxInRight[size - 1] = height[size - 1];

        for (int i = 1; i < size; i++) {
            maxInLeft[i] = Math.max(maxInLeft[i - 1], height[i]);
        }

        for (int i = size - 2; i >= 0; i--) {
            maxInRight[i] = Math.max(maxInRight[i + 1], height[i]);
        }

        int collected = 0;
        for (int i = 0; i < size; i++) {
            int currHeight = height[i];
            if (maxInLeft[i] > currHeight && currHeight < maxInRight[i]) {
                collected += Math.min(maxInLeft[i], maxInRight[i]) - currHeight;
            }
        }

        return collected;
    }
}
```

```text
Time:  O(n)
Space: O(n)   (two extra arrays)
```

---

## 4. Approach B — Two Pointers, O(1) Space

We only ever need `min(maxAtLeft, maxAtRight)` at a time — we don't need the *whole* array of maxes, just the running max from whichever side we're currently advancing.

```java
class Solution {
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        long maxAtLeft = 0, maxAtRight = 0;
        long collected = 0;

        while (left < right) {
            if (height[left] <= height[right]) {
                if (height[left] >= maxAtLeft) {
                    maxAtLeft = height[left];
                } else {
                    collected += maxAtLeft - height[left];
                }
                left++;
            } else {
                if (height[right] >= maxAtRight) {
                    maxAtRight = height[right];
                } else {
                    collected += maxAtRight - height[right];
                }
                right--;
            }
        }

        return (int) collected;
    }
}
```

Why moving the **smaller side** is always safe: if `height[left] <= height[right]`, then whatever the true `maxAtRight` eventually turns out to be, it is *at least* `height[right] >= height[left]`. So the water level above `left` is governed purely by `maxAtLeft` — we don't need to know the real `maxAtRight` yet to finalize `left`'s contribution.

```text
Time:  O(n)
Space: O(1)
```

---

## 5. Approach C — Monotonic Stack (fills as you scan)

A third way, useful when the problem is framed as "process bars left to right and resolve water the moment a taller bar closes off a basin":

```java
import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public int trap(int[] height) {
        Deque<Integer> stack = new ArrayDeque<>(); // indices, decreasing height
        int collected = 0;

        for (int i = 0; i < height.length; i++) {
            while (!stack.isEmpty() && height[stack.peekFirst()] < height[i]) {
                int top = stack.pollFirst();
                if (stack.isEmpty()) break;

                int left = stack.peekFirst();
                int width = i - left - 1;
                int boundedHeight = Math.min(height[i], height[left]) - height[top];
                collected += width * boundedHeight;
            }
            stack.addFirst(i);
        }

        return collected;
    }
}
```

Intuition: the stack holds indices of bars in decreasing height order (a "basin wall waiting to be closed"). When a taller bar arrives, it closes off the basin whose floor was the popped index; the new stack top is the other wall.

```text
Time:  O(n)   (each index pushed once, popped once)
Space: O(n)
```

---

## 6. Dry Run (Approach B — two pointers)

Input:

```text
height = [0,1,0,2,1,0,1,3,2,1,2,1]
```

| left | right | h[left] | h[right] | action | collected |
|---:|---:|---:|---:|---|---:|
| 0 | 11 | 0 | 1 | maxAtLeft=0, left++ | 0 |
| 1 | 11 | 1 | 1 | maxAtLeft=1, left++ | 0 |
| 2 | 11 | 0 | 1 | 1-0=1 added, left++ | 1 |
| 3 | 11 | 2 | 1 | h[right]<=h[left]? no (2>1) -> move right; maxAtRight=1, right-- | 1 |
| 3 | 10 | 2 | 2 | h[left]<=h[right] -> maxAtLeft=2, left++ | 1 |
| ... | | | | (continues similarly) | ... |

Final result:

```text
6
```

(Full step-by-step trace omitted for brevity beyond illustrating the pointer-movement rule — the key invariant is: always advance the side with the smaller height, and either raise that side's running max or add trapped water.)

---

## 7. Edge Cases

- Fewer than 3 bars: no water can ever be trapped (need a "valley" between two higher walls). `[]`, `[5]`, `[5,3]` all return `0`.
- All bars equal height (e.g. `[3,3,3,3]`): flat surface, `0` water.
- Strictly increasing or strictly decreasing heights: no basin ever forms, `0` water.
- A single deep valley (e.g. `[5,0,0,0,5]`): all three approaches correctly trap `5 + 5 + 5 = 15`.
- Very large heights: use `long` accumulators if the input constraints allow overflow-prone sums (not required for LC's stated constraints, but a safe habit).

---

## 8. Common Mistakes

- In the prefix/suffix approach, forgetting `maxInLeft[i] > currHeight && currHeight < maxInRight[i]` and instead assuming water is always non-negative without checking — the check is what prevents negative contributions.
- In the two-pointer approach, comparing `maxAtLeft`/`maxAtRight` against the *other* side's height instead of tracking each side's own running max.
- In the stack approach, forgetting the `if (stack.isEmpty()) break;` check after popping — without a left wall, there's nothing to bound the water on that side.
- Off-by-one on `width = i - left - 1` in the stack approach — the width excludes both boundary indices.

---

## 9. Complexity Summary

| Approach | Time | Space |
|---|---|---|
| Prefix/suffix max arrays | O(n) | O(n) |
| Two pointers | O(n) | O(1) |
| Monotonic stack | O(n) | O(n) |

---

## 10. Pattern Recognition

"Water trapped above bar `i`" is fundamentally a **"nearest greater element on both sides"** problem — conceptually a cousin of [[MS_02_Next_Greater_Element_LC496]], just combined from both directions instead of one.

---

## 11. Takeaway

Three valid mental models for the same problem: precompute both walls (arrays), converge with two pointers (O(1) space by proving you only need the smaller side's max at each step), or resolve basins immediately with a monotonic stack. Interviewers usually want the two-pointer version for the space optimization, but the stack version generalizes better to variants like "trapping rain water II" (2D).

---

# End of Notes
