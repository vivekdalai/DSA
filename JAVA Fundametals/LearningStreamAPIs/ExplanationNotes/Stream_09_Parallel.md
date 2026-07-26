# Stream_09_Parallel.java

## What This File Teaches

This file demonstrates:

- serial streams
- parallel streams
- thread names
- lazy evaluation
- short-circuiting with `findFirst`

## Serial Stream

```java
Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
        .forEach(num -> System.out.println(Thread.currentThread().getName() + " :: " + num));
```

In a normal stream, work usually happens on the main thread.

The output will commonly show:

```text
main :: 1
main :: 2
...
```

## Parallel Stream

```java
Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
        .parallel()
        .forEach(num -> System.out.println(Thread.currentThread().getName() + " :: " + num));
```

`parallel()` allows Java to process stream elements across multiple threads.

The output order is not guaranteed with `forEach` on a parallel stream. You may see numbers printed in a different order.

If order matters, use `forEachOrdered`, but that may reduce the benefit of parallelism.

## When Parallel Streams Help

Parallel streams can help when:

- the data set is large
- each element takes meaningful CPU work
- operations are independent
- there are no shared mutable side effects

Parallel streams can hurt performance when:

- the data set is small
- work per element is tiny
- operations depend on shared mutable state
- ordering is required

## Lazy Evaluation

```java
int val = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
        .filter(e -> {
            System.out.println("First filter : " + e);
            return e > 5;
        })
        .filter(e -> {
            System.out.println("Second filter : " + e);
            return e % 7 == 0;
        })
        .findFirst()
        .orElse(-1);
```

Streams are lazy. The filters do not process the entire stream immediately.

Elements move through the pipeline one at a time as much as possible.

For each number:

1. First filter checks whether the number is greater than 5.
2. If it passes, second filter checks whether it is divisible by 7.
3. `findFirst` stops when the first matching value is found.

The first value greater than 5 and divisible by 7 is `7`, so the final value is `7`.

## Important Takeaway

Parallel streams can change execution order. Lazy stream pipelines only do the work needed by the terminal operation.
