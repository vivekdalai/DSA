## Fundamentals

<details>
<summary>Q1. Difference between abstract class and interface</summary>

**Abstract Class:**
- Can have both abstract and concrete methods
- Can have instance variables (fields)
- Can have constructors
- Supports single inheritance (extends one class)
- Can have access modifiers (private, protected, public)

**Interface:**
- Before Java 8: only abstract methods
- Java 8+: can have default and static methods
- Only constants (public static final fields)
- No constructors
- Supports multiple inheritance (implements multiple interfaces)
- All methods are public by default

```java
// Abstract class example
abstract class Animal {
    private String name;

    public Animal(String name) {
        this.name = name;
    }

    abstract void makeSound();

    public void eat() {
        System.out.println("Eating...");
    }
}

// Interface example
interface Flyable {
    void fly();

    default void land() {
        System.out.println("Landing...");
    }

    static void info() {
        System.out.println("Flying capability");
    }
}
```

</details>

<details>
<summary>Q2. Why were <code>default</code> and <code>static</code> methods introduced in interfaces in Java 8?</summary>

**Default methods:** Allow interfaces to evolve without breaking existing implementations. Provide a default implementation that implementing classes can override or inherit.

**Static methods:** Utility methods that belong to the interface, not to implementing classes. Can be called without an instance.

This enables backward compatibility when adding new methods to interfaces like Collection, List, etc.

```java
interface MyInterface {
    void abstractMethod();

    default void defaultMethod() {
        System.out.println("Default implementation");
    }

    static void staticMethod() {
        System.out.println("Static utility method");
    }
}

class MyClass implements MyInterface {
    @Override
    public void abstractMethod() {
        // Must implement
    }

    // Can override default method or use as is
}
```

</details>

<details>
<summary>Q3. Contract of <code>equals()</code> and <code>hashCode()</code></summary>

If two objects are equal according to `equals()`, they must return the same `hashCode()`.

The reverse is not required: two objects can have the same `hashCode()` and still not be equal.

</details>

<details>
<summary>Q4. What is a checked exception?</summary>

Checked exceptions are exceptions that are checked at compile time. The compiler forces the programmer to either handle them using try-catch blocks or declare them in the method signature using `throws`.

Examples: `IOException`, `SQLException`, `ClassNotFoundException`.

Unchecked exceptions (RuntimeException and its subclasses) are not checked at compile time.

```java
public void readFile() throws IOException {  // Must declare checked exception
    FileReader fr = new FileReader("file.txt");
    // ...
}

public void divide(int a, int b) {
    if (b == 0) {
        throw new ArithmeticException();  // Unchecked, no need to declare
    }
}
```

</details>

<details>
<summary>Q5. Difference between <code>throw</code> and <code>throws</code></summary>

- `throw`: Used to explicitly throw an exception in the code.
- `throws`: Used in method signature to declare that the method can throw certain exceptions.

```java
public void method1() {
    throw new RuntimeException("Error occurred");  // throw keyword
}

public void method2() throws IOException {  // throws keyword
    // method can throw IOException
    if (someCondition) {
        throw new IOException("File not found");
    }
}
```

</details>

<details>
<summary>Q6. Why is <code>String</code> immutable? How does it improve security?</summary>

`String` is immutable because:
- The class is `final`, preventing subclassing
- All fields are `final` and private
- No setter methods to modify the state

**Security benefits:**
- Strings are used as keys in HashMap, parameters in security methods
- Prevents accidental or malicious modification
- Thread-safe without synchronization
- String literals are interned in string pool for memory efficiency

```java
String s1 = "Hello";
String s2 = s1.concat(" World");  // Creates new string, s1 unchanged
System.out.println(s1);  // "Hello"
System.out.println(s2);  // "Hello World"
```

</details>

<details>
<summary>Q7. Difference between <code>Comparator</code> and <code>Comparable</code></summary>

**Comparable:**
- Used for natural ordering of objects
- Implement in the class itself
- Method: `compareTo(T o)`
- Used by Collections.sort(), Arrays.sort()

**Comparator:**
- Used for custom/external sorting
- Implement as separate class or lambda
- Method: `compare(T o1, T o2)`
- Allows multiple sorting strategies

