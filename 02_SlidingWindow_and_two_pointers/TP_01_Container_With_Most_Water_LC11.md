# Two Pointers Notes

## 01 - Container With Most Water (LC 11)

**Generated on:** 2026-03-23 01:05:59 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given an integer array `height` where each element represents a vertical line, find two lines that together with the x-axis form a container containing the most water.

Area between indices `i` and `j`:
- `width = j - i`
- `height = min(height[i], height[j])`
- `area = width * min(height[i], height[j])`

Goal:
- maximize area

Examples:
- `height = [1,8,6,2,5,4,8,3,7]` → `49`

------------------------------------------------------------------------

## 🪜 2. Core Two-Pointer Idea

Start with the widest container:
- `left = 0`
- `right = n - 1`

At each step:
1. Compute current area
2. Move the pointer pointing to the smaller height

Why?
- Area is limited by the smaller wall
- Keeping the smaller wall and reducing width can never help
- Only moving the smaller wall gives a chance to find a taller boundary

------------------------------------------------------------------------

## 🔁 3. Steps

1. Initialize `left = 0`, `right = n - 1`
2. Compute area
3. Update max area
4. Move smaller-height pointer inward
5. Continue until `left < right`

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class ContainerWithMostWater {
    public int maxArea(int[] height) {
        int left = 0, right = height.length - 1;
        int maxArea = 0;

        while (left < right) {
            int h = Math.min(height[left], height[right]);
            int w = right - left;
            maxArea = Math.max(maxArea, h * w);

            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
        }

        return maxArea;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`height = [1,8,6,2,5,4,8,3,7]`

- left=0, right=8
  - area = min(1,7) * 8 = 8
  - move left
- left=1, right=8
  - area = min(8,7) * 7 = 49
  - move right
- continue...

Maximum = `49`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- answer depends on a pair from left and right side
- moving one pointer can be justified greedily
- sorted order is not required here, but two-end reasoning is

This is a classic:
- **greedy two pointers from both ends**

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Need at least 2 lines
- Always move the smaller wall, not the larger one
- Equal heights: move either pointer

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Start with maximum width
- Area is limited by smaller height
- Move the smaller pointer inward
- One of the most famous two-pointer interview problems

------------------------------------------------------------------------

# End of Notes
