# Two Pointers Notes

## 03 - 3Sum (LC 15)

**Generated on:** 2026-03-23 01:06:47 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `nums`, return all unique triplets `[nums[i], nums[j], nums[k]]` such that:
- `i != j`, `i != k`, `j != k`
- `nums[i] + nums[j] + nums[k] == 0`

Examples:
- `nums = [-1,0,1,2,-1,-4]` → `[[-1,-1,2],[-1,0,1]]`

Important:
- Need unique triplets
- Brute force O(n^3) is too slow

------------------------------------------------------------------------

## 🪜 2. Core Idea

Sort the array first.

Then:
- fix one element `nums[i]`
- solve the remaining problem as:
  - find two numbers in `nums[i+1 ... n-1]` whose sum is `-nums[i]`

That inner problem is classic two pointers:
- `left = i + 1`
- `right = n - 1`

Move pointers based on the current sum.

------------------------------------------------------------------------

## 🔁 3. Steps

1. Sort the array
2. For each index `i`:
   - skip duplicates for `i`
   - set `target = -nums[i]`
   - use two pointers on remaining part
3. If sum is too small → move `left++`
4. If sum is too large → move `right--`
5. If sum matches:
   - store triplet
   - skip duplicate values for `left` and `right`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class ThreeSum {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue; // skip duplicate first element

            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];

                if (sum == 0) {
                    ans.add(Arrays.asList(nums[i], nums[left], nums[right]));

                    left++;
                    right--;

                    while (left < right && nums[left] == nums[left - 1]) left++;
                    while (left < right && nums[right] == nums[right + 1]) right--;
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return ans;
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(1) extra excluding answer list

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`nums = [-1,0,1,2,-1,-4]`

After sorting:
- `[-4,-1,-1,0,1,2]`

Fix `i = 1` → `nums[i] = -1`
- left = 2, right = 5
- sum = -1 + (-1) + 2 = 0 → add `[-1,-1,2]`
- move both pointers

Then:
- left = 3, right = 4
- sum = -1 + 0 + 1 = 0 → add `[-1,0,1]`

Answer:
- `[[-1,-1,2], [-1,0,1]]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- need unique triplets / quadruplets
- array can be sorted
- reduce problem to 2Sum with two pointers

This is the standard:
- **sort + fix one element + two pointers**

Related:
- 2Sum II
- 4Sum
- 3Sum Closest

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Must sort first
- Must skip duplicates:
  - for fixed `i`
  - after finding a valid triplet
- Don’t use HashSet-only brute force if interviewer expects two-pointers optimization
- Need `left < right`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Sort array
- Fix one element
- Solve remaining 2Sum with two pointers
- Skip duplicates carefully
- One of the most standard two-pointer interview problems

------------------------------------------------------------------------

# End of Notes
