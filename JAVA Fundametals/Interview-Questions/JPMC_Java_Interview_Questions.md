# JPMorgan Chase (JPMC) - Java Interview Question Bank

**Compiled:** 2026-08-07, from 2025-2026 candidate interview reports (Glassdoor, Medium,
NodeFlair, Unstop, Blind) for JPMC Java Developer / Software Engineer roles (India & US).
See **Sources** at the bottom.

------------------------------------------------------------------------

## 0. How JPMC Actually Runs the Java Interview

Based on multiple 2025-2026 reports, the loop typically looks like:

| Round | Format | Duration | Focus |
|---|---|---|---|
| Online Assessment | HackerRank | 60-90 min | 2 coding problems + Java/DBMS/OS MCQs |
| Technical Screen | Video call | 45-60 min | Core Java, OOP, project discussion |
| Technical Round(s) ("Super Day") | In-person/Video | 45-60 min each | Deep Java internals, multithreading, coding, clean code |
| System/Framework Round | In-person/Video | 45-60 min | Spring Boot, DB, or lightweight system design |
| Managerial / Behavioral | Phone/Video | 30-45 min | Project ownership, teamwork, on-call, banking-domain fit |
| HR Round | Phone/Video | 20-30 min | Compensation, logistics |

**Patterns across reports:**
- JPMC is a **Java-first shop** — expect depth on Collections internals, multithreading,
  and JVM behavior, not just syntax trivia.
- Interviewers frequently ask you to **write code on the spot** for "explain X" answers
  (e.g. "write a singleton class", "implement an LRU cache using `LinkedHashMap`").
- Finance/banking-flavored coding prompts show up often: stock buy/sell profit, grouping
  transactions by date, fraud-detection design, ACID/transactions.
- For DSA-only prep (not Java-language questions), use this repo's numbered topic folders
  (`01_DynamicProgramming` .. `11_MonotonicStack`); for system design use `HLD/` and `LLD/`.
  This file focuses specifically on **Java-the-language + JVM + concurrency + Spring basics**,
  which is what makes JPMC's Java round distinct from a generic DSA round.

------------------------------------------------------------------------

## 1. Core Java & OOP

<details>
<summary>Q1. How does <code>HashMap</code> work internally? What happens on a hash collision?</summary>

Asked in nearly every JPMC Java interview report reviewed — treat this as the single
highest-priority question in this file.

- `put(key, value)`: compute `hashCode()`, spread the bits (`hash ^ (hash >>> 16)`), map to
  a bucket via `index = hash & (capacity - 1)`, then insert.
- If the bucket is empty, insert directly. If occupied (collision), traverse the bucket
  (linked list, or a red-black tree if the bucket has grown past the treeify threshold) and
  use `equals()` to check if the key already exists — update the value if so, else append.
- `get(key)`: same hash -> bucket lookup, then `equals()` scan within the bucket.
- **Java 8 change:** once a single bucket's chain exceeds `TREEIFY_THRESHOLD` (8) *and* the
  table capacity is at least 64, the bucket converts from a linked list to a red-black tree,
  turning worst-case lookup from O(n) to O(log n). It untreeifies back to a list at 6 entries
  (hysteresis, to avoid thrashing near the threshold).
- Capacity is always a power of 2 so `hash & (capacity - 1)` is a valid, cheap substitute for
  `hash % capacity`, and also correctly handles negative hash codes (`%` cannot).

```java
class BadKey {
    @Override
    public int hashCode() { return 1; }   // forces every key into one bucket
}
```

**Follow-up asked in real reports:** "What thread-safety issues does `HashMap` have, and
what happens if two threads resize it concurrently?" — pre-Java 8, concurrent resize could
create a cyclic linked list, causing `get()` to infinite-loop. Java 8 changed the resize
algorithm so it no longer creates cycles, but `HashMap` is still not thread-safe (lost
updates, size inconsistency) — use `ConcurrentHashMap` instead.

</details>

<details>
<summary>Q2. <code>ConcurrentHashMap</code> internals — how is thread safety achieved?</summary>

- **Java 7 and earlier:** segment-based locking — the map was divided into segments (default
  16), each independently lockable, so writes to different segments didn't block each other.
- **Java 8+:** dropped segments entirely. Locking is now per-bucket, using `synchronized` on
  the first node of a bin combined with CAS (`Unsafe.compareAndSwapObject`) operations for
  the common case of inserting into an empty bin. This gives finer-grained concurrency than
  segment locking.
- Reads (`get`) are largely lock-free (volatile reads).
- Resize can be helped by multiple threads cooperatively transferring bins.

