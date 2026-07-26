# Stream_01_Basics.java

## What This File Teaches

This file introduces the most common beginner Stream API operations:

- creating streams
- `forEach`
- `filter`
- `map`
- `flatMap`
- method references
- stream single-use behavior

## Creating a Stream Directly

```java
Stream<Integer> numStream = Stream.of(1, 2, 3, 4, 5);
```

`Stream.of(...)` creates a stream directly from values.

Important: a stream can be consumed only once. Once a terminal operation like `forEach`, `count`, or `collect` runs, the same stream cannot be used again.

In this file, the first `forEach` on `numStream` is commented out because using it there would close the stream and the later filter pipeline would fail.

## Creating a Stream From a List

```java
List<String> stringList = new ArrayList<>(List.of("a", "b", "c", "d"));
Stream<String> stream2 = stringList.stream();
```

Collections like `List`, `Set`, and `Map` views can create streams.

## `forEach`

```java
stream2.forEach(s -> System.out.print(s + ", "));
```

`forEach` is a terminal operation. It performs an action for each element and ends the stream.

## `filter`

```java
numStream
        .filter(num -> num % 2 == 0)
        .filter(num -> num > 5)
        .forEach(num -> System.out.print(num + ", "));
```

`filter` keeps elements that match a condition.

This pipeline means:

1. Keep even numbers.
2. From those, keep numbers greater than 5.
3. Print them.

The condition passed to `filter` is a `Predicate`.

## `map`

```java
nameList.stream()
        .map(String::toUpperCase)
        .forEach(System.out::println);
```

`map` transforms each element into another value.

Here each `String` becomes its uppercase version.

Important: `map` does not change the original list. It creates transformed values in the stream pipeline.

```java
nameList.stream()
        .map(String::length)
        .forEach(System.out::println);
```

This transforms each `String` into an `Integer` length.

## `flatMap`

The file creates a nested list:

```java
List<List<String>> listOfString
```

This means the stream type is:

```java
Stream<List<String>>
```

If you want to work with each string inside all inner lists, use `flatMap`.

```java
listOfString.stream()
        .flatMap(Collection::stream)
        .forEach(e -> System.out.print(e + ", "));
```

`flatMap` converts:

```text
Stream<List<String>> -> Stream<String>
```

This is useful whenever you have nested data.

## Filtering Inside `flatMap`

```java
listOfString.stream()
        .flatMap(arr -> arr.stream().filter(e -> e.equals("a")))
        .forEach(System.out::println);
```

Each inner list is converted to a stream, filtered to keep only `"a"`, and then flattened into one stream.

## Important Takeaway

Use `map` when one input becomes one output. Use `flatMap` when one input contains many nested outputs and you want one flat stream.
