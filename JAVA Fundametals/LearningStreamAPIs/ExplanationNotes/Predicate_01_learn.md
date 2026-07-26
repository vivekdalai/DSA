# Predicate_01_learn.java

## What This File Teaches

This file introduces `Predicate<T>`, one of Java's most important functional interfaces. A predicate represents a condition that takes one value and returns `true` or `false`.

```java
Predicate<Person> greaterThanEighteen = person -> person.age >= 18;
```

This is the same idea as asking: "Does this `Person` satisfy this rule?"

## Key Concept: Predicate

`Predicate<T>` has one main abstract method:

```java
boolean test(T value)
```

For `Predicate<Person>`, the `test` method accepts a `Person` and returns a boolean.

In the file, this is first written using an anonymous class:

```java
Predicate<Person> greaterThanEighteen = new Predicate<Person>() {
    @Override
    public boolean test(Person person) {
        return person.age >= 18;
    }
};
```

Then the same style is simplified using a lambda:

```java
Predicate<Person> greaterThanSixty = p -> p.age >= 60;
```

The lambda version is preferred because `Predicate` has only one abstract method, so Java knows the lambda body is implementing `test`.

## Combining Predicates

The file demonstrates default methods available on predicates.

### `and`

```java
Predicate<Person> newPredicateAND = greaterThanEighteen.and(greaterThanSixty);
```

This means:

```text
person is at least 18 AND person is at least 60
```

Both conditions must be true.

### `or`

```java
Predicate<Person> newPredicateOR = greaterThanSixty.or(greaterThanEighteen.negate());
```

This means:

```text
person is at least 60 OR person is not at least 18
```

So this predicate is true for senior people and minors.

### `negate`

```java
greaterThanEighteen.negate()
```

This flips the result:

```text
age >= 18 becomes age < 18
```

## `Predicate.isEqual`

```java
Predicate<String> stringPredicate1 = Predicate.isEqual("World");
```

This creates a predicate that checks whether the tested value equals `"World"`.

For objects:

```java
Predicate<Person> isEqualPredicate = Predicate.isEqual(p1);
```

This uses the object's `equals` method. Since the `Person` class in this file does not override `equals`, Java uses reference equality. That means two different `Person` objects with the same values would still not be equal unless they are the exact same object.

## Stream API Connection

Predicates are used heavily in streams, especially with `filter`.

```java
people.stream()
      .filter(greaterThanEighteen)
      .toList();
```

Whenever you write `filter(p -> p.age >= 18)`, you are passing a predicate.

## Important Takeaway

Use `Predicate<T>` when you want to represent a reusable boolean rule.
