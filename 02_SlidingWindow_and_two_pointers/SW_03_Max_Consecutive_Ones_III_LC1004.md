# Sliding Window / Two Pointers Notes

## 03 - Max Consecutive Ones III (LC 1004)

**Generated on:** 2026-03-13 02:32:53 (IST)

---

## 1. Problem Understanding

Given a binary array `nums` and an integer `k`, return the maximum number of consecutive `1`s in the array if you can flip at most `k` zeros.

Instead of thinking about flips directly, convert the problem into this:

> Find the longest contiguous subarray that contains at most `k` zeros.

Why?
- Every `0` inside the chosen subarray consumes one flip.
- If the subarray has `k` or fewer zeros, all those zeros can be flipped to `1`.
- After flipping, the entire subarray becomes consecutive `1`s.

Examples:
- `nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2` -> `6`
- `nums = [0,0,1,1,1,0,0], k = 0` -> `3`

---

## 2. Why Sliding Window?

We need the longest valid contiguous segment.

A window is valid when:

```text
number of zeros inside the window <= k
```

So we maintain a window:

```text
[left ... right]
```

The job of each pointer:
- `right` expands the window by adding new elements.
- `left` shrinks the window when it becomes invalid.

This works because once a window has more than `k` zeros, expanding it more cannot make it valid again. The only fix is to move `left` forward and remove elements from the window.

---

## 3. Core Window Idea

Maintain:

```java
int left = 0;
int zeros = 0;
int maxLen = 0;
```

As `right` moves:

1. Add `nums[right]` into the window.
2. If `nums[right] == 0`, increment `zeros`.
3. If `zeros > k`, shrink from the left.
4. Once valid again, update the best length.

Window invariant after shrinking:

```text
zeros <= k
```

Current valid window length:

```text
right - left + 1
```

---

## 4. Standard Java Implementation

```java
class MaxConsecutiveOnesIII {
    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int zeros = 0;
        int maxLen = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeros++;
            }

            while (zeros > k) {
                if (nums[left] == 0) {
                    zeros--;
                }
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}
```

Complexity:
- Time: `O(n)`
- Space: `O(1)`

Why time is `O(n)`:
- `right` moves from left to right once.
- `left` also moves from left to right once.
- No index is repeatedly reprocessed.

---

## 5. Dry Run With Shrinking

Use an example where the window actually becomes invalid:

```text
nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2
```

Initial state:

```text
left = 0, zeros = 0, maxLen = 0
```

| right | nums[right] | Action | Window after shrinking | zeros | maxLen |
|---:|---:|---|---|---:|---:|
| 0 | 1 | add 1 | `[1]` | 0 | 1 |
| 1 | 1 | add 1 | `[1,1]` | 0 | 2 |
| 2 | 1 | add 1 | `[1,1,1]` | 0 | 3 |
| 3 | 0 | add 0 | `[1,1,1,0]` | 1 | 4 |
| 4 | 0 | add 0 | `[1,1,1,0,0]` | 2 | 5 |
| 5 | 0 | add 0, invalid | `[0,0]` | 2 | 5 |
| 6 | 1 | add 1 | `[0,0,1]` | 2 | 5 |
| 7 | 1 | add 1 | `[0,0,1,1]` | 2 | 5 |
| 8 | 1 | add 1 | `[0,0,1,1,1]` | 2 | 5 |
| 9 | 1 | add 1 | `[0,0,1,1,1,1]` | 2 | 6 |
| 10 | 0 | add 0, invalid | `[0,1,1,1,1,0]` | 2 | 6 |

Answer:

```text
6
```

Important moment:
- At `right = 5`, the window has 3 zeros, so it is invalid.
- We move `left` until one zero is removed from the window.
- Then the window becomes valid again with exactly 2 zeros.

---

## 6. Compact Version

There is also a shorter version:

```java
class MaxConsecutiveOnesIIICompact {
    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int right = 0;
        int zeros = 0;

        while (right < nums.length) {
            if (nums[right] == 0) {
                zeros++;
            }

            if (zeros > k) {
                if (nums[left] == 0) {
                    zeros--;
                }
                left++;
            }

            right++;
        }

        return right - left;
    }
}
```

Why does `return right - left` work here?
- This version does not shrink the window completely using `while`.
- It shrinks by only one step whenever the window becomes invalid.
- Because `right` always moves one step and `left` moves at most one step per loop, the window size never decreases.
- By the end, `right - left` represents the largest window size that was maintained.

Interview recommendation:
- Prefer the standard `while (zeros > k)` version with `maxLen`.
- It is easier to reason about.
- It directly maintains the invariant `zeros <= k` before updating the answer.

---

## 7. Pattern Recognition

This problem belongs to the pattern:

```text
Longest subarray with at most K bad elements
```

Here:

```text
bad element = 0
```

Same idea appears in:
- Longest substring with at most `k` distinct characters
- Fruit Into Baskets
- Longest Repeating Character Replacement
- Longest subarray after deleting one element
- Binary subarray counting variants

General template:

```java
for (int right = 0; right < n; right++) {
    add nums[right] to the window;

    while (window is invalid) {
        remove nums[left] from the window;
        left++;
    }

    update answer using current window;
}
```

---

## 8. Edge Cases and Pitfalls

### `k = 0`

No zeros can be flipped.

The answer becomes the longest existing block of `1`s.

Example:

```text
nums = [0,0,1,1,1,0,0], k = 0
answer = 3
```

### All zeros

Example:

```text
nums = [0,0,0,0], k = 2
answer = 2
```

The answer is at most `k`, unless `k >= nums.length`.

### All ones

Example:

```text
nums = [1,1,1,1], k = 2
answer = 4
```

There are no zeros to worry about, so the full array is valid.

### Common Mistakes

- Forgetting to decrement `zeros` when `nums[left] == 0`.
- Using `if (zeros > k)` in the standard version when a full shrink is needed before updating `maxLen`.
- Treating the problem as exactly `k` flips instead of at most `k` flips.
- Updating `maxLen` before restoring the valid condition.

---

## 9. Final Intuition

Think of `k` as a flip budget.

Each zero costs one unit of budget.

The sliding window tries to keep the biggest possible segment whose cost is within budget.

If the cost exceeds the budget, move `left` until the segment becomes affordable again.

---

## 10. Takeaway

- Convert the problem to longest subarray with at most `k` zeros.
- Track how many zeros are inside the current window.
- Expand using `right`.
- Shrink using `left` when zeros exceed `k`.
- Update the answer only when the window is valid.
- This is one of the most reusable sliding window templates.

---

# End of Notes
