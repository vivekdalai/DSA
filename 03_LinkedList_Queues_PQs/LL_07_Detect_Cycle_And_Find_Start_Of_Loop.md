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

## 🔁 3. Why the Meeting Happens

Inside the loop:
- `slow` moves by 1 step
- `fast` moves by 2 steps
- so in every iteration, the distance between them decreases by 1
- because this distance keeps shrinking inside a finite cycle, it must eventually become 0
- once the distance becomes 0, `slow` and `fast` collide

Why resetting works for the loop start:

Let:
- `L` = distance from head to loop start
- `d` = distance from loop start to collision point
- `C` = cycle length

At collision:
- slow traveled `L + d`
- fast traveled `2(L + d)`

Difference is a multiple of cycle length:
- `2(L + d) - (L + d) = kC`
- so `L + d = kC`
- therefore `L = kC - d`

That means:
- one pointer from head
- one pointer from collision point
- both moving one step at a time
will meet at the loop start

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
