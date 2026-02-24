# Dynamic Programming Notes

## 31 - Wildcard Matching (Pattern with '?' and '*')

**Generated on:** 2026-02-24 20:58:01 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given:
- A source string `s` (length N)
- A pattern string `p` (length M) containing:
  - `?` → matches exactly one character
  - `*` → matches any sequence of characters (including empty)
  - other characters match themselves

Return whether `p` matches `s` entirely.

Examples:
- s = "aa", p = "a" → false  
- s = "aa", p = "*" → true  
- s = "cb", p = "?a" → false  
- s = "adceb", p = "*a*b" → true

------------------------------------------------------------------------

## 🪜 2. State Definition

Use prefix-based DP:

- `dp[i][j]` = whether `s[0..i-1]` matches `p[0..j-1]` (1-based DP table)

Answer:
- `dp[N][M]`

Character semantics:
- If `p[j-1]` is a normal char or `?`:
  - it consumes exactly one character from `s`
- If `p[j-1]` is `*`:
  - it consumes zero or more characters from `s`

------------------------------------------------------------------------

## 🔁 3. Recurrence and Base Cases

Base:
- `dp[0][0] = true` (empty matches empty)
- `dp[i][0] = false` for `i > 0` (non-empty s cannot match empty p)
- `dp[0][j] = true` if and only if `p[0..j-1]` are all `*` (i.e., `'*'` can match empty)

Transition:
- If `p[j-1] == s[i-1]` or `p[j-1] == '?'`
  - `dp[i][j] = dp[i-1][j-1]`
- Else if `p[j-1] == '*'`
  - `dp[i][j] = dp[i][j-1]` (use `*` as empty)
    OR
  - `dp[i][j] = dp[i-1][j]` (use `*` to consume one char from s)
- Else
  - `dp[i][j] = false`

Time: O(N·M)  
Space: O(N·M) (can optimize to O(M) with 1D dp)

------------------------------------------------------------------------

## 💻 4A. Recursive + Memoization (Top-Down)

```java
import java.util.*;

class WildcardMemo {
    private Boolean[][] memo;

    public boolean isMatch(String s, String p) {
        int n = s.length(), m = p.length();
        memo = new Boolean[n + 1][m + 1];
        return dfs(s, p, n, m);
    }

    private boolean dfs(String s, String p, int i, int j) {
        // i -> length of s prefix considered, j -> length of p prefix considered
        if (i == 0 && j == 0) return true;
        if (j == 0) return false;

        if (memo[i][j] != null) return memo[i][j];

        char pj = p.charAt(j - 1);

        if (i == 0) {
            // s is empty -> p must be all '*'
            return memo[i][j] = isAllStars(p, j);
        }

        if (pj == s.charAt(i - 1) || pj == '?') {
            return memo[i][j] = dfs(s, p, i - 1, j - 1);
        } else if (pj == '*') {
            // '*' as empty: dfs(i, j-1)
            // '*' consuming one char: dfs(i-1, j)
            return memo[i][j] = dfs(s, p, i, j - 1) || dfs(s, p, i - 1, j);
        } else {
            return memo[i][j] = false;
        }
    }

    private boolean isAllStars(String p, int upto) {
        for (int k = 0; k < upto; k++) {
            if (p.charAt(k) != '*') return false;
        }
        return true;
    }
}
```

Complexity:
- Time: O(N·M)
- Space: O(N·M) for memo + O(N+M) recursion stack

------------------------------------------------------------------------

## 💻 4B. Bottom-Up Tabulation (1-based)

