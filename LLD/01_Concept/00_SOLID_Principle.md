# SOLID Design Principles

SOLID is a set of 5 object-oriented design principles that help us write code that is:

- easier to understand
- easier to maintain
- easier to test
- easier to extend
- less tightly coupled

SOLID stands for:

1. **S** - Single Responsibility Principle (SRP)
2. **O** - Open/Closed Principle (OCP)
3. **L** - Liskov Substitution Principle (LSP)
4. **I** - Interface Segregation Principle (ISP)
5. **D** - Dependency Inversion Principle (DIP)

---

## Why use SOLID?

Using SOLID principles gives the following benefits:

- reduces code duplication
- improves readability
- improves maintainability
- makes testing easier
- supports flexibility and change
- reduces unnecessary coupling
- helps build scalable low-level design

---

## 1. Single Responsibility Principle (SRP)

### Definition
A class should have **only one reason to change**.

In simple words, a class should have **one clear responsibility**.

### Wrong thinking
If one class is doing multiple jobs like:
- business logic
- database save
- printing/logging
- email sending

then that class has multiple reasons to change.

### Good thinking
Follow **logical grouping of responsibilities**.
Each class should focus on one job.

### Benefits
- better maintainability
- easier testing
- cleaner code
- better reusability

### Bad example

```java
class Invoice {
    public void calculateTotal() {
        System.out.println("Calculating invoice total");
    }

    public void saveToDB() {
        System.out.println("Saving invoice to database");
    }

    public void printInvoice() {
        System.out.println("Printing invoice");
    }
}
```

Problem: `Invoice` is handling calculation, persistence, and printing.

### Better example

```java
class Invoice {
    public void calculateTotal() {
        System.out.println("Calculating invoice total");
    }
}

class InvoiceRepository {
    public void save(Invoice invoice) {
        System.out.println("Saving invoice to database");
    }
}

class InvoicePrinter {
    public void print(Invoice invoice) {
        System.out.println("Printing invoice");
    }
}
```

Now:
- `Invoice` handles invoice logic
- `InvoiceRepository` handles storage
- `InvoicePrinter` handles printing

---

## 2. Open/Closed Principle (OCP)

### Definition
Software entities should be **open for extension but closed for modification**.

This means:
- we should be able to add new behavior
- without changing already tested existing code too much

This is commonly achieved using:
- interfaces
- abstract classes
- polymorphism

### Benefits
- reduces risk of breaking old code
- improves extensibility
- supports polymorphism
- improves testability

### Bad example

```java
class PaymentService {
    public void pay(String paymentType) {
        if (paymentType.equals("CREDIT_CARD")) {
            System.out.println("Paying using credit card");
        } else if (paymentType.equals("UPI")) {
            System.out.println("Paying using UPI");
        }
    }
}
```

Problem: every time a new payment type is added, we must modify `PaymentService`.

### Better example

```java
interface PaymentMethod {
    void pay();
}

class CreditCardPayment implements PaymentMethod {
    public void pay() {
        System.out.println("Paying using credit card");
    }
}

class UpiPayment implements PaymentMethod {
    public void pay() {
        System.out.println("Paying using UPI");
    }
}

class PaymentService {
    public void processPayment(PaymentMethod paymentMethod) {
        paymentMethod.pay();
    }
}
```

If tomorrow we add `WalletPayment`, we create a new class instead of modifying existing logic.

---

## 3. Liskov Substitution Principle (LSP)

### Definition
Objects of a superclass should be replaceable with objects of its subclass **without breaking the correctness of the program**.

In simple words:
- child class should behave like a proper replacement of parent class
- subclass should not reduce or violate expected behavior

The subclass should **extend capability**, not break existing contract.

### Bad example

```java
class Bird {
    public void fly() {
        System.out.println("Bird is flying");
    }
}

class Ostrich extends Bird {
    @Override
    public void fly() {
        throw new UnsupportedOperationException("Ostrich cannot fly");
    }
}
```

