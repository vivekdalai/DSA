# Java Stream API Learning Notes

This file summarizes the Stream API topics already covered inside `src/LearningStreamAPIs` and turns them into a quick revision guide.

## Files Covered

File

Topic

`Stream_01_Basics.java`

stream creation, one-time use, `filter`, `map`, `flatMap`

`Stream_02_Basics.java`

`mapToInt`, primitive streams, `count`

`Stream_03_slicing.java`

`distinct`, `limit`, `skip`

`Stream_04_Matching.java`

`anyMatch`, `allMatch`, `noneMatch`, `findAny`, `Optional`

`Stream_05_Reduce.java`

`reduce`, `sum`, `max`

`Stream_06_collectors.java`

`toList`, `toSet`, `toMap`, duplicate key merge function

`Stream_07_Aggregation.java`

`count`, `averagingInt`, `minBy`, `max`, `joining`

`Stream_08_Grouping.java`

`groupingBy`, nested grouping, counting, summing, `maxBy`

`Stream_09_Parallel.java`

parallel streams, lazy evaluation, `findFirst`

`Streams_01_findDuplicates.java`

duplicate detection, `groupingBy + counting`, unique elements

`Predicate_01_learn.java`

`Predicate`, `and`, `or`, `negate`, `isEqual`

`Supplier_01_learn.java`

`Supplier`, `get()`, helper functional interfaces

## What You Have Learned

### 1. What a Stream Is

-   A stream is a sequence of elements processed in a declarative way.
-   Streams do not store data. They operate on data from collections, arrays, or generated values.
-   A stream can be consumed only once.

Example idea from your code:

```java
Stream<Integer> numStream = Stream.of(1,2,3,4,5);
```

## 2. How Streams Are Created

You used these creation styles:

-   `Stream.of(...)`
-   `list.stream()`
-   stream from nested collections using `flatMap`

Examples:

```java
Stream<Integer> stream = Stream.of(1, 2, 3);
List<String> names = List.of("A", "B", "C");
Stream<String> nameStream = names.stream();
```

## 3. Intermediate Operations

Intermediate operations return another stream and are lazy.

### `filter`

-   Used to keep only elements matching a condition.
-   Example from your code: even numbers, values greater than 5, employees with salary criteria.

```java
numStream.filter(num -> num % 2 == 0)
```

### `map`

-   Transforms each element into another value.
-   You used it to:
-   convert strings to uppercase
-   get string length
-   extract employee names

```java
nameList.stream().map(String::toUpperCase)
```

### `flatMap`

-   Used when each element itself contains multiple values.
-   Flattens nested structures into a single stream.
-   Example: `List<List<String>>` to `Stream<String>`

```java
listOfString.stream().flatMap(Collection::stream)
```

### `distinct`

-   Removes duplicates.

```java
countries.stream().distinct()
```

### `limit`

-   Returns only the first `n` elements.

```java
countries.stream().distinct().limit(2)
```

### `skip`

-   Skips the first `n` elements.

```java
countries.stream().distinct().skip(2)
```

## 4. Terminal Operations

Terminal operations end the stream pipeline and produce a result or side effect.

### `forEach`

-   Used to print or perform an action on every element.

### `count`

-   Returns how many elements match after filtering.

```java
long count = list.stream().filter(e -> e.getAge() > 30).count();
```

### `findAny`

-   Returns an `Optional<T>`.
-   Useful when any matching element is enough.

### `findFirst`

-   Returns the first matching element.
-   In your parallel/lazy evaluation file, it helps stop work early once a match is found.

### Matching Operations

#### `anyMatch`

-   True if at least one element matches.

#### `allMatch`

-   True if all elements match.

#### `noneMatch`

-   True if no element matches.

## 5. Primitive Streams

You used `mapToInt` to avoid unnecessary boxing and to perform numeric operations easily.

Common benefits:

-   better performance than `Stream<Integer>` in numeric pipelines
-   direct numeric operations like `sum()`, `max()`, `average()`

Example:

```java
list.stream().mapToInt(Person::getAge)
```

## 6. Reduce and Aggregation

### `reduce`

-   Used to combine stream elements into a single result.
-   Can be used for total sum, multiplication, string combination, max, min, and custom accumulation.

