# Classes & Objects
- A class in Java is a blueprint for creating objects. 
- It encapsulates data for the object and methods to manipulate that data. 
- When you define a class, you're essentially defining a new data type. 
- An object, on the other hand, is an instance of a class.

# What is a Constructor?
* A constructor is a special type of method that is called when an object is instantiated.
* Unlike regular methods, a constructor has the same name as the class and does not return a value—not even void. 
* This unique behavior allows Java to recognize it as a constructor.

## Types of Constructors
1. Default Constructors
2. Parameterized Constructors

## Constructor Overloading

```java
public class Person {
    String name;
    int age;

    // Constructor with name only
    public Person(String name) {
        this.name = name;
        this.age = 0; // default age
    }

    // Constructor with name and age
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```


## The this keyword in Constructors

- The this keyword is essential in constructors to refer to the current object. 
- It can help disambiguate between instance variables and parameters when they share the same name

### When to Use this

1. Disambiguation
2. Accessing Instance Methods
3. Returning the Current Instance

Point 3 example
```java
public class Builder {
    private String value;

    public Builder setValue(String value) {
        this.value = value;
        return this; // returning the current instance for chaining
    }
}

// Usage
Builder builder = new Builder()
    .setValue("Hello")
    .setValue("World");

```

- Remember that this cannot be used in static methods.
- Static methods do not belong to any instance, so there is no this to refer to

For eg.
```java
public class Example {
    public static void staticMethod() {
        // System.out.println(this); // This would cause a compilation error
    }
}
```

# static Keyword

## Static Variables
- When you declare a variable as static, it is shared across all instances of the class.
- This means that if one instance modifies the static variable, the change is reflected in all instances.

## Static Methods
- Static methods can be called without creating an instance of the class. 
- This is particularly useful for utility or helper methods that don’t require any object state to perform their tasks.

## Static Blocks
- Static blocks are a way to initialize static variables or perform actions that should happen only once when the class is loaded. 
- They are executed in the order they appear in the code.

Static Blocks example: 
```java
class Outer {
    static class Nested {
        void display() {
            System.out.println("Inside Nested Class");
        }
    }
}

class Main {
    public static void main(String[] args) {
        Outer.Nested nested = new Outer.Nested(); // No need for an Outer instance
        nested.display(); // Outputs: Inside Nested Class
    }
}
```

# Final keyword

- final variable
  - Can't be changed
  - defining constants
- final method
  - can't be overridden by subclasses
  - mainly used in frameworks or libraries ensuring critical functionalities intact
- final class
  - can't be extended to other classes
  - Commonly used in utility classes where inheritance doesn't make sense and could lease to misuse
- final variable with constructor 
  - can be initialized through constructor if it has not been initialized at the point of declaration
  - promotes the concept of immutability
  - lead to safer multi-threaded code, as immutable object are inherently thread-safe

## Common Mistake
- Final Reference, Not Final Object
```java
final List<String> list = new ArrayList<>();
list.add("Hello"); // This is valid
// list = new ArrayList<>(); // This would cause a compile-time error
```

- static final variables 
  - static final should be initialized at declaration.
- final methods in interface
  - can't declare method as final in interface as it's meant to be implemented and extended.

# Initializer Blocks
- At their core, initializer blocks are blocks of code that run when an instance of a class is created, even before the constructor is executed.

- There are two types of initializer blocks:
  - Instance Initializer Blocks: These run whenever an instance of the class is created. 
  - Static Initializer Blocks: These run only once, when the class is loaded, and are used to initialize static variables.
## Instance Initializer Blocks
- defined inside a class but outside of any method or constructor
- execute in the order they appear in the class definition
- run every time a constructor is called.

```java
class Car {
    private String model;
    private int year;

    // Instance initializer block
    {
        model = "Default Model";
        year = 2020;
        System.out.println("Instance Initializer Block Executed");
    }

    public Car() {
        System.out.println("Default Constructor Executed");
    }

    public Car(String model, int year) {
        this.model = model;
        this.year = year;
        System.out.println("Parameterized Constructor Executed");
    }

    public void displayInfo() {
        System.out.println("Model: " + model + ", Year: " + year);
    }
}

public class Main {
    public static void main(String[] args) {
        Car car1 = new Car();
        car1.displayInfo();
        
        Car car2 = new Car("Toyota", 2021);
        car2.displayInfo();
    }
}
```

### When to use Instance Intializer blocks?
1. Common Initialization Logic: When multiple constructors need to execute the same initialization code, use an initializer block to reduce duplication. 
2. Complex Initialization Logic: If the initialization logic is too complex for a single line, an initializer block can help keep your constructor clean while still performing necessary tasks.

## Static Initializer Blocks

