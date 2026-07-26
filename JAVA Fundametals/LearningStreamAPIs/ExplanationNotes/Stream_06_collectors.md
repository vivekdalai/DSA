# Stream_06_collectors.java

## What This File Teaches

This file explains collectors: how to convert stream results into collections or maps.

It covers:

- `toList`
- `Collectors.toSet`
- `Collectors.toMap`
- resolving duplicate map keys

## Collecting Names Into a List

```java
List<String> empNames = list.stream()
        .map(Employee::getName)
        .toList();
```

This pipeline:

1. Streams employees.
2. Converts each employee into a name.
3. Stores the names in a list.

`toList()` is a terminal operation.

## Collecting Names Into a Set

```java
Set<String> empNames2 = list.stream()
        .map(Employee::getName)
        .collect(Collectors.toSet());
```

A `Set` stores unique values only. If names repeat, duplicate names appear only once.

## Creating a Map

```java
Map<String, Integer> empMap = list.stream()
        .collect(Collectors.toMap(s -> s.name, s -> s.sal));
```

This creates a map where:

- key = employee name
- value = employee salary

Equivalent method reference version:

```java
Collectors.toMap(Employee::getName, Employee::getSal)
```

## Duplicate Key Problem

After this line:

```java
list.add(new Employee("Dave", 500));
```

There are two employees named `"Dave"`.

If you use simple `toMap`, Java does not know which salary to keep and throws an exception.

So the file provides a merge function.

## Keeping the First Salary

```java
Map<String, Integer> newMap = list.stream()
        .collect(Collectors.toMap(
                Employee::getName,
                Employee::getSal,
                (s1, s2) -> s1
        ));
```

If duplicate keys appear:

- `s1` is the existing value.
- `s2` is the new value.

Returning `s1` keeps the first salary.

## Keeping the Maximum Salary

```java
Map<String, Integer> newMap3 = list.stream()
        .collect(Collectors.toMap(
                Employee::getName,
                Employee::getSal,
                (s1, s2) -> Math.max(s1, s2)
        ));
```

This keeps the larger salary when duplicate names appear.

## Important Takeaway

When using `Collectors.toMap`, always think about duplicate keys. If duplicates are possible, provide a merge function.
