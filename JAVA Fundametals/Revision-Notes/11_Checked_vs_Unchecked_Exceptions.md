# Checked vs Unchecked Exceptions

## Checked Exceptions

- Represent recoverable conditions the caller can reasonably be expected to handle (e.g. a file might not exist, a network call might time out).
- The compiler enforces handling — a method that can throw one must either catch it or declare it via `throws`.
- Extend `Exception` (but not `RuntimeException`).

```java
public class FileReaderService {
    public String readConfig(String path) throws IOException {
        return Files.readString(Path.of(path)); // IOException is checked
    }
}

public class App {
    public void start() {
        try {
            String config = new FileReaderService().readConfig("app.conf");
        } catch (IOException e) {
            // caller has a realistic local recovery: fall back to defaults
            useDefaultConfig();
        }
    }
}
```

Here the caller genuinely can do something useful with the failure — retry, use a default, prompt the user for a different path. That's what makes a checked exception a good fit.

## Unchecked Exceptions

- Represent programming errors or conditions that shouldn't normally be caught deep in the call stack — null references, invalid arguments, illegal state.
- Extend `RuntimeException`. The compiler does not force handling.

```java
public class OrderService {
    public void placeOrder(Order order) {
        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be positive"); // unchecked
        }
        // ...
    }
}
```

Nobody expects (or wants) every caller of `placeOrder` to be forced to wrap it in a `try/catch` — this is a bug in the caller's input, not a recoverable runtime condition. It should typically propagate up to a top-level handler (e.g. a global `@ExceptionHandler` in a Spring controller) rather than be caught locally.

## Design-Taste Follow-Up: Should a Custom Business Exception Be Checked or Unchecked?

This is a common interview probe. In most modern services — especially Spring-based ones — **unchecked is preferred**.

### Why checked exceptions fall out of favor

**1. They pollute method signatures and propagate through every layer.**

```java
// Checked version — every layer up the call stack must declare or catch it
public interface OrderRepository {
    Order findById(Long id) throws OrderNotFoundException;
}

public interface OrderService {
    Order getOrder(Long id) throws OrderNotFoundException; // forced to redeclare
}

public interface OrderController {
    Order handleGetOrder(Long id) throws OrderNotFoundException; // forced again
}
```

Every interface and every implementation up the chain has to keep repeating `throws OrderNotFoundException`, even if none of those layers actually do anything with it besides passing it along.

**2. They don't compose with lambdas and streams.**

```java
// This won't compile if OrderNotFoundException is checked —
// functional interfaces like Function<T, R> don't declare checked exceptions
List<Order> orders = ids.stream()
    .map(id -> orderRepository.findById(id)) // compile error: unhandled checked exception
    .collect(Collectors.toList());
```

To make this compile, you'd be forced into an awkward wrapper:

```java
List<Order> orders = ids.stream()
    .map(id -> {
        try {
            return orderRepository.findById(id);
        } catch (OrderNotFoundException e) {
            throw new RuntimeException(e); // rethrow as unchecked just to satisfy the compiler
        }
    })
    .collect(Collectors.toList());
```

That extra boilerplate is a sign the checked exception was the wrong tool — you're immediately converting it to unchecked anyway.

### The preferred modern approach

Make the business exception unchecked, and let a centralized handler deal with it:

```java
public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Order not found: " + id);
    }
}

public interface OrderRepository {
    Order findById(Long id); // no throws clause needed
}

// Composes cleanly with streams
List<Order> orders = ids.stream()
    .map(orderRepository::findById)
    .collect(Collectors.toList());

// Handled centrally, not by every caller
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleNotFound(OrderNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
```

No signature pollution, no stream workarounds, and the failure still gets handled — just in one place instead of being threaded through every method signature in the call chain.

### When checked still makes sense

Use checked only when the caller has a realistic, local recovery action right at the call site — not just a generic "log and rethrow." Classic examples: `IOException` (retry, fall back to a default, ask the user for another path), `SQLException` in low-level JDBC code before it's wrapped by a framework. If the only thing every caller does is catch-and-rethrow-as-unchecked or catch-and-log, that's a strong signal it should have been unchecked from the start.

## Quick Rule of Thumb

| | Checked | Unchecked |
|---|---|---|
| Extends | `Exception` | `RuntimeException` |
| Compiler enforcement | Yes (`throws` required) | No |
| Best for | Recoverable conditions with a real local fix | Programming errors, business rule violations meant to bubble up |
| Streams/lambdas | Awkward, needs wrapping | Composes cleanly |
| Modern service design (e.g. Spring) | Rare, low-level only (I/O, JDBC) | Preferred for business/domain exceptions |
