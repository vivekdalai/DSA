# Greedy Notes

## 09 - Largest Odd Number in String

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given a numeric string `s`.

Return the largest-valued odd integer that is a non-empty substring of `s`.

If no odd integer exists, return an empty string.

**Example 1**

    Input: s = "52"
    Output: "5"

**Example 2**

    Input: s = "4206"
    Output: ""

------------------------------------------------------------------------

## 2. The Only Digit That Matters

An integer is odd if and only if its last digit is odd.

So the problem reduces to:

- find the rightmost odd digit
- return the prefix ending there

Why the whole prefix?

Because removing digits from the front only makes the number smaller.

------------------------------------------------------------------------

## 3. Greedy Scan

Start from the end of the string and move left:

- if `s[i]` is odd, return `s.substring(0, i + 1)`

If no odd digit is found, return `""`.

This is greedy because the rightmost odd ending gives the longest and largest possible prefix.

------------------------------------------------------------------------

## 4. Quick Walkthrough

**Input:**

    s = "35427"

Scan from right:

- `7` is odd

So we can immediately return:

    "35427"

For:

    s = "4206"

No odd digit exists, so the answer is:

    ""

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public String largestOddNumber(String s) {
    for (int i = s.length() - 1; i >= 0; i--) {
        if ((s.charAt(i) - '0') % 2 == 1) {
            return s.substring(0, i + 1);
        }
    }
    return "";
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)` extra

Pattern:

- use the last-digit property of the target number
- scan from the right for the latest valid ending

------------------------------------------------------------------------

## End of Notes
