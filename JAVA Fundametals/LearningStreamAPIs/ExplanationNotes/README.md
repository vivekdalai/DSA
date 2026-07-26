# Learning Stream APIs - Explanation Notes

This folder contains explanation notes for each Java example in `LearningStreamAPIs`.
Read them in this order if you are learning the topic step by step:

1. `Predicate_01_learn.md`
2. `Supplier_01_learn.md`
3. `Streams_01_findDuplicates.md`
4. `Stream_01_Basics.md`
5. `Stream_02_Basics.md`
6. `Stream_03_slicing.md`
7. `Stream_04_Matching.md`
8. `Stream_05_Reduce.md`
9. `Stream_06_collectors.md`
10. `Stream_07_Aggregation.md`
11. `Stream_08_Grouping.md`
12. `Stream_09_Parallel.md`
13. `Stream_10_practice.md`

## Core Mental Model

A Java stream is a pipeline over data:

```java
source.stream()
      .intermediateOperation(...)
      .intermediateOperation(...)
      .terminalOperation(...);
```

- Source: where data comes from, such as `List`, array, or `Stream.of(...)`.
- Intermediate operations: return another stream, such as `filter`, `map`, `distinct`, `sorted`, `limit`, `skip`, `peek`.
- Terminal operations: produce the final result or side effect, such as `forEach`, `count`, `collect`, `reduce`, `sum`, `toList`, `findAny`.

Streams are lazy. Intermediate operations do not run until a terminal operation is called.
Streams are also single-use. After a terminal operation, the same stream object cannot be reused.
