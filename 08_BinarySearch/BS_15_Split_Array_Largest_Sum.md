# Binary Search Notes

## 15 - Split Array Largest Sum

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/split-array-largest-sum/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given an integer array `nums` and an integer `k`, split the array into `k` non-empty contiguous subarrays.

Minimize the largest subarray sum.

Example:

```text
Input: nums = [7,2,5,10,8], k = 2
Output: 18
```

Best split is `[7,2,5]` and `[10,8]`, where largest sum is `18`.

------------------------------------------------------------------------

## 2. First Intuition

This is another "minimize the maximum" problem.

Ask:

Can I split the array such that no subarray sum exceeds `X`?

If yes, try smaller `X`.
If no, increase `X`.

------------------------------------------------------------------------

## 3. Methodology

Search space:

- `low = max(nums)`
- `high = sum(nums)`

For a limit `maxAllowedSum`:

- greedily build a subarray until adding the next number exceeds the limit
- then start a new subarray
- count how many subarrays are required

If required subarrays `<= k`, the limit is possible.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
class Solution {
    public int splitArray(int[] nums, int k) {
        int low = 0;
        int high = 0;

        for (int num : nums) {
            low = Math.max(low, num);
            high += num;
        }

        int ans = high;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (requiredSubarrays(nums, mid) <= k) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return ans;
    }

    private int requiredSubarrays(int[] nums, int maxAllowedSum) {
        int count = 1;
        int currentSum = 0;

        for (int num : nums) {
            if (currentSum + num > maxAllowedSum) {
                count++;
                currentSum = num;
            } else {
                currentSum += num;
            }
        }

        return count;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `nums = [7,2,5,10,8]`, `k = 2`:

- Limit `18` works: `[7,2,5]`, `[10,8]`.
- Limit `17` fails because it needs 3 subarrays.
- Minimum largest sum is `18`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n log sum)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Same core pattern as allocate pages and ship packages.
- When the array must be split into contiguous groups, binary search the maximum allowed group sum.
- The feasibility check is greedy.

------------------------------------------------------------------------

## End of Notes
