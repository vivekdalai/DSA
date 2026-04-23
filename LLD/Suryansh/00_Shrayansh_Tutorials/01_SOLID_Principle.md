# What is SOLID?

1. S - Single Responsibility Principle (SRP)
2. O - Open/Close Principle
3. L - Liskov Substitute Principle
4. I - Interface Segregation Principle
5. D - Dependency Inversion Principle

# Advantages of using SOLID Design Principles

- Help us write better code
- Avoid Duplicate code
- Easy to maintain
- Easy to understand
- Flexible software
- Reduce complexity

# 1. What is SRP

A class should have ony one reason to change.
It should have only one open job or responsibility

actually LOGICAL GROUPING should be followed by a class

- Better Maintainability
- Improved Testability
- Enhanced Reusability

# 2. Open/Closed Principle

Open for extension and Closed for modification

we can achieve by using Interface

- Reduce risk
- Better Principle
- Improved Testability
- Enhanced Reusability
- Supports Polymorphism

# 3. Liskov Substitution Principle

Object of a superclass should be replaceable with objects of its subclass without breaking the application

subclass should extend the CAPABILITY of the parent class, not narrow it down

solution: have a proper interface which have common capabilities.

focus: provides consistent behaviour, code should not break when we switch into objects

# 4. Interface Segregation Principle

Interface should be such that the client should NOT implement unnecessary functions they do not need.

Unnecessary forcing the child class to implement the method don't ever need.

create proper interface with specific tasks and distributes amongst the interfaces

focus: if child class is not providing the behaviour then remove the capabilities into different interface

# 5. Dependency Inversion Principle

High level components should not depend on low-level components directly; instead, they should depend on abstractions.

rely on abstractions or interface






