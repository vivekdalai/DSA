# Builder Pattern

The Builder pattern separates the construction of a complex object from its representation, so the same construction process can produce different configurations. It's most useful when a class has many fields — especially many optional ones — where telescoping constructors (a constructor overload for every combination of fields) become unreadable and error-prone.

## The problem it solves

```java
// Telescoping constructor anti-pattern
public class Pizza {
    public Pizza(String size) { ... }
    public Pizza(String size, boolean extraCheese) { ... }
    public Pizza(String size, boolean extraCheese, boolean stuffedCrust) { ... }
    public Pizza(String size, boolean extraCheese, boolean stuffedCrust, boolean thinCrust) { ... }
    // gets worse with every optional field, and call sites are unreadable:
    // new Pizza("Large", true, false, true);  <- what do these booleans mean?
}
```

## The Builder solution

Implemented as a `static` nested class (see [[10_Equals_HashCode]] pattern of nesting for related helper types — the Builder is a similar case of a class that belongs conceptually to its outer class but needs no outer-instance state).

```java
public class Pizza {
    private final String size;
    private final boolean extraCheese;
    private final boolean stuffedCrust;
    private final boolean thinCrust;

    private Pizza(Builder builder) {
        this.size = builder.size;
        this.extraCheese = builder.extraCheese;
        this.stuffedCrust = builder.stuffedCrust;
        this.thinCrust = builder.thinCrust;
    }

    @Override
    public String toString() {
        return "Pizza{size=" + size + ", extraCheese=" + extraCheese
                + ", stuffedCrust=" + stuffedCrust + ", thinCrust=" + thinCrust + "}";
    }

    public static class Builder {
        // required
        private final String size;

        // optional — default values
        private boolean extraCheese = false;
        private boolean stuffedCrust = false;
        private boolean thinCrust = false;

        public Builder(String size) {
            this.size = size; // required field enforced via constructor
        }

        public Builder extraCheese(boolean value) {
            this.extraCheese = value;
            return this; // returning `this` enables method chaining
        }

        public Builder stuffedCrust(boolean value) {
            this.stuffedCrust = value;
            return this;
        }

        public Builder thinCrust(boolean value) {
            this.thinCrust = value;
            return this;
        }

        public Pizza build() {
            return new Pizza(this);
        }
    }
}
```

## Usage

```java
Pizza pizza = new Pizza.Builder("Large")
        .extraCheese(true)
        .stuffedCrust(true)
        .build();

System.out.println(pizza);
// Pizza{size=Large, extraCheese=true, stuffedCrust=true, thinCrust=false}
```

Every optional attribute is now named at the call site, order doesn't matter, and unset fields fall back to sensible defaults — no ambiguous boolean soup, no combinatorial explosion of constructors.

## Key mechanics

1. **Outer class constructor is private** — the only way to build a `Pizza` is through `Builder`, which guarantees the object is always constructed in a valid, intentional way.
2. **Outer class fields are `final`** — once built, the object is immutable. This pairs well with the Builder pattern; you get the flexibility of optional/named parameters during construction without sacrificing immutability afterward.
3. **Each setter method returns `this`** — this is what enables the fluent, chained call style (`.extraCheese(true).stuffedCrust(true)...`).
4. **`build()` does the final assembly** — calls the outer class's private constructor, passing itself so the outer class can pull values straight from the builder's fields.

## When to reach for it

- A class has 4+ constructor parameters, especially if several are optional or of the same type (e.g. multiple booleans or Strings — easy to accidentally swap at a call site).
- You want immutable objects but also want readable, flexible construction.
- Common in real frameworks: `StringBuilder`, `Stream.Builder`, Lombok's `@Builder` annotation, and most HTTP client / request builders (e.g. `HttpRequest.newBuilder()...`).

## Trade-off

More boilerplate than a plain constructor — not worth it for simple classes with 1-3 fields. Reach for it once constructors start requiring documentation to use correctly.
