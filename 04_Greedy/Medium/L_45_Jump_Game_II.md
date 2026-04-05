# Greedy Notes

## Jump Game II

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/jump-game-ii/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given an array `nums`, where `nums[i]` is the maximum jump length from index `i`.

Return the minimum number of jumps needed to reach the last index.

You can assume the last index is always reachable.

**Example 1**

    Input: nums = [2,3,1,1,4]
    Output: 2

Because:

    0 -> 1 -> 4

**Example 2**

    Input: nums = [2,3,0,1,4]
    Output: 2

------------------------------------------------------------------------

## 2. Level-by-Level Intuition

The file treats jumps like BFS levels on an implicit graph.

At any moment:

- indices from `left` to `right` are all positions reachable using the current number of jumps

From that whole range, compute the farthest next reach.

Then move to the next level and increment the jump count once.

------------------------------------------------------------------------

## 3. Greedy Window Expansion

Loop while the current window does not already reach the end:

- scan every index in `[left, right]`
- compute:

    farthest = max(i + nums[i])

- next window becomes:

    left = right + 1
    right = farthest

- increment `jumps`

This guarantees each jump covers the maximum possible next frontier.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [2,3,1,1,4]

Start:

    left = 0, right = 0, jumps = 0

Window `[0,0]` can reach up to `2`.

Next:

    left = 1, right = 2, jumps = 1

Window `[1,2]` can reach up to `4`.

Next:

    right = 4, jumps = 2

We reached the end.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int jump(int[] nums) {
    int jumps = 0, left = 0, right = 0;
    int n = nums.length;

    while (right < n - 1) {
        int farthest = 0;

        for (int i = left; i <= right; i++) {
            farthest = Math.max(farthest, i + nums[i]);
        }

        left = right + 1;
        right = farthest;
        jumps++;
    }

    return jumps;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- treat reachability windows as BFS levels
- one full window corresponds to one jump

------------------------------------------------------------------------

## End of Notes
