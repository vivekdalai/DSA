# Java Stream API Interview Questions and Answers

This file contains answers for the interview questions listed in `streamLearning.md`.

## Conceptual Questions

### 1. What is the Java Stream API?

The Java Stream API is a feature introduced in Java 8 for processing collections of data in a declarative and functional style. It allows operations like filtering, mapping, grouping, sorting, and reducing without manually writing loops.

### 2. What is the difference between a Collection and a Stream?

- A `Collection` stores data in memory.
- A `Stream` does not store data. It processes data from a source such as a collection, array, or generator.
- A collection can be traversed multiple times.
- A stream is usually consumed only once.
- Collections focus on data storage. Streams focus on data processing.

### 3. Why can a stream be consumed only once?

A stream represents a pipeline of operations over a source. After a terminal operation like `forEach`, `collect`, or `count` runs, the stream is considered consumed and closed. Reusing it throws `IllegalStateException`.

### 4. What is the difference between intermediate and terminal operations?

- Intermediate operations return another stream and are lazy.
  Examples: `filter`, `map`, `flatMap`, `distinct`, `sorted`, `limit`, `skip`
- Terminal operations produce the final result or side effect.
  Examples: `collect`, `forEach`, `count`, `reduce`, `findFirst`

### 5. Why are streams called lazy?

Intermediate stream operations are not executed immediately. They run only when a terminal operation is called. This improves efficiency and enables optimizations like short-circuiting.

### 6. What is the difference between `map()` and `flatMap()`?

- `map()` transforms each element into exactly one result.
- `flatMap()` transforms each element into a stream or collection and then flattens everything into one stream.

Example:

```java
List<String> names = List.of("a", "bb");
names.stream().map(String::length); // Stream<Integer>
```

```java
List<List<String>> nested = List.of(List.of("a", "b"), List.of("c"));
nested.stream().flatMap(List::stream); // Stream<String>
```

### 7. What is the difference between `findFirst()` and `findAny()`?

- `findFirst()` returns the first element according to encounter order.
- `findAny()` returns any matching element and may be faster in parallel streams.
- Both return `Optional<T>`.

### 8. What is the difference between `anyMatch()`, `allMatch()`, and `noneMatch()`?

- `anyMatch()` returns `true` if at least one element matches.
- `allMatch()` returns `true` if every element matches.
- `noneMatch()` returns `true` if no element matches.

### 9. What is the difference between `Stream<T>` and primitive streams like `IntStream`?

- `Stream<T>` works with objects.
- `IntStream`, `LongStream`, and `DoubleStream` work with primitive values.
- Primitive streams avoid boxing and unboxing overhead.
- They provide numeric helper methods like `sum()`, `average()`, and `max()`.

### 10. What is the difference between `reduce()` and `collect()`?

- `reduce()` combines elements into a single value.
  Example: sum, multiplication, finding max
- `collect()` gathers elements into a container or structured result.
  Example: list, set, map, grouped result

Use `reduce()` for one final combined value. Use `collect()` for mutable result containers or grouping logic.

### 11. When should you use `reduce()` and when should you use `sum()` or collectors?

- Use `sum()`, `max()`, `min()`, `average()` when built-in numeric methods already exist.
- Use `reduce()` when you need custom accumulation logic.
- Use collectors when the result is a list, set, map, grouped data, joined string, or aggregated structure.

### 12. What happens if duplicate keys appear in `Collectors.toMap()`?

If duplicate keys appear and no merge function is provided, `toMap()` throws `IllegalStateException`.

To fix it:

```java
Collectors.toMap(
    Employee::getName,
    Employee::getSal,
    Integer::max
)
```

### 13. What is `Optional` and why is it useful with streams?

`Optional<T>` is a container that may or may not hold a value. It helps avoid `null` handling issues and makes absence explicit. It is commonly returned by stream methods like `findFirst`, `findAny`, `min`, `max`, and `reduce` without identity.

### 14. What is the use of `groupingBy()`?

`groupingBy()` groups elements based on a classifier function and returns a map.

Example:

```java
Map<String, List<Employee>> byDept =
    employees.stream().collect(Collectors.groupingBy(Employee::getDept));
```

### 15. What are downstream collectors?

Downstream collectors are collectors passed inside another collector, usually inside `groupingBy()` or `partitioningBy()`.

Examples:

- `groupingBy(Employee::getDept, counting())`
- `groupingBy(Employee::getDept, summingInt(Employee::getSal))`
- `groupingBy(Employee::getDept, maxBy(...))`

### 16. What is the difference between `sorted()`, `distinct()`, `limit()`, and `skip()`?

- `sorted()` orders the elements.
- `distinct()` removes duplicates.
- `limit(n)` keeps the first `n` elements.
- `skip(n)` ignores the first `n` elements.

### 17. What are stateless vs stateful intermediate operations?

- Stateless operations process each element independently.
  Examples: `map`, `filter`
- Stateful operations need information about earlier elements.
  Examples: `distinct`, `sorted`, `limit`, `skip`

Stateful operations may need extra memory or coordination.

### 18. What is short-circuiting in streams?

Short-circuiting means the stream stops processing as soon as the result is known.

Examples:

- `findFirst()`
- `findAny()`
- `anyMatch()`
- `allMatch()`
- `noneMatch()`
- `limit()`

### 19. What is the difference between `stream()` and `parallelStream()`?

- `stream()` processes sequentially.
- `parallelStream()` splits work across multiple threads using the fork-join framework.
- Parallel streams may improve performance for large independent computations, but they can also add overhead.

### 20. When should parallel streams be avoided?

Avoid parallel streams when:

