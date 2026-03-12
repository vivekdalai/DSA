# Sliding Window / Two Pointers Notes

## 01 - Longest Substring Without Repeating Characters (LC 3)

**Generated on:** 2026-03-13 02:06:32 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s`, return the length of the longest substring without repeating characters.

Important:
- Substring means contiguous.
- We need the maximum window where all characters are unique.

Examples:
- `s = "abcabcbb"` → `3` (`"abc"`)
- `s = "bbbbb"` → `1` (`"b"`)
- `s = "pwwkew"` → `3` (`"wke"`)

Why Sliding Window:
- We need the longest valid contiguous segment.
- If a duplicate appears, we shrink the left side until the window becomes valid again.

------------------------------------------------------------------------

## 🪜 2. Core Sliding Window Idea

Maintain a window `[left ... right]` such that:
- all characters inside the window are unique

As we move `right`:
- include `s[right]`
- if it creates a duplicate, move `left` forward until the duplicate is removed

Track:
- `left` = start of current valid window
- `right` = end of current window
- `lastSeen[ch]` or `freq[ch]` to know if a character is already in the window

------------------------------------------------------------------------

## 🔁 3. Two Common Approaches

### A) Frequency / Set based shrinking
- Expand right
- If duplicate exists, keep shrinking left until valid

### B) Last seen index optimization
- Store the last index where each character was seen
- If current character was seen inside current window, jump `left` directly:
  - `left = max(left, lastSeen[ch] + 1)`

Approach B is the cleanest and most optimal.

------------------------------------------------------------------------

## 💻 4A. Optimized Java Solution using Last Seen Index

```java
import java.util.*;

class LongestSubstringNoRepeat {
    public int lengthOfLongestSubstring(String s) {
        int[] lastSeen = new int[256];
        Arrays.fill(lastSeen, -1); //IMP -> else first occurrence will not work

        int left = 0;
        int maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);

            // if character already appeared inside current window,
            // move left just after its previous occurrence
            if (lastSeen[ch] >= left) {
                left = lastSeen[ch] + 1;
            }

            lastSeen[ch] = right;
            maxLen = Math.max(maxLen, right - left + 1);
        }

        return maxLen;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1) for fixed character set

------------------------------------------------------------------------

## 💻 4B. Java Solution using Frequency Array + Shrinking

```java
class LongestSubstringNoRepeatFreq {
    public int lengthOfLongestSubstring(String s) {
        int[] freq = new int[256];
        int left = 0;
        int maxLen = 0;

        for (int right = 0; right < s.length(); right++) {
            char ch = s.charAt(right);
            freq[ch]++;

            while (freq[ch] > 1) {
                freq[s.charAt(left)]--;
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

`s = "abcabcbb"`

Using lastSeen approach:

- `right = 0`, `'a'`
  - not seen in current window
  - window = `"a"`, maxLen = 1

- `right = 1`, `'b'`
  - window = `"ab"`, maxLen = 2

- `right = 2`, `'c'`
  - window = `"abc"`, maxLen = 3

- `right = 3`, `'a'`
  - `'a'` was last seen at 0, inside current window
  - move `left = 0 + 1 = 1`
  - new window = `"bca"`, length = 3

- `right = 4`, `'b'`
  - last seen at 1, inside current window
  - move `left = 2`
  - window = `"cab"`, length = 3

Answer = `3`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

This pattern appears when:
- problem asks for **longest/maximum substring/subarray**
- condition must hold inside a contiguous window
- when invalid, we can restore validity by shrinking from left

Typical trigger words:
- longest substring
- without repeating
- at most K distinct
- maximum consecutive ones after flips
- fruit into baskets

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty string → answer `0`
- Single character → answer `1`
- Don’t move `left` backwards:
  - always do `left = max(left, lastSeen[ch] + 1)`
- If using array indexing by char, ensure charset size is enough:
  - 256 for ASCII
  - for full Unicode, use `HashMap<Character, Integer>`

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Maintain a valid window with unique characters.
- If duplicate appears, shrink window or jump `left` using last seen index.
- Best template:
  - `if (lastSeen[ch] >= left) left = lastSeen[ch] + 1`
  - update answer with `right - left + 1`

------------------------------------------------------------------------

# End of Notes