Problem: `Ostrich` is a `Bird`, but it cannot behave like a normal `Bird` where `fly()` is expected.

### Better example

```java
interface Bird {
    void eat();
}

interface FlyingBird extends Bird {
    void fly();
}

class Sparrow implements FlyingBird {
    public void eat() {
        System.out.println("Sparrow is eating");
    }

    public void fly() {
        System.out.println("Sparrow is flying");
    }
}

class Ostrich implements Bird {
    public void eat() {
        System.out.println("Ostrich is eating");
    }
}
```

Now the design models behavior correctly.

### Focus
When using inheritance, always ask:

"Can this child class be used everywhere the parent is expected without surprising behavior?"

If the answer is no, inheritance may be wrong.

---

## 4. Interface Segregation Principle (ISP)

### Definition
Clients should not be forced to depend on methods they do not use.

In simple words:
- do not create large fat interfaces
- create small, specific interfaces
- a class should implement only the behaviors it actually supports

### Bad example

```java
interface Worker {
    void work();
    void eat();
}

class RobotWorker implements Worker {
    public void work() {
        System.out.println("Robot is working");
    }

    public void eat() {
        throw new UnsupportedOperationException("Robot does not eat");
    }
}
```

Problem: `RobotWorker` is forced to implement an unnecessary method.

### Better example

```java
interface Workable {
    void work();
}

interface Eatable {
    void eat();
}

class HumanWorker implements Workable, Eatable {
    public void work() {
        System.out.println("Human is working");
    }

    public void eat() {
        System.out.println("Human is eating");
    }
}

class RobotWorker implements Workable {
    public void work() {
        System.out.println("Robot is working");
    }
}
```

Now each class implements only what it really needs.

### Focus
If a class is not able to provide meaningful implementation of a method, that method probably belongs in a separate interface.

---

## 5. Dependency Inversion Principle (DIP)

### Definition
High-level modules should not depend directly on low-level modules. Both should depend on abstractions.

Also:
- abstractions should not depend on details
- details should depend on abstractions

In simple words:
write code against **interfaces**, not concrete implementations.

### Bad example

```java
class WiredKeyboard {
    public void type() {
        System.out.println("Typing with wired keyboard");
    }
}

class Computer {
    private WiredKeyboard keyboard = new WiredKeyboard();

    public void startTyping() {
        keyboard.type();
    }
}
```

Problem: `Computer` is tightly coupled to one concrete keyboard type.

### Better example

```java
interface Keyboard {
    void type();
}

class WiredKeyboard implements Keyboard {
    public void type() {
        System.out.println("Typing with wired keyboard");
    }
}

class BluetoothKeyboard implements Keyboard {
    public void type() {
        System.out.println("Typing with bluetooth keyboard");
    }
}

class Computer {
    private final Keyboard keyboard;

    public Computer(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void startTyping() {
        keyboard.type();
    }
}
```

### Usage

```java
public class Main {
    public static void main(String[] args) {
        Keyboard keyboard = new BluetoothKeyboard();
        Computer computer = new Computer(keyboard);
        computer.startTyping();
    }
}
```

Now `Computer` depends on the abstraction `Keyboard`, not a concrete class.

---

## Quick Summary

| Principle | Core idea |
|---|---|
| SRP | One class should have one responsibility |
| OCP | Extend behavior without modifying stable existing code |
| LSP | Child classes should properly replace parent classes |
| ISP | Do not force classes to implement unused methods |
| DIP | Depend on abstractions, not concrete implementations |

---

## Final Note

SOLID principles are not about making code complicated. They are about making code more maintainable as the project grows.

While designing classes, always ask:

- is this class doing too much?
- can I extend this without modifying old code?
- is inheritance being used correctly?
- is this interface too large?
- am I depending on abstraction or concrete implementation?

If you keep these questions in mind, your low-level design becomes much cleaner.