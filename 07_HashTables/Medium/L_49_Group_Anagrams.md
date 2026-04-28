# Hash Table Notes

## 49 - Group Anagrams

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/group-anagrams/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given an array of strings `strs`, group the anagrams together. You can return the answer in **any order**.

**Example 1:**

**Input:** strs = ["eat","tea","tan","ate","nat","bat"]

**Output:** [["bat"],["nat","tan"],["ate","eat","tea"]]

**Explanation:**

- There is no string in strs that can be rearranged to form `"bat"`.

- The strings `"nat"` and `"tan"` are anagrams as they can be rearranged to form each other.

- The strings `"ate"`, `"eat"`, and `"tea"` are anagrams as they can be rearranged to form each other.

**Example 2:**

**Input:** strs = [""]

**Output:** [[""]]

**Example 3:**

**Input:** strs = ["a"]

**Output:** [["a"]]

**Constraints:**

- `1 <= strs.length <= 104`

- `0 <= strs[i].length <= 100`

- `strs[i]` consists of lowercase English letters.

**Additional revision examples**



**Revision Example 1**

```text
Input: strs = ["eat","tea","tan","ate","nat","bat"]
Output: [["bat"],["nat","tan"],["ate","eat","tea"]]
```

**Revision Example 2**

```text
Input: strs = [""]
Output: [[""]]
```

------------------------------------------------------------------------

## 2. First Intuition

Anagrams have the same characters in different orders. Sorting each word gives every anagram in the same group the same key.

------------------------------------------------------------------------

## 3. Canonical Sorted Key

- The file converts each string to a char array.
- It sorts the array and uses the sorted string as the map key.
- Each original word is appended to the list for that key.
- Finally, the map values are returned as the grouped answer.

------------------------------------------------------------------------

## 4. Short Dry Run

For `["eat","tea","tan","ate","nat","bat"]`: `eat`, `tea`, and `ate` all become key `aet`; `tan` and `nat` become `ant`; `bat` stays alone.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public List<List<String>> groupAnagrams(String[] strs) {
    Map<String, List<String>> groups = new HashMap<>();

    for (String s : strs) {
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        String key = new String(chars);
        groups.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
    }
    return new ArrayList<>(groups.values());
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n * k log k)`, where `k` is the maximum word length.
- Space: `O(n * k)` for grouped strings and keys.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Canonical key problems ask: how can two equivalent objects become identical?
- For lowercase-only words, a 26-count key can reduce sorting cost.

------------------------------------------------------------------------

## End of Notes
