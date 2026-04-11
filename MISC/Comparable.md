# Nuances of Comparable in Java

## Overview
The `Comparable` interface in Java defines the natural ordering of objects within a class. It allows objects of a class to be compared to each other for sorting purposes. Unlike `Comparator`, which is external, `Comparable` is implemented directly in the class.

## Key Characteristics

### Interface Definition
- `Comparable<T>` has a single method: `int compareTo(T o)`
- Returns negative, zero, or positive integer based on ordering

### Natural Ordering
- Defines the default way objects of the class should be ordered
- Used by sorting methods when no explicit comparator is provided

## Implementation

### Basic Implementation
```java
public class Person implements Comparable<Person> {
    private String name;
    private int age;

    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }
}
```

### Multi-Criteria Comparison
```java
public class Employee implements Comparable<Employee> {
    private String department;
    private int salary;
    private String name;

    @Override
    public int compareTo(Employee other) {
        int deptCompare = this.department.compareTo(other.department);
        if (deptCompare != 0) {
            return deptCompare;
        }
        int salaryCompare = Integer.compare(this.salary, other.salary);
        if (salaryCompare != 0) {
            return salaryCompare;
        }
        return this.name.compareTo(other.name);
    }
}
```

### Multi-Criteria with Descending Reward and Ascending Deadline
For scenarios like job scheduling where higher reward is preferred but earlier deadlines take priority when rewards are equal:

```java
class Pair implements Comparable<Pair> {
    int deadline;
    int reward;
    
    public Pair(int deadline, int reward) {
        this.deadline = deadline;
        this.reward = reward;
    }
    
    @Override
    public int compareTo(Pair other) {
        // First compare reward in descending order (higher reward first)
        int rewardCompare = Integer.compare(other.reward, this.reward);
        if (rewardCompare != 0) {
            return rewardCompare;
        }
        // If rewards are equal, compare deadline in ascending order (earlier deadline first)
        return Integer.compare(this.deadline, other.deadline);
    }
}
```

This implementation ensures that:
- Pairs with higher rewards come first
- When rewards are equal, pairs with earlier deadlines come first
- The comparison is transitive and consistent

## Return Value Semantics
- Returns a **negative integer** if `this` < `o`
- Returns a **zero** if `this` == `o`
- Returns a **positive integer** if `this` > `o`

## Best Practices

1. **Consistency with equals()**: If `compareTo()` returns 0, `equals()` should return true
2. **Transitivity**: If `a.compareTo(b) > 0` and `b.compareTo(c) > 0`, then `a.compareTo(c) > 0`
3. **Antisymmetry**: `a.compareTo(b)` should be opposite of `b.compareTo(a)`
4. **Handle nulls**: Throw `NullPointerException` if comparing with null

## Common Pitfalls

1. **Integer Overflow**: Avoid `this.value - other.value`; use `Integer.compare()`
2. **Floating Point Issues**: Handle `NaN` values carefully
3. **Null Values**: Decide how to handle null fields

## Comparable vs Comparator

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

## Usage with Collections
- `Collections.sort(list)` - uses natural ordering
- `Arrays.sort(array)` - uses natural ordering
- `TreeSet` and `TreeMap` - use natural ordering by default

## Thread Safety
- `compareTo()` should be thread-safe if the object is used in concurrent contexts
- Avoid mutable state that could affect comparison