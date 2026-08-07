# Linked List / Queue / Priority Queue Notes

## 07 - Detect Cycle and Find Start of Loop in a Linked List

**Generated on:** 2026-03-31 19:23:46 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

This topic is usually asked in **two interview questions**:

### Question 1: Detect if a loop exists
Given the head of a linked list, return whether the list contains a cycle.

### Question 2: Find the start of the loop
If a cycle exists, return the node where the cycle begins. Otherwise return `null`.

Both are solved using Floyd’s Tortoise and Hare algorithm.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Use two pointers:
- `slow` moves one step at a time
- `fast` moves two steps at a time

If there is a cycle:
- `slow` and `fast` must eventually meet

Then:

- For **Question 1**, once they meet, return `true`
- For **Question 2**, after meeting:
  - reset `slow = head`
  - move both one step at a time
  - the node where they meet again is the start of the loop

------------------------------------------------------------------------

## 🔁 3. Why the Meeting Happens (Mathematical Derivation)

### 3.1 Setup

Let:
- `L` = distance (number of nodes) from `head` to the loop start
- `C` = length of the cycle (number of nodes in it), `C ≥ 1`
- `slow` moves 1 step/iteration, `fast` moves 2 steps/iteration

### 3.2 Proof that they are *guaranteed* to meet

**Step 1 — both pointers are inside the cycle after `L` steps of `slow`.**
`slow` reaches the loop start after exactly `L` steps. At that same moment `fast` has taken `2L` steps, and since `fast` is always at least as far along the list as `slow`, it has already entered the cycle. Measure position *from the loop start, mod `C`*:
```
pos_slow = 0
pos_fast = 2L mod C
```

**Step 2 — the gap changes by exactly 1 every iteration.**
Define `gap = (pos_fast - pos_slow) mod C`. Once both pointers are inside the cycle, each iteration:
```
pos_slow += 1   (mod C)
pos_fast += 2   (mod C)
=> gap  += 1    (mod C)
```
This is just the relative speed: `fast` gains on `slow` at a constant rate of `2 - 1 = 1` node per iteration. This is the precise meaning of "the distance between them shrinks by 1 each iteration" — really, the gap fast needs to close (going around the loop to catch slow from behind) decreases by 1 per step.

**Step 3 — a strictly-monotonic counter mod a finite `C` must hit 0.**
`gap` takes the values `gap₀, gap₀+1, gap₀+2, …` reduced mod `C`. Since `C` is finite, this sequence must pass through `0` within at most `C` steps (pigeonhole — you can't add 1 forever mod `C` without wrapping back to 0). When `gap = 0`, `pos_fast = pos_slow`, i.e. they are on the same node → **they collide.**

So the meeting isn't a coincidence — it's forced by two facts: (1) relative speed is a nonzero constant (1 node/iteration), and (2) the cycle is finite, so a constantly-changing position mod `C` cannot avoid returning to the starting offset. Total steps until collision is bounded by `L + C`.

### 3.3 Proof that resetting to `head` lands exactly on the loop start

At the moment of collision:
- `slow` has traveled `L + d`, where `d` = distance from loop start to the collision point (`0 ≤ d < C`)
- `fast` has traveled `2(L + d)` (it always moves exactly twice slow's step count)

The extra distance `fast` covered is entirely made up of whole laps of the cycle (that's the only way two same-speed-ratio walkers on a loop can coincide again):
```
2(L + d) - (L + d) = kC      for some integer k ≥ 1
L + d = kC
L = kC - d                    ... (*)
```

Now place `p1` at `head` and `p2` at the collision point, and move both one step at a time.

- `p1` needs exactly `L` steps to reach the loop start (by definition of `L`), and for all `t < L`, `p1` is still on the acyclic "tail" leading into the loop — it has **not** touched the cycle yet.
- `p2` is always inside the cycle. After `L` steps its position (measured from loop start, mod `C`) is:
```
d + L  (mod C)  =  d + (kC - d)  (mod C)      [substitute (*)]
               =  kC (mod C) = 0
```
  i.e. `p2` is back exactly at the loop start after `L` steps.

Since `p1` reaches the loop start for the first time at step `L`, and `p2` also lands on the loop start at step `L`, they meet there — and they **cannot** have met any earlier, because for `t < L`, `p1` isn't in the cycle at all while `p2` always is, so they occupy disjoint sets of nodes until `t = L`.

**Conclusion:** moving one pointer from `head` and one from the collision point, one step at a time, they meet for the first time exactly at the loop start.

------------------------------------------------------------------------

## 💻 4A. Question 1 - Detect if a Cycle Exists

```java
class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
    }
}

class LinkedListCycleDetection {
    public boolean hasCycle(ListNode head) {
        if (head == null || head.next == null) {
            return false;
        }

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return true;
            }
        }

        return false;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 💻 4B. Question 2 - Find the Start of the Loop

```java
class LinkedListCycleStart {
    public ListNode detectCycle(ListNode head) {
        if (head == null || head.next == null) {
            return null;
        }

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                slow = head;

                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }

                return slow;
            }
        }

        return null;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

List:
- `1 -> 2 -> 3 -> 4 -> 5`
- and `5.next = 3`

### For Question 1: cycle detection
- `slow` moves by 1
- `fast` moves by 2
- they eventually meet inside the cycle
- answer = `true`

### For Question 2: start of loop
- after collision, reset `slow = head`
- move both by 1
- they meet at node `3`

Answer:
- start of cycle = node `3`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use Floyd’s algorithm when:
- the structure is pointer-based
- you need cycle detection without extra memory
- the question asks for:
  - cycle existence
  - loop entry point
  - repeated-state detection in implicit graphs

Related problems:
- Linked List Cycle
- Linked List Cycle II
- Find Duplicate Number
- Happy Number

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty list -> no cycle
- Single node without self-loop -> no cycle
- Single node with `next` to itself -> cycle exists, start is that node
- Always check `fast != null && fast.next != null`
- For Question 1, return `true` immediately on collision
- For Question 2, do not return collision point directly; first reset `slow = head`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- This topic naturally splits into two interview questions.
- Same Floyd setup solves both.
- Collision answers cycle existence.
- Resetting one pointer to head gives the loop start.

------------------------------------------------------------------------

# End of Notes
