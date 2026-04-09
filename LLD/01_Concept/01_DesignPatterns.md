# Design Patterns

Design patterns are **reusable solution templates** for common software design problems. They are not complete applications or fixed code you blindly copy. Instead, they provide a **proven way of organizing classes, objects, and responsibilities**.

A good way to think about design patterns:

- A problem appears repeatedly in software design.
- Over time, developers discover a clean reusable structure to solve it.
- That reusable structure becomes a **design pattern**.

So, a pattern is more about **design thinking** than about syntax.

---

## Why do we use design patterns?

We use design patterns to make code:

- easier to understand
- easier to extend
- less tightly coupled
- more maintainable
- easier to test

But an important warning:

> Do not use a design pattern just because it exists.  
> Use it only when the problem actually needs it.

If the code is simple, simple code is better than an unnecessary pattern.

---

## Main categories of design patterns

The classic GoF (Gang of Four) design patterns are mainly divided into **3 categories**:

1. **Creational Patterns**
   - Focus on object creation

2. **Structural Patterns**
   - Focus on class/object composition

3. **Behavioral Patterns**
   - Focus on communication between objects

The classic GoF categorization uses **3 main groups**: Creational, Structural, and Behavioral.

---

## Pattern notes separated by responsibility

To keep the notes cleaner and more focused, each category is now placed in a separate file:

- [CreationalPatterns.md](./02_CreationalPatterns.md)
- [StructuralPatterns.md](./03_StructuralPatterns.md)
- [BehavioralPatterns.md](./04_BehavioralPatterns.md)

---

## Quick understanding

### Creational Patterns
These focus on **how objects are created**.

Use them when:
- object creation is complex
- object creation should be controlled
- construction should be more flexible or readable

Examples:
- Constructor
- Singleton
- Builder
- Factory

### Structural Patterns
These focus on **how classes and objects are arranged together**.

Use them when:
- systems need better composition
- interfaces do not match
- a complex subsystem should look simpler

Examples:
- Adapter
- Decorator
- Facade
- Composite
- Proxy

### Behavioral Patterns
These focus on **how objects communicate and divide responsibility**.

Use them when:
- behavior changes dynamically
- one object should notify others
- request flow or action handling needs flexibility

Examples:
- Strategy
- Observer
- Command
- State
- Iterator

---

## Final intuition

When you study a design pattern, do not first memorize code.  
First ask:

1. **What problem is this pattern solving?**
2. **Why is normal coding becoming messy here?**
3. **How does this pattern reduce coupling or improve readability?**
4. **What trade-off does it introduce?**

That thinking is more important than remembering syntax.

---

## Summary

- Design patterns are reusable design solutions, not fixed code.
- The standard main categories are **Creational, Structural, and Behavioral**.
- Each category has now been separated into its own file for better clarity and organization.
- The most important skill is not just knowing pattern names, but knowing **when not to use them**.
