# Dynamic Programming Notes

## 24 - Longest Common Subsequence (LCS)

**Generated on:** 2026-02-24 20:48:40 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given two strings `text1` and `text2`, find the length of their Longest Common Subsequence (LCS).  
A subsequence is a sequence that appears in the same relative order, but not necessarily contiguous.

Example:
- X = "abcdgh"
- Y = "abedfhr"
- LCS = "abdh" (length 4)

Why it’s DP:
- Optimal substructure with overlapping subproblems:
  - If last characters match, include it and move both pointers left.
  - Otherwise, skip one character from either string and take the max.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `f(i, j)` = length of LCS of `X[0..i]` and `Y[0..j]` (0-based indices)

Answer:
- `f(n-1, m-1)` where `n = X.length`, `m = Y.length`

Tabulation form:
- `dp[i][j]` = LCS length of prefixes `X[0..i-1]` and `Y[0..j-1]` (1-based dp)

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For 0-based recursion:
- If `X[i] == Y[j]`:
  - `f(i, j) = 1 + f(i-1, j-1)`
- Else:
  - `f(i, j) = max(f(i-1, j), f(i, j-1))`

Base:
- If `i < 0` or `j < 0`: return 0

For 1-based tabulation:
- If `X[i-1] == Y[j-1]`:
  - `dp[i][j] = 1 + dp[i-1][j-1]`
- Else:
  - `dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

Base row/col:
- `dp[0][*] = 0`, `dp[*][0] = 0`

------------------------------------------------------------------------

## 💻 4A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class LCSMemo {
    public int longestCommonSubsequence(String text1, String text2) {
        int n = text1.length(), m = text2.length();
        int[][] dp = new int[n][m];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(text1, text2, n - 1, m - 1, dp);
    }

    private int solve(String a, String b, int i, int j, int[][] dp) {
        if (i < 0 || j < 0) return 0;
        if (dp[i][j] != -1) return dp[i][j];

        if (a.charAt(i) == b.charAt(j)) {
            return dp[i][j] = 1 + solve(a, b, i - 1, j - 1, dp);
        } else {
            int left  = solve(a, b, i - 1, j, dp);
            int right = solve(a, b, i, j - 1, dp);
            return dp[i][j] = Math.max(left, right);
        }
    }
}
```

Complexity:
- Time: O(n · m)
- Space: O(n · m) memo + O(n + m) recursion

------------------------------------------------------------------------

## 💻 4B. Bottom-Up Tabulation (1-based)

```java
class LCSTab {
    public int longestCommonSubsequence(String text1, String text2) {
        int n = text1.length(), m = text2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            char ca = text1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char cb = text2.charAt(j - 1);
                if (ca == cb) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n][m];
    }
}
```

Complexity:
- Time: O(n · m)
- Space: O(n · m)

------------------------------------------------------------------------

## 💻 4C. Space-Optimized (O(m)) using rolling rows

```java
class LCSSpace {
    public int longestCommonSubsequence(String text1, String text2) {
        int n = text1.length(), m = text2.length();
        int[] prev = new int[m + 1];

        for (int i = 1; i <= n; i++) {
            int[] curr = new int[m + 1];
            char ca = text1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char cb = text2.charAt(j - 1);
                if (ca == cb) {
                    curr[j] = 1 + prev[j - 1];
                } else {
                    curr[j] = Math.max(prev[j], curr[j - 1]);
                }
            }
            prev = curr;
        }
        return prev[m];
    }
}
```

Complexity:
- Time: O(n · m)
- Space: O(m)

------------------------------------------------------------------------

## 🔎 5. Full Dry Run Example

X = "abef" (n=4)  
Y = "abcdaf" (m=6)

Build 1-based dp (rows for X, cols for Y):
- Initialize first row/col with 0
- Matches at (i,j): when `X[i-1] == Y[j-1]` then `dp[i][j] = 1 + dp[i-1][j-1]`
- Else `max(top, left)`

Final `dp[4][6] = 3` (LCS length is 3: e.g., "abf").

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: LCS (DP on two strings)
- Family: DP on sequences (edit/compare)
- Triggers:
  - Compare two sequences to find common ordered pattern
  - Similar structure underlies Edit Distance, SCS, Distinct Subsequences, LPS (via reverse)

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty string ⇒ LCS length 0
- Highly repetitive characters ⇒ no issue for DP; but recursion stack can be deep; prefer bottom-up for very long strings
- Don’t confuse with Longest Common Substring (requires contiguous match and resets on mismatch)

------------------------------------------------------------------------

## 🔁 8. Variants

- Print the actual LCS (requires backtracking the dp table)
- Shortest Common Supersequence (SCS) length = n + m - LCS
- Longest Palindromic Subsequence (LPS): LCS of `s` and `reverse(s)`
- Distinct Subsequences and Edit Distance use similar 2D DP structures

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Core LCS transitions:
  - If last chars match: 1 + diag
  - Else: max(top, left)
- Use O(m) space rolling arrays for memory efficiency.
- Forms the base for many string DP problems.

------------------------------------------------------------------------

# End of Notes
