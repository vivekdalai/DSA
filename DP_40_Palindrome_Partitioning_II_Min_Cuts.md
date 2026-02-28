# Dynamic Programming Notes

## 40 - Palindrome Partitioning II (Minimum Cuts)

**Generated on:** 2026-02-25 23:04:40 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s`, partition `s` such that every substring of the partition is a palindrome.  
Return the minimum number of cuts needed for a palindrome partitioning of `s`.

Example:
- s = "aab" → minimum cuts = 1 (partition: "aa" | "b")
- s = "a"   → 0
- s = "ab"  → 1 ("a" | "b")

------------------------------------------------------------------------

## 🪜 2. Key Insight

- Let `pal[i][j]` indicate whether `s[i..j]` is a palindrome.
- If we know all palindromic substrings, the problem becomes:
  - `dp[i]` = minimum cuts for prefix `s[0..i]`.
  - Transition:
    - If `pal[0][i]` is true → `dp[i] = 0` (no cut needed)
    - Else `dp[i] = min(dp[i], dp[j-1] + 1)` for all `j` in `[1..i]` such that `pal[j][i] = true`
- Precompute pal table in O(n^2).
- Compute dp in O(n^2).

Overall complexity: O(n^2) time, O(n^2) space (can optimize pal check with center expansion for O(n^2) time, O(1) extra if computed on the fly).

------------------------------------------------------------------------

## 🔁 3. Precompute Palindrome Table

- Base:
  - `pal[i][i] = true` (single char)
  - `pal[i][i+1] = (s[i] == s[i+1])` (two chars)
- For length `len` from 3..n:
  - `pal[i][j] = (s[i] == s[j]) && pal[i+1][j-1]`

Alternatively, compute with center expansions for each center (odd/even) and mark pal intervals.

------------------------------------------------------------------------

## 💻 4. Clean Java Implementation (O(n^2))

```java
import java.util.*;

public class PalindromePartitioningII {
    public int minCut(String s) {
        int n = s.length();
        if (n <= 1) return 0;

        // pal[i][j] == true if s[i..j] is palindrome
        boolean[][] pal = new boolean[n][n];

        // Precompute palindrome table
        for (int i = 0; i < n; i++) pal[i][i] = true;
        for (int i = 0; i + 1 < n; i++) pal[i][i + 1] = (s.charAt(i) == s.charAt(i + 1));
        for (int len = 3; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                pal[i][j] = (s.charAt(i) == s.charAt(j)) && pal[i + 1][j - 1];
            }
        }

        // dp[i] = min cuts for s[0..i]
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE / 4);

        for (int i = 0; i < n; i++) {
            if (pal[0][i]) {
                dp[i] = 0; // whole prefix is palindrome → no cut
            } else {
                for (int j = 1; j <= i; j++) {
                    if (pal[j][i]) {
                        dp[i] = Math.min(dp[i], dp[j - 1] + 1);
                    }
                }
            }
        }
        return dp[n - 1];
    }

    // Optional: O(n^2) center expansion pal-check
    public int minCutCenterExpand(String s) {
        int n = s.length();
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE / 4);

        // expand around each index as center
        for (int center = 0; center < n; center++) {
            // odd-length palindromes
            expand(s, center, center, dp);
            // even-length palindromes
            expand(s, center, center + 1, dp);
        }
        return dp[n - 1];
    }

    private void expand(String s, int l, int r, int[] dp) {
        int n = s.length();
        while (l >= 0 && r < n && s.charAt(l) == s.charAt(r)) {
            if (l == 0) dp[r] = 0; // entire prefix is palindrome
            else dp[r] = Math.min(dp[r], dp[l - 1] + 1);
            l--; r++;
        }
    }

    // Example
    public static void main(String[] args) {
        PalindromePartitioningII solver = new PalindromePartitioningII();
        System.out.println(solver.minCut("aab")); // 1
        System.out.println(solver.minCutCenterExpand("aab")); // 1
    }
}
```

Complexity:
- Time: O(n^2)
- Space: O(n^2) for pal table (or O(1) extra with center expansion)

------------------------------------------------------------------------

## 🧪 5. Dry Run

s = "aab", n=3  
pal:
- pal[0][0] = true (“a”)
- pal[1][1] = true (“a”)
- pal[2][2] = true (“b”)
- pal[0][1] = true (“aa”)
- pal[1][2] = false (“ab”)
- pal[0][2] = false (“aab”)

dp:
- i=0: pal[0][0] true → dp[0] = 0
- i=1: pal[0][1] true → dp[1] = 0
- i=2: pal[0][2] false:
  - j=1..2 → pal[2][2] true → dp[2] = min(INF, dp[1] + 1 = 1) = 1
Answer: 1

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Palindrome Partitioning (Minimum Cuts)
- Family: Interval/2D precomputation + 1D DP on prefixes
- Triggers:
  - Partition into palindromic substrings
  - Optimize cuts → dp on prefixes with pal table / center expands

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Entire string is palindrome → answer 0
- Single character → 0
- Be mindful of large n ~2000; O(n^2) is fine, avoid O(n^3)
- Center expansion version is often simpler to implement and space-friendly

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Precompute palindromes then compute min cuts, or use center expansion to integrate pal-check on the fly.
- Classic O(n^2) DP; interview-friendly and frequently asked.

------------------------------------------------------------------------

# End of Notes
