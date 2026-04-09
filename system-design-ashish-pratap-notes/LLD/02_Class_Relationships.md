# Association

**Association represents a relationship between two classes where one object uses, communicates with, or references another.**

This relationship models the idea:

“One object need to know about the existence of another object to perform its responsibilities”

Association reflects a "has-a" or "uses-a" relationship.
Associated objects are loosely coupled and can exist independently of one another.
The association can be unidirectional or bidirectional, and can follow different multiplicity patterns (1-to-1, 1-to-many, etc.).

In UML class diagrams, association is represented by a solid line between two classes.


| Symbol             | Meaning                         | Example Scenario                     |
| ------------------ | ------------------------------- | ------------------------------------ |
| Solid line (`---`) | An association between classes  | Student --- Teacher                  |
| Arrowhead (`-->`)  | Directionality (who knows whom) | Order --> PaymentGateway             |
| No arrowhead       | Bidirectional association       | Team --- Developer                   |
| `1`                | Exactly one                     | Each User has one Profile            |
| `0..1`             | Zero or one (optional)          | An Employee may have a Manager       |
| `*`                | Many (zero or more)             | A Project can have many Tasks        |
| `1..*`             | At least one                    | Each Course has one or more Students |

### Exercise 1: Online Course Platform

Design Online Course Platform

Problem: Build a course platform where instructors create courses and students enroll in them. This exercise practices unidirectional and one-to-many associations.

```java
import java.util.ArrayList;
import java.util.List;

class Instructor {
    private String name;
    private List<Course> courses = new ArrayList<>();

    public Instructor(String name) {
        this.name = name;
    }

    public void addCourse(Course course) {
        // TODO: Add course to list and set this as the course's instructor
        courses.add(course);
        course.setInstructor(this);
    }

    public String getName() { return name; }
    public List<Course> getCourses() { return courses; }
}

class Course {
    private String title;
    private Instructor instructor;
    private List<Student> students = new ArrayList<>();

    public Course(String title) {
        this.title = title;
    }

    public void setInstructor(Instructor instructor) {
        // TODO: Set the instructor reference
        this.instructor = instructor;
    }

    public void enrollStudent(Student student) {
        // TODO: Add student to list and set this as the student's enrolled course
        students.add(student);
        student.setEnrolledCourse(this);
    }

    public String getTitle() { return title; }
    public Instructor getInstructor() { return instructor; }
    public List<Student> getStudents() { return students; }
}

class Student {
    private String name;
    private Course enrolledCourse;

    public Student(String name) {
        this.name = name;
    }

    public void setEnrolledCourse(Course course) {
        // TODO: Set the enrolled course reference
        this.enrolledCourse = course;
    }

    public String getInstructorName() {
        // TODO: Navigate through enrolledCourse to get the instructor's name
        // Return "No instructor" if course or instructor is null
        if (enrolledCourse != null && enrolledCourse.getInstructor() != null) return enrolledCourse.getInstructor().getName();
        return "No instructor";
    }

    public String getName() { return name; }
    public Course getEnrolledCourse() { return enrolledCourse; }
}

public class Main {
    public static void main(String[] args) {
        Instructor alice = new Instructor("Alice");
        Course dsa = new Course("Data Structures");
        Course sysDesign = new Course("System Design");

        alice.addCourse(dsa);
        alice.addCourse(sysDesign);

        Student bob = new Student("Bob");
        Student charlie = new Student("Charlie");

        dsa.enrollStudent(bob);
        dsa.enrollStudent(charlie);
        sysDesign.enrollStudent(charlie);

        System.out.println(alice.getName() + "'s courses:");
        for (Course c : alice.getCourses())
            System.out.println("  - " + c.getTitle());

        System.out.println("Students in " + dsa.getTitle() + ":");
        for (Student s : dsa.getStudents())
            System.out.println("  - " + s.getName());

        System.out.println(bob.getName() + "'s instructor: " + bob.getInstructorName());
    }
}
```

### Exercise 2: Social Network

Design Social Network Classes

Problem: Build a social network where users can follow each other and send messages. This exercise practices bidirectional and many-to-many associations with synchronization guards.

