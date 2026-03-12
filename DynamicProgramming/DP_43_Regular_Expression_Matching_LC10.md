# Dynamic Programming Notes

## 43 - Regular Expression Matching (LC 10) — DP with '.' and '*'

**Generated on:** 2026-02-25 23:12:14 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a text string `s` and a pattern string `p`, implement regular expression matching with support for:
- `.` (dot) matches any single character,
- `*` (asterisk) matches zero or more of the preceding element.

Return true if the entire string `s` matches the pattern `p`.

Examples:
- s = "aa", p = "a" → false
- s = "aa", p = "a*" → true  (zero or more 'a')
- s = "ab", p = ".*" → true  ('.*' matches any string)
- s = "aab", p = "c*a*b" → true (c* → "", a* → "aa", then 'b')
- s = "mississippi", p = "mis*is*p*." → false

------------------------------------------------------------------------

## 🪜 2. State Definition

Let:
- `dp[i][j]` = whether `s[0..i-1]` matches `p[0..j-1]` (1-based DP table indices)

Answer:
- `dp[n][m]` where `n = s.length`, `m = p.length`

Character semantics at pattern index `j-1`:
- Normal char (a-z): must match `s[i-1]`
- Dot `.`: matches any single char `s[i-1]`
- Asterisk `*`: applies to the preceding element (pattern[j-2]); can represent zero or more occurrences of that preceding element

------------------------------------------------------------------------

## 🔁 3. Recurrence and Base Cases

Base:
- `dp[0][0] = true` (empty matches empty)
- For `j >= 2`, if `p[j-1] == '*'` then:
  - `dp[0][j] = dp[0][j-2]`  // '*' takes zero occurrence of preceding element
- Otherwise `dp[0][j] = false`

Transitions:
- If `p[j-1]` is normal char or `.`:
  - `dp[i][j] = dp[i-1][j-1] && charMatch(s[i-1], p[j-1])`
    where `charMatch(x, y) = (x == y) or (y == '.')`
- If `p[j-1] == '*'`:
  - Let `prev = p[j-2]` (the element being repeated by '*')
  - Two options:
    1) Zero occurrence of `prev`:
       - `dp[i][j] |= dp[i][j-2]`
    2) One or more occurrences:
       - If `charMatch(s[i-1], prev)` then
         - `dp[i][j] |= dp[i-1][j]`  // consume one char from s, keep '*' active

Result is the OR of applicable options.

------------------------------------------------------------------------

## 💻 4. Clean Java Implementation (Bottom-Up)

```java
import java.util.*;

public class RegexMatching {
    public boolean isMatch(String s, String p) {
        int n = s.length(), m = p.length();
        boolean[][] dp = new boolean[n + 1][m + 1];

        dp[0][0] = true;
        for (int j = 2; j <= m; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2];
            }
        }

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char pc = p.charAt(j - 1);
                if (pc == '.' || pc == s.charAt(i - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pc == '*') {
                    // zero occurrence
                    dp[i][j] = dp[i][j - 2];

                    char prev = p.charAt(j - 2);
                    if (prev == '.' || prev == s.charAt(i - 1)) {
                        // one or more occurrence
                        dp[i][j] |= dp[i - 1][j];
                    }
                } else {
                    dp[i][j] = false;
                }
            }
        }
        return dp[n][m];
    }

    // Example
    public static void main(String[] args) {
        RegexMatching solver = new RegexMatching();
        System.out.println(solver.isMatch("aa", "a"));       // false
        System.out.println(solver.isMatch("aa", "a*"));      // true
        System.out.println(solver.isMatch("ab", ".*"));      // true
        System.out.println(solver.isMatch("aab", "c*a*b"));  // true
        System.out.println(solver.isMatch("mississippi", "mis*is*p*.")); // false
    }
}
```

Complexity:
- Time: O(n * m)
- Space: O(n * m) (can reduce to O(m) with careful 1D rolling and right-to-left updates)

------------------------------------------------------------------------

## 💡 5. Top-Down (Memoized DFS)

```java
import java.util.*;

class RegexMatchingMemo {
    Boolean[][] memo;

    public boolean isMatch(String s, String p) {
        memo = new Boolean[s.length() + 1][p.length() + 1];
        return dfs(0, 0, s, p);
    }

    private boolean dfs(int i, int j, String s, String p) {
        if (memo[i][j] != null) return memo[i][j];

        // If pattern is fully consumed, s must also be fully consumed.
        if (j == p.length()) {
            return memo[i][j] = (i == s.length());
        }

        boolean firstMatch = (i < s.length() &&
                (p.charAt(j) == s.charAt(i) || p.charAt(j) == '.'));

        boolean ans;
        // If next pattern char is '*', we have two choices
        if (j + 1 < p.length() && p.charAt(j + 1) == '*') {
            // zero occurrence OR one occurrence (if first matches)
            ans = dfs(i, j + 2, s, p) || (firstMatch && dfs(i + 1, j, s, p));
        } else {
            ans = firstMatch && dfs(i + 1, j + 1, s, p);
        }
        return memo[i][j] = ans;
    }
}
```

Complexity:
- Time: O(n * m) states, each processed once
- Space: O(n * m) memo + recursion stack

------------------------------------------------------------------------

## 🧪 6. Edge Cases

- Empty pattern vs empty string → true
- Patterns like "a*b*c*" can match empty string
- Ensure `'*'` is always used with a valid preceding element (as per LC10 input guarantees)
- Long repeated chars test performance of dp logic

------------------------------------------------------------------------

## 🏷 7. Pattern Recognition

- Name: Regular Expression Matching (with '.' and '*')
- Family: 2D DP on (i, j) with special handling for Kleene star
- Triggers:
  - Matching semantics depend on previous token (`*` applies to previous element)
  - Full string match required (use anchors implicitly)

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Use `dp[i][j]` (or memoized dfs) with clear handling of `*`:
  - Zero occurrence: `dp[i][j-2]`
  - One or more: if match, `dp[i-1][j]`
- Dot `.` is a single-character wildcard.
- O(n*m) solution is standard and interview-acceptable.

------------------------------------------------------------------------

# End of Notes
