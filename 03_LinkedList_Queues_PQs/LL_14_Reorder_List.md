# Linked List / Queue / Priority Queue Notes

## 14 - Reorder List

Problem: LeetCode 143 - Reorder List

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given the head of a singly linked list:

    L0 -> L1 -> L2 -> ... -> Ln-1 -> Ln

Reorder it **in place** into:

    L0 -> Ln -> L1 -> Ln-1 -> L2 -> Ln-2 -> ...

You may not modify the values in the list's nodes — only node links may change.

Example:
- `1 -> 2 -> 3 -> 4 -> 5`
- result = `1 -> 5 -> 2 -> 4 -> 3`

Example (even length):
- `1 -> 2 -> 3 -> 4`
- result = `1 -> 4 -> 2 -> 3`

------------------------------------------------------------------------

## 🪜 2. Core Idea

The target order interleaves the list's front half with its **reversed** back half.

So instead of trying to jump back and forth in a singly linked list (which has no `prev` pointer), do it in three clean phases:

1. **Find the middle** — slow/fast pointers.
2. **Reverse the second half** — standard in-place reversal, starting right after the middle.
3. **Merge the two halves alternately** — one node from the front list, one node from the reversed back list, repeat.

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

**Step 1 — Find the middle.**
Move `slow` by 1 and `fast` by 2 until `fast` runs out. `slow` ends on (or just before, for even length) the middle node.

**Step 2 — Split and reverse.**
Cut the list at the middle: `mid.next = null` detaches the first half (`l1 = head`) from the second half. Reverse the second half to get `l2`.

**Step 3 — Merge alternately.**
Walk `l1` and `l2` together. For each pair, save `l1`'s original next (`t1`) and `l2`'s original next (`t2`), splice `l2`'s node right after `l1`'s node, then advance both pointers to `t1` / `t2`. Stop when either list runs out — since `l2` (the reversed back half) is never longer than `l1`, `l2` becoming `null` first is what naturally ends the loop.

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
// Definition for a Linked List node
// class ListNode {
//     int val;
//     ListNode next;
//
//     // Constructor
//     public ListNode(int val) {
//         this.val = val;
//         this.next = null;
//     }
// }
import ds_v1.LinkedList.ListNode;
import java.util.*;

public class Solution
{
    public static ListNode reorderList(ListNode head) {
    if(head == null || head.next == null || head.next.next == null)
        return head;
        
    ListNode slow = head;
    ListNode fast = head;
    while(fast != null && fast.next != null){
        slow = slow.next;
        fast = fast.next.next;
    }
    ListNode mid = slow;
    ListNode l2 = reverseList(mid.next);
    mid.next = null;
    ListNode l1 = head;
    
    while(l1 != null && l2 != null){
        ListNode t1 = l1.next;
        l1.next = l2;
        ListNode t2 = l2.next;
        l2.next = t1;
        
        l1 = t1;
        l2 = t2;
    }
    
    return head;
  }
  
  private static ListNode reverseList(ListNode node){
      if(node == null || node.next == null)
        return node;
        
      ListNode prev = null;
      ListNode curr = node;
      
      while(curr != null){
            ListNode next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
      }
      
      return prev;
  }
}
```

Complexity:
- Time: `O(n)` — one pass to find the middle, one pass to reverse, one pass to merge.
- Space: `O(1)` — everything is rewired in place, no extra data structure.

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For `1 -> 2 -> 3 -> 4 -> 5`:

**Find middle:**

| step | slow | fast |
|---|---|---|
| start | 1 | 1 |
| 1 | 2 | 3 |
| 2 | 3 | 5 |

`fast.next == null` now (fast = 5), loop stops. `mid = slow = 3`.

**Split and reverse:**
- `l2 = reverseList(4 -> 5) = 5 -> 4`
- `mid.next = null` -> first half is now `1 -> 2 -> 3`
- `l1 = 1 -> 2 -> 3`

**Merge:**

| l1 before | l2 before | splice | l1 after | l2 after |
|---|---|---|---|---|
| `1` | `5` | `1 -> 5`, `5 -> 2` | `2` | `4` |
| `2` | `4` | `2 -> 4`, `4 -> 3` | `3` | `null` |

`l2 == null` -> loop stops.

Result:

    1 -> 5 -> 2 -> 4 -> 3

which matches the expected output.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This problem combines three linked-list fundamentals into one:
- middle-of-linked-list (slow/fast pointers) — see `LL_09_Middle_Of_Linked_List`
- in-place reversal — see `LL_03_Reverse_Linked_List`
- merging two lists node-by-node — see `LL_05_Merge_Two_Sorted_Linked_Lists`

Related problems:
- palindrome linked list (also uses find-middle + reverse) — see `LL_11_Palindrome_Linked_List`
- swap nodes in pairs
- rotate list

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- `head == null`, single node, or two nodes -> already "reordered", return as-is (the early guard `head.next.next == null` covers 0/1/2-node lists).
- Odd-length list: `mid` ends up as the true middle node, and it stays as the last node output by the merge (e.g. `3` in the 5-node example) since `l1` still has one extra node once `l2` runs dry.
- Even-length list: `l1` and `l2` end up the same length, so the merge consumes both fully with no leftover.
- Must cut `mid.next = null` **before** merging — otherwise the first half still trails into the (now-reversed) second half and creates a cycle-like mess when splicing.
- Save `t1`/`t2` before overwriting `.next` in the merge loop — overwrite-then-lose-the-rest is the classic bug here, same class of mistake as in-place list reversal.

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Find the middle with slow/fast pointers.
- Reverse only the second half.
- Merge the two halves one node at a time, saving "next" pointers before overwriting them.
- All in `O(n)` time, `O(1)` extra space — no arrays, no recursion needed.

------------------------------------------------------------------------

# End of Notes
