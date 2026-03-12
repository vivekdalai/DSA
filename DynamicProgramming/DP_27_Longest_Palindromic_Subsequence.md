# Dynamic Programming Notes

## 27 - Longest Palindromic Subsequence (LPS)

**Generated on:** 2026-02-24 20:51:45 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s`, find the length of its Longest Palindromic Subsequence (LPS).  
A subsequence is a sequence that retains the original relative order but need not be contiguous.  
A palindrome reads the same forwards and backwards.

Examples:
- s = "bbbab" → LPS length = 4 ("bbbb")
- s = "cbbd" → LPS length = 2 ("bb")

Why DP:
- Optimal substructure on intervals (i..j)
- Overlapping subproblems across sub-intervals

------------------------------------------------------------------------

## 🪜 2. Two Canonical Approaches

A) LCS Reduction  
- LPS(s) = LCS(s, reverse(s))  
- Any LCS between s and reverse(s) is palindromic.

B) Interval DP on (i, j)  
- dp[i][j] = LPS length in s[i..j]
- Transition based on matching end characters.

Both run in O(n^2) time.

------------------------------------------------------------------------

## 🔁 3. Recurrence (Interval DP)

Let n = s.length.

Base:
- For all i: dp[i][i] = 1 (single char is palindrome)

Transition:
- If s[i] == s[j]: dp[i][j] = 2 + dp[i+1][j-1]
- Else: dp[i][j] = max(dp[i+1][j], dp[i][j-1])

Fill order:
- Increasing substring length (len = 2..n), i from 0..n-len, j = i+len-1

Answer:
- dp[0][n-1]

------------------------------------------------------------------------

## 💻 4A. LCS-Based Solution (Java)

```java
class LPS_LCS_Reduction {
    public int longestPalindromeSubseq(String s) {
        String r = new StringBuilder(s).reverse().toString();
        int n = s.length(), m = r.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            char a = s.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char b = r.charAt(j - 1);
                if (a == b) dp[i][j] = 1 + dp[i - 1][j - 1];
                else dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[n][m];
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(n^2) (can optimize to O(n) with rolling arrays)

------------------------------------------------------------------------

## 💻 4B. Interval DP Solution (Java)

```java
class LPS_IntervalDP {
    public int longestPalindromeSubseq(String s) {
        int n = s.length();
        if (n == 0) return 0;

        int[][] dp = new int[n][n];

        // base: single chars
        for (int i = 0; i < n; i++) dp[i][i] = 1;

        // len from 2 to n
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = (len == 2) ? 2 : 2 + dp[i + 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[0][n - 1];
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(n^2)

------------------------------------------------------------------------

## 💻 4C. Reconstruct One LPS String (from Interval DP)

```java
import java.util.*;

class LPS_Reconstruct {
    public String longestPalindromicSubsequenceString(String s) {
        int n = s.length();
        if (n == 0) return "";

        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) dp[i][i] = 1;

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = (len == 2) ? 2 : 2 + dp[i + 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }

        // backtrack to build string
        int i = 0, j = n - 1;
        char[] res = new char[dp[0][n - 1]];
        int left = 0, right = res.length - 1;

        while (i <= j) {
            if (s.charAt(i) == s.charAt(j)) {
                if (left == right) {
                    res[left] = s.charAt(i); // center char for odd length
                } else {
                    res[left] = s.charAt(i);
                    res[right] = s.charAt(j);
                }
                i++; j--;
                left++; right--;
            } else if (dp[i + 1][j] >= dp[i][j - 1]) {
                i++;
            } else {
                j--;
            }
        }
        return new String(res);
    }
}
```

Notes:
- Any valid LPS is acceptable; ties in dp can yield different strings.

------------------------------------------------------------------------

## 🔎 5. Relationships and Corollaries

- Minimum insertions to make `s` a palindrome:
  - `minInsertions = n - LPS(s)`
- Minimum deletions to make `s` a palindrome:
  - `minDeletions = n - LPS(s)`
- LPS vs LPSUB (Longest Palindromic Substring, contiguous):
  - Different problems; LPS uses subsequence, LPSUB uses substring.

------------------------------------------------------------------------

## 🔎 6. Example Dry Run

s = "bbbab"
- Interval DP finds:
  - dp[i][i] = 1
  - matching 'b' at far ends grows palindromic length from inside
- Result dp[0][4] = 4 (e.g., "bbbb")

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: LPS (Interval DP / LCS with reverse)
- Family: DP on strings (edit/compare)
- Triggers:
  - Palindromic subsequence lengths
  - Intervals with matching ends

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- Empty string → 0
- All same char string → LPS = n
- Be careful with len==2 case in interval DP to avoid accessing dp[i+1][j-1] out of bounds

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Two interchangeable solutions: LCS(s, reverse(s)) or interval DP.
- Interval DP also enables reconstruction easily.
- LPS immediately gives minimum insertions/deletions to palindrome via `n - LPS`.

------------------------------------------------------------------------

# End of Notes
