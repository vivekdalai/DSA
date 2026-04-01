# Linked List / Queue / Priority Queue Notes

## Queue 1 - Queue Implementation in Java

**Generated on:** 2026-04-01 09:31:57 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

This note covers the basic queue implementation in Java using the `Queue` interface and `LinkedList` class.

Important points:
- In Java, a queue can be created using `LinkedList`
- Insertion happens at the rear
- Deletion happens from the front
- Queue follows **FIFO**:
  - First In, First Out

------------------------------------------------------------------------

## 🪜 2. Core Idea

Queue in Java can be implemented as:

```java
Queue<Integer> queue = new LinkedList<>();
```

Key note:
- `LinkedList` internally maintains links between nodes
- insertion and deletion at the ends are efficient
- both enqueue and dequeue are `O(1)` operations in practice for this structure

Why:
- it maintains references to the start and end
- so adding to rear and removing from front does not require shifting elements

------------------------------------------------------------------------

## 🔁 3. Common Queue Operations

- `add(x)` -> insert element at rear
- `remove()` -> remove element from front
- `peek()` -> get front element without removing it
- `isEmpty()` -> check whether queue is empty
- `size()` -> number of elements currently present

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.LinkedList;
import java.util.Queue;

public class QueueExample {
    public static void main(String[] args) {
        // Create a queue using LinkedList
        Queue<Integer> queue = new LinkedList<>();

        // Add elements to the queue
        queue.add(10);
        queue.add(20);
        queue.add(30);
        queue.add(40);

        // Display the elements in the queue
        System.out.println("Queue: " + queue);

        // Remove elements from the queue
        int removedElement = queue.remove();
        System.out.println("Removed Element: " + removedElement);

        // Display the elements in the queue after removal
        System.out.println("Queue after removal: " + queue);

        // Peek the front element of the queue
        int frontElement = queue.peek();
        System.out.println("Front Element: " + frontElement);

        // Check if the queue is empty
        boolean isEmpty = queue.isEmpty();
        System.out.println("Is the queue empty? " + isEmpty);

        // Get the size of the queue
        int size = queue.size();
        System.out.println("Queue size: " + size);
    }
}
```

Complexity:
- `add()` -> O(1)
- `remove()` -> O(1)
- `peek()` -> O(1)
- `isEmpty()` -> O(1)
- `size()` -> O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

Start:
- `queue = []`

After insertions:
- add `10`, `20`, `30`, `40`
- `queue = [10, 20, 30, 40]`

Remove front:
- removed = `10`
- `queue = [20, 30, 40]`

Peek:
- front = `20`

Other checks:
- `isEmpty()` -> `false`
- `size()` -> `3`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use queue when:
- elements must be processed in arrival order
- you need FIFO behavior
- tasks are handled level by level

Common applications:
- BFS in graphs
- BFS in trees
- task scheduling
- producer-consumer flow
- buffering

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Calling `remove()` on an empty queue throws an exception
- `peek()` returns `null` if queue is empty
- Do not confuse queue with stack:
  - queue = FIFO
  - stack = LIFO
- `LinkedList` works, but `ArrayDeque` is often preferred for pure queue usage in interviews

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Java queue can be created using `Queue<Integer> queue = new LinkedList<>();`
- Queue follows FIFO order.
- Insertion at rear and deletion from front are efficient.
- Basic methods to remember:
  - `add()`
  - `remove()`
  - `peek()`
  - `isEmpty()`
  - `size()`

------------------------------------------------------------------------

# End of Notes
