# Prefix Sum Notes

## 03 - Subarray Sums Divisible By K

**Updated on:** 2026-04-12

------------------------------------------------------------------------

## 1. Problem Understanding

Given an integer array `nums` and an integer `k`, count the number of subarrays whose sum is divisible by `k`.

That means for a subarray sum `S`, we need:
- `S % k == 0`

------------------------------------------------------------------------

## 2. Key Insight

Let two prefix sums be:
- `prefix1`
- `prefix2`

If they have the same remainder when divided by `k`, then:
- `(prefix2 - prefix1) % k == 0`

So the subarray between them has sum divisible by `k`.

This converts the problem into:
- count pairs of prefix sums with the same modulo remainder

------------------------------------------------------------------------

## 3. Why Equal Remainders Work

Suppose:
- `prefix1 = a * k + r`
- `prefix2 = b * k + r`

Then:
- `prefix2 - prefix1 = (b - a) * k`

So the difference is a multiple of `k`.

That difference is exactly the subarray sum between those two prefix sums.

------------------------------------------------------------------------

## 4. Map Meaning

Use:
- `map[remainder] = frequency of this remainder seen so far`

Base case:
- `map.put(0, 1)`
- because a prefix sum itself may already be divisible by `k`

------------------------------------------------------------------------

## 5. Negative Modulo Handling

In Java, `%` can produce a negative result.

So normalize remainder as:

```java
int mod = ((sum % k) + k) % k;
```

This ensures:
- remainder always lies in `[0, k - 1]`

Example:
- if `sum = -2` and `k = 5`
- Java may give `-2`
- but mathematically `-2` and `3` are equivalent modulo `5`

So we convert `-2` to `3`.

------------------------------------------------------------------------

## 6. Step-by-Step Logic

For each element:
1. update running prefix sum
2. compute normalized remainder `sum % k`
3. if this remainder was seen before, add its frequency to the answer
4. increase the frequency of this remainder

------------------------------------------------------------------------

## 7. Code

```java
import java.util.HashMap;
import java.util.Map;

class Solution {
    public int subarraysDivByK(int[] nums, int k) {
        Map<Integer, Integer> remainderCount = new HashMap<>();
        remainderCount.put(0, 1);

        int sum = 0;
        int count = 0;

        for (int num : nums) {
            sum += num;

            int mod = ((sum % k) + k) % k;

            count += remainderCount.getOrDefault(mod, 0);

            remainderCount.put(mod, remainderCount.getOrDefault(mod, 0) + 1);
        }

        return count;
    }
}
```

------------------------------------------------------------------------

## 8. Dry Run

Take:
- `nums = [4, 5, 0, -2, -3, 1]`
- `k = 5`

Process:
- start: `remainderCount = {0=1}`, `sum = 0`, `count = 0`
- read `4`:
  - `sum = 4`, `mod = 4`
  - seen `4` zero times
  - store `4 -> 1`
- read `5`:
  - `sum = 9`, `mod = 4`
  - seen `4` once
  - `count = 1`
  - store `4 -> 2`
- read `0`:
  - `sum = 9`, `mod = 4`
  - seen `4` twice
  - `count = 3`
  - store `4 -> 3`
- read `-2`:
  - `sum = 7`, `mod = 2`
  - store `2 -> 1`
- read `-3`:
  - `sum = 4`, `mod = 4`
  - seen `4` three times
  - `count = 6`
  - store `4 -> 4`
- read `1`:
  - `sum = 5`, `mod = 0`
  - seen `0` once
  - `count = 7`

Answer:
- `7`

------------------------------------------------------------------------

## 9. Complexity

- Time: `O(n)`
- Space: `O(min(n, k))` in practice, or `O(n)` with a hashmap bound

------------------------------------------------------------------------

## 10. Pattern Recognition

Use this pattern when:
- question says divisible by `k`
- or asks whether subarray sum leaves the same remainder

Signal:
- divisibility + subarray -> think prefix remainder

------------------------------------------------------------------------

## 11. Edge Cases and Pitfalls

- Normalize negative modulo values
- Do not forget `remainderCount.put(0, 1)`
- This is a count problem, so store frequency

------------------------------------------------------------------------

## 12. Takeaway

- Exact-sum problems look for `sum - k`
- Divisibility problems look for matching remainders
- Equal remainder of two prefix sums means the subarray between them is divisible by `k`

------------------------------------------------------------------------

# End of Notes
