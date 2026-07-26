# Stream_02_Basics.java

## What This File Teaches

This file shows how to stream custom objects and extract primitive values from them.

It covers:

- streaming a `List<Person>`
- `mapToInt`
- method references
- `count`
- filtering objects by fields

## Streaming Custom Objects

```java
List<Person> list = new ArrayList<>();
list.add(new Person("A", 12));
list.add(new Person("B", 45));
```

The list contains `Person` objects. A stream over this list has this type:

```java
Stream<Person>
```

Each stream element is one `Person`.

## `mapToInt`

```java
list.stream()
        .mapToInt(p -> p.getAge())
        .forEach(System.out::println);
```

`mapToInt` transforms each `Person` into an `int`.

Result:

```text
Stream<Person> -> IntStream
```

`IntStream` is a primitive stream. It is useful because it has numeric operations like `sum`, `average`, `max`, and `min`.

## Method Reference

```java
list.stream()
        .mapToInt(Person::getAge)
        .forEach(e -> System.out.println("Age : " + e));
```

This is equivalent to:

```java
p -> p.getAge()
```

`Person::getAge` is shorter and more readable when the lambda only calls one method.

## Counting Filtered Objects

```java
long count = list.stream()
        .filter(e -> e.getAge() > 30)
        .count();
```

This pipeline means:

1. Create a stream of people.
2. Keep only people whose age is greater than 30.
3. Count how many remain.

`count()` is a terminal operation and returns `long`.

## Important Takeaway

When working with object streams, use `mapToInt`, `mapToLong`, or `mapToDouble` when you want numeric calculations from object fields.
