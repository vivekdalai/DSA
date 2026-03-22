# Two Pointers Notes

## 04 - 3Sum Closest (LC 16)

**Generated on:** 2026-03-23 01:07:13 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums` of length `n` and an integer `target`, find three integers in `nums` such that the sum is closest to `target`.

Return the sum of the three integers.

Examples:
- `nums = [-1,2,1,-4], target = 1` → `2`
- because `(-1 + 2 + 1) = 2`, which is closest to `1`

------------------------------------------------------------------------

## 🪜 2. Core Idea

This is very similar to 3Sum:
- sort the array
- fix one element
- use two pointers on the rest

Difference:
- instead of finding exact zero
- we minimize:

`abs(currentSum - target)`

------------------------------------------------------------------------

## 🔁 3. Steps

1. Sort the array
2. Initialize `bestSum` as sum of first 3 elements
3. For each index `i`:
   - set `left = i + 1`
   - set `right = n - 1`
4. Compute current sum
5. If current sum is closer to target, update best
6. If sum < target → move `left++`
7. If sum > target → move `right--`
8. If sum == target → return immediately

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class ThreeSumClosest {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;

        int bestSum = nums[0] + nums[1] + nums[2];

        for (int i = 0; i < n - 2; i++) {
            int left = i + 1;
            int right = n - 1;

            while (left < right) {
                int currSum = nums[i] + nums[left] + nums[right];

                if (Math.abs(currSum - target) < Math.abs(bestSum - target)) {
                    bestSum = currSum;
                }

                if (currSum < target) {
                    left++;
                } else if (currSum > target) {
                    right--;
                } else {
                    return currSum;
                }
            }
        }

        return bestSum;
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(1) extra excluding sort cost

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`nums = [-1,2,1,-4]`, `target = 1`

After sorting:
- `[-4,-1,1,2]`

Initialize:
- bestSum = -4 + -1 + 1 = -4

Fix `i = 0`:
- left = 1, right = 3
- currSum = -4 + -1 + 2 = -3
- closer than -4 → update best = -3
- currSum < target → left++

- left = 2, right = 3
- currSum = -4 + 1 + 2 = -1
- closer → update best = -1
- currSum < target → left++

Fix `i = 1`:
- left = 2, right = 3
- currSum = -1 + 1 + 2 = 2
- closer than -1 → update best = 2

Answer = `2`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is the standard variation of:
- sort + fix one element + two pointers

Use this when:
- need triplet sum
- not exact answer but closest / nearest answer

Related:
- 3Sum
- 4Sum
- Closest pair / nearest target two-pointer variants

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- No need to skip duplicates here for correctness
  - because we only need best sum, not unique triplets
- Can return immediately if exact target found
- Initialize `bestSum` safely with first three elements

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Same structure as 3Sum
- But instead of exact matching, keep track of closest sum
- Move pointers based on whether current sum is smaller or larger than target

------------------------------------------------------------------------

# End of Notes
