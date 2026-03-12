# Dynamic Programming Notes

## 29 - Minimum Insertions To Make String Palindrome

**Generated on:** 2026-02-24 20:55:17 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s`, find the minimum number of insertions required to make `s` a palindrome.  
You can insert characters at any position (commonly thought of as inserting in front/back, but any position works).

Examples:
- s = "zzazz" → 0 (already palindrome)
- s = "mbadm" → 2 ("mbdadbm" or "mdbabdm")
- s = "leetcode" → 5

Key insight:
- If we keep the longest palindromic subsequence (LPS) intact and insert characters to fix the rest,  
  the minimum insertions needed is exactly the number of characters not in the LPS.

Formula:
- minInsertions = n - LPS(s)

------------------------------------------------------------------------

## 🪜 2. Approaches

A) LCS with Reverse (Recommended)
- LPS(s) = LCS(s, reverse(s))
- minInsertions = n - LPS(s)

B) Interval DP on substrings (i..j)
- dp[i][j] = minimum insertions to make s[i..j] a palindrome
- Transition depends on s[i] == s[j]

Both are O(n^2) time. LCS approach is concise; interval DP gives direct transform cost and can reconstruct a result.

------------------------------------------------------------------------

## 🔁 3. Interval DP Recurrence

Let:
- dp[i][j] = minimum insertions to make s[i..j] a palindrome

Base:
- dp[i][i] = 0 (single char is palindrome)
- dp[i][j] = 0 for i > j (empty string)

Transition:
- If s[i] == s[j]:
  - dp[i][j] = dp[i+1][j-1]
- Else:
  - We can insert s[i] near s[j], or s[j] near s[i]:
  - dp[i][j] = 1 + min(dp[i+1][j], dp[i][j-1])

Fill by increasing length (len = 2..n).  
Answer: dp[0][n-1].

------------------------------------------------------------------------

## 💻 4A. LCS-Based Solution (Concise and Interview-Friendly)

```java
class MinInsertionsPalindromeLCS {
    public int minInsertions(String s) {
        int n = s.length();
        String r = new StringBuilder(s).reverse().toString();
        int[][] dp = new int[n + 1][n + 1];

        for (int i = 1; i <= n; i++) {
            char a = s.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                char b = r.charAt(j - 1);
                if (a == b) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        int lps = dp[n][n];
        return n - lps;
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(n^2) (can be optimized to O(n) with rolling arrays)

------------------------------------------------------------------------

## 💻 4B. Interval DP (Direct Transform)

```java
class MinInsertionsPalindromeInterval {
    public int minInsertions(String s) {
        int n = s.length();
        if (n <= 1) return 0;

        int[][] dp = new int[n][n];

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = (len == 2) ? 0 : dp[i + 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i + 1][j], dp[i][j - 1]);
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

## 🔎 5. Example Dry Run

s = "mbadm"
- LPS = "mam" or "bab" (length 3)
- n = 5 → minInsertions = 5 - 3 = 2

Interval DP yields same: 2 (insert 'd' and 'a' in appropriate places).

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: “Palindromic Transform via LPS”
- Family: String DP (LCS/LPS/Interval DP)
- Triggers:
  - “Minimum insertions to make palindrome”
  - Equivalent to preserving LPS and fixing the rest with insertions
- Related:
  - Minimum deletions to make palindrome = n - LPS(s)
  - Min insertions/deletions to convert string A → B via LCS(A, B)

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty string → 0
- Already palindrome → 0
- Strings with all identical chars → 0
- Be careful with len==2 in interval DP to avoid out-of-bounds on dp[i+1][j-1]

------------------------------------------------------------------------

## 🔁 8. Variants

- Minimum insertions + return one valid palindromic string (requires backtracking)
- Minimum deletions to make palindrome (same as insertions: n - LPS)
- Given costs for insertion/deletion/substitution → edit distance style variants

------------------------------------------------------------------------

## ✅ 9. Takeaway

- The fastest mental route: minInsertions = n - LPS(s) = n - LCS(s, reverse(s)).
- Interval DP provides direct transform counts and is just as efficient.
- Both are O(n^2) and standard for interviews.

------------------------------------------------------------------------

# End of Notes
