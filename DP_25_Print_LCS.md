# Dynamic Programming Notes

## 25 - Print LCS (Reconstruct the Longest Common Subsequence)

**Generated on:** 2026-02-24 20:49:46 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given two strings `s1` and `s2`, print one valid Longest Common Subsequence (LCS) string (not just its length).

Recall:
- LCS is a subsequence (not necessarily contiguous) common to both strings having maximum possible length.
- Multiple valid LCS strings may exist; printing any one is acceptable.

------------------------------------------------------------------------

## 🪜 2. Approach Overview

Two steps:
1) Compute the standard LCS length table `dp[i][j]` (1-based) where:
   - `dp[i][j] = LCS length of s1[0..i-1] and s2[0..j-1]`
2) Backtrack from `dp[n][m]` to build one LCS string:
   - If `s1[i-1] == s2[j-1]`: append this character and move to `(i-1, j-1)`
   - Else move in the direction of the larger value: `(i-1, j)` or `(i, j-1)`

Finally reverse the constructed string (since we build from the end) or build using a stack.

------------------------------------------------------------------------

## 🔁 3. DP Definition and Recurrence (1-based)

- Base:
  - `dp[0][*] = 0`, `dp[*][0] = 0`
- Recurrence:
  - If `s1[i-1] == s2[j-1]`:  
    `dp[i][j] = 1 + dp[i-1][j-1]`
  - Else:  
    `dp[i][j] = max(dp[i-1][j], dp[i][j-1])`

Backtracking preference:
- If `s1[i-1] == s2[j-1]`: pick that char and move `(i-1, j-1)`.
- Else move towards the cell with the greater dp value:
  - If `dp[i-1][j] > dp[i][j-1]`: `i--`
  - Else: `j--`

------------------------------------------------------------------------

## 💻 4. Clean Implementation (Java)

```java
import java.util.*;

class PrintLCS {
    public String lcsString(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        // Build LCS length table
        for (int i = 1; i <= n; i++) {
            char c1 = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char c2 = s2.charAt(j - 1);
                if (c1 == c2) dp[i][j] = 1 + dp[i - 1][j - 1];
                else dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        // Reconstruct one LCS by backtracking
        int i = n, j = m;
        StringBuilder sb = new StringBuilder();
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                sb.append(s1.charAt(i - 1));
                i--; j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        return sb.reverse().toString();
    }

    // Optional: return both length and string
    public Map.Entry<Integer, String> lcsLengthAndString(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 1; i <= n; i++) {
            char c1 = s1.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char c2 = s2.charAt(j - 1);
                if (c1 == c2) dp[i][j] = 1 + dp[i - 1][j - 1];
                else dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        // Backtrack
        int i = n, j = m;
        StringBuilder sb = new StringBuilder();
        while (i > 0 && j > 0) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                sb.append(s1.charAt(i - 1));
                i--; j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        String lcs = sb.reverse().toString();
        return new AbstractMap.SimpleEntry<>(dp[n][m], lcs);
    }
}
```

Complexity:
- Time: O(n · m)
- Space: O(n · m) for table; backtracking O(n + m)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

s1 = "abcde"  
s2 = "bdgek"

- dp ends with dp[5][5] = 2
- One valid LCS: "bde" is not common (since 'e' not in s2 subsequence after 'd'?), better example:
  For these strings, a valid LCS is "bde"? Actually "bde" is not subsequence of "bdgek" (order: b d g e k → 'e' after 'g', still OK, but 'c' not present).  
  A safe, simple example:
  s1 = "AGGTAB", s2 = "GXTXAYB"
  LCS = "GTAB" (length 4)

Backtracking correctly builds "GTAB".

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: LCS Reconstruction
- Family: DP on strings comparison (edit/compare)
- Triggers:
  - Need actual subsequence, not just its length
  - Backtracking over the LCS length table

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Multiple valid LCS outputs; your backtrack path determines which one you get.
- If you pick `>` vs `>=` differently on ties, you may get a different valid LCS.
- Empty string input returns empty LCS.

------------------------------------------------------------------------

## 🔁 8. Variants

- Print all LCS: requires additional bookkeeping; exponential count in worst case.
- SCS (Shortest Common Supersequence): build string while backtracking dp table; add non-matching chars appropriately.
- Print SCS and Print LPS use similar backtracking ideas.

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Compute dp length table, then backtrack to collect characters.
- Tie-handling affects which LCS you get, but any one is valid.
- This template is foundational for SCS construction and related problems.

------------------------------------------------------------------------

# End of Notes