```java
Map<String, Integer> map = new ConcurrentHashMap<>();
map.merge("count", 1, Integer::sum);  // atomic read-modify-write, no external locking needed
```

</details>

<details>
<summary>Q3. Difference between <code>Comparable</code> and <code>Comparator</code></summary>

**Comparable** — natural ordering, implemented inside the class, single strategy:
```java
class Employee implements Comparable<Employee> {
    int salary;
    public int compareTo(Employee o) { return Integer.compare(this.salary, o.salary); }
}
```

**Comparator** — external, pluggable ordering, supports multiple strategies and chaining:
```java
Comparator<Employee> byNameThenSalary =
    Comparator.comparing((Employee e) -> e.name)
              .thenComparingInt(e -> e.salary)
              .reversed();
```
Use `Comparable` when there is one obvious natural order; use `Comparator` for
sort-by-context or when you can't modify the class.

</details>

<details>
<summary>Q4. Write a thread-safe Singleton class</summary>

The **double-checked locking with a volatile field** version is the one interviewers expect
you to be able to write live:

```java
class Singleton {
    private static volatile Singleton instance;

    private Singleton() {}

    public static Singleton getInstance() {
        if (instance == null) {                 // first check, no locking
            synchronized (Singleton.class) {
                if (instance == null) {          // second check, inside the lock
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```
`volatile` is required — without it, another thread could observe a partially-constructed
object due to instruction reordering during `new Singleton()`.

**Preferred modern alternative (mention this too):** the initialization-on-demand holder
idiom, or an `enum` singleton, both of which are simpler and thread-safe by construction:
```java
enum Singleton { INSTANCE; }
```

</details>

<details>
<summary>Q5. Difference between abstract class and interface — when do you choose one over the other?</summary>

Same content as the general note in `Common asked quetions.md` — abstract class for shared
state + partial implementation among closely related types; interface for a capability
contract across unrelated types, especially with `default`/`static` methods (Java 8+) for
API evolution without breaking implementers.

**JPMC-specific framing to have ready:** "abstract class vs. interface — with a use case,"
i.e. be ready to justify the choice with a concrete example (e.g. `PaymentProcessor`
interface implemented by `CardPayment`, `WirePayment`, `ACHPayment` vs. an abstract
`BaseReportGenerator` sharing template-method logic).

</details>

<details>
<summary>Q6. Exception hierarchy in Java — checked vs. unchecked, and why does it matter for API design?</summary>

```
Throwable
├── Error (JVM-level, not meant to be caught: OutOfMemoryError, StackOverflowError)
└── Exception
    ├── Checked (must handle/declare: IOException, SQLException)
    └── RuntimeException (unchecked: NullPointerException, IllegalArgumentException)
```
- Checked exceptions force callers to handle recoverable conditions (e.g. a file might not
  exist) — the compiler enforces it via `throws`.
- Unchecked exceptions represent programming errors that shouldn't normally be caught deep
  in the call stack.
- **Design-taste follow-up interviewers ask:** "should a custom business exception be
  checked or unchecked?" — in most modern services (including Spring-based ones), unchecked
  is preferred, since checked exceptions pollute method signatures and don't compose well
  with lambdas/streams. Use checked only when the caller has a realistic, local recovery
  action.

```java
try (BufferedReader br = new BufferedReader(new FileReader("f.txt"))) {   // try-with-resources
    return br.readLine();
} catch (IOException | NumberFormatException e) {                        // multi-catch
    throw new RuntimeException(e);
}
```

</details>

<details>
<summary>Q7. Why is <code>String</code> immutable in Java?</summary>

- `final` class, `final` internal char/byte array, no mutator methods.
- Enables safe sharing via the **string pool** (interning) without defensive copies.
- Makes `String` safe as a `HashMap` key (hash code can be cached once, computed lazily and
  memoized, since the value can never change).
- Thread-safe without synchronization — immutable objects are inherently shareable across
  threads.
- Security: strings used for class names (`Class.forName`), file paths, network hosts, and
  passwords in JDBC URLs can't be mutated after a security check but before use (a classic
  TOCTOU-style bug that immutability prevents).

</details>

<details>
<summary>Q8. `==` vs `.equals()` for objects, and specifically for `String`</summary>

- `==` compares references (or primitive values). `.equals()` compares logical/contractual
  equality, as defined by the class.
- `String` literals are interned, so two literals with the same content share a reference:
  `"abc" == "abc"` is `true`, but `new String("abc") == "abc"` is `false`.
- Always use `.equals()` for content comparison; JPMC interviewers sometimes pair this with
  a live trick question about `Integer` caching (`Integer.valueOf(127) == Integer.valueOf(127)`
  is `true` due to the `-128..127` integer cache, but `128 == 128` boxed is `false`).

