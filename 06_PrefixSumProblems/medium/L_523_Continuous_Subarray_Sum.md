# Prefix Notes

## 523 - Continuous Subarray Sum

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/continuous-subarray-sum/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an integer array nums and an integer k, return `true` if `nums` has a **good subarray** or `false` otherwise.

A **good subarray** is a subarray where:

- its length is **at least two**, and

- the sum of the elements of the subarray is a multiple of `k`.

**Note** that:

- A **subarray** is a contiguous part of the array.

- An integer `x` is a multiple of `k` if there exists an integer `n` such that `x = n * k`. `0` is **always** a multiple of `k`.

**Example 1:**

```text
Input: nums = [23,2,4,6,7], k = 6
Output: true
Explanation: [2, 4] is a continuous subarray of size 2 whose elements sum up to 6.
```

**Example 2:**

```text
Input: nums = [23,2,6,4,7], k = 6
Output: true
Explanation: [23, 2, 6, 4, 7] is an continuous subarray of size 5 whose elements sum up to 42.
42 is a multiple of 6 because 42 = 7 * 6 and 7 is an integer.
```

**Example 3:**

```text
Input: nums = [23,2,6,4,7], k = 13
Output: false
```

**Constraints:**

- `1 <= nums.length <= 105`

- `0 <= nums[i] <= 109`

- `0 <= sum(nums[i]) <= 231 - 1`

- `1 <= k <= 231 - 1`

------------------------------------------------------------------------

## 2. First Intuition

If two prefix sums have the same remainder modulo `k`, the subarray between them has sum divisible by `k`. The only extra condition is length at least two.

------------------------------------------------------------------------

## 3. Prefix Remainder Map

- The file keeps `remainder -> earliest index` in a map.
- It seeds remainder `0` at index `-1` so a prefix from the start can count.
- At each index, compute `prefixSum % k`.
- If that remainder was seen before and the distance is greater than `1`, return `true`.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[23,2,4,6,7]`, `k = 6`: prefix remainders are `5,1,5...`. Remainder `5` repeats from index `0` to `2`, and the subarray `[2,4]` sums to `6`, so return `true`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean checkSubarraySum(int[] nums, int k) {
    Map<Integer, Integer> firstIndex = new HashMap<>();
    firstIndex.put(0, -1);

    int sum = 0;
    for (int i = 0; i < nums.length; i++) {
        sum += nums[i];
        int rem = sum % k;

        if (firstIndex.containsKey(rem)) {
            if (i - firstIndex.get(rem) > 1) return true;
        } else {
            firstIndex.put(rem, i);
        }
    }
    return false;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(min(n, k))` for stored remainders.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Same modulo remainder means the difference is divisible by `k`.
- Store the earliest index so the length has the best chance to be large enough.

------------------------------------------------------------------------

## End of Notes
