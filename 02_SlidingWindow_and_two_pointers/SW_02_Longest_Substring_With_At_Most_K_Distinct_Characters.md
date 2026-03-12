# Sliding Window / Two Pointers Notes

## 02 - Longest Substring with At Most K Distinct Characters

**Generated on:** 2026-03-13 02:32:24 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s` and an integer `k`, return the length of the longest substring that contains at most `k` distinct characters.

Examples:
- `s = "eceba", k = 2` → `3` (`"ece"`)
- `s = "aa", k = 1` → `2`

Why Sliding Window:
- We need the longest valid contiguous substring.
- Valid condition is:
  - number of distinct characters in current window `<= k`
- If it becomes `> k`, shrink from the left until valid again.

------------------------------------------------------------------------

## 🪜 2. Core Window Idea

Maintain a window `[left ... right]` such that:
- it contains at most `k` distinct characters

Use:
- `Map<Character, Integer>` to store frequency of characters inside the current window

Steps:
1. Expand `right`
2. Add `s[right]` to frequency map
3. If distinct count becomes greater than `k`, shrink `left`
4. Track maximum valid window length

------------------------------------------------------------------------

## 🔁 3. Recurrence / Invariant

Window invariant:
- after shrinking, map size must always satisfy:
  - `freqMap.size() <= k`

Update:
- `maxLen = max(maxLen, right - left + 1)` whenever window is valid

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

class LongestSubstringAtMostKDistinct {
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        if (k == 0 || s.length() == 0) return 0;

        Map<Character, Integer> freq = new HashMap<>();
        int left = 0;
        int maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            freq.put(ch, freq.getOrDefault(ch, 0) + 1);

            while (freq.size() > k) {
                char leftChar = s.charAt(left);
                freq.put(leftChar, freq.get(leftChar) - 1);
                if (freq.get(leftChar) == 0) {
                    freq.remove(leftChar);
                }
                left++;
            }

            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(k) in the window, O(charset) in general

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`s = "eceba", k = 2`

- `right = 0`, window = `"e"` → distinct = 1 → maxLen = 1
- `right = 1`, window = `"ec"` → distinct = 2 → maxLen = 2
- `right = 2`, window = `"ece"` → distinct = 2 → maxLen = 3
- `right = 3`, window = `"eceb"` → distinct = 3 → invalid
  - shrink from left:
    - remove `'e'`, still 3 distinct
    - remove `'c'`, now window = `"eb"`, distinct = 2
- continue
- answer = `3`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This is the standard:
- **longest subarray / substring with at most K constraint**

Recognize by phrases like:
- at most k distinct
- at most k zeros
- at most k odd numbers
- at most k replacements

Template:
- Expand right
- While invalid, shrink left
- Update answer on valid window

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- `k = 0` → answer is `0`
- Empty string → `0`
- Remove map entries when frequency becomes 0
- Don’t update answer before restoring validity

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Use a frequency map to track distinct characters in window.
- If distinct count exceeds `k`, shrink from left.
- This is one of the most important generic sliding window templates.

------------------------------------------------------------------------

# End of Notes