</details>

------------------------------------------------------------------------

## 2. Collections Framework

<details>
<summary>Q9. Difference between <code>ArrayList</code> and <code>LinkedList</code> — and array vs <code>ArrayList</code></summary>

- **Array**: fixed size, primitive or object elements, no built-in resize/utility methods.
- **ArrayList**: backed by a resizable array — O(1) amortized append, O(1) random access,
  O(n) insert/remove in the middle (shifting).
- **LinkedList**: doubly-linked list — O(1) insert/remove at known node (e.g. via iterator),
  O(n) random access. Rarely the right default in practice — `ArrayList` has better cache
  locality for almost all real workloads; know this trade-off, don't reach for `LinkedList`
  by habit.

</details>

<details>
<summary>Q10. Implement an LRU Cache with O(1) get/put</summary>

Asked directly in multiple JPMC reports, usually phrased as "implement an LRU cache," with
the expected solution using `LinkedHashMap`'s access-order mode:

```java
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    LRUCache(int capacity) {
        super(capacity, 0.75f, true);   // true = access-order (not insertion-order)
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;       // auto-evict the least-recently-used entry
    }
}
```
Be ready to also explain the from-scratch version (`HashMap` + doubly linked list) if asked
to avoid using `LinkedHashMap` — that's the classic LC146 solution already in
[03_LinkedList_Queues_PQs/LL_07... folder pattern](../../03_LinkedList_Queues_PQs) /
[12_Stacks/ST_07_LRU_Cache_LC146.md](../../12_Stacks/ST_07_LRU_Cache_LC146.md).

</details>

<details>
<summary>Q11. How do you sort a <code>Map</code> by value?</summary>

```java
Map<String, Integer> sorted = map.entrySet().stream()
    .sorted(Map.Entry.comparingByValue())
    .collect(Collectors.toMap(
        Map.Entry::getKey, Map.Entry::getValue,
        (a, b) -> a, LinkedHashMap::new));   // merge fn + LinkedHashMap preserves order
```
Know why a plain `Collectors.toMap` without the `LinkedHashMap::new` supplier loses the
sort order (default implementation is `HashMap`).

</details>

<details>
<summary>Q12. <code>fail-fast</code> vs <code>fail-safe</code> iterators</summary>

- **Fail-fast** (`ArrayList`, `HashMap` iterators): throw `ConcurrentModificationException`
  if the collection is structurally modified while iterating, detected via a `modCount`
  check.
- **Fail-safe** (`CopyOnWriteArrayList`, `ConcurrentHashMap` iterators): iterate over a
  snapshot or tolerate concurrent structural changes, never throwing, but may not reflect
  the latest state.

</details>

------------------------------------------------------------------------

## 3. Multithreading & Concurrency

<details>
<summary>Q13. <code>Runnable</code> vs <code>Callable</code></summary>

- `Runnable.run()` returns nothing and cannot throw checked exceptions.
- `Callable<V>.call()` returns a value `V` and can throw checked exceptions — designed to be
  submitted to an `ExecutorService` and tracked via `Future<V>`.

```java
ExecutorService pool = Executors.newFixedThreadPool(4);
Future<Integer> f = pool.submit(() -> { Thread.sleep(100); return 42; });  // Callable
pool.submit(() -> System.out.println("fire and forget"));                 // Runnable
Integer result = f.get();  // blocks
pool.shutdown();
```

</details>

<details>
<summary>Q14. <code>synchronized</code> vs <code>ReentrantLock</code></summary>

| | `synchronized` | `ReentrantLock` |
|---|---|---|
| Acquisition | Implicit (block/method) | Explicit `lock()`/`unlock()` |
| Fairness | No fairness guarantee | Optional fair mode |
| Interruptible wait | No | Yes (`lockInterruptibly()`) |
| Try without blocking | No | Yes (`tryLock()`) |
| Condition variables | One implicit monitor | Multiple `Condition`s per lock |

Always pair explicit locks with `try { ... } finally { lock.unlock(); }` — a missed unlock
after an exception is the classic bug interviewers probe for.

</details>

<details>
<summary>Q15. Explain the producer-consumer problem and how you'd implement it</summary>

