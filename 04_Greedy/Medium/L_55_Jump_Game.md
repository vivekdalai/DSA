# Greedy Notes

## Jump Game

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given an array `nums`, where `nums[i]` is the maximum jump length from index `i`.

Return `true` if you can reach the last index, otherwise return `false`.

**Example 1**

    Input: nums = [2,3,1,1,4]
    Output: true

**Example 2**

    Input: nums = [3,2,1,0,4]
    Output: false

------------------------------------------------------------------------

## 2. Greedy Reachability

We only need one variable:

    maxReach = farthest index reachable so far

If we ever arrive at an index beyond `maxReach`, then that index is unreachable and the answer is immediately `false`.

Otherwise, update `maxReach` using the current jump.

------------------------------------------------------------------------

## 3. One Pass Rule

For each index `i`:

- if `i > maxReach`, return `false`
- otherwise:

    maxReach = max(maxReach, i + nums[i])

If the scan completes, then every visited index was reachable, so the last index is reachable too.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [3,2,1,0,4]

Process:

- at `0`, maxReach becomes `3`
- at `1`, still within reach
- at `2`, still within reach
- at `3`, still within reach but jump length is `0`
- next index is `4`, but `4 > maxReach`

So the answer is:

    false

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean canJump(int[] nums) {
    int maxReach = 0;

    for (int i = 0; i < nums.length; i++) {
        if (i > maxReach) return false;
        maxReach = Math.max(maxReach, i + nums[i]);
    }

    return true;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- scan while maintaining the farthest reachable boundary

------------------------------------------------------------------------

## End of Notes
