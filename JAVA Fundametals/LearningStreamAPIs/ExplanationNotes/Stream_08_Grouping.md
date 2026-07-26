# Stream_08_Grouping.java

## What This File Teaches

This file focuses on `Collectors.groupingBy`, one of the most powerful collectors in the Stream API.

It covers:

- grouping employees by department
- nested grouping
- counting per group
- summing per group
- finding max salary per group
- choosing the map implementation

## Grouping By Department

```java
Map<String, List<Employee>> empMap = empList.stream()
        .collect(Collectors.groupingBy(Employee::getDept));
```

This creates a map where:

- key = department
- value = list of employees in that department

Example shape:

```text
ENGG -> [A, C, D]
MRKT -> [E, F]
FINA -> [B]
SUPP -> [G]
```

## Nested Grouping

```java
Map<String, Map<Integer, List<Employee>>> employeeMap = empList.stream()
        .collect(Collectors.groupingBy(
                Employee::getDept,
                Collectors.groupingBy(Employee::getAge)
        ));
```

This first groups by department, then inside each department groups by age.

Result shape:

```text
dept -> age -> employees
```

The type tells the structure:

```java
Map<String, Map<Integer, List<Employee>>>
```

## Counting Employees Per Department

```java
Map<String, Long> empCountMap = empList.stream()
        .collect(Collectors.groupingBy(Employee::getDept, Collectors.counting()));
```

Instead of storing a list of employees for each department, the downstream collector counts employees in each department.

Result shape:

```text
dept -> count
```

## Total Salary Per Department

```java
Map<String, Integer> totalSalMap = empList.stream()
        .collect(Collectors.groupingBy(
                Employee::getDept,
                Collectors.summingInt(Employee::getSal)
        ));
```

This groups by department and sums salaries inside each group.

Result shape:

```text
dept -> total salary
```

## Max Salary Employee Per Department

```java
HashMap<String, Optional<Employee>> maxSalInDept = empList.stream()
        .collect(Collectors.groupingBy(
                Employee::getDept,
                HashMap::new,
                Collectors.maxBy(Comparator.comparingInt(Employee::getSal))
        ));
```

This version of `groupingBy` has three parts:

1. classifier: `Employee::getDept`
2. map supplier: `HashMap::new`
3. downstream collector: `Collectors.maxBy(...)`

The result value is `Optional<Employee>` because max may not exist for an empty group.

In normal grouping, empty groups are not created, so values will usually be present. But the collector type still uses `Optional`.

## Important Takeaway

`groupingBy` lets you build reports from lists. The downstream collector decides what each group contains: a list, count, sum, max, min, or another grouped map.
