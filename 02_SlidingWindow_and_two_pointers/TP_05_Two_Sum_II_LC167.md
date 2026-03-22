# Two Pointers Notes

## 05 - Two Sum II - Input Array Is Sorted (LC 167)

**Generated on:** 2026-03-23 01:07:37 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a 1-indexed array `numbers` sorted in non-decreasing order, find two numbers such that they add up to a specific target number.

Return:
- indices `[index1, index2]` such that
- `numbers[index1] + numbers[index2] == target`
- `index1 < index2`

Examples:
- `numbers = [2,7,11,15], target = 9` → `[1,2]`
- `numbers = [2,3,4], target = 6` → `[1,3]`

------------------------------------------------------------------------

## 🪜 2. Core Two-Pointer Idea

Because the array is sorted:
- if current sum is too small, move left pointer rightward
- if current sum is too large, move right pointer leftward

Maintain:
- `left = 0`
- `right = n - 1`

This avoids HashMap and works in O(n).

------------------------------------------------------------------------

## 🔁 3. Steps

1. Initialize `left = 0`, `right = n - 1`
2. Compute `sum = numbers[left] + numbers[right]`
3. If sum == target:
   - return indices in 1-based format
4. If sum < target:
   - move `left++`
5. If sum > target:
   - move `right--`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class TwoSumII {
    public int[] twoSum(int[] numbers, int target) {
        int left = 0, right = numbers.length - 1;

        while (left < right) {
            int sum = numbers[left] + numbers[right];

            if (sum == target) {
                return new int[]{left + 1, right + 1}; // 1-based indexing
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }

        return new int[]{-1, -1}; // though problem guarantees one solution
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`numbers = [2,7,11,15], target = 9`

- left = 0, right = 3
- sum = 2 + 15 = 17 > 9 → move right
- left = 0, right = 2
- sum = 2 + 11 = 13 > 9 → move right
- left = 0, right = 1
- sum = 2 + 7 = 9 → found

Return `[1,2]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- array is sorted
- need pair sum / pair target
- want O(n) with O(1) space

This is the most basic:
- **sorted two pointers from both ends**

Related:
- 3Sum
- 4Sum
- Container With Most Water
- Closest sum variants

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Problem uses 1-based indexing in return
- Only works because array is sorted
- If unsorted, use HashMap instead (classic Two Sum LC 1)

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Sorted array + pair target = try two pointers first
- If sum too small, move left
- If sum too large, move right
- Fundamental two-pointer interview pattern

------------------------------------------------------------------------

# End of Notes
