# Greedy Notes

## Break a Palindrome

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given a palindromic string `palindrome`.

Replace exactly one character so that the resulting string is not a palindrome and is lexicographically smallest possible.

If it is impossible, return an empty string.

**Example 1**

    Input: palindrome = "abccba"
    Output: "aaccba"

**Example 2**

    Input: palindrome = "a"
    Output: ""

------------------------------------------------------------------------

## 2. Lexicographic Intuition

To get the smallest possible string:

- change the earliest possible character
- make that character as small as possible

For letters, the smallest useful replacement is usually `'a'`.

But we must also avoid ending up with a palindrome again.

------------------------------------------------------------------------

## 3. Greedy Rule

If the string length is `1`, no answer exists.

Otherwise:

- scan only the first half
- if you find a character not equal to `'a'`, change it to `'a'` and return

Why only the first half?

Because changing an earlier position has a stronger lexicographic effect than changing its mirrored partner later.

If every character in the first half is already `'a'`, then the palindrome looks like:

    a...a

So the smallest valid break is to change the last character to `'b'`.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    palindrome = "abba"

First half:

    "ab"

The first non-`a` character is `'b'` at index `1`.

Change it to `'a'`:

    "aaba"

That is no longer a palindrome and is lexicographically minimal.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public String breakPalindrome(String palindrome) {
    int n = palindrome.length();
    if (n == 1) return "";

    char[] c = palindrome.toCharArray();

    for (int i = 0; i < n / 2; i++) {
        if (c[i] != 'a') {
            c[i] = 'a';
            return new String(c);
        }
    }

    c[n - 1] = 'b';
    return new String(c);
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(n)` because of the char array copy

Pattern:

- minimize lexicographically by fixing the earliest impactful position

------------------------------------------------------------------------

## End of Notes
