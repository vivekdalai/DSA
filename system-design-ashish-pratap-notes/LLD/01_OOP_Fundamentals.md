# Classes and Objects
### 1. What is a Class?
**A class is a blueprint, template, or recipe for creating objects. It defines what an object will contain (its data) and what it will be able to do (its behavior).**

### 2. What is an Object?
**An object is an instance of a class.  It's the actual thing you can interact with, store data in, and invoke methods on.**

### Exercise 1: Bank Account

Problem: Create a BankAccount class that manages a simple bank account with deposit, withdrawal, and balance checking functionality.

```java
class BankAccount {
   // Add private fields: accountNumber (String), ownerName (String), balance (double)
   private String accountNumber;
   private String ownerName;
   private double balance;

   public BankAccount(String accountNumber, String ownerName) {
      // Initialize fields. Balance should start at 0.
      this.accountNumber = accountNumber;
      this.ownerName = ownerName;
      balance = 0;
   }

   public void deposit(double amount) {
      // Add amount to balance (only if amount is positive)
      if(amount > 0){
         balance += amount;
      }
   }

   public boolean withdraw(double amount) {
      // Remove amount from balance if sufficient funds exist
      // Return true if successful, false otherwise
      if(amount > 0 && this.balance >= amount){
         balance -= amount;
         return true;
      }
      return false;
   }

   public double getBalance() {
      // Return the current balance
      return balance;
   }
}

// Test your implementation
public class Main {
   public static void main(String[] args) {
      BankAccount account = new BankAccount("123456", "John Doe");
      account.deposit(1000);
      System.out.println(account.getBalance());  // Should print 1000.0

      boolean success = account.withdraw(500);
      System.out.println(success);               // Should print true
      System.out.println(account.getBalance());  // Should print 500.0

      success = account.withdraw(1000);
      System.out.println(success);               // Should print false
   }
}
```

### Exercise 2: Library Book
Design Library Book Class
Problem: Create a Book class for a library management system.

```java
class Book {
   // Add private fields: title (String), author (String), isbn (String), isAvailable (boolean)
   private String title;
   private String author;
   private String isbn;
   private boolean isAvailable;

   public Book(String title, String author, String isbn) {
      // Initialize fields. Book starts as available.
      this.title = title;
      this.author = author;
      this.isbn = isbn;
      this.isAvailable = true;
   }

   public boolean borrowBook() {
      // Mark book as unavailable if currently available
      // Return true if successful, false if already borrowed
      if(isAvailable){
         isAvailable = false;
         return true;
      }
      return false;
   }

   public void returnBook() {
      // Mark book as available
      isAvailable = true;
   }

   public void displayInfo() {
      // Print: "Title by Author (ISBN: isbn) - Available" or "- Borrowed"
      String status;
      if(isAvailable)
         status = "Available";
      else
         status = "Borrowed";
      System.out.println(title + " by " + author + " (ISBN: " + isbn + ") - " + status);
   }
}

// Test your implementation
public class Main {
   public static void main(String[] args) {
      Book book = new Book("The Pragmatic Programmer", "David Thomas", "978-0135957059");
      book.displayInfo();

      boolean success = book.borrowBook();
      System.out.println("Borrow successful: " + success);
      book.displayInfo();

      success = book.borrowBook();
      System.out.println("Borrow successful: " + success);

      book.returnBook();
      book.displayInfo();
   }
}
```
# Enums
### 1. What is an Enum?

**An enum (short for enumeration) is a special data type that defines a fixed set of named constants. Unlike strings or integers, enums are type-safe, meaning the compiler ensures you can only use values that actually exist in your defined set.**

**If a value can only be one of a predefined set of options, consider using an enum.**


### Exercise 1: Traffic Light
Design Traffic Light Class

Problem: Create a TrafficLight enum where each light has a color (RED, YELLOW, GREEN), a duration in seconds, and a next() method that returns the next light in the cycle (RED -> GREEN -> YELLOW -> RED).