- the dataset is small
- order matters strictly
- shared mutable state exists
- the work per element is very light
- debugging simplicity is important
- the operation is I/O-heavy instead of CPU-heavy

## Coding Questions

### 1. Find duplicate elements in a list using streams.

```java
List<Integer> nums = List.of(1, 2, 3, 2, 4, 1, 5);

List<Integer> duplicates = nums.stream()
    .collect(Collectors.groupingBy(n -> n, Collectors.counting()))
    .entrySet()
    .stream()
    .filter(entry -> entry.getValue() > 1)
    .map(Map.Entry::getKey)
    .toList();
```

### 2. Remove duplicates from a list.

```java
List<Integer> unique = nums.stream()
    .distinct()
    .toList();
```

### 3. Count the frequency of each element in a list.

```java
Map<Integer, Long> frequency = nums.stream()
    .collect(Collectors.groupingBy(n -> n, Collectors.counting()));
```

### 4. Find the first non-repeated character in a string.

```java
String input = "swiss";

Character firstNonRepeated = input.chars()
    .mapToObj(c -> (char) c)
    .collect(Collectors.groupingBy(
        ch -> ch,
        LinkedHashMap::new,
        Collectors.counting()))
    .entrySet()
    .stream()
    .filter(entry -> entry.getValue() == 1)
    .map(Map.Entry::getKey)
    .findFirst()
    .orElse(null);
```

`LinkedHashMap` is used here so insertion order is preserved.

### 5. Convert a list of employees to a map of name to salary.

```java
Map<String, Integer> salaryMap = employees.stream()
    .collect(Collectors.toMap(Employee::getName, Employee::getSal));
```

### 6. Handle duplicate keys while converting a list to a map.

```java
Map<String, Integer> salaryMap = employees.stream()
    .collect(Collectors.toMap(
        Employee::getName,
        Employee::getSal,
        Integer::max
    ));
```

This keeps the maximum salary if duplicate names exist.

### 7. Group employees by department.

```java
Map<String, List<Employee>> byDept = employees.stream()
    .collect(Collectors.groupingBy(Employee::getDept));
```

### 8. Find the highest salary employee.

```java
Employee highest = employees.stream()
    .max(Comparator.comparingInt(Employee::getSal))
    .orElse(null);
```

### 9. Find all employees having the maximum age.

```java
int maxAge = employees.stream()
    .mapToInt(Employee::getAge)
    .max()
    .orElse(0);

List<Employee> oldest = employees.stream()
    .filter(e -> e.getAge() == maxAge)
    .toList();
```

### 10. Find the total salary paid in each department.

```java
Map<String, Integer> totalSalaryByDept = employees.stream()
    .collect(Collectors.groupingBy(
        Employee::getDept,
        Collectors.summingInt(Employee::getSal)
    ));
```

### 11. Find the average salary of employees.

```java
double avgSalary = employees.stream()
    .collect(Collectors.averagingInt(Employee::getSal));
```

### 12. Partition numbers into even and odd.

```java
Map<Boolean, List<Integer>> partitioned = nums.stream()
    .collect(Collectors.partitioningBy(n -> n % 2 == 0));
```

- `true` key contains even numbers
- `false` key contains odd numbers

### 13. Join a list of strings into a sentence.

```java
List<String> words = List.of("I", "am", "learning", "streams");

String sentence = words.stream()
    .collect(Collectors.joining(" "));
```

### 14. Find the longest word in a sentence.

```java
String sentence = "I am learning java streams";

String longest = Arrays.stream(sentence.split("\\s+"))
    .max(Comparator.comparingInt(String::length))
    .orElse("");
```

### 15. Filter employees whose salary is greater than a given value.

```java
int threshold = 50000;

List<Employee> filtered = employees.stream()
    .filter(e -> e.getSal() > threshold)
    .toList();
```

### 16. Find whether any employee belongs to a specific department.

```java
boolean exists = employees.stream()
    .anyMatch(e -> "ENGG".equals(e.getDept()));
```

### 17. Find all unique characters from a list of words.

```java
List<String> words = List.of("java", "stream");

List<Character> uniqueChars = words.stream()
    .flatMap(word -> word.chars().mapToObj(c -> (char) c))
    .distinct()
    .toList();
```

### 18. Flatten a nested list using `flatMap`.

```java
List<List<String>> nested = List.of(
    List.of("a", "b"),
    List.of("c", "d")
);

List<String> flat = nested.stream()
    .flatMap(List::stream)
    .toList();
```

### 19. Sort employees by salary and then age.

```java
List<Employee> sorted = employees.stream()
    .sorted(Comparator.comparingInt(Employee::getSal)
        .thenComparingInt(Employee::getAge))
    .toList();
```

### 20. Find top `N` highest values from a list.

```java
int n = 3;

List<Integer> topN = nums.stream()
    .sorted(Comparator.reverseOrder())
    .limit(n)
    .toList();
```

## Extra Interview Tips

- First explain the idea in plain words before writing the stream pipeline.
- Mention time and space complexity when the interviewer asks for optimization.
- If order matters, say so clearly before choosing `findFirst`, `LinkedHashMap`, or `sorted`.
- If the stream solution becomes hard to read, say that a loop may be more maintainable in production code.
- For duplicate-key map questions, always mention the merge function in `toMap()`.

## Quick Memory Lines

- `filter` keeps elements
- `map` transforms elements
- `flatMap` flattens nested data
- `collect` gathers results
- `reduce` combines to one value
- `groupingBy` groups similar elements
- `partitioningBy` splits into true and false groups
- `distinct` removes duplicates
- `mapToInt` is great for numeric pipelines
