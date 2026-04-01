# Linked List / Queue / Priority Queue Notes

## 01 - Build a Singly Linked List from an Array

**Generated on:** 2026-03-31 19:20:58 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array of integers, create a singly linked list containing the same values in the same order.

Example:
- `arr = [1, 2, 4, 5, 6]`
- linked list = `1 -> 2 -> 4 -> 5 -> 6`

This is a foundational linked list construction problem.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Maintain:
- `head` = first node of the linked list
- `prev` = last node added so far

For each value in the array:
- create a new node
- if this is the first node, set `head = newNode`
- otherwise connect `prev.next = newNode`
- move `prev` to the new node

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

- Start with `head = null` and `prev = null`
- Traverse the array from left to right
- Create one node per value
- Link current node after `prev`
- Return `head` at the end

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class Node {
    int val;
    Node next;

    Node(int val) {
        this.val = val;
        this.next = null;
    }
}

class BuildSinglyLinkedList {
    public Node build(int[] arr) {
        Node head = null;
        Node prev = null;

        for (int value : arr) {
            Node newNode = new Node(value);

            if (prev != null) {
                prev.next = newNode;
            } else {
                head = newNode;
            }

            prev = newNode;
        }

        return head;
    }

    public void print(Node head) {
        Node curr = head;
        while (curr != null) {
            System.out.print(curr.val);
            if (curr.next != null) {
                System.out.print(" -> ");
            }
            curr = curr.next;
        }
        System.out.println();
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n) for the created nodes

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For `arr = [1, 2, 4]`:

- Create node `1`
  - `head = 1`, `prev = 1`
- Create node `2`
  - connect `1.next = 2`
  - `prev = 2`
- Create node `4`
  - connect `2.next = 4`
  - `prev = 4`

Result:
- `1 -> 2 -> 4`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is basic linked list construction.

Typical triggers:
- "Convert array to linked list"
- "Create a linked list from values"
- "Build list manually"

This also appears as a helper step in:
- merge sort on linked list
- testing linked list interview questions
- converting input into node form

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty array -> return `null`
- Single element -> that node becomes the head
- Do not return `prev`; return `head`
- Do not forget to move `prev = newNode`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Use `head` for the first node and `prev` for linking.
- First insertion is the only special case.
- Every later node is attached using `prev.next = newNode`.
- Final answer is always `head`.

------------------------------------------------------------------------

# End of Notes
