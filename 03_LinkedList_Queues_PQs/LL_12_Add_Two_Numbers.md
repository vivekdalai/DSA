# Linked List / Queue / Priority Queue Notes

## 12 - Add Two Numbers

**Generated on:** 2026-03-31 23:53:24 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Two non-empty linked lists represent two non-negative integers.
Digits are stored in reverse order, and each node contains a single digit.

Return the sum as a linked list in the same reverse-order format.

Example:
- `l1 = 2 -> 4 -> 3`
- `l2 = 5 -> 6 -> 4`
- number values = `342 + 465 = 807`
- answer = `7 -> 0 -> 8`

This is one of the most frequently asked linked list problems.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Traverse both lists together like normal addition with carry.

At each step:
- read digit from list1 if available
- read digit from list2 if available
- compute `sum = x + y + carry`
- create a node with `sum % 10`
- update `carry = sum / 10`

Use a dummy node to build the answer list easily.

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

- initialize `carry = 0`
- while `l1 != null` or `l2 != null` or `carry != 0`
  - read values from both lists
  - compute sum
  - append digit node
  - update carry
- return `dummy.next`

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

class AddTwoNumbers {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        int carry = 0;

        while (l1 != null || l2 != null || carry != 0) {
            int x = (l1 != null) ? l1.val : 0;
            int y = (l2 != null) ? l2.val : 0;

            int sum = x + y + carry;
            carry = sum / 10;

            tail.next = new ListNode(sum % 10);
            tail = tail.next;

            if (l1 != null) {
                l1 = l1.next;
            }
            if (l2 != null) {
                l2 = l2.next;
            }
        }

        return dummy.next;
    }
}
```

Complexity:
- Time: O(max(n, m))
- Space: O(max(n, m)) for output list

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For:
- `l1 = 2 -> 4 -> 3`
- `l2 = 5 -> 6 -> 4`

Step 1:
- `2 + 5 + 0 = 7`
- digit = `7`, carry = `0`

Step 2:
- `4 + 6 + 0 = 10`
- digit = `0`, carry = `1`

Step 3:
- `3 + 4 + 1 = 8`
- digit = `8`, carry = `0`

Result:
- `7 -> 0 -> 8`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this pattern when:
- linked lists represent numbers digit by digit
- addition must be simulated with carry
- result length may exceed input length

Related problems:
- Add Two Numbers II
- multiply represented numbers
- plus one linked list

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Different list lengths are normal
- Final carry may create an extra node
- Use a dummy node to avoid special handling for first insertion
- Do not try converting entire list to integer if values may overflow

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Traverse both lists together.
- Add digit1 + digit2 + carry.
- Store `sum % 10`, carry `sum / 10`.
- Keep going while nodes remain or carry is non-zero.

------------------------------------------------------------------------

# End of Notes
