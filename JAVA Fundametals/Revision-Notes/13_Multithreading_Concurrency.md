# Multithreading & Concurrency

## Why it matters

Modern CPUs have multiple cores. A single-threaded program can only ever use one. Multithreading lets a program do multiple things concurrently (interleaved, possibly on one core) or in parallel (simultaneously, on multiple cores) — better CPU utilization, responsiveness (e.g. UI thread stays free while work happens in the background), and throughput for I/O-bound work (a thread can wait on a network call while others keep running).

The cost: shared mutable state accessed from multiple threads introduces race conditions, visibility problems, and deadlocks — problems that don't exist in single-threaded code. Most of concurrency is about controlling that cost.

---

## 1. Creating Threads

### Extending `Thread`

```java
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}

MyThread t = new MyThread();
t.start(); // starts a new thread and calls run() on it
```

### Implementing `Runnable` (preferred)

```java
class MyTask implements Runnable {
    @Override
    public void run() {
        System.out.println("Running in: " + Thread.currentThread().getName());
    }
}

Thread t = new Thread(new MyTask());
t.start();

// Or with a lambda, since Runnable is a functional interface
Thread t2 = new Thread(() -> System.out.println("Lambda thread"));
t2.start();
```

**Prefer `Runnable` over extending `Thread`.** Java has single inheritance — extending `Thread` burns your one superclass slot. `Runnable` lets the class extend something else if needed, and it separates "the task" from "the mechanism that runs it," which composes better with thread pools (see Executors below).

### `start()` vs `run()`

Calling `run()` directly just executes the method on the *current* thread — no new thread is created. Only `start()` spawns a new OS thread and schedules `run()` on it. This is a classic gotcha.

---

## 2. Thread Lifecycle

```
NEW -> RUNNABLE -> (BLOCKED / WAITING / TIMED_WAITING) -> TERMINATED
```

- **NEW** — created but `start()` not yet called.
- **RUNNABLE** — eligible to run; may or may not actually be executing (depends on OS scheduler).
- **BLOCKED** — waiting to acquire a lock held by another thread (e.g. blocked entering a `synchronized` block).
- **WAITING / TIMED_WAITING** — waiting indefinitely or for a bounded time (`Object.wait()`, `Thread.join()`, `Thread.sleep()`).
- **TERMINATED** — `run()` has completed.

```java
Thread t = new Thread(() -> {});
System.out.println(t.getState()); // NEW
t.start();
System.out.println(t.getState()); // RUNNABLE (likely)
t.join();
System.out.println(t.getState()); // TERMINATED
```

---

## 3. The Core Problem: Race Conditions

A race condition happens when multiple threads read/modify shared state without coordination, and the outcome depends on timing.

```java
class Counter {
    private int count = 0;

    public void increment() {
        count++; // NOT atomic: read, add 1, write — 3 separate steps
    }

    public int getCount() {
        return count;
    }
}
```

```java
Counter counter = new Counter();
Runnable task = () -> {
    for (int i = 0; i < 100_000; i++) counter.increment();
};

Thread t1 = new Thread(task);
Thread t2 = new Thread(task);
t1.start(); t2.start();
t1.join(); t2.join();

System.out.println(counter.getCount()); // expected 200000, actual: unpredictable, usually less
```

`count++` compiles to a read, an increment, and a write. If two threads interleave these steps, updates get lost — thread A reads 5, thread B reads 5, both write 6, and one increment vanishes.

---

## 4. `synchronized` — Mutual Exclusion

`synchronized` ensures only one thread executes a given block/method on a given lock at a time, and also establishes a happens-before relationship (visibility guarantee), like `volatile` does for individual fields.

```java
class Counter {
    private int count = 0;

    public synchronized void increment() { // lock is `this`
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
```

Equivalent explicit block form, useful when you don't want to lock the whole method or want to lock on a different object:

