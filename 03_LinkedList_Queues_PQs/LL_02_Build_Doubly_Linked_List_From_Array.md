# Linked List / Queue / Priority Queue Notes

## 02 - Build a Doubly Linked List from an Array

**Generated on:** 2026-03-31 19:21:27 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array, construct a doubly linked list containing the same values in the same order.

Each node stores:
- `data`
- `next`
- `prev`

Example:
- `arr = [1, 2, 3]`
- result = `1 <-> 2 <-> 3`

This is the doubly linked list version of basic list construction.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Maintain:
- `head` = first node
- `prev` = previously created node

For each array value:
- create a new node
- if this is the first node, set `head`
- otherwise:
  - `prev.next = newNode`
  - `newNode.prev = prev`
- move `prev` forward

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

- Start with `head = null`, `prev = null`
- Iterate through the array
- Create one node per value
- Link in both directions
- Return `head`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class Node {
    int data;
    Node next;
    Node prev;

    Node(int data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}

class BuildDoublyLinkedList {
    Node constructDLL(int[] arr) {
        Node head = null;
        Node prev = null;

        for (int value : arr) {
            Node newNode = new Node(value);

            if (prev == null) {
                head = newNode;
            } else {
                prev.next = newNode;
                newNode.prev = prev;
            }

            prev = newNode;
        }

        return head;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n) for created nodes

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For `arr = [7, 9, 12]`:

- Create `7`
  - `head = 7`, `prev = 7`
- Create `9`
  - `7.next = 9`
  - `9.prev = 7`
  - `prev = 9`
- Create `12`
  - `9.next = 12`
  - `12.prev = 9`

Result:
- `7 <-> 9 <-> 12`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This pattern appears when:
- the problem asks for reverse traversal support
- insertion/deletion near a known node should be easy
- an LRU cache or browser-history style structure is used

Common related problems:
- construct DLL
- delete node in DLL
- LRU cache with hash map + doubly linked list

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty array -> return `null`
- Single element -> `head.next = null`, `head.prev = null`
- Do not forget `newNode.prev = prev`
- Return `head`, not `prev`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- DLL construction is the same as singly linked list construction plus one backward link.
- The only extra line is `newNode.prev = prev`.
- `head` tracks the start, `prev` tracks the tail being built.

------------------------------------------------------------------------

# End of Notes
