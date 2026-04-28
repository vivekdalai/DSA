# Hash Table Notes

## 659 - Split Array into Consecutive Subsequences

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/split-array-into-consecutive-subsequences/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given an integer array `nums` that is **sorted in non-decreasing order**.

Determine if it is possible to split `nums` into **one or more subsequences** such that **both** of the following conditions are true:

- Each subsequence is a **consecutive increasing sequence** (i.e. each integer is **exactly one** more than the previous integer).

- All subsequences have a length of `3`**or more**.

Return `true` if you can split `nums` according to the above conditions, or `false` otherwise.

A **subsequence** of an array is a new array that is formed from the original array by deleting some (can be none) of the elements without disturbing the relative positions of the remaining elements. (i.e., `[1,3,5]` is a subsequence of `[1,2,3,4,5]` while `[1,3,2]` is not).

**Example 1:**

```text
Input: nums = [1,2,3,3,4,5]
Output: true
Explanation: nums can be split into the following subsequences:
[1,2,3,3,4,5] --> 1, 2, 3
[1,2,3,3,4,5] --> 3, 4, 5
```

**Example 2:**

```text
Input: nums = [1,2,3,3,4,4,5,5]
Output: true
Explanation: nums can be split into the following subsequences:
[1,2,3,3,4,4,5,5] --> 1, 2, 3, 4, 5
[1,2,3,3,4,4,5,5] --> 3, 4, 5
```

**Example 3:**

```text
Input: nums = [1,2,3,4,4,5]
Output: false
Explanation: It is impossible to split nums into consecutive increasing subsequences of length 3 or more.
```

**Constraints:**

- `1 <= nums.length <= 104`

- `-1000 <= nums[i] <= 1000`

- `nums` is sorted in **non-decreasing** order.

------------------------------------------------------------------------

## 2. First Intuition

A number should extend an existing subsequence if possible. Starting a new subsequence is allowed only when the next two numbers exist, because every subsequence must have length at least three.

------------------------------------------------------------------------

## 3. Frequency and Vacancy Maps

- `freq` counts unused occurrences of each number.
- `vMap` records how many existing subsequences are currently waiting for a specific next number.
- For each number, the file first tries to place it into a waiting subsequence.
- If none is waiting, it tries to start `x, x + 1, x + 2` and then marks that sequence as waiting for `x + 3`.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[1,2,3,3,4,5]`: start `1,2,3`, which waits for `4`; the next `3` starts `3,4,5`. Both subsequences reach length at least three, so return `true`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean isPossible(int[] nums) {
    Map<Integer, Integer> freq = new HashMap<>();
    Map<Integer, Integer> need = new HashMap<>();

    for (int num : nums) {
        freq.put(num, freq.getOrDefault(num, 0) + 1);
    }

    for (int num : nums) {
        if (freq.getOrDefault(num, 0) == 0) continue;
        freq.put(num, freq.get(num) - 1);

        if (need.getOrDefault(num, 0) > 0) {
            need.put(num, need.get(num) - 1);
            need.put(num + 1, need.getOrDefault(num + 1, 0) + 1);
        } else if (freq.getOrDefault(num + 1, 0) > 0 && freq.getOrDefault(num + 2, 0) > 0) {
            freq.put(num + 1, freq.get(num + 1) - 1);
            freq.put(num + 2, freq.get(num + 2) - 1);
            need.put(num + 3, need.getOrDefault(num + 3, 0) + 1);
        } else {
            return false;
        }
    }
    return true;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(n)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Greedy priority: extend existing valid subsequences before starting new ones.
- The `need` map represents future demand, not current frequency.

------------------------------------------------------------------------

## End of Notes
