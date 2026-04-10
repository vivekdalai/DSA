# Dynamic Programming Notes

## 26 - Longest Common Substring

**Generated on:** 2026-02-24 20:50:43 (IST)

------------------------------------------------------------------------

## 1. Problem Understanding

Given two strings `s1` and `s2`, find the length of the Longest Common Substring (continuous segment) present in both strings. Optionally reconstruct one such substring.

Important distinction:
- Subsequence: characters stay in order but are not required to be contiguous.
- Substring: characters must be contiguous in both strings.
- Because of that, a mismatch immediately breaks the current matching streak.

Example:
- `s1 = "abcjklp"`
- `s2 = "acjkp"`
- Longest common substring is `"cjk"` with length `3`.

------------------------------------------------------------------------

## 2. State Definition

The key word in this problem is **substring**.

That means our DP state should not mean:
- "best answer using the first `i` and `j` characters"

That kind of state is useful for Longest Common Subsequence, where skipping characters is allowed.

Instead, define:
- `dp[i][j]` = length of the longest common substring ending exactly at `s1[i - 1]` and `s2[j - 1]`

So each cell stores the length of the current matching suffix/streak ending at those two positions.

Why this state is correct:
- If the current characters match, we can extend the previous diagonal streak.
- If they do not match, contiguity is broken, so the streak becomes `0`.

Important consequence:
- The answer is not necessarily `dp[n][m]`.
- The best substring can end anywhere, so we take the maximum over all cells.

------------------------------------------------------------------------

## 3. Recurrence Relation

Base:
- `dp[0][*] = 0`
- `dp[*][0] = 0`
- If one prefix is empty, no common substring can end there.

Transition:
- If `s1[i - 1] == s2[j - 1]`:
  - `dp[i][j] = 1 + dp[i - 1][j - 1]`
- Else:
  - `dp[i][j] = 0`

Why only the diagonal matters:
- A substring must be continuous in both strings.
- So if `s1[i - 1]` and `s2[j - 1]` match, the previous characters must also come from the immediately previous positions.
- That is why we extend only from `dp[i - 1][j - 1]`.
- We do **not** use `max(dp[i - 1][j], dp[i][j - 1])` like LCS, because that would allow skipping characters and would represent subsequences, not substrings.

Track while solving:
- `maxLen` = best length seen so far
- `endIndex` in `s1` if we want to reconstruct one actual substring

------------------------------------------------------------------------

## 4A. Recursive + Memoization

For top-down DP, memoize the same meaning as the table state:
- `solve(i, j)` = length of the longest common substring ending at `s1[i - 1]` and `s2[j - 1]`

So:
- on match, extend diagonally
- on mismatch, return `0`

Since `solve(i, j)` gives the answer only for substrings ending at one specific pair of indices, we still evaluate all `(i, j)` pairs and keep the maximum.

```java
class LongestCommonSubstringMemo {
    public int longestCommonSubstring(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] memo = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= m; j++) {
                memo[i][j] = -1;
            }
        }

        int maxLen = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                maxLen = Math.max(maxLen, solve(i, j, s1, s2, memo));
            }
        }

        return maxLen;
    }

    private int solve(int i, int j, String s1, String s2, int[][] memo) {
        if (i == 0 || j == 0) return 0;

        if (memo[i][j] != -1) return memo[i][j];

        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
            memo[i][j] = 1 + solve(i - 1, j - 1, s1, s2, memo);
        } else {
            memo[i][j] = 0;
        }

        return memo[i][j];
    }
}
```

Complexity:
- Time: `O(n * m)`
- Space: `O(n * m)` for memoization + recursion stack

Why this version is useful:
- It makes the recurrence very explicit.
- It is a nice bridge between plain recursion and tabulation.
- Bottom-up is usually preferred in interviews and production because it avoids recursion overhead.

------------------------------------------------------------------------

## 4B. Bottom-Up Tabulation (Length only)

