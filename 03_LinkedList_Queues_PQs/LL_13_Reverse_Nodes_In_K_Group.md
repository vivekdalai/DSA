# Linked List / Queue / Priority Queue Notes

## 13 - Reverse Nodes in K Group

**Generated on:** 2026-03-31 23:56:44 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the head of a linked list and an integer `k`, reverse the nodes of the list `k` at a time.

Rules:
- reverse every complete group of size `k`
- if the remaining nodes are fewer than `k`, leave them as they are

Example:
- `1 -> 2 -> 3 -> 4 -> 5`, `k = 2`
- result = `2 -> 1 -> 4 -> 3 -> 5`

Example:
- `1 -> 2 -> 3 -> 4 -> 5`, `k = 3`
- result = `3 -> 2 -> 1 -> 4 -> 5`

This is a very common medium-hard linked list interview problem.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Process the list group by group.

For each group:
1. check whether `k` nodes are available
2. if not, stop
3. reverse exactly those `k` nodes
4. connect:
   - previous group tail -> new group head
   - reversed group tail -> next group start

A dummy node makes reconnection easier.

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

Maintain:
- `groupPrev` = node before current group
- `kth` = kth node from `groupPrev`
- `groupNext` = node after current group

For each valid group:
- reverse nodes from `groupPrev.next` up to `kth`
- connect reversed part back into the list
- move `groupPrev` to the tail of the reversed group

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

class ReverseNodesInKGroup {
    public ListNode reverseKGroup(ListNode head, int k) {
        if (head == null || k <= 1) {
            return head;
        }

        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode groupPrev = dummy;

        while (true) {
            ListNode kth = getKthNode(groupPrev, k);
            if (kth == null) {
                break;
            }

            ListNode groupNext = kth.next;
            ListNode prev = groupNext;
            ListNode curr = groupPrev.next;

            while (curr != groupNext) {
                ListNode nextNode = curr.next;
                curr.next = prev;
                prev = curr;
                curr = nextNode;
            }

            ListNode newGroupHead = kth;
            ListNode newGroupTail = groupPrev.next;

            groupPrev.next = newGroupHead;
            groupPrev = newGroupTail;
        }

        return dummy.next;
    }

    private ListNode getKthNode(ListNode start, int k) {
        while (start != null && k > 0) {
            start = start.next;
            k--;
        }
        return start;
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
- `k = 2`

First group:
- nodes = `1, 2`
- reverse -> `2 -> 1`
- list becomes: `2 -> 1 -> 3 -> 4 -> 5`

Second group:
- nodes = `3, 4`
- reverse -> `4 -> 3`
- list becomes: `2 -> 1 -> 4 -> 3 -> 5`

Last group:
- only `5` remains
- size < `k`, so leave it unchanged

Answer:
- `2 -> 1 -> 4 -> 3 -> 5`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This problem combines:
- reversing a linked list
- checking fixed-size groups
- reconnecting sublists carefully

Related problems:
- reverse linked list
- reverse linked list II
- swap nodes in pairs
- rotate list

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- `k = 1` -> no change
- Empty list -> no change
- Remaining nodes fewer than `k` must stay in original order
- Always save `groupNext` before reversal
- Reconnection mistakes are the most common bug here

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Work one complete group at a time.
- First verify that `k` nodes exist.
- Reverse the group in-place.
- Reconnect previous tail, reversed group, and next section carefully.

------------------------------------------------------------------------

# End of Notes
