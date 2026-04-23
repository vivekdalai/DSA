# Classes and Objects

### 1. What is a Class?

**A class is a blueprint, template, or recipe for creating objects. It defines what an object will contain (its data)
and what it will be able to do (its behavior).**

### 2. What is an Object?

**An object is an instance of a class. It's the actual thing you can interact with, store data in, and invoke methods
on.**

# Enums

### 1. What is an Enum?

**An enum (short for enumeration) is a special data type that defines a fixed set of named constants. Unlike strings or
integers, enums are type-safe, meaning the compiler ensures you can only use values that actually exist in your defined
set.**

**If a value can only be one of a predefined set of options, consider using an enum.**

# Interface

### 1. What is an Interface?

**At its core, an interface is a contract: a list of methods that any implementing class must provide. It specifies a
set of behaviors that a class agrees to implement but leaves the details of those behaviors up to each implementation.**

In other words:

**_An interface defines the "what", while classes provide the "how".**

### 2. Key Properties

Here are their most important characteristics:

1. Defines Behavior Without Dictating Implementation
2. Enables Polymorphism
3. Promotes Decoupling

This makes your code easier to:
Extend (add new implementations without modifying existing ones),
Test (mock interfaces in unit tests),
Maintain (fewer ripple effects from code changes).

Example: Payment Service, Notification service

# Encapsulation

**Encapsulation is one of the four foundational principles of object-oriented design. It is the practice of grouping
data (variables) and behavior (methods) that operate on that data into a single unit (typically a class) and restricting
direct access to the internal details of that class.**

In simple terms:

**_Encapsulation = Data hiding + Controlled access**

### 1. Why Encapsulation Matters

1. Data Hiding
   Sensitive data (like a bank balance or password) should not be exposed directly. Encapsulation keeps this data
   private and accessible only through controlled methods.

2. Controlled Access and Validation
   It ensures that data can only be modified in controlled, predictable ways. \
   For example, you can prevent invalid deposits or withdrawals by validating input inside methods.

3. Improved Maintainability
   Because internal details are hidden, you can change the implementation (e.g., how data is stored or validated)
   without affecting the code that depends on it.

4. Security and Stability
   By preventing external tampering, encapsulation reduces the risk of inconsistent or invalid system states.

# Abstraction

**Abstraction is the process of hiding complex internal implementation details and exposing only the relevant,
high-level functionality to the outside world. It allows developers to focus on what an object does, rather than how it
does it.**

In short:

**Abstraction = Hiding Complexity + Showing Essentials**
By separating the what from the how, abstraction:
Reduces cognitive load
Improves modularity
Leads to cleaner, more intuitive APIs

**“Abstraction is about creating a simplified view of a system that highlights the essential features while suppressing
the irrelevant details.”**

**Real world examples:**

You turn the steering wheel, press the accelerator, and shift the gears.

But you don’t need to know:

How the transmission works
How the fuel is injected
How torque or combustion is calculated

### 1. Why Abstraction Matters

1. Swap Implementations Without Changing Callers
2. Reduce Complexity for Consumers
3. Extend Without Modifying
4. Share Common Logic Once

### 2. How Abstraction Is Achieved

1. Abstract Classes
2. Interfaces as Abstraction
3. Public APIs as Abstraction

### 3. Abstraction vs Encapsulation

| Aspect  | Encapsulation                        | Abstraction                                                          |
|---------|--------------------------------------|----------------------------------------------------------------------|
| Focus   | Protecting data within a class       | Hiding implementation complexity                                     |
| Goal    | Restrict access to internal state    | Simplify usage and expose only essentials                            |
| Level   | Implementation-level                 | Design-level                                                         |
| Example | Private balance field in BankAccount | Exposing only deposit() and withdraw() without showing how they work |

# Inheritance

**Inheritance allows one class (called the subclass or child class) to inherit the properties and behaviors of another
class (called the superclass or parent class).**

In simpler terms:

Inheritance enables code reuse by letting you define common logic once in a base class and then extend or specialize it
in multiple derived classes.
This leads to cleaner, modular, and more maintainable software.

### 1. Why Inheritance Matters

1. Code Reusability
   It embodies the DRY (Don't Repeat Yourself) principle. Common logic is written once in the parent class and shared
   across all subclasses reducing redundancy.

2. Logical Hierarchy
   It creates a clear and intuitive hierarchy that model real-world “is-a” relationships like ElectricCar is a Car or
   Admin is a User.

3. Ease of Maintenance
   If a bug is found or a change is needed in the shared logic, you only need to fix it in one place, the superclass.
   All subclasses automatically inherit the fix.

4. Polymorphism
   Inheritance is a prerequisite for polymorphism, allowing objects of different subclasses to be treated as objects of
   the superclass.

### 2. Types of Inheritance

1. Single Inheritance is the simplest form: one child class extends one parent class
2. Multi-level Inheritance is when a child class itself becomes a parent
3. Hierarchical Inheritance is when multiple child classes extend the same parent.
4. Multiple Inheritance is when a child class extends more than one parent.

**Only C++ and Python support multiple inheritance directly. Java, C#, and TypeScript do not. The reason? The diamond
problem.**

### 3. When to Use Inheritance

There is a clear "is-a" relationship (e.g., Dog is an Animal, Car is a Vehicle).
The parent class defines common behavior or data that children should share.
The child class does not violate the behavior expected from the parent.

### 4. Avoid Inheritance when

The relationship is "has-a" or "uses-a" rather than "is-a". A Car has an Engine, it is not an Engine. A Printer uses a
Logger, it is not a Logger.

You want to combine behaviors from multiple sources dynamically.
You need runtime flexibility to swap behaviors.

You want to avoid tight coupling between child and parent internals.

# Polymorphism

Polymorphism lets you call the same method on different objects, and have each object respond in its own way.

### Why Polymorphism Matters

Here are four concrete benefits that polymorphism provides.

Encourages loose coupling: You interact with abstractions (interfaces or base classes), not specific implementations.
Enhances flexibility: You can introduce new behaviors without modifying existing code, supporting the Open/Closed
Principle.
Promotes scalability: Systems can grow to support more features with minimal impact on existing code.
Enables extensibility: You can “plug in” new implementations without touching the core business logic.

### How Polymorphism Works

1. Compile-time Polymorphism (Method Overloading)
2. Runtime Polymorphism (Method Overriding / Dynamic Dispatch)

### Polymorphism with Interfaces vs Abstract Classes

| Aspect          | Interface                                      | Abstract Class                            |
|-----------------|------------------------------------------------|-------------------------------------------|
| Relationship    | "can do" (capability)                          | "is a" (family)                           |
| Shared behavior | None (contract only)                           | Yes (concrete methods + fields)           |
| Multiple        | A class can implement many                     | A class can extend only one               |
| When to use     | Unrelated classes share a capability           | Related classes share logic               |
| Example         | Sendable implemented by Email, Invoice, Report | Notification extended by Email, SMS, Push |





























