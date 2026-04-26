# String

- Strings in Java are immutable, which means once a string is created, it cannot be changed. 

```java
String original = "Hello";
original = original + " World!"; // Creates a new string instead of modifying 'original'
System.out.println(original); // Output: Hello World!
```

## Why Immutability Matters

Immutability provides several benefits:

1. Thread Safety: Since strings can't be altered, they can be safely shared between threads without synchronization issues.
2. Performance Optimization: The Java Virtual Machine (JVM) can optimize memory usage with immutable objects.
3. Java uses a technique called string interning. If two identical string literals exist, Java will store just one instance in memory, reducing memory usage.
4. Hashing: Strings can be used as keys in a hash table without worrying about their values changing.
5. Enhanced Security
6. Java keeps a single instance of each unique string in the string pool.

Point 6 example
```java
String str1 = "Java";
String str2 = "Java"; // No new object is created
System.out.println(str1 == str2); // Output: true
```

However, this characteristic can lead to inefficiencies when performing numerous modifications. That’s where StringBuilder and StringBuffer come into play.

## String Interpolation

1. Using **String.format()**

```java
String name = "Alice";
int age = 30;
String formattedString = String.format("%s is %d years old.", name, age);
System.out.println(formattedString); // Output: Alice is 30 years old.
```

2. Using **String.join()**

```java
String[] elements = {"Java", "Python", "C++"};
String joined = String.join(", ", elements);
System.out.println(joined); // Output: Java, Python, C++
```

## String Comparison

1. The == operator checks for reference equality, meaning it checks if both variables point to the same object in memory.
2. The .equals() method checks for value equality, meaning it checks if the values of the strings are the same.


```java
String str1 = "Hello";
String str2 = new String("Hello");

System.out.println(str1 == str2); // Output: false
System.out.println(str1.equals(str2)); // Output: true
```

# StringBuiilder
- Mutable
- Performance: More efficient for frequent modifications
- NOT synchronized
- NOT thread safe
- Used in Single-threaded environment

# StringBuffer
- Mutable
- Synchronized
- Thread-safe
- Used in parallel-threaded environment






























