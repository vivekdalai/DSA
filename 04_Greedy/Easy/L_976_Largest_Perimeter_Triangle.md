# Greedy Notes

## 18 - Largest Perimeter Triangle

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/largest-perimeter-triangle/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an integer array `nums`, return the largest possible perimeter of a triangle with non-zero area formed from three of these lengths.

If no such triangle can be formed, return `0`.

**Example 1**

    Input: nums = [2, 1, 2]
    Output: 5

**Example 2**

    Input: nums = [1, 2, 1]
    Output: 0

------------------------------------------------------------------------

## 2. Triangle Condition

After sorting:

    a <= b <= c

three sides form a valid triangle if:

    a + b > c

That is the only condition we need to check.

------------------------------------------------------------------------

## 3. Greedy Search Direction

Sort the array in ascending order.

Then scan from the largest side downward.

For each triple:

    nums[i - 2], nums[i - 1], nums[i]

if it satisfies the triangle rule, return its perimeter immediately.

Why immediately?

Because we are scanning from the largest possible side, so the first valid triple gives the maximum perimeter.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [3, 6, 2, 3]

Sorted:

    [2, 3, 3, 6]

Check from the end:

- `(3, 3, 6)` -> `3 + 3 = 6`, not valid
- `(2, 3, 3)` -> `2 + 3 > 3`, valid

Perimeter:

    2 + 3 + 3 = 8

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int largestPerimeter(int[] nums) {
    Arrays.sort(nums);

    for (int i = nums.length - 1; i >= 2; i--) {
        if (nums[i - 2] + nums[i - 1] > nums[i]) {
            return nums[i - 2] + nums[i - 1] + nums[i];
        }
    }

    return 0;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort
- validate local triples
- scan from the largest candidate downward

------------------------------------------------------------------------

## End of Notes