- similar to instance blocks
- run once when the class is loaded
- useful for initializing static variables or performing one-time setup for the class

```java
class Configuration {
    private static String configFilePath;

    // Static initializer block
    static {
        configFilePath = "/etc/config.properties";
        System.out.println("Static Initializer Block Executed");
    }

    public static String getConfigFilePath() {
        return configFilePath;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Config File Path: " + Configuration.getConfigFilePath());
    }
}
```

### When to Use Static Initializer Blocks?

1. You need to perform complex initialization for static variables.
2. You want to load resources or configurations at class load time, ensuring they are ready before any instances are created.

# Inner Classes
- Inner classes are classes defined within the scope of another class.

### Why Use Inner Classes?

1. Encapsulation: Inner classes can help encapsulate functionality that is only relevant in the context of the outer class. 
2. Readability: By grouping related classes, your code can be easier to navigate and understand. 
3. Access: Inner classes have direct access to the outer class’s instance variables and methods, which can eliminate boilerplate code.

```java
public class OuterClass {
    private String outerField = "Outer field";

    public class InnerClass {
        public void display() {
            System.out.println("Accessing: " + outerField);
        }
    }

    public void createInnerInstance() {
        InnerClass inner = new InnerClass();
        inner.display();
    }

    public static void main(String[] args) {
        OuterClass outer = new OuterClass();
        outer.createInnerInstance();
    }
}
```

## Types of Inner Classes

### Member Inner Class
- declared within the body of the outer class
- can access all the members of the outer class

```java
public class MemberInnerClass {
    private String message = "Hello from the outer class!";

    public class Inner {
        public void printMessage() {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        MemberInnerClass outer = new MemberInnerClass();
        MemberInnerClass.Inner inner = outer.new Inner();
        inner.printMessage();
    }
}
```

### Static Nested Class

- does not require an instance of the outer class to be instantiated
- cannot access instance variables or methods of the outer class without a reference

```java
public class StaticNestedClass {
    private static String staticMessage = "I'm static!";

    public static class Nested {
        public void printStaticMessage() {
            System.out.println(staticMessage);
        }
    }

    public static void main(String[] args) {
        StaticNestedClass.Nested nested = new StaticNestedClass.Nested();
        nested.printStaticMessage();
    }
}
```

### Local Inner Class
- defined within a method and can access local variables and parameters of the method
- these variables must be final or effectively final

```java
public class LocalInnerClass {
  public void createInnerClass() {
    final String localVar = "I am local";

    class Local {
      public void display() {
        System.out.println(localVar);
      }
    }

    Local localInner = new Local();
    localInner.display();
  }

  public static void main(String[] args) {
    LocalInnerClass obj = new LocalInnerClass();
    obj.createInnerClass();
  }
}
```

### Anonymous Inner Classes
- inner classes without a name, used for instantiating a class that may not need a separate class file.
- often used in event-handling

```java
public class AnonymousInnerClass {
    interface Greeting {
        void sayHello();
    }

    public static void main(String[] args) {
        Greeting greeting = new Greeting() {
            @Override
            public void sayHello() {
                System.out.println("Hello from an anonymous inner class!");
            }
        };

        greeting.sayHello();
    }
}
```

## Use Cases for Inner Classes
### Implementing Callback Interfaces
- Inner classes are great for implementing callback interfaces, especially in GUI applications, where you often need to handle events.

```java
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonExample {
    public void createButton() {
        JButton button = new JButton("Click Me");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button was clicked!");
            }
        });

        JFrame frame = new JFrame("Inner Class Example");
        frame.add(button);
        frame.setSize(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        ButtonExample example = new ButtonExample();
        example.createButton();
    }
}
```

### Data Structures
- to create data structures

```java
public class Tree {
    private Node root;

    private class Node {
        int value;
        Node left, right;

        Node(int value) {
            this.value = value;
        }
    }

    public void insert(int value) {
        root = insertRec(root, value);
    }

    private Node insertRec(Node root, int value) {
        if (root == null) {
            root = new Node(value);
            return root;
        }
        if (value < root.value) {
            root.left = insertRec(root.left, value);
        } else if (value > root.value) {
            root.right = insertRec(root.right, value);
        }
        return root;
    }
}
```

### State Machines
- can help encapsulate states in a state machine pattern
- allowing you to better manage transitions

```java
public class StateMachine {
    private State currentState;

    public StateMachine() {
        currentState = new StateA();
    }

    private interface State {
        void handle();
    }

    private class StateA implements State {
        public void handle() {
            System.out.println("Handling State A");
            currentState = new StateB();
        }
    }

    private class StateB implements State {
        public void handle() {
            System.out.println("Handling State B");
            currentState = new StateA();
        }
    }

    public void execute() {
        currentState.handle();
    }

    public static void main(String[] args) {
        StateMachine sm = new StateMachine();
        sm.execute();
        sm.execute(); // Switches to State B
    }
}
```

