# Stream_04_Matching.java

## What This File Teaches

This file explains stream matching and finding operations:

- `anyMatch`
- `allMatch`
- `noneMatch`
- `filter`
- `findAny`
- `Optional`

These operations are useful when you want a yes/no answer or one matching element.

## `anyMatch`

```java
boolean anyMatch = list.stream()
        .anyMatch(p -> p.country.equals("IND"));
```

`anyMatch` returns `true` if at least one element matches the condition.

For this list, one person has country `"IND"`, so the result is `true`.

```java
list.stream().anyMatch(p -> p.country.equals("EUR"))
```

No person has country `"EUR"`, so the result is `false`.

## `allMatch`

```java
boolean allMatch = list.stream()
        .allMatch(p -> p.age > 18);
```

`allMatch` returns `true` only if every element satisfies the condition.

In the file, all people are older than 18, so this returns `true`.

## `noneMatch`

```java
list.stream().noneMatch(p -> p.country.equals("EUR"))
```

`noneMatch` returns `true` if no elements match the condition.

Since no person has country `"EUR"`, this returns `true`.

## Short-Circuiting

`anyMatch`, `allMatch`, and `noneMatch` are short-circuiting terminal operations.

That means Java can stop early:

- `anyMatch` can stop after the first match.
- `allMatch` can stop after the first failure.
- `noneMatch` can stop after the first match.

## `findAny`

```java
Optional<Person> p5 = list.stream()
        .filter(p -> p.country.equals("IND"))
        .findAny();
```

This keeps only people from India and then returns any one matching person.

The result is wrapped in `Optional<Person>` because there may be no match.

## Optional

```java
if (p5.isPresent()) {
    System.out.println(p5.get());
}
```

`Optional` is a container that may or may not hold a value.

This prevents directly assuming that a matching person exists.

Modern style often uses:

```java
p5.ifPresent(System.out::println);
```

## Important Takeaway

Use matching operations when you need a boolean answer. Use `findAny` or `findFirst` when you need one matching element.
