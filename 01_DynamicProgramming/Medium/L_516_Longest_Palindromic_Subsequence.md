# DP Notes

## 516 - Longest Palindromic Subsequence

**Generated on:** 2026-04-08 00:02:17 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/longest-palindromic-subsequence/description/
<!-- leetcode-link-end -->

## 1. LeetCode Question Statement

Given a string `s`, return the length of the longest subsequence that reads the same from both ends.
You may skip characters, but you must preserve relative order. This is the important difference from the substring version.

**Example 1**

```text
Input: s = "bbbab"
Output: 4
Explanation: One longest palindromic subsequence is "bbbb".
```

**Example 2**

```text
Input: s = "cbbd"
Output: 2
Explanation: The best subsequence is "bb".
```

**Constraints**

- `1 <= s.length <= 1000`
- `s` contains only lowercase English letters.

------------------------------------------------------------------------

## 2. Interval DP Intuition

The file uses classic interval DP on `s[l..r]`.

- If the two ends match, they can both participate in the best subsequence.
- If the two ends do not match, one of them must be skipped.

That gives a very small decision tree for every window, which is exactly why a 2D DP table works well here.

------------------------------------------------------------------------

## 3. What `dp[l][r]` Means In This File

`dp[l][r]` stores the longest palindromic subsequence length inside substring `s[l..r]`.

The implementation fills the table with `l` moving from right to left and `r` moving from `l + 1` to the end. That order guarantees these smaller states are already ready:

- `dp[l + 1][r - 1]`
- `dp[l + 1][r]`
- `dp[l][r - 1]`

Transition used by the code:

- Match: `dp[l][r] = 2 + dp[l + 1][r - 1]`
- Mismatch: `dp[l][r] = max(dp[l + 1][r], dp[l][r - 1])`

Single characters are initialized to `1`. For adjacent equal characters, Java's default `0` in the empty inner window slot makes the `2 + dp[l + 1][r - 1]` formula work naturally.

------------------------------------------------------------------------

## 4. Walkthrough on `bbbab`

Start with all diagonal cells as `1` because every one-letter string is a palindrome.

- Window `"bb"` gives `2`
- Window `"bbb"` grows to `3`
- Window `"bbba"` cannot use both ends, so it keeps the better of the two smaller windows
- Window `"bbbab"` has matching ends `'b'` and `'b'`, so it becomes `2 + dp[1][3] = 4`

Final answer: `dp[0][4] = 4`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int longestPalindromeSubseq(String s) {
    int n = s.length();
    int[][] dp = new int[n][n];

    for (int i = 0; i < n; i++) {
        dp[i][i] = 1;
    }

    for (int left = n - 1; left >= 0; left--) {
        for (int right = left + 1; right < n; right++) {
            if (s.charAt(left) == s.charAt(right)) {
                dp[left][right] = 2 + dp[left + 1][right - 1];
            } else {
                dp[left][right] = Math.max(dp[left + 1][right], dp[left][right - 1]);
            }
        }
    }

    return dp[0][n - 1];
}
```

------------------------------------------------------------------------

## 6. Time and Space Complexity

- **Time:** `O(n^2)`
- **Space:** `O(n^2)`

------------------------------------------------------------------------

## 7. Pattern Recognition / Revision Notes

- Think `interval DP` whenever a string answer for `l..r` depends on shrinking one or both ends.
- Subsequence problems often use `skip left` vs `skip right` when characters do not match.
- The fill order matters: reverse `left`, forward `right` is the cleanest way to ensure inner windows are already solved.

------------------------------------------------------------------------

## End of Notes