```java
import java.util.ArrayList;
import java.util.List;

class Message {
    private User author;
    private String content;
    private String timestamp;

    public Message(User author, String content, String timestamp) {
        // TODO: Initialize fields
        this.author = author;
        this.content = content;
        this.timestamp = timestamp;
    }

    public User getAuthor() { return author; }
    public String getContent() { return content; }
    public String getTimestamp() { return timestamp; }
}

class User {
    private String name;
    private List<User> followers = new ArrayList<>();
    private List<User> following = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public void follow(User user) {
        // TODO: Add user to following, add this to user's followers
        // Guard against: self-follows, duplicates
        if (this != user && !following.contains(user) && !user.getFollowers().contains(this)) {
            following.add(user);
            user.getFollowers().add(this);
      
        }
    }

    public void sendMessage(String content, String timestamp) {
        // TODO: Create Message and add to messages list
        Message message = new Message(this, content, timestamp);
        messages.add(message);
    }

    public String getName() { return name; }
    public List<User> getFollowers() { return followers; }
    public List<User> getFollowing() { return following; }
    public List<Message> getMessages() { return messages; }
}

public class Main {
    public static void main(String[] args) {
        User alice = new User("Alice");
        User bob = new User("Bob");
        User charlie = new User("Charlie");

        alice.follow(bob);
        alice.follow(charlie);
        bob.follow(alice);

        alice.sendMessage("Hello world!", "10:00 AM");
        bob.sendMessage("Learning OOP!", "10:30 AM");

        System.out.println(alice.getName() + " is following:");
        for (User u : alice.getFollowing())
            System.out.println("  - " + u.getName());

        System.out.println(bob.getName() + "'s followers:");
        for (User u : bob.getFollowers())
            System.out.println("  - " + u.getName());

        System.out.println(alice.getName() + "'s messages:");
        for (Message m : alice.getMessages())
            System.out.println("  [" + m.getTimestamp() + "] " + m.getContent());
    }
}
```

# Aggregation

Aggregation is a specialized form of association that models a whole-part relationship with loose ownership. One class (the "whole") contains references to other class objects (the "parts"), but the parts can exist independently of the whole.

### Key Characteristics of Aggregation:

The whole and the part are logically connected through a container-contained hierarchy.
The part can exist independently of the whole.
The whole does not create or destroy the part.
The part can be shared among multiple wholes.
Both the whole and the part can be created and destroyed independently.

If a class contains other classes for logical grouping only without lifecycle ownership, it is an aggregation.

In UML class diagrams, aggregation is represented by a hollow diamond (◊) on the "whole" side of the relationship

### Exercise 1: Shopping Cart System

Design Shopping Cart Class
Solved
Problem: Build an e-commerce system where products exist in a catalog and customers add them to shopping carts. This exercise practices aggregation where parts (products) are shared across multiple wholes (carts) and survive when a cart is cleared.

