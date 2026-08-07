# Predicate, Consumer, Supplier

These are built-in functional interfaces from `java.util.function`, used heavily with lambdas and streams. Each represents a different "shape" of function based on what it takes in and what it gives back.

| Interface | Takes | Returns | Method | Think of it as |
|---|---|---|---|---|
| `Predicate<T>` | `T` | `boolean` | `test(T t)` | A yes/no question about a value |
| `Consumer<T>` | `T` | nothing (`void`) | `accept(T t)` | "Do something with this value" |
| `Supplier<T>` | nothing | `T` | `get()` | "Give me a value" (the "producer" you're thinking of) |

---

## 1. `Predicate<T>` — tests a condition, returns `boolean`

Used anywhere you need a reusable "is this true?" check — most commonly `filter()` in streams, or validation logic.

```java
Predicate<Integer> isEven = n -> n % 2 == 0;

System.out.println(isEven.test(4)); // true
System.out.println(isEven.test(7)); // false
```

### Usage with streams

```java
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

List<Integer> evens = numbers.stream()
        .filter(isEven)
        .collect(Collectors.toList());

System.out.println(evens); // [2, 4, 6]
```

### Combining predicates: `and()`, `or()`, `negate()`

```java
Predicate<Integer> isPositive = n -> n > 0;
Predicate<Integer> isEvenAndPositive = isEven.and(isPositive);
Predicate<Integer> isOdd = isEven.negate();

System.out.println(isEvenAndPositive.test(-4)); // false (even, but not positive)
System.out.println(isOdd.test(3));               // true
```

### Real-world usage: validation

```java
Predicate<String> isValidEmail = email -> email.contains("@") && email.contains(".");

public void register(String email) {
    if (!isValidEmail.test(email)) {
        throw new IllegalArgumentException("Invalid email");
    }
    // proceed with registration
}
```

---

## 2. `Consumer<T>` — takes a value, does something, returns nothing

Used when you want to perform a side effect (print, save, mutate, log) with a value, without producing a new result.

```java
Consumer<String> printer = s -> System.out.println("Value: " + s);
printer.accept("hello"); // Value: hello
```

### Usage with `forEach`

```java
List<String> names = List.of("Alice", "Bob", "Charlie");
names.forEach(name -> System.out.println("Hi, " + name));

// or pass a method reference directly
names.forEach(System.out::println);
```

### Chaining with `andThen()`

```java
Consumer<String> logToConsole = s -> System.out.println("LOG: " + s);
Consumer<String> saveToFile = s -> System.out.println("Saving '" + s + "' to file"); // stand-in for real file I/O

Consumer<String> combined = logToConsole.andThen(saveToFile);
combined.accept("event-1");
// LOG: event-1
// Saving 'event-1' to file
```

### Real-world usage: processing each item in a collection

```java
Map<String, Double> prices = Map.of("Apple", 1.5, "Banana", 0.5);

Consumer<Map.Entry<String, Double>> printLine =
        entry -> System.out.printf("%s: $%.2f%n", entry.getKey(), entry.getValue());

prices.entrySet().forEach(printLine);
```

---

## 3. `Supplier<T>` — takes nothing, produces a value ("producer")

This is what people usually mean by "producer" — a factory for a value, evaluated lazily (only when `get()` is actually called).

```java
Supplier<Double> randomValue = () -> Math.random();
System.out.println(randomValue.get()); // a new random double each call
```

### Usage: lazy/deferred computation

Useful when creating a value is expensive and you only want to pay that cost if it's actually needed.

```java
public void log(Level level, Supplier<String> messageSupplier) {
    if (isEnabled(level)) {
        System.out.println(messageSupplier.get()); // only builds the message if actually logging
    }
}

log(Level.DEBUG, () -> "Expensive debug info: " + computeExpensiveDebugInfo());
// if DEBUG isn't enabled, computeExpensiveDebugInfo() never runs
```

### Usage: default values / factories

```java
Supplier<List<String>> listFactory = ArrayList::new;
List<String> list = listFactory.get(); // fresh empty ArrayList

Map<String, List<String>> map = new HashMap<>();
map.computeIfAbsent("fruits", k -> new ArrayList<>()).add("apple");
```

### Usage: `Optional.orElseGet()`

```java
Optional<String> maybeName = Optional.empty();

String name = maybeName.orElseGet(() -> fetchDefaultNameFromDatabase());
// fetchDefaultNameFromDatabase() only runs if maybeName is actually empty —
// unlike orElse(...), whose argument is always evaluated eagerly
```

---

## Putting all three together

```java
public class Pipeline<T> {
    public static <T> void process(Supplier<T> source, Predicate<T> filter, Consumer<T> action) {
        T value = source.get();      // produce
        if (filter.test(value)) {    // decide
            action.accept(value);    // consume
        }
    }
}

Pipeline.process(
    () -> 42,                                   // Supplier: produce a value
    n -> n > 10,                                 // Predicate: only proceed if > 10
    n -> System.out.println("Processing: " + n)  // Consumer: do something with it
);
// Processing: 42
```

## Quick rule of thumb

- Need to ask "true or false" about something → `Predicate<T>`
- Need to do something with a value, no result expected → `Consumer<T>`
- Need to produce/generate a value from nothing (a factory, a lazy computation) → `Supplier<T>`
- (Bonus, not asked but related) Need to take a value and transform it into another value → `Function<T, R>`
