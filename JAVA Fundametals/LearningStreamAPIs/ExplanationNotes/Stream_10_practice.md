# Stream_10_practice.java

## What This File Teaches

This file contains practice examples that combine several Stream API ideas:

- primitive streams from arrays
- object streams from collections
- `distinct`
- `sorted`
- `peek`
- `sum`
- descending sort
- stream operation order
- `min`
- `Optional`

Only `practice3()` is currently called from `main`.

## Practice 1: Array to `IntStream`

```java
int[] arr = new int[]{4, 1, 5, 7, ...};
IntStream integerStream = Arrays.stream(arr);
```

For primitive arrays like `int[]`, `Arrays.stream(arr)` returns `IntStream`.

```java
long sum = integerStream.distinct()
        .sorted()
        .peek(System.out::println)
        .sum();
```

Pipeline:

1. `distinct()` removes duplicate numbers.
2. `sorted()` sorts numbers in ascending order.
3. `peek(System.out::println)` prints each value as it passes through.
4. `sum()` adds the values.

`peek` is mainly useful for debugging. Avoid using it as your main business logic.

## Practice 2: Collection to `Stream<Integer>`

```java
List<Integer> integers = new ArrayList<>(List.of(...));
Stream<Integer> integerStream = integers.stream();
```

For collections, `.stream()` returns `Stream<Integer>`, not `IntStream`.

```java
List<Integer> distinctEven = integerStream.distinct()
        .filter(e -> e % 2 == 0)
        .sorted((a, b) -> Integer.compare(b, a))
        .toList();
```

Pipeline:

1. Remove duplicates.
2. Keep even numbers.
3. Sort descending.
4. Store result in a list.

Descending sort:

```java
(a, b) -> Integer.compare(b, a)
```

This compares `b` before `a`, reversing the natural ascending order.

## Practice 3: Operation Sequence

```java
Stream<Integer> arrStream = arrList.stream()
        .filter(e -> e > 3)
        .peek(e -> System.out.println("peek after filter: " + e))
        .map(e -> e * -1)
        .peek(e -> System.out.println("peek after map: " + e))
        .sorted()
        .peek(e -> System.out.println("peek after sorted: " + e));
```

This demonstrates how stream operations flow.

`filter` and `map` can process one element at a time.

`sorted` needs the complete stream before it can emit sorted values.

That is why output appears like this:

```text
peek after filter
peek after map
peek after filter
peek after map
...
peek after sorted
peek after sorted
...
```

The stream first filters and maps eligible values. Then `sorted` collects those mapped values, sorts them, and sends them forward.

## Terminal Operation: `toList`

```java
List<Integer> result = arrStream.toList();
```

This triggers the stream pipeline. Without a terminal operation, none of the earlier operations would run.

## Finding Minimum Value

```java
Optional<Integer> minVal = arrList.stream()
        .filter(e -> e > 4)
        .min(Integer::compare);
```

This keeps values greater than 4 and finds the minimum among them.

The result is `Optional<Integer>` because there may be no value greater than 4.

The file uses:

```java
minVal.get()
```

That works only when a value is present. Safer patterns are:

```java
minVal.ifPresent(System.out::println);
```

or:

```java
int value = minVal.orElse(-1);
```

## Important Takeaway

Stream pipelines are lazy and run only when a terminal operation is called. Operations like `sorted` need the full stream, while `filter` and `map` can process element by element.
