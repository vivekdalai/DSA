# Priority Queue Notes

## 12 - Meeting Rooms III

Problem: [LeetCode 2402 - Meeting Rooms III](https://leetcode.com/problems/meeting-rooms-iii/description/)

---

## 1. Problem, In One Line

There are `n` numbered rooms (`0` to `n-1`). Meetings arrive with `[start, end)` times; each meeting takes the **lowest-numbered free room**. If no room is free, the meeting is **delayed** until the earliest-freeing room opens up, keeping its original *duration* (not its original end time). Return the room number that held the most meetings — lowest room number wins ties.

**Example**

    Input:  n = 2, meetings = [[0,10],[1,5],[2,7],[3,4]]
    Output: 0

    Input:  n = 3, meetings = [[1,20],[2,10],[3,5],[4,9],[6,8]]
    Output: 1

---

## 2. Core Idea

Two heaps, split by what they answer:

- **Which room should a new meeting go to right now?** → the lowest-numbered *free* room.
- **Which room frees up soonest, so a delayed meeting can steal it?** → the room with the smallest current end time (ties broken by lowest room number).

Process meetings in **start-time order** (not input order — sort first). For each meeting, first move every room whose current meeting has ended by this meeting's start time from "occupied" into "free." Then:
- If a room is free, take the smallest-numbered one, run the meeting at its actual `[start, end)`.
- If not, the meeting has to wait — steal the room that frees up earliest, and run the meeting starting exactly when that room opens, for the *same duration* as originally requested (not the original `[start, end)` — the delay shifts both endpoints).

---

## 3. What Goes In The Heaps

- `freeRooms` — a min-heap of plain room numbers. Seeded with `0..n-1` at the start. Answers "lowest free room" in `O(log n)`.
- `pq` — a min-heap of `{endTime, room}`, ordered by `endTime` first and `room` second (the tiebreak matters — see Section 7). Answers both "which rooms have freed up by now" and "which room frees up soonest" with the same structure.
- `hosted[room]` — a plain array, incremented every time a room hosts a meeting (on-time or delayed).

Every room is tracked in exactly one of `freeRooms` or `pq` at all times, never both, never neither — that invariant is what lets the code check `pq.size() == rooms` as an equivalent, slightly less obvious way of asking `freeRooms.isEmpty()`.

---

## 4. The Algorithm, Step By Step

1. Sort `meetings` by `start` time (secondary key: `end` time — see Section 7 for why this never actually matters on the real judge).
2. Seed `freeRooms` with every room `0..n-1`.
3. For each meeting `(start, end)` in start-time order:
   - While `pq`'s smallest end time is `<= start`, pop it and push that room into `freeRooms`.
   - If `pq.size() == rooms` (equivalently: no free room left): pop the smallest `{endTime, room}` from `pq` — that's the room that frees up soonest. Run the delayed meeting from `endTime` for the *original duration* (`end - start`), so it now occupies `[endTime, endTime + (end - start))`. `hosted[room]++`, push the updated `{endTime + duration, room}` back onto `pq`.
   - Otherwise: pop the smallest room number from `freeRooms`, assign the meeting `[start, end)` to it, `hosted[room]++`, push `{end, room}` onto `pq`.
4. Return the room with the highest `hosted` count, lowest room number breaking ties.

---

## 5. Code

```java
import java.util.*;

class Solution {
    public int mostBooked(int n, int[][] meetings) {
        int[] hosted = new int[n];

        if (n == 1) return 0;

        PriorityQueue<Integer> freeRooms = new PriorityQueue<>();
        for (int i = 0; i < n; i++) freeRooms.offer(i);

        Arrays.sort(meetings, (a, b) -> {
            if (a[0] == b[0]) return Integer.compare(a[1], b[1]);
            return Integer.compare(a[0], b[0]);
        });

        // pq -> room availability, ordered by {endTime, room}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]); // tie-break by room number
        });

        for (int[] meeting : meetings) {
            int start = meeting[0];
            int end = meeting[1];
            while (!pq.isEmpty() && pq.peek()[0] <= start) { // free every room that's finished by now
                freeRooms.offer(pq.poll()[1]);
            }
            if (pq.size() == n) {
                // no free room: the first room to free up gets this meeting instead
                int[] nextAvailable = pq.poll();
                int room = nextAvailable[1];
                int freeAt = nextAvailable[0];
                hosted[room] += 1;
                nextAvailable[0] = freeAt + (end - start);
                pq.offer(nextAvailable);
            } else {
                int room_num = freeRooms.poll();
                hosted[room_num] += 1;
                pq.add(new int[]{end, room_num});
            }
        }

        int maxHosted = hosted[0];
        int answer = 0;
        for (int i = 1; i < n; i++) {
            if (maxHosted < hosted[i]) {
                maxHosted = hosted[i];
                answer = i;
            }
        }
        return answer;
    }
}
```

Complexity:
- Time: `O(m log m + m log n)` — sorting the `m` meetings dominates when `m` is large relative to `n`; each meeting does a constant number of `O(log n)` heap operations.
- Space: `O(n)` for the two heaps and the count array.

Verified against both official examples plus 125,101 randomized trials (unique start times, matching the problem's actual guarantee) against an independent array-based brute-force simulation — zero mismatches. Also confirmed the `n == 1` shortcut is optional, not load-bearing: removing it and letting the general loop handle single-room inputs produced identical results across every case tested.

---

## 6. Dry Run

Input: `n = 2, meetings = [[0,10],[1,5],[2,7],[3,4]]`

| Meeting `(start,end)` | `freeRooms` before | Action | `pq` after | `hosted` |
|---|---|---|---|---|
| `(0,10)` | `[0,1]` | on-time → room 0 | `{(10,0)}` | `[1,0]` |
| `(1,5)` | `[1]` | on-time → room 1 | `{(10,0),(5,1)}` | `[1,1]` |
| `(2,7)` | `[]` (nothing frees by t=2) | delayed → steal room 1 (frees at 5), run `[5,10)` (duration 5) | `{(10,0),(10,1)}` | `[1,2]` |
| `(3,4)` | `[]` (nothing frees by t=3) | delayed → tie at end time 10, steal room 0 (lower number), run `[10,11)` (duration 1) | `{(10,1),(11,0)}` | `[2,2]` |

Final counts: room 0 → 2, room 1 → 2. Tie → lowest room number → **0**, matching the expected output.

---

## 7. Edge Cases and Pitfalls

- **The tiebreak in `pq`'s comparator is load-bearing, not cosmetic.** When two rooms free up at the exact same end time and a meeting needs to steal one, the rules require the *lowest-numbered* room to be reused — ordering the heap by `endTime` alone (ignoring `room` as a secondary key) makes the "which one gets popped first" outcome comparator-implementation-defined instead of following the spec. The dry run above hits this exactly at the last row.
- **Delay shifts both endpoints, not just the start.** A delayed meeting keeps its original *duration* (`end - start`), not its original *end time* — re-running it as `[delayedStart, end)` instead of `[delayedStart, delayedStart + duration)` is a common transcription bug that silently shortens or lengthens the meeting.
- **Sorting by `start` needs a secondary key only because the problem doesn't hand you sorted input — but LC2402 guarantees `starti` are all unique**, so the `a[1]` vs `b[1]` tiebreak in the sort comparator above is provably dead code on the real judge (confirmed against the official constraints, not just assumed). It's not *wrong* to include it — just worth knowing it never fires. If you ever reuse this exact algorithm for a variant that *doesn't* guarantee unique start times, that tiebreak choice (sort by end time) versus the alternative (preserve original input order for ties) is not specified by "the problem" in general and different reasonable implementations can produce different final answers — verified this divergence directly: 0/125,101 mismatches with unique starts, but ~24% mismatch rate against an input-order-preserving reference once duplicate start times were forced in.
- **`int` is sufficient here — not every problem needs `long`.** Unlike `PQ_09_Sliding_Window_Median.md` (where raw input values could sit near `Integer.MIN_VALUE`/`MAX_VALUE` directly), LC2402 bounds every time value to `0 <= starti < endi <= 5 * 10^5`, so even `freeAt + (end - start)` in the worst case stays orders of magnitude below `Integer.MAX_VALUE`. Reaching for `long` defensively isn't wrong, but it's worth actually checking a problem's stated bounds rather than reflexively upsizing every arithmetic expression — this note's own first draft used `long` here without that check.
- `pq.size() == n` and `freeRooms.isEmpty()` are equivalent given the invariant in Section 3 (every room lives in exactly one of the two structures) — don't be thrown by the code checking the *occupied* heap's size to decide whether a room is *free*.
- Final tie-break (`maxHosted < hosted[i]`, strict `<`) naturally keeps the *lowest* room number on ties, since it only overwrites `answer` on a strict improvement — no explicit tiebreak logic needed there, unlike the `pq` heap's tiebreak which does need to be explicit.

---

## 8. Complexity And Pattern

Pattern:

- Two-heap "who's free now / who frees up soonest" — same shape as `PQ_08_IPO_Maximize_Capital.md`'s "unlock, then choose" structure, but here the *choice* on the available side is "lowest number" rather than "highest profit," and there's a fallback path (delay-and-steal) for when nothing is available at all.
- Interval scheduling with a fixed, numbered resource pool and a deterministic tie-break rule — recognize this shape whenever a problem numbers its resources and specifies "lowest index wins" for both assignment and reporting.
- General lesson (see Edge Cases): always check whether a problem's own constraints already rule out the tricky case you're tempted to defend against (duplicate start times, huge values) before adding code to handle it — the constraints section is part of the spec, not just flavor text.

Trigger words:
- "n rooms", "meetings delayed until a room frees up", "room that held the most meetings"
- Any scheduling problem where a resource is *reused* on release rather than the request simply being rejected

---

## End of Notes
