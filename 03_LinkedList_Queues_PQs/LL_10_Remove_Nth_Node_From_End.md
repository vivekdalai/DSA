# Linked List / Queue / Priority Queue Notes

## 10 - Remove Nth Node From End of List

**Generated on:** 2026-03-31 23:52:37 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the head of a linked list and an integer `n`, remove the `n`th node from the end of the list.

Example:
- `1 -> 2 -> 3 -> 4 -> 5`, `n = 2`
- remove `4`
- result = `1 -> 2 -> 3 -> 5`

This is a very common two-pointer linked list question.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Use two pointers with a fixed gap of `n` nodes.

This version keeps:
- `slow = head`
- `fast = head`

Steps:
- first move `fast` ahead by `n` steps
- if `fast == null`, that means the head itself must be deleted
- otherwise move both pointers together until `fast.next == null`
- then `slow.next` is the node to remove

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

- place both `slow` and `fast` at `head`
- move `fast` ahead by `n` steps
- if `fast == null`, return `head.next`
- move both `slow` and `fast` until `fast.next == null`
- remove node using `slow.next = slow.next.next`
- return `head`

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

class RemoveNthFromEnd {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        ListNode slow = head;
        ListNode fast = head;

        for (int i = 0; i < n; i++) {
            fast = fast.next;
        }

        if (fast == null) {
            return head.next;
        }

        while (fast.next != null) {
            slow = slow.next;
            fast = fast.next;
        }

        slow.next = slow.next.next;
        return head;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For:
- `1 -> 2 -> 3 -> 4 -> 5`
- `n = 2`

Move `fast` by `2` steps:
- `slow = 1`
- `fast = 3`

Move both until `fast.next == null`:
- step 1 -> `slow = 2`, `fast = 4`
- step 2 -> `slow = 3`, `fast = 5`

Now:
- `slow.next = 4`
- remove it using `slow.next = slow.next.next`

Result:
- `1 -> 2 -> 3 -> 5`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this pattern when:
- question asks for kth/nth node from end
- deletion must be done in one pass
- you need predecessor of a target node

Related problems:
- find kth node from end
- remove middle node
- partition list

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- If `n` equals list length, head gets deleted
- In the head-start version, you must explicitly handle `fast == null`
- Be careful whether the loop condition should be `fast != null` or `fast.next != null`
- If constraints are invalid, add safety checks as needed

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Keep both pointers at `head`.
- Move `fast` ahead by `n` steps.
- If `fast` becomes `null`, delete the head.
- Otherwise move both together until `fast.next == null`.
- Then delete `slow.next`.

------------------------------------------------------------------------

# End of Notes