```java
enum TrafficLight {
   // Define enum constants with duration: RED(30), YELLOW(5), GREEN(25)
   RED(30),
   YELLOW(5),
   GREEN(25);

   private final int duration;

   //passes value to constructor while initialisation -> RED(30)
   TrafficLight(int duration) {
      this.duration = duration;
   }

   public int getDuration() {
      return duration;
   }

   public TrafficLight next() {
      // Return next light: RED->GREEN, GREEN->YELLOW, YELLOW->RED
      switch (this) {
         case RED: return GREEN;
         case GREEN: return YELLOW;
         case YELLOW: return RED;
      }

      return this;
   }

   public void display() {
      // Print: "COLOR (Xs)" e.g. "RED (30s)"
      //this.name() --> built in method.
      //this.duration --> created property in constructor
      System.out.println(this.name() + " (" + this.duration + "s)");
   }
}

// Test your implementation
public class Main {
   public static void main(String[] args) {
      TrafficLight light = TrafficLight.RED;
      for (int i = 0; i < 6; i++) {
         light.display();  // Should show color and duration
         light = light.next();
      }
   }
}
```

### Exercise 2: HTTP Status Code

Implement HTTP Status Code
Problem: Create an HttpStatus enum where each status has a numeric code and a message string.

```java
enum HttpStatus {
   // Define constants: OK(200, "OK"), BAD_REQUEST(400, "Bad Request"),
// NOT_FOUND(404, "Not Found"), INTERNAL_SERVER_ERROR(500, "Internal Server Error")
   OK(200, "OK"),
   BAD_REQUEST(400, "Bad Request"),
   NOT_FOUND(404, "Not Found"),
   INTERNAL_SERVER_ERROR(500, "Internal Server Error");

   private final int code;
   private final String message;

   HttpStatus(int code, String message) {
      this.code = code;
      this.message = message;
   }

   public int getCode() {
      return code;
   }

   public String getMessage() {
      return message;
   }

   public boolean isSuccess() {
      // Return true if code < 400
      return code < 400;
   }

   public void display() {
      // Print: "CODE MESSAGE" e.g. "200 OK"
      System.out.println(code + " " + message);
   }

   public static HttpStatus fromCode(int code) {
      // Return the HttpStatus matching the code, or null if not found
      for(HttpStatus status : values()){
         if(status.code == code) return status;
      }
      return null;
   }
}

public class Main {
   public static void main(String[] args) {
      HttpStatus.OK.display();
      HttpStatus.NOT_FOUND.display();

      System.out.println("Is 200 success? " + HttpStatus.OK.isSuccess());
      System.out.println("Is 404 success? " + HttpStatus.NOT_FOUND.isSuccess());

      HttpStatus found = HttpStatus.fromCode(500);
      if (found != null) {
         System.out.print("Found by code 500: ");
         found.display();
      }
   }
}
```

# Interface

### 1. What is an Interface?

**At its core, an interface is a contract: a list of methods that any implementing class must provide. It specifies a set of behaviors that a class agrees to implement but leaves the details of those behaviors up to each implementation.**

In other words:

**_An interface defines the "what", while classes provide the "how".**

### 2. Key Properties

Here are their most important characteristics:

1. Defines Behavior Without Dictating Implementation
2. Enables Polymorphism
3.  Promotes Decoupling

This makes your code easier to:
Extend (add new implementations without modifying existing ones),
Test (mock interfaces in unit tests),
Maintain (fewer ripple effects from code changes).

Example: Payment Service, Notification service

### Exercise 1: Log Formatter
Design Log Formatter Class

Problem: Build a logging system where the format of log messages is configurable. A Logger class writes log messages, but the format (plain text vs. JSON) is determined by an injected Formatter interface.


