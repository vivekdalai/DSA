# Sliding Window / Two Pointers Notes

## 08 - Minimum Window Substring (LC 76)

**Generated on:** 2026-03-23 01:04:17 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given two strings `s` and `t`, return the minimum window substring of `s` such that every character in `t` (including duplicates) is included in the window.

If no such window exists, return `""`.

Examples:
- `s = "ADOBECODEBANC", t = "ABC"` → `"BANC"`
- `s = "a", t = "a"` → `"a"`
- `s = "a", t = "aa"` → `""`

This is a classic:
- **minimum valid window** problem

------------------------------------------------------------------------

## 🪜 2. Core Idea

Maintain a sliding window `[left ... right]`.

We need:
- all required characters from `t`
- with at least the required frequency

Use:
- `need[ch]` = frequency required from `t`
- `window[ch]` = frequency currently inside the window

Track:
- `required` = number of distinct chars needed
- `formed` = number of distinct chars currently satisfied in the window

Steps:
1. Expand `right`
2. Add `s[right]` into window
3. If this character now satisfies its required count, increment `formed`
4. Once `formed == required`, try shrinking from left to minimize window
5. Update best minimum window during shrinking

------------------------------------------------------------------------

## 🔁 3. Steps

1. Build frequency map of `t`
2. Expand `right`
3. Add current char into window count
4. If requirement is satisfied for this char, update `formed`
5. While window is valid (`formed == required`):
   - update best answer
   - remove `s[left]`
   - if window becomes invalid, stop shrinking
6. Continue

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class MinimumWindowSubstring {
    public String minWindow(String s, String t) {
        if (s.length() < t.length()) return "";

        Map<Character, Integer> need = new HashMap<>();
        for (char ch : t.toCharArray()) {
            need.put(ch, need.getOrDefault(ch, 0) + 1);
        }

        Map<Character, Integer> window = new HashMap<>();
        int required = need.size();
        int formed = 0;

        int left = 0;
        int bestLen = Integer.MAX_VALUE;
        int bestStart = 0;

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            window.put(ch, window.getOrDefault(ch, 0) + 1);

            if (need.containsKey(ch) && window.get(ch).intValue() == need.get(ch).intValue()) {
                formed++;
            }

            while (formed == required) {
                if (right - left + 1 < bestLen) {
                    bestLen = right - left + 1;
                    bestStart = left;
                }

                char leftChar = s.charAt(left);
                window.put(leftChar, window.get(leftChar) - 1);

                if (need.containsKey(leftChar)
                        && window.get(leftChar).intValue() < need.get(leftChar).intValue()) {
                    formed--;
                }

                left++;
            }
        }

        return bestLen == Integer.MAX_VALUE ? "" : s.substring(bestStart, bestStart + bestLen);
    }
}
```

Complexity:
- Time: O(n)
- Space: O(charset)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`s = "ADOBECODEBANC"`, `t = "ABC"`

Need:
- A:1, B:1, C:1

As we expand:
- first valid window becomes `"ADOBEC"`
- then we shrink
- later we get smaller valid windows
- final minimum valid window = `"BANC"`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is the standard:
- **minimum window satisfying a requirement**

Recognize by phrases like:
- smallest substring containing all characters
- minimum window covering target
- shortest valid window

Difference from longest-window problems:
- longest problems:
  - shrink only when invalid
- minimum window problems:
  - once valid, shrink as much as possible

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- `t` may contain duplicates, so frequency matters
- Don’t just check presence; check counts
- Only increment `formed` when exact required count is reached
- Only decrement `formed` when frequency drops below required
- If no window exists, return empty string

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Track required counts from `t`
- Expand until window becomes valid
- Then shrink aggressively to minimize
- This is one of the most important “minimum window” interview patterns

------------------------------------------------------------------------

# End of Notes
