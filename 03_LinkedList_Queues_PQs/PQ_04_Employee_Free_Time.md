# Priority Queue Notes

## 04 - Employee Free Time

Problem: LeetCode 759 - Employee Free Time

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/employee-free-time/description/
<!-- leetcode-link-end -->

---

## 1. LeetCode Question Statement

We are given a list `schedule` of employees, which represents the working time for each employee.

Each employee has a list of non-overlapping `Interval`s, and these intervals are in sorted order.

Return the list of finite intervals representing **common, positive-length free time** for all employees, also in sorted order. (Even though we are representing `Interval`s in the form `[x, y]`, the objects inside are `Interval`s, not lists or arrays.)

**Example 1**

    Input: schedule = [[[1,2],[5,6]],[[1,3]],[[4,10]]]
    Output: [[3,4]]

Explanation: There are a total of three employees, and all common free time intervals would be `[-inf, 1]`, `[3, 4]`, `[10, inf]`. We discard any intervals that contain `inf` as they aren't finite.

---

## 2. Insight

Free time for *everyone* only exists in the gaps left over after every employee's busy intervals are merged together.

So the trick is:

1. Flatten every employee's intervals into one big list — ownership of an interval no longer matters, only where it sits on the timeline.
2. Sort that flattened list by start time (breaking ties by end time).
3. Sweep left to right, merging overlapping or touching intervals exactly like the classic **Merge Intervals** problem.
4. Every time the sweep finds a gap — i.e. the next interval starts *after* the current merged block ends — that gap is free time shared by all employees, because nobody's interval occupies it.

This reduces "free time across N employees" to "merge intervals, then read off the gaps," the same greedy sweep used in `L_435_Non_Overlapping_Intervals` and `L_452_Minimum_Number_Of_Arrows_To_Burst_Balloons`.

---

## 3. File Logic

- Flatten `schedule` into `List<int[]>`, each entry `{start, end}`.
- Sort by `start` ascending, tie-broken by `end` ascending.
- Walk the sorted list keeping `prev` = the current merged block:
  - if `prev[1] >= curr[0]` the intervals touch/overlap -> merge by extending `prev[1] = Math.max(prev[1], curr[1])`.
  - otherwise there is a real gap `[prev[1], curr[0]]` -> that is a free-time interval, so add it to `ans`, then start a new merged block at `curr`.

Note: this assumes `schedule` (and therefore the flattened `intervals`) is non-empty, since the code reads `intervals.get(0)` before the loop starts — that matches LeetCode's constraints for this problem (each employee has at least one interval).

---

## 4. Dry Run

Input:

    schedule = [[[1,2],[5,6]], [[1,3]], [[4,10]]]

Flatten:

    [1,2], [5,6], [1,3], [4,10]

Sort by start (tie-break by end):

    [1,2], [1,3], [4,10], [5,6]

Sweep, `prev = [1,2]`:

| i | curr | `prev[1] >= curr[0]`? | Action | `prev` after |
|---|---|---|---|---|
| 1 | `[1,3]` | `2 >= 1` -> yes | merge: `prev[1] = max(2,3) = 3` | `[1,3]` |
| 2 | `[4,10]` | `3 >= 4` -> no | gap found: `ans.add([3,4])`, `prev = curr` | `[4,10]` |
| 3 | `[5,6]` | `10 >= 5` -> yes | merge: `prev[1] = max(10,6) = 10` | `[4,10]` |

Result:

    ans = [[3, 4]]

which matches the expected output.

---

## 5. Approach 1: Sort + Merge Sweep (Clean Interview Version)

```java
import java.util.*;

class Solution {
    public static List<Interval> employeeFreeTime(List<List<Interval>> schedule) {
        List<int[]> intervals = new ArrayList<>();
        for (List<Interval> sch : schedule) {
            for (Interval interval : sch) {
                intervals.add(new int[]{interval.start, interval.end});
            }
        }

        // sort by start, tie-break by end
        intervals.sort((a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(a[0], b[0]);
        });

        List<Interval> ans = new ArrayList<>();
        int[] prev = intervals.get(0);

        for (int i = 1; i < intervals.size(); i++) {
            int[] curr = intervals.get(i);
            if (prev[1] >= curr[0]) {
                // merge
                prev[1] = Math.max(prev[1], curr[1]);
            } else {
                ans.add(new Interval(prev[1], curr[0]));
                prev = curr;
            }
        }

        return ans;
    }
}
```

---

## 6. Approach 2: Min-Heap K-Way Merge

Approach 1 flattens and fully sorts *every* interval up front — `O(n log n)`.

But each employee's own list is **already sorted**. That's exactly the setup for a **k-way merge**: instead of sorting everything, keep one "pointer" per employee and repeatedly pull the globally-smallest next interval using a min-heap of size `k` (number of employees), the same idea as merging `k` sorted linked lists — see `PQ_03_Merge_K_Sorted_Arrays`.

Idea:

