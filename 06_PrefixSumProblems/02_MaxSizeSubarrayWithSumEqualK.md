# Prefix Sum Notes

## 02 - Maximum Size Subarray With Sum Equal K

**Updated on:** 2026-04-12

------------------------------------------------------------------------

## 1. Problem Understanding

Given an integer array `nums` and an integer `k`, return the length of the longest subarray whose sum equals `k`.

This is different from the previous problem:
- there we counted subarrays
- here we want the maximum length

That change affects what we store in the map.

------------------------------------------------------------------------

## 2. Key Insight

Let current prefix sum be `sum` at index `i`.

If some earlier prefix sum equals `sum - k` at index `j`, then:
- subarray `(j + 1) ... i` has sum `k`
- its length is `i - j`

To maximize length, for each prefix sum we should store:
- the first index where it appeared

Why first index?
- earlier index gives longer subarray

------------------------------------------------------------------------

## 3. Map Meaning

Use:
- `map[prefixSum] = first index where this prefix sum appeared`

Base case:
- `map.put(0, -1)`
- means prefix sum `0` exists before the array starts

------------------------------------------------------------------------

## 4. Step-by-Step Logic

For each index `i`:
1. update running sum
2. check whether `sum - k` exists in the map
3. if yes, update maximum length
4. store current prefix sum only if it is not already present

That last rule is important:
- we keep the earliest index only

------------------------------------------------------------------------

## 5. Code

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int maxSubArrayLen(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1); // prefix sum 0 before index 0

        int sum = 0;
        int maxLength = 0;

        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];

            if (map.containsKey(sum - k)) {
                maxLength = Math.max(maxLength, i - map.get(sum - k));
            }

            map.putIfAbsent(sum, i);
        }

        return maxLength;
    }
}
```

------------------------------------------------------------------------

## 6. Dry Run

Take:
- `nums = [1, -1, 5, -2, 3]`
- `k = 3`

Process:
- start: `map = {0=-1}`, `sum = 0`, `maxLength = 0`
- `i = 0`, value `1`:
  - `sum = 1`
  - need `-2`, not found
  - store `1 -> 0`
- `i = 1`, value `-1`:
  - `sum = 0`
  - need `-3`, not found
  - keep existing `0 -> -1`
- `i = 2`, value `5`:
  - `sum = 5`
  - need `2`, not found
  - store `5 -> 2`
- `i = 3`, value `-2`:
  - `sum = 3`
  - need `0`, found at `-1`
  - length = `3 - (-1) = 4`
  - `maxLength = 4`
- `i = 4`, value `3`:
  - `sum = 6`
  - need `3`, found at `3`
  - length = `1`

Answer:
- `4`
- longest subarray is `[1, -1, 5, -2]`

------------------------------------------------------------------------

## 7. Complexity

- Time: `O(n)`
- Space: `O(n)`

------------------------------------------------------------------------

## 8. Pattern Recognition

This pattern appears when:
- the question asks for longest / maximum size subarray
- condition is exact sum
- negative values may exist

Signal:
- max length problem -> store first occurrence index

------------------------------------------------------------------------

## 9. Edge Cases and Pitfalls

- Do not overwrite the first occurrence of a prefix sum
- Do not use prefix sum frequency here; index is what matters
- `map.put(0, -1)` is required for subarrays starting at index `0`

------------------------------------------------------------------------

## 10. Takeaway

- Same prefix-sum equation as the counting version:
  - look for `sum - k`
- The only major difference is what the map stores:
  - count problem -> frequency
  - max length problem -> earliest index

------------------------------------------------------------------------

# End of Notes
