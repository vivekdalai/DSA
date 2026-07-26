# Supplier_01_learn.java

## What This File Teaches

This file introduces `Supplier<T>` and also revisits `Predicate.isEqual`. It also shows `DoubleUnaryOperator`, which is a primitive-specialized functional interface.

## `Predicate.isEqual` With Custom Objects

```java
Predicate<Person> samePersonPredicate = Predicate.isEqual(p1);
System.out.println(samePersonPredicate.test(p2));
```

Here `p1` and `p2` are different objects but contain the same values:

```java
new Person(10, "Arjun")
new Person(10, "Arjun")
```

This file overrides `equals`, so `Predicate.isEqual(p1)` compares by value instead of by memory reference.

```java
@Override
public boolean equals(Object obj) {
    if (obj instanceof Person other) {
        return this.age == other.age && this.name.equals(other.name);
    }

    return false;
}
```

Because of this override, `samePersonPredicate.test(p2)` returns `true`.

## Key Concept: Supplier

`Supplier<T>` represents something that supplies or creates a value.

It has one abstract method:

```java
T get()
```

In this file:

```java
Supplier<Person> personSupplier = () -> new Person(20, "Krishna");
Person p3 = personSupplier.get();
```

The lambda does not take any input. It simply returns a new `Person` whenever `get()` is called.

Think of a supplier as:

```text
No input -> returns something
```

## `DoubleUnaryOperator`

```java
DoubleUnaryOperator operator = num -> num * num;
System.out.println(operator.applyAsDouble(25));
```

`DoubleUnaryOperator` takes one `double` and returns one `double`.

```text
double input -> double output
```

The lambda squares the number, so `25` becomes `625.0`.

## Stream API Connection

Suppliers are useful when values should be generated lazily. For example:

```java
Stream.generate(() -> Math.random())
      .limit(5)
      .forEach(System.out::println);
```

Here `Stream.generate` accepts a supplier.

## Important Takeaway

Use `Supplier<T>` when no input is required but a value needs to be produced.
