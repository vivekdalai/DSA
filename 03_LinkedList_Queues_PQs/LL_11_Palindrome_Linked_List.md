# Linked List / Queue / Priority Queue Notes

## 11 - Palindrome Linked List

**Generated on:** 2026-03-31 23:53:01 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given the head of a singly linked list, determine whether it is a palindrome.

Example:
- `1 -> 2 -> 2 -> 1` -> `true`
- `1 -> 2 -> 3` -> `false`

This is a very common interview problem because it combines:
- finding middle
- reversing a list
- comparing halves

------------------------------------------------------------------------

## 🪜 2. Core Idea

Steps:
1. Find the middle of the list using slow-fast pointers
2. Reverse the second half
3. Compare first half and reversed second half
4. If all values match, it is a palindrome

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

- use `slow` and `fast` to reach the middle
- reverse the list starting from `slow`
- compare nodes from:
  - `head`
  - reversed second half
- if every value matches, return `true`

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

class PalindromeLinkedList {
    public boolean isPalindrome(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }

        ListNode slow = head;
        ListNode fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        ListNode secondHalf = reverse(slow);
        ListNode firstHalf = head;

        while (secondHalf != null) {
            if (firstHalf.val != secondHalf.val) {
                return false;
            }
            firstHalf = firstHalf.next;
            secondHalf = secondHalf.next;
        }

        return true;
    }

    private ListNode reverse(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;

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

For `1 -> 2 -> 2 -> 1`:

- middle reached near second `2`
- reverse second half: `1 -> 2`
- compare:
  - first half: `1 -> 2`
  - reversed half: `1 -> 2`
- all values match -> palindrome

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This problem combines three standard linked list templates:
- find middle
- reverse linked list
- compare two lists

Related problems:
- reverse linked list
- reorder list
- twin sum of linked list

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty list -> palindrome
- Single node -> palindrome
- For odd length, middle element can be ignored naturally by reversal logic
- If problem requires original list to remain unchanged, reverse second half back after comparison

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Find middle.
- Reverse second half.
- Compare both halves.
- This is the standard O(n) time, O(1) space palindrome template for linked lists.

------------------------------------------------------------------------

# End of Notes


## my code
```java

public class Solution {
    public static boolean isPalindrome(Node head) {
        // write your code here
        //Finding the middle
        Node slow = head;
        Node fast = head;
        Node middle = head;

        while(fast != null && fast.next != null){
            fast = fast.next.next;
            slow = slow.next;
        }
        middle = slow;

        //odd length
        if(fast != null)    slow = slow.next;

        //reverse the half
        Node revHead = reverseList(slow);

        boolean isPalindromeString = true;
        //compare and check
        slow = head;
        while(revHead != null){
            if(slow.data != revHead.data){
                isPalindromeString = false;
                break;
            } else{
                slow = slow.next;
                revHead = revHead.next;
            }
        }

        // reverseList(revHead);

        return isPalindromeString;

    }

    private static Node reverseList(Node node){
        if(node == null || node.next == null)
            return node;
        
        Node newHead = reverseList(node.next);
        node.next.next = node;
        node.next = null;
        
        return newHead;
    }
}


```