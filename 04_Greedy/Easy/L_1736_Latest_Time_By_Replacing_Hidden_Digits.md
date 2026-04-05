# Greedy Notes

## 07 - Latest Time By Replacing Hidden Digits

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given a string `time` in the format `hh:mm`, where some digits may be `'?'`.

Replace every `'?'` with a digit so that the final time is valid and as late as possible.

Return that latest valid time.

**Example 1**

    Input: time = "2?:?0"
    Output: "23:50"

**Example 2**

    Input: time = "0?:3?"
    Output: "09:39"

------------------------------------------------------------------------

## 2. Key Observation

The time format has separate constraints:

- hours must be between `00` and `23`
- minutes must be between `00` and `59`

So each missing digit can be chosen greedily, but only within the valid range forced by neighboring digits.

------------------------------------------------------------------------

## 3. Greedy Choice By Position

### Hours

- if both hour digits are `?`, choose `23`
- if the first hour digit is `?`:
  choose `2` when the second digit is `0..3`, otherwise choose `1`
- if the second hour digit is `?`:
  choose `9` when the first digit is `0` or `1`, otherwise choose `3`

### Minutes

- first minute digit should be `5` if missing
- second minute digit should be `9` if missing

Since we want the latest time, we always pick the maximum valid digit at each position.

------------------------------------------------------------------------

## 4. Quick Walkthrough

**Input:**

    time = "2?:?0"

Choices:

- first hour digit is already `2`
- second hour digit can be at most `3`, so choose `3`
- first minute digit can be at most `5`, so choose `5`
- second minute digit is already `0`

Result:

    "23:50"

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public String maximumTime(String time) {
    char h1 = time.charAt(0);
    char h2 = time.charAt(1);
    char m1 = time.charAt(3);
    char m2 = time.charAt(4);

    if (h1 == '?' && h2 == '?') {
        h1 = '2';
        h2 = '3';
    } else {
        if (h1 == '?') {
            h1 = (h2 >= '0' && h2 <= '3') ? '2' : '1';
        } else if (h2 == '?') {
            h2 = (h1 == '0' || h1 == '1') ? '9' : '3';
        }
    }

    if (m1 == '?') m1 = '5';
    if (m2 == '?') m2 = '9';

    return "" + h1 + h2 + ":" + m1 + m2;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(1)`
- **Space:** `O(1)`

Pattern:

- fill constrained positions greedily from left to right
- always choose the largest valid digit

------------------------------------------------------------------------

## End of Notes
