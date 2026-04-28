# Prefix Notes

## 525 - Contiguous Array

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/contiguous-array/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a binary array `nums`, return the maximum length of a contiguous subarray with an equal number of `0` and `1`.

**Example 1:**

```text
Input: nums = [0,1]
Output: 2
Explanation: [0, 1] is the longest contiguous subarray with an equal number of 0 and 1.
```

**Example 2:**

```text
Input: nums = [0,1,0]
Output: 2
Explanation: [0, 1] (or [1, 0]) is a longest contiguous subarray with equal number of 0 and 1.
```

**Example 3:**

```text
Input: nums = [0,1,1,1,1,1,0,0,0]
Output: 6
Explanation: [1,1,1,0,0,0] is the longest contiguous subarray with equal number of 0 and 1.
```

**Constraints:**

- `1 <= nums.length <= 105`

- `nums[i]` is either `0` or `1`.

------------------------------------------------------------------------

## 2. First Intuition

Treat `0` as `-1` and `1` as `+1`. A subarray has equal zeros and ones exactly when its transformed sum is zero.

------------------------------------------------------------------------

## 3. Balance Prefix Sum

- The file tracks a running balance.
- It stores the first index where each balance was seen.
- When the same balance appears again, the values between those indices cancel out to equal zeros and ones.
- The maximum distance between equal balances is the answer.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[0,1,0]`: balances are `-1,0,-1`. Balance `0` was seeded at `-1`, so index `1` gives length `2`; balance `-1` repeats from `0` to `2`, also length `2`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int findMaxLength(int[] nums) {
    Map<Integer, Integer> first = new HashMap<>();
    first.put(0, -1);

    int balance = 0;
    int best = 0;
    for (int i = 0; i < nums.length; i++) {
        balance += nums[i] == 0 ? -1 : 1;
        if (first.containsKey(balance)) {
            best = Math.max(best, i - first.get(balance));
        } else {
            first.put(balance, i);
        }
    }
    return best;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(n)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Convert two-category balance problems into prefix sums.
- Seed balance `0` at `-1` to support subarrays starting at index `0`.

------------------------------------------------------------------------

## End of Notes
