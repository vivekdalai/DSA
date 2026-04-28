# Hash Table Notes

## 1189 - Maximum Number of Balloons

**Generated on:** 2026-04-29 01:33:12 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/maximum-number-of-balloons/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a string `text`, you want to use the characters of `text` to form as many instances of the word **"balloon"** as possible.

You can use each character in `text` **at most once**. Return the maximum number of instances that can be formed.

**Example 1:**

**![Official LeetCode diagram](https://assets.leetcode.com/uploads/2019/09/05/1536_ex1_upd.JPG)**

```text
Input: text = "nlaebolko"
Output: 1
```

**Example 2:**

**![Official LeetCode diagram](https://assets.leetcode.com/uploads/2019/09/05/1536_ex2_upd.JPG)**

```text
Input: text = "loonbalxballpoon"
Output: 2
```

**Example 3:**

```text
Input: text = "leetcode"
Output: 0
```

**Constraints:**

- `1  2287: Rearrange Characters to Make Target String.

------------------------------------------------------------------------

## 2. First Intuition

The word `balloon` needs fixed letter counts: one `b`, one `a`, two `l`s, two `o`s, and one `n`. The bottleneck letter decides how many complete words can be formed.

------------------------------------------------------------------------

## 3. Frequency Counting Idea

- The Java file counts all lowercase letters in a `26` sized array.
- It then repeatedly subtracts the letters of `balloon` until one needed letter becomes negative.
- For interviews, the same idea is usually expressed by taking the minimum among the required letter frequencies.

------------------------------------------------------------------------

## 4. Short Dry Run

For `text = "loonbalxballpoon"`: counts allow `b = 2`, `a = 2`, `l / 2 = 2`, `o / 2 = 2`, `n = 2`, so the answer is `2`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maxNumberOfBalloons(String text) {
    int[] freq = new int[26];
    for (char c : text.toCharArray()) {
        freq[c - 'a']++;
    }

    return Math.min(
        Math.min(freq['b' - 'a'], freq['a' - 'a']),
        Math.min(Math.min(freq['l' - 'a'] / 2, freq['o' - 'a'] / 2), freq['n' - 'a'])
    );
}
```

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n)`
- Space: `O(1)` because the alphabet size is fixed.

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- When a target word has repeated letters, divide those counts before taking the minimum.
- A fixed lowercase alphabet usually means `int[26]` is cleaner than a map.

------------------------------------------------------------------------

## End of Notes
