# Greedy Notes

## 13 - Array Partition

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given an array `nums` of `2n` integers, pair them into `n` pairs.

Return the maximum possible value of:

    sum(min(ai, bi))

over all pairs.

**Example 1**

    Input: nums = [1, 4, 3, 2]
    Output: 4

One optimal pairing is:

    (1, 2) and (3, 4)

So:

    min(1, 2) + min(3, 4) = 1 + 3 = 4

**Example 2**

    Input: nums = [6, 2, 6, 5, 1, 2]
    Output: 9

------------------------------------------------------------------------

## 2. Sorting Insight

We want the smaller member of each pair to be as large as possible.

If a very small number is paired with a very large number, that large value is mostly wasted because only the smaller one contributes.

So after sorting, the best strategy is to pair adjacent numbers.

Then the smaller member of each pair is simply every even-indexed element.

------------------------------------------------------------------------

## 3. Why Adjacent Pairing Works

Sorted array:

    a0 <= a1 <= a2 <= a3 <= ...

If we pair adjacent values:

    (a0, a1), (a2, a3), ...

then the chosen minima become:

    a0 + a2 + a4 + ...

Any cross pairing can only push a smaller number into a minimum position and cannot improve the sum.

------------------------------------------------------------------------

## 4. Quick Walkthrough

**Input:**

    nums = [1, 4, 3, 2]

Sorted:

    [1, 2, 3, 4]

Take every first element of each adjacent pair:

    1 + 3 = 4

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int arrayPairSum(int[] nums) {
    Arrays.sort(nums);

    int ans = 0;
    for (int i = 0; i < nums.length; i += 2) {
        ans += nums[i];
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort
- pair neighbors
- sum the smaller item from each pair

------------------------------------------------------------------------

## End of Notes
