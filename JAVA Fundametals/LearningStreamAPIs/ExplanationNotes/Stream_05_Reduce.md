# Stream_05_Reduce.java

## What This File Teaches

This file introduces reduction: converting many stream elements into one result.

Examples:

- total salary
- maximum salary
- sum using `reduce`
- sum using `sum`

## What Is Reduction?

Reduction means combining many values into one value.

Examples:

```text
1, 2, 3, 4 -> 10
Dave, Joe, Ryan -> one selected employee
100, 200, 300 -> 300
```

## Mapping Employees to Salaries

```java
list.stream()
        .mapToInt(Employee::getSal)
```

The stream starts as:

```java
Stream<Employee>
```

After `mapToInt`:

```java
IntStream
```

Now the stream contains salary numbers instead of employee objects.

## `reduce` Without Identity

```java
OptionalInt total = list.stream()
        .mapToInt(Employee::getSal)
        .reduce((p, q) -> p + q);
```

This adds salaries pair by pair.

The accumulator:

```java
(p, q) -> p + q
```

means:

```text
take previous result p and next value q, then add them
```

Because there is no starting value, Java returns `OptionalInt`. If the stream were empty, there would be no result.

## `sum`

```java
int total2 = list.stream()
        .mapToInt(Employee::getSal)
        .sum();
```

For numeric streams, `sum()` is clearer than manual `reduce`.

Use `sum()` when you are simply adding numbers.

## `reduce` With Identity

```java
int total3 = list.stream()
        .mapToInt(Employee::getSal)
        .reduce(1, (p, q) -> p + q);
```

The identity is the starting value.

Here the sum starts at `1`, so the result is one more than the real salary total.

Usually for addition, the identity should be `0`:

```java
reduce(0, (p, q) -> p + q)
```

## `max`

```java
OptionalInt maxSal = list.stream()
        .mapToInt(Employee::getSal)
        .max();
```

`max()` finds the largest salary.

It returns `OptionalInt` because the stream might be empty.

## Important Takeaway

Use `reduce` when you need custom combining logic. For common numeric operations, prefer built-in methods like `sum`, `max`, `min`, and `average`.
