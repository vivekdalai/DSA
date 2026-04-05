# Greedy Notes

## 06 - Maximum Units on a Truck

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/maximum-units-on-a-truck/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given `boxTypes`, where:

- `boxTypes[i][0]` = number of boxes of this type
- `boxTypes[i][1]` = units per box

You are also given `truckSize`, the maximum number of boxes the truck can carry.

Return the maximum total units that can be loaded.

**Example 1**

    Input: boxTypes = [[1, 3], [2, 2], [3, 1]], truckSize = 4
    Output: 8

One optimal loading is:

- take `1` box of `3` units
- take `2` boxes of `2` units
- take `1` box of `1` unit

**Example 2**

    Input: boxTypes = [[5, 10], [2, 5], [4, 7], [3, 9]], truckSize = 10
    Output: 91

------------------------------------------------------------------------

## 2. Greedy Insight

Each box takes the same amount of truck space:

    1 box slot

So the only thing that matters is:

    units per box

That means we should always load the richest box types first.

------------------------------------------------------------------------

## 3. Strategy In The File

Sort `boxTypes` by units per box in descending order.

Then iterate:

- take as many boxes as possible from the current type
- reduce `truckSize`
- continue until the truck is full

This is exactly like a greedy fractional-style choice, except boxes are taken in whole units.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    boxTypes = [[1, 3], [2, 2], [3, 1]]
    truckSize = 4

After sorting by units:

    [[1, 3], [2, 2], [3, 1]]

Load greedily:

- take `1` box of type `[1, 3]` -> units `3`, capacity left `3`
- take `2` boxes of type `[2, 2]` -> units `4`, capacity left `1`
- take `1` box of type `[3, 1]` -> units `1`

Total:

    3 + 4 + 1 = 8

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static int maximumUnits(int[][] boxTypes, int truckSize) {
    Arrays.sort(boxTypes, (a, b) -> b[1] - a[1]);

    int units = 0;

    for (int[] boxType : boxTypes) {
        if (truckSize == 0) break;

        int take = Math.min(truckSize, boxType[0]);
        units += take * boxType[1];
        truckSize -= take;
    }

    return units;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort items by reward density
- consume the best choices first

------------------------------------------------------------------------

## End of Notes
