# Prefix Sum Notes

## 05 - Maximum Length Contiguous Subarray With Equal 0 and 1

**Updated on:** 2026-04-12

------------------------------------------------------------------------

## 1. Problem Understanding

Given a binary array `nums`, return the maximum length of a contiguous subarray with an equal number of `0` and `1`.

Important:
- subarray means contiguous
- we need the maximum length, not the count

------------------------------------------------------------------------

## 2. Key Insight

Equal number of `0` and `1` means their contribution should cancel out.

So transform the array as:
- `0 -> -1`
- `1 -> +1`

Then the problem becomes:
- find the longest subarray whose sum is `0`

Why this works:
- if a subarray has equal number of `1` and `0`
- then after transformation, the total sum is `0`

------------------------------------------------------------------------

## 3. Reduction to a Known Pattern

After transformation, this is exactly:
- maximum size subarray with sum equal to `0`

So we reuse the standard prefix-sum + hashmap approach for longest subarray length.

------------------------------------------------------------------------

## 4. Map Meaning

Use:
- `map[prefixSum] = first index where this prefix sum appeared`

Base case:
- `map.put(0, -1)`
- means a prefix sum of `0` exists before the array starts

------------------------------------------------------------------------

## 5. Step-by-Step Logic

For each index `i`:
1. convert current value:
   - `0 -> -1`
   - `1 -> +1`
2. add it to the running sum
3. if this sum has been seen before at index `j`, then subarray `(j + 1) ... i` has sum `0`
4. update the maximum length
5. if this sum is new, store its index

Why keep the first occurrence only?
- because an earlier index gives a longer zero-sum subarray

------------------------------------------------------------------------

## 6. Code

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int findMaxLength(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);

        int sum = 0;
        int maxLength = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += (nums[i] == 0) ? -1 : 1;

            if (map.containsKey(sum)) {
                maxLength = Math.max(maxLength, i - map.get(sum));
            } else {
                map.put(sum, i);
            }
        }

        return maxLength;
    }
}
```

------------------------------------------------------------------------

## 7. Dry Run

Take:
- `nums = [0, 1, 0]`

Transform on the fly:
- `[-1, 1, -1]`

Process:
- start: `map = {0=-1}`, `sum = 0`, `maxLength = 0`
- `i = 0`, value `0 -> -1`
  - `sum = -1`
  - first time seen, store `-1 -> 0`
- `i = 1`, value `1 -> +1`
  - `sum = 0`
  - seen before at `-1`
  - length = `1 - (-1) = 2`
  - `maxLength = 2`
- `i = 2`, value `0 -> -1`
  - `sum = -1`
  - seen before at `0`
  - length = `2 - 0 = 2`

Answer:
- `2`

Valid longest subarrays:
- `[0, 1]`
- `[1, 0]`

------------------------------------------------------------------------

## 8. Complexity

- Time: `O(n)`
- Space: `O(n)`

------------------------------------------------------------------------

## 9. Pattern Recognition

Use this trick when:
- the array has two categories that should balance each other
- we need equal count of two values
- the question asks for longest subarray

Signal:
- equal `0` and `1` often means transform to `-1` and `+1`

------------------------------------------------------------------------

## 10. Edge Cases and Pitfalls

- This works because the array is binary
- Do not store the latest index of a prefix sum; keep the first one
- `map.put(0, -1)` is necessary for subarrays starting from index `0`
- Do not use `0 -> 0` and `1 -> 1`; that would not capture the balance condition

------------------------------------------------------------------------

## 11. Takeaway

- Convert `0` to `-1` and `1` to `+1`
- Then equal number of `0` and `1` becomes a zero-sum subarray problem
- From there, apply the longest-subarray-with-given-sum prefix map pattern

------------------------------------------------------------------------

# End of Notes