- The heap holds `[employeeIdx, intervalIdx]` pairs, ordered by that interval's `start` time — never the raw intervals themselves, so the heap always knows *whose* interval it is looking at and can advance that employee's pointer later.
- `prevEnd` tracks the end of the merged block seen *so far* (the running frontier), playing the same role `prev[1]` played in Approach 1 — except here it's updated incrementally, one popped interval at a time, instead of via an explicit merge step.
- Pop the interval with the smallest start:
  - if it starts after `prevEnd`, everyone was free in between -> record `[prevEnd, interval.start]`.
  - either way, advance `prevEnd = max(prevEnd, interval.end)`.
  - push that employee's *next* interval (if any) back onto the heap, so the heap always holds at most one candidate per still-active employee.
- Stop when the heap is empty — every employee's intervals have been consumed.

This never materializes the flattened, fully-sorted list that Approach 1 builds; it only ever holds `k` candidates at a time.

---

## 7. Approach 2: Code

```java
import java.util.*;

class Solution {
    public static List<Interval> employeeFreeTime(List<List<Interval>> schedule) {
        // Min-heap stores [employee_index, interval_index]
        // Sorted by the start time of the interval
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b) -> 
            schedule.get(a[0]).get(a[1]).start - schedule.get(b[0]).get(b[1]).start
        );

        // Initialize the heap with the first interval from each employee
        for (int i = 0; i < schedule.size(); i++) {
            if (schedule.get(i) != null && !schedule.get(i).isEmpty()) {
                minHeap.offer(new int[]{i, 0});
            }
        }

        List<Interval> ans = new ArrayList<>();
        if (minHeap.isEmpty()) return ans;

        // Tracks the end time of the last processed interval to find gaps
        int[] first = minHeap.peek();
        int prevEnd = schedule.get(first[0]).get(first[1]).end;

        while (!minHeap.isEmpty()) {
            int[] curr = minHeap.poll();
            int employeeIdx = curr[0];
            int intervalIdx = curr[1];
            Interval interval = schedule.get(employeeIdx).get(intervalIdx);

            // If the current interval starts after the previous end, we found free time
            if (interval.start > prevEnd) {
                ans.add(new Interval(prevEnd, interval.start));
            }

            // Update the tracked end time to the furthest point seen so far
            prevEnd = Math.max(prevEnd, interval.end);

            // Move to the next interval for this specific employee
            if (intervalIdx + 1 < schedule.get(employeeIdx).size()) {
                minHeap.offer(new int[]{employeeIdx, intervalIdx + 1});
            }
        }

        return ans;
    }
}
```

Note: `new PriorityQueue<>((a, b) -> schedule.get(a[0]).get(a[1]).start - schedule.get(b[0]).get(b[1]).start)` subtracts raw `int`s for the comparator instead of `Integer.compare(...)`. That's fine here since `Interval.start` values are small/bounded LeetCode inputs, but it can silently overflow for arbitrary `int` inputs near `Integer.MIN_VALUE`/`MAX_VALUE` — `Integer.compare` is the safer habit.

---

## 8. Approach 2: Dry Run

Same input:

    schedule = [ [[1,2],[5,6]],   // employee 0
                 [[1,3]],         // employee 1
                 [[4,10]] ]       // employee 2

Initial heap seeded with each employee's first interval: `(0,0)->[1,2]`, `(1,0)->[1,3]`, `(2,0)->[4,10]`.

`peek()` breaks the start=1 tie toward `(0,0)` here, so `prevEnd = 2` initially.

| Popped | Interval | `start > prevEnd`? | Action | `prevEnd` after | Pushed next |
|---|---|---|---|---|---|
| `(0,0)` | `[1,2]` | `1 > 2` -> no | no gap | `max(2,2) = 2` | `(0,1)` -> `[5,6]` |
| `(1,0)` | `[1,3]` | `1 > 2` -> no | no gap | `max(2,3) = 3` | employee 1 exhausted |
| `(2,0)` | `[4,10]` | `4 > 3` -> **yes** | `ans.add([3,4])` | `max(3,10) = 10` | employee 2 exhausted |
| `(0,1)` | `[5,6]` | `5 > 10` -> no | no gap | `max(10,6) = 10` | employee 0 exhausted |

Heap empty -> loop ends.

Result:

    ans = [[3, 4]]

Same answer as Approach 1, reached without ever fully sorting the flattened interval list.

---

## 9. Complexity And Pattern (Both Approaches)

**Approach 1 — sort + merge sweep**

- **Time:** `O(n log n)`, where `n` is the total number of intervals across all employees — dominated by sorting the flattened list.
- **Space:** `O(n)` for the flattened list and the answer list.

**Approach 2 — min-heap k-way merge**

- **Time:** `O(n log k)`, where `k` is the number of employees — each of the `n` intervals is pushed and popped from a heap of size at most `k`. This beats Approach 1 whenever `k << n` (few employees, each with a long schedule).
- **Space:** `O(k)` for the heap, plus `O(n)` for the answer in the worst case — no need to ever hold all `n` intervals flattened together.

Pattern:

- flatten multiple sorted lists into one, when ownership doesn't matter, only position on the timeline
- gaps *between* merged blocks are the actual answer, not the merged blocks themselves — the inverse of the usual "merge intervals" output
- when each source list is already sorted, prefer a **k-way merge via min-heap** over sorting everything from scratch — same trade-off as "merge k sorted lists"

---

## End of Notes