```java
class WildcardTab {
    public boolean isMatch(String s, String p) {
        int n = s.length(), m = p.length();
        boolean[][] dp = new boolean[n + 1][m + 1];

        dp[0][0] = true;

        // dp[0][j] -> s is empty; p must be all '*'
        for (int j = 1; j <= m; j++) {
            if (p.charAt(j - 1) == '*') dp[0][j] = dp[0][j - 1];
        }

        for (int i = 1; i <= n; i++) {
            char sc = s.charAt(i - 1);
            for (int j = 1; j <= m; j++) {
                char pc = p.charAt(j - 1);

                if (pc == sc || pc == '?') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pc == '*') {
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                } else {
                    dp[i][j] = false;
                }
            }
        }
        return dp[n][m];
    }
}
```

Complexity:
- Time: O(N·M)
- Space: O(N·M)

------------------------------------------------------------------------

## 💻 4C. Space-Optimized (O(M))

```java
class WildcardSpace {
    public boolean isMatch(String s, String p) {
        int n = s.length(), m = p.length();

        boolean[] prev = new boolean[m + 1];
        prev[0] = true;
        for (int j = 1; j <= m; j++) {
            if (p.charAt(j - 1) == '*') prev[j] = prev[j - 1];
        }

        for (int i = 1; i <= n; i++) {
            boolean[] curr = new boolean[m + 1];
            // curr[0] = false (non-empty s cannot match empty p)
            for (int j = 1; j <= m; j++) {
                char pc = p.charAt(j - 1);
                char sc = s.charAt(i - 1);
                if (pc == sc || pc == '?') {
                    curr[j] = prev[j - 1];
                } else if (pc == '*') {
                    curr[j] = curr[j - 1] || prev[j];
                } else {
                    curr[j] = false;
                }
            }
            prev = curr;
        }
        return prev[m];
    }
}
```

------------------------------------------------------------------------

## 💡 4D. Alternative Greedy (Two Pointers + Backtracking to Last '*') — O(N+M)

In practice, a well-known greedy works:
- Iterate `s` and `p` with pointers `i`, `j`
- Track the last seen `*` position in `p` (say `star = -1`) and a `match` index in `s`
- If characters match or `?`, advance both
- If `*` in `p`, record `star = j`, `match = i`, advance `j`
- Else if mismatch and `star != -1`, backtrack: set `j = star + 1`, `i = ++match`
- Else fail

```java
class WildcardGreedy {
    public boolean isMatch(String s, String p) {
        int i = 0, j = 0, match = 0, star = -1;
        int n = s.length(), m = p.length();

        while (i < n) {
            if (j < m && (p.charAt(j) == '?' || p.charAt(j) == s.charAt(i))) {
                i++; j++; // match one
            } else if (j < m && p.charAt(j) == '*') {
                star = j++;
                match = i; // record position in s where '*' started to match
            } else if (star != -1) {
                j = star + 1;    // backtrack in pattern to char after '*'
                i = ++match;     // extend '*' to cover one more char of s
            } else {
                return false;
            }
        }
        // remaining pattern must be all '*'
        while (j < m && p.charAt(j) == '*') j++;
        return j == m;
    }
}
```

This is elegant and very efficient for production use.

------------------------------------------------------------------------

## 🔎 5. Edge Cases and Pitfalls

- Leading/trailing sequences of `*` are common corner cases; ensure dp[0][j] initialization is correct.
- Multiple `*` in a row behave like a single `*` (both dp and greedy handle this inherently).
- Empty `s` with non-empty `p`: only matches if `p` is all `*`.
- Very long inputs: prefer greedy for performance and memory.

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Name: Wildcard Matching (DP on two strings with special tokens)
- Family: 2D DP (edit/compare style)
- Triggers:
  - Pattern contains `?` and `*` with exact/any-length semantics
  - Need full string match (not substring)

------------------------------------------------------------------------

## ✅ 7. Takeaway

- Canonical DP:
  - `?` → diagonal (consume 1 from both)
  - `*` → left (empty) OR up (consume 1 from s)
- Base case: dp[0][j] depends on whether `p[0..j-1]` is all `*`.
- Greedy two-pointer with star-backtracking is an O(N+M) alternative that’s great in practice.

------------------------------------------------------------------------

# End of Notes
