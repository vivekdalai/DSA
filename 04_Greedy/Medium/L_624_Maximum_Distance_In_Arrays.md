# Greedy Notes

## Maximum Distance In Arrays

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/maximum-distance-in-arrays/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given `arrays`, where each inner array is sorted in ascending order.

Choose one integer from one array and another integer from a different array.

Return the maximum distance:

    |a - b|

**Example 1**

    Input: arrays = [[1,2,3],[4,5],[1,2,3]]
    Output: 4

Take `1` from the third array and `5` from the second array.

**Example 2**

    Input: arrays = [[1],[1]]
    Output: 0

------------------------------------------------------------------------

## 2. Running Extremes Intuition

For each new array, the only candidates that matter are:

- its smallest element
- its largest element

And they must be compared with global extremes seen in earlier arrays:

- `currMin`
- `currMax`

That guarantees the two chosen numbers come from different arrays.

------------------------------------------------------------------------

## 3. One-Pass Logic

Initialize using the first array:

- `currMin = first element`
- `currMax = last element`

Then for each later array:

- try `abs(currentMin - currMax)`
- try `abs(currentMax - currMin)`
- update the global answer
- then refresh `currMin` and `currMax`

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    arrays = [[1,2,3],[4,5],[1,2,3]]

Start with:

    currMin = 1, currMax = 3

Second array `[4,5]`:

- `|4 - 3| = 1`
- `|5 - 1| = 4`

Best becomes `4`.

Third array does not improve it.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maxDistance(List<List<Integer>> arrays) {
    int currMin = arrays.get(0).get(0);
    int currMax = arrays.get(0).get(arrays.get(0).size() - 1);
    int diff = Integer.MIN_VALUE;

    for (int i = 1; i < arrays.size(); i++) {
        List<Integer> curr = arrays.get(i);
        int first = curr.get(0);
        int last = curr.get(curr.size() - 1);

        diff = Math.max(diff, Math.abs(first - currMax));
        diff = Math.max(diff, Math.abs(last - currMin));

        currMin = Math.min(currMin, first);
        currMax = Math.max(currMax, last);
    }

    return diff;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(k)` where `k` is the number of arrays
- **Space:** `O(1)`

Pattern:

- maintain global min and max from previous groups
- compare each new group before updating the extremes

------------------------------------------------------------------------

## End of Notes
