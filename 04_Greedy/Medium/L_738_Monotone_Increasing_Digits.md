# Greedy Notes

## Monotone Increasing Digits

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/monotone-increasing-digits/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an integer `n`, return the largest number less than or equal to `n` whose digits are monotone increasing.

That means:

    digit[i] <= digit[i + 1]

for every adjacent pair.

**Example 1**

    Input: n = 10
    Output: 9

**Example 2**

    Input: n = 332
    Output: 299

------------------------------------------------------------------------

## 2. Backward Fix Intuition

When a violation appears:

    s[i - 1] > s[i]

the left digit is too large.

To make the number as large as possible but still valid:

- decrease that left digit by `1`
- make every digit to its right equal to `9`

Why `9`?

Because after fixing the violating prefix, the best suffix is the largest possible non-decreasing tail, which is all `9`s.

------------------------------------------------------------------------

## 3. Why We Scan Right To Left

After decreasing one digit, a new violation can appear to its left.

Example:

    332

Fixing the last violation first gives:

    329

But `3 > 2` still violates monotonicity, so we must continue leftward.

That is why the backward scan is necessary.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    n = 332

Digits:

    [3,3,2]

From right to left:

- `3 > 2` -> decrease middle `3` to `2`, mark suffix
- now left `3 > 2` -> decrease first `3` to `2`, mark suffix again

Fill suffix with `9`s:

    [2,9,9]

Answer:

    299

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int monotoneIncreasingDigits(int n) {
    char[] s = String.valueOf(n).toCharArray();
    int len = s.length;
    int mark = len;

    for (int i = len - 1; i >= 1; i--) {
        if (s[i - 1] > s[i]) {
            mark = i - 1;
            s[i - 1]--;
        }
    }

    for (int i = mark + 1; i < len; i++) {
        s[i] = '9';
    }

    return Integer.parseInt(new String(s));
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(d)`
- **Space:** `O(d)`

Pattern:

- scan from right to left for the first broken prefix
- decrease once, then maximize the suffix

------------------------------------------------------------------------

## End of Notes
