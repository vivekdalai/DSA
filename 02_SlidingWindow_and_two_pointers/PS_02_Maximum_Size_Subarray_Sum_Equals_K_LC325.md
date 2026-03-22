# Prefix Sum / HashMap Notes

## 02 - Maximum Size Subarray Sum Equals K (LC 325)

**Generated on:** 2026-03-23 00:59:04 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums` and an integer `k`, return the length of the longest subarray whose sum equals `k`.

Examples:
- `nums = [1,-1,5,-2,3], k = 3` → `4`
- `nums = [-2,-1,2,1], k = 1` → `2`

Important:
- Array can contain negative numbers
- Sliding window does not work
- This is also a prefix sum + hashmap problem

Difference from LC 560:
- LC 560 asks for **count**
- LC 325 asks for **maximum length**

------------------------------------------------------------------------

## 🪜 2. Core Idea

At current index `i`, let:
- running prefix sum = `sum`

We want a previous prefix sum `prevSum` such that:

`sum - prevSum = k`

So:
- `prevSum = sum - k`

If such a prefix sum occurred first at index `j`,
then subarray `(j+1 ... i)` has sum `k`
and its length is:

`i - j`

To maximize length:
- store the **first occurrence** of each prefix sum

Why first occurrence?
- earliest index gives the longest subarray ending at current index

------------------------------------------------------------------------

## 🔁 3. Steps

1. Initialize map:
   - `map.put(0, -1)`
   - means prefix sum 0 occurs before array starts
2. Traverse array
3. Keep running sum
4. If `(sum - k)` exists in map:
   - compute length = `i - map.get(sum - k)`
   - update max length
5. If current `sum` is not already in map:
   - store its first occurrence index

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class MaximumSizeSubarraySumEqualsK {
    public int maxSubArrayLen(int[] nums, int k) {
        Map<Integer, Integer> firstIndex = new HashMap<>();
        firstIndex.put(0, -1);

        int sum = 0;
        int maxLen = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];

            if (firstIndex.containsKey(sum - k)) {
                maxLen = Math.max(maxLen, i - firstIndex.get(sum - k));
            }

            // store only first occurrence
            firstIndex.putIfAbsent(sum, i);
        }

        return maxLen;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`nums = [1,-1,5,-2,3], k = 3`

Start:
- map = `{0:-1}`
- sum = 0
- maxLen = 0

Index 0:
- num = 1, sum = 1
- need `sum-k = -2` → not found
- store `1 -> 0`

Index 1:
- num = -1, sum = 0
- need `sum-k = -3` → not found
- do not overwrite `0 -> -1`

Index 2:
- num = 5, sum = 5
- need `sum-k = 2` → not found
- store `5 -> 2`

Index 3:
- num = -2, sum = 3
- need `sum-k = 0` → found at `-1`
- length = `3 - (-1) = 4`
- maxLen = 4

Index 4:
- num = 3, sum = 6
- need `sum-k = 3` → found at `3`
- length = `4 - 3 = 1`

Answer = `4`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- longest / maximum length subarray with sum = k
- negative numbers are allowed
- sliding window fails

Standard pattern:
- **prefix sum + hashmap of first occurrence**

Compare:
- LC 560 → store frequency
- LC 325 → store first index

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Must initialize `map.put(0, -1)`
  - handles subarrays starting from index 0
- Store first occurrence only
  - later occurrences are useless for maximizing length
- Works with positive, negative, and zero values
- Don’t overwrite an existing prefix sum index

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Running sum at index `i` = `sum`
- Need previous prefix sum = `sum - k`
- For max length, store the earliest index of each prefix sum
- Prefix sum + first occurrence map is the standard pattern here

------------------------------------------------------------------------

# End of Notes
