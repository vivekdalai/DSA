# Introduction
- Java is a high-level, object-oriented programming language designed to be platform-independent. This means you can write your code once and run it on any device that supports Java, thanks to its principle of “write once, run anywhere” (WORA).
- platform independence is achieved through the Java Virtual Machine (JVM), which interprets compiled Java bytecode
- C/C++ --> compiled directly to machine code
- Characteristics of JAVA
  - Object Oriented
  - Platform-Independent
  - Robust and Secure
  - Multithreaded
  - Rich Standard Library
# Real-World Applications of Java
- Web Applications
- Mobile Applications --> Android Development
- Enterprise Applications --> Large organization used Java EE 
- Embedded Systems --> Smart Appliances

# Libraries and Frameworks
Java has a rich ecosystem of libraries and frameworks that enhance its capabilities:

- Spring: A comprehensive framework for building enterprise applications. 
- Hibernate: An ORM (Object-Relational Mapping) tool for database interactions. 
- Apache Maven: A build automation tool that simplifies project management.

# Features of Java
- OOP
  - Encapsulation
  - Inheritance
  - Polymorphism
- Platform Independence
- Automatic Memory Management
  - Java uses automatic memory management, primarily through its Garbage Collector (GC). 
  - This means that you don’t have to manually allocate and deallocate memory, which reduces the chances of memory leaks and other related issues.
  - While Java's garbage collector is very effective, it's still good practice to nullify references when you are done with an object, especially for large objects.
For eg:
```java
public class Example {
    public static void main(String[] args) {
        String str = new String("Hello");
        str = null; // The object is now eligible for garbage collection
    }
}
```
- Rich Standard Library
  - Data Structures
  - Networking
  - File I/O
  - Concurrency
- Multithreading
  - While multithreading offers powerful capabilities, it also introduces complexity, such as race conditions and deadlocks. Always ensure proper synchronization when multiple threads access shared resources.
- Exception Handling
For eg:
```java
public class ExceptionExample {
    public static void main(String[] args) {
        try {
            int result = 10 / 0; // This will throw an ArithmeticException
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide by zero: " + e.getMessage());
        } finally {
            System.out.println("This block always executes.");
        }
    }
}
```

# Java Development Kit (JDK)

- The Java Development Kit (JDK) is a comprehensive package that provides tools and resources needed to develop Java applications. 
- Think of it as the toolbox for Java developers, containing everything from compilers to debuggers

**Components of the JDK**
- Java Compiler 
  - Java Compiler (javac): This tool converts Java source code (files ending in .java) into bytecode (files ending in .class).
  - Bytecode is platform-independent, allowing Java to run on any device that has a JVM.
- Java Runtime Environment (JRE)
  - The JRE is part of the JDK. It provides the libraries and the JVM required to run Java applications.
  - Without the JRE, your Java code would not execute, even if it was properly compiled.
- Development Tools
  - The JDK includes various utilities like javadoc for generating documentation, jar for packaging applications, and jdb for debugging.
  
**Why use JDK?**
- compile and package code for distribution.

# Java Runtime Environment (JRE)

- The Java Runtime Environment (JRE) is the part of the Java software platform that allows you to run Java applications. 
- It provides the necessary libraries and components needed for the execution of Java bytecode.


**Components of the JRE**
- Java Virtual Machine (JVM)
  - The core of the JRE, the JVM is responsible for interpreting bytecode and executing it on the host machine. 
  - It provides a runtime environment where Java applications can run.
- Set of Libraries
  - The JRE includes a set of standard libraries that provide necessary functionalities, such as I/O operations, networking, and user interface design.

# JRE vs. JDK

- It’s important to note that while the JRE allows you to run Java applications, it does not include development tools. 
- Therefore, if you only need to run Java applications, you can install the JRE without the full JDK. 
- For instance, if you want to run a Java application on a server, you would typically install the JRE. 
- This is particularly useful in production environments where you do not need to develop or compile code.