```java
import java.util.ArrayList;
import java.util.List;

class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
}

class Catalog {
    private List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        // TODO: Add product to catalog
        products.add(product);
    }

    public Product findByName(String name) {
        // TODO: Find and return product by name, return null if not found
        for (Product product : products) {
            if (product.getName().equals(name)) return product;
        }
        return null;
    }

    public int getProductCount() { return products.size(); }
}

class Cart {
    private List<Product> items = new ArrayList<>();

    public void addItem(Product product) {
        // TODO: Add product to cart
        items.add(product);
    }

    public void clearCart() {
        // TODO: Remove all items (don't destroy the products!)
        items.clear();
    }

    public double getTotal() {
        // TODO: Sum prices of all items
        double total = 0;
        for (Product product : items) {
            total += product.getPrice();
        }
        return total;
    }

    public List<Product> getItems() { return items; }
    public int getItemCount() { return items.size(); }
}

class Customer {
    private String name;
    private Cart cart;

    public Customer(String name, Cart cart) {
        this.name = name;
        this.cart = cart;
    }

    public void checkout() {
        // TODO: Print cart items and total, then clear cart
        System.out.println(name + " checking out:");
        for (Product product : cart.getItems()) {
            System.out.println("  - " + product.getName() + ": $" + product.getPrice());
        }

        System.out.println("  Total: $" + cart.getTotal());

        cart.clearCart();
    }

    public String getName() { return name; }
    public Cart getCart() { return cart; }
}

public class Main {
    public static void main(String[] args) {
        // Create products and add to catalog
        Product laptop = new Product("Laptop", 999.99);
        Product mouse = new Product("Mouse", 29.99);
        Product keyboard = new Product("Keyboard", 79.99);

        Catalog catalog = new Catalog();
        catalog.addProduct(laptop);
        catalog.addProduct(mouse);
        catalog.addProduct(keyboard);

        // Two customers share the same catalog products
        Cart cart1 = new Cart();
        Cart cart2 = new Cart();

        Customer alice = new Customer("Alice", cart1);
        Customer bob = new Customer("Bob", cart2);

        // Both customers add the same laptop to their carts
        cart1.addItem(laptop);
        cart1.addItem(mouse);
        cart2.addItem(laptop);
        cart2.addItem(keyboard);

        System.out.println("Alice's cart: " + cart1.getItemCount() + " items, $" + cart1.getTotal());
        System.out.println("Bob's cart: " + cart2.getItemCount() + " items, $" + cart2.getTotal());

        alice.checkout();

        // After Alice checks out, products still exist
        System.out.println("Catalog still has " + catalog.getProductCount() + " products");
        System.out.println("Bob's cart still has " + cart2.getItemCount() + " items");
        System.out.println("Laptop still exists: " + laptop.getName());
    }
}
```

### Exercise 2: Company Team Management

Design Company Team Management
Problem: Build a company system where employees can belong to multiple teams. Dissolving a team doesn't remove the employees. This exercise reinforces that aggregation parts survive the deletion of the whole and can be shared.

```java
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Employee {
    private String name;
    private String role;
    private List<Team> teams = new ArrayList<>();

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public void addTeam(Team team) {
        // TODO: Add team to employee's team list
        teams.add(team);
    }

    public void removeTeam(Team team) {
        // TODO: Remove team from employee's team list
        teams.remove(team);
    }

    public List<String> getTeamNames() {
        // TODO: Return list of team names this employee belongs to
        List<String> listOfTeamNames = new ArrayList<>();

        for (Team team : teams) {
            listOfTeamNames.add(team.getName());
        }

        return listOfTeamNames;
    }

    public String getName() { return name; }
    public String getRole() { return role; }
}

class Team {
    private String name;
    private List<Employee> members = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public void addMember(Employee employee) {
        // TODO: Add employee to team and register this team on the employee
        members.add(employee);
        employee.addTeam(this);
    }

    public void dissolve() {
        // TODO: Remove all members (notify employees to drop this team), don't destroy employees
        for (Employee employee : members) {
            employee.removeTeam(this);
        }
        members.clear();
    }

    public String getName() { return name; }
    public List<Employee> getMembers() { return members; }
    public int getMemberCount() { return members.size(); }
}

class Company {
    private String name;
    private List<Employee> employees = new ArrayList<>();
    private List<Team> teams = new ArrayList<>();

    public Company(String name) {
        this.name = name;
    }

    public void addEmployee(Employee employee) {
        // TODO: Add employee to company
        employees.add(employee);
    }

    public void addTeam(Team team) {
        // TODO: Add team to company
        teams.add(team);
    }

    public void dissolveTeam(Team team) {
        // TODO: Dissolve the team and remove it from the company's team list
        team.dissolve();
        teams.remove(team);
    }

    public String getName() { return name; }
    public int getEmployeeCount() { return employees.size(); }
    public int getTeamCount() { return teams.size(); }
}

public class Main {
    public static void main(String[] args) {
        Company company = new Company("TechCorp");

        Employee alice = new Employee("Alice", "Engineer");
        Employee bob = new Employee("Bob", "Designer");
        Employee charlie = new Employee("Charlie", "Engineer");

        company.addEmployee(alice);
        company.addEmployee(bob);
        company.addEmployee(charlie);

        Team backend = new Team("Backend");
        Team frontend = new Team("Frontend");

        company.addTeam(backend);
        company.addTeam(frontend);

        // Alice is on both teams
        backend.addMember(alice);
        backend.addMember(charlie);
        frontend.addMember(alice);
        frontend.addMember(bob);

        System.out.println("Before dissolving:");
        System.out.println("  " + alice.getName() + "'s teams: " + alice.getTeamNames());
        System.out.println("  Backend has " + backend.getMemberCount() + " members");
        System.out.println("  Company has " + company.getTeamCount() + " teams, "
            + company.getEmployeeCount() + " employees");

        company.dissolveTeam(backend);

        System.out.println("\nAfter dissolving Backend:");
        System.out.println("  " + alice.getName() + "'s teams: " + alice.getTeamNames());
        System.out.println("  " + charlie.getName() + "'s teams: " + charlie.getTeamNames());
        System.out.println("  Company has " + company.getTeamCount() + " teams, "
            + company.getEmployeeCount() + " employees");
        System.out.println("  " + alice.getName() + " still exists: " + alice.getRole());
    }
}
```

