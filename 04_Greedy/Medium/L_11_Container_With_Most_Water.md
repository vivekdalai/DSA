# Greedy Notes

## Container With Most Water

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/container-with-most-water/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given an array `height`, where `height[i]` is the height of a vertical line at index `i`.

Choose two lines that together with the x-axis form a container.

Return the maximum amount of water the container can store.

**Example 1**

    Input: height = [1,8,6,2,5,4,8,3,7]
    Output: 49

The best pair is at indices `1` and `8`:

    width = 8 - 1 = 7
    height = min(8, 7) = 7
    area = 49

**Example 2**

    Input: height = [1,1]
    Output: 1


<!-- leetcode-images-start -->
**Official LeetCode Image**

![LeetCode image 1](https://s3-lc-upload.s3.amazonaws.com/uploads/2018/07/17/question_11.jpg)

<!-- leetcode-images-end -->
------------------------------------------------------------------------

## 2. Two-Pointer Intuition

Area is:

    min(height[l], height[r]) * (r - l)

If we keep the shorter wall, the height cap does not improve.

So after checking one pair:

- move the shorter side inward
- keep the taller side for now

That is the only move that can potentially increase the minimum height enough to beat the lost width.

------------------------------------------------------------------------

## 3. Greedy Pointer Rule

Start with the widest container:

    l = 0, r = n - 1

At each step:

- compute current area
- update answer
- if `height[l] < height[r]`, move `l++`
- otherwise move `r--`

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    height = [1,8,6,2,5,4,8,3,7]

First pair:

    l = 0, r = 8
    area = min(1, 7) * 8 = 8

Left wall is shorter, so move `l`.

Next strong pair:

    l = 1, r = 8
    area = min(8, 7) * 7 = 49

That becomes the best answer.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maxArea(int[] height) {
    int l = 0, r = height.length - 1, maxArea = 0;

    while (l < r) {
        maxArea = Math.max(maxArea, Math.min(height[l], height[r]) * (r - l));

        if (height[l] < height[r]) l++;
        else r--;
    }

    return maxArea;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- start from both ends
- discard the weaker side after each evaluation

------------------------------------------------------------------------

## End of Notes