```java
class LongestCommonSubstringLen {
    public int longestCommonSubstring(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        int maxLen = 0;

        for (int i = 1; i <= n; i++) {
            char a = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char b = s2.charAt(j - 1);
                if (a == b) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                    if (dp[i][j] > maxLen) maxLen = dp[i][j];
                } else {
                    dp[i][j] = 0; // mismatch breaks the current substring
                }
            }
        }
        return maxLen;
    }
}
```

Complexity:
- Time: `O(n * m)`
- Space: `O(n * m)`

------------------------------------------------------------------------

## 4C. Bottom-Up with Reconstruction (Return one substring)

To reconstruct one actual substring, store:
- `maxLen`
- the ending index of that best substring in one of the strings

If the best substring ends at index `endIdxInS1 - 1` and has length `maxLen`, then:
- start index = `endIdxInS1 - maxLen`

```java
class LongestCommonSubstring {
    public String longestCommonSubstring(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        int maxLen = 0;
        int endIdxInS1 = -1;

        for (int i = 1; i <= n; i++) {
            char a = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char b = s2.charAt(j - 1);
                if (a == b) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                    if (dp[i][j] > maxLen) {
                        maxLen = dp[i][j];
                        endIdxInS1 = i;
                    }
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        if (maxLen == 0) return "";
        int start = endIdxInS1 - maxLen;
        return s1.substring(start, endIdxInS1);
    }
}
```

------------------------------------------------------------------------

## 4D. Space-Optimized (Two Rows)

We can reduce space to `O(m)` using rolling rows because each row depends only on the previous row.

Important note:
- For length only, two rows are enough.
- For reconstruction, we still need to track `maxLen` and the ending index.

```java
class LongestCommonSubstringSpace {
    public int longestCommonSubstring(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[] prev = new int[m + 1];
        int maxLen = 0;

        for (int i = 1; i <= n; i++) {
            int[] curr = new int[m + 1];
            char a = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char b = s2.charAt(j - 1);
                if (a == b) {
                    curr[j] = 1 + prev[j - 1];
                    if (curr[j] > maxLen) maxLen = curr[j];
                } else {
                    curr[j] = 0;
                }
            }
            prev = curr;
        }
        return maxLen;
    }
}
```

------------------------------------------------------------------------

## 5. Full Dry Run Example

Take:
- `s1 = "ababc"`
- `s2 = "babca"`

Interpretation of `dp[i][j]`:
- it stores the length of the matching substring ending at `s1[i - 1]` and `s2[j - 1]`

What happens:
- matching characters extend the diagonal streak
- mismatch resets the value to `0`

Best substring found:
- `"babc"`
- length = `4`

This example is a good reminder that the answer comes from the maximum value anywhere in the table, not just the last cell.

------------------------------------------------------------------------

## 6. Pattern Recognition

- Name: Longest Common Substring
- Family: DP on two strings
- Core signal:
  - matching characters extend diagonally
  - mismatch resets to `0`

How to identify it quickly:
- the problem asks for a **contiguous** segment common to both strings
- the transition talks about a current streak rather than a general best answer so far

------------------------------------------------------------------------

## 7. Edge Cases and Pitfalls

- Empty string input -> answer is `0` or `""`
- No characters match at all -> answer is `0`
- The best substring may appear in the middle, so do not return only `dp[n][m]`
- Do not confuse it with LCS:
  - LCS allows skipping
  - Longest Common Substring resets to `0` on mismatch
- For very large inputs, prefer the `O(m)` space version when only the length is required

------------------------------------------------------------------------

## 8. Variants

- Return only the length
- Return one actual substring
- Return all longest common substrings
- Case-insensitive comparison after normalizing the strings
- Longest repeated substring within one string (related idea, but often solved with suffix-based techniques)

------------------------------------------------------------------------

## 9. Takeaway

- Use `dp[i][j]` as "length of the matching suffix ending at `i - 1` and `j - 1`"
- On match: take `1 + diagonal`
- On mismatch: reset to `0`
- Maintain a global maximum because the answer can end anywhere

------------------------------------------------------------------------

# End of Notes
