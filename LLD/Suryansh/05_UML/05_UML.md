# Access Modifiers (UML / Java)

| Marker | Access Level | Meaning |
|--------|-------------|---------|
| + | Public | Accessible from any class |
| - | Private | Accessible only within the same class |
| # | Protected | Accessible within the class and its subclasses |
| ~ | Package (Default) | Accessible within the same package (Java-specific) |

# Relationships

## 1. Dependency (Weakest)

[OrderService] ----dashed---- (uses) ----arrow----> [EmailService]


## 2. Association

[Student] ---solid--- (learns from) ---arrow--->> [Teacher]



- One class knows about another and holds a reference to it as a field. 
- Independent Lifecycle
- many-many relationships

## 3. Aggregation

- whole-part relationships
- whole contains part but part can exist independently

[Playlist] <>------(contains)-------[Songs]


## 4. Composition

- 'has-a' relationship
- Stronger version of aggregation
- whole-part relationship but strong ownership.
- part lifecycle depends on whole
- whole gets destroyed ---> parts go with it

[Order] <->-------(contains)--------[LineItem]
 <-> --> filled diamond

## 5. Inheritance (Generalization)

- 'is-a' relationship


[Animal] <| -----solid line with hollow triangle ------ [Dog], [Cat]


## 6. Realization (Implementation)

- relationship between class and an interface it implements.
- class promises to provide concrete implementations of all the methods declared in the interface

[Flyable] <| ---------dashed line with hollow triangle --------- [Bird], [Airplane]


# Relationship Strength Spectrum

[Dependency] temporary use---> [Association] persistent reference ---> [Aggregation] weak whole-part ---> [Composition] strong whole-part ---> [Inheritance] structural 'is-a'---> [Realization] contractual 'can do'
















