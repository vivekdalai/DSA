# Priority Queue Notes

## 06 - The Number of the Smallest Unoccupied Chair

Problem: [LeetCode 1942 - The Number of the Smallest Unoccupied Chair](https://leetcode.com/problems/the-number-of-the-smallest-unoccupied-chair/description/)

---

## 1. Problem, In One Line

`n` friends arrive at `times[i][0]` and leave at `times[i][1]`. Chairs are numbered `0, 1, 2, ...`; each friend takes the **smallest-numbered free chair** on arrival. Return which chair `targetFriend` ends up in.

**Example**

    Input:  times = [[1,4],[2,3],[3,4]], targetFriend = 1
    Output: 1

    Input:  times = [[3,10],[1,5],[2,6]], targetFriend = 0
    Output: 2

---

## 2. Core Idea

Process friends in **arrival order** (sort by `times[i][0]` first — the input isn't guaranteed sorted). For each arrival:

1. Release every chair whose current occupant has already left by this arrival time.
2. Seat this friend in the smallest-numbered chair that's currently free — reuse one if any are free, otherwise mint a brand-new chair number.

The only real design question is *how* to track "currently free chair numbers" so step 2 is fast — that's where a heap earns its keep, and where it's easy to under-track state (see Section 7).

---

## 3. What Goes In The Heap(s)

Two separate heaps, tracking two different things:

- `occupied` — a min-heap of `{departureTime, chairNumber}`, ordered by `departureTime`. This tells you which chair frees up next.
- `available` — a min-heap of bare chair **numbers**, so "smallest free chair" is always `available.peek()`.

`nextNewChair` is a plain counter for chair numbers that have never existed yet.

---

## 4. The Algorithm, Step By Step

1. Sort friend indices by arrival time.
2. For each friend, in that order:
   - While `occupied` is non-empty and its smallest departure time is `<= arrival`: pop it and push **that chair's number** onto `available`. Do this for *every* chair that has freed up, not just the first one.
   - If `available` is non-empty, pop the smallest chair number from it — that's this friend's seat. Otherwise, this friend gets `nextNewChair++`.
   - Push `{departure, assignedChair}` onto `occupied`.
3. Record the answer for `targetFriend` as you go; return it at the end.

The key discipline: **every** chair that frees up gets pushed onto `available`, even if this friend only needs one of them. The rest stay there for whoever arrives next.

---

## 5. Code

```java
import java.util.*;

class Solution {
    public int smallestChair(int[][] times, int targetFriend) {
        int n = times.length;
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, (a, b) -> Integer.compare(times[a][0], times[b][0]));

        int[] result = new int[n];
        PriorityQueue<int[]> occupied = new PriorityQueue<>((p, q) -> Integer.compare(p[0], q[0])); // {departure, chair}
        PriorityQueue<Integer> available = new PriorityQueue<>(); // free chair numbers
        int nextNewChair = 0;

        for (int idx : order) {
            int arrival = times[idx][0], dept = times[idx][1];

            while (!occupied.isEmpty() && occupied.peek()[0] <= arrival) {
                available.offer(occupied.poll()[1]);
            }

            int chair = available.isEmpty() ? nextNewChair++ : available.poll();
            result[idx] = chair;
            occupied.offer(new int[]{dept, chair});
        }

        return result[targetFriend];
    }
}
```

Complexity:
- Time: `O(n log n)` — sorting, plus each friend does at most `O(log n)` heap work (amortized, since each chair is pushed/popped a bounded number of times).
- Space: `O(n)` across both heaps.

---

## 6. Dry Run

Input: `times = [[3,10],[1,5],[2,6]], targetFriend = 0`

Sorted by arrival: `(1,5)` idx1, `(2,6)` idx2, `(3,10)` idx0

| Friend | Arrival | Freed this step | `available` before seating | Seated in | `occupied` after |
|---|---|---|---|---|---|
| idx1 | 1 | none | `{}` | 0 (new) | `{(5,0)}` |
| idx2 | 2 | none (5 > 2) | `{}` | 1 (new) | `{(5,0),(6,1)}` |
| idx0 | 3 | none (5 > 3, 6 > 3) | `{}` | 2 (new) | `{(5,0),(6,1),(10,2)}` |

`result[0] = 2` — matches the expected output.

---

## 7. A Tempting But Buggy Shortcut

A natural first attempt uses **one** heap of `{departure, chair}` instead of two, and when a friend arrives, drains every chair that's freed up but only keeps the *minimum*-numbered one:

```java
// BUGGY — do not use. Kept here as a worked pitfall.
int[] top = pq.peek();
if (top[0] > arrival) {
    result[friendIdx] = pq.size();
    pq.offer(new int[]{dept, pq.size()});
} else {
    int minChair = top[1];
    while (!pq.isEmpty() && pq.peek()[0] <= arrival) {
        minChair = Math.min(minChair, pq.peek()[1]);
        pq.poll();
    }
    result[friendIdx] = minChair;
    pq.offer(new int[]{dept, minChair}); // <-- only minChair goes back in
}
```

This looks reasonable and passes small hand-traced examples, but it silently **drops every freed chair except the one it reuses**. Verified with a brute-force simulator across 1.6M random cases: **21,355 mismatches**. Concrete failing input:

    times = [[3,10],[10,24],[7,14],[16,22],[5,8],[12,26]], targetFriend = 5

Sorted by arrival: `(3,10)`→chair0, `(5,8)`→chair1, `(7,14)`→chair2 (chair1 not free yet: `8 > 7`).

The break happens on the next friend, `(10,24)`:
- Both chair1 (`dept 8`) and chair0 (`dept 10`) are free by arrival `10` — both get popped.
- `minChair = min(1, 0) = 0` → this friend reuses chair0, and only `{new_dept, 0}` is pushed back.
- **Chair 1 — genuinely free since time 8 — is now gone from the heap. Nobody is tracking it anymore.**

Then friend `(12,26)` (target 5) arrives. Heap only contains chair2 (`dept 14`, still occupied) and chair0 (busy again). Since the earliest tracked departure (`14`) is still in the future, the buggy code assumes *no* chair is free and mints a "new" one via `pq.size()` — which evaluates to `2`. But chair `2` is **already occupied** (that friend doesn't leave until `14`) — this doesn't just skip the correct answer, it double-books a chair. The correct answer is chair `1`, sitting free and forgotten since step 2.

**Root cause:** popping is fine, but re-pushing is selective. The fix is the two-heap version above — every freed chair goes into `available`, not just the one this friend happens to take.

---

## 8. Edge Cases and Pitfalls

- Input isn't sorted by arrival — always sort indices first and map results back by original index, don't just process `times` in given order.
- Two friends can share a departure/arrival boundary (`dept == arrival`): the departing friend's chair counts as free for someone arriving at that exact instant — use `<=`, not `<`, when releasing chairs.
- `available` empty but `occupied` non-empty just means every existing chair is still in use — correctly falls through to minting `nextNewChair`, don't confuse this with "no friends seated yet."
- The one-heap "drain and keep only the min" shortcut in Section 7 is the single most common bug in this problem — if you see chair numbers that look plausible but are occasionally too high or collide, this is almost always why.

---

## 9. Complexity And Pattern

Pattern:

- greedy resource reuse — "release everything that's expired, then hand out the smallest free slot" — same family as interval scheduling / room booking problems.
- two heaps used for two different questions: "what frees up next" (by time) vs. "what's the smallest currently free" (by chair number) — don't try to collapse these into one heap without carefully re-pushing *everything* that gets popped.

Trigger words:
- "assign the smallest available/unoccupied slot/chair/room number"
- "arrival and departure times", "release resources when they expire"

---

## End of Notes
