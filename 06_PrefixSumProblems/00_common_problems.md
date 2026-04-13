# Prefix Sum Problems

**Updated on:** 2026-04-12

------------------------------------------------------------------------

## 1. Core Idea

Prefix sum means:
- `prefix[i]` = sum of elements from index `0` to `i`

For any subarray `l..r`:
- `subarraySum(l, r) = prefix[r] - prefix[l - 1]`

This one identity powers many array problems:
- count subarrays with a given sum
- find the longest subarray with a given sum
- count subarrays divisible by `k`
- transform the array, then reuse the same prefix-sum pattern

------------------------------------------------------------------------

## 2. The Main Pattern

While scanning left to right:
- maintain running prefix sum
- store some information about earlier prefix sums in a map
- use that stored information to answer the current index quickly

Typical map meanings:
- prefix sum -> frequency
  - used when we want a **count**
- prefix sum -> first index
  - used when we want a **maximum length**
- prefix sum modulo `k` -> frequency
  - used when divisibility is involved

------------------------------------------------------------------------

## 3. Common Problems in This Folder

1. [01_CountSubarraysSumEqualsK.md](./01_CountSubarraysSumEqualsK.md)
   Count the number of subarrays whose sum is exactly `k`

2. [02_MaxSizeSubarrayWithSumEqualK.md](./02_MaxSizeSubarrayWithSumEqualK.md)
   Find the maximum length subarray whose sum is exactly `k`

3. [03_SubarraysSumDivisibleByK.md](./03_SubarraysSumDivisibleByK.md)
   Count subarrays whose sum is divisible by `k`

4. [04_SubarraysWithKoddNumbers.md](./04_SubarraysWithKoddNumbers.md)
   Count subarrays with exactly `k` odd numbers

5. [05_MaxLengthEqual0And1.md](./05_MaxLengthEqual0And1.md)
   Find the maximum length subarray with equal number of `0` and `1`

------------------------------------------------------------------------

## 4. Recognition Hints

Think prefix sum + hashmap when:
- question asks about **subarray**
- elements can be negative, so sliding window is unsafe
- we need count / longest / exact sum / divisible sum
- the condition can be rewritten using:
  - `prefix[j] - prefix[i] = target`
  - or equal remainders modulo `k`

------------------------------------------------------------------------

## 5. Base Cases You Should Memorize

- For exact-sum counting:
  - `map.put(0, 1)`
  - means: we have seen prefix sum `0` once before starting

- For maximum length with exact sum:
  - `map.put(0, -1)`
  - means: prefix sum `0` exists before index `0`

- For divisibility by `k`:
  - store remainder frequency, starting with `0 -> 1`

------------------------------------------------------------------------

## 6. Common Pitfalls

- Forgetting the base case in the map
- Overwriting the first index when the problem wants maximum length
- Using sliding window on arrays that contain negative numbers
- Forgetting to normalize negative modulo:
  - use `((sum % k) + k) % k`
- Mixing up:
  - count problems -> store frequency
  - max length problems -> store first occurrence index

------------------------------------------------------------------------

## 7. Takeaway

- Prefix sum converts subarray problems into relationships between two prefix states.
- The hashmap stores what earlier prefix states we need.
- Once you identify what the map should represent, the implementation becomes almost mechanical.

------------------------------------------------------------------------

# End of Notes