```java
interface Formatter {
   String format(String message);
}

class PlainFormatter implements Formatter {
   public String format(String message) {
// Return the message as-is
      return message;
   }
}

class JsonFormatter implements Formatter {
   public String format(String message) {
// Return the message wrapped in JSON: {"log": "message"}
      return "{\"log\": \"" + message + "\"}";
   }
}

class Logger {
   private Formatter formatter;

   public Logger(Formatter formatter) {
      this.formatter = formatter;
   }

   public void log(String message) {
      // Use the formatter to format the message, then print the result
      System.out.println(formatter.format(message));
   }
}

// Test your implementation
public class Main {
   public static void main(String[] args) {
      Logger plainLogger = new Logger(new PlainFormatter());
      plainLogger.log("Server started on port 8080");

      Logger jsonLogger = new Logger(new JsonFormatter());
      jsonLogger.log("Server started on port 8080");
   }
}
```

### Exercise 2: Input Validator
Design Input Validator Class
Problem: Build a registration system where multiple validation rules are applied to user input. Each rule is a separate implementation of a Validator interface, and the RegistrationService runs all validators before accepting the registration.

```java
interface Validator {
   boolean validate(String input);
}

class EmailValidator implements Validator {
   public boolean validate(String input) {
// Return true if input contains "@"
      return input.contains("@");
   }
}

class PasswordValidator implements Validator {
   public boolean validate(String input) {
// Return true if input.length() >= 8
      return input.length() >= 8;
   }
}

class RegistrationService {
   private List<Validator> validators;

   public RegistrationService(List<Validator> validators) {
      this.validators = validators;
   }

   public void register(String input) {
      // Run all validators on input. If all pass, print "input" - PASSED
      // If any fail, print "input" - FAILED
      String status;
      boolean allPassed = true;
      for (Validator validator : validators) {
         if (!validator.validate(input)) {
            allPassed = false;
            break;
         }
      }


      System.out.println("\"" + input + "\" - " + (allPassed?"PASSED":"FAILED"));

   }
}

// Test your implementation
public class Main {
   public static void main(String[] args) {
      List<Validator> emailValidators = List.of(new EmailValidator());
      RegistrationService emailReg = new RegistrationService(emailValidators);
      emailReg.register("user@example.com"); // Should pass
      emailReg.register("invalid-email");     // Should fail

      List<Validator> passwordValidators = List.of(new PasswordValidator());
      RegistrationService passReg = new RegistrationService(passwordValidators);
      passReg.register("strongpassword"); // Should pass
      passReg.register("short");           // Should fail
   }
}
```

# Encapsulation

**Encapsulation is one of the four foundational principles of object-oriented design. It is the practice of grouping data (variables) and behavior (methods) that operate on that data into a single unit (typically a class) and restricting direct access to the internal details of that class.**

In simple terms:

**_Encapsulation = Data hiding + Controlled access**

### 1. Why Encapsulation Matters

1. Data Hiding
   Sensitive data (like a bank balance or password) should not be exposed directly. Encapsulation keeps this data private and accessible only through controlled methods.

2. Controlled Access and Validation
   It ensures that data can only be modified in controlled, predictable ways. \
   For example, you can prevent invalid deposits or withdrawals by validating input inside methods.

3. Improved Maintainability
   Because internal details are hidden, you can change the implementation (e.g., how data is stored or validated) without affecting the code that depends on it.

4. Security and Stability
   By preventing external tampering, encapsulation reduces the risk of inconsistent or invalid system states.


### Exercise 1: TemperatureSensor
Design Temperature Sensor Class

Problem: Build a TemperatureSensor class that collects temperature readings and provides statistical access. The sensor should validate that readings fall within a reasonable range and never expose its internal list of readings directly.cise

