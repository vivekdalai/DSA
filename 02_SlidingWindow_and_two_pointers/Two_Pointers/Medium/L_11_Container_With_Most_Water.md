# Two Pointers Notes

## 11 - Container With Most Water

**Generated on:** 2026-04-08 00:02:17 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/container-with-most-water/description/
<!-- leetcode-link-end -->

## 1. LeetCode Question Statement

Each index gives the height of a vertical line. Choose two lines so that, together with the x-axis, they form the container holding the maximum water.
The area is controlled by the width between indices and the shorter of the two chosen heights.

![Official LeetCode figure](https://s3-lc-upload.s3.amazonaws.com/uploads/2018/07/17/question_11.jpg)

**Example 1**

```text
Input: height = [1,8,6,2,5,4,8,3,7]
Output: 49
Explanation: Using heights `8` and `7` with width `7` gives `7 * 7 = 49`.
```

**Example 2**

```text
Input: height = [1,1]
Output: 1
```

**Constraints**

- `2 <= height.length <= 10^5`
- `0 <= height[i] <= 10^4`

------------------------------------------------------------------------

## 2. Eliminate The Shorter Wall

For a pair `(l, r)`, area is:

- `width = r - l`
- `height = min(height[l], height[r])`

If you keep the shorter wall and move the taller wall inward, width definitely shrinks and the limiting height does not improve. So that move cannot beat the current pair.

The only move that can unlock a larger area is to discard the shorter wall and hope to find a taller one.

------------------------------------------------------------------------

## 3. Why The Pointer Update In This File Is Correct

The method starts with the widest possible container: `l = 0`, `r = n - 1`.

On every step it:

- computes the current area
- updates `maxArea`
- moves the pointer on the shorter side

Actual code rule:

- if `height[r] > height[l]`, move `l++`
- else move `r--`

Ties fall into the `else` branch, so the right pointer moves. That is still safe because either equal-height side can be discarded.

------------------------------------------------------------------------

## 4. Walkthrough on `[1,8,6,2,5,4,8,3,7]`

- Start with `(0, 8)`: area is `min(1, 7) * 8 = 8`
- Left side is shorter, so move left to index `1`
- Pair `(1, 8)`: area is `min(8, 7) * 7 = 49`, best becomes `49`
- Right side is shorter now, so keep shrinking from the right
- No later pair beats `49`

Final answer: `49`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maxArea(int[] height) {
    int left = 0;
    int right = height.length - 1;
    int best = 0;

    while (left < right) {
        int area = Math.min(height[left], height[right]) * (right - left);
        best = Math.max(best, area);

        if (height[right] > height[left]) {
            left++;
        } else {
            right--;
        }
    }

    return best;
}
```

------------------------------------------------------------------------

## 6. Time and Space Complexity

- **Time:** `O(n)`
- **Space:** `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition / Revision Notes

- When the score depends on both ends of a window, try two pointers from the extremes first.
- If one side is mathematically limiting the answer, move that side; this is a common pointer-elimination proof pattern.
- This problem is worth remembering as: widest first, then discard the bottleneck wall.

------------------------------------------------------------------------

## End of Notes
