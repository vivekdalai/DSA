# Greedy Notes

## 15 - Valid Palindrome II

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given a string `s`, return `true` if it can become a palindrome after deleting at most one character.

Otherwise, return `false`.

**Example 1**

    Input: s = "aba"
    Output: true

**Example 2**

    Input: s = "abca"
    Output: true

Explanation:

- Delete `b` to get `"aca"`
- or delete `c` to get `"aba"`

------------------------------------------------------------------------

## 2. Two-Pointer Intuition

A palindrome check normally uses:

- one pointer from the left
- one pointer from the right

As long as characters match, we keep moving inward.

The only tricky moment is the first mismatch.

Since only one deletion is allowed, there are only two legal choices:

- skip the left character
- skip the right character

------------------------------------------------------------------------

## 3. Greedy Structure

Walk inward with `i` and `j`.

If:

    s.charAt(i) == s.charAt(j)

continue.

Otherwise, return:

    isPalindrome(i + 1, j) || isPalindrome(i, j - 1)

We do not need more branching after that, because the deletion budget is already used.

------------------------------------------------------------------------

## 4. Quick Walkthrough

**Input:**

    s = "abca"

Compare:

- `a` and `a` -> match
- `b` and `c` -> mismatch

Try both options:

- skip `b` -> `"aca"` is a palindrome
- skip `c` -> `"aba"` is also a palindrome

So the answer is:

    true

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static boolean validPalindrome(String s) {
    int i = 0, j = s.length() - 1;

    while (i < j) {
        if (s.charAt(i) == s.charAt(j)) {
            i++;
            j--;
        } else {
            return isPalindrome(s, i + 1, j) || isPalindrome(s, i, j - 1);
        }
    }

    return true;
}

private static boolean isPalindrome(String s, int i, int j) {
    while (i < j) {
        if (s.charAt(i) != s.charAt(j)) return false;
        i++;
        j--;
    }
    return true;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- two pointers
- on first mismatch, branch exactly once

------------------------------------------------------------------------

## End of Notes