```java
class TemperatureSensor {
   private List<Double> readings = new ArrayList<>();

   public void addReading(double value) {
      // Only add if value is between -50 and 150 (inclusive)
      if (value >= -50 && value <= 150) readings.add(value);
   }

   public double getAverage() {
      // Return the average of all readings, or 0.0 if no readings exist
      if (readings.size() == 0) return 0.0;
      double sum = 0.0;
      for (double reading :  readings)
         sum += reading;
      return Math.round(sum / readings.size() * 100.0) / 100.0;
   }

   public int getReadingCount() {
      // Return how many readings have been recorded
      return readings.size();
   }

   public List<Double> getReadings() {
      // Return a defensive copy of the readings list (not the original)
      return new ArrayList<>(readings);
   }
}

// Test your implementation
public class Main {
   public static void main(String[] args) {
      TemperatureSensor sensor = new TemperatureSensor();
      sensor.addReading(22.5);
      sensor.addReading(23.1);
      sensor.addReading(200.0);  // Should be rejected
      sensor.addReading(-10.0);

      System.out.println("Count: " + sensor.getReadingCount());  // 3
      System.out.println("Average: " + sensor.getAverage());     // 11.87
   }
}
```

### Exercise 2: ShoppingCart
Design ShoppingCart Class

Problem: Build a ShoppingCart class that manages items, supports a one-time discount code, and prevents modifications after checkout.

```java
class ShoppingCart {
   private Map<String, Double> items = new HashMap<>();
   private boolean discountApplied = false;
   private boolean isCheckedOut = false;

   public void addItem(String name, double price) {
      // Add item to cart, but reject if already checked out
      if (isCheckedOut) {
         System.out.println("Cannot modify a checked-out cart"); return;
      }
      if (!items.containsKey(name)) items.put(name,price);
   }

   public boolean applyDiscount(String code) {
      // If code is "SAVE10" and no discount applied yet and not checked out,
      // mark discount as applied and return true. Otherwise return false.
      if (!discountApplied && !isCheckedOut && code.equals("SAVE10")) {
         discountApplied = true;
         return true;
      }

      return false;
   }

   public double getTotal() {
      // Sum all item prices. If discount was applied, subtract 10%.

      double sum = 0;
      for (Map.Entry<String,Double> entry : items.entrySet()){
         sum += entry.getValue();
      }
      return discountApplied ?  Math.round ((sum - (10.0 * sum / 100.0)) * 100.0) / 100.0 : sum;
   }

   public void checkout() {
      // Mark cart as checked out (only if it has items and isn't already checked out)
      isCheckedOut = true;
   }
}

// Test your implementation
public class Main {
   public static void main(String[] args) {
      ShoppingCart cart = new ShoppingCart();
      cart.addItem("Laptop", 999.99);
      cart.addItem("Mouse", 29.99);

      System.out.println("Total: $" + cart.getTotal());            // 1029.98

      System.out.println("Discount: " + cart.applyDiscount("SAVE10")); // true
      System.out.println("Total: $" + cart.getTotal());            // 926.98

      System.out.println("Discount: " + cart.applyDiscount("SAVE10")); // false (already applied)

      cart.checkout();
      cart.addItem("Keyboard", 79.99);  // Should be rejected
      System.out.println("Total: $" + cart.getTotal());            // 926.98 (unchanged)
   }
}
```

# Abstraction

**Abstraction is the process of hiding complex internal implementation details and exposing only the relevant, high-level functionality to the outside world. It allows developers to focus on what an object does, rather than how it does it.**

In short:

**Abstraction = Hiding Complexity + Showing Essentials**
By separating the what from the how, abstraction:
Reduces cognitive load
Improves modularity
Leads to cleaner, more intuitive APIs

**“Abstraction is about creating a simplified view of a system that highlights the essential features while suppressing the irrelevant details.”**

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

| Aspect  | Encapsulation                          | Abstraction                                      |
|---------|----------------------------------------|--------------------------------------------------|
| Focus   | Protecting data within a class         | Hiding implementation complexity                 |
| Goal    | Restrict access to internal state      | Simplify usage and expose only essentials        |
| Level   | Implementation-level                  | Design-level                                     |
| Example | Private balance field in BankAccount   | Exposing only deposit() and withdraw() without showing how they work |


### Exercise 1: Shape Calculator
Design Shape Calculator Class

Problem: Build a shape calculation system using an abstract class. The abstract Shape class has abstract methods for calculating area and perimeter, plus a concrete describe() method that all shapes inherit.

