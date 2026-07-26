# Stream_07_Aggregation.java

## What This File Teaches

This file covers aggregate operations over a stream.

Aggregation means calculating summary information, such as:

- count
- average
- minimum
- maximum
- joined string
- all employees matching a max value

## Counting Employees

```java
long count = empList.stream()
        .filter(e -> e.getAge() > 30)
        .count();
```

This counts employees older than 30.

The stream first filters employees, then `count()` returns how many remain.

## Average Salary

```java
double avgSal = empList.stream()
        .collect(Collectors.averagingInt(Employee::getSal));
```

`Collectors.averagingInt` calculates the average of integer values extracted from each employee.

Here, `Employee::getSal` tells the collector which integer field to average.

## Minimum Salary Employee

```java
Optional<Employee> minSalEmp = empList.stream()
        .collect(Collectors.minBy(Comparator.comparing(Employee::getSal)));
```

This finds one employee with the minimum salary.

`Comparator.comparing(Employee::getSal)` compares employees by salary.

The result is `Optional<Employee>` because the list might be empty.

## Maximum Age Employee

```java
Optional<Employee> maxAgeEmp = empList.stream()
        .max(Comparator.comparing(Employee::getAge));
```

This directly uses the stream's `max` method to find one employee with the highest age.

## Finding All Employees With Max Age

First, the code finds the highest age:

```java
int maxAge = empList.stream()
        .mapToInt(Employee::getAge)
        .max()
        .orElse(0);
```

Then it filters all employees whose age equals that max:

```java
List<Employee> maxAgeEmpList = empList.stream()
        .filter(e -> e.getAge() == maxAge)
        .collect(Collectors.toList());
```

This is important because `max()` returns only one element. If multiple employees share the same max age, filtering after finding the max gets all of them.

## Small Code Warning

The file has this line:

```java
if(maxAge != 0);
```

The semicolon ends the `if` immediately. That means the block below always runs.

It should usually be:

```java
if (maxAge != 0) {
    ...
}
```

In this example, it does not break the visible result because `maxAge` is not `0`, but it is an important Java syntax issue to notice.

## Joining Strings

```java
String joinedStr = strArr.stream()
        .collect(Collectors.joining(" "));
```

This joins all strings with a space separator.

The file also shows:

```java
String.join(" ", strArr);
```

For simple string joining, `String.join` is shorter. `Collectors.joining` is useful when you are already working in a stream pipeline.

## Important Takeaway

Aggregation answers summary questions about data. When the summary might not exist, Java often returns `Optional`.
