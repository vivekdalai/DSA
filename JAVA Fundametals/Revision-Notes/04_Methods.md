# Remember
- Java uses pass-by-value, meaning that it passes copies of the variables to methods.

# Varargs

```java
public void printNumbers(int... numbers) {
    for (int number : numbers) {
        System.out.println(number);
    }
}
```

**The varargs parameter must be the last parameter in the method signature.**
**You can only have one varargs parameter in a method.**


# Varargs vs. Arrays

The key difference lies in ease of use. With varargs, the caller can pass arguments directly, while with arrays, you need to create an array before passing it.