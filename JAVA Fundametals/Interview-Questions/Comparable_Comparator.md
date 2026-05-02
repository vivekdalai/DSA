# Comparable & Comparator – Practice Questions (Interview Prep)

## Dataset

```java
List<Person> list = Arrays.asList(
    new Person(25, "Rahul", 50000),
    new Person(25, "Amit", 60000),
    new Person(30, "Zoya", 70000),
    new Person(25, "Amit", 40000),
    new Person(30, "amit", 80000),
    new Person(22, "Neha", 45000),
    new Person(22, null, 30000)
);
```

---

## 🟢 Level 1 – Basics

### Q1. Sort by age (ascending)

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
