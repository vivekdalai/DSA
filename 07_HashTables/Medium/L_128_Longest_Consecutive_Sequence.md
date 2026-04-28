# Hash Table Notes

## 128 - Longest Consecutive Sequence

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/longest-consecutive-sequence/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an unsorted array of integers `nums`, return the length of the longest consecutive elements sequence.

You must write an algorithm that runs in `O(n)` time.

**Example 1:**

```text
Input: nums = [100,4,200,1,3,2]
Output: 4
Explanation: The longest consecutive elements sequence is [1, 2, 3, 4]. Therefore its length is 4.
```

**Example 2:**

```text
Input: nums = [0,3,7,2,5,8,4,6,0,1]
Output: 9
```

**Example 3:**

```text
Input: nums = [1,0,1,2]
Output: 3
```

**Constraints:**

- `0 <= nums.length <= 105`

- `-109 <= nums[i] <= 109`

------------------------------------------------------------------------

## 2. First Intuition

A number is worth expanding only if it is the first number of a consecutive run. If `n - 1` exists, then `n` is in the middle and starting there would duplicate work.

------------------------------------------------------------------------

## 3. Set Starts of Runs

- The file inserts every number into a `HashSet`.
- It checks only numbers where `n - 1` is absent.
- From each start, it counts upward while the next value exists in the set.

**Code note:** The file's intent is correct, but `localMax` starts at `0` and increments after pre-incrementing `curr`, so it undercounts by one. The interview version below initializes the run length as `1`.

------------------------------------------------------------------------

## 4. Short Dry Run

For `[100,4,200,1,3,2]`, only `1`, `100`, and `200` are starts. Starting at `1` expands through `2,3,4`, length `4`, which is the answer.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int longestConsecutive(int[] nums) {
    Set<Integer> set = new HashSet<>();
    for (int num : nums) {
        set.add(num);
    }

    int best = 0;
    for (int num : set) {
        if (!set.contains(num - 1)) {
            int curr = num;
            int len = 1;
            while (set.contains(curr + 1)) {
                curr++;
                len++;
            }
            best = Math.max(best, len);
        }
    }
    return best;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)` average because each run is expanded once.
- Space: `O(n)` for the set.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Look for sequence starts with `!set.contains(x - 1)`.
- This avoids sorting and keeps the expected time linear.

------------------------------------------------------------------------

## End of Notes
