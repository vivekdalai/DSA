# Monotonic Stack Notes

## 04 - Stock Span Problem (GFG classic / related to LC 901)

Problem: Given the price of a stock on `n` consecutive days, find the **span** of the stock's price for each day. The span on day `i` is the maximum number of *consecutive* days (ending at and including day `i`) for which the price was less than or equal to `price[i]`.

---

## 1. Problem Understanding

Example:

```text
price = [100, 80, 60, 70, 60, 75, 85]
span  = [1,   1,  1,  2,  1,  4,  6]
```

For day `5` (`price = 75`), we look backward: `75 >= 60`, `75 >= 70`, `75 >= 60`, `75 >= 80`? No — `80 > 75`, so we stop. Days counted backward: `75, 60, 70, 60` = 4 days. Span = `4`.

This is the same shape as **"previous greater element"**: we want the distance back to the nearest day whose price was *strictly greater*.

```text
span[i] = i - indexOfPreviousGreaterElement(i)
```

---

## 2. Core Intuition

Scan **left to right**, keeping a stack of `(price, index)` pairs.

While the current price is greater than or equal to the price at the stack's top, that top entry can never be a "previous greater" for anything further right either (today's price blocks it, being at least as large) — pop it.

What remains on top after popping is the nearest strictly-greater price to the left.

---

## 3. Why Monotonic Stack?

```text
for i in 0..n-1:
    while stack not empty and stack.top().price <= price[i]:
        pop()
    span[i] = stack.empty() ? (i + 1) : (i - stack.top().index)
    push((price[i], i))
```

We need the **index**, not just the value, to compute the distance — that's why the stack stores pairs, not raw prices.

---

## 4. Java Implementation

```java
import java.util.ArrayDeque;
import java.util.Deque;

class Pair {
    int val, pos;

    Pair(int val, int pos) {
        this.val = val;
        this.pos = pos;
    }
}

class Solution {
    public int[] calculateSpan(int[] price, int n) {
        Deque<Pair> stack = new ArrayDeque<>();
        int[] ans = new int[n];

        for (int i = 0; i < n; i++) {
            Pair curr = new Pair(price[i], i);

            while (!stack.isEmpty() && stack.peekFirst().val <= price[i]) {
                stack.pollFirst();
            }

            ans[i] = stack.isEmpty() ? (i + 1) : (i - stack.peekFirst().pos);
            stack.addFirst(curr);
        }

        return ans;
    }
}
```

### Streaming / Online version (LC 901 "Online Stock Span")

LC 901 asks for an `StockSpanner` class where prices arrive one at a time (you don't know future prices). The same stack idea works, but instead of storing `(price, index)`, store `(price, span)` and fold in previously-computed spans when popping:

```java
class StockSpanner {
    private Deque<int[]> stack = new ArrayDeque<>(); // {price, span}

    public int next(int price) {
        int span = 1;

        while (!stack.isEmpty() && stack.peekFirst()[0] <= price) {
            span += stack.pollFirst()[1];
        }

        stack.addFirst(new int[]{price, span});
        return span;
    }
}
```

---

## 5. Dry Run

Input:

```text
price = [100, 80, 60, 70, 60, 75, 85]
```

| i | price | pops (val,pos) | stack after push | span |
|---:|---:|---|---|---:|
| 0 | 100 | - | [(100,0)] | 1 |
| 1 | 80 | - | [(80,1),(100,0)] | 1 |
| 2 | 60 | - | [(60,2),(80,1),(100,0)] | 1 |
| 3 | 70 | pop (60,2) | [(70,3),(80,1),(100,0)] | 2 |
| 4 | 60 | - | [(60,4),(70,3),(80,1),(100,0)] | 1 |
| 5 | 75 | pop (60,4), pop (70,3) | [(75,5),(80,1),(100,0)] | 4 |
| 6 | 85 | pop (75,5), pop (80,1) | [(85,6),(100,0)] | 6 |

Final:

```text
span = [1, 1, 1, 2, 1, 4, 6]
```

---

## 6. Edge Cases

- First day: no history exists, span is always `1`.
- Strictly increasing prices (e.g. `[10,20,30,40]`): every price is a new maximum, so span grows as `1, 2, 3, 4` — the stack effectively empties every time.
- Strictly decreasing prices (e.g. `[40,30,20,10]`): every span is `1`; the stack keeps growing and nothing is ever popped.
- All equal prices (e.g. `[5,5,5,5]`): since the pop condition uses `<=`, everything before the current day gets absorbed and spans become `1,2,3,4` (equal counts as "not greater").
- Single day: span is `[1]`.

---

## 7. Common Mistakes

- Using `<` instead of `<=` for the pop condition — the span definition explicitly says "less than **or equal to**," so equal prices must count toward the span.
- Storing only the price and not the index — without the index, the distance `i - previousGreaterIndex` cannot be computed.
- Confusing this with "next greater" — span looks **backward** (previous greater), so the scan direction is left-to-right, opposite of [[MS_02_Next_Greater_Element_LC496]].

---

## 8. Complexity

```text
Time:  O(n)
Space: O(n)
```

---

## 9. Pattern Recognition

"How far back to the previous element that breaks a condition" is the mirror image of "how far forward to the next element that breaks a condition" ([[MS_02_Next_Greater_Element_LC496]]). Same monotonic-stack machinery, just scanning in the opposite direction and computing `i - stack.top().index` instead of storing the neighbor's value directly.

---

## 10. Takeaway

Stock span is "previous greater element," phrased as a distance instead of a value. Store indices (or fold precomputed spans, for the streaming variant) so the popped information isn't lost — it's exactly what makes the answer computable in O(1) amortized per element.

---

# End of Notes
