# Stack Notes

## 07 - LRU Cache (LC 146)

Problem: Design a data structure that implements a Least Recently Used (LRU) cache with a fixed `capacity`, supporting `get(key)` and `put(key, value)`, both in `O(1)` average time.

---

## 1. Problem Understanding

Example:

```text
LRUCache cache = new LRUCache(2);
cache.put(1, 1);
cache.put(2, 2);
cache.get(1);       // returns 1, and marks key 1 as most recently used
cache.put(3, 3);    // capacity full -> evicts key 2 (the least recently used)
cache.get(2);       // returns -1 (evicted)
```

Every `get` or `put` on an existing key must move that key to the "most recently used" end. When `put` is called on a new key and the cache is at capacity, the "least recently used" key must be evicted first.

---

## 2. Core Intuition

We need two operations, both `O(1)`:

1. **Find a key's node instantly** — a `HashMap<Integer, Node>` gives O(1) lookup.
2. **Move a node to the front (most recently used) or remove the node at the back (least recently used) instantly** — a **doubly linked list** allows O(1) removal/insertion at both ends, given a direct reference to the node (no traversal needed).

The recency order **is** conceptually a stack-like structure: the most-recently-touched key sits at the "top" (head), and eviction always happens from the "bottom" (tail) — like a stack that also supports removing an arbitrary middle element in O(1) when a key is re-accessed.

---

## 3. Why HashMap + Doubly Linked List (Not Just a Stack)?

A plain stack only supports push/pop at one end — it can't efficiently move an arbitrary already-present element to the top without shifting everything (O(n)). A doubly linked list, combined with a hash map pointing directly at each node, lets us:

```text
remove any node in O(1)   (given the node reference — no search needed)
insert at the head in O(1)
remove from the tail in O(1)
```

Two dummy sentinel nodes (`head` and `tail`) eliminate special-casing for "list is empty" or "removing the only node."

---

## 4. Java Implementation

```java
import java.util.HashMap;
import java.util.Map;

class LRUCache {
    private final Map<Integer, DLL> myMap;
    private final DLL head;
    private final DLL tail;
    private final int capacity;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        head = new DLL(-1, -1);
        tail = new DLL(-1, -1);
        head.next = tail;
        tail.prev = head;
        myMap = new HashMap<>();
    }

    public int get(int key) {
        DLL node = myMap.get(key);
        if (node == null) {
            return -1;
        }

        node = deleteNodeInCache(node);
        insertAtStartDLL(head, node);

        return node.value;
    }

    public void put(int key, int value) {
        DLL node = myMap.get(key);

        if (node != null) {
            node.value = value;
            node = deleteNodeInCache(node);
            insertAtStartDLL(head, node);
        } else {
            if (myMap.size() == capacity) {
                DLL evicted = deleteLastNode(tail);
                myMap.remove(evicted.key);
            }

            DLL newNode = new DLL(key, value);
            insertAtStartDLL(head, newNode);
            myMap.put(key, newNode);
        }
    }

    private void insertAtStartDLL(DLL head, DLL newNode) {
        DLL nextNode = head.next;
        head.next = newNode;
        newNode.prev = head;
        newNode.next = nextNode;
        nextNode.prev = newNode;
    }

    private DLL deleteNodeInCache(DLL node) {
        DLL prevNode = node.prev;
        DLL nextNode = node.next;
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        node.prev = null;
        node.next = null;
        return node;
    }

    private DLL deleteLastNode(DLL tail) {
        DLL node = tail.prev;
        node.prev.next = tail;
        tail.prev = node.prev;
        node.prev = null;
        node.next = null;
        return node;
    }
}

class DLL {
    DLL prev, next;
    int key, value;

    DLL(int key, int value) {
        this.key = key;
        this.value = value;
        prev = null;
        next = null;
    }
}
```

Layout:

```text
head <-> [most recently used] <-> ... <-> [least recently used] <-> tail
```

---

## 5. Dry Run

```text
LRUCache cache = new LRUCache(2);
```

| operation | list (head -> tail, excluding sentinels) | map keys | notes |
|---|---|---|---|
| `put(1,1)` | `1` | `{1}` | inserted at head |
| `put(2,2)` | `2, 1` | `{1,2}` | inserted at head |
| `get(1)` -> `1` | `1, 2` | `{1,2}` | node 1 removed then reinserted at head |
| `put(3,3)` | `3, 1` | `{1,3}` | cache full (size==2); evict tail (`2`); insert `3` at head |
| `get(2)` -> `-1` | `3, 1` | `{1,3}` | key 2 no longer in map |

---

## 6. Edge Cases

- `capacity == 0`: every `put` should immediately be evictable / effectively a no-op (or per problem constraints, `capacity >= 1` is usually guaranteed — but if `0` is possible, `myMap.size() == capacity` is `0 == 0` immediately true, so the eviction path runs on every put; ensure it doesn't try to evict from an empty list, i.e., guard `deleteLastNode` against `tail.prev == head`).
- `get` on a key that was never inserted: returns `-1`, no structural change.
- `put` on an existing key: must update the value **and** refresh recency (move to head) — a common bug is updating the value but forgetting to move the node.
- Repeated `get`/`put` on the same single key when `capacity == 1`: the node is continually removed and reinserted at the head, which is still correct but is a good stress case to verify no self-referential pointer corruption occurs (removing then reinserting the *same* node object must not break `prev`/`next` links).
- Cache never reaching capacity: eviction logic should simply never trigger; verify `myMap.size() == capacity` doesn't fire prematurely.

---

## 7. Common Mistakes

- Using a singly linked list instead of doubly linked — without a `prev` pointer, removing an arbitrary node requires traversal from the head to find its predecessor, degrading to O(n).
- Forgetting the dummy `head`/`tail` sentinels and then having to special-case insertion/removal when the list is empty or has one element.
- In `put` for an existing key, forgetting to update `node.value` before or after moving it — the value update and the recency update are two separate concerns that both must happen.
- Off-by-one in eviction: evicting **before** checking whether the key already exists (which doesn't need a new slot at all) wastes a slot unnecessarily.
- Not removing the evicted key from the `HashMap` — leaving a stale map entry pointing to a detached node causes incorrect future `get` results.

---

## 8. Complexity

```text
Time:  O(1) average for both get and put
Space: O(capacity)   (map + doubly linked list both bounded by capacity)
```

---

## 9. Pattern Recognition

"O(1) access **and** O(1) reordering by recency" is the signature of the **HashMap + Doubly Linked List** combo. It generalizes to LFU caches (LC 460, using multiple frequency-bucketed doubly linked lists) and any "keep the most/least recently used item accessible in O(1)" design question.

---

## 10. Takeaway

A stack alone can't efficiently promote an arbitrary already-present element to the top; pairing a hash map (for O(1) lookup) with a doubly linked list (for O(1) removal/insertion at both ends, given a direct node reference) is what makes LRU eviction and recency-refresh both O(1).

---

# End of Notes