Prefer `BlockingQueue` (`put()`/`take()` block automatically) over manual
`wait()`/`notify()`. Full worked example already in
[LL_07-style notes / this repo's earlier Java Fundamentals note](../Common%20asked%20quetions.md)
under Q7 of the Advance section — know both the `BlockingQueue` version and the manual
`synchronized` + `wait()`/`notify()` version (always `wait()` in a `while` loop, never `if`,
to guard against spurious wakeups).

</details>

<details>
<summary>Q16. What are Virtual Threads (Java 21)? Why do they matter for a backend like JPMC's?</summary>

- Lightweight threads managed by the JVM, not 1:1 mapped to OS threads — you can spin up
  millions of them.
- Ideal for I/O-bound workloads (exactly what a banking backend making many downstream
  calls to other services/DBs looks like): a blocking call on a virtual thread unmounts the
  underlying OS "carrier" thread instead of tying it up, so throughput scales far beyond
  what a fixed platform-thread pool allows.
- API-compatible with existing `Thread`/`ExecutorService` code:
```java
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    executor.submit(() -> callDownstreamService());
}
```
- Know the caveat: `synchronized` blocks can still pin a virtual thread to its carrier
  (improved in later JDKs) — prefer `ReentrantLock` in virtual-thread-heavy code.

</details>

<details>
<summary>Q17. What race conditions have you debugged? How does a deadlock happen and how do you avoid it?</summary>

- **Race condition:** outcome depends on non-deterministic thread interleaving over shared
  mutable state (e.g. unsynchronized `counter++`, which is read-modify-write, not atomic).
- **Deadlock:** classic four conditions — mutual exclusion, hold-and-wait, no preemption,
  circular wait. Avoid via consistent lock ordering, lock timeouts (`tryLock` with timeout),
  or avoiding nested locks altogether.
- Be ready with a concrete story (JPMC interviewers explicitly ask "what race conditions
  have you debugged in production" per candidate reports) — this is a behavioral-technical
  hybrid question, have a real example ready, not just the textbook definition.

</details>

------------------------------------------------------------------------

## 4. Java Version Features (heavily asked at JPMC)

<details>
<summary>Q18. Key features by version — 7, 8, 11, 17, 21</summary>

- **Java 7:** try-with-resources, multi-catch, diamond operator (`<>`), switch on Strings.
- **Java 8 (the big one — know this cold):** lambdas, Streams API, `Optional`, default/static
  interface methods, new `java.time` package, method references.
- **Java 11 (LTS):** `var` for local variables (from 10), new `HttpClient`, `String` methods
  (`isBlank`, `strip`, `repeat`, `lines`), single-file source-code launching.
- **Java 17 (LTS):** sealed classes, pattern matching for `instanceof`, records (from 16),
  text blocks (from 15), stronger encapsulation of JDK internals.
- **Java 21 (LTS):** virtual threads (Project Loom), pattern matching for `switch`, record
  patterns, sequenced collections.

```java
// Records (Java 16+) — immutable data carrier, auto-generates constructor/equals/hashCode/toString
record Point(int x, int y) {}

// Sealed classes (Java 17) — restrict which classes can implement/extend
sealed interface Shape permits Circle, Square {}

// Pattern matching for switch (Java 21)
String describe(Object o) {
    return switch (o) {
        case Integer i when i > 0 -> "positive int";
        case String s -> "string: " + s;
        default -> "unknown";
    };
}
```

</details>

<details>
<summary>Q19. Given an array, remove odd numbers, multiply the rest by a constant, and sum — using Streams</summary>

Asked verbatim in JPMC reports as a live Streams-fluency check:

```java
int result = Arrays.stream(nums)
    .filter(n -> n % 2 == 0)
    .map(n -> n * constant)
    .sum();
```
Know `IntStream` vs `Stream<Integer>` boxing overhead, and be ready to rewrite with
`Collectors.groupingBy`/`Collectors.summingInt` if the interviewer changes it to "group by X
then sum."

</details>

------------------------------------------------------------------------

## 5. Design Patterns

<details>
<summary>Q20. Which design patterns have you used, and why?</summary>

Have 3-4 ready with a concrete "used it because..." story, not just definitions:

- **Singleton** — shared config/connection pool (see Q4 for the thread-safe implementation).
- **Factory / Abstract Factory** — creating one of several `PaymentProcessor` types based on
  runtime config, without callers depending on concrete classes.
- **Strategy** — pluggable interest-calculation or risk-scoring algorithms selected at
  runtime.
- **Builder** — constructing complex immutable objects (e.g. a `TransactionRequest` with
  many optional fields) without telescoping constructors.
- **Observer** — event/notification systems (a trade executing and notifying multiple
  downstream listeners).
- **Decorator** — wrapping a base service with logging/retry/caching layers without
  modifying it.

Full write-ups for these (with problem framing, structure, and trade-offs) live in
[LLD/](../../LLD).

</details>

------------------------------------------------------------------------

## 6. Spring / Spring Boot Basics (asked alongside core Java at JPMC)

<details>
<summary>Q21. What is Dependency Injection, and what is Spring's role in it?</summary>

DI is a pattern where a class's dependencies are provided from the outside (constructor,
setter, or field) rather than the class constructing them itself — this is Inversion of
Control. Spring's `ApplicationContext` is an IoC container: it reads bean definitions
(annotations or XML), builds the dependency graph, and injects collaborators automatically.
Full example already in `Common asked quetions.md` (Advance Q10) — prefer constructor
injection (enables `final` fields, makes required dependencies explicit, plays well with
unit testing without a container).

</details>

<details>
<summary>Q22. Spring vs Spring Boot; what does <code>@SpringBootApplication</code> do?</summary>

- **Spring**: the core framework (IoC container, AOP, MVC, etc.) — historically required a
  lot of manual XML/Java config.
- **Spring Boot**: opinionated auto-configuration and starter dependencies on top of Spring,
  removing most boilerplate (embedded Tomcat/Jetty, sane defaults, `application.properties`).
- `@SpringBootApplication` = `@Configuration` + `@ComponentScan` + `@EnableAutoConfiguration`.

</details>

<details>
<summary>Q23. How does <code>@Autowired</code> / autowiring work, and how do you handle a circular dependency?</summary>

Spring resolves autowiring by type (falling back to name on ambiguity, or requiring
`@Qualifier`). For circular dependencies (`A` needs `B`, `B` needs `A`):
- Refactor to remove the cycle where possible (often signals a design smell).
- If unavoidable, use setter/field injection for one side (constructor injection cannot
  resolve a cycle, since both beans need to be fully constructed before either is usable),
  or use `@Lazy` on one of the injection points.

</details>

<details>
<summary>Q24. What is a RESTful API? REST vs SOAP?</summary>

REST is an architectural style over HTTP: stateless, resource-oriented (nouns as URLs, HTTP
verbs as actions), typically JSON. SOAP is a stricter, XML-based protocol with a formal
contract (WSDL), built-in error handling, and stronger standards for security/transactions
— historically common in enterprise/banking integrations, which is why JPMC interviewers
specifically check you know both, not just REST.

</details>

------------------------------------------------------------------------

## 7. Rapid-Fire List (seen across multiple JPMC reports, answer in 1-2 sentences each)

- What is `try-with-resources` and what interface must a resource implement? (`AutoCloseable`)
- What's the difference between `throw` and `throws`?
- What is the contract between `equals()` and `hashCode()`?
- What is a checked exception, and name two examples?
- Difference between `TRUNCATE` and `DELETE` (asked alongside Java — know it, it's not Java
  but shows up in the same round).
- What is an `ExceptionHandler` (`@ExceptionHandler`/`@ControllerAdvice` in Spring)?
- What is AOP, and what is a Spring Boot Actuator?
- How would you swap the embedded server from Tomcat to Jetty in Spring Boot?
- What is `ACID`, and how does it relate to a banking transaction system specifically?
- What is a `Quarkus` framework, and how does it differ from Spring Boot (fast-startup,
  GraalVM-native-image-oriented alternative)?

------------------------------------------------------------------------

## Sources

- [JPMorganChase Interview Questions For a Java Developer — Javarevisited/Medium](https://medium.com/javarevisited/jpmorganchase-interview-questions-for-a-java-developer-5e82a7c69be5)
- [JP Morgan AVP/SDE3 Interview Experience — Medium](https://khaniqbal.medium.com/jp-morgan-avp-sde3-interview-experience-selected-df2d199dfd20)
- [JP Morgan Interview Questions (Technical & HR) 2026 — Unstop](https://unstop.com/blog/jp-morgan-interview-questions)
- [JPMorgan Chase Interview Questions India 2026 — JobRise](https://jobrise.io/en/blog/jpmorgan-interview-questions-india-2026/)
- [J.P. Morgan Java Developer Interview Questions — Glassdoor](https://www.glassdoor.com/Interview/J-P-Morgan-Java-Developer-Interview-Questions-EI_IE145.0,10_KO11,25.htm)
- [My Interview Experience at JP Morgan: Java, Multithreading, Microservices — Medium](https://medium.com/@karunakunwar899/my-interview-experience-at-jp-morgan-java-multithreading-and-microservices-questions-da12f118396c)
- [JPMorgan Chase & Co. Software Engineer - Java Developer Interviews — NodeFlair](https://nodeflair.com/companies/jpmorgan-chase-co/interviews/software-engineer-java-developer)

------------------------------------------------------------------------

# End of Notes
