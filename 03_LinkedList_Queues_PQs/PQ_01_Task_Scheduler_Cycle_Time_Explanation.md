# Priority Queue Notes

## 01 - Task Scheduler: Why Full Cycle Time Matters

Problem: LeetCode 621 - Task Scheduler

---

## 1. Context

In the priority queue solution, we use a max-heap to always pick the task with the highest remaining frequency.

For each round, we try to execute at most:

```text
n + 1
```

tasks.

Why `n + 1`?

If the cooldown is `n`, then after running task `A`, we need at least `n` other time slots before running `A` again.

Example when `n = 2`:

```text
A _ _ A
```

So each cycle has size:

```text
n + 1 = 3
```

---

## 2. Code Pattern

```java
class Solution {
    public int leastInterval(char[] tasks, int n) {
        int[] freq = new int[26];

        for (char ch : tasks) {
            freq[ch - 'A']++;
        }

        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());

        for (int i = 0; i < 26; i++) {
            if (freq[i] > 0) {
                pq.add(freq[i]);
            }
        }

        int totalTime = 0;

        while (!pq.isEmpty()) {
            int cycle = n + 1;
            List<Integer> store = new ArrayList<>();
            int taskCount = 0;

            while (cycle-- > 0 && !pq.isEmpty()) {
                int currFreq = pq.poll();

                if (currFreq > 1) {
                    store.add(currFreq - 1);
                }

                taskCount++;
            }

            for (int remainingFreq : store) {
                pq.add(remainingFreq);
            }

            totalTime += pq.isEmpty() ? taskCount : n + 1;
        }

        return totalTime;
    }
}
```

---

## 3. Meaning Of `taskCount`

`taskCount` counts only the number of real tasks executed in the current cycle.

It does not count idle CPU slots.

This line:

```java
taskCount++;
```

is inside this loop:

```java
while (cycle-- > 0 && !pq.isEmpty())
```

So `taskCount` increases only when the priority queue has a task available and we execute it.

If the CPU is idle, no task is polled from the heap, so `taskCount` does not increase.

---

## 4. Why `totalTime += taskCount` Is Wrong

Using only:

```java
totalTime += taskCount;
```

is wrong because it ignores idle time.

Example:

```text
tasks = [A,A,A], n = 2
```

Correct schedule:

```text
A idle idle A idle idle A
```

Correct answer:

```text
7
```

Now see what happens in the first cycle.

Cycle size:

```text
n + 1 = 3
```

We execute:

```text
A
```

Then there are still more `A` tasks left, but we cannot execute them immediately because of cooldown.

So the real cycle is:

```text
A idle idle
```

Actual time used:

```text
3
```

But `taskCount` is only:

```text
1
```

because only one real task was executed.

So if we do:

```java
totalTime += taskCount;
```

we add only `1`, which wrongly treats the schedule like:

```text
A A A
```

That violates the cooldown rule.

---

## 5. Why This Line Is Correct

```java
totalTime += pq.isEmpty() ? taskCount : n + 1;
```

There are two cases.

### Case 1: Priority Queue Is Not Empty

```java
pq.isEmpty() == false
```

This means more tasks are still left after the current cycle.

So we must count the full cycle length:

```text
n + 1
```

because any unused slots inside the cycle are idle slots.

Example:

```text
A idle idle
```

Even though only one task ran, the CPU still spent 3 units of time.

### Case 2: Priority Queue Is Empty

```java
pq.isEmpty() == true
```

This means all tasks are finished after the current cycle.

Now we should add only:

```text
taskCount
```

because we do not need idle time after all tasks are done.

Example:

```text
A B
```

If these are the last tasks, we do not need to add extra idle slots at the end.

---

## 6. Dry Run

Input:

```text
tasks = [A,A,A], n = 2
```

Frequencies:

```text
A -> 3
```

Cycle size:

```text
n + 1 = 3
```

| Cycle | Executed tasks | Actual slots | Tasks left after cycle | Time added |
|---:|---|---|---:|---:|
| 1 | `A` | `A idle idle` | 2 | 3 |
| 2 | `A` | `A idle idle` | 1 | 3 |
| 3 | `A` | `A` | 0 | 1 |

Total:

```text
3 + 3 + 1 = 7
```

---

## 7. Mathematical Greedy Solution

There is also a direct formula solution.

Instead of simulating cycles with a priority queue, we build the answer around the most frequent task.

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

Formula:

```text
time = (maxFreq - 1) * (n + 1) + countOfMaxFreqTasks
```

Why `maxFreq - 1`?

The most frequent task creates the main structure.

Example:

```text
A A A, n = 2
```

Arrange the `A`s first:

```text
A _ _ A _ _ A
```

There are only `maxFreq - 1` full cycles before the last `A`.

The last `A` is handled separately by counting tasks whose frequency is equal to `maxFreq`.

If both `A` and `B` have max frequency:

```text
A B _ A B _ A B
```

Both `A` and `B` appear in the final group, so both add one extra slot.

Why `Math.max(time, tasks.length)`?

- `time` is the minimum time forced by cooldown gaps.
- `tasks.length` is the minimum time needed to execute all real tasks.
- If other tasks fill all idle gaps, the answer becomes just `tasks.length`.

---

## 8. Key Takeaway

`taskCount` means:

```text
number of actual tasks executed in this cycle
```

`n + 1` means:

```text
full cycle length, including idle slots
```

Therefore:

```java
totalTime += pq.isEmpty() ? taskCount : n + 1;
```

means:

- If tasks are still left, count the full cycle including idle time.
- If no tasks are left, count only the tasks executed in the final cycle.

---

## 9. Common Mistake

Do not write:

```java
totalTime += taskCount;
```

That counts only completed tasks and misses idle CPU slots.

The CPU timeline may contain both:

```text
task slots + idle slots
```

The final answer asks for total time, so idle slots must be counted whenever they are needed between remaining tasks.

---

# End of Notes
