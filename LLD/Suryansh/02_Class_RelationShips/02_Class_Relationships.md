# Association

**Association represents a relationship between two classes where one object uses, communicates with, or references
another.**

This relationship models the idea:

“One object need to know about the existence of another object to perform its responsibilities”

Association reflects a "has-a" or "uses-a" relationship.
Associated objects are loosely coupled and can exist independently of one another.
The association can be unidirectional or bidirectional, and can follow different multiplicity patterns (1-to-1,
1-to-many, etc.).

In UML class diagrams, association is represented by a solid line between two classes.

| Symbol             | Meaning                         | Example Scenario                     |
|--------------------|---------------------------------|--------------------------------------|
| Solid line (`---`) | An association between classes  | Student --- Teacher                  |
| Arrowhead (`-->`)  | Directionality (who knows whom) | Order --> PaymentGateway             |
| No arrowhead       | Bidirectional association       | Team --- Developer                   |
| `1`                | Exactly one                     | Each User has one Profile            |
| `0..1`             | Zero or one (optional)          | An Employee may have a Manager       |
| `*`                | Many (zero or more)             | A Project can have many Tasks        |
| `1..*`             | At least one                    | Each Course has one or more Students |

# Aggregation

Aggregation is a specialized form of association that models a whole-part relationship with loose ownership. One class (
the "whole") contains references to other class objects (the "parts"), but the parts can exist independently of the
whole.

### Key Characteristics of Aggregation:

The whole and the part are logically connected through a container-contained hierarchy.
The part can exist independently of the whole.
The whole does not create or destroy the part.
The part can be shared among multiple wholes.
Both the whole and the part can be created and destroyed independently.

If a class contains other classes for logical grouping only without lifecycle ownership, it is an aggregation.

In UML class diagrams, aggregation is represented by a hollow diamond (◊) on the "whole" side of the relationship

# Composition

Composition is a special type of association that signifies strong ownership between objects. The “whole” class is fully
responsible for creating, managing, and destroying the “part” objects. In fact, the parts cannot exist without the
whole.

### Key Characteristics of Composition:

Represents a strong “has-a” relationship.
The whole owns the part and controls its lifecycle.
When the whole is destroyed, the parts are also destroyed.
The parts are not shared with any other object.
The part has no independent meaning or identity outside the whole.

If the part makes no sense without the whole, use composition.

In UML class diagrams, composition is represented by a filled diamond (◆) at the “whole” end of the relationship.

“**Favor composition over inheritance.**” — GoF Design Principle

| Feature            | Association              | Aggregation                    | Composition                       |
|--------------------|--------------------------|--------------------------------|-----------------------------------|
| Ownership          | None                     | Weak -- has-a                  | Strong -- owns-a                  |
| Lifecycle          | Independent              | Independent                    | Dependent -- part dies with whole |
| Tightness          | Loose coupling           | Moderate coupling              | Tight coupling                    |
| Multiplicity       | Flexible (1:1, 1:N, N:N) | Whole can group many parts     | Whole composed of integral parts  |
| Reusability        | High -- parts reusable   | Moderate -- parts often reused | Low -- parts not reused outside   |
| UML Symbol         | Solid Line               | Hollow Diamond (◊)             | Filled Diamond (◆)                |
| Who creates parts? | Either side or external  | External -- passed in          | Whole -- created internally       |
| Real Example       | Student ↔ Course         | Playlist → Song<br /><br />    | Order → LineItem                  |

### Think of it like this:

Association is a general connection: two classes simply know about each other.
Aggregation is a grouping: the whole and parts can exist independently.
Composition is an ownership: the part’s existence is bound to the whole.

# Dependency

A Dependency exists when one class relies on another to fulfill a responsibility, but does so without retaining a
permanent reference to it.

This typically happens when:

A class accepts another class as a method parameter.
A class instantiates or uses another class inside a method.
A class returns an object of another class from a method.

### Key Characteristics of Dependency

Short-lived: The relationship exists only during method execution.
No ownership: The dependent class does not store the other as a field.
"Uses-a" relationship: The class uses another to accomplish a task, but does not retain it.

In UML class diagrams, dependency is represented by a dashed arrow (..>) pointing from the dependent class to the class
it depends on.

# Realization

Realization represents a contract fulfillment relationship. Think of it as a promise: the interface declares "these
methods must exist," and the implementing class promises to provide them.

The relationship works like this:

An interface defines what must be done (the contract)
A class implements how it's done (the fulfillment)
The implementing class must provide all methods declared in the interface
Multiple classes can realize the same interface differently

Real-world analogy

Think of a job description. The job description (interface) defines what skills and responsibilities are required.
Different employees (classes) can fulfill that job description in their own way, but they all meet the requirements.

In UML class diagrams, realization is represented by a dashed line with a hollow (unfilled) triangle pointing to the
interface.

**Inheritance models identity where as Realization models capability**
