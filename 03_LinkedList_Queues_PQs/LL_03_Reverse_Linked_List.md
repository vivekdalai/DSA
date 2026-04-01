# Linked List / Queue / Priority Queue Notes

## 04 - Reverse a Linked List

**Generated on:** 2026-03-31 19:22:29 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the head of a singly linked list, reverse the list and return the new head.

Example:
- `1 -> 2 -> 3 -> 4`
- reversed = `4 -> 3 -> 2 -> 1`

This is one of the most standard linked list interview problems.

------------------------------------------------------------------------

## 🪜 2. Core Idea

There are two common approaches:

- Recursive:
  - reverse the rest of the list
  - attach current node at the end
- Iterative:
  - keep reversing one pointer at a time using `prev`, `curr`, and `nextNode`

The iterative method is the most interview-friendly because it uses `O(1)` extra space.

------------------------------------------------------------------------

## 🔁 3. Pointer Transition

At every step in the iterative approach:
- save `nextNode = curr.next`
- reverse link: `curr.next = prev`
- move `prev = curr`
- move `curr = nextNode`

At the end:
- `prev` becomes the new head

------------------------------------------------------------------------

## 💻 4A. Recursive Java Implementation

```java
class ListNode {
    int val;
    ListNode next;

    ListNode(int val) {
        this.val = val;
    }
}

class ReverseLinkedListRecursive {
    public ListNode reverseList(ListNode node) {
        if (node == null || node.next == null) {
            return node;
        }

        ListNode newHead = reverseList(node.next);
        node.next.next = node;
        node.next = null;
        return newHead;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n) recursion stack

------------------------------------------------------------------------

## 💻 4B. Iterative Java Implementation

```java
class ReverseLinkedListIterative {
    public ListNode reverseList(ListNode head) {
        ListNode curr = head;
        ListNode prev = null;

        while (curr != null) {
            ListNode nextNode = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextNode;
        }

        return prev;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For `1 -> 2 -> 3`:

Initial:
- `prev = null`
- `curr = 1`

Step 1:
- `nextNode = 2`
- `1.next = null`
- `prev = 1`
- `curr = 2`

Step 2:
- `nextNode = 3`
- `2.next = 1`
- `prev = 2`
- `curr = 3`

Step 3:
- `nextNode = null`
- `3.next = 2`
- `prev = 3`
- `curr = null`

Return:
- `3 -> 2 -> 1`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This pattern appears in:
- reverse linked list
- reverse sublist between positions `l` and `r`
- palindrome linked list
- reorder list
- k-group reversal

Trigger words:
- "reverse the list"
- "reverse links"
- "do it iteratively in-place"

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty list -> return `null`
- Single node -> return same node
- Always save `nextNode` before reversing the pointer
- In recursion, do not forget `node.next = null`
- Return `prev`, not the original `head`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Use `prev`, `curr`, `nextNode` for in-place reversal.
- Iterative solution is the cleanest and uses `O(1)` extra space.
- Recursive version is elegant but uses call stack space.
- After reversal, `prev` is the new head.

------------------------------------------------------------------------

# End of Notes