```java
abstract class Shape {
   protected String name;

   public Shape(String name) {
      this.name = name;
   }

   abstract double area();
   abstract double perimeter();

   void describe() {
      // Print: "Shape: [name], Area: [area], Perimeter: [perimeter]"
      // Use String.format("%.2f", value) to format to 2 decimal places
      System.out.println("Shape: " + name + ", Area: " + String.format("%.2f", area()) + ", Perimeter: " + String.format("%.2f", perimeter()));
   }
}

class Circle extends Shape {
   private double radius;

   public Circle(double radius) {
      super("Circle");
      this.radius = radius;
   }

   @Override
   double area() {
      // Area = pi * r^2
      return Math.PI * radius * radius;
   }

   @Override
   double perimeter() {
      // Perimeter = 2 * pi * r
      return 2 * Math.PI * radius;
   }
}

class Rectangle extends Shape {
   private double width;
   private double height;

   public Rectangle(double width, double height) {
      super("Rectangle");
      this.width = width;
      this.height = height;
   }

   @Override
   double area() {
      // Area = width * height
      return width * height;
   }

   @Override
   double perimeter() {
      // Perimeter = 2 * (width + height)
      return 2 * (width + height);
   }
}

public class Main {
   public static void main(String[] args) {
      Shape circle = new Circle(5.0);
      circle.describe();

      Shape rectangle = new Rectangle(4.0, 6.0);
      rectangle.describe();
   }
}
```

### Exercise 2: Data Exporter
Design Data Exporter Class
Problem: Build a data export system where the abstract class provides shared validation logic, and subclasses handle the actual export format.


```java
abstract class DataExporter {
   boolean validate(List<String> data) {
// Return false and print "Export failed: No data to export." if data is null or empty
// Return true and print "Validation passed. Exporting N records." otherwise
      if (data != null && !data.isEmpty() && data.size() > 0){
         System.out.println("Validation passed. Exporting " + data.size() + " records.");
         return true;
      }
      System.out.println("Export failed: No data to export.");
      return false;
   }

   abstract void export(List<String> data);
}

class CSVExporter extends DataExporter {
   @Override
   void export(List<String> data) {
// Call validate(data) first. If validation fails, return early.
// Otherwise, print CSV format: "CSV: Alice,Bob,Charlie"
// Hint: use String.join(",", data)
      if (!validate(data)) return;

      System.out.println("CSV: " + String.join(",", data));
   }
}

class JSONExporter extends DataExporter {
   @Override
   void export(List<String> data) {
// Call validate(data) first. If validation fails, return early.
// Otherwise, print JSON array format: JSON: ["Alice", "Bob", "Charlie"]
      if (!validate(data)) return;
      StringBuilder sb = new StringBuilder("JSON: [");
      for (int i = 0; i < data.size(); i++) {
         sb.append("\"").append(data.get(i)).append("\"");
         if (i < data.size() - 1) sb.append(", ");
      }
      sb.append("]");
      System.out.println(sb.toString());
   }
}

public class Main {
   public static void main(String[] args) {
      DataExporter csv = new CSVExporter();
      csv.export(List.of("Alice", "Bob", "Charlie"));

      System.out.println();

      DataExporter json = new JSONExporter();
      json.export(List.of("Alice", "Bob", "Charlie"));

      System.out.println();

      csv.export(List.of()); // Should fail validation
   }
}
```

# Inheritance

**Inheritance allows one class (called the subclass or child class) to inherit the properties and behaviors of another class (called the superclass or parent class).**

In simpler terms:

Inheritance enables code reuse by letting you define common logic once in a base class and then extend or specialize it in multiple derived classes.
This leads to cleaner, modular, and more maintainable software.

### 1. Why Inheritance Matters

