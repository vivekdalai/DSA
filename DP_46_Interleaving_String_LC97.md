# Dynamic Programming Notes

## 46 - Interleaving String (LC 97) — DP on Two Pointers

**Generated on:** 2026-02-25 23:18:35 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given strings `s1`, `s2`, and `s3`, return true if `s3` is formed by an interleaving of `s1` and `s2`.

Interleaving means:
- `s3` contains all characters of `s1` and `s2` exactly once,
- and preserves the relative order of characters from `s1` and from `s2`.

Example:
- s1="aabcc", s2="dbbca", s3="aadbbcbcac" → true
- s1="aabcc", s2="dbbca", s3="aadbbbaccc" → false

Basic check: if `s1.length + s2.length != s3.length` → false.

------------------------------------------------------------------------

## 🪜 2. State Definition

Let `n = len(s1)`, `m = len(s2)`.  
Define:
- `dp[i][j]` = whether `s3[0..i+j-1]` can be formed by interleaving `s1[0..i-1]` and `s2[0..j-1]`.

Answer:
- `dp[n][m]`

Base:
- `dp[0][0] = true`
- First row: `dp[0][j] = dp[0][j-1] && (s2[j-1] == s3[j-1])`
- First col: `dp[i][0] = dp[i-1][0] && (s1[i-1] == s3[i-1])`

Transition:
- `dp[i][j] = (dp[i-1][j] && s1[i-1] == s3[i+j-1]) OR (dp[i][j-1] && s2[j-1] == s3[i+j-1])`

Complexity: O(n*m) time, O(n*m) space (can optimize to O(m)).

------------------------------------------------------------------------

## 💻 3. Bottom-Up (2D)

```java
import java.util.*;

public class InterleavingString2D {
    public boolean isInterleave(String s1, String s2, String s3) {
        int n = s1.length(), m = s2.length();
        if (n + m != s3.length()) return false;

        boolean[][] dp = new boolean[n + 1][m + 1];
        dp[0][0] = true;

        for (int j = 1; j <= m; j++) {
            dp[0][j] = dp[0][j - 1] && (s2.charAt(j - 1) == s3.charAt(j - 1));
        }
        for (int i = 1; i <= n; i++) {
            dp[i][0] = dp[i - 1][0] && (s1.charAt(i - 1) == s3.charAt(i - 1));
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char c = s3.charAt(i + j - 1);
                dp[i][j] = (dp[i - 1][j] && s1.charAt(i - 1) == c)
                        || (dp[i][j - 1] && s2.charAt(j - 1) == c);
            }
        }
        return dp[n][m];
    }

    public static void main(String[] args) {
        InterleavingString2D solver = new InterleavingString2D();
        System.out.println(solver.isInterleave("aabcc", "dbbca", "aadbbcbcac")); // true
        System.out.println(solver.isInterleave("aabcc", "dbbca", "aadbbbaccc")); // false
    }
}
```

------------------------------------------------------------------------

## ⚡ 4. Space-Optimized (1D over s2)

We can compress to 1D (size m+1):
- dp[j] uses previous row’s dp[j] for s1 extend, and current row’s dp[j-1] for s2 extend.

```java
import java.util.*;

public class InterleavingString1D {
    public boolean isInterleave(String s1, String s2, String s3) {
        int n = s1.length(), m = s2.length();
        if (n + m != s3.length()) return false;

        boolean[] dp = new boolean[m + 1];
        dp[0] = true;

        // First row (i=0)
        for (int j = 1; j <= m; j++) {
            dp[j] = dp[j - 1] && (s2.charAt(j - 1) == s3.charAt(j - 1));
        }

        for (int i = 1; i <= n; i++) {
            // Update dp[0] for this row (use s1 only)
            dp[0] = dp[0] && (s1.charAt(i - 1) == s3.charAt(i - 1));
            for (int j = 1; j <= m; j++) {
                char c = s3.charAt(i + j - 1);
                dp[j] = (dp[j] && s1.charAt(i - 1) == c) // extend from above (previous row)
                     || (dp[j - 1] && s2.charAt(j - 1) == c); // extend from left (current row)
            }
        }
        return dp[m];
    }

    public static void main(String[] args) {
        InterleavingString1D solver = new InterleavingString1D();
        System.out.println(solver.isInterleave("aabcc", "dbbca", "aadbbcbcac")); // true
        System.out.println(solver.isInterleave("aabcc", "dbbca", "aadbbbaccc")); // false
    }
}
```

------------------------------------------------------------------------

## 🧠 5. Intuition Check

At (i, j), we need to match s3[i+j-1] with either:
- s1[i-1] (coming vertically from dp[i-1][j]), or
- s2[j-1] (coming horizontally from dp[i][j-1]).

Both pointers must maintain order within their own strings; dp enforces that.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Interleaving DP on two strings
- Family: 2D DP (i, j) with concatenated index i+j in third string
- Triggers:
  - Combine two sequences preserving relative orders
  - Verify if third sequence is a merge of two subsequences

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Quick length mismatch check
- Handle empty strings correctly via base rows/cols
- Watch out for off-by-one in indices (i+j-1 for s3)

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Standard O(n*m) DP; 1D optimization reduces space to O(m).
- Think “merge two sequences while preserving internal order.”

------------------------------------------------------------------------

# End of Notes