```java
class Counter {
    private int count = 0;
    private final Object lock = new Object();

    public void increment() {
        synchronized (lock) {
            count++;
        }
    }
}
```

With this fix, the same two-thread test above reliably prints `200000`.

### Cost of `synchronized`

Every `synchronized` call has overhead (lock acquisition/release, potential thread blocking) and, more importantly, **serializes** access — only one thread proceeds at a time through that critical section, killing parallelism for that piece of code. Keep synchronized blocks as small as possible.

---

## 5. `volatile` vs `synchronized`

| | `volatile` | `synchronized` |
|---|---|---|
| Guarantees visibility | Yes | Yes |
| Prevents reordering | Yes (for that field) | Yes (within the block) |
| Guarantees atomicity of compound ops (`count++`) | No | Yes |
| Can block threads | No | Yes |
| Applies to | A single field | A block of code |

`volatile` is for simple flag/reference visibility (e.g. a `boolean running` flag read by multiple threads, or the singleton `instance` field — see [[09_Singleton_Volatile]]). It does **not** make compound operations atomic.

```java
private volatile boolean running = true;

// Thread A
while (running) { doWork(); }

// Thread B
running = false; // guaranteed to be seen promptly by Thread A
```

---

## 6. `wait()` / `notify()` / `notifyAll()` — Inter-Thread Communication

These let threads coordinate rather than just exclude each other. They must be called from within a `synchronized` block on the object being waited on, or they throw `IllegalMonitorStateException`.

Classic producer-consumer with a bounded buffer:

```java
class BoundedBuffer<T> {
    private final Queue<T> queue = new LinkedList<>();
    private final int capacity;

    BoundedBuffer(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void produce(T item) throws InterruptedException {
        while (queue.size() == capacity) {
            wait(); // releases the lock and waits until notified
        }
        queue.add(item);
        notifyAll(); // wake up any waiting consumers
    }

    public synchronized T consume() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        T item = queue.poll();
        notifyAll(); // wake up any waiting producers
        return item;
    }
}
```

Note the `while` (not `if`) around `wait()` — this guards against **spurious wakeups** (a thread can wake up without an actual `notify()`), and against another thread grabbing the resource first once notified.

In modern code, prefer `BlockingQueue` (see below) over hand-rolled `wait`/`notify` — it's easy to get subtly wrong.

---

## 7. `ExecutorService` — Thread Pools

Creating a raw `Thread` per task doesn't scale — thread creation is expensive, and unbounded thread creation can exhaust system resources. `ExecutorService` manages a pool of reusable worker threads.

```java
ExecutorService executor = Executors.newFixedThreadPool(4);

for (int i = 0; i < 10; i++) {
    int taskId = i;
    executor.submit(() -> {
        System.out.println("Task " + taskId + " on " + Thread.currentThread().getName());
    });
}

executor.shutdown(); // stop accepting new tasks, let submitted ones finish
```

### Common factory methods

- `Executors.newFixedThreadPool(n)` — fixed-size pool, good for CPU-bound work sized to core count.
- `Executors.newCachedThreadPool()` — grows/shrinks as needed, good for many short-lived I/O-bound tasks.
- `Executors.newSingleThreadExecutor()` — one worker, tasks run sequentially in submission order.
- `Executors.newScheduledThreadPool(n)` — supports delayed/periodic tasks.

### `Callable` + `Future` — getting results back

`Runnable.run()` returns nothing and can't throw checked exceptions. `Callable<V>` can do both.

```java
ExecutorService executor = Executors.newFixedThreadPool(2);

Callable<Integer> task = () -> {
    Thread.sleep(1000);
    return 42;
};

Future<Integer> future = executor.submit(task);

// do other work here while the task runs...

Integer result = future.get(); // blocks until the result is available
System.out.println(result); // 42

executor.shutdown();
```

`future.get(timeout, unit)` avoids blocking forever; `future.isDone()` / `future.cancel()` give more control.

### `CompletableFuture` — composing async work without blocking

