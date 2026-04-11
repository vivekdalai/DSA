# DP Notes

## 647 - Palindromic Substrings

**Generated on:** 2026-04-08 00:02:17 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/palindromic-substrings/description/
<!-- leetcode-link-end -->

## 1. LeetCode Question Statement

Given a string `s`, count how many of its contiguous substrings are palindromes.
Every single character counts, and longer palindromes are counted separately whenever they appear in a different position.

**Example 1**

```text
Input: s = "abc"
Output: 3
Explanation: Only the one-letter substrings are palindromes.
```

**Example 2**

```text
Input: s = "aaa"
Output: 6
Explanation: Three singles, two length-2 palindromes, and one length-3 palindrome.
```

**Constraints**

- `1 <= s.length <= 1000`
- `s` contains only lowercase English letters.

------------------------------------------------------------------------

## 2. Count While Filling The Palindrome Table

The implementation uses the same interval-DP idea as Longest Palindromic Substring, but the goal is different.

Here, every time a window is confirmed to be a palindrome, the answer increments immediately. There is no need to keep only the longest one.

------------------------------------------------------------------------

## 3. How The Boolean DP Works

`dp[l][r]` is `true` if substring `s[l..r]` is a palindrome.

The file iterates `l` from right to left and `r` from `l` to the end.

- Ends mismatch: `dp[l][r]` stays `false`
- Ends match and window length is at most `3`: `true`
- Ends match and window is longer: copy `dp[l + 1][r - 1]`

Right after that check, the code does:

- `if (dp[l][r]) cnt++;`

That single line is the whole reason this problem becomes a counting problem instead of a longest-window problem.

------------------------------------------------------------------------

## 4. Walkthrough on `aaa`

Start from the right.

- All three single characters are palindromes, so count becomes `3`
- Substring `"aa"` at indices `(1, 2)` is valid, count becomes `4`
- Substring `"aa"` at indices `(0, 1)` is valid, count becomes `5`
- Substring `"aaa"` is valid because the outer `'a'` matches and the inner single `'a'` is already valid, so count becomes `6`

Final answer: `6`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int countSubstrings(String s) {
    int n = s.length();
    int count = 0;
    boolean[][] dp = new boolean[n][n];

    for (int left = n - 1; left >= 0; left--) {
        for (int right = left; right < n; right++) {
            if (s.charAt(left) == s.charAt(right)) {
                if (right - left <= 2) {
                    dp[left][right] = true;
                } else {
                    dp[left][right] = dp[left + 1][right - 1];
                }
            }

            if (dp[left][right]) {
                count++;
            }
        }
    }

    return count;
}
```

------------------------------------------------------------------------

## 6. Time and Space Complexity

- **Time:** `O(n^2)`
- **Space:** `O(n^2)`

------------------------------------------------------------------------

## 7. Pattern Recognition / Revision Notes

- Same DP table as problem `5`, different output objective.
- When a boolean DP state directly answers `is this window good?`, you can often count on the fly instead of storing extra information.
- Revision shortcut: longest palindrome and count palindromes share the same recurrence; only the bookkeeping changes.

------------------------------------------------------------------------

## End of Notes
