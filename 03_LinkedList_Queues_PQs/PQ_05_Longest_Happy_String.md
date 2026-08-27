# Priority Queue Notes

## 05 - Longest Happy String

Problem: [LeetCode 1405 - Longest Happy String](https://leetcode.com/problems/longest-happy-string/description/)

---

## 1. Problem, In One Line

Given counts `a`, `b`, `c`, build the longest string possible using at most that many `'a'`s, `'b'`s, and `'c'`s such that no character repeats 3 times in a row (no `"aaa"`, `"bbb"`, or `"ccc"` substring).

**Example**

    Input:  a = 1, b = 1, c = 7
    Output: "ccaccbcc"

    Input:  a = 7, b = 1, c = 0
    Output: "aabaa"

---

## 2. Core Idea

At every position, the best move is to append whichever remaining character currently has the **highest count** — that preserves the most flexibility for whichever character turns out to be scarcest later.

The only unsafe moment is when the last two characters already placed equal that same character: adding a third would form `"aaa"`/`"bbb"`/`"ccc"`. In that one case, greedily borrow the **second-most-frequent** character instead, place one copy of it, then put the blocked character back for the next round.

Same shape as `PQ_01_Task_Scheduler_Cycle_Time_Explanation.md` — "always take the most urgent thing, unless it's currently blocked, then take the next most urgent" — but the block condition here is a 1-step lookback on the output string instead of a cooldown timer.

---

## 3. What Goes In The Heap

A max-heap of `{count, charIndex}` pairs, ordered by `count` descending, seeded with only the characters whose count is `> 0`.

- `count` — copies of this character remaining to place.
- `charIndex` — `0/1/2` for `'a'/'b'/'c'`, recovered as `(char) ('a' + charIndex)`.

No separate state is needed to check "would this create 3 in a row" — that's read directly off the last two characters of the result built so far.

---

## 4. The Algorithm, Step By Step

1. Push each of `a`, `b`, `c` onto the heap as `{count, charIndex}`, skipping any that are `0`.
2. Pop the top entry (`top1`, the currently most frequent character).
3. If appending `top1` would make the last 3 characters identical:
   - if the heap is now empty, stop — nothing safe is left to place.
   - otherwise pop the next entry (`top2`), append it instead, decrement its count and push it back if it's still `> 0`, then push `top1` back unchanged for the next round.
4. Otherwise append `top1` directly, decrement its count, and push it back if it's still `> 0`.
5. Repeat until the heap is empty.

---

## 5. Code

```java
import java.util.*;

class Solution {
    public String longestDiverseString(int a, int b, int c) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((p, q) -> Integer.compare(q[0], p[0]));
        if (a > 0) pq.offer(new int[]{a, 0});
        if (b > 0) pq.offer(new int[]{b, 1});
        if (c > 0) pq.offer(new int[]{c, 2});

        StringBuilder sb = new StringBuilder();

        while (!pq.isEmpty()) {
            int[] top1 = pq.poll();
            int len = sb.length();

            boolean wouldTriple = len >= 2
                    && sb.charAt(len - 1) == (char) ('a' + top1[1])
                    && sb.charAt(len - 2) == (char) ('a' + top1[1]);

            if (wouldTriple) {
                if (pq.isEmpty()) {
                    break; // top1 is blocked and nothing else is left to place
                }
                int[] top2 = pq.poll();
                sb.append((char) ('a' + top2[1]));
                if (--top2[0] > 0) {
                    pq.offer(top2);
                }
                pq.offer(top1);
            } else {
                sb.append((char) ('a' + top1[1]));
                if (--top1[0] > 0) {
                    pq.offer(top1);
                }
            }
        }

        return sb.toString();
    }
}
```

---

## 6. Dry Run

Input: `a = 7, b = 1, c = 0`

Initial heap: `(7,'a')`, `(1,'b')`

| Popped `top1` | Last two in result | Blocked? | Action | Result after |
|---|---|---|---|---|
| `(7,a)` | — | no | append 'a' | `a` |
| `(6,a)` | `"a"` (len 1) | no | append 'a' | `aa` |
| `(5,a)` | `"aa"` | **yes** | borrow `(1,b)`: append 'b', re-push `(5,a)` | `aab` |
| `(5,a)` | `"ab"` | no | append 'a' | `aaba` |
| `(4,a)` | `"ba"` | no | append 'a' | `aabaa` |
| `(3,a)` | `"aa"` | **yes**, heap empty | break | `aabaa` |

Result: `"aabaa"` — matches the expected output.

---

## 7. Complexity

- **Time:** `O((a + b + c) * log 3)`, effectively `O(n)` — the heap never holds more than 3 entries.
- **Space:** `O(a + b + c)` for the output `StringBuilder`, `O(1)` for the heap.

---

## 8. Edge Cases and Pitfalls

- Only one character has a positive count (e.g. `a = 100, b = 0, c = 0`): caps at `"aa"` once the top character self-blocks and the heap is empty — correctly length 2, not `""`.
- `a + b + c > 0` is guaranteed at the start, but the heap **can** still empty out mid-loop after `top1` gets blocked with nothing left to borrow from — that's the `break` case, not a bug.
- Must re-push `top1` unchanged after borrowing `top2` — skipping this silently drops remaining copies of the blocked character and produces a shorter-than-optimal string.
- Decrement-then-check (`if (--top2[0] > 0)`) before re-offering avoids ever pushing a `0`-count pair back onto the heap.
- Comparator ties (two characters with equal top count) can resolve in either order — both are valid, since the problem accepts any longest happy string, not one specific string.

---

## End of Notes
