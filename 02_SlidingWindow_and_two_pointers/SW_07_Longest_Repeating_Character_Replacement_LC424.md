# Sliding Window / Two Pointers Notes

## 07 - Longest Repeating Character Replacement (LC 424) | IMPORTANT

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

## 🧠 7. Why We Do NOT Recompute `maxFreq` While Shrinking

A very common doubt in this problem is:

Inside this loop:

```java
while ((right - left + 1) - maxFreq > k) {
    freq[s.charAt(left) - 'A']--;
    left++;
}
```

we reduce the window, but we do **not** recompute `maxFreq`.

So why does the solution still work?

### Key idea

`maxFreq` is used as a **historical upper bound** on the highest frequency seen in the current-or-earlier valid window.

It may become stale after shrinking, meaning:
- actual max frequency in current window may be smaller
- but `maxFreq` still stores an older, larger value

Even then, the algorithm is still correct.

### Why stale `maxFreq` does not break correctness

We are solving for:
- the **maximum window length**

We are **not** asked to return the exact valid window contents.

If `maxFreq` is a little too large:
- `(windowSize - maxFreq)` becomes a little smaller than the real required replacements
- so we may temporarily keep a window that looks valid even though the actual current window would need more than `k` replacements

But that is okay, because:
- this does **not** create a wrong larger answer out of nowhere
- the only reason such a large `maxFreq` existed is that earlier, some window really had that many repeated characters
- so the window length we are allowing has already been justified by an earlier valid configuration

In other words:
- stale `maxFreq` may delay shrinking
- but it never causes us to miss the correct answer
- and it never invents a longer answer that was impossible to achieve

### Intuition

Think of `maxFreq` as:
- "the best dominant frequency we have seen so far"

It is safe to use this for deciding when to shrink because the algorithm only cares about the largest achievable window length, not the exact best window at every moment.

### Example

Take:

`s = "AABABBA", k = 1`

At some point we may have had a window like:
- `"AABA"`
- frequencies: A = 3, B = 1
- so `maxFreq = 3`

Now suppose later the window shifts and becomes:
- `"ABBA"`

Actual frequencies now:
- A = 2, B = 2
- actual max frequency = 2

But stored `maxFreq` may still be `3`.

Then:
- actual replacements needed = `4 - 2 = 2`
- but computed using stale value = `4 - 3 = 1`

So the algorithm may temporarily think this window is valid.

Why is this still okay?
- Because length `4` was already achievable earlier by a truly valid window like `"AABA"`
- So keeping this stale window does not falsely increase the final answer beyond what is actually possible

That is why the final answer remains correct.

### If we recompute `maxFreq` every time?

That also works, but:
- recomputing max frequency after every shrink costs extra work
- with 26 uppercase letters, recomputation is still acceptable
- but the stale-`maxFreq` optimization is cleaner and more elegant

So interview-wise:
- stale `maxFreq` version is the standard optimal approach

------------------------------------------------------------------------

## 🔄 8. Edge Cases and Pitfalls

- `maxFreq` is allowed to be a stale upper bound in this optimized solution
- It may delay shrinking, but it does not affect the correctness of the final maximum length
- Use uppercase size 26 here because LC 424 uses uppercase letters
- Don’t recompute full max frequency every time unless using the slower clearer version

------------------------------------------------------------------------

## ✅ 9. Takeaway

- Required replacements in a window =
  - `windowLength - maxFreq`
- If replacements exceed `k`, shrink
- This is a very important sliding window interview problem

------------------------------------------------------------------------

# End of Notes
