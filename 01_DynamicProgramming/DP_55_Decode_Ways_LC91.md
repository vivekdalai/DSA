# Dynamic Programming Notes

## 55 - Decode Ways (LC 91) — Guarded Fibonacci-Style Counting DP

**Generated on:** 2026-08-03 01:20:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

A message of digits can be decoded via the mapping `'A' -> "1", 'B' -> "2", ...,
'Z' -> "26"`. Given a digit string `s`, return the total number of ways to decode it.

Example: `s = "226"` → 3 ways: `"2,2,6"`, `"22,6"`, `"2,26"`.

The catch is `'0'`: it has no letter mapping on its own, so it's only ever valid as
the second digit of a two-digit group (`10` or `20`) — never standalone, and never
as the first digit of a group.

------------------------------------------------------------------------

## 🪜 2. State Definition

- `dp[i]` = number of ways to decode the prefix `s[0..i)` (first `i` characters).

Goal: `dp[n]` where `n = s.length()`.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

For `i >= 1`:

    oneDigit  = s[i-1]              // last single character
    twoDigit  = s[i-2..i)           // last two characters (only if i >= 2)

    dp[i] = (oneDigit != '0' ? dp[i-1] : 0)
          + (i >= 2 AND "10" <= twoDigit <= "26" ? dp[i-2] : 0)

Meaning: the last character can stand alone only if it isn't `'0'`; the last two
characters can be grouped only if they form a value in `[10, 26]`.

------------------------------------------------------------------------

## 🧱 4. Base Cases and Initialization

- `dp[0] = 1` — the empty prefix has exactly one (trivial) decoding.
- `dp[1] = s[0] != '0' ? 1 : 0` — a leading `'0'` makes the whole string undecodable.

Pitfalls:
- Skipping the `dp[0] = 1` seed makes every downstream count 0.
- Forgetting the `i >= 2` guard before reading `twoDigit` causes an index-out-of-bounds.

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

`dp[i]` only ever depends on `dp[i-1]` and `dp[i-2]` — exactly the Climbing Stairs /
Fibonacci shape, just with an extra validity guard on each term. Collapse to two
rolling variables for O(1) space.

------------------------------------------------------------------------

## 💻 6A. Top-Down (Memoized Recursion)

```java
class SolutionTopDown {
    public int numDecodings(String s) {
        Integer[] memo = new Integer[s.length() + 1];
        return solve(s, 0, memo);
    }

    private int solve(String s, int i, Integer[] memo) {
        if (i == s.length()) return 1;
        if (s.charAt(i) == '0') return 0;
        if (memo[i] != null) return memo[i];

        int ways = solve(s, i + 1, memo); // take one digit
        if (i + 2 <= s.length()) {
            int twoDigit = Integer.parseInt(s.substring(i, i + 2));
            if (twoDigit <= 26) {
                ways += solve(s, i + 2, memo); // take two digits
            }
        }
        return memo[i] = ways;
    }
}
```

Complexity: Time O(n), Space O(n) (recursion + memo)

------------------------------------------------------------------------

## 💻 6B. Bottom-Up (1D Tabulation)

```java
class SolutionTabulation {
    public int numDecodings(String s) {
        int n = s.length();
        if (n == 0 || s.charAt(0) == '0') return 0;

        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = 1;

        for (int i = 2; i <= n; i++) {
            char one = s.charAt(i - 1);
            char ten = s.charAt(i - 2);

            if (one != '0') dp[i] += dp[i - 1];

            int twoDigit = (ten - '0') * 10 + (one - '0');
            if (ten != '0' && twoDigit >= 10 && twoDigit <= 26) dp[i] += dp[i - 2];
        }
        return dp[n];
    }
}
```

Complexity: Time O(n), Space O(n)

------------------------------------------------------------------------

## 💻 6C. Space-Optimized (O(1) Rolling Variables) — Interview-Preferred

```java
class SolutionOptimized {
    public int numDecodings(String s) {
        int n = s.length();
        if (n == 0 || s.charAt(0) == '0') return 0;

        int prev2 = 1; // dp[i-2], starts as dp[0]
        int prev1 = 1; // dp[i-1], starts as dp[1]

        for (int i = 2; i <= n; i++) {
            char one = s.charAt(i - 1);
            char ten = s.charAt(i - 2);
            int curr = 0;

            if (one != '0') curr += prev1;

            int twoDigit = (ten - '0') * 10 + (one - '0');
            if (ten != '0' && twoDigit >= 10 && twoDigit <= 26) curr += prev2;

            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}
```

Complexity: Time O(n), Space O(1)

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input: `s = "226"`

- `dp[0] = 1`, `dp[1] = 1` (`'2'` valid alone)
- `i=2`: one=`'2'` (≠0) → `+dp[1]=1`; twoDigit=`"22"`=22 (10-26) → `+dp[0]=1` → `dp[2]=2`
- `i=3`: one=`'6'` (≠0) → `+dp[2]=2`; twoDigit=`"26"`=26 (10-26) → `+dp[1]=1` → `dp[3]=3`

Answer: `dp[3] = 3` → `"2,2,6"`, `"22,6"`, `"2,26"`

Zero pitfall example: `s = "100"`
- `dp[0]=1`, `dp[1]=1` (`'1'` valid)
- `i=2`: one=`'0'` → contributes 0; twoDigit=`"10"`=10 → `+dp[0]=1` → `dp[2]=1`
- `i=3`: one=`'0'` → contributes 0; twoDigit=`"00"`=0, invalid (ten=='0') → `dp[3]=0`

Answer: `dp[3] = 0` — `"100"` cannot be decoded (`'0'` can never stand alone, and
`"00"` isn't a valid two-digit group).

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Guarded Fibonacci Counting DP
- Family: same shape as [Climbing Stairs](DP_05_Climbing_Stairs_Count_Ways.md) —
  `dp[i] = dp[i-1] + dp[i-2]` — but each term is conditionally zeroed by a validity
  check on the substring itself.
- Triggers: "count ways to split/decode a string into valid 1- or 2-length pieces,"
  fixed small lookback window (here always exactly 1 or 2).

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

Edge Cases:
- Leading `'0'` (e.g. `"06"`) → 0 ways, must check before anything else
- Single character → 1 way if non-zero, else 0
- `"10"`, `"20"` → valid single two-digit group where the second digit is `0`
- Consecutive zeros (`"100"`, `"00"`) → almost always 0 ways; double-check by hand

Pitfalls:
- Treating `'0'` as usable standalone — it never is
- Reading two digits when the tens digit is `'0'` (`"0X"` is never a valid group —
  only `[10, 26]`, and anything starting with `0` is out of that range anyway, but
  it's an easy off-by-logic bug to reintroduce)
- Off-by-one in substring bounds (`i-2` vs `i-1`, inclusive/exclusive)

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Same recurrence shape as Climbing Stairs, gated by two validity checks: last digit
  non-zero (single-digit path) and last-two-digits in `[10,26]` (two-digit path).
- `'0'` is the entire difficulty of this problem — trace it explicitly in the dry run
  before coding.
- O(1) rolling-variable version is the clean interview answer.

------------------------------------------------------------------------

# End of Notes
