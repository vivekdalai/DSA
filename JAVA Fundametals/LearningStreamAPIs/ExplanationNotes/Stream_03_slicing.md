# Stream_03_slicing.java

## What This File Teaches

This file introduces stream slicing operations:

- `distinct`
- `limit`
- `skip`
- `Collectors.joining`

Slicing means taking only part of a stream.

## `distinct`

```java
countries.stream()
        .distinct()
        .forEach(System.out::println);
```

`distinct` removes duplicates.

Input:

```text
India, USA, China, India, UK, China
```

After `distinct`:

```text
India, USA, China, UK
```

For strings, `distinct` uses `equals` to decide whether two values are duplicates.

## Joining Stream Values

```java
String result = countries.stream()
        .distinct()
        .collect(Collectors.joining(","));
```

`Collectors.joining(",")` joins stream elements into one string separated by commas.

Result:

```text
India,USA,China,UK
```

## `limit`

```java
String result2 = countries.stream()
        .distinct()
        .limit(2)
        .collect(Collectors.joining(","));
```

`limit(2)` keeps only the first two elements from the stream after duplicates are removed.

After `distinct`:

```text
India, USA, China, UK
```

After `limit(2)`:

```text
India, USA
```

## `skip`

```java
String result3 = countries.stream()
        .distinct()
        .skip(2)
        .collect(Collectors.joining(","));
```

`skip(2)` ignores the first two elements from the stream after duplicates are removed.

After `distinct`:

```text
India, USA, China, UK
```

After `skip(2)`:

```text
China, UK
```

## Order Matters

This file applies `distinct` before `limit` or `skip`.

That is different from applying `limit` before `distinct`.

Example:

```java
countries.stream().limit(2).distinct()
```

This would first take `India, USA`, then remove duplicates.

```java
countries.stream().distinct().limit(2)
```

This first removes duplicates from the whole stream, then takes the first two unique countries.

## Important Takeaway

`distinct`, `limit`, and `skip` are intermediate operations, and their order changes the final result.
