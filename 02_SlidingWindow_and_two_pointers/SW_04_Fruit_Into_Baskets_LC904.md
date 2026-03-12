# Sliding Window / Two Pointers Notes

## 04 - Fruit Into Baskets (LC 904)

**Generated on:** 2026-03-13 02:33:25 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given an integer array `fruits` where `fruits[i]` is the type of fruit on the `i-th` tree.

Rules:
- You have only 2 baskets.
- Each basket can hold only one fruit type.
- You can pick exactly one fruit from each tree while moving only to the right.
- Once you start, you must keep picking from consecutive trees until you cannot.

Goal:
- Return the maximum number of fruits you can collect.

Equivalent interpretation:
- Find the longest subarray containing at most 2 distinct values.

Examples:
- `fruits = [1,2,1]` → `3`
- `fruits = [0,1,2,2]` → `3`
- `fruits = [1,2,3,2,2]` → `4`

------------------------------------------------------------------------

## 🪜 2. Core Sliding Window Idea

This is exactly:
- **Longest subarray with at most 2 distinct elements**

Maintain a window `[left ... right]` such that:
- it contains at most 2 fruit types

Use:
- `Map<Integer, Integer>` to store frequency of fruit types in current window

Steps:
1. Expand `right`
2. Add current fruit to map
3. If map size becomes greater than 2, shrink from the left
4. Track maximum valid window length

------------------------------------------------------------------------

## 🔁 3. Window Invariant

After shrinking:
- `freqMap.size() <= 2`

Valid window length:
- `right - left + 1`

Update:
- `maxWindow = max(maxWindow, right - left + 1)`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class FruitIntoBaskets {
    public int totalFruit(int[] fruits) {
        Map<Integer, Integer> freq = new HashMap<>();
        int left = 0;
        int maxWindow = 0;

        for (int right = 0; right < fruits.length; right++) {
            int fruit = fruits[right];
            freq.put(fruit, freq.getOrDefault(fruit, 0) + 1);

            while (freq.size() > 2) {
                int leftFruit = fruits[left];
                freq.put(leftFruit, freq.get(leftFruit) - 1);
                if (freq.get(leftFruit) == 0) {
                    freq.remove(leftFruit);
                }
                left++;
            }

            maxWindow = Math.max(maxWindow, right - left + 1);
        }

        return maxWindow;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1) effectively, since map holds at most 3 fruit types briefly and then shrinks

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`fruits = [1,2,3,2,2]`

- `right = 0`: window = `[1]`, types = {1}, len = 1
- `right = 1`: window = `[1,2]`, types = {1,2}, len = 2
- `right = 2`: window = `[1,2,3]`, types = {1,2,3}, invalid
  - shrink:
    - remove `1`
    - window becomes `[2,3]`, types = {2,3}
- `right = 3`: window = `[2,3,2]`, len = 3
- `right = 4`: window = `[2,3,2,2]`, len = 4

Answer = `4`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is a direct application of:
- **Longest subarray with at most K distinct elements**
- Here `K = 2`

If you understand:
- longest substring with at most K distinct characters

then Fruit Into Baskets is the exact same pattern on arrays.

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Array length 0 → answer `0`
- If all fruits are same type → answer is full array length
- Remove fruit type from map when frequency becomes 0
- Don’t overthink baskets; it reduces directly to “at most 2 distinct”

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Fruit Into Baskets = longest subarray with at most 2 distinct values
- Use frequency map + sliding window
- Expand right, shrink left when distinct count exceeds 2
- This is one of the most common sliding window pattern conversions

------------------------------------------------------------------------

# End of Notes
