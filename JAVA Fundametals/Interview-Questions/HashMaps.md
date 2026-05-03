# HashMap Interview Questions

## Core Concept Questions
1. ***What happens internally when you call `put()` in a HashMap?***
    - `put()` computes hash â†’ finds bucket â†’ if empty insert â†’ else check equals â†’ update or handle collision.

2. ***How does `get()` work internally?***
    - `get()` computes hash â†’ finds bucket â†’ traverses (list/tree) â†’ uses equals() to match key.

3. ***Why is HashMapâ€™s average time complexity O(1)?***
    - Due to hashing distributing keys uniformly across buckets.

4. ***What is the role of `hashCode()` and `equals()` in HashMap?***
    - `hashCode()` decides bucket, `equals()` resolves collisions.

5. ***Why must `equals()` and `hashCode()` be consistent?***
    - Required to ensure correct retrieval from same bucket.

## Hashing & Indexing
6. ***Why is the formula `index = hash & (n - 1)` used instead of `%`?***
    - Bitwise AND is faster than modulo. Also, AND operations efficiently handles negative hashcode unlike % which gives negative index.

7. ***Why must the capacity of HashMap be a power of 2?***
    - Enables efficient bitmasking (`& (n-1)`).

8. ***What is hash spreading and why is it needed?***
    - Mixes high bits into low bits to avoid poor distribution.

9. ***What happens if `hashCode()` returns the same value for all objects?***
    - All keys go into one bucket -> performance degrades.

10. ***Can hashCode be negative? How does HashMap handle it?***
    - Yes, AND operation ensures valid index.

## Collision Handling
11. ***What happens when two keys have the same hashCode?***
    - Stored in same bucket, resolved via equals().

12. ***How are collisions handled before Java 8 vs after Java 8?***
    - Pre-Java 8: LinkedList; Java 8+: Tree after threshold.

13. ***What is treeification in HashMap?***
    - Converting list to Red-Black Tree for performance.

14. ***Why is the treeify threshold 8 and untreeify threshold 6?***
    - Balance between memory and performance.

15. ***Under what conditions does HashMap convert a bucket to a tree?***
    - When bucket size > 8 and capacity >= 64.

## equals() & hashCode() Edge Cases
16. ***What happens if you override `equals()` but not `hashCode()`?***
    - Retrieval fails due to different buckets.

17. ***What happens if you override `hashCode()` but not `equals()`?***
    - Same bucket but equals fails -> lookup fails.

18. ***Can two unequal objects have the same hashCode?***
    - Yes, collisions can occur.

19. ***Can two equal objects have different hashCodes?***
    - No, violates contract.

20. ***What happens if `equals()` is inconsistent?***
    - Leads to unpredictable behavior.

## Tricky Code-Based Questions
21. ***Predict output when inserting duplicate keys.***
    - Value is replaced, size unchanged.

22. ***What happens if you mutate a key after inserting it into HashMap?***
    - Retrieval fails due to changed hash.

23. ***Why does `map.get(new Key(...))` sometimes return null even if logically equal?***
    - Different hash -> different bucket.

24. ***What happens if key fields used in hashCode change?***
    - Map becomes inconsistent.

25. ***Can HashMap store itself as a key?***
    - Yes, but risky (can cause recursion issues).

## Internal Behavior
26. ***What is load factor and default value?***
    - Load factor = 0.75 (default), controls resize threshold.

27. ***When does resizing happen?***
    - When size > capacity x load factor.

28. ***What happens during rehashing?***
    - New array created, elements redistributed.

29. ***Does resizing rehash all elements?***
    - Yes, all entries rehashed.

30. ***What is the default initial capacity?***
    - Default = 16.

## Null Handling
31. ***Why does HashMap allow only one null key?***
    - Only one null key allowed due to uniqueness.

32. ***How are null keys stored internally?***
    - Stored in bucket 0.

33. ***Why does Hashtable not allow null keys/values?***
    - To avoid ambiguity in multithreading.

## Performance & Optimization
34. ***What causes performance degradation in HashMap?***
    - Poor hashCode, high load factor, collisions.

35. ***How does a bad hashCode affect performance?***
    - Causes clustering -> slows down operations.

36. ***Why is 0.75 used as default load factor?***
    - Optimal balance of space vs performance.

37. ***What is the worst-case time complexity of HashMap?***
    - O(n)

38. ***How does Java 8 improve worst-case performance?***
    - Uses Red-Black Tree -> O(log n)

## Concurrency & Threading
39. ***Is HashMap thread-safe?***
    - No

40. ***What issues occur if multiple threads use HashMap?***
    - Data inconsistency, race conditions.

41. ***What was the infinite loop issue in pre-Java 8 HashMap?***
    - Concurrent resize causing cycle -> infinite loop.

42. ***Difference between HashMap and ConcurrentHashMap?***
    - ConcurrentHashMap is thread-safe.

43. ***Why doesnâ€™t ConcurrentHashMap allow null keys?***
    - Avoids ambiguity in concurrent reads.

## Comparison Questions
44. ***HashMap vs Hashtable?***
    - HashMap not thread-safe, Hashtable is.

45. ***HashMap vs LinkedHashMap?***
    - LinkedHashMap maintains insertion order.

46. ***HashMap vs TreeMap?***
    - TreeMap is sorted (uses Red-Black Tree).

47. ***When would you prefer TreeMap over HashMap?***
    - When sorted order is needed.

48. ***Difference between identity-based and equals-based maps?***
    - IdentityHashMap uses `==`, HashMap uses equals().

## Advanced / Deep Dive
49. ***How does HashMap resize internally (step-by-step)?***
    - Capacity doubles -> rehash -> redistribute.

50. ***What is the role of bitwise operations in HashMap?***
    - Used for fast index calculation.

51. ***How does HashMap maintain performance during high collisions?***
    - Treeification reduces worst-case complexity.

52. ***What is the structure of Node in HashMap?***
    - Node contains key, value, hash, next pointer.

53. ***How does HashMap handle memory vs performance trade-offs?***
    - Load factor + resizing balance memory/performance.

## Real-world / Design Thinking
54. ***When would HashMap perform poorly in production?***
    - Poor hash functions, high collisions.

55. ***How would you design a better hash function?***
    - Use prime multipliers, include all fields.

56. ***What happens if all keys land in one bucket?***
    - Becomes linked list/tree -> slower.

57. ***Can HashMap be used as a cache? What are limitations?***
    - Yes, but lacks eviction policy.

58. ***How would you debug a slow HashMap?***
    - Analyze collisions, hashCode, bucket distribution.
