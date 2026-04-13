# Prefix Sum Notes

## 01 - Count Subarrays Sum Equals K

**Updated on:** 2026-04-12

------------------------------------------------------------------------

## 1. Problem Understanding

Given an integer array `nums` and an integer `k`, count how many subarrays have sum exactly `k`.

Important:
- subarray means contiguous
- numbers may be negative, so sliding window is not reliable in general

------------------------------------------------------------------------

## 2. Key Insight

Let current prefix sum be `sum`.

If there exists an earlier prefix sum equal to `sum - k`, then:
- current subarray sum = `sum - (sum - k) = k`

So at each index we ask:
- how many times have we already seen prefix sum `sum - k`?

That count tells us how many valid subarrays end at the current index.

------------------------------------------------------------------------

## 3. Map Meaning

Use:
- `map[prefixSum] = frequency of this prefix sum seen so far`

Base case:
- `map.put(0, 1)`
- this handles subarrays that start from index `0`

------------------------------------------------------------------------

## 4. Step-by-Step Logic

For each number:
1. add it to running sum
2. check whether `sum - k` has appeared before
3. if yes, add its frequency to the answer
4. record the current `sum` in the map

------------------------------------------------------------------------

## 5. Code

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 1); // empty prefix

        int sum = 0;
        int count = 0;

        for (int num : nums) {
            sum += num;

            count += map.getOrDefault(sum - k, 0);

            map.put(sum, map.getOrDefault(sum, 0) + 1);
        }

        return count;
    }
}
```

------------------------------------------------------------------------

## 6. Dry Run

Take:
- `nums = [1, 2, 3]`
- `k = 3`

Process:
- start: `map = {0=1}`, `sum = 0`, `count = 0`
- read `1`:
  - `sum = 1`
  - need `1 - 3 = -2`, not found
  - store `1`
- read `2`:
  - `sum = 3`
  - need `3 - 3 = 0`, found once
  - `count = 1`
  - store `3`
- read `3`:
  - `sum = 6`
  - need `6 - 3 = 3`, found once
  - `count = 2`
  - store `6`

Answer:
- `2`
- subarrays are `[1, 2]` and `[3]`

------------------------------------------------------------------------

## 7. Complexity

- Time: `O(n)`
- Space: `O(n)`

------------------------------------------------------------------------

## 8. Pattern Recognition

This pattern appears when:
- the question asks for **count of subarrays**
- condition is `sum == k`
- negative values may be present

Signal:
- count problem -> store frequency, not index

------------------------------------------------------------------------

## 9. Edge Cases and Pitfalls

- Negative numbers are allowed; this method still works
- Do not forget `map.put(0, 1)`
- If many prefix sums repeat, frequency matters; do not store only a boolean

------------------------------------------------------------------------

## 10. Takeaway

- We do not search every subarray explicitly.
- We count how many earlier prefix sums can pair with the current prefix sum to form `k`.
- Formula to remember: if current prefix is `sum`, look for `sum - k`.

------------------------------------------------------------------------

# End of Notes
