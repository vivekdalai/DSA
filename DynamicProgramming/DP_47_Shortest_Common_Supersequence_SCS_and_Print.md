# Dynamic Programming Notes

## 47 - Shortest Common Supersequence (SCS) — Length and Print One SCS

**Generated on:** 2026-02-25 23:19:37 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given two strings `s1` and `s2`, find:
- The length of their Shortest Common Supersequence (SCS)
- Optionally construct one valid SCS string

A supersequence of `s1` and `s2` is a string that contains both as subsequences (order preserved).  
The SCS is the shortest such string.

Examples:
- s1 = "abac", s2 = "cab"  
  SCS could be "cabac" (length 5)

Key relation:
- SCS length = n + m − LCS(s1, s2) length

------------------------------------------------------------------------

## 🪜 2. DP Definitions

A) Via LCS (length only, then reconstruct SCS using LCS backtrack)
- Compute LCS dp table
- SCS length = n + m − dp[n][m]

B) Direct SCS DP (constructive and interview-friendly)
- Let `dp[i][j]` = length of SCS of `s1[0..i-1]` and `s2[0..j-1]`
- Recurrence:
  - If `i == 0`: `dp[i][j] = j`
  - If `j == 0`: `dp[i][j] = i`
  - If `s1[i-1] == s2[j-1]`:
    - `dp[i][j] = 1 + dp[i-1][j-1]`
  - Else:
    - `dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1])`
- Backtrack to build one SCS

Complexity:
- Time: O(n · m)
- Space: O(n · m) (can optimize length to O(min(n,m)), but reconstruction benefits from 2D)

------------------------------------------------------------------------

## 💻 3A. SCS Length via LCS (Concise)

```java
import java.util.*;

class SCS_Length_via_LCS {
    public int scsLength(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            char a = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char b = s2.charAt(j - 1);
                if (a == b) dp[i][j] = 1 + dp[i - 1][j - 1];
                else dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        int lcs = dp[n][m];
        return n + m - lcs;
    }
}
```

------------------------------------------------------------------------

## 💻 3B. Direct SCS (Length + Construct One SCS)

```java
import java.util.*;

class SCS_Construct {
    // Returns one valid SCS string (shortest supersequence)
    public String shortestCommonSupersequence(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        // base cases
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        // fill table
        for (int i = 1; i <= n; i++) {
            char a = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char b = s2.charAt(j - 1);
                if (a == b) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // backtrack to build one SCS
        int i = n, j = m;
        StringBuilder sb = new StringBuilder();
        while (i > 0 && j > 0) {
            char a = s1.charAt(i - 1);
            char b = s2.charAt(j - 1);
            if (a == b) {
                sb.append(a);
                i--; j--;
            } else {
                if (dp[i - 1][j] <= dp[i][j - 1]) {
                    sb.append(a);
                    i--;
                } else {
                    sb.append(b);
                    j--;
                }
            }
        }
        // append remaining prefix (one of them could be non-empty)
        while (i > 0) {
            sb.append(s1.charAt(i - 1));
            i--;
        }
        while (j > 0) {
            sb.append(s2.charAt(j - 1));
            j--;
        }
        return sb.reverse().toString();
    }

    // Optional: length directly from dp
    public int scsLength(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            char a = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char b = s2.charAt(j - 1);
                if (a == b) dp[i][j] = 1 + dp[i - 1][j - 1];
                else dp[i][j] = 1 + Math.min(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[n][m];
    }

    // Example
    public static void main(String[] args) {
        SCS_Construct scs = new SCS_Construct();
        System.out.println(scs.shortestCommonSupersequence("abac", "cab")); // "cabac"
        System.out.println(scs.scsLength("abac", "cab"));                   // 5
    }
}
```

Notes:
- Backtracking chooses matching chars once and otherwise decides based on the smaller dp predecessor.
- If equal, either choice yields a valid shortest SCS; tie-breaking only affects which valid SCS you get.

------------------------------------------------------------------------

## 🔎 4. Worked Example

s1 = "abac", s2 = "cab"

dp (SCS lengths):
- dp[0][j] = j, dp[i][0] = i
- Matching chars advance diagonally; mismatches take 1 + min(top, left)
Final dp[n][m] = 5; one SCS is "cabac".

------------------------------------------------------------------------

## 🏷 5. Pattern Recognition

- Name: Shortest Common Supersequence (SCS)
- Family: Two-string DP (closely related to LCS)
- Triggers:
  - Need the shortest string that contains both strings as subsequences
  - Reconstruction by standard backtracking on 2D dp

------------------------------------------------------------------------

## 🔄 6. Edge Cases and Pitfalls

- One string empty → SCS is the other string
- Many repeated characters → backtracking ties; any valid SCS acceptable
- For length only, SCS = n + m − LCS; for printing SCS, build from direct SCS dp or leverage LCS backtrack to interleave leftovers

------------------------------------------------------------------------

## ✅ 7. Takeaway

- SCS length has a simple relationship to LCS.
- To print SCS, backtrack the SCS dp table, appending matched chars once and otherwise taking the path of smaller predecessor and appending that char.
- O(n·m) is standard and interview-acceptable.

------------------------------------------------------------------------

# End of Notes
