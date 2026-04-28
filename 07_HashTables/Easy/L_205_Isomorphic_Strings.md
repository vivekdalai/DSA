# Hash Table Notes

## 205 - Isomorphic Strings

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/isomorphic-strings/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given two strings `s` and `t`, determine if they are isomorphic.

Two strings `s` and `t` are isomorphic if the characters in `s` can be replaced to get `t`.

All occurrences of a character must be replaced with another character while preserving the order of characters. No two characters may map to the same character, but a character may map to itself.

**Example 1:**

**Input:** s = "egg", t = "add"

**Output:** true

**Explanation:**

The strings `s` and `t` can be made identical by:

- Mapping `'e'` to `'a'`.

- Mapping `'g'` to `'d'`.

**Example 2:**

**Input:** s = "f11", t = "b23"

**Output:** false

**Explanation:**

The strings `s` and `t` can not be made identical as `'1'` needs to be mapped to both `'2'` and `'3'`.

**Example 3:**

**Input:** s = "paper", t = "title"

**Output:** true

**Constraints:**

- `1 <= s.length <= 5 * 104`

- `t.length == s.length`

- `s` and `t` consist of any valid ascii character.

**Additional revision examples**



**Revision Example 1**

```text
Input: s = "egg", t = "add"
Output: true
```

**Revision Example 2**

```text
Input: s = "foo", t = "bar"
Output: false
```

------------------------------------------------------------------------

## 2. First Intuition

Every character in `s` must consistently map to one character in `t`, and two different characters in `s` cannot collapse into the same character in `t`. That makes this a bijection check.

------------------------------------------------------------------------

## 3. Two-Way Character Mapping

- The file calls `helper(s, t)` and `helper(t, s)`.
- Each helper verifies one direction of the mapping using an ASCII array.
- Running both directions prevents cases like `ab -> aa`, where forward consistency alone is not enough.

------------------------------------------------------------------------

## 4. Short Dry Run

`s = "egg"`, `t = "add"`: `e -> a` and `g -> d` stay consistent both ways, so return `true`. For `foo` and `bar`, `o` would need to map to both `a` and `r`, so return `false`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean isIsomorphic(String s, String t) {
    int[] st = new int[256];
    int[] ts = new int[256];
    Arrays.fill(st, -1);
    Arrays.fill(ts, -1);

    for (int i = 0; i < s.length(); i++) {
        char a = s.charAt(i);
        char b = t.charAt(i);
        if (st[a] == -1 && ts[b] == -1) {
            st[a] = b;
            ts[b] = a;
        } else if (st[a] != b || ts[b] != a) {
            return false;
        }
    }
    return true;
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)` for fixed ASCII/extended ASCII arrays.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- Mapping problems often need both directions.
- If two source characters cannot map to the same target character, think bijection.

------------------------------------------------------------------------

## End of Notes
