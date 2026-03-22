# Sliding Window / Two Pointers Notes

## 05 - Binary Subarrays With Sum (LC 930)

**Generated on:** 2026-03-23 00:57:44 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a binary array `nums` and an integer `goal`, return the number of non-empty subarrays with sum exactly equal to `goal`.

Examples:
- `nums = [1,0,1,0,1], goal = 2` → `4`
- `nums = [0,0,0,0,0], goal = 0` → `15`

Important:
- This is a **counting** problem, not a longest-window problem.
- Direct sliding window for “exactly equal to goal” does not work cleanly in general.
- But because the array is binary and all values are non-negative, we can use:

`count(sum == goal) = count(sum <= goal) - count(sum <= goal - 1)`

------------------------------------------------------------------------

## 🪜 2. Core Idea

If we know how to count:
- number of subarrays with sum `<= goal`

Then:
- exact count with sum `goal`
- = subarrays with sum `<= goal`
-   minus subarrays with sum `<= goal - 1`

This works because:
- all subarray sums are integers
- array is binary / non-negative
- so counts are monotonic by target

------------------------------------------------------------------------

## 🔁 3. Steps

To count subarrays with sum `<= target`:

1. Expand `right`
2. Add `nums[right]` to `windowSum`
3. If `windowSum > target`, shrink from left
4. For current `right`, all windows starting from `left ... right` are valid
5. Add `(right - left + 1)` to answer

Then final result:
- `atMost(goal) - atMost(goal - 1)`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class BinarySubarraysWithSum {
    public int numSubarraysWithSum(int[] nums, int goal) {
        return countAtMost(nums, goal) - countAtMost(nums, goal - 1);
    }

    private int countAtMost(int[] nums, int target) {
        if (target < 0) return 0;

        int left = 0;
        int windowSum = 0;
        int count = 0;

        for (int right = 0; right < nums.length; right++) {
            windowSum += nums[right];

            while (windowSum > target) {
                windowSum -= nums[left];
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

`nums = [1,0,1,0,1], goal = 2`

We compute:
- `countAtMost(2)`
- `countAtMost(1)`

Then:
- answer = `countAtMost(2) - countAtMost(1) = 14 - 10 = 4`

Valid subarrays are:
- `[1,0,1]`
- `[1,0,1,0]`
- `[0,1,0,1]`
- `[1,0,1]` (last 3 elements)

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this pattern when:
- array elements are non-negative
- question asks for **count of subarrays with exact sum = k**
- direct exact-window handling is messy
- but `atMost(k)` is easy

Very common transformation:
- `exactly(k) = atMost(k) - atMost(k - 1)`

Same idea is used in:
- Binary Subarrays With Sum
- Nice Subarrays
- Subarrays with at most/exactly K distinct

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- If `goal < 0`, return `0` in `countAtMost`
- This trick works because values are non-negative
- For arrays with negative numbers, this sliding window method breaks
- Don’t forget:
  - for each `right`, valid subarrays count is `(right - left + 1)`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Exact count can often be converted into:
  - `atMost(goal) - atMost(goal - 1)`
- For binary / non-negative arrays, `countAtMost` is a standard sliding window
- This is one of the most important counting patterns in sliding window

------------------------------------------------------------------------

# End of Notes