Forms you learned:

```java
reduce((a, b) -> a + b)
reduce(identity, (a, b) -> a + b)
```

From your code:

-   salary total using `reduce`
-   salary total using `sum()`
-   max salary using `max()`

Important idea:

-   Without identity, `reduce` returns `Optional` or primitive optional like `OptionalInt`.
-   With identity, it returns a direct value.

## 7. Collectors

Collectors are heavily used in interviews because they convert or aggregate stream results cleanly.

### `toList`

```java
list.stream().map(Employee::getName).toList()
```

### `toSet`

-   Good for unique results.

### `toMap`

You learned:

-   how to create a map from a list
-   how to handle duplicate keys using a merge function

```java
Collectors.toMap(Employee::getName, Employee::getSal, (s1, s2) -> s1)
```

Useful duplicate-key strategies:

-   keep first value
-   keep latest value
-   keep max value
-   merge values in custom way

## 8. Aggregation Collectors

You covered several useful aggregation collectors:

-   `counting()`
-   `averagingInt()`
-   `summingInt()`
-   `minBy()`
-   `maxBy()`
-   `joining()`

Examples from your code:

-   average employee salary
-   minimum salary employee
-   maximum age employee
-   joining strings with spaces

## 9. Grouping

Grouping is one of the most important interview topics in Streams.

### Basic Grouping

```java
Collectors.groupingBy(Employee::getDept)
```

This gives:

```java
Map<String, List<Employee>>
```

### Nested Grouping

You also learned grouping by department and then age:

```java
Collectors.groupingBy(Employee::getDept,
    Collectors.groupingBy(Employee::getAge))
```

### Grouping with Downstream Collectors

You used:

-   `groupingBy(..., counting())`
-   `groupingBy(..., summingInt(...))`
-   `groupingBy(..., maxBy(...))`

This is a very strong pattern for interviews.

## 10. Parallel Streams

You learned:

-   serial stream runs on the main thread
-   parallel stream can run on multiple threads
-   output order may not remain predictable with `forEach`

Example:

```java
Stream.of(1,2,3,4,5).parallel().forEach(...)
```

Important note:

-   Parallel streams are not always faster.
-   They help only when the task is large enough, independent, and safe for parallel execution.
-   Avoid them when shared mutable state is involved.

## 11. Lazy Evaluation

Streams are lazy. Intermediate operations do not execute until a terminal operation is called.

Your example with chained filters and `findFirst()` shows short-circuiting:

-   elements are processed only as needed
-   work stops once the result is found

This is an important concept for debugging and optimization.

## 12. Functional Interfaces Used Around Streams

Although not all of these are stream operations, they support stream-style programming.

### `Predicate<T>`

-   returns `boolean`
-   used for conditions in `filter`, `anyMatch`, `allMatch`, `noneMatch`

Methods you practiced:

-   `test`
-   `and`
-   `or`
-   `negate`
-   `Predicate.isEqual(...)`

### `Supplier<T>`

-   supplies a value when `get()` is called
-   useful for deferred object creation or default object generation

## 13. Practical Problems Solved

From your practice files and notes, you have already touched these common patterns:

-   find duplicate elements in a list
-   remove duplicate elements
-   count frequency of values
-   extract matching objects from a list
-   join strings into one output
-   find max/min based on a field
-   group employees by department
-   total salary by department
-   find employee records with a condition

## 14. Common Patterns to Remember

### Find duplicates in a list

```java
Map<Integer, Long> freq = nums.stream()
    .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

List<Integer> duplicates = freq.entrySet().stream()
    .filter(entry -> entry.getValue() > 1)
    .map(Map.Entry::getKey)
    .toList();
```

### Remove duplicates

```java
List<Integer> unique = nums.stream().distinct().toList();
```

### Convert list to map

```java
Map<String, Integer> map = employees.stream()
    .collect(Collectors.toMap(Employee::getName, Employee::getSal));
```

### Group by department

```java
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept));
```

### Get total salary

```java
int total = employees.stream()
    .mapToInt(Employee::getSal)
    .sum();
```

## 15. Pitfalls and Best Practices

