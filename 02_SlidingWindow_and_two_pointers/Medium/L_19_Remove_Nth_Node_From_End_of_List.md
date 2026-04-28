# Two Pointers and Sliding Window Notes

## 19 - Remove Nth Node From End of List

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/remove-nth-node-from-end-of-list/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given the `head` of a linked list, remove the `nth` node from the end of the list and return its head.

**Example 1:**

![Official LeetCode diagram](https://assets.leetcode.com/uploads/2020/10/03/remove_ex1.jpg)

```text
Input: head = [1,2,3,4,5], n = 2
Output: [1,2,3,5]
```

**Example 2:**

```text
Input: head = [1], n = 1
Output: []
```

**Example 3:**

```text
Input: head = [1,2], n = 1
Output: [1]
```

**Constraints:**

- The number of nodes in the list is `sz`.

- `1 <= sz <= 30`

- `0 <= Node.val <= 100`

- `1 <= n <= sz`

**Follow up:** Could you do this in one pass?

------------------------------------------------------------------------

## 2. First Intuition

If the fast pointer starts `n` nodes ahead of the slow pointer, then when fast reaches the end, slow is just before the node that must be removed.

------------------------------------------------------------------------

## 3. Fast Pointer Gap

- The file first moves `fast` forward `n` steps.
- If `fast` becomes `null`, the head itself is the node to delete.
- Otherwise, move `fast` and `slow` together until `fast.next` is `null`.
- At that point, `slow.next` is the target node, so bypass it.

------------------------------------------------------------------------

## 4. Short Dry Run

For `1 -> 2 -> 3 -> 4 -> 5`, `n = 2`: move fast to node `3`; then move both until fast is at `5`. Slow is at `3`, so delete `4`, producing `1 -> 2 -> 3 -> 5`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public ListNode removeNthFromEnd(ListNode head, int n) {
    ListNode dummy = new ListNode(0);
    dummy.next = head;

    ListNode fast = dummy;
    ListNode slow = dummy;

    for (int i = 0; i < n; i++) {
        fast = fast.next;
    }

    while (fast.next != null) {
        fast = fast.next;
        slow = slow.next;
    }

    slow.next = slow.next.next;
    return dummy.next;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(length)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Linked-list nth-from-end problems usually use a fixed pointer gap.
- A dummy node removes the special case for deleting the head.

------------------------------------------------------------------------

## End of Notes
