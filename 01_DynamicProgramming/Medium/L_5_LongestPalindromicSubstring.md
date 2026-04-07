# DP Notes

## 5 - Longest Palindromic Substring

**Generated on:** 2026-04-08 00:02:17 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/longest-palindromic-substring/description/
<!-- leetcode-link-end -->

## 1. LeetCode Question Statement

Given a string `s`, return the longest contiguous substring that is a palindrome.
Unlike the subsequence version, you are not allowed to skip characters, so the answer must come from a continuous window.

**Example 1**

```text
Input: s = "babad"
Output: "bab"
Explanation: "aba" is also valid. This file's DP traversal often lands on "aba" first.
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

## 2. Which Version To Revise From This File

This file keeps two approaches:

- `longestPalindrome_v2`: bottom-up DP over all substrings
- `longestPalindrome`: recursive search plus a memoized palindrome check

For revision, `longestPalindrome_v2` is the one worth memorizing. It is shorter, easier to reason about, and matches the cleanest interview explanation.

------------------------------------------------------------------------

## 3. Palindrome Table Logic

`dp[l][r]` is `true` if substring `s[l..r]` is a palindrome.

The code fills `l` from right to left so `dp[l + 1][r - 1]` is already known when checking a larger window.

Transition used in `longestPalindrome_v2`:

- If `s.charAt(l) != s.charAt(r)`, the window is not a palindrome.
- If the ends match and the length is `1`, `2`, or `3`, the window is automatically valid.
- For longer windows, validity depends on `dp[l + 1][r - 1]`.

Whenever a window becomes `true` and beats the current best length, the code updates `start` and `maxLen`.

The recursive method in the same file uses pruning with `ans.length() >= (r - l + 1)`, which is clever, but it is harder to present cleanly in an interview.

------------------------------------------------------------------------

## 4. Quick Walkthrough on `babad`

Single letters are palindromes, so the table diagonal is `true`.

- Window `"aba"` becomes valid because the ends match and the inner `"b"` is already known to be a palindrome.
- That updates the best answer to length `3`.
- Later `"bab"` is also valid, but it does not beat the current best length, so the stored answer stays the first max window found by the traversal.

That is why this implementation may return `"aba"` for `"babad"`, even though `"bab"` is equally correct.

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

- For palindrome substring DP, remember the state as a boolean question: `is this window valid?`
- `right - left <= 2` is the small-window shortcut that avoids special casing lengths `1`, `2`, and `3` separately.
- If you see a file with both recursion and bottom-up DP, revise the bottom-up version first unless the recursion is clearly simpler.

------------------------------------------------------------------------

## End of Notes
