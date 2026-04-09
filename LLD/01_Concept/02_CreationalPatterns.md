# Creational Design Patterns

Creational patterns deal with **how objects are created**.

They help when:
- object creation logic is becoming complicated
- object creation should be controlled
- direct use of `new` everywhere makes code rigid
- you want flexibility in what gets created

Common creational patterns include:
- Constructor
- Singleton
- Builder
- Factory Method / Factory Pattern

---

## 1. Constructor Pattern

This is the most basic way of creating an object in Java.

### Example

```java
class User {
    private String name;
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

public class Main {
    public static void main(String[] args) {
        User user = new User("Vivek", 25);
    }
}
```

### Thinking process
Use a constructor when:
- the object is simple
- the number of fields is small
- most fields are required
- creation logic is straightforward

### Problem with too many constructor parameters
When the object becomes large, constructors become hard to read:

```java
User user = new User("Vivek", 25, "Delhi", "India", true, false, "Developer");
```

Now it becomes difficult to understand:
- what each value means
- which fields are optional
- whether the order is correct

This is where **Builder Pattern** becomes a better choice.

---

## 2. Singleton Pattern

The Singleton pattern ensures that **only one instance of a class exists** in the application and provides a global access point to it.

### Common use cases
- configuration manager
- logger
- cache manager
- shared service object
- application-wide settings

### Core idea
- constructor is `private`
- object cannot be created directly from outside
- a static method returns the single object

### Basic example

```java
class Singleton {
    private static Singleton instance;

    private Singleton() {
        // private constructor
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        Singleton s1 = Singleton.getInstance();
        Singleton s2 = Singleton.getInstance();

        System.out.println(s1 == s2); // true
    }
}
```

### Thinking process
Ask yourself:

- Should this class have exactly one shared object?
- Should all parts of the application use the same instance?
- Would multiple instances create inconsistency?

If the answer is yes, Singleton may help.

For example, imagine every class creates its own configuration object. That can lead to duplication and inconsistent state.  
Instead, a single shared configuration object makes more sense.

The core idea is:

- check whether the **stored object reference** is `null`
- if yes, create the object
- otherwise return the already-created object

### Thread-safety issue
The basic singleton shown above is **not thread-safe**. In multithreaded code, two threads may create two instances at the same time.

### Thread-safe version

```java
class Singleton {
    private static Singleton instance;

    private Singleton() {
    }

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

### Better eager initialization version

```java
class Singleton {
    private static final Singleton instance = new Singleton();

    private Singleton() {
    }

    public static Singleton getInstance() {
        return instance;
    }
}
```

### Caution
Singleton is useful, but overusing it can make code behave like global state, which makes testing and maintenance harder.

---

## 3. Builder Pattern

The Builder pattern is used to create **complex objects step by step**.

It is especially helpful when:
- a class has many fields
- some fields are optional
- constructors are becoming too large
- object creation should be readable

### How builder pattern is created
Usually, the main class has:
- a constructor that takes a `Builder` object as input
- a nested or separate `Builder` class that stores the values step by step
- a `build()` method that finally calls the main class constructor and returns the object

So the flow is:

1. set values inside the `Builder`
2. call `build()`
3. `build()` internally calls the constructor that accepts the `Builder`
4. final object gets created

### Problem
Suppose you want to create a `House` object with many properties:
- doors
- windows
- color
- hasParking
- hasGarden

A constructor with many parameters becomes messy.

### Without builder

```java
class House {
    private int doors;
    private int windows;
    private String color;
    private boolean hasParking;
    private boolean hasGarden;

    public House(int doors, int windows, String color, boolean hasParking, boolean hasGarden) {
        this.doors = doors;
        this.windows = windows;
        this.color = color;
        this.hasParking = hasParking;
        this.hasGarden = hasGarden;
    }
}
```

Usage:

```java
House house = new House(2, 4, "White", true, false);
```

This works, but is not very expressive.

### With builder

```java
class House {
    private int doors;
    private int windows;
    private String color;
    private boolean hasParking;
    private boolean hasGarden;

    private House(Builder builder) {
        this.doors = builder.doors;
        this.windows = builder.windows;
        this.color = builder.color;
        this.hasParking = builder.hasParking;
        this.hasGarden = builder.hasGarden;
    }

    public static class Builder {
        private int doors;
        private int windows;
        private String color;
        private boolean hasParking;
        private boolean hasGarden;

        public Builder setDoors(int doors) {
            this.doors = doors;
            return this;
        }

        public Builder setWindows(int windows) {
            this.windows = windows;
            return this;
        }

        public Builder setColor(String color) {
            this.color = color;
            return this;
        }

        public Builder setHasParking(boolean hasParking) {
            this.hasParking = hasParking;
            return this;
        }

        public Builder setHasGarden(boolean hasGarden) {
            this.hasGarden = hasGarden;
            return this;
        }

        public House build() {
            return new House(this);
        }
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        House house = new House.Builder()
                .setDoors(2)
                .setWindows(4)
                .setColor("White")
                .setHasParking(true)
                .setHasGarden(false)
                .build();
    }
}
```

### Thinking process
Use Builder when:
- object creation has many optional choices
- you want code to read almost like a sentence
- constructor parameter order is becoming error-prone

Builder improves readability because each value is attached to a meaningful method name.

Instead of remembering parameter order, the code itself explains the object creation process.

---

## 4. Factory Pattern

The Factory pattern is used when the client should **not directly decide which concrete class object to create**.

Instead of writing `new` everywhere, object creation is centralized in one place.

### Why do we need it?
Suppose we have a parent type `Shape`, and child classes:
- `Circle`
- `Rectangle`
- `Square`

If client code directly creates objects using `new`, then every place in the code needs to know the exact subclass.

That increases coupling.

Factory helps by moving object creation logic into a separate class.

### Example without factory

```java
interface Shape {
    void draw();
}

class Circle implements Shape {
    public void draw() {
        System.out.println("Drawing Circle");
    }
}

class Rectangle implements Shape {
    public void draw() {
        System.out.println("Drawing Rectangle");
    }
}

public class Main {
    public static void main(String[] args) {
        Shape shape = new Circle(); // client directly chooses class
        shape.draw();
    }
}
```

This is okay for small code, but not ideal when object choice depends on input or business rules.

### Example with factory

```java
interface Shape {
    void draw();
}

class Circle implements Shape {
    public void draw() {
        System.out.println("Drawing Circle");
    }
}

class Rectangle implements Shape {
    public void draw() {
        System.out.println("Drawing Rectangle");
    }
}

class ShapeFactory {
    public Shape getShape(String type) {
        if (type == null) {
            return null;
        }

        if (type.equalsIgnoreCase("circle")) {
            return new Circle();
        } else if (type.equalsIgnoreCase("rectangle")) {
            return new Rectangle();
        }

        return null;
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        ShapeFactory factory = new ShapeFactory();

        Shape shape = factory.getShape("circle");
        shape.draw();
    }
}
```

### Thinking process
Use Factory when:
- object type depends on input
- client should work with an interface or parent type
- you want to isolate object creation logic
- new subclasses may be added later

The main idea is:

> “Client should ask for an object, not worry too much about how exactly it is created.”

Factory is useful when:
- multiple subclasses exist or may grow over time
- client code should not be tightly coupled to specific subclass creation
- object creation logic should be centralized

### Real-life analogy
Think of a pizza store:
- customer asks for a pizza type
- the store decides which concrete pizza object to create
- customer does not manually create `CheesePizza`, `VegPizza`, etc.

That is the factory idea.
