# Greedy Notes

## Max Chunks To Make Sorted

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/max-chunks-to-make-sorted/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given a permutation of the integers `0` to `n - 1`.

Split the array into the maximum number of chunks such that sorting each chunk individually and concatenating them gives the fully sorted array.

Return that maximum number of chunks.

**Example 1**

    Input: arr = [4,3,2,1,0]
    Output: 1

**Example 2**

    Input: arr = [1,0,2,3,4]
    Output: 4

------------------------------------------------------------------------

## 2. Prefix Invariant

Because the array is a permutation of `0..n-1`, a prefix can form a chunk exactly when it contains the same set of values as the sorted prefix.

The file uses a sum-based shortcut:

- prefix index sum
- prefix value sum

When they match, the prefix has exactly the correct values for one chunk boundary.

------------------------------------------------------------------------

## 3. Why The Sum Trick Works Here

This works because:

- values are unique
- values come from `0` to `n - 1`

So if the sum of values in prefix `[0..i]` equals:

    0 + 1 + ... + i

then the prefix must contain exactly those numbers.

That means it can be sorted independently.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    arr = [1,0,2,3,4]

Prefix sums:

- index `0`: indexSum = `0`, valueSum = `1`
- index `1`: indexSum = `1`, valueSum = `1` -> chunk ends
- index `2`: indexSum = `3`, valueSum = `3` -> chunk ends
- index `3`: indexSum = `6`, valueSum = `6` -> chunk ends
- index `4`: indexSum = `10`, valueSum = `10` -> chunk ends

Total chunks:

    4

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maxChunksToSorted(int[] arr) {
    int idxSum = 0, sum = 0, cnt = 0;

    for (int i = 0; i < arr.length; i++) {
        idxSum += i;
        sum += arr[i];

        if (idxSum == sum) cnt++;
    }

    return cnt;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- detect valid chunk boundaries using a prefix invariant

------------------------------------------------------------------------

## End of Notes
