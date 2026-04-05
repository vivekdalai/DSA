# Greedy Notes

## Non Overlapping Intervals

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given an array of intervals, return the minimum number of intervals you need to remove to make the rest non-overlapping.

**Example 1**

    Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
    Output: 1

Remove `[1,3]`.

**Example 2**

    Input: intervals = [[1,2],[1,2],[1,2]]
    Output: 2

------------------------------------------------------------------------

## 2. Greedy Scheduling Insight

To keep as many intervals as possible, always keep the interval that finishes earliest.

Why?

Because an earlier finishing interval leaves the most room for future intervals.

That is the exact same logic as interval scheduling.

------------------------------------------------------------------------

## 3. File Logic

The file sorts intervals by end time.

Then it keeps:

- `prevInterval` = last interval that survived
- `skip` = number removed

For each next interval:

- if it starts before `prevInterval` ends, it overlaps and must be removed
- otherwise keep it and update `prevInterval`

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    intervals = [[1,2],[2,3],[3,4],[1,3]]

Sorted by end:

    [[1,2],[1,3],[2,3],[3,4]]

Keep `[1,2]`.

- `[1,3]` overlaps -> remove
- `[2,3]` does not overlap -> keep
- `[3,4]` does not overlap -> keep

Removed:

    1

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int eraseOverlapIntervals(int[][] intervals) {
    Arrays.sort(intervals, (a, b) -> a[1] - b[1]);

    int[] prevInterval = intervals[0];
    int skip = 0;

    for (int i = 1; i < intervals.length; i++) {
        if (intervals[i][0] < prevInterval[1]) {
            skip++;
        } else {
            prevInterval = intervals[i];
        }
    }

    return skip;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort by finishing time
- keep the interval that leaves maximum room for the future

------------------------------------------------------------------------

## End of Notes
