# Linked List / Queue / Priority Queue Notes

## 05 - Merge Two Sorted Linked Lists

**Generated on:** 2026-03-31 19:22:52 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the heads of two sorted linked lists, merge them into one sorted linked list and return the head.

Example:
- list1 = `1 -> 3 -> 5`
- list2 = `2 -> 4 -> 6`
- result = `1 -> 2 -> 3 -> 4 -> 5 -> 6`

This is the linked list version of merge step in merge sort.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Maintain two pointers:
- one on `list1`
- one on `list2`

At every step:
- choose the smaller node
- attach it to the merged result
- move that pointer forward

Two common implementations:
- recursive
- iterative with a dummy node

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

Iterative version:
- create `dummy` node
- keep `tail` at the end of merged list
- while both lists are non-empty:
  - attach smaller node to `tail.next`
  - move pointer of chosen list
  - move `tail`
- attach whichever list is still left

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

class MergeTwoSortedListsRecursive {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        if (list1 == null) {
            return list2;
        }
        if (list2 == null) {
            return list1;
        }

        if (list1.val < list2.val) {
            list1.next = mergeTwoLists(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoLists(list1, list2.next);
            return list2;
        }
    }
}
```

Complexity:
- Time: O(n + m)
- Space: O(n + m) recursion stack in worst case

------------------------------------------------------------------------

## 💻 4B. Iterative Java Implementation

```java
class MergeTwoSortedListsIterative {
    public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;

        while (list1 != null && list2 != null) {
            if (list1.val <= list2.val) {
                tail.next = list1;
                list1 = list1.next;
            } else {
                tail.next = list2;
                list2 = list2.next;
            }
            tail = tail.next;
        }

        tail.next = (list1 != null) ? list1 : list2;
        return dummy.next;
    }
}
```

Complexity:
- Time: O(n + m)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For:
- list1 = `1 -> 4 -> 7`
- list2 = `2 -> 3 -> 8`

Steps:
- compare `1` and `2` -> take `1`
- compare `4` and `2` -> take `2`
- compare `4` and `3` -> take `3`
- compare `4` and `8` -> take `4`
- compare `7` and `8` -> take `7`
- list1 ends -> attach remaining `8`

Result:
- `1 -> 2 -> 3 -> 4 -> 7 -> 8`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This pattern appears in:
- merge two sorted lists
- merge k sorted lists
- sort linked list using merge sort
- external merging / divide and conquer merge

Trigger words:
- "merge sorted linked lists"
- "combine in sorted order"
- "stable linear merge"

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- One list empty -> return the other list
- Both empty -> return `null`
- Do not forget to attach the leftover nodes
- Use node references, do not create unnecessary new nodes
- The PDF snippet has a naming mismatch:
  - method declared as `mergeTwoSortedList`
  - recursive call written as `mergeTwoLists`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Compare front nodes of both lists and attach the smaller one.
- Dummy node makes iterative merging clean.
- Recursive solution is shorter; iterative solution is space-optimal.
- This merge routine is reusable inside linked list merge sort.

------------------------------------------------------------------------

# End of Notes
