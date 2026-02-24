# Dynamic Programming Notes

## 28 - Longest Palindromic Substring (LPSUB)

**Generated on:** 2026-02-24 20:54:14 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s`, return the longest palindromic substring (contiguous segment) in `s`.

Key distinction:
- Palindromic Substring (contiguous) vs Palindromic Subsequence (not necessarily contiguous).
- Substring solutions reset on mismatch; subsequence solutions do not.

Examples:
- s = "babad" → "bab" or "aba"
- s = "cbbd" → "bb"

------------------------------------------------------------------------

## 🪜 2. Approaches

A) Center Expansion (recommended in interviews)
- For each index i, expand around:
  - Odd-length center: (i, i)
  - Even-length center: (i, i+1)
- Keep track of the longest span where s[l] == s[r]

B) DP on substring intervals (O(n^2) time and space)
- dp[i][j] = true if s[i..j] is palindrome
- Recurrence: s[i] == s[j] && (j - i <= 2 || dp[i+1][j-1])

C) Manacher’s Algorithm (O(n))
- Advanced; linear time, more complex to implement
- Mention as optional if interviewer asks for optimal asymptotics

------------------------------------------------------------------------

## 🔁 3. Center Expansion Recurrence

- Expand around center (L, R):
  - While L >= 0 && R < n && s[L] == s[R]: update best, L--, R++
- Repeat for all centers:
  - odd: (i, i)
  - even: (i, i+1)

Time: O(n^2) in worst-case  
Space: O(1)

------------------------------------------------------------------------

## 💻 4A. Clean Interview Version — Center Expansion

```java
class LongestPalindromicSubstringCenter {
    public String longestPalindrome(String s) {
        int n = s.length();
        if (n <= 1) return s;

        int bestStart = 0, bestLen = 1;

        for (int i = 0; i < n; i++) {
            // Odd-length center at i
            int[] odd = expand(s, i, i);
            if (odd[1] > bestLen) {
                bestStart = odd[0];
                bestLen = odd[1];
            }

            // Even-length center between i and i+1
            int[] even = expand(s, i, i + 1);
            if (even[1] > bestLen) {
                bestStart = even[0];
                bestLen = even[1];
            }
        }
        return s.substring(bestStart, bestStart + bestLen);
    }

    // returns [startIndex, length] of the palindrome
    private int[] expand(String s, int left, int right) {
        int n = s.length();
        while (left >= 0 && right < n && s.charAt(left) == s.charAt(right)) {
            left--;
            right++;
        }
        // when loop breaks, s[left+1..right-1] is palindrome
        int start = left + 1;
        int len = right - left - 1;
        return new int[]{start, len};
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(1)

------------------------------------------------------------------------

## 💻 4B. Dynamic Programming (Intervals) — Optional

```java
class LongestPalindromicSubstringDP {
    public String longestPalindrome(String s) {
        int n = s.length();
        if (n <= 1) return s;

        boolean[][] dp = new boolean[n][n];
        int bestStart = 0, bestLen = 1;

        // length 1 (single chars)
        for (int i = 0; i < n; i++) {
            dp[i][i] = true;
        }

        // length 2
        for (int i = 0; i + 1 < n; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                dp[i][i + 1] = true;
                bestStart = i;
                bestLen = 2;
            }
        }

        // length >= 3
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                if (s.charAt(i) == s.charAt(j) && dp[i + 1][j - 1]) {
                    dp[i][j] = true;
                    if (len > bestLen) {
                        bestStart = i;
                        bestLen = len;
                    }
                }
            }
        }
        return s.substring(bestStart, bestStart + bestLen);
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(n^2)

------------------------------------------------------------------------

## 💡 4C. Manacher’s Algorithm (Reference only)

- Converts to run in O(n) by transforming s and computing palindrome radii.
- Implementation is verbose; rarely necessary unless specifically asked.

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

s = "babad"

Centers at i=2:
- odd center (2,2) → expands to "bab" (len 3)
- even center (2,3) → "": mismatch
Best so far "bab". Also center near i=2/i=3 can yield "aba" of same length.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Expand Around Center (two-pointer growth)
- Family: String two-pointers with symmetry constraint
- Triggers:
  - Longest palindromic substring (contiguous)
  - Check neighbors expanding outward

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty or length-1 string → return s
- Multiple answers with same length → any is valid
- Strings with repeated characters ("aaaa") → many centers expand equally
- DP approach can be memory heavy for long inputs

------------------------------------------------------------------------

## 🔁 8. Variants

- Count palindromic substrings (expand around centers; count all valid expansions)
- Longest Palindromic Subsequence (non-contiguous → DP on subsequences)
- Palindrome partitioning (min cuts) → DP over cuts with palindrome checks

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Center expansion is simplest and optimal enough for interviews (O(n^2), O(1) space).
- DP interval method is educational but heavier on memory.
- Manacher achieves O(n) time but is intricate.

------------------------------------------------------------------------

# End of Notes
