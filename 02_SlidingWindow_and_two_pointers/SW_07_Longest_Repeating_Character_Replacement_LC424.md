# Sliding Window / Two Pointers Notes

## 07 - Longest Repeating Character Replacement (LC 424)

**Generated on:** 2026-03-23 01:03:43 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s` consisting of uppercase English letters and an integer `k`, you can change at most `k` characters in the string.

Return the length of the longest substring that can be made of the same character after performing at most `k` replacements.

Examples:
- `s = "ABAB", k = 2` → `4`
- `s = "AABABBA", k = 1` → `4`

Key question:
- In a window, how many replacements are needed to make all characters equal?

Answer:
- `windowLength - maxFreqCharacterInWindow`

------------------------------------------------------------------------

## 🪜 2. Core Idea

For a window `[left ... right]`:
- let `maxFreq` = frequency of the most common character in this window
- then characters to replace =
  - `windowSize - maxFreq`

If:
- `windowSize - maxFreq <= k`
then the window is valid.

So we:
1. Expand `right`
2. Update frequency of `s[right]`
3. Track `maxFreq`
4. If replacements needed exceed `k`, shrink from left
5. Track the longest valid window

------------------------------------------------------------------------

## 🔁 3. Steps

1. Expand `right`
2. Add `s[right]` to frequency array
3. Update `maxFreq`
4. If `(right - left + 1) - maxFreq > k`, shrink left
5. Track maximum valid window size

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class LongestRepeatingCharacterReplacement {
    public int characterReplacement(String s, int k) {
        int[] freq = new int[26];
        int left = 0;
        int maxFreq = 0;
        int maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            freq[ch - 'A']++;
            maxFreq = Math.max(maxFreq, freq[ch - 'A']);

            while ((right - left + 1) - maxFreq > k) {
                freq[s.charAt(left) - 'A']--;
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
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`s = "AABABBA", k = 1`

Suppose current window is `"AABA"`:
- counts: A=3, B=1
- maxFreq = 3
- windowSize = 4
- replacements needed = 4 - 3 = 1 → valid

If window becomes `"AABABB"`:
- maxFreq maybe = 3
- windowSize = 6
- replacements = 6 - 3 = 3 → invalid
- shrink from left

Answer = `4`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this pattern when:
- longest substring/subarray
- allowed at most `k` modifications
- want to make the whole window satisfy one dominant property

Common trigger:
- make all characters equal
- replace at most k elements
- longest window after at most k edits

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- `maxFreq` is allowed to be a stale upper bound in this optimized solution
- Still works because window only shrinks when definitely invalid
- Use uppercase size 26 here because LC 424 uses uppercase letters
- Don’t recompute full max frequency every time unless using the slower clearer version

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Required replacements in a window =
  - `windowLength - maxFreq`
- If replacements exceed `k`, shrink
- This is a very important sliding window interview problem

------------------------------------------------------------------------

# End of Notes