```java
class Person implements Comparable<Person> {
    String name;
    int age;

    @Override
    public int compareTo(Person other) {
        return this.age - other.age;  // Natural order by age
    }
}

// Custom comparator
Comparator<Person> byName = (p1, p2) -> p1.name.compareTo(p2.name);

List<Person> people = Arrays.asList(new Person("Alice", 25), new Person("Bob", 30));
Collections.sort(people);  // Uses compareTo
Collections.sort(people, byName);  // Uses comparator
```

</details>

## Advance

<details>
<summary>Q1. What does <code>@SpringBootApplication</code> do? What is the alternative?</summary>

`@SpringBootApplication` is a convenience annotation that combines:

1. `@ComponentScan`
2. `@EnableAutoConfiguration`
3. `@Configuration`

Alternative: use these three annotations separately instead of `@SpringBootApplication`.

</details>

<details>
<summary>Q2. Difference between process and thread</summary>

**Process:**
- An instance of a program in execution
- Has its own memory space, resources, and address space
- Processes are independent and isolated
- Communication between processes is complex (IPC)
- Heavyweight - more resource intensive

**Thread:**
- A unit of execution within a process
- Shares the process's memory and resources
- Threads within the same process can communicate easily
- Lightweight - less overhead than processes
- Multiple threads can run concurrently in the same process

```java
// Creating a thread
Thread thread = new Thread(() -> {
    System.out.println("Running in thread: " + Thread.currentThread().getName());
});
thread.start();

// Process example (using ProcessBuilder)
ProcessBuilder pb = new ProcessBuilder("notepad.exe");
Process process = pb.start();
```

</details>

<details>
<summary>Q3. What is <code>Future</code> in Java, and how is it used with <code>ExecutorService</code>?</summary>

`Future` represents the result of an asynchronous computation. It allows you to check if the computation is complete, wait for it to complete, and retrieve the result.

Used with `ExecutorService` to submit tasks and get a `Future` back.

```java
ExecutorService executor = Executors.newFixedThreadPool(2);

// Submit Runnable - returns Future<Void>
Future<?> future1 = executor.submit(() -> {
    System.out.println("Running task");
});

// Submit Callable - returns Future<T>
Future<Integer> future2 = executor.submit(() -> {
    Thread.sleep(1000);
    return 42;
});

// Check if done
if (future2.isDone()) {
    Integer result = future2.get();  // Blocking call
    System.out.println("Result: " + result);
}

// Cancel if needed
future2.cancel(true);

executor.shutdown();
```

</details>

<details>
<summary>Q4. Difference between <code>Callable</code> and <code>Runnable</code></summary>

`Runnable` does not return a result.

`Callable` can return a result and can throw checked exceptions.

</details>

<details>
<summary>Q5. How does Java handle concurrency?</summary>

Java provides comprehensive support for concurrency through:

1. **Thread class and Runnable interface** - Basic thread creation
2. **Executor framework** - High-level concurrency utilities
3. **Synchronization** - `synchronized` keyword, locks
4. **Concurrent collections** - Thread-safe collections (ConcurrentHashMap, etc.)
5. **Atomic variables** - Lock-free thread-safe operations
6. **CompletableFuture** - Composable asynchronous programming

```java
// Synchronization
public synchronized void synchronizedMethod() {
    // Only one thread can execute this at a time
}

// Using locks
Lock lock = new ReentrantLock();
lock.lock();
try {
    // Critical section
} finally {
    lock.unlock();
}

// Atomic variables
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();  // Thread-safe

// ExecutorService
ExecutorService executor = Executors.newFixedThreadPool(10);
Future<String> future = executor.submit(() -> "Result");
```

</details>

<details>
<summary>Q6. What are virtual threads in Java 21?</summary>

Virtual threads (Project Loom) are lightweight threads that dramatically reduce the effort of writing, maintaining, and debugging high-throughput concurrent applications.

**Key characteristics:**
- Not tied to OS threads - managed by JVM
- Cheap to create and destroy
- Enable millions of concurrent threads
- Ideal for I/O-bound applications
- Backward compatible with existing Thread API

```java
// Creating virtual threads
Thread virtualThread = Thread.ofVirtual().start(() -> {
    System.out.println("Running in virtual thread");
});

// Using ExecutorService with virtual threads
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> {
        // Task running on virtual thread
        return "Result";
    });
}

// Traditional platform thread for comparison
Thread platformThread = Thread.ofPlatform().start(() -> {
    System.out.println("Running on platform thread");
});
```

