# DRY Principle

Do not Repeat Yourself

Every piece of knowledge must have a single, unambiguous, authoritative representation within a system.

The DRY principle says that each piece of knowledge in your system should live in exactly one place.

keep in mind: the Rule of Three.

### Why Repetition Is a Problem

1. Harder to Maintain
2. Higher Risk of Bugs
3. Bloated Codebase
4. Poor Test Coverage

# KISS Principle

Keep It Simple, Stupid.

In software, KISS means writing code that is:

Easy to read. Another developer can understand what the code does without spending 30 minutes tracing through
abstractions.
Easy to understand. The logic flows naturally. There are no surprises, no hidden side effects, no clever tricks.
Easy to change. When requirements shift, you can modify the code confidently without worrying about breaking something
three layers deep.

# YAGNI Principle

You Aren’t Gonna Need It.

“Always implement things when you actually need them, never when you just foresee that you need them.” — Ron Jeffries,
co-founder of Extreme Programming

YAGNI is a principle that encourages you to resist the temptation to build features or add flexibility until you are
absolutely sure you need them.

# Law of Demeter

```java
Money price = customer.getShoppingCart().getItems().get(0).getProduct().getPrice();
```

Look at that chain. OrderService is coupled to six classes deep. It knows about the customer's cart, the cart's internal
list, the structure of a cart item, the product inside it, and the price object. A change to any of these classes could
break the OrderService, even though those classes have nothing to do with order processing.

This is called a "train wreck" or "dot-chaining": one object reaching through several others to get what it wants. You
start with a Customer, go into their ShoppingCart, peek into its internal list of CartItems, grab the first one, extract
the Product, and finally get the Price.

```java
void displayFirstItemPrice(Customer customer) {
    Money price = customer.getShoppingCart()
            .getItems()
            .get(0)
            .getProduct()
            .getPrice();
    System.out.println("Price of the first item: " + price.getAmount());
}
```

### What's wrong in this?

- High Coupling
- Encapsulation Violation
- Maintenance
- Testability Issues

**RULE: Only talk to your immediate friends.**

### Common doubts regarding LoD

**Isn't this just more code? I have to write all these wrapper methods!**
Yes, following LoD can result in additional small, delegating methods. But this “extra” code serves a critical purpose:
it reduces coupling.

**Does the Law of Demeter mean I can’t use getters at all?**
Not at all. LoD doesn’t forbid getters.

Simple property access like customer.getName() is perfectly fine—name is a direct part of Customer.

The issue arises with chained getters across object boundaries:

```java
customer.getCart().getItems().get(0).getProduct().getPrice();
```

# Separation of Concerns

Separation of concerns is the process of organizing a software system so that each part addresses a distinct concern.

A concern is a piece of information or a set of behaviors that affects the system. It’s a particular aspect of the
system’s functionality or requirements.

Concerns can be broad or granular:

1. User Interface (UI) Logic: How information is displayed to the user and how user input is captured.
2. Business Logic: The core rules and processes that define the application's purpose (e.g., calculating interest,
   validating an order, booking a flight).
3. Data Access: The mechanics of retrieving data from and saving data to a database, file, or API.
4. Authentication: Verifying a user's identity.
5. Logging: Recording events and errors for debugging and monitoring.
6. Caching: Storing data temporarily to improve performance.

The core idea of SoC is simple:

Each part of your system should do one thing and do it well.

This naturally leads to two related qualities that we will explore in depth in the next chapter:

1. High Cohesion: All the elements within a single module are closely related and work together toward a single,
   well-defined purpose. A highly cohesive module is a specialist. A DatabaseConnection class manages connections. It
   should not also format user-facing error messages.
2. Low Coupling: Different modules are independent of each other. A change in one module has little to no impact on
   another. Low coupling is like building with LEGO bricks: you can swap out a red brick for a blue one without
   rebuilding the entire structure. High coupling is like a model glued together, where changing one part requires
   breaking everything around it.

# Coupling and Cohesion

## Cohesion
- Cohesion measures how closely related and focused the responsibilities of a single module (like a class or a package) are. 
- It is an internal quality of a module.

- High cohesion is the goal 
  - A highly cohesive module does one thing and does it exceptionally well. 
- Low cohesion is a major code smell 
  - A module with low cohesion is a generalist "junk drawer" or a "God Class."
  - It does many unrelated things.


## Coupling
- Coupling measures the degree to which one module depends on the inner workings of another module. 
- It is an external quality that describes the relationship between modules.

- Low coupling is the goal 
  - In a low-coupled system, modules interact through stable, simple, and well-defined interfaces. 
  - They don't need to know about each other's implementation details.
- High coupling is a recipe for disaster 
  - When modules are tightly coupled, a change in one module often forces a cascade of changes in other modules. 
  - The system becomes rigid, fragile, and difficult to maintain.


- Goal --> High Cohesion and Low Coupling



# Composing Objects Principle

- Favour Composition over Inheritance

## Inheritance vs Composition

| Aspect | Inheritance | Composition |
|--------|------------|-------------|
| Relationship | "is-a" (Car is a Vehicle) | "has-a" (Car has an Engine) |
| Coupling | Tight — subclass depends on parent implementation | Loose — depends on abstraction (interface) |
| Flexibility | Fixed at compile time | Can change at runtime |
| Code Reuse | Via class hierarchy | Via delegation |
| Adding Behavior | Requires new subclasses (can lead to class explosion) | Combine components dynamically |
| Testing | Harder — tightly coupled to parent | Easier — dependencies can be mocked |
| Hierarchy Depth | Can become deep and fragile | Flat and maintainable |
| Reusability | Limited to hierarchy | High — reusable components |
| When to Use | When there is a true LSP-compliant "is-a" relationship | When behavior varies or needs flexibility |















































