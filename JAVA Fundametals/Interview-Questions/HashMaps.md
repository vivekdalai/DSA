# HashMap Interview Questions

## Core Concept Questions
1. What happens internally when you call `put()` in a HashMap?
2. How does `get()` work internally?
3. Why is HashMap’s average time complexity O(1)?
4. What is the role of `hashCode()` and `equals()` in HashMap?
5. Why must `equals()` and `hashCode()` be consistent?

## Hashing & Indexing
6. Why is the formula `index = hash & (n - 1)` used instead of `%`?
7. Why must the capacity of HashMap be a power of 2?
8. What is hash spreading and why is it needed?
9. What happens if `hashCode()` returns the same value for all objects?
10. Can hashCode be negative? How does HashMap handle it?

## Collision Handling
11. What happens when two keys have the same hashCode?
12. How are collisions handled before Java 8 vs after Java 8?
13. What is treeification in HashMap?
14. Why is the treeify threshold 8 and untreeify threshold 6?
15. Under what conditions does HashMap convert a bucket to a tree?

## equals() & hashCode() Edge Cases
16. What happens if you override `equals()` but not `hashCode()`?
17. What happens if you override `hashCode()` but not `equals()`?
18. Can two unequal objects have the same hashCode?
19. Can two equal objects have different hashCodes?
20. What happens if `equals()` is inconsistent?

## Tricky Code-Based Questions
21. Predict output when inserting duplicate keys.
22. What happens if you mutate a key after inserting it into HashMap?
23. Why does `map.get(new Key(...))` sometimes return null even if logically equal?
24. What happens if key fields used in hashCode change?
25. Can HashMap store itself as a key?

## Internal Behavior
26. What is load factor and default value?
27. When does resizing happen?
28. What happens during rehashing?
29. Does resizing rehash all elements?
30. What is the default initial capacity?

## Null Handling
31. Why does HashMap allow only one null key?
32. How are null keys stored internally?
33. Why does Hashtable not allow null keys/values?

## Performance & Optimization
34. What causes performance degradation in HashMap?
35. How does a bad hashCode affect performance?
36. Why is 0.75 used as default load factor?
37. What is the worst-case time complexity of HashMap?
38. How does Java 8 improve worst-case performance?

## Concurrency & Threading
39. Is HashMap thread-safe?
40. What issues occur if multiple threads use HashMap?
41. What was the infinite loop issue in pre-Java 8 HashMap?
42. Difference between HashMap and ConcurrentHashMap?
43. Why doesn’t ConcurrentHashMap allow null keys?

## Comparison Questions
44. HashMap vs Hashtable?
45. HashMap vs LinkedHashMap?
46. HashMap vs TreeMap?
47. When would you prefer TreeMap over HashMap?
48. Difference between identity-based and equals-based maps?

## Advanced / Deep Dive
49. How does HashMap resize internally (step-by-step)?
50. What is the role of bitwise operations in HashMap?
51. How does HashMap maintain performance during high collisions?
52. What is the structure of Node in HashMap?
53. How does HashMap handle memory vs performance trade-offs?

## Real-world / Design Thinking
54. When would HashMap perform poorly in production?
55. How would you design a better hash function?
56. What happens if all keys land in one bucket?
57. Can HashMap be used as a cache? What are limitations?
58. How would you debug a slow HashMap?