# Greedy Notes

## Task Scheduler

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/task-scheduler/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given tasks represented by capital letters and a non-negative cooling interval `n`.

Two same tasks must be separated by at least `n` units of time.

Return the minimum time required to finish all tasks.

**Example 1**

    Input: tasks = ["A","A","A","B","B","B"], n = 2
    Output: 8

One valid schedule is:

    A B idle A B idle A B

**Example 2**

    Input: tasks = ["A","C","A","B","D","B"], n = 1
    Output: 6

------------------------------------------------------------------------

## 2. Simulation Intuition

The file uses a max-heap plus a cooldown queue.

At each time unit:

- run the task type with highest remaining frequency
- if it still has copies left, push it into cooldown with its release time
- when a cooled task becomes available again, move it back to the heap

This directly simulates the greedy idea:

- always execute the most urgent available task

------------------------------------------------------------------------

## 3. Data Structures

- max-heap stores currently available task counts
- queue stores:

    [remainingCount, timeWhenAvailableAgain]

The clock `time` advances one step at a time.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    tasks = [A,A,A,B,B,B], n = 2

Counts:

    A -> 3
    B -> 3

Schedule by highest remaining count:

    A, B, idle, A, B, idle, A, B

Total time:

    8

------------------------------------------------------------------------

## 5. Clean Interview Version - Simulation

```java
public static int leastInterval(char[] tasks, int n) {
    int[] freq = new int[26];
    for (char c : tasks) freq[c - 'A']++;

    PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    for (int f : freq) if (f > 0) maxHeap.add(f);

    Queue<int[]> queue = new LinkedList<>();
    int time = 0;

    while (!maxHeap.isEmpty() || !queue.isEmpty()) {
        time++;

        if (!maxHeap.isEmpty()) {
            int remaining = maxHeap.poll() - 1;
            if (remaining > 0) {
                queue.add(new int[]{remaining, time + n});
            }
        }

        if (!queue.isEmpty() && queue.peek()[1] == time) {
            maxHeap.add(queue.poll()[0]);
        }
    }

    return time;
}
```

------------------------------------------------------------------------

## 6. Mathematical Greedy Version

This version avoids full simulation.

It is based on the task with the maximum frequency.

Suppose the most frequent task appears `maxFreq` times.

Example:

    A A A, n = 2

To place these `A`s safely:

    A _ _ A _ _ A

There are `maxFreq - 1` full gaps before the last `A`.

Each full gap has size:

    n + 1

So the base time is:

    (maxFreq - 1) * (n + 1)

Then we add the number of tasks that have frequency equal to `maxFreq`.

Why?

If both `A` and `B` have the same maximum frequency:

    A B _ A B _ A B

The last group contains both `A` and `B`, so each max-frequency task adds one slot at the end.

Code:

```java
class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];

        int maxFreq = 0;
        for (char ch : tasks) {
            freq[ch - 'A']++;
            maxFreq = Math.max(maxFreq, freq[ch - 'A']);
        }

        int cycle = n + 1;
        int time = (maxFreq - 1) * cycle;

        for (int f : freq) {
            if (f == maxFreq) {
                time++;
            }
        }

        return Math.max(time, tasks.length);
    }
}
```

Why return `Math.max(time, tasks.length)`?

- `time` counts the ideal cooldown-based frame.
- `tasks.length` counts the real number of tasks.
- If there are enough different tasks to fill all idle slots, the answer is simply `tasks.length`.

Example:

    tasks = [A,A,A,B,B,B], n = 2

Frequencies:

    A -> 3
    B -> 3

Calculation:

    maxFreq = 3
    cycle = 3
    time = (3 - 1) * 3 = 6
    A has maxFreq -> time = 7
    B has maxFreq -> time = 8

Answer:

    max(8, 6) = 8

------------------------------------------------------------------------

## 7. Complexity And Pattern

Simulation version:
- **Time:** `O(T log 26)` which is effectively `O(T)`
- **Space:** `O(26)`

Mathematical greedy version:
- **Time:** `O(T + 26)` which is effectively `O(T)`
- **Space:** `O(26)`

Pattern:

- greedy scheduling with cooldown
- max-heap for next best choice
- mathematical greedy using maximum frequency

------------------------------------------------------------------------

## End of Notes
