# Greedy Notes

## 05 - Minimum Subsequence In Non Increasing Order

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given an integer array `nums`, return a subsequence such that:

- the sum of the subsequence is strictly greater than the sum of the remaining elements
- the subsequence has minimum size
- if multiple answers have the same size, return the one with the largest total sum

The returned subsequence must be in non-increasing order.

**Example 1**

    Input: nums = [4, 3, 10, 9, 8]
    Output: [10, 9]

Because:

    10 + 9 = 19
    4 + 3 + 8 = 15

**Example 2**

    Input: nums = [4, 4, 7, 6, 7]
    Output: [7, 7, 6]

------------------------------------------------------------------------

## 2. Greedy View

If we want:

- minimum number of elements
- and among equal lengths, the largest sum

then we should take the biggest elements first.

That single observation drives the whole solution.

------------------------------------------------------------------------

## 3. Two Ways Shown In The File

### Sort-based version

- sort the array
- walk from right to left
- keep adding large numbers until:

    currSum > totalSum - currSum

### Frequency-based version

Because values are small, the file also shows a counting approach with `freq[101]`.

That avoids sorting by directly taking values from `100` down to `1`.

------------------------------------------------------------------------

## 4. Why This Is Optimal

Suppose we skipped a larger value and took a smaller one instead.

That would:

- never help reduce the subsequence length
- and would only reduce the subsequence sum

So taking larger values first is always at least as good, and usually strictly better.

------------------------------------------------------------------------

## 5. Quick Walkthrough

**Input:**

    nums = [4, 3, 10, 9, 8]

Sorted:

    [3, 4, 8, 9, 10]

Take from the end:

- take `10` -> `currSum = 10`, remaining `14`
- take `9`  -> `currSum = 19`, remaining `5`

Now:

    19 > 5

So the answer is:

    [10, 9]

------------------------------------------------------------------------

## 6. Clean Interview Version

```java
public static List<Integer> minSubsequence(int[] nums) {
    Arrays.sort(nums);

    int totalSum = 0;
    for (int x : nums) totalSum += x;

    int currSum = 0;
    List<Integer> ans = new ArrayList<>();

    for (int i = nums.length - 1; i >= 0; i--) {
        currSum += nums[i];
        ans.add(nums[i]);

        if (currSum > totalSum - currSum) {
            return ans;
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 7. Complexity And Pattern

- **Time:** `O(n log n)` for the sort version
- **Space:** `O(1)` extra, ignoring output

Pattern:

- sort descending by value
- keep the strongest elements until a threshold is crossed

------------------------------------------------------------------------

## End of Notes
