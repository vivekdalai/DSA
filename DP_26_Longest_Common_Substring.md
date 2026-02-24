# Dynamic Programming Notes

## 26 - Longest Common Substring

**Generated on:** 2026-02-24 20:50:43 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given two strings `s1` and `s2`, find the length of the Longest Common Substring (continuous segment) present in both strings. Optionally reconstruct one such substring.

Important distinction:
- Subsequence: characters kept in order but not necessarily contiguous (LCS).
- Substring: characters must be contiguous. On mismatch, current matching streak must reset to 0.

Example:
- s1 = "abcjklp", s2 = "acjkp"
- Longest common substring is "cjk" (length 3).

------------------------------------------------------------------------

## 🪜 2. State Definition

For tabulation (1-based indexing):
- `dp[i][j]` = length of the longest common substring that ends at `s1[i-1]` and `s2[j-1]`.

Answer:
- `max(dp[i][j])` over all `i, j`.

Key idea:
- If `s1[i-1] == s2[j-1]` → extend the previous diagonal streak: `dp[i][j] = 1 + dp[i-1][j-1]`.
- Else → reset: `dp[i][j] = 0`.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

Base:
- `dp[0][*] = 0`, `dp[*][0] = 0` (empty prefix gives 0-length substring)

Transition:
- If `s1[i-1] == s2[j-1]`:
  - `dp[i][j] = 1 + dp[i-1][j-1]`
- Else:
  - `dp[i][j] = 0`

Track:
- Maintain `maxLen` and optionally `endIndex` in `s1` to reconstruct the substring.

------------------------------------------------------------------------

## 💻 4A. Bottom-Up Tabulation (Length only)

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
                    dp[i][j] = 0; // reset streak on mismatch
                }
            }
        }
        return maxLen;
    }
}
```

Complexity:
- Time: O(n · m)
- Space: O(n · m)

------------------------------------------------------------------------

## 💻 4B. Bottom-Up with Reconstruction (Return one substring)

```java
class LongestCommonSubstring {
    public String longestCommonSubstring(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        int maxLen = 0;
        int endIdxInS1 = -1; // end index (1-based 'i') of best substring

        for (int i = 1; i <= n; i++) {
            char a = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char b = s2.charAt(j - 1);
                if (a == b) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                    if (dp[i][j] > maxLen) {
                        maxLen = dp[i][j];
                        endIdxInS1 = i; // substring ends at i-1 in s1
                    }
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        if (maxLen == 0) return "";
        int start = endIdxInS1 - maxLen; // inclusive
        return s1.substring(start, endIdxInS1);
    }
}
```

------------------------------------------------------------------------

## 💻 4C. Space-Optimized (Two Rows)

We can reduce space to O(m) using rolling rows, but to reconstruct the substring you still need to track end index and length.

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

## 🔎 5. Full Dry Run Example

s1 = "ababc", s2 = "babca"

dp (i rows for s1, j cols for s2) tracks streaks that end at i-1 and j-1:
- Matching positions extend the diagonal streak.
- Mismatch resets to 0.
Max observed streak length = 4 for substring "babc" (present in both).

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Longest Common Substring (contiguous match)
- Family: DP on two strings (match streak)
- Triggers:
  - Require contiguous matching segment across two strings
  - Reset streak on mismatch (contrast with LCS which takes max without reset)

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty strings → answer 0 / "".
- All characters mismatch → 0.
- Very long strings → prefer O(m) space version for memory.
- Don’t confuse with LCS: for substring, dp cell becomes 0 on mismatch (no carry-over max at cell level).

------------------------------------------------------------------------

## 🔁 8. Variants

- Longest Common Substring across multiple strings (harder; generalization).
- Case-insensitive comparisons (normalize input first).
- Longest Repeated Substring in a single string (different techniques: suffix arrays/automata; DP for small constraints).

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Use dp[i][j] as “length of matching suffix ending at i-1, j-1”.
- On match: 1 + diagonal; on mismatch: 0.
- Maintain global max (and end index for reconstruction if needed).

------------------------------------------------------------------------

# End of Notes