1. Code Reusability
   It embodies the DRY (Don't Repeat Yourself) principle. Common logic is written once in the parent class and shared across all subclasses reducing redundancy.

2. Logical Hierarchy
   It creates a clear and intuitive hierarchy that model real-world “is-a” relationships like ElectricCar is a Car or Admin is a User.

3. Ease of Maintenance
   If a bug is found or a change is needed in the shared logic, you only need to fix it in one place, the superclass. All subclasses automatically inherit the fix.

4. Polymorphism
   Inheritance is a prerequisite for polymorphism, allowing objects of different subclasses to be treated as objects of the superclass.

### 2. Types of Inheritance

1. Single Inheritance is the simplest form: one child class extends one parent class
2. Multi-level Inheritance is when a child class itself becomes a parent
3. Hierarchical Inheritance is when multiple child classes extend the same parent.
4. Multiple Inheritance is when a child class extends more than one parent.

**Only C++ and Python support multiple inheritance directly. Java, C#, and TypeScript do not. The reason? The diamond problem.**

### 3. When to Use Inheritance

There is a clear "is-a" relationship (e.g., Dog is an Animal, Car is a Vehicle).
The parent class defines common behavior or data that children should share.
The child class does not violate the behavior expected from the parent.

### 4. Avoid Inheritance when

The relationship is "has-a" or "uses-a" rather than "is-a". A Car has an Engine, it is not an Engine. A Printer uses a Logger, it is not a Logger.

You want to combine behaviors from multiple sources dynamically.
You need runtime flexibility to swap behaviors.

You want to avoid tight coupling between child and parent internals.

### Exercise 1: Bank Account Hierarchy
Design Bank Account Hierarchy Class

Problem: Build a bank account system using inheritance. The base BankAccount class has common fields and methods for deposits and withdrawals. Specialized account types have different withdrawal rules.

```java
class BankAccount {
   protected String ownerName;
   protected String accountNumber;
   protected double balance;

   public BankAccount(String ownerName, String accountNumber, double balance) {
      // TODO: initialize ownerName, accountNumber, and balance
      this.ownerName = ownerName;
      this.accountNumber = accountNumber;
      this.balance = balance;
   }

   public boolean deposit(double amount) {
      // TODO: add amount to balance if amount > 0, return true if successful
      if (amount > 0) {
         balance += amount;
         return true;
      }
      return false;
   }

   public boolean withdraw(double amount) {
      // TODO: subtract amount from balance if balance >= amount, return true
      if (amount > 0 && balance >= amount) {
         balance -= amount;
         return true;
      }
      return false;
   }

   public void displayAccount() {
      // TODO: print "ownerName (accountNumber) | Balance: $balance"
      // Hint: use String.format("$%.2f", balance) for formatting
      System.out.println(ownerName + " (" + accountNumber + ") | Balance: " + String.format("$%.2f", balance));
   }
}

class SavingsAccount extends BankAccount {
   private final double interestRate;

   public SavingsAccount(String ownerName, String accountNumber,
                         double balance, double interestRate) {
      super(ownerName, accountNumber, balance);
      // TODO: initialize interestRate
      this.interestRate = interestRate;
   }

   @Override
   public boolean withdraw(double amount) {
      // TODO: only allow if (balance - amount) >= 100 (minimum balance rule)
      if (amount > 0 && (balance - amount) >= 100) {
         balance -= amount;
         return true;
      }
      return false;
   }

   public void applyInterest() {
      // TODO: add (balance * interestRate / 100) to balance
      balance += (balance * interestRate / 100);
   }
}

class CheckingAccount extends BankAccount {
   private final double overdraftLimit;

   public CheckingAccount(String ownerName, String accountNumber,
                          double balance, double overdraftLimit) {
      super(ownerName, accountNumber, balance);
      // TODO: initialize overdraftLimit
      this.overdraftLimit = overdraftLimit;
   }

   @Override
   public boolean withdraw(double amount) {
      // TODO: allow if (balance + overdraftLimit) >= amount
      if ((balance + overdraftLimit) >= amount) {
         balance -= amount;
         return true;
      }
      return false;
   }
}

public class Main {
   public static void main(String[] args) {
      SavingsAccount savings = new SavingsAccount("Alice", "SAV-001", 1000, 2.0);
      savings.displayAccount();
      System.out.println("Withdraw $950: " + savings.withdraw(950));
      savings.applyInterest();
      savings.displayAccount();

      System.out.println();

      CheckingAccount checking = new CheckingAccount("Bob", "CHK-002", 500, 300);
      checking.displayAccount();
      System.out.println("Withdraw $700: " + checking.withdraw(700));
      checking.displayAccount();
   }
}
```


### Exercise 2: Shape Hierarchy
Design Shape Hierarchy Class

Problem: Build a shape hierarchy where the base class provides a shared describe() method, and each child class implements its own area() and perimeter() methods.


```java
class Shape {
   protected String name;

   public Shape(String name) {
      this.name = name;
   }

   public double area() {
      // TODO: return 0 by default
      return 0;
   }

   public double perimeter() {
      // TODO: return 0 by default
      return 0;
   }

   public void describe() {
      // TODO: print "Shape: name, Area: area, Perimeter: perimeter"
      // Hint: use String.format("%.2f", value) for formatting
      System.out.println("Shape: " + name + ", Area: " + String.format("%.2f", area()) + ", Perimeter: " + String.format("%.2f", perimeter()));
   }
}

class Circle extends Shape {
   private double radius;

   public Circle(double radius) {
      super("Circle");
      // TODO: initialize this.radius
      this.radius = radius;
   }

   @Override
   public double area() {
      // TODO: return Math.PI * radius * radius
      return Math.PI * radius * radius;
   }

   @Override
   public double perimeter() {
      // TODO: return 2 * Math.PI * radius
      return 2 * Math.PI * radius;
   }
}

class Rectangle extends Shape {
   private double width;
   private double height;

   public Rectangle(double width, double height) {
      super("Rectangle");
      // TODO: initialize this.width and this.height
      this.width = width;
      this.height = height;
   }

   @Override
   public double area() {
      // TODO: return width * height
      return width * height;
   }

   @Override
   public double perimeter() {
      // TODO: return 2 * (width + height)
      return 2 * (width + height);
   }
}

public class Main {
   public static void main(String[] args) {
      Circle circle = new Circle(5.0);
      circle.describe();

      Rectangle rect = new Rectangle(4.0, 6.0);
      rect.describe();
   }
}
```

# Polymorphism


Polymorphism lets you call the same method on different objects, and have each object respond in its own way.


### Why Polymorphism Matters
Here are four concrete benefits that polymorphism provides.

Encourages loose coupling: You interact with abstractions (interfaces or base classes), not specific implementations.
Enhances flexibility: You can introduce new behaviors without modifying existing code, supporting the Open/Closed Principle.
Promotes scalability: Systems can grow to support more features with minimal impact on existing code.
Enables extensibility: You can “plug in” new implementations without touching the core business logic.


### How Polymorphism Works

1. Compile-time Polymorphism (Method Overloading)
2. Runtime Polymorphism (Method Overriding / Dynamic Dispatch)

### Polymorphism with Interfaces vs Abstract Classes

| Aspect            | Interface                                      | Abstract Class                              |
|-------------------|------------------------------------------------|---------------------------------------------|
| Relationship      | "can do" (capability)                          | "is a" (family)                             |
| Shared behavior   | None (contract only)                           | Yes (concrete methods + fields)             |
| Multiple          | A class can implement many                     | A class can extend only one                 |
| When to use       | Unrelated classes share a capability           | Related classes share logic                 |
| Example           | Sendable implemented by Email, Invoice, Report | Notification extended by Email, SMS, Push   |



### Exercise 1: Discount Calculator
Design Discount Calculator Class
Problem: Build a pricing system where an OrderProcessor applies discounts polymorphically. Different discount types share a common base class with shared formatting logic, but each one calculates the discounted price differently. The processor works with any discount through the abstract Discount type.

```java
abstract class Discount {
   protected String label;

   public Discount(String label) {
      this.label = label;
   }

   abstract double apply(double price);

   public void describe(double originalPrice) {
      // TODO: call apply(originalPrice) and print:
      //   "label: $originalPrice -> $discountedPrice"
      // Hint: use String.format("%.2f", value) for formatting
      System.out.println(label + ": $" + String.format("%.2f", originalPrice) + " -> $" + String.format("%.2f", apply(originalPrice)));
   }
}

class PercentageDiscount extends Discount {
   private double percentage;

   public PercentageDiscount(double percentage) {
      super(percentage + "% off");
      // TODO: initialize this.percentage
      this.percentage = percentage;
   }

   double apply(double price) {
      // TODO: return price * (1 - percentage / 100)
      return price * (1 - percentage / 100);
   }
}

class FlatDiscount extends Discount {
   private double amount;

   public FlatDiscount(double amount) {
      super("$" + amount + " off");
      // TODO: initialize this.amount
      this.amount = amount;
   }

   double apply(double price) {
      // TODO: return max(price - amount, 0)
      return Math.max(price - amount, 0);
   }
}

class BuyOneGetOneFree extends Discount {
   public BuyOneGetOneFree() {
      super("Buy 1 Get 1 Free");
   }

   double apply(double price) {
      // TODO: return price / 2
      return price / 2;
   }
}

class OrderProcessor {
   public void processOrder(String itemName, double price, Discount discount) {
// TODO: print "Item: itemName" then call discount.describe(price)
      System.out.println("Item: "+ itemName);
      discount.describe(price);
   }
}

public class Main {
   public static void main(String[] args) {
      OrderProcessor processor = new OrderProcessor();

      processor.processOrder("Laptop", 999.99, new PercentageDiscount(20));
      processor.processOrder("Headphones", 49.99, new FlatDiscount(15));
      processor.processOrder("Keyboard", 79.98, new BuyOneGetOneFree());
   }
}
```

### Exercise 2: Logging System
Design Logging System Class
Problem: Build a logging system where the application uses a Logger interface polymorphically. Different logger implementations send messages to different destinations, and the application doesn't know or care which one it's using.


```java
interface Logger {
   void log(String level, String message);
   String getDestination();
}

class ConsoleLogger implements Logger {
   public void log(String level, String message) {
// TODO: print "[level] message" to console
      System.out.println("[" + level + "] " + message);
   }

   public String getDestination() {
      // TODO: return "Console"
      return "Console";
   }
}

class FileLogger implements Logger {
   private String filePath;

   public FileLogger(String filePath) {
      // TODO: initialize this.filePath
      this.filePath = filePath;
   }

   public void log(String level, String message) {
      // TODO: print "Writing to filePath: [level] message"
      System.out.println("Writing to " + filePath + ": [" + level + "] " + message);
   }

   public String getDestination() {
      // TODO: return "File: filePath"
      return "File: " + filePath;
   }
}

class DatabaseLogger implements Logger {
   private String tableName;

   public DatabaseLogger(String tableName) {
      // TODO: initialize this.tableName
      this.tableName = tableName;
   }

   public void log(String level, String message) {
      // TODO: print "INSERT INTO tableName: [level] message"
      System.out.println("INSERT INTO " + tableName + ": [" + level + "] " + message);
   }

   public String getDestination() {
      // TODO: return "Database: tableName"
      return "Database: " + tableName;
   }
}

class Application {
   private Logger logger;

   public Application(Logger logger) {
      // TODO: initialize this.logger
      this.logger = logger;
   }

   public void run() {
      // TODO: log three messages with level "INFO":
      //   "Application starting..."
      //   "Processing data..."
      //   "Application shutting down."
      logger.log("INFO","Application starting...");
      logger.log("INFO","Processing data...");
      logger.log("INFO","Application shutting down.");

   }
}

public class Main {
   public static void main(String[] args) {
      List<Logger> loggers = List.of(
              new ConsoleLogger(),
              new FileLogger("/var/log/app.log"),
              new DatabaseLogger("app_logs")
      );

      for (Logger logger : loggers) {
         System.out.println("--- Using " + logger.getDestination() + " ---");
         Application app = new Application(logger);
         app.run();
         System.out.println();
      }
   }
}
```


























