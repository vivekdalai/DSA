# Hash Table Notes

## 1512 - Number of Good Pairs

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/number-of-good-pairs/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an array of integers `nums`, return the number of **good pairs**.

A pair `(i, j)` is called good if `nums[i] == nums[j]` and `i` < `j`.

**Example 1:**

```text
Input: nums = [1,2,3,1,1,3]
Output: 4
Explanation: There are 4 good pairs (0,3), (0,4), (3,4), (2,5) 0-indexed.
```

**Example 2:**

```text
Input: nums = [1,1,1,1]
Output: 6
Explanation: Each pair in the array are good.
```

**Example 3:**

```text
Input: nums = [1,2,3]
Output: 0
```

**Constraints:**

- `1 <= nums.length <= 100`

- `1 <= nums[i] <= 100`

------------------------------------------------------------------------

## 2. First Intuition

A good pair is formed by two equal values at different indices. Once you know a value appears `f` times, it contributes `f choose 2` pairs.

------------------------------------------------------------------------

## 3. Counting Equal Values

- The file uses an array of size `101`, matching the value bounds.
- It counts how many times each number appears.
- For every frequency `n`, it adds `n * (n - 1) / 2` to the answer.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[1,2,3,1,1,3]`: value `1` appears `3` times and contributes `3` pairs; value `3` appears `2` times and contributes `1`; total is `4`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int numIdenticalPairs(int[] nums) {
    int[] freq = new int[101];
    for (int num : nums) {
        freq[num]++;
    }

    int ans = 0;
    for (int count : freq) {
        ans += count * (count - 1) / 2;
    }
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n + 101)`, which is `O(n)`
- Space: `O(1)` because the value range is fixed.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Equal-pair counting usually reduces to combinations.
- Alternative one-pass trick: add `freq[num]` to the answer before incrementing it.

------------------------------------------------------------------------

## End of Notes
