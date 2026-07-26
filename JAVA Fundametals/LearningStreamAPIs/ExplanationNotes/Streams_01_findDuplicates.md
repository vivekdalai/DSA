# Streams_01_findDuplicates.java

## What This File Teaches

This file compares traditional collection logic with Stream API logic for counting frequencies and finding duplicates.

It covers:

- `HashMap` frequency counting
- `TreeMap` with custom comparator
- `Collectors.groupingBy`
- `Collectors.counting`
- filtering map entries
- mapping entries to keys
- collecting to a list or set
- `Predicate<Integer>`

## Traditional Frequency Count

```java
Map<Integer, Integer> freqCount = new HashMap<>();

for (int n : nums) {
    freqCount.put(n, freqCount.getOrDefault(n, 0) + 1);
}
```

This manually counts how many times each number appears.

`getOrDefault(n, 0)` means:

```text
If n already exists, get its count.
If n does not exist, start from 0.
```

Then `+ 1` increments the count.

## TreeMap With Descending Order

```java
Map<Integer, Integer> treeMapCount = new TreeMap<>((a, b) -> Integer.compare(b, a));
```

`TreeMap` stores keys in sorted order. The comparator reverses the natural order, so keys are sorted descending.

## Stream-Based Frequency Count

```java
Map<Integer, Long> newMap = nums.stream()
        .collect(Collectors.groupingBy(n -> n, Collectors.counting()));
```

This is the Stream API way to count frequencies.

Breakdown:

```java
nums.stream()
```

Creates a stream from the list.

```java
Collectors.groupingBy(n -> n, ...)
```

Groups equal numbers together. The number itself becomes the map key.

```java
Collectors.counting()
```

Counts how many values exist in each group.

Result type:

```java
Map<Integer, Long>
```

The key is the number. The value is the count.

## Finding Duplicate Numbers

```java
newMap.entrySet().stream()
        .filter(entry -> entry.getValue() > 1)
        .forEach(entry -> System.out.print(entry.getKey() + ", "));
```

This turns the map entries into a stream, keeps only entries where the count is greater than `1`, and prints the key.

```java
List<Integer> duplicates = newMap.entrySet().stream()
        .filter(entry -> entry.getValue() > 1)
        .map(Map.Entry::getKey)
        .toList();
```

This does the same thing but stores the duplicate numbers in a list.

Important distinction:

- `filter` keeps or removes elements.
- `map` transforms each element.

Here, `map(Map.Entry::getKey)` converts each `Map.Entry<Integer, Long>` into just its integer key.

## Predicate Example

```java
Predicate<Integer> isEven = n -> n % 2 == 0;
```

This is a reusable boolean condition. It returns `true` for even numbers.

## Set Example

```java
Set<Integer> hashSet = new HashSet<>();
Set<Integer> finalHashSet = nums.stream()
        .filter(n -> hashSet.add(n))
        .collect(Collectors.toSet());
```

`hashSet.add(n)` returns:

- `true` if the value was not already present
- `false` if the value already existed

So this keeps only the first occurrence of each number.

Note: This approach depends on side effects inside `filter`, which is okay for learning but should be used carefully in real projects. For unique values, prefer `distinct()` when possible.

## Important Takeaway

`groupingBy(..., counting())` is the standard Stream API pattern for frequency counting.
