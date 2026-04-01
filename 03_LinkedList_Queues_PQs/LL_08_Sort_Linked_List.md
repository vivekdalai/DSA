# Linked List / Queue / Priority Queue Notes

## 08 - Sort a Linked List

**Generated on:** 2026-03-31 19:24:09 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the head of a linked list, sort it in ascending order.

Example:
- `4 -> 2 -> 1 -> 3`
- result = `1 -> 2 -> 3 -> 4`

For linked lists, merge sort is the standard optimal solution because:
- finding the middle is easy with slow/fast pointers
- merging two sorted lists is linear

------------------------------------------------------------------------

## 🪜 2. Core Idea

Use merge sort:

1. Find the middle of the list
2. Split the list into two halves
3. Recursively sort both halves
4. Merge the two sorted halves

Key detail:
- while finding the middle, use `fast = head.next`
- this ensures a clean split for small lists like `[1, 3]`

------------------------------------------------------------------------

## 🔁 3. Merge Sort Structure

Base case:
- if `head == null` or `head.next == null`, it is already sorted

Recursive case:
- find middle
- save right half start
- cut the list using `mid.next = null`
- sort left half
- sort right half
- merge both sorted halves

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

class SortLinkedList {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }

        ListNode mid = findMiddle(head);
        ListNode rightStart = mid.next;
        mid.next = null;

        ListNode left = sortList(head);
        ListNode right = sortList(rightStart);

        return merge(left, right);
    }

    private ListNode findMiddle(ListNode head) {
        ListNode slow = head;
        ListNode fast = head.next;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    private ListNode merge(ListNode left, ListNode right) {
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;

        while (left != null && right != null) {
            if (left.val <= right.val) {
                tail.next = left;
                left = left.next;
            } else {
                tail.next = right;
                right = right.next;
            }
            tail = tail.next;
        }

        tail.next = (left != null) ? left : right;
        return dummy.next;
    }
}
```

Complexity:
- Time: O(n log n)
- Space: O(log n) recursion stack

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For `4 -> 2 -> 1 -> 3`:

Split:
- left = `4 -> 2`
- right = `1 -> 3`

Sort left:
- `4 -> 2` -> `2 -> 4`

Sort right:
- `1 -> 3` -> already sorted

Merge:
- merge `2 -> 4` and `1 -> 3`
- result = `1 -> 2 -> 3 -> 4`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use merge sort on linked lists when:
- the list must be sorted in `O(n log n)`
- array-style random access is not available
- stable linear merging is natural

Related problems:
- merge two sorted linked lists
- split list into halves
- bottom-up merge sort on linked list

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty list -> return `null`
- Single node -> already sorted
- Always cut the list using `mid.next = null`
- If `fast = head` instead of `head.next`, splitting for size 2 can behave badly
- Reuse a correct merge routine to avoid pointer bugs

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Linked list sorting is merge sort by default.
- Use slow/fast pointers to split.
- Use linear merge to combine sorted halves.
- Total complexity is `O(n log n)` with recursion stack overhead only.

------------------------------------------------------------------------

# End of Notes
