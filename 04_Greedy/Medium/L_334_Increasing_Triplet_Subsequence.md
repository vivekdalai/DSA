# Greedy Notes

## Increasing Triplet Subsequence

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given an integer array `nums`, return `true` if there exists an increasing triplet subsequence.

That means indices `i < j < k` exist such that:

    nums[i] < nums[j] < nums[k]

Otherwise, return `false`.

**Example 1**

    Input: nums = [1,2,3,4,5]
    Output: true

**Example 2**

    Input: nums = [5,4,3,2,1]
    Output: false

------------------------------------------------------------------------

## 2. Two Threshold Intuition

We do not need to store full subsequences.

We only need the best possible first two values:

- `f` = smallest first number seen so far
- `s` = smallest second number that can come after `f`

If any later number is bigger than both, then we found:

    f < s < current

------------------------------------------------------------------------

## 3. Greedy Update Rule

For each number `curr`:

- if `curr <= f`, replace `f`
- else if `curr <= s`, replace `s`
- else we found a valid third number, so return `true`

Why minimize `f` and `s`?

Because smaller thresholds make it easier for a future number to complete the triplet.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [2,1,5,0,4,6]

Process:

- `2` -> `f = 2`
- `1` -> `f = 1`
- `5` -> `s = 5`
- `0` -> `f = 0`
- `4` -> `s = 4`
- `6` -> bigger than both `f` and `s`, so answer is `true`

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean increasingTriplet(int[] nums) {
    int f = Integer.MAX_VALUE;
    int s = Integer.MAX_VALUE;

    for (int curr : nums) {
        if (curr <= f) {
            f = curr;
        } else if (curr <= s) {
            s = curr;
        } else {
            return true;
        }
    }

    return false;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- maintain the smallest possible thresholds for a future success

------------------------------------------------------------------------

## End of Notes
