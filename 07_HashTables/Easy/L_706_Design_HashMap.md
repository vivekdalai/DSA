# Hash Table Notes

## 706 - Design HashMap

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/design-hashmap/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Design a HashMap without using any built-in hash table libraries.

Implement the `MyHashMap` class:

- `MyHashMap()` initializes the object with an empty map.

- `void put(int key, int value)` inserts a `(key, value)` pair into the HashMap. If the `key` already exists in the map, update the corresponding `value`.

- `int get(int key)` returns the `value` to which the specified `key` is mapped, or `-1` if this map contains no mapping for the `key`.

- `void remove(key)` removes the `key` and its corresponding `value` if the map contains the mapping for the `key`.

**Example 1:**

```text
Input
["MyHashMap", "put", "put", "get", "get", "put", "get", "remove", "get"]
[[], [1, 1], [2, 2], [1], [3], [2, 1], [2], [2], [2]]
Output
[null, null, null, 1, -1, null, 1, null, -1]

Explanation
MyHashMap myHashMap = new MyHashMap();
myHashMap.put(1, 1); // The map is now [[1,1]]
myHashMap.put(2, 2); // The map is now [[1,1], [2,2]]
myHashMap.get(1);    // return 1, The map is now [[1,1], [2,2]]
myHashMap.get(3);    // return -1 (i.e., not found), The map is now [[1,1], [2,2]]
myHashMap.put(2, 1); // The map is now [[1,1], [2,1]] (i.e., update the existing value)
myHashMap.get(2);    // return 1, The map is now [[1,1], [2,1]]
myHashMap.remove(2); // remove the mapping for 2, The map is now [[1,1]]
myHashMap.get(2);    // return -1 (i.e., not found), The map is now [[1,1]]
```

**Constraints:**

- `0 <= key, value <= 106`

- At most `104` calls will be made to `put`, `get`, and `remove`.

**Additional revision examples**



**Revision Example 1**

```text
Input: operations = ["MyHashMap","put","put","get","remove","get"], values = [[],[1,1],[2,2],[1],[1],[1]]
Output: [null,null,null,1,null,-1]
```

**Revision Example 2**

```text
Input: operations = ["MyHashMap","put","put","get"], values = [[],[10001,7],[1,5],[10001]]
Output: [null,null,null,7]
Explanation: Both keys collide into bucket 1, but the bucket scan still finds the exact key.
```

------------------------------------------------------------------------

## 2. First Intuition

A hash map needs a deterministic bucket for each key and a way to resolve collisions inside that bucket. The implementation uses separate chaining with small lists of key-value pairs.

------------------------------------------------------------------------

## 3. Bucketed Hashing Used in the Class

- The outer problem class contains an inner `MyHashMap` class.
- `key % 10000` selects a bucket.
- `put` updates an existing pair if the key is already present, otherwise it appends a new pair.
- `get` scans the bucket and returns `-1` if the key is absent.
- `remove` deletes the pair from the bucket using `removeIf`.

------------------------------------------------------------------------

## 4. Short Dry Run

`put(1, 10)` stores `[1,10]` in bucket `1`. `put(10001, 20)` lands in the same bucket because `10001 % 10000 = 1`, so it is chained beside it. `get(1)` scans that bucket and returns `10`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
class MyHashMap {
    private static final int SIZE = 10000;
    private List<int[]>[] buckets;

    public MyHashMap() {
        buckets = new ArrayList[SIZE];
        for (int i = 0; i < SIZE; i++) {
            buckets[i] = new ArrayList<>();
        }
    }

    public void put(int key, int value) {
        int idx = key % SIZE;
        for (int[] pair : buckets[idx]) {
            if (pair[0] == key) {
                pair[1] = value;
                return;
            }
        }
        buckets[idx].add(new int[] {key, value});
    }

    public int get(int key) {
        int idx = key % SIZE;
        for (int[] pair : buckets[idx]) {
            if (pair[0] == key) return pair[1];
        }
        return -1;
    }

    public void remove(int key) {
        int idx = key % SIZE;
        buckets[idx].removeIf(pair -> pair[0] == key);
    }
}
```

------------------------------------------------------------------------

## 6. Complexity

- Average time: close to `O(1)` when buckets stay small.
- Worst-case time: `O(n)` if many keys collide into one bucket.
- Space: `O(size + numberOfKeys)`.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Design questions care about collision handling.
- Separate chaining is easier to explain than open addressing for this problem.

------------------------------------------------------------------------

## End of Notes
