# What is Singleton Pattern?
- Singleton Pattern is a creational design pattern that guarantees a class has only one instance and provides a global point of access to it.

## Two requirements define the pattern:

1. Single instance: No matter how many times any part of the code requests it, the same object is returned.
2. Global access: Any component can reach the instance without needing it passed through constructors or method parameters.


## Singleton is useful in scenarios like:

* Managing Shared Resources (database connections, thread pools, caches, configuration settings)
* Coordinating System-Wide Actions (logging, print spoolers, file managers)
* Managing State (user session, application state)


- The constructor is private or otherwise restricted, so other code cannot create new instances directly.

# Implementation

## 1. Lazy Initialization (Not Thread-Safe)

```java
class LazySingleton {
    // Holds the single shared instance (initially not created)
    private static LazySingleton instance;

    // Private constructor prevents creating objects from outside the class
    private LazySingleton() {}

    // Global access point to get the Singleton instance
    public static LazySingleton getInstance() {
		
        // Create the instance only when first requested (lazy initialization)
        if (instance == null) {
            instance = new LazySingleton();
        }

        // Return the shared instance
        return instance;
    }
}
```


- Not Thread-Safe
  - This implementation is not thread-safe. If multiple threads call getInstance() simultaneously when instance is null, it's possible to create multiple instances.


## 2. Thread-Safe Singleton

```java
class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;

    private ThreadSafeSingleton() {}

    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }

        return instance;
    }
}
```

- This approach is correct but has a performance cost: 
  - every call to getInstance() acquires a lock, even after the instance has been created. Once the instance exists, there is no reason to synchronize.
  - The next approach fixes this.

## 3. Double-Checked Locking

```java
class DoubleCheckedSingleton {
    // Holds the single shared instance (requires safe publication)
	// volatile prevents reordering of instructions in: instance = new Singleton()
	// since it involves three steps: allocate memory, call constructor, assign reference
    private static volatile DoubleCheckedSingleton instance;

    // Private constructor prevents external instantiation
    private DoubleCheckedSingleton() {}

    // Global access point to get the Singleton instance
    public static DoubleCheckedSingleton getInstance() {
        
		// Fast path: first check without locking
        if (instance == null) {
            // Lock only when the instance might need to be created
            synchronized (DoubleCheckedSingleton.class) {
                // Second check inside the lock (prevents double creation)
                if (instance == null) {
                    instance = new DoubleCheckedSingleton();
                }
            }
        }

        // Return the shared instance (existing or newly created)
        return instance;
    }
}
```

- Good Performance
  - Although this approach is more complex to implement, it can drastically reduce performance overhead, especially when the singleton is accessed frequently.


## 4. Eager Initialization

```java
class EagerSingleton {
    // Holds the single shared instance (created immediately at class load time)
    private static final EagerSingleton instance = new EagerSingleton();

    // Private constructor prevents creating objects from outside the class
    private EagerSingleton() {}

    // Global access point to get the Singleton instance
    public static EagerSingleton getInstance() {
        // Return the already-created shared instance
        return instance;
    }
}
```
- Tradeoff
  - While it is inherently thread-safe, it could potentially waste resources if the singleton instance is never used by the client application.

## 5. Language Specific Implementations

- JAVA
```java
public class BillPughSingleton {
    private BillPughSingleton() { }

    // Inner class is not loaded until getInstance() is called
    private static class Holder {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return Holder.INSTANCE;
    }
}
```

- The Bill Pugh Singleton implementation, while more complex than Eager Initialization provides a perfect balance of lazy initialization, thread safety, and performance, without the complexities of some other patterns like double-checked locking.




## 6. Enum Singleton (Recommended)


```java
enum EnumSingleton {
    INSTANCE;

    // Public method
    public void doSomething() {
        // Add any singleton logic here
    }
}
```

### The JVM provides four guarantees that no other approach offers:


1. Thread-safe initialization: Enum constants are initialized exactly once when the enum class is loaded, and class loading is thread-safe.
2. Serialization safety: Serializing and deserializing an enum returns the same instance.
3. Reflection safety: The JVM prevents creating enum instances via reflection. Constructor.newInstance() throws an IllegalArgumentException.
4. Single instance guarantee: Enforced at the JVM level, not by your code.

- The only limitation is that enums cannot extend other classes (they implicitly extend java.lang.Enum), so if your Singleton needs a base class, you cannot use this approach.


## 7. Static Block Initialization

```java
class StaticBlockSingleton {
    private static StaticBlockSingleton instance;

    private StaticBlockSingleton() {}

    // Static block for initialization
    static {
        try {
            instance = new StaticBlockSingleton();
        } catch (Exception e) {
            throw new RuntimeException("Exception occurred in creating singleton instance");
        }
    }

    // Public method to get the instance
    public static StaticBlockSingleton getInstance() {
        return instance;
    }
}
```
- The static block is executed when the class is loaded by the JVM. 
- If an exception occurs, it's wrapped in a RuntimeException.
- Performance Overhead 
  - It is thread safe but not lazy-loaded, which might be a drawback if the initialization is resource-intensive or time-consuming.


# Pros and Cons of Singleton Pattern

- Pros:
  - Ensures a single instance of a class and provides a global point of access to it. 
  - Only one object is created, which can be particularly beneficial for resource-heavy classes. 
  - Provides a way to maintain global state within an application. 
  - Supports lazy loading, where the instance is only created when it's first needed. 
  - Guarantees that every object in the application uses the same global resource.

- Cons:
  - Violates the Single Responsibility Principle: The pattern solves two problems at the same time. 
  - In multithreaded environments, special care must be taken to implement Singletons correctly to avoid race conditions. 
  - Introduces global state into an application, which might be difficult to manage. 
  - Classes using the singleton can become tightly coupled to the singleton class. 
  - Singleton patterns can make unit testing difficult due to the global state it introduces.















