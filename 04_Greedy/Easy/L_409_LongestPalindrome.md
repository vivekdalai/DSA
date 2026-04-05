# Greedy Notes

## 11 - Longest Palindrome

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/longest-palindrome/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a string `s` consisting of uppercase and lowercase letters, return the length of the longest palindrome that can be built using those letters.

You do not need to use all characters.

**Example 1**

    Input: s = "abccccdd"
    Output: 7

One valid palindrome is:

    "dccaccd"

**Example 2**

    Input: s = "a"
    Output: 1

------------------------------------------------------------------------

## 2. Pairing Intuition

A palindrome is symmetric.

So most characters must be used in pairs:

- one copy on the left
- one copy on the right

Only one character with odd frequency can sit in the center.

That gives the greedy rule:

- take all even counts completely
- from odd counts, take `count - 1`
- if any odd count exists, add `1` for the center

------------------------------------------------------------------------

## 3. Logic In The File

The code counts character frequencies, then scans them:

- if `cnt` is even, add all of it
- if `cnt` is odd, add `cnt - 1` and remember that an odd count exists

At the end:

- if at least one odd frequency existed, add one center character

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    s = "abccccdd"

Frequencies:

    a -> 1
    b -> 1
    c -> 4
    d -> 2

Take usable pairs:

    0 + 0 + 4 + 2 = 6

Since odd counts exist, place one extra character in the middle:

    6 + 1 = 7

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static int longestPalindrome(String s) {
    int[] freq = new int[200];

    for (char ch : s.toCharArray()) {
        freq[ch]++;
    }

    int ans = 0;
    boolean oddFound = false;

    for (int cnt : freq) {
        if (cnt % 2 == 0) {
            ans += cnt;
        } else {
            ans += cnt - 1;
            oddFound = true;
        }
    }

    if (oddFound) ans++;
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)` because the frequency table size is fixed

Pattern:

- count frequencies
- extract all pairs
- allow one center

------------------------------------------------------------------------

## End of Notes
