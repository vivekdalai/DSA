# Singleton Class (Double-Checked Locking)

```java
public class Singleton {
    private static volatile Singleton instance;

    private Singleton() {
        // private constructor prevents external instantiation
    }

    public static Singleton getInstance() {
        if (instance == null) {                 // 1st check (no lock, fast path)
            synchronized (Singleton.class) {
                if (instance == null) {          // 2nd check (inside lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

# Why `volatile` on `instance`?

`volatile` here isn't about a "volatile class" — it's a modifier on the `instance` field, and it does two jobs that `synchronized` alone doesn't fully cover in this pattern.

## Problem 1: Instruction reordering

`instance = new Singleton()` is not a single atomic operation. It breaks down into roughly three steps:

1. Allocate memory for the object.
2. Run the constructor to initialize it.
3. Assign the memory address to `instance`.

The JVM and CPU are allowed to reorder steps 2 and 3 as an optimization, since a single thread's observable behavior doesn't change. So another thread could see `instance` become non-null (step 3 done) before the constructor has actually finished running (step 2 not done). If that second thread hits the first `if (instance == null)` check, sees it's non-null, and returns it, it gets a half-constructed object — reading fields that haven't been initialized yet.

`volatile` prevents this specific reordering. It establishes a happens-before relationship: any write to a volatile field is guaranteed to be fully visible, in the correct order, to any thread that subsequently reads it. So with `volatile`, by the time another thread sees `instance != null`, the constructor is guaranteed to have fully completed too.

## Problem 2: Visibility across threads (caching)

Without `volatile`, one thread's write to `instance` might sit in a CPU register or core-local cache and not be flushed to main memory right away. Another thread checking `instance == null` outside the synchronized block could keep reading a stale `null` from its own cache, even after the object was created.

`volatile` forces every read/write to go through main memory (or at least be coherent across cores), so all threads see the update promptly.

## Why double-check + `synchronized` isn't enough alone

`synchronized` guarantees mutual exclusion and visibility, but only inside the synchronized block. The whole point of double-checked locking is to avoid paying the synchronization cost on every call — the first `if (instance == null)` check happens outside any lock, specifically so most calls (after the singleton is already created) can return fast without ever entering `synchronized`. Since that first check is unsynchronized, it needs `volatile` to be safe against both reordering and caching issues.

## History

Without `volatile`, this exact pattern is a well-known bug (documented as broken in older JVMs, pre-Java 5) — it can return a partially-constructed instance under real concurrent load, even though it "looks" correct and often works fine in casual testing. `volatile` (with correct semantics since Java 5's memory model update, JSR-133) is what makes double-checked locking actually safe.
