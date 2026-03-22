# Sliding Window / Two Pointers Notes

## 10 - Find All Anagrams in a String (LC 438)

**Generated on:** 2026-03-23 01:05:11 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given two strings `s` and `p`, return all start indices of `p`'s anagrams in `s`.

In other words:
- find every substring of `s` of length `p.length()` whose character frequencies match `p`

Examples:
- `s = "cbaebabacd", p = "abc"` → `[0, 6]`
- `s = "abab", p = "ab"` → `[0, 1, 2]`

This is almost the same as:
- Permutation in String (LC 567)

Difference:
- LC 567 asks whether at least one exists
- LC 438 asks to return all matching start indices

------------------------------------------------------------------------

## 🪜 2. Core Idea

Use a fixed-size sliding window of length `p.length()`.

Maintain:
- frequency array for `p`
- frequency array for current window in `s`

At each step:
1. Compare frequencies
2. If they match, store the left index
3. Slide by one step:
   - add incoming char
   - remove outgoing char

------------------------------------------------------------------------

## 🔁 3. Steps

1. If `p.length() > s.length()`, return empty list
2. Build frequency arrays for:
   - `p`
   - first window of `s`
3. If first window matches, add index `0`
4. Slide the window one character at a time
5. On each slide:
   - increment incoming character count
   - decrement outgoing character count
   - if arrays match, record start index

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class FindAllAnagramsInString {
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> result = new ArrayList<>();
        int n = s.length(), m = p.length();

        if (m > n) return result;

        int[] need = new int[26];
        int[] window = new int[26];

        for (int i = 0; i < m; i++) {
            need[p.charAt(i) - 'a']++;
            window[s.charAt(i) - 'a']++;
        }

        if (matches(need, window)) result.add(0);

        for (int right = m; right < n; right++) {
            window[s.charAt(right) - 'a']++;
            window[s.charAt(right - m) - 'a']--;

            if (matches(need, window)) {
                result.add(right - m + 1);
            }
        }

        return result;
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

## 🔎 5. Dry Run Example

`s = "cbaebabacd"`, `p = "abc"`

Window size = 3

Windows:
- `"cba"` → anagram → add `0`
- `"bae"` → no
- `"aeb"` → no
- ...
- `"bac"` → anagram → add `6`

Answer = `[0, 6]`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- fixed-size substring
- need all anagram/permutation matches
- frequency counts define validity

Related:
- Permutation in String (same pattern, boolean answer)
- Longest substring problems (variable window)
- Minimum Window Substring (variable window with requirements)

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- If `p.length() > s.length()`, return empty list
- Use fixed-size window only
- Don’t forget to check the very first window
- For lowercase English letters, 26-size arrays are enough

------------------------------------------------------------------------

## ✅ 8. Takeaway

- This is the “return all matching fixed windows” version of LC 567
- Window size is fixed to `p.length()`
- Maintain frequency arrays and collect indices when matched

------------------------------------------------------------------------

# End of Notes