- To develop/code and run application -> JDK (Javac, JRE, Dev Tools included)
- To run application only -> JRE (JVM, Libraries included)

# Java Virtual Machine (JVM)
- The Java Virtual Machine (JVM) is an abstract computing machine that enables a computer to run Java programs.
- The JVM is a critical component of the JRE and is responsible for converting Java bytecode into machine code.
 
**How JVM works**
* Class Loader: The class loader loads Java classes into the JVM. It reads the compiled bytecode and prepares it for execution.
* Execution Engine: This component executes the bytecode. It can use two approaches:
  * Interpreter: Executes the code line-by-line.
  * Just-In-Time (JIT) Compiler: Compiles bytecode into native code for better performance. Converts to application-leve code.
* Garbage Collector: The JVM manages memory through garbage collection, which automatically frees up memory that is no longer in use. This helps prevent memory leaks and improves application performance.


**Why is the JVM Important?**
- The JVM provides platform independence, which is one of Java’s major selling points. 
- Write your code once, and it can run anywhere that has a JVM. 
- This is made possible because the JVM abstracts the underlying hardware and operating system.

# Full flow 
1. You write your code using the JDK.
2. The javac compiler in the JDK compiles your code into bytecode.
3. When you run your application, the JRE is invoked, which includes the JVM.
4. The JVM executes the bytecode, converting it into machine code that your computer can understand.

# Common Misconceptions
1. **JDK includes JRE**: Many new developers think the JRE is separate from the JDK. While it is technically a part of the JDK, you can install the JRE independently for running applications.
2. **JVM is platform-dependent**: While the JVM is responsible for executing bytecode, it is designed to be platform-independent. Each OS has its own JVM implementation, but they all perform the same function.
3. **JDK is only for development**: Although primarily used for development, the JDK also contains tools for monitoring and performance analysis, which can be beneficial in production environments.

# Why we configure Environment Variables?
- Setting up your environment variables is an important step that allows your system to recognize the Java commands globally. 
- This means you can compile and run Java programs from any directory.
- No need hardcode environment variables/metadata.

# How Java Works

## The Role of the JVM

1. Class Loader -> responsible for loading .class file. Verifies the bytecode and prepares if for execution. 
   - The class loader follows a hierarchical structure: 
      - Bootstrap Class Loader: Loads core Java classes from the JRE.
      - Extension Class loader: Loads classes from the extension directories.
      - Application Class Loader: Loads classes from the application's classpath.
2. ByteCode Verification
   - JVM conducts a verification process
   - Ensures tha the bytcode adheres to Java's safety and security constraints
   - Checks access violations and ensures that the bytecode doesn't perform illegal operations.
3. Execution Begins
   - Interpreter
     - JVM interpret the bytecode line by line.
     - Straightforward but can be SLOW
   - Just-in-time (JIT) Compilation
     - Converts bytecode into native machine code at runtime. (performance improved)
     - Machine code is executed directly by the CPU. FASTER

## Java Memory Management

- Critical aspect of Java
- Can signifcanlty affect performance
- Automatically manages memory -> Garbage Collection

1. Memory Areas in Java
   - Heap
     - Class Instances and Arrays are allocated
     - Shared among all threads
   - Stack
     - Each thread has its own stack that stores local variables and method call information
     - New method called --> a block is pushed into the stack
     - Method returns --> Popped up from the stack
   - Method Area
     - Stores class-level information, such as metadata, constants and static variables.
2. Garbage Collection --> process of identifying and reclaiming memory that is no longer in use
   - GC algorithms used in JAVA
     - Serial GC
       - Designed for single-threaded environment
       - Uses a single thread for GC
     - Parallel GC
       - Uses multiple threads for minor garbage collection
       - improving efficiency on multi-core processors
     - G1 GC
       - Default collector in newer Java versions.
       - Divides heap into regions
       - perform garbage collection in parallel into the heap region






































