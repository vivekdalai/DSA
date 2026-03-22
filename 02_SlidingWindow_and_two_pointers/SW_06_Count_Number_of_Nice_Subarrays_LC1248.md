# Sliding Window / Two Pointers Notes

## 06 - Count Number of Nice Subarrays (LC 1248)

**Generated on:** 2026-03-23 00:58:10 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums` and an integer `k`, return the number of subarrays with exactly `k` odd numbers.

A subarray is called **nice** if it contains exactly `k` odd numbers.

Examples:
- `nums = [1,1,2,1,1], k = 3` → `2`
- `nums = [2,4,6], k = 1` → `0`

Core reduction:
- odd number = `1`
- even number = `0`

So this becomes:
- count subarrays with exactly `k` ones

That is the same pattern as Binary Subarrays With Sum.

------------------------------------------------------------------------

## 🪜 2. Core Idea

Use:

`count(exactly k odds) = count(atMost k odds) - count(atMost (k - 1) odds)`

Why it works:
- number of odds in a subarray is non-negative
- `atMost(k)` is easy using sliding window
- exact count can be derived by subtraction

------------------------------------------------------------------------

## 🔁 3. Steps

To count subarrays with at most `target` odd numbers:

1. Expand `right`
2. If `nums[right]` is odd, increment `oddCount`
3. If `oddCount > target`, shrink from left
4. For current `right`, all windows from `left ... right` are valid
5. Add `(right - left + 1)` to answer

Then:
- exact answer = `atMost(k) - atMost(k - 1)`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class NiceSubarrays {
    public int numberOfSubarrays(int[] nums, int k) {
        return countAtMost(nums, k) - countAtMost(nums, k - 1);
    }

    private int countAtMost(int[] nums, int target) {
        if (target < 0) return 0;

        int left = 0;
        int oddCount = 0;
        int count = 0;

        for (int right = 0; right < nums.length; right++) {
            if ((nums[right] & 1) == 1) {
                oddCount++;
            }

            while (oddCount > target) {
                if ((nums[left] & 1) == 1) {
                    oddCount--;
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
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`nums = [1,1,2,1,1], k = 3`

We compute:
- `countAtMost(3)`
- `countAtMost(2)`

Then:
- answer = `countAtMost(3) - countAtMost(2) = 14 - 12 = 2`

Valid nice subarrays:
- `[1,1,2,1]`
- `[1,2,1,1]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is the same pattern as:
- Binary Subarrays With Sum

Transformation:
- odd → 1
- even → 0

Then apply:
- `exactly(k) = atMost(k) - atMost(k - 1)`

This pattern is useful whenever:
- the exact condition is hard
- but at-most condition is easy

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- `k = 0`:
  - asks for subarrays with exactly 0 odd numbers
  - formula still works
- If `target < 0`, return `0`
- Don’t forget to count all valid windows:
  - add `(right - left + 1)` at each step
- Works because odds count is non-negative and window is monotonic

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Convert odd/even into a counting property
- Use:
  - `exactly(k) = atMost(k) - atMost(k - 1)`
- Sliding window is applied on “number of odds in window”
- This is one of the most important counting patterns after standard longest-window problems

------------------------------------------------------------------------

# End of Notes
