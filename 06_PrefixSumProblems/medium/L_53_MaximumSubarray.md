# Prefix Notes

## 53 - Maximum Subarray

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/maximum-subarray/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an integer array `nums`, find the subarray with the largest sum, and return its sum.

**Example 1:**

```text
Input: nums = [-2,1,-3,4,-1,2,1,-5,4]
Output: 6
Explanation: The subarray [4,-1,2,1] has the largest sum 6.
```

**Example 2:**

```text
Input: nums = [1]
Output: 1
Explanation: The subarray [1] has the largest sum 1.
```

**Example 3:**

```text
Input: nums = [5,4,-1,7,8]
Output: 23
Explanation: The subarray [5,4,-1,7,8] has the largest sum 23.
```

**Constraints:**

- `1 <= nums.length <= 105`

- `-104 <= nums[i] <= 104`

**Follow up:** If you have figured out the `O(n)` solution, try coding another solution using the **divide and conquer** approach, which is more subtle.

------------------------------------------------------------------------

## 2. First Intuition

The best subarray ending at the current position is the current prefix sum minus the smallest prefix sum seen before it.

------------------------------------------------------------------------

## 3. Prefix Sum Minus Minimum Prefix

- The file maintains `sum`, `minSum`, and `maxSum`.
- Before adding the current value, it records whether the previous prefix is the smallest so far.
- After adding the current value, `sum - minSum` is the best subarray ending here.
- This is closely related to Kadane's algorithm, just written in prefix-sum language.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[-2,1,-3,4,-1,2,1,-5,4]`, the minimum prefix before the best region lets `4 + -1 + 2 + 1 = 6` become the maximum.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maxSubArray(int[] nums) {
    int sum = 0;
    int minPrefix = 0;
    int best = nums[0];

    for (int num : nums) {
        sum += num;
        best = Math.max(best, sum - minPrefix);
        minPrefix = Math.min(minPrefix, sum);
    }
    return best;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- For maximum subarray, either use Kadane or prefix sum minus minimum prefix.
- Initialize carefully so all-negative arrays still return the largest element.

------------------------------------------------------------------------

## End of Notes
