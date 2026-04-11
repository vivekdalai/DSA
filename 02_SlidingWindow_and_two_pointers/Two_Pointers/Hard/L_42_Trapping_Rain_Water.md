# Two Pointers Notes

## 42 - Trapping Rain Water

**Generated on:** 2026-04-06 02:07:20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/trapping-rain-water/description/
<!-- leetcode-link-end -->

## 1. LeetCode Question Statement

You are given an array `height` where each value represents the height of a bar of width `1`.

After rain, water can collect between taller bars. Return the total amount of trapped water.

![Official example diagram](https://assets.leetcode.com/uploads/2018/10/22/rainwatertrap.png)

**Example 1**

```text
Input: height = [0,1,0,2,1,0,1,3,2,1,2,1]
Output: 6
```

**Example 2**

```text
Input: height = [4,2,0,3,2,5]
Output: 9
```

Useful constraint to remember:

- `1 <= height.length <= 2 * 10^4`
- `0 <= height[i] <= 10^5`

------------------------------------------------------------------------

## 2. Intuition

Water above an index depends on two things:

- the tallest bar seen on the left
- the tallest bar seen on the right

At index `i`, trapped water is:

```text
min(leftMax, rightMax) - height[i]
```

The brute-force thought is to compute `leftMax` and `rightMax` for every position.

This file avoids that extra space. It keeps two pointers and two running maxima:

- `lMax` for the best wall seen from the left
- `rMax` for the best wall seen from the right

The key decision is simple:

- if `height[l] <= height[r]`, the left side is safe to process now
- otherwise, the right side is safe to process now

That is the greedy step in this implementation.

------------------------------------------------------------------------

## 3. Why The Two-Pointer Greedy Works

The code does this:

```java
if (height[l] <= height[r]) {
    ...
    l++;
} else {
    ...
    r--;
}
```

Why is that valid?

When `height[l] <= height[r]`, there is already a bar on the right that is at least as tall as `height[l]`.
So the amount of water at `l` is controlled entirely by `lMax`:

- if `lMax > height[l]`, we trap `lMax - height[l]`
- otherwise this bar becomes the new left maximum

We do not need the exact final `rMax` yet, because the right boundary is already tall enough for the current left index to be decided.

The same logic applies symmetrically when processing `r`.

This is why the file can solve the problem in one pass with `O(1)` extra space instead of building DP arrays or using a stack.

------------------------------------------------------------------------

## 4. Code-Aligned Explanation

Your Java method:

```java
public static int trap(int[] height) {
    int lMax = 0, rMax = 0, total = 0, l = 0, r = height.length - 1;

    while (l < r) {
        if (height[l] <= height[r]) {
            if (lMax > height[l]) total += lMax - height[l];
            else lMax = height[l];
            l++;
        } else {
            if (rMax > height[r]) total += rMax - height[r];
            else rMax = height[r];
            r--;
        }
    }

    return total;
}
```

What each variable means:

- `l` and `r` scan from both ends
- `lMax` is the tallest wall seen so far from the left
- `rMax` is the tallest wall seen so far from the right
- `total` stores the accumulated trapped water

What happens inside the loop:

1. Compare the current boundary heights.
2. Process the smaller side, because that side's answer can already be finalized.
3. If the running max is taller than the current bar, add trapped water.
4. Otherwise, update the running max.
5. Move that pointer inward.

------------------------------------------------------------------------

## 5. Short Dry Run

Take:

```text
height = [4,2,0,3,2,5]
```

Start:

```text
l = 0, r = 5
lMax = 0, rMax = 0, total = 0
```

Walkthrough:

- `height[0] = 4`, `height[5] = 5` -> process left
  `lMax = 4`
- `height[1] = 2`, right is still taller -> process left
  trap `4 - 2 = 2`, total = `2`
- `height[2] = 0` -> trap `4 - 0 = 4`, total = `6`
- `height[3] = 3` -> trap `4 - 3 = 1`, total = `7`
- `height[4] = 2` -> trap `4 - 2 = 2`, total = `9`

Answer:

```text
9
```

The important pattern is that the right side being taller let the code safely finish left positions one by one.

------------------------------------------------------------------------

## 6. Clean Interview Version

```java
public static int trap(int[] height) {
    int l = 0;
    int r = height.length - 1;
    int lMax = 0;
    int rMax = 0;
    int total = 0;

    while (l < r) {
        if (height[l] <= height[r]) {
            if (lMax > height[l]) {
                total += lMax - height[l];
            } else {
                lMax = height[l];
            }
            l++;
        } else {
            if (rMax > height[r]) {
                total += rMax - height[r];
            } else {
                rMax = height[r];
            }
            r--;
        }
    }

    return total;
}
```

------------------------------------------------------------------------

## 7. Complexity

- **Time:** `O(n)`
- **Space:** `O(1)`

Each index is processed at most once, and the solution uses only a few variables.

------------------------------------------------------------------------

## 8. Pattern Recognition / Revision Notes

- If water at an index depends on both left and right boundaries, first think in terms of `leftMax` and `rightMax`.
- A full DP version precomputes both arrays, but if only the final total is needed, try compressing it into two pointers.
- The revision rule to remember is: process the side with the smaller current height.
- This problem is also solvable with a monotonic stack, but this file uses the tighter `O(1)` space two-pointer form.
- When explaining correctness in interviews, say: "the smaller side is the bottleneck, so its trapped water can be finalized now."

------------------------------------------------------------------------

## End of Notes
