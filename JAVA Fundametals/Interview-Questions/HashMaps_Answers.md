# HashMap Interview Questions — Answers

## Core Concept Questions

1. `put()` computes hash → finds bucket → if empty insert → else check equals → update or handle collision.
2. `get()` computes hash → finds bucket → traverses (list/tree) → uses equals() to match key.
3. Due to hashing distributing keys uniformly across buckets.
4. `hashCode()` decides bucket, `equals()` resolves collisions.
5. Required to ensure correct retrieval from same bucket.

## Hashing & Indexing

6. Bitwise AND is faster than modulo. Also, AND operations efficiently handles negative hashcode unlike % which gives negative index.
7. Enables efficient bitmasking (`& (n-1)`).
8. Mixes high bits into low bits to avoid poor distribution.
9. All keys go into one bucket → performance degrades.
10. Yes, AND operation ensures valid index.

## Collision Handling

11. Stored in same bucket, resolved via equals().
12. Pre-Java 8: LinkedList; Java 8+: Tree after threshold.
13. Converting list to Red-Black Tree for performance.
14. Balance between memory and performance.
15. When bucket size > 8 and capacity ≥ 64.

## equals() & hashCode() Edge Cases

16. Retrieval fails due to different buckets.
17. Same bucket but equals fails → lookup fails.
18. Yes, collisions can occur.
19. No, violates contract.
20. Leads to unpredictable behavior.

## Tricky Code-Based Questions

21. Value is replaced, size unchanged.
22. Retrieval fails due to changed hash.
23. Different hash → different bucket.
24. Map becomes inconsistent.
25. Yes, but risky (can cause recursion issues).

## Internal Behavior

26. Load factor = 0.75 (default), controls resize threshold.
27. When size > capacity × load factor.
28. New array created, elements redistributed.
29. Yes, all entries rehashed.
30. Default = 16.

## Null Handling

31. Only one null key allowed due to uniqueness.
32. Stored in bucket 0.
33. To avoid ambiguity in multithreading.

## Performance & Optimization

34. Poor hashCode, high load factor, collisions.
35. Causes clustering → slows down operations.
36. Optimal balance of space vs performance.
37. O(n)
38. Uses Red-Black Tree → O(log n)

## Concurrency & Threading

39. No
40. Data inconsistency, race conditions.
41. Concurrent resize causing cycle → infinite loop.
42. ConcurrentHashMap is thread-safe.
43. Avoids ambiguity in concurrent reads.

## Comparison Questions

44. HashMap not thread-safe, Hashtable is.
45. LinkedHashMap maintains insertion order.
46. TreeMap is sorted (uses Red-Black Tree).
47. When sorted order is needed.
48. IdentityHashMap uses `==`, HashMap uses equals().

## Advanced / Deep Dive

49. Capacity doubles → rehash → redistribute.
50. Used for fast index calculation.
51. Treeification reduces worst-case complexity.
52. Node contains key, value, hash, next pointer.
53. Load factor + resizing balance memory/performance.

## Real-world / Design Thinking

54. Poor hash functions, high collisions.
55. Use prime multipliers, include all fields.
56. Becomes linked list/tree → slower.
57. Yes, but lacks eviction policy.
58. Analyze collisions, hashCode, bucket distribution.