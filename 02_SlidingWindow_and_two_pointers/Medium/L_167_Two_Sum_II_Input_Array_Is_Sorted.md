# Two Pointers and Sliding Window Notes

## 167 - Two Sum II - Input Array Is Sorted

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a **1-indexed** array of integers `numbers` that is already **sorted in non-decreasing order**, find two numbers such that they add up to a specific `target` number. Let these two numbers be `numbers[index1]` and `numbers[index2]` where `1  the indices of the two numbers `index1` and `index2`, **each incremented by one,** as an integer array `[index1, index2]` of length 2.

The tests are generated such that there is **exactly one solution**. You **may not** use the same element twice.

Your solution must use only constant extra space.

**Example 1:**

```text
Input: numbers = [2,7,11,15], target = 9
Output: [1,2]
Explanation: The sum of 2 and 7 is 9. Therefore, index1 = 1, index2 = 2. We return [1, 2].
```

**Example 2:**

```text
Input: numbers = [2,3,4], target = 6
Output: [1,3]
Explanation: The sum of 2 and 4 is 6. Therefore index1 = 1, index2 = 3. We return [1, 3].
```

**Example 3:**

```text
Input: numbers = [-1,0], target = -1
Output: [1,2]
Explanation: The sum of -1 and 0 is -1. Therefore index1 = 1, index2 = 2. We return [1, 2].
```

**Constraints:**

- `2 <= numbers.length <= 3 * 104`

- `-1000 <= numbers[i] <= 1000`

- `numbers` is sorted in **non-decreasing order**.

- `-1000 <= target <= 1000`

- The tests are generated such that there is **exactly one solution**.

------------------------------------------------------------------------

## 2. First Intuition

With a sorted array, the smallest remaining value is on the left and the largest remaining value is on the right. Their sum tells which pointer can be safely moved.

------------------------------------------------------------------------

## 3. Sorted Two-Pointer Search

- Start `l` at `0` and `r` at `n - 1`.
- If the sum is too large, decrement `r` to reduce it.
- If the sum is too small, increment `l` to increase it.
- Return 1-based indices when the target is found.

------------------------------------------------------------------------

## 4. Short Dry Run

For `numbers = [2,7,11,15]`, `target = 9`: `2 + 15` is too large, so move right; `2 + 11` is too large; `2 + 7` matches, return `[1,2]`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int[] twoSum(int[] numbers, int target) {
    int left = 0;
    int right = numbers.length - 1;

    while (left < right) {
        int sum = numbers[left] + numbers[right];
        if (sum == target) return new int[] {left + 1, right + 1};
        if (sum > target) right--;
        else left++;
    }
    return new int[] {-1, -1};
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Sorted input often unlocks two pointers.
- Move the pointer that can move the sum toward the target.

------------------------------------------------------------------------

## End of Notes