# Composition

Composition is a special type of association that signifies strong ownership between objects. The “whole” class is fully responsible for creating, managing, and destroying the “part” objects. In fact, the parts cannot exist without the whole.

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
| ------------------ | ------------------------ | ------------------------------ | --------------------------------- |
| Ownership          | None                     | Weak -- has-a                  | Strong -- owns-a                  |
| Lifecycle          | Independent              | Independent                    | Dependent -- part dies with whole |
| Tightness          | Loose coupling           | Moderate coupling              | Tight coupling                    |
| Multiplicity       | Flexible (1:1, 1:N, N:N) | Whole can group many parts     | Whole composed of integral parts  |
| Reusability        | High -- parts reusable   | Moderate -- parts often reused | Low -- parts not reused outside   |
| UML Symbol         | Solid Line               | Hollow Diamond (◊)            | Filled Diamond (◆)               |
| Who creates parts? | Either side or external  | External -- passed in          | Whole -- created internally       |
| Real Example       | Student ↔ Course        | Playlist → Song<br /><br />   | Order → LineItem                 |


### Think of it like this:
Association is a general connection: two classes simply know about each other.
Aggregation is a grouping: the whole and parts can exist independently.
Composition is an ownership: the part’s existence is bound to the whole.

### Exercise 1: Computer System
Design Computer System Class

Problem: Build a Computer that composes a CPU, RAM, and HardDrive. The computer creates these parts internally based on specs passed to its constructor. No component exists outside of a computer, and destroying the computer destroys all its components.

```java
class CPU {
    private String model;
    private int cores;

    public CPU(String model, int cores) {
        this.model = model;
        this.cores = cores;
    }

    public void describe() {
        // TODO: Print CPU model and core count
        System.out.println("  CPU: " + model + " (" + cores + " cores)");
    }
}

class RAM {
    private int sizeGB;

    public RAM(int sizeGB) {
        this.sizeGB = sizeGB;
    }

    public void describe() {
        // TODO: Print RAM size
        System.out.println("  RAM: " + sizeGB + " GB");
    }

    public int getSizeGB() { return sizeGB; }
}

class HardDrive {
    private int capacityGB;

    public HardDrive(int capacityGB) {
        this.capacityGB = capacityGB;
    }

    public void describe() {
        // TODO: Print hard drive capacity
        System.out.println("  Storage: " + capacityGB + " GB");
    }
}

class Computer {
    private String name;
    private CPU cpu;
    private RAM ram;
    private HardDrive hardDrive;

    public Computer(String name, String cpuModel, int cpuCores,
                    int ramGB, int storageGB) {
        this.name = name;
        // TODO: Create CPU, RAM, and HardDrive internally
        this.cpu = new CPU(cpuModel, cpuCores);
        this.ram = new RAM(ramGB);
        this.hardDrive = new HardDrive(storageGB);
    }

    public void describeSpecs() {
        // TODO: Print computer name and describe all components
        System.out.println("Computer: " + name);
        cpu.describe();
        ram.describe();
        hardDrive.describe();
    }

    public void upgradeRAM(int newSizeGB) {
        // TODO (Challenge): Replace RAM with a higher-capacity one
        this.ram = new RAM(newSizeGB);
    }
}

public class Main {
    public static void main(String[] args) {
        Computer pc = new Computer("Dev Workstation",
            "Intel i7-13700K", 16, 32, 1000);

        pc.describeSpecs();

        // Challenge: upgrade RAM and verify
        pc.upgradeRAM(64);
        System.out.println("\nAfter RAM upgrade:");
        pc.describeSpecs();

        // When pc is destroyed, all components are destroyed with it.
    }
}
```


