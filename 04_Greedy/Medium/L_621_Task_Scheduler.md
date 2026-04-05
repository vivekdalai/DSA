# Greedy Notes

## Task Scheduler

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

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

## 5. Clean Interview Version

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

## 6. Complexity And Pattern

- **Time:** `O(T log 26)` which is effectively `O(T)`
- **Space:** `O(26)`

Pattern:

- greedy scheduling with cooldown
- max-heap for next best choice

------------------------------------------------------------------------

## End of Notes
