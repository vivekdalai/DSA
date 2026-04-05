# Greedy Notes

## Valid Triangle Number

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given an integer array `nums`, return the number of triplets that can form a triangle.

Three lengths `a`, `b`, and `c` form a triangle if:

    a + b > c

after sorting so that `a <= b <= c`.

**Example 1**

    Input: nums = [2,2,3,4]
    Output: 3

Valid triplets are:

    (2,3,4), (2,3,4), (2,2,3)

**Example 2**

    Input: nums = [4,2,3,4]
    Output: 4

------------------------------------------------------------------------

## 2. Sorted Two-Pointer Intuition

After sorting, fix the largest side `nums[i]`.

Now we need pairs in the prefix such that:

    nums[l] + nums[r] > nums[i]

If this is true for some `l`, then it is also true for every index between `l` and `r - 1`, because the array is sorted.

That is where the count jump comes from.

------------------------------------------------------------------------

## 3. Greedy Counting Rule

Sort the array first.

Then for each `i` from right to left:

- set `l = 0`, `r = i - 1`
- if `nums[l] + nums[r] > nums[i]`, then every index from `l` to `r - 1` works

So:

    ans += r - l
    r--

Otherwise:

    l++

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    nums = [2,2,3,4]

Sorted array stays:

    [2,2,3,4]

Fix `4`:

- `2 + 3 > 4`, so one valid pair with `r = 2`:
  add `2 - 0 = 2`

Fix `3`:

- `2 + 2 > 3`, so add `1`

Total:

    3

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int triangleNumber(int[] nums) {
    Arrays.sort(nums);
    int n = nums.length, ans = 0;

    for (int i = n - 1; i >= 2; i--) {
        int l = 0, r = i - 1;

        while (l < r) {
            if (nums[l] + nums[r] > nums[i]) {
                ans += r - l;
                r--;
            } else {
                l++;
            }
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n^2)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort
- fix one side
- use two pointers to count many pairs at once

------------------------------------------------------------------------

## End of Notes
