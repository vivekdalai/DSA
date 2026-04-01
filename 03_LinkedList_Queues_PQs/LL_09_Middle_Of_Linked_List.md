# Linked List / Queue / Priority Queue Notes

## 09 - Middle of Linked List

**Generated on:** 2026-03-31 23:52:12 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the head of a singly linked list, return the middle node.

Example:
- `1 -> 2 -> 3 -> 4 -> 5` -> middle = `3`
- `1 -> 2 -> 3 -> 4 -> 5 -> 6` -> middle = `4` if the problem asks for the second middle

This is one of the most common warm-up linked list questions.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Use two pointers:
- `slow` moves by 1
- `fast` moves by 2

When `fast` reaches the end:
- `slow` is at the middle

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

- initialize `slow = head`, `fast = head`
- while `fast != null && fast.next != null`
  - move `slow = slow.next`
  - move `fast = fast.next.next`
- return `slow`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
    }
}

class MiddleOfLinkedList {
    public ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For `1 -> 2 -> 3 -> 4 -> 5`:
- start: `slow = 1`, `fast = 1`
- step 1: `slow = 2`, `fast = 3`
- step 2: `slow = 3`, `fast = 5`
- next step not possible, return `3`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This pattern appears in:
- middle of linked list
- splitting a list into halves
- merge sort on linked list
- palindrome linked list

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty list -> return `null`
- Single node -> return that node
- For even length, clarify whether first middle or second middle is required
- This template returns the second middle

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Slow-fast pointer is the default tool for finding the middle.
- When fast moves twice as quickly, slow lands in the middle.
- This technique is reused in many linked list problems.

------------------------------------------------------------------------

# End of Notes
