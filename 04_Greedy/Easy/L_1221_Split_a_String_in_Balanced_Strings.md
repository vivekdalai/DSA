# Greedy Notes

## 03 - Split a String in Balanced Strings

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

A balanced string has the same number of `L` and `R` characters.

Given a balanced string `s`, split it into the maximum number of balanced substrings.

Return that maximum count.

**Example 1**

    Input: s = "RLRRLLRLRL"
    Output: 4

One optimal split is:

    "RL" | "RRLL" | "RL" | "RL"

**Example 2**

    Input: s = "RLRRRLLRLL"
    Output: 2

------------------------------------------------------------------------

## 2. Core Intuition

We do not need to try all split points.

Just track the balance:

- `+1` for `L`
- `-1` for `R`

Whenever the running balance becomes `0`, the current segment is balanced and should be cut immediately.

Why immediately?

Because we want the maximum number of pieces, so the earliest valid cut is always best.

------------------------------------------------------------------------

## 3. Greedy Rule

Initialize:

    balance = 0
    ans = 0

Then scan the string:

- if char is `L`, increment balance
- else decrement balance
- if balance becomes `0`, increment answer

That is the entire solution.

------------------------------------------------------------------------

## 4. Quick Walkthrough

**Input:**

    s = "RLRRLLRLRL"

Running balance:

- `R` -> `-1`
- `L` -> `0`  => one balanced substring
- `R` -> `-1`
- `R` -> `-2`
- `L` -> `-1`
- `L` -> `0`  => second
- `R` -> `-1`
- `L` -> `0`  => third
- `R` -> `-1`
- `L` -> `0`  => fourth

Answer:

    4

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static int balancedStringSplit(String s) {
    int balance = 0;
    int ans = 0;

    for (char c : s.toCharArray()) {
        if (c == 'L') balance++;
        else balance--;

        if (balance == 0) ans++;
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- balance tracking
- greedy cut at the earliest valid point

------------------------------------------------------------------------

## End of Notes
