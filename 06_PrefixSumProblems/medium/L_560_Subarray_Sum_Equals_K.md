# Prefix Notes

## 560 - Subarray Sum Equals K

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/subarray-sum-equals-k/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an array of integers `nums` and an integer `k`, return the total number of subarrays whose sum equals to `k`.

A subarray is a contiguous **non-empty** sequence of elements within an array.

**Example 1:**

```text
Input: nums = [1,1,1], k = 2
Output: 2
```

**Example 2:**

```text
Input: nums = [1,2,3], k = 3
Output: 2
```

**Constraints:**

- `1 <= nums.length <= 2 * 104`

- `-1000 <= nums[i] <= 1000`

- `-107 <= k <= 107`

------------------------------------------------------------------------

## 2. First Intuition

If the current prefix sum is `sum`, any earlier prefix sum equal to `sum - k` forms a subarray ending here with sum `k`.

------------------------------------------------------------------------

## 3. Prefix Count Map

- The file stores how often each prefix sum has appeared.
- It seeds `0 -> 1` for subarrays that start at index `0`.
- For each number, update `sum`, add the count of `sum - k` to the answer, then record the current `sum`.

------------------------------------------------------------------------

## 4. Short Dry Run

For `nums = [1,1,1]`, `k = 2`: at index `1`, `sum = 2` and `sum - k = 0`, so `[1,1]` counts. At index `2`, `sum = 3` and `sum - k = 1`, so the second `[1,1]` counts.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int subarraySum(int[] nums, int k) {
    Map<Integer, Integer> freq = new HashMap<>();
    freq.put(0, 1);

    int sum = 0;
    int ans = 0;
    for (int num : nums) {
        sum += num;
        ans += freq.getOrDefault(sum - k, 0);
        freq.put(sum, freq.getOrDefault(sum, 0) + 1);
    }
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(n)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Use prefix frequency, not just first index, when the question asks for a count.
- This works with negative numbers, unlike a simple sliding window.

------------------------------------------------------------------------

## End of Notes
