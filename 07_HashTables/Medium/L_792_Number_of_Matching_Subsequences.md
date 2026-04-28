# Hash Table Notes

## 792 - Number of Matching Subsequences

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/number-of-matching-subsequences/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a string `s` and an array of strings `words`, return the number of `words[i]` that is a subsequence of `s`.

A **subsequence** of a string is a new string generated from the original string with some characters (can be none) deleted without changing the relative order of the remaining characters.

- For example, `"ace"` is a subsequence of `"abcde"`.

**Example 1:**

```text
Input: s = "abcde", words = ["a","bb","acd","ace"]
Output: 3
Explanation: There are three strings in words that are a subsequence of s: "a", "acd", "ace".
```

**Example 2:**

```text
Input: s = "dsahjpjauf", words = ["ahjpjau","ja","ahbwzgqnuk","tnmlanowax"]
Output: 2
```

**Constraints:**

- `1 <= s.length <= 5 * 104`

- `1 <= words.length <= 5000`

- `1 <= words[i].length <= 50`

- `s` and `words[i]` consist of only lowercase English letters.

------------------------------------------------------------------------

## 2. First Intuition

To test whether a word is a subsequence, each next character must appear after the previous matched index. Pre-indexing `s` lets each word jump directly to the next valid occurrence.

------------------------------------------------------------------------

## 3. Index Lists Plus Binary Search

- The file builds `character -> sorted list of indices` from `s`.
- For each word, it tracks the last chosen index, initially `-1`.
- For every character, it binary-searches the first stored index greater than the current index.
- If every character finds a next index, the word is a matching subsequence.

------------------------------------------------------------------------

## 4. Short Dry Run

For `s = "abcde"` and word `"ace"`: choose `a` at `0`, `c` at `2`, and `e` at `4`, so it matches. Word `"bb"` fails because there is no second `b` after index `1`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int numMatchingSubseq(String s, String[] words) {
    Map<Character, List<Integer>> positions = new HashMap<>();
    for (int i = 0; i < s.length(); i++) {
        positions.computeIfAbsent(s.charAt(i), k -> new ArrayList<>()).add(i);
    }

    int count = 0;
    for (String word : words) {
        int prev = -1;
        boolean ok = true;
        for (char c : word.toCharArray()) {
            List<Integer> list = positions.get(c);
            if (list == null) {
                ok = false;
                break;
            }
            int next = firstGreaterThan(list, prev);
            if (next == -1) {
                ok = false;
                break;
            }
            prev = next;
        }
        if (ok) count++;
    }
    return count;
}

private int firstGreaterThan(List<Integer> list, int target) {
    int l = 0, r = list.size() - 1, ans = -1;
    while (l <= r) {
        int mid = l + (r - l) / 2;
        if (list.get(mid) > target) {
            ans = list.get(mid);
            r = mid - 1;
        } else {
            l = mid + 1;
        }
    }
    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(|s| + totalWordLength * log |s|)`
- Space: `O(|s|)` for index lists.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Preprocess once when many queries ask about the same string.
- Subsequence checks often become lower-bound searches over positions.

------------------------------------------------------------------------

## End of Notes
