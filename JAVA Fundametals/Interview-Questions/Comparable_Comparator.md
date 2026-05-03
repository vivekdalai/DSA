# Comparable & Comparator – Practice Questions (Interview Prep)

## Dataset & Class

```java
import java.util.*;
class Person implements Comparable<Person> {
    int age;
    String name;
    double salary;

    public Person (int age, String name, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
    // for Q3
//    @Override
//    public int compareTo(Person o) {
//        if (this.name == null && o.name == null) return 0;
//        if (this.name == null) return 1;
//        if (o.name == null) return -1;
//        return this.name.compareTo(o.name);
//    }

    // for Q4
    @Override
    public int compareTo(Person o) {
        int ageOrder = Integer.compare(this.age, o.age);
        if (ageOrder != 0) return ageOrder;
        if (this.name == null && o.name == null) return 0;
        if (this.name == null) return 1;
        if (o.name == null) return -1;
        return this.name.compareTo(o.name);
    }

    @Override
    public String toString() {
        return age + " " + name + " " + salary;
    }
}
public class Main {
    public static void main(String[] args) {
        List<Person> list = Arrays.asList(
                new Person(25, "Rahul", 50000),
                new Person(25, "Amit", 60000),
                new Person(30, "Zoya", 70000),
                new Person(25, "Amit", 40000),
                new Person(30, "amit", 80000),
                new Person(22, "Neha", 45000),
                new Person(22, null, 30000)
        );

        // code here

    }

    public static void display(List<Person> list) {
        for (Person p : list) {
            System.out.println(p.toString());
        }
        System.out.println();
        System.out.println();
    }
}
```

---

## 🟢 Level 1 – Basics

### Q1. Sort by age (ascending)
```java
Comparator<Person> ageComparator = (a, b) -> Integer.compare(a.age, b.age);
Collections.sort(list, ageComparator);
```

**Expected Output:**

```
22 Neha 45000
22 null 30000
25 Rahul 50000
25 Amit 60000
25 Amit 40000
30 Zoya 70000
30 amit 80000
```



---

### Q2. Sort by age (descending)
```java
Comparator<Person> ageComparator = (a, b) -> Integer.compare(a.age, b.age);
Collections.sort(list, ageComparator.reversed());
```
**Expected Output:**

```
30 Zoya 70000
30 amit 80000
25 Rahul 50000
25 Amit 60000
25 Amit 40000
22 Neha 45000
22 null 30000
```

---

### Q3. Natural ordering using Comparable (sort by name)

```java
    @Override
    public int compareTo(Person o) {
       if (this.name == null && o.name == null) return 0;
       if (this.name == null) return 1;
       if (o.name == null) return -1;
       return this.name.compareTo(o.name);
    }
```


```java
Collections.sort(list);
```

**Note:** Handle null values to avoid NullPointerException.

**Expected Output:**

```
25 Amit 60000
25 Amit 40000
22 Neha 45000
25 Rahul 50000
30 Zoya 70000
30 amit 80000
22 null 30000
```

---

## 🟡 Level 2 – Multi-field Sorting

### Q4. Sort by age → then name
```java
    @Override
    public int compareTo(Person o) {
        int ageOrder = Integer.compare(this.age, o.age);
        if (ageOrder != 0) return ageOrder;
        if (this.name == null && o.name == null) return 0;
        if (this.name == null) return 1;
        if (o.name == null) return -1;
        return this.name.compareTo(o.name);
    }
```

```java
Collections.sort(list);
```
**Expected Output:**

```
22 Neha 45000
22 null 30000
25 Amit 60000
25 Amit 40000
25 Rahul 50000
30 Zoya 70000
30 amit 80000
```

---

### Q5. Sort by age → name → salary
```java
Comparator<Person> ageNameSalaryComparator = Comparator.comparingInt(Person::getAge)
                                                    .thenComparing(Person::getName, Comparator.nullsLast(String::compareTo))
                                                    .thenComparingDouble(Person::getSalary);
Collections.sort(list, ageNameSalaryComparator);
```
**Expected Output:**

```
22 Neha 45000
22 null 30000
25 Amit 40000
25 Amit 60000
25 Rahul 50000
30 Zoya 70000
30 amit 80000
```

---

### Q6. Sort by age (ascending) and salary (descending)

```java
Comparator<Person> ageSalaryComparator = Comparator.comparingInt(Person::getAge)
                                                .thenComparing(Comparator.comparingDouble(Person::getSalary).reversed());
Collections.sort(list, ageSalaryComparator);

```

**Expected Output:**

```
22 Neha 45000
22 null 30000
25 Amit 60000
25 Rahul 50000
25 Amit 40000
30 amit 80000
30 Zoya 70000
```

---

## 🟠 Level 3 – Real Interview Patterns

### Q7. Age < 30 first, then salary ascending

```java
Comparator<Person> ageSalaryComparator2 = Comparator.comparingInt((Person p) -> p.getAge() < 30 ? 0 : 1)
                                                .thenComparingDouble(Person::getSalary);
Collections.sort(list, ageSalaryComparator2);

```

**Expected Output:**

```
22 null 30000
22 Neha 45000
25 Amit 40000
25 Rahul 50000
25 Amit 60000
30 Zoya 70000
30 amit 80000
```

---

### Q8. Sort by name (nulls last), then age

```java
Comparator<Person> nameAgeComparator = Comparator.comparing(Person::getName, Comparator.nullsLast(String::compareTo))
                                                .thenComparingInt(Person::getAge);
Collections.sort(list, nameAgeComparator);

```


**Expected Output:**

```
25 Amit 60000
25 Amit 40000
22 Neha 45000
25 Rahul 50000
30 Zoya 70000
30 amit 80000
22 null 30000
```

---

### Q9. Case-insensitive name sorting

```java
Comparator<Person> caseSensitiveNameComparator = Comparator.comparing(Person::getName, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER));

Collections.sort(list, caseSensitiveNameComparator);

```

**Expected Output:**

```
25 Amit 60000
25 Amit 40000
30 amit 80000
22 Neha 45000
25 Rahul 50000
30 Zoya 70000
22 null 30000
```

---

## 🔴 Level 4 – Tricky Concepts

### Q10. Comparator vs Comparable

* If both are present, Comparator takes precedence when passed explicitly.

---

### Q11. TreeSet behavior

**Scenario:** Comparator compares only by age

**Input:**

```
(25, Amit), (25, Rahul)
```

**Result:**

```
Only one element stored
```

**Reason:**

```
compare(a, b) == 0 → treated as duplicate
```

---

## 💣 Level 5 – Advanced Problem

### Q15. Largest Number Problem



**Input:**

```
[3, 30, 34, 5, 9]
```

**Expected Output:**

```
9534330
```


```java
public static void main(String[] args) {

        int[] nums = new int[] {3, 30, 34, 5, 9};

        String[] arr = Arrays.stream(nums)
                .mapToObj(String::valueOf)
                .toArray(String[]::new);


        Arrays.sort(arr, (a, b) -> (b + a).compareTo(a + b));

        StringBuilder sb = new StringBuilder();

        for (String s : arr) {
            sb.append(s);
        }

        System.out.println(sb);


    }
```

---

## 🧠 Notes

* Comparable defines natural/default ordering.
* Comparator is used for custom/multiple sorting logic.
* Always handle nulls carefully.
* Prefer Java 8 comparator chaining for clean code.

---

## ✅ Practice Tip

For each question:

1. Write comparator/compareTo
2. Run on dataset
3. Match expected output
