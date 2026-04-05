# Greedy Notes

## Maximum Length Of Pair Chain

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given pairs `pairs[i] = [lefti, righti]`.

A pair `[c, d]` can follow `[a, b]` if:

    b < c

Return the length of the longest chain that can be formed.

**Example 1**

    Input: pairs = [[1,2],[2,3],[3,4]]
    Output: 2

One valid chain is:

    [1,2] -> [3,4]

**Example 2**

    Input: pairs = [[1,2],[7,8],[4,5]]
    Output: 3

------------------------------------------------------------------------

## 2. Greedy Scheduling Insight

This is another interval scheduling problem.

To maximize how many pairs survive, keep the pair that ends earliest.

Why?

Because a smaller ending value leaves the most room for future pairs.

------------------------------------------------------------------------

## 3. Revision-Friendly Strategy

Sort pairs by their second value.

Then scan:

- if the current pair starts after the end of the last chosen pair, take it

That yields the maximum chain length.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    pairs = [[1,2],[2,3],[3,4]]

Sorted by end:

    [[1,2],[2,3],[3,4]]

Take `[1,2]`.

- `[2,3]` cannot follow because `2 < 2` is false
- `[3,4]` can follow

Answer:

    2

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int findLongestChain(int[][] pairs) {
    Arrays.sort(pairs, Comparator.comparingInt(a -> a[1]));

    int ans = 0;
    int prevEnd = Integer.MIN_VALUE;

    for (int[] pair : pairs) {
        if (pair[0] > prevEnd) {
            ans++;
            prevEnd = pair[1];
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

- sort by end
- greedily keep the earliest finishing compatible pair

------------------------------------------------------------------------

## End of Notes
