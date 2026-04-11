# Structural Design Patterns

Structural patterns focus on **how classes and objects are combined** to build larger systems.

They help answer questions like:
- how do we connect objects cleanly?
- how do we simplify a complex system?
- how do we make incompatible interfaces work together?

Examples:
- Adapter
- Decorator
- Facade
- Composite
- Proxy

---

## 1. Adapter Pattern

Adapter allows two incompatible interfaces to work together.

### Real-life analogy
A mobile charger plug may not fit directly into a different socket, so we use an adapter.

### Payment example
Suppose your application expects every payment system to follow this interface:

```java
interface PaymentProcessor {
    void pay(int amount);
}
```

But a third-party payment gateway already exists and uses a different method name:

```java
class ThirdPartyGateway {
    public void makePayment(int value) {
        System.out.println("Payment done using third-party gateway: " + value);
    }
}
```

Now your system cannot directly use `ThirdPartyGateway` because it does not match the expected interface.

So we create an adapter:

```java
interface PaymentProcessor {
    void pay(int amount);
}

class ThirdPartyGateway {
    public void makePayment(int value) {
        System.out.println("Payment done using third-party gateway: " + value);
    }
}

class PaymentAdapter implements PaymentProcessor {
    private ThirdPartyGateway gateway = new ThirdPartyGateway();

    public void pay(int amount) {
        gateway.makePayment(amount);
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        PaymentProcessor paymentProcessor = new PaymentAdapter();
        paymentProcessor.pay(500);
    }
}
```

### Thinking process
Use Adapter when:
- an existing class is useful
- but its interface does not match what your current code expects
- you do not want to modify third-party or legacy code

### Intuition
Instead of changing working external code, you place a wrapper in between so your application can talk to it using the format it already understands.

---

## 2. Decorator Pattern

Decorator adds new behavior to an object **without changing its original class**.

### Problem
Suppose you have a simple coffee object, and you want to add:
- milk
- sugar
- cream

If you use inheritance only, you may end up creating too many subclasses such as:
- MilkCoffee
- SugarCoffee
- MilkSugarCoffee
- CreamMilkCoffee

This becomes hard to maintain.

### Thinking process
Use Decorator when:
- you want flexible feature combinations
- subclass explosion is becoming a problem
- behavior should be added dynamically

### Small intuition
Decorator wraps an object and adds extra behavior before or after calling the original object.

Example idea:
- base coffee gives base cost
- milk decorator adds milk cost
- sugar decorator adds sugar cost

That way, behavior is layered instead of hardcoded into many subclasses.

---

## 3. Facade Pattern

Facade provides a **simple interface** over a complex subsystem.

### Real-life analogy
When you press a “Start Computer” button, internally many things happen:
- CPU starts
- memory checks happen
- OS loads

But the user sees only one simple action.

### Thinking process
Use Facade when:
- a subsystem is too complex
- clients need a simpler entry point
- you want to hide internal workflow details

### Small intuition
A facade does not remove the complexity inside the system.  
It simply gives the client a clean and easy way to use that system.

---

## 4. Composite Pattern

Composite lets you treat **individual objects and groups of objects in the same way**.

### Real-life analogy
A file system has:
- files
- folders

A folder can contain files and other folders.  
But both can be treated as items in the file system.

### Thinking process
Use Composite when:
- your data forms a tree structure
- you want leaf objects and container objects to be handled uniformly

### Intuition
If both single objects and grouped objects share a common interface, client code becomes simpler.

---

## 5. Proxy Pattern

Proxy provides a placeholder or representative for another object.

### Common use cases
- access control
- lazy loading
- logging
- remote object access

### Thinking process
Use Proxy when:
- direct access to an object should be controlled
- object creation is expensive and should be delayed
- extra behavior should happen before forwarding the request

### Intuition
A proxy stands in front of the real object and decides how and when the real object should be used.

---

# Quick summary

| Pattern | Main purpose | When to use |
|---|---|---|
| Adapter | Make incompatible interfaces work together | Reuse old/external code |
| Decorator | Add behavior dynamically | Avoid too many subclasses |
| Facade | Simplify a complex subsystem | Provide clean entry point |
| Composite | Treat single objects and groups uniformly | Tree-like structures |
| Proxy | Control access to another object | Lazy loading / access control |

---

# Final intuition

Structural patterns are mostly about **how to arrange pieces of code together**.

Ask these questions:
1. Are two parts unable to communicate because interfaces differ?
2. Is the system too complex for the client?
3. Are too many subclasses being created just for feature combinations?
4. Do I need a wrapper to control access to something?

Those questions usually point toward a structural pattern.
