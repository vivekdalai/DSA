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

## 6B. Space-Optimized Alternative — Center Expansion (O(1) extra space)

The `O(n^2)` table is not required to solve this problem; every palindrome has a unique center (either a single character for odd length, or a gap between two characters for even length). Expanding outward from each of the `2n - 1` centers while `s[l] == s[r]` counts every palindromic substring exactly once, with no table at all.

```java
public int countSubstrings(String s) {
    int n = s.length();
    int count = 0;

    for (int center = 0; center < 2 * n - 1; center++) {
        int l = center / 2;
        int r = l + (center % 2);
        while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
            count++;
            l--;
            r++;
        }
    }
    return count;
}
```

Complexity:
- Time: `O(n^2)` worst case (e.g., `"aaaa...a"`), but `O(1)` extra space beyond the input.
- Space: `O(1)`

This is the version to lead with in an interview: same asymptotic time as the DP table, but no `O(n^2)` memory.

------------------------------------------------------------------------

## 7. Edge Cases and Pitfalls

- Empty string is disallowed by constraints (`1 <= s.length`), but a length-1 string must still return `1`.
- All-identical characters (e.g., `"aaa"`) is the worst case for center expansion — every one of the `2n - 1` centers expands fully, giving `n(n+1)/2` palindromes.
- Off-by-one in center expansion: `center / 2` and `center + center % 2` must be derived carefully to cover both odd-length (single character) and even-length (gap) centers; verify with a 2-character string like `"aa"`.
- In the DP table version, the `right - left <= 2` shortcut must be checked *before* falling back to `dp[left + 1][right - 1]`, otherwise length-2/3 windows read an uninitialized cell.
- Don't confuse this with Longest Palindromic Substring (LC 5) — that problem tracks the single best window; this one accumulates a count over every valid window.

------------------------------------------------------------------------

## 8. Pattern Recognition / Revision Notes

- Same DP table as problem `5`, different output objective.
- When a boolean DP state directly answers `is this window good?`, you can often count on the fly instead of storing extra information.
- Revision shortcut: longest palindrome and count palindromes share the same recurrence; only the bookkeeping changes.
- Center expansion is the space-optimal way to enumerate all palindromic substrings/counts; reach for the DP table mainly when you need random-access answers to "is s[l..r] a palindrome?" queries.

------------------------------------------------------------------------

## End of Notes