-   A stream cannot be reused after a terminal operation.
-   Prefer `mapToInt`, `mapToLong`, `mapToDouble` for numeric calculations.
-   Use `Optional` safely. Avoid calling `get()` without checking.
-   `forEach` is fine for printing, but prefer collectors when you want structured output.
-   `toMap()` throws an exception on duplicate keys unless you provide a merge function.
-   `parallelStream()` is not a default optimization. Use it carefully.
-   Avoid mutating shared objects inside stream operations.
-   Keep stream pipelines readable. A simple loop is sometimes clearer.

## Frequently Asked Interview Questions

Detailed answers and sample solutions for this section are stored in `CommonQuestions.md`.

### Conceptual Questions

1.  What is the Java Stream API?
2.  What is the difference between a Collection and a Stream?
3.  Why can a stream be consumed only once?
4.  What is the difference between intermediate and terminal operations?
5.  Why are streams called lazy?
6.  What is the difference between `map()` and `flatMap()`?
7.  What is the difference between `findFirst()` and `findAny()`?
8.  What is the difference between `anyMatch()`, `allMatch()`, and `noneMatch()`?
9.  What is the difference between `Stream<T>` and primitive streams like `IntStream`?
10.  What is the difference between `reduce()` and `collect()`?
11.  When should you use `reduce()` and when should you use `sum()` or collectors?
12.  What happens if duplicate keys appear in `Collectors.toMap()`?
13.  What is `Optional` and why is it useful with streams?
14.  What is the use of `groupingBy()`?
15.  What are downstream collectors?
16.  What is the difference between `sorted()`, `distinct()`, `limit()`, and `skip()`?
17.  What are stateless vs stateful intermediate operations?
18.  What is short-circuiting in streams?
19.  What is the difference between `stream()` and `parallelStream()`?
20.  When should parallel streams be avoided?

### Coding Questions

1.  Find duplicate elements in a list using streams.
2.  Remove duplicates from a list.
3.  Count the frequency of each element in a list.
4.  Find the first non-repeated character in a string.
5.  Convert a list of employees to a map of name to salary.
6.  Handle duplicate keys while converting a list to a map.
7.  Group employees by department.
8.  Find the highest salary employee.
9.  Find all employees having the maximum age.
10.  Find the total salary paid in each department.
11.  Find the average salary of employees.
12.  Partition numbers into even and odd.
13.  Join a list of strings into a sentence.
14.  Find the longest word in a sentence.
15.  Filter employees whose salary is greater than a given value.
16.  Find whether any employee belongs to a specific department.
17.  Find all unique characters from a list of words.
18.  Flatten a nested list using `flatMap`.
19.  Sort employees by salary and then age.
20.  Find top `N` highest values from a list.

## Quick Revision Cheat Sheet

```java
// filter
list.stream().filter(x -> condition)

// map
list.stream().map(x -> transform)

// flatMap
nestedList.stream().flatMap(Collection::stream)

// count
list.stream().filter(x -> condition).count()

// numeric conversion
list.stream().mapToInt(Person::getAge).sum()

// reduce
list.stream().reduce((a, b) -> ...)

// collect to list
list.stream().toList()

// collect to set
list.stream().collect(Collectors.toSet())

// collect to map
list.stream().collect(Collectors.toMap(k -> ..., v -> ...))

// grouping
list.stream().collect(Collectors.groupingBy(x -> ...))
```

## Good Next Topics to Learn

-   `sorted()` with custom comparators
-   `partitioningBy()`
-   `collectingAndThen()`
-   `mapping()` downstream collector
-   `flatMapping()` downstream collector
-   `teeing()` collector
-   `Optional` best practices
-   stream vs loop tradeoffs in real code

## Final Summary

You have already covered the most important Stream API building blocks:

-   creating streams
-   filtering and transforming data
-   flattening nested collections
-   slicing streams
-   matching and searching
-   reduce and numeric aggregation
-   collectors
-   grouping
-   duplicate handling
-   parallel stream basics
-   supporting functional interfaces like `Predicate` and `Supplier`

This is already a strong foundation for both interviews and real Java coding. The next step is to solve more real-world questions using combinations of `filter`, `map`, `groupingBy`, `counting`, `summingInt`, `maxBy`, and `toMap`.
