# Sliding Window / Two Pointers Notes

## 09 - Permutation in String (LC 567)

**Generated on:** 2026-03-23 01:04:44 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given two strings `s1` and `s2`, return true if `s2` contains a permutation of `s1`.

Equivalent interpretation:
- Is there any substring of `s2` of length `s1.length()` whose character frequencies match `s1`?

Examples:
- `s1 = "ab", s2 = "eidbaooo"` → `true`
- `s1 = "ab", s2 = "eidboaoo"` → `false`

------------------------------------------------------------------------

## 🪜 2. Core Idea

A permutation must have:
- same length
- same character counts

So:
- keep a fixed-size sliding window of length `s1.length()` on `s2`
- compare character frequencies of:
  - `s1`
  - current window of `s2`

If they match at any point, answer is true.

------------------------------------------------------------------------

## 🔁 3. Steps

1. Build frequency array for `s1`
2. Build frequency array for first window of size `s1.length()` in `s2`
3. Compare arrays
4. Slide the window:
   - add incoming char
   - remove outgoing char
5. If frequencies match at any point, return true

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class PermutationInString {
    public boolean checkInclusion(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        if (n > m) return false;

        int[] need = new int[26];
        int[] window = new int[26];

        for (int i = 0; i < n; i++) {
            need[s1.charAt(i) - 'a']++;
            window[s2.charAt(i) - 'a']++;
        }

        if (matches(need, window)) return true;

        for (int right = n; right < m; right++) {
            window[s2.charAt(right) - 'a']++;
            window[s2.charAt(right - n) - 'a']--;

            if (matches(need, window)) return true;
        }

        return false;
    }

    private boolean matches(int[] a, int[] b) {
        for (int i = 0; i < 26; i++) {
            if (a[i] != b[i]) return false;
        }
        return true;
    }
}
```

Complexity:
- Time: O(26 * n) ~ O(n)
- Space: O(1)

------------------------------------------------------------------------

## 💻 4B. Optimized Matching Count Version

Instead of comparing full arrays every time, maintain how many positions currently match.

```java
class PermutationInStringOptimized {
    public boolean checkInclusion(String s1, String s2) {
        int n = s1.length(), m = s2.length();
        if (n > m) return false;

        int[] a = new int[26];
        int[] b = new int[26];

        for (int i = 0; i < n; i++) {
            a[s1.charAt(i) - 'a']++;
            b[s2.charAt(i) - 'a']++;
        }

        int matches = 0;
        for (int i = 0; i < 26; i++) {
            if (a[i] == b[i]) matches++;
        }

        int left = 0;
        for (int right = n; right < m; right++) {
            if (matches == 26) return true;

            int in = s2.charAt(right) - 'a';
            int out = s2.charAt(left) - 'a';
            left++;

            b[in]++;
            if (b[in] == a[in]) matches++;
            else if (b[in] == a[in] + 1) matches--;

            b[out]--;
            if (b[out] == a[out]) matches++;
            else if (b[out] == a[out] - 1) matches--;
        }

        return matches == 26;
    }
}
```

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`s1 = "ab"`, `s2 = "eidbaooo"`

Need:
- a:1, b:1

Window size = 2

Windows:
- `"ei"` → no
- `"id"` → no
- `"db"` → no
- `"ba"` → yes

Answer = `true`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this pattern when:
- need to check whether some substring is an anagram/permutation of another string
- fixed-size window
- compare frequency counts

Related problems:
- Find All Anagrams in a String
- Minimum Window Substring (variable-size)
- Longest substring with character constraints

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- If `s1.length() > s2.length()`, return false immediately
- Works because letters are lowercase English in standard LC problem
- Fixed-size window is the key; don’t use variable shrinking logic here
- Frequency comparison must happen after every slide

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Permutation in string = fixed window anagram check
- Window size is fixed to `s1.length()`
- Maintain frequency arrays and compare
- One of the most frequent interview sliding-window problems

------------------------------------------------------------------------

# End of Notes
