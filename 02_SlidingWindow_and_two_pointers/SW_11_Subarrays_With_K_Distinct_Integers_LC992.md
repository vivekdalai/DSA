# Sliding Window / Two Pointers Notes

## 11 - Subarrays with K Different Integers (LC 992)

**Generated on:** 2026-03-23 01:05:35 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums` and an integer `k`, return the number of good subarrays of `nums`.

A good subarray is one where:
- the number of distinct integers is exactly `k`

Examples:
- `nums = [1,2,1,2,3], k = 2` → `7`
- `nums = [1,2,1,3,4], k = 3` → `3`

This is a counting problem.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Exactly like:
- Binary Subarrays With Sum
- Nice Subarrays

We use:

`count(exactly k distinct) = count(atMost k distinct) - count(atMost (k - 1) distinct)`

So the main task is:
- count subarrays with at most `k` distinct numbers

------------------------------------------------------------------------

## 🔁 3. Steps

To compute `countAtMost(k)`:

1. Expand `right`
2. Add `nums[right]` into frequency map
3. If distinct count becomes greater than `k`, shrink from left
4. For current `right`, all windows starting from `left ... right` are valid
5. Add `(right - left + 1)` to answer

Then:
- exact answer = `atMost(k) - atMost(k - 1)`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class SubarraysWithKDistinct {
    public int subarraysWithKDistinct(int[] nums, int k) {
        return countAtMost(nums, k) - countAtMost(nums, k - 1);
    }

    private int countAtMost(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        int left = 0;
        int count = 0;

        for (int right = 0; right < nums.length; right++) {
            freq.put(nums[right], freq.getOrDefault(nums[right], 0) + 1);

            while (freq.size() > k) {
                int leftVal = nums[left];
                freq.put(leftVal, freq.get(leftVal) - 1);
                if (freq.get(leftVal) == 0) {
                    freq.remove(leftVal);
                }
                left++;
            }

            count += (right - left + 1);
        }

        return count;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(k) / O(n) in worst case

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`nums = [1,2,1,2,3], k = 2`

We compute:
- `atMost(2) = 12`
- `atMost(1) = 5`

So:
- exactly `2` distinct = `12 - 5 = 7`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is the standard:
- **exactly K = atMost(K) - atMost(K-1)**

Use this when:
- exact window counting is hard
- but at-most version is easy with sliding window

Same family:
- binary subarrays with sum
- nice subarrays
- subarrays with exactly K distinct

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Don’t forget to remove keys from map when frequency becomes 0
- Count all valid windows at each step:
  - `(right - left + 1)`
- This is counting, not longest-length
- Works because “number of distinct elements” is monotonic with window expansion/shrinking

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Exact distinct count problems often reduce to:
  - `atMost(k) - atMost(k - 1)`
- `countAtMost(k)` is a standard sliding window with frequency map
- This is one of the most frequent advanced sliding window counting problems

------------------------------------------------------------------------

# End of Notes