```java
CompletableFuture<Integer> future = CompletableFuture
        .supplyAsync(() -> fetchUserId())          // runs on a common ForkJoinPool thread
        .thenApply(id -> id * 2)                    // transform the result
        .thenApply(doubled -> doubled + 1);

future.thenAccept(result -> System.out.println("Final: " + result));

// combining two independent async calls
CompletableFuture<Integer> a = CompletableFuture.supplyAsync(() -> 10);
CompletableFuture<Integer> b = CompletableFuture.supplyAsync(() -> 20);
CompletableFuture<Integer> sum = a.thenCombine(b, Integer::sum);
System.out.println(sum.join()); // 30
```

`CompletableFuture` is the modern preferred way to chain async operations without manually blocking on `Future.get()` everywhere — it composes, and lets you build pipelines that only block (if ever) at the very end.

---

## 8. `java.util.concurrent.atomic` — Lock-Free Thread Safety

For simple counters/flags, atomic classes give you thread safety without the overhead of locking, using low-level CPU compare-and-swap (CAS) instructions.

```java
class Counter {
    private final AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet(); // atomic, no synchronized needed
    }

    public int getCount() {
        return count.get();
    }
}
```

Same two-thread test as before now reliably prints `200000`, with no explicit locking at all. Use `AtomicInteger`, `AtomicLong`, `AtomicBoolean`, `AtomicReference` when the operation you need is a single atomic update — they're faster than `synchronized` under contention because they avoid blocking.

---

## 9. Concurrent Collections

Regular `HashMap`, `ArrayList`, etc. are **not** thread-safe — concurrent modification can corrupt internal structure, not just produce wrong values. `java.util.concurrent` provides safe alternatives:

```java
// Thread-safe map, better concurrency than Collections.synchronizedMap()
Map<String, Integer> map = new ConcurrentHashMap<>();
map.put("a", 1);
map.computeIfAbsent("b", k -> 2);

// Thread-safe queue for producer-consumer, blocks instead of needing manual wait/notify
BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

// Producer
new Thread(() -> {
    try {
        for (int i = 0; i < 5; i++) queue.put(i); // blocks if full
    } catch (InterruptedException ignored) {}
}).start();

// Consumer
new Thread(() -> {
    try {
        for (int i = 0; i < 5; i++) System.out.println(queue.take()); // blocks if empty
    } catch (InterruptedException ignored) {}
}).start();

// Thread-safe list variant optimized for many reads, few writes
List<String> list = new CopyOnWriteArrayList<>();
```

`ConcurrentHashMap` achieves high concurrency by locking only small internal segments/buckets rather than the whole map, unlike `Collections.synchronizedMap()`, which locks the entire map on every operation.

---

## 10. Coordination Utilities

```java
// CountDownLatch — wait for N events to complete before proceeding (one-time use)
CountDownLatch latch = new CountDownLatch(3);
for (int i = 0; i < 3; i++) {
    new Thread(() -> {
        doWork();
        latch.countDown();
    }).start();
}
latch.await(); // blocks until count reaches 0
System.out.println("All workers finished");

// Semaphore — limit concurrent access to a resource to N permits
Semaphore semaphore = new Semaphore(2); // only 2 threads at a time
Runnable task = () -> {
    try {
        semaphore.acquire();
        useLimitedResource();
    } catch (InterruptedException ignored) {
    } finally {
        semaphore.release();
    }
};

// CyclicBarrier — wait for N threads to all reach a point, then release them together, reusable
CyclicBarrier barrier = new CyclicBarrier(3, () -> System.out.println("All parties arrived"));
Runnable worker = () -> {
    doPhase1Work();
    try {
        barrier.await(); // waits until all 3 threads call await()
    } catch (Exception ignored) {}
    doPhase2Work();
};
```

---

## 11. Deadlock

Occurs when two or more threads each hold a lock the other needs, and neither can proceed.

