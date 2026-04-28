# Hash Table Notes

## 219 - Contains Duplicate II

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/contains-duplicate-ii/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an integer array `nums` and an integer `k`, return `true` if there are two **distinct indices** `i` and `j` in the array such that `nums[i] == nums[j]` and `abs(i - j) <= k`.

**Example 1:**

```text
Input: nums = [1,2,3,1], k = 3
Output: true
```

**Example 2:**

```text
Input: nums = [1,0,1,1], k = 1
Output: true
```

**Example 3:**

```text
Input: nums = [1,2,3,1,2,3], k = 2
Output: false
```

**Constraints:**

- `1 <= nums.length <= 105`

- `-109 <= nums[i] <= 109`

- `0 <= k <= 105`

------------------------------------------------------------------------

## 2. First Intuition

For each number, only the most recent previous index can give the smallest distance to the current index. If that distance is within `k`, the answer is immediately true.

------------------------------------------------------------------------

## 3. Last-Seen Index Map

- The Java file stores `value -> latest index` in a `HashMap`.
- When a value repeats, it checks `i - previousIndex <= k`.
- If the repeat is too far away, it updates the stored index to the current one.

------------------------------------------------------------------------

## 4. Short Dry Run

For `nums = [1,2,3,1]`, `k = 3`: the second `1` is at index `3`, previous `1` is at index `0`, and distance `3` is allowed, so return `true`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean containsNearbyDuplicate(int[] nums, int k) {
    Map<Integer, Integer> lastSeen = new HashMap<>();

    for (int i = 0; i < nums.length; i++) {
        int num = nums[i];
        if (lastSeen.containsKey(num) && i - lastSeen.get(num) <= k) {
            return true;
        }
        lastSeen.put(num, i);
    }
    return false;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(n)` in the worst case.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Use a map when the question asks for nearby repeated values.
- Keep the latest index, not every index, when only the shortest distance matters.

------------------------------------------------------------------------

## End of Notes
