# Behavioral Design Patterns

Behavioral patterns focus on **how objects communicate and share responsibilities**.

They help when:
- behavior changes dynamically
- request flow must be controlled
- communication logic should be flexible

Examples:
- Strategy
- Observer
- Command
- State
- Iterator

---

## 1. Strategy Pattern

Strategy pattern allows you to define a family of algorithms and switch between them at runtime.

### Example
Suppose a payment system supports:
- Credit Card
- UPI
- PayPal

Instead of writing one huge `if-else` block, define a common interface.

```java
interface PaymentStrategy {
    void pay(int amount);
}

class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid using Credit Card: " + amount);
    }
}

class UpiPayment implements PaymentStrategy {
    public void pay(int amount) {
        System.out.println("Paid using UPI: " + amount);
    }
}

class PaymentService {
    private PaymentStrategy strategy;

    public PaymentService(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void processPayment(int amount) {
        strategy.pay(amount);
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        PaymentService service = new PaymentService(new UpiPayment());
        service.processPayment(500);
    }
}
```

### Thinking process
Use Strategy when:
- multiple ways exist to perform the same task
- you want to avoid large `if-else` or `switch` blocks
- behavior should be replaceable at runtime

### Intuition
The main object delegates work to a strategy object.  
If the behavior changes later, you replace the strategy, not the whole class.

---

## 2. Observer Pattern

Observer defines a **one-to-many dependency** between objects.  
When one object changes state, all dependent objects are notified.

### Real-life example
You subscribe to a YouTube channel.  
When the creator uploads a video, all subscribers get notified.

### Small example

```java
import java.util.ArrayList;
import java.util.List;

interface Observer {
    void update(String message);
}

class User implements Observer {
    private String name;

    public User(String name) {
        this.name = name;
    }

    public void update(String message) {
        System.out.println(name + " received: " + message);
    }
}

class Channel {
    private List<Observer> subscribers = new ArrayList<>();

    public void subscribe(Observer observer) {
        subscribers.add(observer);
    }

    public void notifyAllUsers(String message) {
        for (Observer observer : subscribers) {
            observer.update(message);
        }
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        Channel channel = new Channel();

        channel.subscribe(new User("Vivek"));
        channel.subscribe(new User("Rahul"));

        channel.notifyAllUsers("New video uploaded!");
    }
}
```

### Thinking process
Use Observer when:
- one object’s state change should automatically notify many others
- sender should not be tightly coupled to receivers

### Intuition
One publisher broadcasts an event, and many subscribers react to it independently.

---

## 3. Command Pattern

Command pattern wraps a request inside an object.

### Why use it?
Instead of directly calling methods everywhere, you package the action into a command object.

### Example intuition
Imagine a remote control:
- button does not know TV internals
- it simply triggers a command like `TurnOnCommand`

### Thinking process
Use Command when:
- requests should be parameterized as objects
- you want undo/redo behavior
- sender and receiver should be decoupled

---

## 4. State Pattern

State pattern allows an object to change its behavior when its internal state changes.

### Example intuition
Think about a vending machine:
- idle state
- money inserted state
- item dispensed state

The same action can behave differently depending on the current state.

### Thinking process
Use State when:
- behavior changes based on internal condition
- big conditional logic is becoming hard to manage

---

## 5. Iterator Pattern

Iterator provides a way to access elements of a collection sequentially without exposing its internal structure.

### Example intuition
When you loop through a list, you do not need to know how the list is internally implemented.

### Thinking process
Use Iterator when:
- collection traversal should be standardized
- internal representation of the collection should stay hidden

---

# Quick summary

| Pattern | Main purpose | When to use |
|---|---|---|
| Strategy | Switch behavior dynamically | Multiple algorithms exist |
| Observer | Notify many dependents | One-to-many updates needed |
| Command | Wrap requests as objects | Undo/redo, decoupled actions |
| State | Change behavior by state | State-driven logic |
| Iterator | Traverse collections uniformly | Hide collection internals |

---

# Final intuition

Behavioral patterns are about **how responsibility and communication flow through the system**.

Ask these questions:
1. Does behavior change depending on situation?
2. Does one object need to notify many others?
3. Should requests be represented as separate objects?
4. Is a large conditional block controlling behavior?
5. Should traversal be independent of data structure details?

These questions usually point toward a behavioral pattern.
