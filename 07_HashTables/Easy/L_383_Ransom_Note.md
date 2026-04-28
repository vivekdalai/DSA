# Hash Table Notes

## 383 - Ransom Note

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/ransom-note/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given two strings `ransomNote` and `magazine`, return `true` if `ransomNote` can be constructed by using the letters from `magazine` and `false` otherwise.

Each letter in `magazine` can only be used once in `ransomNote`.

**Example 1:**

```text
Input: ransomNote = "a", magazine = "b"
Output: false
```

**Example 2:**

```text
Input: ransomNote = "aa", magazine = "ab"
Output: false
```

**Example 3:**

```text
Input: ransomNote = "aa", magazine = "aab"
Output: true
```

**Constraints:**

- `1 <= ransomNote.length, magazine.length <= 105`

- `ransomNote` and `magazine` consist of lowercase English letters.

------------------------------------------------------------------------

## 2. First Intuition

The magazine is a limited supply of characters. Every character used by the ransom note consumes one unit from that supply.

------------------------------------------------------------------------

## 3. Magazine Letter Inventory

- The file builds letter counts from `magazine`.
- It then walks through `ransomNote` and fails as soon as the requested count is more than the available count.
- A clean version can use one array and decrement directly.

------------------------------------------------------------------------

## 4. Short Dry Run

For `ransomNote = "aa"`, `magazine = "aab"`: magazine count for `a` starts at `2`; consuming two `a`s leaves `0`, so the note can be constructed.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean canConstruct(String ransomNote, String magazine) {
    int[] freq = new int[26];
    for (char c : magazine.toCharArray()) {
        freq[c - 'a']++;
    }

    for (char c : ransomNote.toCharArray()) {
        if (--freq[c - 'a'] < 0) {
            return false;
        }
    }
    return true;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(m + n)`
- Space: `O(1)` for 26 lowercase letters.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Inventory problems are usually frequency arrays.
- Decrement while consuming; a negative count means demand exceeded supply.

------------------------------------------------------------------------

## End of Notes
