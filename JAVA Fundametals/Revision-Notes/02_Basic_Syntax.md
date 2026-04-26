# Variables
1. Local Variables : declared within a method or block
2. Instance Variables: Declared within a class but outside any method
3. Static Variables: Declared with **static** keyword. Can be accessed without creating an object of the class.

# Data Types
1. Primitive Types -> char, boolean, int, double
2. Reference Types (Stores reference to objects) -> String, Class, Arrays, Interface

# Few data types with their size
1. byte: 8-bit signed integer (Ranges from -128 to 127)
2. short: 16-bit signed integer (Ranges from -32,768 to 32,767)
3. int: 32-bit signed integer (-2,147,483,648 to 2,147,483,647)
4. long: 64-bit signed integer (-9,223,372,036,854,775,808 to 9,223,372,036,854,775,807)
5. float: 32-bit floating-point
6. double: 64-bit floating-point
7. char: 16-bit Unicode character
8. boolean: Represents true or false

**Initializing variables**
```java
public class IntegerExample {
    public static void main(String[] args) {
        byte b = 100; // Good for small numbers
        short s = 10000; // A little larger
        int i = 100000; // Default choice for integers
        long l = 10000000000L; // Use 'L' for long literals
        float f = 5.75f; // Use 'f' for float literals
        double d = 19.99; // No suffix needed for double
        
        System.out.println("Byte: " + b);
        System.out.println("Short: " + s);
        System.out.println("Integer: " + i);
        System.out.println("Long: " + l);
        System.out.println("Float: " + f);
        System.out.println("Double: " + d);
    }
}
```

- Primitive types are generally more efficient than their wrapper types, especially in performance-critical applications.

# Type Casting
- Implicit --> small to large (no data loss -> safe)
- Explicit --> large to small (data loss may occur)


## Type Casting with Objects

- Upcasting
  - Subclass Object is treated as an object of its superclass.
  - Safe
  - Doesn't require explicit casting
- Downcasting
  - Treating a superclass reference as a subclass reference
  - Requires explicit casting
  - Can lead to ClassCastException (If the object is NOT actually an instance of the subclass)

Example:
```java
class Animal {
    void makeSound() {
        System.out.println("Animal sound");
    }
}

class Dog extends Animal {
    void makeSound() {
        System.out.println("Bark");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal myDog = new Dog(); // Upcasting
        myDog.makeSound(); // Outputs: Bark

        Dog dog = (Dog) myDog; // Downcasting
        dog.makeSound(); // Outputs: Bark
    }
}
```

```java
Animal animal = new Animal();
Dog dog = (Dog) animal; // This will throw ClassCastException
```

```java
if (myDog instanceof Dog) {
    Dog dog = (Dog) myDog; // Safe downcasting
}
```
## Practical Applications of Type Casting

1. Example: Handling User Input
```java
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter your age: ");
        String ageInput = scanner.nextLine(); // Read input as a String
        int age = Integer.parseInt(ageInput); // Convert to int

        System.out.println("Your age is: " + age);
    }
}
```
2. Example: Data Serialization

```java
Object data = readData(); // Assume this returns an Object

if (data instanceof String) {
    String stringData = (String) data; // Downcast safely
    System.out.println("Data: " + stringData);
}
```

# Bitwise Operators
Bitwise operators work on binary numbers at the bit level. They include:

1. AND (&)
2. OR (|)
3. XOR (^)
4. Complement (~)
5. Left shift (<<)
6. Right shift (>>)
7. Unsigned right shift (>>>)

**Example: Working with Bits**
Here's a quick example of how bitwise operators work:
```java
public class BitwiseExample {
    public static void main(String[] args) {
        int a = 5;  // 0101 in binary
        int b = 3;  // 0011 in binary

        System.out.println("a & b: " + (a & b)); // 1  (0001 in binary)
        System.out.println("a | b: " + (a | b)); // 7  (0111 in binary)
        System.out.println("a ^ b: " + (a ^ b)); // 6  (0110 in binary)
        System.out.println("~a: " + (~a));       // -6 (inverts all bits)
        System.out.println("a << 1: " + (a << 1)); // 10 (1010 in binary)
        System.out.println("a >> 1: " + (a >> 1)); // 2  (0010 in binary)
    }
}
```

# File Input and Output

1. Writing to a File
```java
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class FileWriteExample {
    public static void main(String[] args) {
        String filename = "output.txt";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Hello, world!");
            writer.newLine(); // Move to the next line
            writer.write("Welcome to file writing in Java.");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
        }
    }
}
```
2. Reading from a File
```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReadExample {
    public static void main(String[] args) {
        String filename = "output.txt";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print each line read
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the file.");
        }
    }
}
```

# Labeled Statements

- Labeled statements in Java allow you to create a label for a block of code. 
- This label can then be used with break or continue statements to control the flow of your program more precisely.

```java

outerLoop: for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (j == 1) {
            break outerLoop; // Exits the outer loop
        }
        System.out.println("i: " + i + ", j: " + j);
    }
}
```





