### Exercise 2: Chat Conversation System
Design Chat Conversation System

Problem: A Conversation composes Message objects. Messages are created when sendMessage(sender, text) is called. Deleting a conversation deletes all its messages. Messages belong to exactly one conversation and have no meaning outside of it.


```java
import java.util.ArrayList;
import java.util.List;

class Message {
    private String sender;
    private String text;
    private long timestamp;

    public Message(String sender, String text) {
        this.sender = sender;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

    public void display() {
        // TODO: Print message in format "[sender]: text"
        System.out.println("[" + sender + "]: " + text);
    }

    public String getSender() { return sender; }
    public String getText() { return text; }
}

class Conversation {
    private String title;
    private List<Message> messages;

    public Conversation(String title) {
        this.title = title;
        this.messages = new ArrayList<>();
    }

    public void sendMessage(String sender, String text) {
        // TODO: Create a Message internally and add it to the list
        messages.add(new Message(sender, text));
    }

    public void printHistory() {
        // TODO: Print conversation title and all messages
        System.out.println("--- " + title + " ---");
        for (Message message : messages) {
            message.display();
        }

    }

    public void delete() {
        // TODO: Clear all messages (they are destroyed with the conversation)
        messages.clear();
    }

    public int getMessageCount() { return messages.size(); }
    public String getTitle() { return title; }

    public void forwardMessage(Conversation target, int messageIndex) {
        // TODO (Challenge): Copy message content into a NEW Message
        // in the target conversation. Don't move the original.
        Message copiedMessage = messages.get(messageIndex);
        String copiedMessageSender = copiedMessage.getSender();
        String copiedMessagetext = copiedMessage.getText();

        target.sendMessage(copiedMessageSender, copiedMessagetext);
        
    }
}

public class Main {
    public static void main(String[] args) {
        Conversation teamChat = new Conversation("Team Discussion");
        Conversation projectChat = new Conversation("Project Alpha");

        teamChat.sendMessage("Alice", "Hey team, standup in 5 minutes");
        teamChat.sendMessage("Bob", "Got it, joining now");
        teamChat.sendMessage("Alice", "Don't forget to update your tasks");

        projectChat.sendMessage("Charlie", "Deployment is scheduled for Friday");

        System.out.println("Before deletion:");
        teamChat.printHistory();
        System.out.println("Project chat has " + projectChat.getMessageCount() + " messages\n");

        // Challenge: forward a message
        teamChat.forwardMessage(projectChat, 2);
        System.out.println("After forwarding:");
        projectChat.printHistory();

        // Delete team chat - all its messages are destroyed
        teamChat.delete();
        System.out.println("\nAfter deleting team chat:");
        System.out.println("Team chat has " + teamChat.getMessageCount() + " messages");
        System.out.println("Project chat still has " + projectChat.getMessageCount() + " messages");
    }
}
```

# Dependency

A Dependency exists when one class relies on another to fulfill a responsibility, but does so without retaining a permanent reference to it.

This typically happens when:

A class accepts another class as a method parameter.
A class instantiates or uses another class inside a method.
A class returns an object of another class from a method.

### Key Characteristics of Dependency

Short-lived: The relationship exists only during method execution.
No ownership: The dependent class does not store the other as a field.
"Uses-a" relationship: The class uses another to accomplish a task, but does not retain it.

In UML class diagrams, dependency is represented by a dashed arrow (..>) pointing from the dependent class to the class it depends on. 


### Exercise 1: File Conversion Service
Design File Conversion Service

Problem: Build a FileConverter that converts files from one format to another. During the convert() method, it depends on a FileReader to read the source file, a FormatParser to parse the content, and a FileWriter to write the output. None of these are stored as fields.

