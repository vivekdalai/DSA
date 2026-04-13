# String compareTo in Java

## Overview
The `String` class in Java implements `Comparable<String>`, providing a natural lexicographical ordering based on Unicode values. The `compareTo` method compares strings character by character.

## Method Signature
```java
public int compareTo(String anotherString)
```

## Return Value
- **Negative integer**: This string comes before the other string lexicographically
- **Zero**: The strings are equal
- **Positive integer**: This string comes after the other string lexicographically

## Comparison Rules
1. Strings are compared character by character
2. Comparison is case-sensitive ('A' < 'a')
3. Shorter strings that match the prefix of longer strings come first
4. Unicode values determine character ordering

## Examples
```java
"apple".compareTo("banana")  // negative (-1) - "apple" < "banana"
"banana".compareTo("apple")  // positive (1) - "banana" > "apple"
"apple".compareTo("apple")   // zero (0) - strings are equal
"app".compareTo("apple")     // negative (-'l') - "app" < "apple"
"Apple".compareTo("apple")   // negative (-32) - 'A' < 'a'
```

## Sorting Implications
The return value determines the order in sorted collections:

- **Negative return**: `this` string comes **before** `other` in ascending order
- **Positive return**: `this` string comes **after** `other` in ascending order
- **Zero return**: strings are considered equal for sorting

**Example**: `"banana".compareTo("apple")` returns positive (1), meaning "banana" comes **after** "apple" in ascending alphabetical order.

## Custom String Comparison for Concatenation Ordering

In problems like forming the largest number from array of strings, we need to sort strings based on concatenation result rather than lexicographical order.

### Example: Pair Class for Custom Ordering
```java
class Pair implements Comparable<Pair> {
    String str;
    Pair(String str) {
        this.str = str;
    }

    @Override
    public int compareTo(Pair other) {
        // Compare based on concatenation: this + other vs other + this
        // For largest number: want "9" + "8" > "8" + "9"
        String concat1 = this.str + other.str;
        String concat2 = other.str + this.str;
        return concat2.compareTo(concat1); // Reverse for descending
    }
}
```

### Explanation
- `concat1 = this.str + other.str`
- `concat2 = other.str + this.str`
- `concat2.compareTo(concat1)` returns positive if `other + this > this + other`
- This sorts in descending concatenation order (largest first)

### Alternative Implementation
```java
@Override
public int compareTo(Pair other) {
    return (other.str + this.str).compareTo(this.str + other.str);
}
```

This achieves the same result: strings are ordered such that when concatenated in this order, they form the largest possible number.

## Common Use Cases
1. **Lexicographical sorting**: Default `String` comparison
2. **Case-insensitive sorting**: Use `compareToIgnoreCase()`
3. **Custom concatenation-based sorting**: For number formation problems
4. **Reverse lexicographical**: Use `Collections.reverseOrder()`

### Collections.reverseOrder()
`Collections.reverseOrder()` returns a `Comparator` that reverses the natural ordering:

```java
List<String> fruits = Arrays.asList("apple", "Banana", "cherry");
Collections.sort(fruits, Collections.reverseOrder());
// Result: ["cherry", "apple", "Banana"] (descending order)

List<String> fruitsAsc = Arrays.asList("apple", "Banana", "cherry");
Collections.sort(fruitsAsc); // natural order
// Result: ["Banana", "apple", "cherry"] (ascending order)
```

**Key Points:**
- Works with any `Comparable` type
- Returns a `Comparator<T>` that imposes reverse natural ordering
- Useful for descending sorts without custom comparators
- Can be combined with other comparators using `thenComparing()`

## Performance Notes
- `compareTo` stops at the first differing character
- Time complexity: O(min(length1, length2))
- Memory efficient for large strings