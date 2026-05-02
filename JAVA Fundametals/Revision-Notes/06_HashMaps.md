# HashMap
HashMap is implemented as an array of nodes, where each node stores a key-value pair
```java
class Node<K, V> {
      int hash;
     K key;
    V value;
   Node<K, V> next;
}
```
where
- hash: the hash code of the key
- key: the key object
- value: the associated value
- next: reference to the next node in case of a collision

### Points to remember
- implements the Map interface
- time complexity O(1) for basic operations like adding, removing, and retrieving elements.
- allow for one null key and multiple null values.
- Unordered

### Syntax 
```java
HashMap<String, Integer> map = new HashMap<>();
```

### Initializing with Size and Load Factor

```java
HashMap<String, Integer> map = new HashMap<>(16, 0.75f);
```
* Initial Capacity: This is the number of buckets in the hash table at the time of its creation.
* Load Factor: A load factor of 0.75 means that when 75% of the HashMap is filled, it will resize.

### Iterating Over a HashMap

#### using Entry Set
```java
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

#### Using Key Set
```java
for (String key : map.keySet()) {
    System.out.println(key + ": " + map.get(key));
}
```

#### Using Values
```java
for (Integer value : map.values()) {
    System.out.println(value);
}
```
- HashMaps use a hashing algorithm to determine where to store entries
- Java handles collisions using a technique called chaining.

# hashcode()

- hashCode() is a method from Object class
- returns an int (32-bit number) representing the object.
- default hashCode() is typically derived from the memory address (not exactly the address, but something closely related and JVM-dependent)

- Different objects → usually different hash codes
- Same object → same hash code

### coded version
```java
public int hashCode() {
    int h = 0;
    for (char c : value) {
        h = 31 * h + c; // (why 31? --> Prime number → reduces collisions)
    }
    return h;
}
```
### Why 31?
- Prime number → reduces collisions

- 31 * x can be optimized as:
```
(x << 5) - x   --> faster computation
```

### Example

```
String str = "ABC";
```

Calculations:
```
ASCII:

A = 65, B = 66, C = 67

h = 0
h = 31*0 + 65 = 65
h = 31*65 + 66 = 2081
h = 31*2081 + 67 = 64578

Hence,

"ABC".hashCode() = 64578
```


### For custom objects

- Default hashCode() is useless for logical equality.
- so you override

```java
@Override
public int hashCode() {
    return Objects.hash(name, age);
}
```

### Important Rule
- If you override equals(), you must override hashCode().
- Because:
```java
a.equals(b) == true  ⇒  a.hashCode() == b.hashCode()
```

### Golden Rules for hashCode()
1. Use the same fields as equals()

    If two objects are equal:

    a.equals(b) == true

    Then:

    a.hashCode() == b.hashCode()

    👉 Always include the same fields in both methods.

2. Use prime multiplication (usually 31)
    
    result = 31 * result + fieldHash;

    👉 Helps distribute values better and reduce collisions.

3. Include all important fields

    Bad:

    ```java
    return name.hashCode(); // ❌ ignores age
    ```
    Good:
    ```java
    int result = name.hashCode();
    result = 31 * result + age;
    return result;
    ```
4. Handle nulls safely

    ```java 
    result = 31 * result + (name == null ? 0 : name.hashCode());
    ```
5. Prefer Objects.hash() for simplicity
    ```java
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
    ```
    👉 Easy and clean (slightly slower, but fine in most cases)

### One-line memory trick
"Same fields as equals + multiply by 31 + handle nulls"

## Hashing and Index Calculation

- When a key-value pair is inserted
- the hash code of the key is calculated using the hashCode() method,
- and then an index is derived to find the bucket (array position).

```java
index = hashCode(key) & (n - 1) (faster bitwise AND operation) ==> equivalent to index = hash % n (modulo slower);
```
- here n is the number of bucket (initially 16 by default)
- bitwise AND operation ensures even distribution of entries across buckets.

```java
index = hash & 15; --> this keeps only last 4 digits of the hash
```

## Important detail: hash spreading

Before this step, Java improves distribution:

```java
hash = key.hashCode() ^ (key.hashCode() >>> 16);
```

- This mixes high bits into low bits
- Because only lower bits are used in (n - 1)

### Before spreading:

- Index depends only on low bits → poor distribution
- Many keys → same bucket → linked list → O(n)

### After spreading:

- High bits contribute → better bucket distribution
- Fewer collisions
- Keys spread evenly → O(1) average

## Collision

- If both keys have the same hashCode() and equals(), replace the existing value.
- Otherwise, link the new node to the existing one via a linked list.

###  Collision Handling in HashMap
- A collision occurs when two different keys produce the same index in the HashMap.

To handle this:

### Before Java 8: 
Collisions are managed using a linked list — new nodes are added at the same index, linked via the next reference.

### After Java 8:
If a bucket contains more than 8 nodes, the linked list is converted into a balanced tree (TreeNode or Red-Black Tree) for faster lookup (O(log n) instead of O(n)).

## Real-World Applications
1. Caching Results
2. Counting Frequencies
3. Database Indexing

## Common Pitfalls
1. Null Keys and Values: Only one null key is allowed, and multiple null values can be present. This can lead to unexpected behavior if not handled properly. 
2. Concurrent Access: If multiple threads are accessing a HashMap, it can lead to data inconsistency. In such cases, consider using ConcurrentHashMap.






































































