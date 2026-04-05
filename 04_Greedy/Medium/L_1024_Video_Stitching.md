# Greedy Notes

## Video Stitching

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/video-stitching/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given video clips, where `clips[i] = [starti, endi]` means that clip `i` covers the interval `[starti, endi]`.

You may cut clips freely and choose any subset of them.

Return the minimum number of clips needed to cover the full interval `[0, time]`.

If it is impossible, return `-1`.

**Example 1**

    Input: clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], time = 10
    Output: 3

One optimal choice is:

    [0,2], [1,9], [8,10]

**Example 2**

    Input: clips = [[0,1],[1,2]], time = 5
    Output: -1

------------------------------------------------------------------------

## 2. Core Intuition

This is the same shape as minimum-interval covering.

At any current covered endpoint `currEnd`, we should look at every clip that starts at or before `currEnd` and choose the one that pushes coverage farthest.

If we do not take the farthest reachable extension now, we can only make future choices weaker.

------------------------------------------------------------------------

## 3. Greedy Sweep

The file sorts clips by start time.

Then it keeps:

- `currEnd` = coverage already committed
- `farthest` = best extension among clips that can start now
- `count` = chosen clips

Loop:

- collect all clips with `start <= currEnd`
- update `farthest`
- if `farthest == currEnd`, we are stuck, so return `-1`
- otherwise commit one clip jump:

    currEnd = farthest
    count++

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    clips = [[0,2],[1,5],[1,9],[4,6],[5,9],[8,10]], time = 10

Start:

    currEnd = 0

Reachable from `0`:

- `[0,2]` -> farthest becomes `2`

Commit first clip:

    currEnd = 2, count = 1

Reachable from `2`:

- `[1,5]`
- `[1,9]`

Best extension is `9`.

Commit second clip:

    currEnd = 9, count = 2

Reachable from `9`:

- `[4,6]`
- `[5,9]`
- `[8,10]`

Best extension is `10`.

Commit third clip:

    currEnd = 10, count = 3

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int videoStitching(int[][] clips, int time) {
    Arrays.sort(clips, (a, b) -> a[0] - b[0]);

    int currEnd = 0, farthest = 0, i = 0, count = 0, n = clips.length;

    while (currEnd < time) {
        while (i < n && clips[i][0] <= currEnd) {
            farthest = Math.max(farthest, clips[i][1]);
            i++;
        }

        if (farthest == currEnd) return -1;

        currEnd = farthest;
        count++;
    }

    return count;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort intervals
- among all currently usable intervals, commit the farthest reach

------------------------------------------------------------------------

## End of Notes
