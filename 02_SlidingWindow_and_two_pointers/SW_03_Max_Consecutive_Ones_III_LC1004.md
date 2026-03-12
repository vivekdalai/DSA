# Sliding Window / Two Pointers Notes

## 03 - Max Consecutive Ones III (LC 1004)

**Generated on:** 2026-03-13 02:32:53 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a binary array `nums` and an integer `k`, return the maximum number of consecutive `1`s in the array if you can flip at most `k` zeros.

Equivalent interpretation:
- Find the longest subarray containing at most `k` zeros.

Examples:
- `nums = [1,1,1,0,0,0,1,1,1,1,0], k = 2` → `6`
- `nums = [0,0,1,1,1,0,0], k = 0` → `3`

Why Sliding Window:
- We want the longest valid contiguous segment.
- Valid condition:
  - number of zeros inside current window `<= k`

------------------------------------------------------------------------

## 🪜 2. Core Window Idea

Maintain a window `[left ... right]` with:
- at most `k` zeros

As we move `right`:
- if `nums[right] == 0`, increment zero count
- while zero count becomes greater than `k`, move `left` forward and remove zeros from the window
- update the maximum valid window size

------------------------------------------------------------------------

## 🔁 3. Window Invariant

At all times after shrinking:
- `zeros <= k`

Then:
- current valid window length = `right - left + 1`

------------------------------------------------------------------------

## 💻 4A. Standard Java Implementation

```java
class MaxConsecutiveOnesIII {
    public int longestOnes(int[] nums, int k) {
        int left = 0;
        int zeros = 0;
        int maxLen = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) zeros++;

            while (zeros > k) {
                if (nums[left] == 0) zeros--;
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 💻 4B. Slightly Different but Equivalent Version

```java
class MaxConsecutiveOnesIII_Compact {
    public int longestOnes(int[] nums, int k) {
        int left = 0, right = 0;
        int zeros = 0;

        while (right < nums.length) {
            if (nums[right] == 0) zeros++;

            if (zeros > k) {
                if (nums[left] == 0) zeros--;
                left++;
            }
            right++;
        }

        return right - left;
    }
}
```

Why does `return right - left` work here?
- Because this variant keeps the window as large as possible and shrinks by only one step whenever invalid.
- The final maintained window length equals the maximum valid one.
- Interview-wise, the first version with `maxLen` is more intuitive and safer.

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`nums = [1,0,1,1,0,1], k = 2`

- `right = 0` → window `[1]`, zeros = 0, len = 1
- `right = 1` → window `[1,0]`, zeros = 1, len = 2
- `right = 2` → window `[1,0,1]`, zeros = 1, len = 3
- `right = 3` → window `[1,0,1,1]`, zeros = 1, len = 4
- `right = 4` → window `[1,0,1,1,0]`, zeros = 2, len = 5
- `right = 5` → window `[1,0,1,1,0,1]`, zeros = 2, len = 6

Answer = `6`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is the standard:
- **longest subarray with at most K bad elements**

Here:
- bad element = `0`

You’ll see the same pattern in:
- longest substring with at most `k` distinct characters
- fruit into baskets
- longest repeating character replacement
- nice subarray / binary subarray derived counting variants

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- `k = 0`:
  - answer becomes longest existing block of 1s
- All zeros:
  - answer can be at most `k` (or array length if `k >= n`)
- Don’t forget to decrement zero count when removing `nums[left]`
- This is not “exactly k flips”; it is “at most k flips”

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Convert the problem to:
  - longest subarray with at most `k` zeros
- Maintain zero count inside the window
- If zero count exceeds `k`, shrink from left
- One of the most reusable sliding window templates

------------------------------------------------------------------------

# End of Notes
