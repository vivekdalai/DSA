# Nuances of Comparator in Java

## Overview
The `Comparator` interface in Java is a powerful tool for custom sorting and ordering of objects. Unlike `Comparable`, which defines the natural ordering of a class, `Comparator` allows for multiple sorting strategies and is particularly useful for sorting collections of objects that don't implement `Comparable` or when you need alternative ordering.

## Key Characteristics

### Functional Interface
- `Comparator<T>` is a functional interface with a single abstract method: `int compare(T o1, T o2)`
- This makes it ideal for lambda expressions and method references

### Return Value Semantics
- Returns a **negative integer** if `o1` should come before `o2`
- Returns a **zero** if `o1` and `o2` are considered equal for sorting purposes
- Returns a **positive integer** if `o1` should come after `o2`

## Common Usage Patterns

### Basic Comparator Implementation
```java
Comparator<String> lengthComparator = new Comparator<String>() {
    @Override
    public int compare(String s1, String s2) {
        return Integer.compare(s1.length(), s2.length());
    }
};
```

### Lambda Expression (Modern Approach)
```java
Comparator<String> lengthComparator = (s1, s2) -> Integer.compare(s1.length(), s2.length());
```

### Method Reference
```java
List<String> words = Arrays.asList("apple", "Banana", "cherry");
words.sort(String::compareToIgnoreCase);
```

## Advanced Nuances

### Chaining Comparators for Multi-Criteria Sorting
When you need to sort by multiple criteria, use `Comparator.thenComparing()`:

```java
Comparator<Employee> employeeComparator = Comparator
    .comparing(Employee::getDepartment)
    .thenComparing(Employee::getSalary)
    .thenComparing(Employee::getName);
```

This sorts by department first, then salary, then name - all in ascending order.

### Reverse Ordering
```java
Comparator<String> reverseLength = Comparator.comparingInt(String::length).reversed();
```

### Null Handling
- `Comparator.nullsFirst()` and `Comparator.nullsLast()` handle null values
- `Comparator.naturalOrder()` and `Comparator.reverseOrder()` work with `Comparable` objects

```java
Comparator<String> nullSafeComparator = Comparator.nullsLast(String::compareTo);
```

### Primitive Type Comparators
- `Comparator.comparingInt()`, `comparingLong()`, `comparingDouble()` avoid boxing/unboxing
- More efficient for primitive comparisons

### Custom Comparator with Complex Logic
```java
Comparator<Pair> rewardThenDeadline = (p1, p2) -> {
    int rewardCompare = Integer.compare(p2.reward, p1.reward); // descending reward
    if (rewardCompare != 0) {
        return rewardCompare;
    }
    return Integer.compare(p1.deadline, p2.deadline); // ascending deadline
};
```

## Comparator vs Comparable

| Aspect | Comparable | Comparator |
|--------|------------|------------|
| Method | `compareTo(T o)` | `compare(T o1, T o2)` |
| Implementation | In the class itself | External to the class |
| Natural Ordering | Yes | No (custom ordering) |
| Multiple Orderings | No | Yes |
| Modification | Requires class change | No class modification needed |

## When to Use Which

### Use Comparable When:
- You want to define the **natural/default ordering** of objects of a class
- The class represents entities with an obvious primary sort order
- You control the class implementation
- You want automatic sorting in TreeSet/TreeMap without specifying a comparator
- Examples: String, Integer, Date, Employee (by ID or name)

### Use Comparator When:
- You need **multiple different sorting orders** for the same class
- You don't control the class (third-party or library classes)
- You want to sort by different criteria without modifying the class
- You need custom sorting logic for specific use cases
- Examples: Sorting employees by salary, then by name; sorting strings by length

### Best Practices for Comparator
1. **Consistency**: Ensure `compare(a,b) == -compare(b,a)` (antisymmetry)
2. **Transitivity**: If `compare(a,b) > 0` and `compare(b,c) > 0`, then `compare(a,c) > 0`
3. **Null Safety**: Handle null values appropriately
4. **Performance**: Use primitive comparators when possible
5. **Readability**: Use method references and chaining for clarity

## Common Pitfalls
1. **Integer Overflow**: Avoid `a - b` for large integers; use `Integer.compare(a, b)`
2. **Floating Point**: Be cautious with `Double.NaN` and `Float.NaN` comparisons
3. **Null Pointer Exceptions**: Always check for null before accessing object properties
4. **Inconsistent Ordering**: Ensure the comparator provides a total ordering

## Thread Safety
- Comparator implementations should be stateless and thread-safe
- Avoid mutable state in comparators
- Static comparators are generally safe

## Performance Considerations
- Comparators with heavy computation should be cached if reused
- For large datasets, consider the O(n log n) complexity of sorting
- Primitive comparators are faster than boxed equivalents

## Integration with Java Collections
- `Collections.sort(list, comparator)`
- `Arrays.sort(array, comparator)`
- `Stream.sorted(comparator)`
- `PriorityQueue` constructor with comparator
- `TreeSet` and `TreeMap` constructors with comparator