```java
class FileReader {
    public String read(String filePath) {
        // TODO: Simulate reading a file and return its content
        System.out.println("Reading file: " + filePath);
        String content = "name,age,city";
        System.out.println("Content: " + content);
        return content;
    }
}

class FormatParser {
    public String parse(String content, String targetFormat) {
        // TODO: Simulate converting content to the target format
        System.out.println("Parsing content to " + targetFormat + " format");
        String parsed = "[{\"name\":\"Alice\",\"age\":30,\"city\":\"NYC\"}]";
        System.out.println("Parsed: " + parsed);
        return parsed;
    }
}

class FileWriter {
    public void write(String filePath, String content) {
        // TODO: Simulate writing content to a file
        System.out.println("Writing to file: " + filePath);
    }
}

class FileConverter {
    public void convert(String sourcePath, String targetPath, String targetFormat,
                        FileReader reader, FormatParser parser, FileWriter writer) {
        // TODO: Use reader to read, parser to parse, writer to write
        // Print each step so you can verify the flow
        
        String content = reader.read(sourcePath);
        String parsed = parser.parse(content, targetFormat);
        writer.write(targetPath, parsed);

        System.out.println("File conversion complete: " + sourcePath + " -> " + targetPath);

    }
}

public class Main {
    public static void main(String[] args) {
        FileConverter converter = new FileConverter();

        FileReader reader = new FileReader();
        FormatParser parser = new FormatParser();
        FileWriter writer = new FileWriter();

        converter.convert("data.csv", "output.json", "JSON", reader, parser, writer);
    }
}
```


### Exercise 2: Order Processing Pipeline
Design Order Processing Pipeline
Problem: Build an OrderProcessor that processes customer orders. During processOrder(), it depends on an InventoryChecker to verify stock, a PriceCalculator to compute the total, and an InvoiceGenerator to create the invoice. All dependencies come in as method parameters.


```java
class InventoryChecker {
    public boolean checkStock(String itemName, int quantity) {
        // TODO: Simulate checking inventory
        // Return true if available, false otherwise
        System.out.println("Checking stock for " + itemName + " (x"  + quantity +")");
        return quantity > 0;
    }
}

class PriceCalculator {
    public double calculate(String itemName, int quantity) {
        // TODO: Simulate calculating the price

        double price = 2499.98;
        System.out.println("Calculating price: " + itemName + " x " + quantity + " = $" + price);
        return price;
    }
}

class InvoiceGenerator {
    public String generate(String itemName, int quantity, double total) {
        // TODO: Simulate generating an invoice string
        System.out.println("Generating invoice...");
        System.out.println("--- INVOICE ---");
        System.out.println("Item: " + itemName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Total: $" + total);

        // System.out.println("--- END ---");


        return "--- END ---";
    }
}

class OrderProcessor {
    public String processOrder(String itemName, int quantity,
                               InventoryChecker checker, PriceCalculator calculator,
                               InvoiceGenerator generator) {
        // TODO: Check stock, calculate price, generate invoice
        // Return the invoice string, or an error message if out of stock

        boolean stockAvailable = checker.checkStock(itemName, quantity);
        if (stockAvailable){
            System.out.println("Stock available: " + stockAvailable);
            double total = calculator.calculate(itemName, quantity);
            return generator.generate(itemName, quantity, total);

        }
        return "Out of Stock";
    }
}

public class Main {
    public static void main(String[] args) {
        OrderProcessor processor = new OrderProcessor();

        InventoryChecker checker = new InventoryChecker();
        PriceCalculator calculator = new PriceCalculator();
        InvoiceGenerator generator = new InvoiceGenerator();

        String invoice = processor.processOrder("Laptop", 2,
                checker, calculator, generator);
        System.out.println(invoice);
    }
}
```

# Realization

Realization represents a contract fulfillment relationship. Think of it as a promise: the interface declares "these methods must exist," and the implementing class promises to provide them.

The relationship works like this:

An interface defines what must be done (the contract)
A class implements how it's done (the fulfillment)
The implementing class must provide all methods declared in the interface
Multiple classes can realize the same interface differently

