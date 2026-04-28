# Two Pointers and Sliding Window Notes

## 3 - Longest Substring Without Repeating Characters

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/longest-substring-without-repeating-characters/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a string `s`, find the length of the **longest** **substring** without duplicate characters.

**Example 1:**

```text
Input: s = "abcabcbb"
Output: 3
Explanation: The answer is "abc", with the length of 3. Note that "bca" and "cab" are also correct answers.
```

**Example 2:**

```text
Input: s = "bbbbb"
Output: 1
Explanation: The answer is "b", with the length of 1.
```

**Example 3:**

```text
Input: s = "pwwkew"
Output: 3
Explanation: The answer is "wke", with the length of 3.
Notice that the answer must be a substring, "pwke" is a subsequence and not a substring.
```

**Constraints:**

- `0 <= s.length <= 5 * 104`

- `s` consists of English letters, digits, symbols and spaces.

------------------------------------------------------------------------

## 2. First Intuition

Keep a window with no duplicate characters. If the next character is new, expand; if it already exists, shrink from the left until it can fit.

------------------------------------------------------------------------

## 3. Variable Window with a Set

- The file has a first version that checks uniqueness through a frequency map.
- The cleaner `lengthOfLongestSubstring_v2` uses a `HashSet` for the current window.
- When `s[r]` is absent, add it and update the answer.
- When `s[r]` is already present, remove `s[l]` and move `l`.

------------------------------------------------------------------------

## 4. Short Dry Run

For `s = "abcabcbb"`: the window grows to `abc` of length `3`. When the next `a` appears, the left side shrinks until `a` is removed. No longer unique window exceeds length `3`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int lengthOfLongestSubstring(String s) {
    Set<Character> window = new HashSet<>();
    int left = 0;
    int best = 0;

    for (int right = 0; right < s.length(); ) {
        char c = s.charAt(right);
        if (!window.contains(c)) {
            window.add(c);
            best = Math.max(best, right - left + 1);
            right++;
        } else {
            window.remove(s.charAt(left++));
        }
    }
    return best;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(min(n, alphabetSize))`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Longest valid substring: expand when valid, shrink when invalid.
- A set is enough when the constraint is no duplicates.

------------------------------------------------------------------------

## End of Notes
