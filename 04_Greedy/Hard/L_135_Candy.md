# Greedy Notes

## 01 - Candy

**Generated on:** 2026-04-06 01:00:13 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

There are `n` children standing in a line.

Each child has a rating stored in `ratings[i]`.

You must distribute candies such that:

- Every child gets at least `1` candy
- Any child with a higher rating than an adjacent child gets more candies than that neighbor

Return the minimum candies needed.

**Example 1**

    Input: ratings = [1, 0, 2]
    Output: 5

Explanation:

- Child `0` gets `2`
- Child `1` gets `1`
- Child `2` gets `2`

So the minimum distribution is:

    [2, 1, 2]

**Example 2**

    Input: ratings = [1, 2, 2]
    Output: 4

One valid minimum distribution is:

    [1, 2, 1]

------------------------------------------------------------------------

## 2. First Intuition

This problem looks local, but each child is controlled by two directions:

- The left neighbor may force the candy count upward
- The right neighbor may also force the candy count upward

That is why a single greedy sweep is not enough.

Example:

    ratings = [1, 3, 2, 1]

A left-to-right pass can satisfy the increasing slope `1 -> 3`, but it cannot fully repair the decreasing tail `3 -> 2 -> 1`.

So the right mental model is:

- Compute what each child needs when viewed from the left
- Compute what each child needs when viewed from the right
- Take the stronger requirement

------------------------------------------------------------------------

## 3. Two Directional Promises

Your file shows both versions of the same idea.

### Version 1: two helper arrays

    candiesLR[i] = minimum candies needed to satisfy only the left neighbor rule
    candiesRL[i] = minimum candies needed to satisfy only the right neighbor rule

How they are built:

- In the left-to-right pass, if `ratings[i] > ratings[i - 1]`, then `candiesLR[i] = candiesLR[i - 1] + 1`
- In the right-to-left pass, if `ratings[i] > ratings[i + 1]`, then `candiesRL[i] = candiesRL[i + 1] + 1`

### Version 2: one array

The cleaner method in the file is `candy_v2`.

- Start everyone with `1`
- Left pass fixes increasing slopes
- Right pass fixes decreasing slopes with:

    candies[i] = max(candies[i], candies[i + 1] + 1)

That `max` is the key detail. We do not add both requirements. We only need enough candies to satisfy both sides simultaneously.

------------------------------------------------------------------------

## 4. Why Taking `max` Works

Suppose a child needs:

- `3` candies to stay valid compared to the left neighbor
- `2` candies to stay valid compared to the right neighbor

Giving `3` already satisfies both conditions.

So for each child:

    finalCandies[i] = max(leftRequirement[i], rightRequirement[i])

This is the exact reason the approach stays minimal.

------------------------------------------------------------------------

## 5. Greedy Flow Used in the Optimal Version

Start with:

    candies = [1, 1, 1, ..., 1]

### Pass 1: left to right

If ratings are strictly increasing, the later child must get one extra candy:

    if ratings[i] > ratings[i - 1]:
        candies[i] = candies[i - 1] + 1

### Pass 2: right to left

If ratings are strictly decreasing from left to right, the earlier child must be larger:

    if ratings[i] > ratings[i + 1]:
        candies[i] = max(candies[i], candies[i + 1] + 1)

After that, summing the array gives the minimum answer.

------------------------------------------------------------------------

## 6. Why This Greedy Is Correct

The solution is minimal because:

- The left pass gives the minimum candies needed to satisfy only left comparisons
- The right pass gives the minimum candies needed to satisfy only right comparisons
- Taking the maximum at each index is the smallest value that satisfies both

No child gets extra candies unless one of the adjacency rules forces it.

------------------------------------------------------------------------

## 7. Clean Interview Version

```java
public static int candy_v2(int[] ratings) {
    int n = ratings.length;

    int[] candies = new int[n];
    int totalCandies = 0;

    Arrays.fill(candies, 1);

    for (int i = 1; i < n; i++) {
        if (ratings[i] > ratings[i - 1]) {
            candies[i] = candies[i - 1] + 1;
        }
    }

    for (int i = n - 2; i >= 0; i--) {
        if (ratings[i] > ratings[i + 1]) {
            candies[i] = Math.max(candies[i], candies[i + 1] + 1);
        }
    }

    for (int candy : candies) {
        totalCandies += candy;
    }

    return totalCandies;
}
```

------------------------------------------------------------------------

## 8. Dry Run

**Input:**

    ratings = [1, 0, 2]

Initial candies:

    [1, 1, 1]

Left-to-right pass:

- `i = 1`: `0 > 1`? no
- `i = 2`: `2 > 0`? yes -> candies becomes `[1, 1, 2]`

Right-to-left pass:

- `i = 1`: `0 > 2`? no
- `i = 0`: `1 > 0`? yes -> candies[0] = max(1, 1 + 1) = 2

Final candies:

    [2, 1, 2]

Total:

    2 + 1 + 2 = 5

------------------------------------------------------------------------

## 9. Complexity

- **Time:** `O(n)`
- **Space:** `O(n)`

The first version uses two arrays.

The cleaner version still uses `O(n)` space, but only one helper array.

------------------------------------------------------------------------

## 10. Pattern Recognition

This is a classic **bidirectional greedy** problem.

Whenever a condition depends on both left and right neighbors:

- One directional pass is usually incomplete
- Two passes often isolate the two constraints cleanly

Think:

- left requirement
- right requirement
- combine with `max`

------------------------------------------------------------------------

## End of Notes