```java
Object lockA = new Object();
Object lockB = new Object();

// Thread 1
new Thread(() -> {
    synchronized (lockA) {
        sleep(100);
        synchronized (lockB) { // waits forever if Thread 2 holds lockB
            System.out.println("Thread 1 acquired both");
        }
    }
}).start();

// Thread 2 — acquires locks in the OPPOSITE order
new Thread(() -> {
    synchronized (lockB) {
        sleep(100);
        synchronized (lockA) { // waits forever if Thread 1 holds lockA
            System.out.println("Thread 2 acquired both");
        }
    }
}).start();
```

Thread 1 holds `lockA`, waiting for `lockB`. Thread 2 holds `lockB`, waiting for `lockA`. Neither ever releases. Classic fix: always acquire locks in a **consistent global order** across all threads (e.g. always lock the object with the lower `hashCode()`/ID first), or use `tryLock()` with a timeout instead of blocking indefinitely.

```java
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadLocalRandom;

public class TryLockDeadlockAvoidance {

    static final ReentrantLock lockA = new ReentrantLock();
    static final ReentrantLock lockB = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> doWork("Thread 1", lockA, lockB), "Thread-1");
        Thread t2 = new Thread(() -> doWork("Thread 2", lockB, lockA), "Thread-2");

        t1.start();
        t2.start();
    }

    // Tries to acquire 'first' then 'second'. Retries with backoff if it can't get both.
    static void doWork(String name, ReentrantLock first, ReentrantLock second) {
        int attempts = 0;

        while (true) {
            attempts++;
            boolean gotFirst = false;
            boolean gotSecond = false;

            try {
                gotFirst = first.tryLock(100, TimeUnit.MILLISECONDS);
                if (gotFirst) {
                    // simulate work between acquiring the two locks
                    sleep(50);

                    gotSecond = second.tryLock(100, TimeUnit.MILLISECONDS);
                    if (gotSecond) {
                        System.out.println(name + " acquired both locks on attempt " + attempts);
                        sleep(50); // simulate doing the actual work
                        return; // success — done
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                if (gotSecond) second.unlock();
                if (gotFirst) first.unlock();
            }

            // Didn't get both locks — back off before retrying to avoid livelock
            System.out.println(name + " failed attempt " + attempts + ", retrying...");
            sleep(ThreadLocalRandom.current().nextInt(10, 50));
        }
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
```

---

## 12. `ReentrantLock` vs `synchronized`

`ReentrantLock` (from `java.util.concurrent.locks`) offers more flexibility than `synchronized`:

```java
class Counter {
    private final ReentrantLock lock = new ReentrantLock();
    private int count = 0;

    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock(); // MUST be in finally, or a lock leaks on exception
        }
    }
}
```

Advantages over `synchronized`: `tryLock()` (non-blocking attempt), `lockInterruptibly()` (can be interrupted while waiting), fairness policies (`new ReentrantLock(true)` grants the lock to the longest-waiting thread), and `Condition` objects (a more flexible version of `wait`/`notify`). The trade-off is you must remember to unlock manually — `synchronized` releases automatically even if an exception is thrown.

---

## Summary — Picking the Right Tool

| Need | Tool |
|---|---|
| Simple flag/reference visibility across threads | `volatile` |
| Atomic compound update (increment, CAS) | `AtomicInteger` / `AtomicLong` / etc. |
| Exclusive access to a critical section | `synchronized` or `ReentrantLock` |
| Producer-consumer without hand-rolled `wait`/`notify` | `BlockingQueue` |
| Thread-safe map/list under concurrent access | `ConcurrentHashMap` / `CopyOnWriteArrayList` |
| Run many short tasks without manual `Thread` management | `ExecutorService` |
| Get a result back from an async task | `Callable` + `Future`, or `CompletableFuture` for chaining |
| Wait for N threads to finish once | `CountDownLatch` |
| Limit concurrent access to a resource | `Semaphore` |
| Sync N threads at a repeated checkpoint | `CyclicBarrier` |
