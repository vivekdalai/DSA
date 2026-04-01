# Linked List / Queue / Priority Queue Notes

## 03 - Greatest Smaller Element on the Left

**Generated on:** 2026-03-31 19:21:58 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

For every element in the array, find the greatest element on its left that is strictly smaller.

Example:
- `arr = [4, 5, 2, 10, 8]`
- result = `[-1, 4, -1, 5, 5]`

Important clarification:
- The PDF heading says "next smaller element to the left"
- But the provided implementation uses `TreeSet.lower(x)`
- That computes the **greatest smaller value seen so far on the left**
- It does **not** compute the nearest smaller element by index

------------------------------------------------------------------------

## 🪜 2. Core Idea

Maintain a `TreeSet<Integer>` containing all values seen so far.

For current value `arr[i]`:
- `treeSet.lower(arr[i])` returns the greatest element strictly smaller than `arr[i]`
- if no such value exists, answer is `-1`
- then insert `arr[i]` into the set

Since `TreeSet` is balanced BST based, each operation is `O(log n)`.

------------------------------------------------------------------------

## 🔁 3. Step-by-Step Logic

For each index `i`:
- query `lower(arr[i])`
- store `-1` if result is `null`
- add `arr[i]` to the set

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.TreeSet;

class GreatestSmallerOnLeft {
    public int[] solve(int[] arr) {
        int n = arr.length;
        int[] result = new int[n];
        TreeSet<Integer> treeSet = new TreeSet<>();

        for (int i = 0; i < n; i++) {
            Integer lower = treeSet.lower(arr[i]);
            result[i] = (lower == null) ? -1 : lower;
            treeSet.add(arr[i]);
        }

        return result;
    }
}
```

Complexity:
- Time: O(n log n)
- Space: O(n)

------------------------------------------------------------------------

## 🔎 5. Dry Run / Example

For `arr = [4, 5, 2, 10, 8]`:

- `4`
  - left values = `{}`
  - no smaller -> `-1`
  - add `4`
- `5`
  - left values = `{4}`
  - greatest smaller = `4`
  - add `5`
- `2`
  - left values = `{4, 5}`
  - no smaller -> `-1`
  - add `2`
- `10`
  - left values = `{2, 4, 5}`
  - greatest smaller = `5`
  - add `10`
- `8`
  - left values = `{2, 4, 5, 10}`
  - greatest smaller = `5`

Answer:
- `[-1, 4, -1, 5, 5]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this pattern when:
- you need an ordered view of previously seen elements
- you need predecessor/successor queries
- the question asks for nearest value by magnitude, not by index

Related tools:
- `TreeSet.lower(x)` -> greatest element strictly less than `x`
- `TreeSet.higher(x)` -> smallest element strictly greater than `x`

If the question asks for:
- nearest smaller to left by index
- nearest greater/smaller problems
then a **monotonic stack** is usually the right pattern.

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty array -> return empty result
- Duplicates:
  - `TreeSet` stores unique values only
  - this is fine for predecessor lookup by value
- Do not confuse:
  - greatest smaller value on the left
  - nearest smaller element on the left
- If actual requirement is nearest by position, `TreeSet` is the wrong tool

------------------------------------------------------------------------

## ✅ 8. Takeaway

- `TreeSet.lower(x)` gives predecessor by value.
- This solves "greatest smaller seen so far on the left".
- Complexity is `O(n log n)`.
- For nearest-smaller-by-index questions, switch to monotonic stack.

------------------------------------------------------------------------

# End of Notes