Real-world analogy

Think of a job description. The job description (interface) defines what skills and responsibilities are required. Different employees (classes) can fulfill that job description in their own way, but they all meet the requirements.


In UML class diagrams, realization is represented by a dashed line with a hollow (unfilled) triangle pointing to the interface.


**Inheritance models identity where as Realization models capability**


### Exercise 1: Shape Drawing System
Design Shape Drawing System
Problem: Build a drawing system where different shapes implement a common Drawable interface. A Canvas class renders all shapes through the interface without knowing the concrete types.

```java
import java.util.List;

interface Drawable {
    void draw();
    double getArea();
}

class Circle implements Drawable {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    public void draw() {
        // TODO: print "Drawing circle with radius X"
        System.out.println("Drawing circle with radius " + radius);
    }

    public double getArea() {
        // TODO: return π * r²
        return Math.PI * radius * radius;
    }
}

class Rectangle implements Drawable {
    private double width;
    private double height;

    public Rectangle(double width, double height) {
        this.width = width;
        this.height = height;
    }

    public void draw() {
        // TODO: print "Drawing rectangle WxH"
        System.out.println("Drawing rectangle " + width + "x" + height);
    }

    public double getArea() {
        // TODO: return width * height
        return width * height;
    }
}

class Triangle implements Drawable {
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    public void draw() {
        // TODO: print "Drawing triangle with base B and height H"
        System.out.println("Drawing triangle with base " + base + " and height " + height);
    }

    public double getArea() {
        // TODO: return 0.5 * base * height
        return 0.5 * base * height;
    }
}

class Canvas {
    public void drawAll(List<Drawable> shapes) {
        // TODO: For each shape, call draw() and print its area
        for (Drawable drawable : shapes) {
            drawable.draw();
            System.out.printf("  Area: %.2f%n%n", drawable.getArea());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        List<Drawable> shapes = List.of(
                new Circle(5.0),
                new Rectangle(4.0, 6.0),
                new Triangle(3.0, 8.0)
        );

        canvas.drawAll(shapes);
    }
}

```

### Exercise 2: Plugin System
Design Plugin System

Problem: Build a text editor plugin system. A Plugin interface defines what every plugin can do, and a TextEditor loads and runs plugins through the interface without knowing their concrete types.

```java
import java.util.List;
import java.util.ArrayList;

interface Plugin {
    String execute(String text);
    String getName();
}

class SpellCheckPlugin implements Plugin {
    public String execute(String text) {
        // TODO: replace "teh" with "the", "adn" with "and"
        return text.replace("teh", "the").replace("adn", "and");
    }

    public String getName() {
        return "Spell Check";
    }
}

class WordCountPlugin implements Plugin {
    public String execute(String text) {
        // TODO: append "\n[Word count: X]" to the text
        int wordCount = text.trim().split("\\s+").length;
        text = text.concat("\n[Word count: " + wordCount + "]");
        return text;
    }

    public String getName() {
        return "Word Count";
    }
}

class UpperCasePlugin implements Plugin {
    public String execute(String text) {
        // TODO: return text.toUpperCase()
        return text.toUpperCase();
    }

    public String getName() {
        return "Upper Case";
    }
}

class TextEditor {
    private List<Plugin> plugins = new ArrayList<>();

    public void registerPlugin(Plugin plugin) {
        // TODO: Add the plugin to the list and print "Registered: [name]"
        plugins.add(plugin);
        System.out.println("Registered: " + plugin.getName());
    }

    public String runPlugins(String text) {
        // TODO: Run each plugin in sequence, passing output of one as input to the next
        // Print "Running: [name]" before each plugin
        // Return the final processed text
        String finalProcessed = text;
        for (Plugin plugin : plugins) {
            System.out.println("Running: " + plugin.getName());
            finalProcessed = plugin.execute(finalProcessed);
        }
        return finalProcessed;
    }
}

public class Main {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        editor.registerPlugin(new SpellCheckPlugin());
        editor.registerPlugin(new WordCountPlugin());

        String result = editor.runPlugins("teh quick brown fox adn teh lazy dog");
        System.out.println("\nFinal output: " + result);
    }
}
```






















