## Common Pitfalls with Inner Classes

- Memory Leaks 
  - If an inner class holds a reference to an outer class, it can lead to memory leaks if the outer class is long-lived.
- Complexity 
  - Overusing inner classes can make your code harder to read. 
  - Always consider whether the inner class truly adds clarity or if a top-level class might be more appropriate.
- Inheritance Issues 
  - If you plan to use inheritance, remember that inner classes cannot be static if they are part of a class hierarchy.

# Anonymous Classes
- inner class that does not have a name and is declared and instantiated in a single expression
- particularly useful for event handling, callbacks, or any situation where a one-off implementation is required.

### Syntax

```java
Type instanceName = new Type() {
    // body of the anonymous class (Implementation)
};
```
- Type could be a class or an interface


```java
interface Greeting {
    void sayHello();
}

```

```java
Greeting greeting = new Greeting() {
  @Override
  public void sayHello() {
    System.out.println("Hello from an anonymous class!");
  }
};

// Usage
greeting.sayHello(); // Outputs: Hello from an anonymous class!

```

## Practical Use Case
1. Event Handling in GUI Applications 
2. Implementing Interfaces

point 1 example:
```java
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ButtonExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Anonymous Class Example");
        JButton button = new JButton("Click Me");

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Button clicked!");
            }
        });

        frame.add(button);
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
```

point 2 example:
```java
interface Calculator {
    int operate(int a, int b);
}

public class Calculation {
    public static void main(String[] args) {
        Calculator addition = new Calculator() {
            @Override
            public int operate(int a, int b) {
                return a + b;
            }
        };
        
        System.out.println("Addition: " + addition.operate(5, 3)); // Outputs: 8
    }
}
```

## Anonymous Classes vs. Lambda Expressions

- Java 8 introduced lambdas as a more concise way to implement functional interfaces.

- Key Differences 
  - Syntax: Anonymous classes have a more verbose syntax compared to lambda expressions. 
  - Statefulness: Anonymous classes can contain instance variables, while lambdas can only use final or effectively final variables from the enclosing scope. 
  - Use Case: Use anonymous classes when you need to implement multiple methods or maintain state. Use lambdas for simpler, single-method interfaces.

### Example of both in action

```java
// Using an anonymous class
Runnable task1 = new Runnable() {
    @Override
    public void run() {
        System.out.println("Task executed with anonymous class.");
    }
};

// Using a lambda expression
Runnable task2 = () -> System.out.println("Task executed with lambda.");

new Thread(task1).start(); // Outputs: Task executed with anonymous class.
new Thread(task2).start(); // Outputs: Task executed with lambda.
```

## Edge Cases and Nuances

1. Anonymous Classes and Access Modifiers
   Anonymous classes can access the enclosing class's members, including private members. This can lead to tightly coupled code, which could become problematic for maintenance.

2. Performance Considerations
   Creating anonymous classes results in additional class files being generated at runtime. For performance-sensitive applications, consider whether you need the flexibility they provide or if simpler constructs (like lambdas) will suffice.

3. Limitations on Inheritance
   Anonymous classes can only extend one class (single inheritance). If you find yourself needing to extend multiple classes, you’ll have to resort to standard class creation.

4. No Constructor
   Since anonymous classes do not have a name, they cannot have a constructor with parameters. You can initialize fields directly or use the instance initialization block.

# Record Classes

- introduced in Java 14
- special kind of class introduced to provide a compact syntax for modeling immutable data.
- inherently final, meaning they cannot be subclassed.
- Immutable
- When you define a record, Java automatically generates the following:
  - Fields for all the properties declared in the record. 
  - A constructor to initialize these fields. 
  - Getters for each field. 
  - Implementations of equals(), hashCode(), and toString() methods.
- SYNTAX:
```java
public record Person(String name, int age) {
}
```

## Advantages of Using Record Classes

### Reduced Boilerplate

Traditional way:

```java
public class Book {
  private final String title;
  private final String author;
  private final int year;

  public Book(String title, String author, int year) {
    this.title = title;
    this.author = author;
    this.year = year;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public int getYear() {
    return year;
  }

  @Override
  public String toString() {
    return "Book{" +
            "title='" + title + '\'' +
            ", author='" + author + '\'' +
            ", year=" + year +
            '}';
  }
}
```

With Record:

```java
public record Book(String title, String author, int year) {
}
```

### Immutability
- Once you create an instance of a record, you cannot change its fields.
- safer for multithreaded environment
```java
public record Point(int x, int y) {
}

// Usage
Point p = new Point(1, 2);
// p.x = 3; // This line would result in a compilation error
```


























































