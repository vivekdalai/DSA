# Greedy Notes

## Minimum Number Of Arrows To Burst Balloons

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Each balloon is represented by an interval `points[i] = [xstart, xend]`.

An arrow shot at position `x` bursts every balloon whose interval contains `x`.

Return the minimum number of arrows needed to burst all balloons.

**Example 1**

    Input: points = [[10,16],[2,8],[1,6],[7,12]]
    Output: 2

**Example 2**

    Input: points = [[1,2],[3,4],[5,6],[7,8]]
    Output: 4

------------------------------------------------------------------------

## 2. Greedy Interval Insight

If multiple balloons overlap, one arrow can hit them all.

So after sorting by end coordinate, we should keep the current overlapping window as tight as possible.

Whenever the next balloon does not overlap with that window, we must fire a new arrow.

------------------------------------------------------------------------

## 3. Logic In The File

The file sorts by interval end.

Then while scanning:

- if the current balloon overlaps with the previous active window, shrink the window to the intersection
- otherwise start a new arrow count

This intersection update keeps the current arrow position valid for as many future balloons as possible.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    points = [[10,16],[2,8],[1,6],[7,12]]

Sorted by end:

    [[1,6],[2,8],[7,12],[10,16]]

First two overlap, so one arrow can cover their intersection.

Then `[7,12]` no longer overlaps with that active window, so we need arrow `2`.

Total:

    2

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static int findMinArrowShots(int[][] points) {
    Arrays.sort(points, (a, b) -> Integer.compare(a[1], b[1]));

    int ans = 1;

    for (int i = 1; i < points.length; i++) {
        if (points[i][0] < points[i - 1][1]) {
            int maxStart = Math.max(points[i][0], points[i - 1][0]);
            int minEnd = Math.min(points[i][1], points[i - 1][1]);
            points[i][0] = maxStart;
            points[i][1] = minEnd;
        } else {
            ans++;
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort overlapping intervals
- merge the active overlap window until a new arrow becomes unavoidable

------------------------------------------------------------------------

## End of Notes
