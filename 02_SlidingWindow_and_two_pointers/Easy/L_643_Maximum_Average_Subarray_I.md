# Two Pointers and Sliding Window Notes

## 643 - Maximum Average Subarray I

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/maximum-average-subarray-i/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given an integer array `nums` consisting of `n` elements, and an integer `k`.

Find a contiguous subarray whose **length is equal to** `k` that has the maximum average value and return this value. Any answer with a calculation error less than `10-5` will be accepted.

**Example 1:**

```text
Input: nums = [1,12,-5,-6,50,3], k = 4
Output: 12.75000
Explanation: Maximum average is (12 - 5 - 6 + 50) / 4 = 51 / 4 = 12.75
```

**Example 2:**

```text
Input: nums = [5], k = 1
Output: 5.00000
```

**Constraints:**

- `n == nums.length`

- `1 <= k <= n <= 105`

- `-104 <= nums[i] <= 104`

------------------------------------------------------------------------

## 2. First Intuition

The subarray length is fixed at `k`, so maximizing the average is the same as maximizing the window sum.

------------------------------------------------------------------------

## 3. Fixed-Size Sliding Window

- The file grows the right pointer and keeps a running sum.
- When the window becomes larger than `k`, it subtracts `nums[l]` and moves `l`.
- When the window size is exactly `k`, it updates the best sum.

**Code note:** The source currently prints `sum / k` inside the loop. That debug line should be removed for a clean submission.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[1,12,-5,-6,50,3]`, `k = 4`: window sums are `2`, `51`, and `42`; the maximum sum is `51`, so the answer is `12.75`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public double findMaxAverage(int[] nums, int k) {
    int sum = 0;
    for (int i = 0; i < k; i++) {
        sum += nums[i];
    }

    int best = sum;
    for (int r = k; r < nums.length; r++) {
        sum += nums[r] - nums[r - k];
        best = Math.max(best, sum);
    }
    return (double) best / k;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Fixed-length window: add the new right value and remove the value that falls out.
- For fixed `k`, average comparisons can be done with sums.

------------------------------------------------------------------------

## End of Notes
