# Two Pointers Notes

## 02 - Trapping Rain Water (LC 42)

**Generated on:** 2026-03-23 01:06:22 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an array `height` representing elevation bars, compute how much water can be trapped after raining.

Example:
- `height = [0,1,0,2,1,0,1,3,2,1,2,1]` → `6`

For each index:
- trapped water = `min(maxLeft, maxRight) - height[i]`

Brute force:
- for every index, compute max to left and max to right
- O(n^2)

Better:
- precompute leftMax and rightMax arrays → O(n) time, O(n) space

Best:
- two pointers → O(n) time, O(1) space

------------------------------------------------------------------------

## 🪜 2. Core Two-Pointer Idea

Maintain:
- `left` at start
- `right` at end
- `leftMax` = highest wall seen from left
- `rightMax` = highest wall seen from right

Key observation:
- If `height[left] <= height[right]`, then water at `left` depends only on `leftMax`
- because right side definitely has something at least as tall as `height[right]`, which is enough to bound left side
- similarly for right side

So:
- process the smaller side first

------------------------------------------------------------------------

## 🔁 3. Steps

1. Initialize `left = 0`, `right = n - 1`
2. Maintain `leftMax`, `rightMax`
3. If `height[left] <= height[right]`
   - if `height[left] >= leftMax`, update `leftMax`
   - else add `leftMax - height[left]` to water
   - move `left++`
4. Else
   - if `height[right] >= rightMax`, update `rightMax`
   - else add `rightMax - height[right]`
   - move `right--`
5. Continue until pointers cross

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class TrappingRainWater {
    public int trap(int[] height) {
        int left = 0, right = height.length - 1;
        int leftMax = 0, rightMax = 0;
        int water = 0;

        while (left <= right) {
            if (height[left] <= height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    water += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    water += rightMax - height[right];
                }
                right--;
            }
        }

        return water;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`height = [4,2,0,3,2,5]`

- left=0, right=5
- leftMax=4, rightMax=5 eventually
- at index 1: trapped = 4 - 2 = 2
- at index 2: trapped = 4 - 0 = 4
- at index 3: trapped = 4 - 3 = 1
- at index 4: trapped = 4 - 2 = 2

Total = `9`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- every index answer depends on left-side best and right-side best
- there is a monotonic “smaller side determines the bound” observation

This is a classic:
- **two pointers with running maxima**

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty array → 0
- Very small arrays (< 3 bars) → 0
- Process the smaller side first
- Don’t try to trap water at a boundary with no wall on one side

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Water at index depends on `min(leftMax, rightMax)`
- Two-pointer trick avoids storing full left/right max arrays
- This is one of the most famous advanced two-pointer problems

------------------------------------------------------------------------

# End of Notes
