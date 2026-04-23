# Exercise 1: Thread-Safe Counter
Implement Singleton Counter Class
Problem: Implement a Counter singleton that tracks a count across the application. Multiple components should be able to increment the counter, and all must see the same value.

## Using ENUM

```java
import java.util.concurrent.atomic.AtomicInteger;

enum Counter {
    // TODO: Implement as singleton
    // Hint: Use enum or Bill Pugh pattern
    INSTANCE;

    private final AtomicInteger count = new AtomicInteger(0);

    public static Counter getInstance() {
        return INSTANCE;
    }

    public void increment() {
        // TODO: Make thread-safe
        count.incrementAndGet();
    }

    public int getCount() {
        // TODO: Return current count
        return count.get();
    }
}


public class Main {
    public static void main(String[] args) {
        // After implementing, usage should look like:
         Counter c1 = Counter.getInstance();
         Counter c2 = Counter.getInstance();
         System.out.println("Same instance: " + (c1 == c2));
         for (int i = 0; i < 5; i++) {
             c1.increment();
         }
         System.out.println("Count after 5 increments: " + c1.getCount());
    }
}
```

## Using Bill Pugh pattern

```java
class Counter {
    // TODO: Implement as singleton
    // Hint: Use enum or Bill Pugh pattern

    private int count = 0;

    private Counter() {}

    public static class Holder {
        private static final Counter INSTANCE = new Counter();
    }

    public static Counter getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void increment() {
        // TODO: Make thread-safe
        count++;
    }

    public synchronized int getCount() {
        // TODO: Return current count
        return count;
    }
}
```

- If we don't want to use synchronized then we can use AtomicInteger like above in enum example

# Exercise 2: Logger with Log Levels

Implement Logger Class
Problem: Implement a singleton Logger that supports log levels. Only messages at or above the configured minimum level should be written.

 ## Using Lazy Loading

```java
class Logger {
    // TODO: Implement as singleton
    private static Logger instance;
    private Logger(){}

    public enum Level { DEBUG, INFO, WARN, ERROR }

    private Level minLevel = Level.INFO;

    public void setLevel(Level level) {
        // TODO
        this.minLevel = level;
    }

    private void log(Level level, String message) {
        // TODO: Only log if level >= minLevel
        if (level.compareTo(minLevel) >= 0) {
            System.out.println("[" + level + "] " + message);
        }
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
            return instance;
        }
        return instance;
    }

    public void debug(String msg) { log(Level.DEBUG, msg); }
    public void info(String msg)  { log(Level.INFO, msg); }
    public void warn(String msg)  { log(Level.WARN, msg); }
    public void error(String msg) { log(Level.ERROR, msg); }
}

public class Main {
    public static void main(String[] args) {
        // After implementing, usage should look like:
         Logger l1 = Logger.getInstance();
         Logger l2 = Logger.getInstance();
         System.out.println("Same instance: " + (l1 == l2));
         l1.setLevel(Logger.Level.WARN);
         l1.debug("Starting up");
         l1.info("Server listening on port 8080");
         l1.warn("Connection pool running low");
         l1.error("Failed to connect to database");
    }
}
```

# Using ENUM

```java
enum Logger {
    // TODO: Implement as singleton
    INSTANCE;
    public static  Logger getInstance() {
        return INSTANCE;
    }

    public enum Level { DEBUG, INFO, WARN, ERROR }

    private Level minLevel = Level.INFO;

    public void setLevel(Level level) {
        // TODO
        this.minLevel = level;
    }

    private void log(Level level, String message) {
        // TODO: Only log if level >= minLevel
        if (level.compareTo(minLevel) >= 0) {
            System.out.println("[" + level + "] " + message);
        }
    }



    public void debug(String msg) { log(Level.DEBUG, msg); }
    public void info(String msg)  { log(Level.INFO, msg); }
    public void warn(String msg)  { log(Level.WARN, msg); }
    public void error(String msg) { log(Level.ERROR, msg); }
}

public class Main {
    public static void main(String[] args) {
        // After implementing, usage should look like:
         Logger l1 = Logger.getInstance();
         Logger l2 = Logger.getInstance();
         System.out.println("Same instance: " + (l1 == l2));
         l1.setLevel(Logger.Level.WARN);
         l1.debug("Starting up");
         l1.info("Server listening on port 8080");
         l1.warn("Connection pool running low");
         l1.error("Failed to connect to database");
    }
}
```


































