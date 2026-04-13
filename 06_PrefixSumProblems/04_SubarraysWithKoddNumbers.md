# Prefix Sum Notes

## 04 - Subarrays With Exactly K Odd Numbers

**Updated on:** 2026-04-12

------------------------------------------------------------------------

## 1. Problem Understanding

Given an integer array `nums` and an integer `k`, count the number of subarrays that contain exactly `k` odd numbers.

This is also known as:
- Count Number of Nice Subarrays

------------------------------------------------------------------------

## 2. Key Insight

We do not actually care about the values themselves.

We only care whether each number is:
- odd -> `1`
- even -> `0`

After this transformation, the problem becomes:
- count the number of subarrays whose sum equals `k`

So this problem reduces directly to the standard prefix-sum counting problem.

------------------------------------------------------------------------

## 3. Transformation

Convert:
- odd number -> `1`
- even number -> `0`

Example:
- `nums = [1, 1, 2, 1, 1]`
- transformed = `[1, 1, 0, 1, 1]`

Now:
- a subarray with exactly `k` odd numbers
- becomes a subarray with sum exactly `k`

------------------------------------------------------------------------

## 4. Map Meaning

Use:
- `map[prefixSum] = frequency of this prefix sum seen so far`

Base case:
- `map.put(0, 1)`

------------------------------------------------------------------------

## 5. Step-by-Step Logic

For each number:
1. convert it to `0` or `1`
2. add that to running sum
3. look for `sum - k` in the map
4. add its frequency to the answer
5. store current sum

------------------------------------------------------------------------

## 6. Code

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int numberOfSubarrays(int[] nums, int k) {
        Map<Integer, Integer> prefixMap = new HashMap<>();
        prefixMap.put(0, 1);

        int sum = 0;
        int count = 0;

        for (int num : nums) {
            int value = (num % 2 == 0) ? 0 : 1;
            sum += value;

            count += prefixMap.getOrDefault(sum - k, 0);

            prefixMap.put(sum, prefixMap.getOrDefault(sum, 0) + 1);
        }

        return count;
    }
}
```

------------------------------------------------------------------------

## 7. Dry Run

Take:
- `nums = [1, 1, 2, 1, 1]`
- `k = 3`

Transform on the fly:
- `[1, 1, 0, 1, 1]`

Process:
- start: `prefixMap = {0=1}`, `sum = 0`, `count = 0`
- read `1` -> odd -> `1`
  - `sum = 1`
  - need `-2`, not found
- read `1` -> odd -> `1`
  - `sum = 2`
  - need `-1`, not found
- read `2` -> even -> `0`
  - `sum = 2`
  - need `-1`, not found
- read `1` -> odd -> `1`
  - `sum = 3`
  - need `0`, found once
  - `count = 1`
- read `1` -> odd -> `1`
  - `sum = 4`
  - need `1`, found once
  - `count = 2`

Answer:
- `2`

Valid subarrays:
- `[1, 1, 2, 1]`
- `[1, 2, 1, 1]`

------------------------------------------------------------------------

## 8. Complexity

- Time: `O(n)`
- Space: `O(n)`

------------------------------------------------------------------------

## 9. Pattern Recognition

Use this trick when:
- the array condition can be converted into binary contribution
- odd/even, vowel/consonant, positive/non-positive, etc.
- after transformation, the problem becomes exact subarray sum

Signal:
- "exactly k occurrences of something" often suggests transform to `0/1`

------------------------------------------------------------------------

## 10. Edge Cases and Pitfalls

- Do not create a separate transformed array unless needed; on-the-fly is cleaner
- Still remember the base case `0 -> 1`
- This is the same template as `Count Subarrays Sum Equals K` after transformation

------------------------------------------------------------------------

## 11. Takeaway

- Convert the property of interest into a numeric contribution.
- Once odd becomes `1` and even becomes `0`, the problem is just:
  - count subarrays with sum `k`
- This reduction pattern is extremely useful in interviews.

------------------------------------------------------------------------

# End of Notes