</details>

<details>
<summary>Q7. Explain the producer-consumer problem</summary>

A classic synchronization problem where one or more producers produce items and place them in a buffer, and one or more consumers consume items from the buffer.

**Key challenges:**
- Producers must wait when buffer is full
- Consumers must wait when buffer is empty
- Avoid race conditions during access

**Solutions:**
- Use `BlockingQueue` (simplest)
- Use `wait()` and `notify()` with synchronization
- Use semaphores

```java
// Using BlockingQueue (recommended)
BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

Thread producer = new Thread(() -> {
    try {
        while (true) {
            queue.put(produceItem());  // Blocks if queue is full
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
});

Thread consumer = new Thread(() -> {
    try {
        while (true) {
            Integer item = queue.take();  // Blocks if queue is empty
            consumeItem(item);
        }
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
    }
});

// Manual implementation with wait/notify
class Buffer {
    private final Queue<Integer> queue = new LinkedList<>();
    private final int capacity = 10;

    public synchronized void produce(int item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();  // Wait if buffer is full
        }
        queue.add(item);
        notify();  // Notify consumer
    }

    public synchronized int consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();  // Wait if buffer is empty
        }
        int item = queue.remove();
        notify();  // Notify producer
        return item;
    }
}
```

</details>

<details>
<summary>Q8. What happens when <code>ThreadPoolExecutor</code> is exhausted?</summary>

When the thread pool and queue are full, new tasks are rejected.

What happens next depends on the configured `RejectedExecutionHandler` or rejection policy.

</details>

<details>
<summary>Q9. Why choose Spring Boot over the Spring Framework?</summary>

Spring Boot makes configuration and setup easier, reduces boilerplate, and helps build production-ready applications faster.

</details>

<details>
<summary>Q10. What is Dependency Injection?</summary>

Dependency Injection (DI) is a design pattern where dependencies of a class are injected from outside rather than the class creating them itself. This promotes loose coupling and makes code more testable.

**Types of DI:**
- Constructor injection (recommended)
- Setter injection
- Field injection

**Benefits:**
- Loose coupling
- Easier testing (mock dependencies)
- Better maintainability
- Inversion of Control (IoC)

```java
// Without DI - tight coupling
class Service {
    private Repository repo = new Repository();  // Hard dependency
}

// With DI - loose coupling
class Service {
    private final Repository repo;

    // Constructor injection
    public Service(Repository repo) {
        this.repo = repo;
    }

    // Setter injection
    @Autowired
    public void setRepository(Repository repo) {
        this.repo = repo;
    }
}

// In Spring
@Configuration
class AppConfig {
    @Bean
    public Repository repository() {
        return new RepositoryImpl();
    }

    @Bean
    public Service service(Repository repo) {
        return new Service(repo);  // DI via constructor
    }
}
```

</details>

<details>
<summary>Q11. What is the N+1 problem?</summary>

The N+1 problem occurs in ORM (Object-Relational Mapping) frameworks like Hibernate when fetching related entities. It results in N+1 database queries instead of a single optimized query.

**Example:**
- You fetch N parent entities (1 query)
- For each parent, you access lazy-loaded child entities (N additional queries)
- Total: 1 + N queries

**Solutions:**
- Eager loading with `JOIN FETCH` in JPQL
- Batch fetching with `@BatchSize`
- Entity graphs
- DTO projections

```java
// Problematic code (N+1 queries)
List<Order> orders = entityManager.createQuery("SELECT o FROM Order o", Order.class)
                                  .getResultList();

for (Order order : orders) {
    System.out.println(order.getItems().size());  // Triggers N additional queries
}

// Solution 1: JOIN FETCH (single query with join)
List<Order> orders = entityManager.createQuery(
    "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items", Order.class)
    .getResultList();

// Solution 2: Entity Graph
@Entity
@NamedEntityGraph(name = "Order.withItems",
    attributeNodes = @NamedAttributeNode("items"))
class Order {
    // ...
}

// Usage
EntityGraph<?> graph = entityManager.getEntityGraph("Order.withItems");
Map<String, Object> hints = Map.of("javax.persistence.fetchgraph", graph);
List<Order> orders = entityManager.find(Order.class, hints);
```

</details>
