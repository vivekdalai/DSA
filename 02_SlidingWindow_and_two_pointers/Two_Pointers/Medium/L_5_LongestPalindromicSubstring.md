# Two Pointers Notes

## 5 - Longest Palindromic Substring

**Generated on:** 2026-04-08 00:02:17 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/longest-palindromic-substring/description/
<!-- leetcode-link-end -->

## 1. LeetCode Question Statement

Given a string `s`, return the longest contiguous palindromic substring.
The answer must be a continuous segment, not a subsequence with gaps.

**Example 1**

```text
Input: s = "babad"
Output: "bab"
Explanation: "aba" is also valid, and this specific implementation often returns that one because of its traversal order.
```

**Example 2**

```text
Input: s = "cbbd"
Output: "bb"
```

**Constraints**

- `1 <= s.length <= 1000`
- `s` contains digits and English letters.

------------------------------------------------------------------------

## 2. Actual Implementation In This File

Even though this class sits in `Two_Pointers`, the code itself is not a center-expansion solution.

It contains the same two palindrome-DP approaches as the DP folder version:

- a bottom-up boolean table in `longestPalindrome_v2`
- a recursive search with memoized palindrome checks in `longestPalindrome`

For revision, focus on the bottom-up table because that is the cleanest explanation of what the file is doing.

------------------------------------------------------------------------

## 3. DP Table And Memoized Helper

The main DP idea is:

- `dp[l][r] = true` if `s[l..r]` is a palindrome
- outer characters must match
- longer windows depend on the inner window `dp[l + 1][r - 1]`

Small windows of length `1`, `2`, and `3` are handled by the `right - left <= 2` shortcut.

The recursive variant uses `validPalindrome(l, r, s, dp)` as a memoized checker and prunes windows that cannot beat the current answer. That is interesting to read, but the table version is still the better interview story.

------------------------------------------------------------------------

## 4. Walkthrough on `cbbd`

- Single letters `"c"`, `"b"`, `"b"`, `"d"` are palindromes
- Window `"bb"` is valid because the ends match and the length is `2`
- Windows `"cbb"` and `"bbd"` fail because their outer characters do not match
- Window `"cbbd"` also fails for the same reason

Longest valid window found by the DP: `"bb"`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static String longestPalindrome_v2(String s) {
    int n = s.length();
    boolean[][] dp = new boolean[n][n];
    int start = 0;
    int maxLen = 1;

    for (int left = n - 1; left >= 0; left--) {
        for (int right = left; right < n; right++) {
            if (s.charAt(left) == s.charAt(right)) {
                if (right - left <= 2) {
                    dp[left][right] = true;
                } else {
                    dp[left][right] = dp[left + 1][right - 1];
                }

                if (dp[left][right] && right - left + 1 > maxLen) {
                    start = left;
                    maxLen = right - left + 1;
                }
            }
        }
    }

    return s.substring(start, start + maxLen);
}
```

------------------------------------------------------------------------

## 6. Time and Space Complexity

- **Time:** `O(n^2)`
- **Space:** `O(n^2)`

------------------------------------------------------------------------

## 7. Pattern Recognition / Revision Notes

- Folder names can mislead; always revise the algorithm that the file actually implements.
- This is still interval DP, just like the DP folder copy of the same problem.
- For palindrome window problems, remember: outer match plus solved inner window.

------------------------------------------------------------------------

## End of Notes
