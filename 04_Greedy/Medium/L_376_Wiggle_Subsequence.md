# Greedy Notes

## Wiggle Subsequence

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

A wiggle sequence is one where successive differences strictly alternate between positive and negative.

Given an integer array `nums`, return the length of the longest wiggle subsequence.

**Example 1**

    Input: nums = [1,7,4,9,2,5]
    Output: 6

**Example 2**

    Input: nums = [1,17,5,10,13,15,10,5,16,8]
    Output: 7

------------------------------------------------------------------------

## 2. Greedy Sign Change Insight

We do not need the exact best subsequence at every point.

We only care whether the current difference changes sign from the previous chosen difference.

Whenever the sign flips:

- we extend the wiggle length

If the sign does not flip, keeping the more extreme endpoint is always at least as good for future wiggles.

------------------------------------------------------------------------

## 3. Two Versions In The File

### Difference-tracking version

- store `prevDiff`
- whenever `diff` changes sign appropriately, increment answer

### `up / down` DP-style version

- `up` = best wiggle length ending with an upward move
- `down` = best wiggle length ending with a downward move

Both run in linear time.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [1,7,4,9,2,5]

Differences:

    +6, -3, +5, -7, +3

The sign alternates every time, so every element can stay.

Answer:

    6

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static int wiggleMaxLength(int[] nums) {
    int n = nums.length;
    if (n < 2) return n;

    int prevDiff = nums[1] - nums[0];
    int ans = prevDiff != 0 ? 2 : 1;

    for (int i = 2; i < n; i++) {
        int diff = nums[i] - nums[i - 1];
        if ((diff > 0 && prevDiff <= 0) || (diff < 0 && prevDiff >= 0)) {
            ans++;
            prevDiff = diff;
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- keep only turning points
- ignore interior points on the same slope

------------------------------------------------------------------------

## End of Notes
