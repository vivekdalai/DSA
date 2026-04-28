# Two Pointers and Sliding Window Notes

## 2461 - Maximum Sum of Distinct Subarrays With Length K

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/maximum-sum-of-distinct-subarrays-with-length-k/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given an integer array `nums` and an integer `k`. Find the maximum subarray sum of all the subarrays of `nums` that meet the following conditions:

- The length of the subarray is `k`, and

- All the elements of the subarray are **distinct**.

Return the maximum subarray sum of all the subarrays that meet the conditions. If no subarray meets the conditions, return `0`.

A **subarray** is a contiguous non-empty sequence of elements within an array.

**Example 1:**

```text
Input: nums = [1,5,4,2,9,9,9], k = 3
Output: 15
Explanation: The subarrays of nums with length 3 are:
- [1,5,4] which meets the requirements and has a sum of 10.
- [5,4,2] which meets the requirements and has a sum of 11.
- [4,2,9] which meets the requirements and has a sum of 15.
- [2,9,9] which does not meet the requirements because the element 9 is repeated.
- [9,9,9] which does not meet the requirements because the element 9 is repeated.
We return 15 because it is the maximum subarray sum of all the subarrays that meet the conditions
```

**Example 2:**

```text
Input: nums = [4,4,4], k = 3
Output: 0
Explanation: The subarrays of nums with length 3 are:
- [4,4,4] which does not meet the requirements because the element 4 is repeated.
We return 0 because no subarrays meet the conditions.
```

**Constraints:**

- `1 <= k <= nums.length <= 105`

- `1 <= nums[i] <= 105`

------------------------------------------------------------------------

## 2. First Intuition

The window must have exactly length `k` and all values inside it must be distinct. A frequency map tells whether the current window has `k` unique values.

------------------------------------------------------------------------

## 3. Fixed Window with Frequency Map

- The file maintains a running sum and a map of value counts.
- It expands with `r`, and if the window grows beyond `k`, it removes `nums[l]` and moves `l`.
- When `map.size() == k`, all `k` values in the window are distinct, so the sum can update the answer.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[1,5,4,2,9,9,9]`, `k = 3`: windows `[1,5,4]`, `[5,4,2]`, and `[4,2,9]` are distinct; their sums are `10`, `11`, and `15`, so the answer is `15`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public long maximumSubarraySum(int[] nums, int k) {
    Map<Integer, Integer> freq = new HashMap<>();
    long sum = 0;
    long best = 0;
    int left = 0;

    for (int right = 0; right < nums.length; right++) {
        sum += nums[right];
        freq.put(nums[right], freq.getOrDefault(nums[right], 0) + 1);

        if (right - left + 1 > k) {
            int out = nums[left++];
            sum -= out;
            freq.put(out, freq.get(out) - 1);
            if (freq.get(out) == 0) freq.remove(out);
        }

        if (right - left + 1 == k && freq.size() == k) {
            best = Math.max(best, sum);
        }
    }
    return best;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(k)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Fixed window plus uniqueness usually means frequency map.
- `map.size() == k` is valid only when the window length is also `k`.

------------------------------------------------------------------------

## End of Notes
