# Linked List / Queue / Priority Queue Notes

## 06 - Intersection of Two Linked Lists

**Generated on:** 2026-03-31 19:23:20 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the heads of two singly linked lists, find the node where they intersect.

Important:
- Intersection means the two lists share the exact same node reference onward
- Same value alone does not mean intersection

Example:
- A: `1 -> 2 -> 3 -> 7 -> 8`
- B: `4 -> 5 -> 7 -> 8`

If both lists actually share the node `7`, then the answer is that node.

------------------------------------------------------------------------

## 🪜 2. Core Idea

If one list is longer than the other:
- advance the pointer of the longer list by the length difference
- then both pointers will have the same number of nodes left

Now move both one step at a time:
- the first node where `a == b` is the intersection

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

- Compute `lenA`
- Compute `lenB`
- Advance the longer list by `abs(lenA - lenB)`
- Traverse both together
- Compare node references
- Return the first common node, else `null`

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

class IntersectionLinkedLists {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        int lenA = getLength(headA);
        int lenB = getLength(headB);

        ListNode a = headA;
        ListNode b = headB;

        if (lenA > lenB) {
            for (int i = 0; i < lenA - lenB; i++) {
                a = a.next;
            }
        } else {
            for (int i = 0; i < lenB - lenA; i++) {
                b = b.next;
            }
        }

        while (a != null && b != null) {
            if (a == b) {
                return a;
            }
            a = a.next;
            b = b.next;
        }

        return null;
    }

    private int getLength(ListNode head) {
        int len = 0;
        while (head != null) {
            len++;
            head = head.next;
        }
        return len;
    }
}
```

Complexity:
- Time: O(n + m)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Suppose:
- List A length = 5
- List B length = 3

Structure:
- A: `1 -> 2 -> 3 -> 7 -> 8`
- B: `4 -> 5 -> 7 -> 8`

Steps:
- `lenA = 5`, `lenB = 3`
- Advance A by `2` steps
- Now:
  - A points at `3`
  - B points at `4`
- Move together:
  - `3` vs `4` -> not same
  - `7` vs `5` -> not same
  - `8` vs `7` in value-example format can be confusing

Better reference view:
- after alignment, both pointers are equally far from the tail
- the first time the node addresses match, return that node

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- two linked lists may merge into one tail
- the question asks for first common node
- extra space should be avoided

Related pattern:
- two-pointer switching trick also solves this in O(n + m) and O(1) space

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- One or both heads may be `null`
- Compare references using `a == b`, not values using `a.val == b.val`
- Length alignment is required before simultaneous traversal
- If there is no shared node, return `null`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Align both lists by remaining length.
- Then move both pointers together.
- Compare nodes by reference, not by value.
- First common reference is the intersection node.

------------------------------------------------------------------------

# End of Notes
