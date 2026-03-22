# Prefix Sum / HashMap Notes

## 01 - Subarray Sum Equals K (LC 560)

**Generated on:** 2026-03-23 00:58:37 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums` and an integer `k`, return the total number of subarrays whose sum equals `k`.

Examples:
- `nums = [1,1,1], k = 2` → `2`
- `nums = [1,2,3], k = 3` → `2`

Important:
- Array may contain negative numbers.
- Because of negative values, standard sliding window does **not** work.

This is a prefix sum + hashmap problem.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Let:
- `prefixSum[i]` = sum of elements from index `0` to `i`

Suppose at current index:
- current prefix sum = `sum`

We want some earlier prefix sum `prevSum` such that:

`sum - prevSum = k`

So:
- `prevSum = sum - k`

Meaning:
- if we have seen prefix sum `(sum - k)` before,
- then every occurrence of it forms a valid subarray ending at current index.

So we store:
- `map[prefixSum] = frequency of this prefix sum seen so far`

------------------------------------------------------------------------

## 🔁 3. Steps

1. Initialize `map` with:
   - `map.put(0, 1)`
   - meaning prefix sum 0 has appeared once before processing starts
2. Traverse array
3. Update running sum
4. Check if `(sum - k)` exists in map
   - if yes, add its frequency to answer
5. Store/update current `sum` in map

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class SubarraySumEqualsK {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        freq.put(0, 1);

        int sum = 0;
        int count = 0;

        for (int num : nums) {
            sum += num;

            if (freq.containsKey(sum - k)) {
                count += freq.get(sum - k);
            }

            freq.put(sum, freq.getOrDefault(sum, 0) + 1);
        }

        return count;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`nums = [1,1,1], k = 2`

Start:
- map = `{0:1}`
- sum = 0
- count = 0

Step 1: num = 1
- sum = 1
- need `sum - k = -1` → not found
- add `1` to map
- map = `{0:1, 1:1}`

Step 2: num = 1
- sum = 2
- need `sum - k = 0` → found once
- count = 1
- add `2` to map
- map = `{0:1, 1:1, 2:1}`

Step 3: num = 1
- sum = 3
- need `sum - k = 1` → found once
- count = 2
- add `3` to map

Answer = `2`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- subarray sum equals k
- array can contain negative numbers
- sliding window fails due to non-monotonic sums

This is the standard:
- **prefix sum + hashmap frequency**

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Must initialize `map.put(0, 1)`
  - this handles subarrays starting from index 0
- Store frequency, not just existence
  - because same prefix sum can occur multiple times
- Works with negative, zero, positive values
- Don’t update the map before checking `(sum - k)`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Running sum at current index = `sum`
- Need previous prefix sum = `sum - k`
- Add frequency of `(sum - k)` to answer
- Prefix sum + hashmap is the go-to pattern for exact subarray sum with negatives

------------------------------------------------------------------------

# End of Notes
