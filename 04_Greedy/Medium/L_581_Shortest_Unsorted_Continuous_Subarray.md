# Greedy Notes

## Shortest Unsorted Continuous Subarray

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/shortest-unsorted-continuous-subarray/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an integer array `nums`, return the length of the shortest continuous subarray that, if sorted, would make the whole array sorted.

**Example 1**

    Input: nums = [2,6,4,8,10,9,15]
    Output: 5

Because sorting:

    [6,4,8,10,9]

makes the full array sorted.

**Example 2**

    Input: nums = [1,2,3,4]
    Output: 0

------------------------------------------------------------------------

## 2. Three Views In The File

The file includes:

- a monotonic stack approach
- a brute-force expansion
- a clean linear scan

For revision, the linear scan version `v3` is the easiest to remember.

------------------------------------------------------------------------

## 3. Linear Boundary Insight

From left to right:

- keep the maximum seen so far
- if `nums[i] < maxSeen`, then index `i` must belong to the unsorted window

From right to left:

- keep the minimum seen so far
- if `nums[i] > minSeen`, then index `i` must belong to the unsorted window

The final window is:

    [start, end]

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [2,6,4,8,10,9,15]

Left scan finds violations at:

- `4`
- `9`

So `end` moves rightward.

Right scan finds violations at:

- `6`
- `10`

So `start` moves leftward.

Final window:

    [1,5]

Length:

    5

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static int findUnsortedSubarray_v3(int[] nums) {
    int n = nums.length;
    int maxm = Integer.MIN_VALUE;
    int minm = Integer.MAX_VALUE;
    int start = -1, end = -2;

    for (int i = 0; i < n; i++) {
        maxm = Math.max(maxm, nums[i]);
        minm = Math.min(minm, nums[n - i - 1]);

        if (nums[i] < maxm) end = i;
        if (nums[n - i - 1] > minm) start = n - i - 1;
    }

    return end - start + 1;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- left scan for right boundary
- right scan for left boundary

------------------------------------------------------------------------

## End of Notes
