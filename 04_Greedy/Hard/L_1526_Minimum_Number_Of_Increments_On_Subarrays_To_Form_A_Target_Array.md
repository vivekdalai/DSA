# Greedy Notes

## 02 - Minimum Number Of Increments On Subarrays To Form A Target Array

**Generated on:** 2026-04-06 01:00:13 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/minimum-number-of-increments-on-subarrays-to-form-a-target-array/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You start with an array of zeros.

In one operation, you may choose any subarray and increment every element in that subarray by `1`.

Given `target`, return the minimum number of operations needed to build exactly that array.

**Example 1**

    Input: target = [1, 2, 3, 2, 1]
    Output: 3

Explanation:

- Build the whole range once -> `[1, 1, 1, 1, 1]`
- Build middle range `[1..3]` once more -> `[1, 2, 2, 2, 1]`
- Build index `2` once more -> `[1, 2, 3, 2, 1]`

**Example 2**

    Input: target = [3, 1, 1, 2]
    Output: 4

The extra operations are forced by the positive rises in the target shape.

------------------------------------------------------------------------

## 2. Best Way To Visualize It

Do not think in terms of individual operations first.

Think of `target` as a skyline or histogram.

Each operation adds one horizontal layer over some continuous range.

That changes the question into:

- How many new layers start at each index?

The answer is:

- At index `0`, we need `target[0]` layers
- At any later index `i`, we only need extra work if `target[i]` rises above `target[i - 1]`

So every positive jump creates new operations.

------------------------------------------------------------------------

## 3. Greedy Formula

The file uses the cleanest possible form:

    ans = target[0]

    for i from 1 to n - 1:
        if target[i] > target[i - 1]:
            ans += target[i] - target[i - 1]

Equivalent formula:

    answer = target[0] + sum(max(0, target[i] - target[i - 1]))

That is the entire solution.

------------------------------------------------------------------------

## 4. Why Decreases Add Nothing

Suppose:

    previous height = 5
    current height  = 3

We do not need new operations for the drop.

Why?

Because the old operations that were covering height `5` can simply stop before extending further.

So a decrease does not force extra increments.

Only a new rise forces new layers to begin.

------------------------------------------------------------------------

## 5. Intuition Behind Correctness

Every operation contributes to a continuous segment.

So if a column becomes taller than the previous one by `d`, there must be `d` new operations starting there or earlier and still covering that position.

The cheapest choice is to start exactly those `d` new layers at the rise point.

That makes the greedy count both:

- Necessary
- Sufficient

There is no cheaper way to create a positive jump.

------------------------------------------------------------------------

## 6. Clean Interview Version

```java
public static int minNumberOperations(int[] target) {
    int ans = target[0];

    for (int i = 1; i < target.length; i++) {
        if (target[i] > target[i - 1]) {
            ans += target[i] - target[i - 1];
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 7. Dry Run

**Input:**

    target = [1, 3, 2, 3, 1]

Start:

    ans = target[0] = 1

Walk through adjacent differences:

- `i = 1`: `3 > 1`, add `2` -> `ans = 3`
- `i = 2`: `2 > 3`? no -> `ans = 3`
- `i = 3`: `3 > 2`, add `1` -> `ans = 4`
- `i = 4`: `1 > 3`? no -> `ans = 4`

Final answer:

    4

One way to read that:

- `1` layer starts at index `0`
- `2` extra layers start at index `1`
- `1` extra layer starts at index `3`

------------------------------------------------------------------------

## 8. Complexity

- **Time:** `O(n)`
- **Space:** `O(1)`

This is why the problem is much easier than it first appears.

There is no DP table and no simulation of operations.

------------------------------------------------------------------------

## 9. Pattern Recognition

This is a **build-from-zero greedy** problem.

Clues that suggest this pattern:

- Start with all zeros
- Allowed operation affects a contiguous range
- Need the minimum number of operations
- Adjacent height differences matter more than absolute positions

Whenever a target structure is formed by adding layers, check whether:

    answer = first value + positive rises

------------------------------------------------------------------------

## End of Notes
