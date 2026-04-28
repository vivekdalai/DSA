# Prefix Notes

## 974 - Subarray Sums Divisible by K

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/subarray-sums-divisible-by-k/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an integer array `nums` and an integer `k`, return the number of non-empty **subarrays** that have a sum divisible by `k`.

A **subarray** is a **contiguous** part of an array.

**Example 1:**

```text
Input: nums = [4,5,0,-2,-3,1], k = 5
Output: 7
Explanation: There are 7 subarrays with a sum divisible by k = 5:
[4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]
```

**Example 2:**

```text
Input: nums = [5], k = 9
Output: 0
```

**Constraints:**

- `1 <= nums.length <= 3 * 104`

- `-104 <= nums[i] <= 104`

- `2 <= k <= 104`

------------------------------------------------------------------------

## 2. First Intuition

Two prefix sums with the same remainder modulo `k` have a difference divisible by `k`. Count how many previous prefixes share the current remainder.

------------------------------------------------------------------------

## 3. Modulo Frequency Counting

- The file keeps a frequency of remainders already seen.
- It normalizes negative remainders with `if (rem < 0) rem += k`.
- For every current remainder, all previous equal remainders form valid subarrays ending here.
- The file includes both `HashMap` and array versions; the array version is faster when `k` is known.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[4,5,0,-2,-3,1]`, `k = 5`: repeated remainder `4` and repeated remainder `0` create the valid subarrays. The final count is `7`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int subarraysDivByK(int[] nums, int k) {
    int[] freq = new int[k];
    freq[0] = 1;

    int sum = 0;
    int ans = 0;
    for (int num : nums) {
        sum += num;
        int rem = sum % k;
        if (rem < 0) rem += k;
        ans += freq[rem];
        freq[rem]++;
    }
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(k)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Same remainder means divisible difference.
- Always normalize negative modulo results in Java.

------------------------------------------------------------------------

## End of Notes
