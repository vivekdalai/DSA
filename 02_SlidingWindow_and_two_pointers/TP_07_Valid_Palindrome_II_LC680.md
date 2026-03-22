# Two Pointers Notes

## 07 - Valid Palindrome II (LC 680)

**Generated on:** 2026-03-23 01:08:20 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s`, return true if it can be a palindrome after deleting at most one character.

Examples:
- `s = "aba"` → `true`
- `s = "abca"` → `true` (remove `'c'`)
- `s = "abc"` → `false`

------------------------------------------------------------------------

## 🪜 2. Core Two-Pointer Idea

Use two pointers:
- `left` from start
- `right` from end

If characters match:
- move inward

If mismatch happens:
- we are allowed one deletion
- so check either:
  - skip left character
  - skip right character

If either remaining substring is palindrome, answer is true.

------------------------------------------------------------------------

## 🔁 3. Steps

1. Set `left = 0`, `right = n - 1`
2. Move inward while chars match
3. At first mismatch:
   - check if `s[left+1 ... right]` is palindrome
   - or if `s[left ... right-1]` is palindrome
4. If either works, return true
5. If no mismatch occurs, return true

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class ValidPalindromeII {
    public boolean validPalindrome(String s) {
        int left = 0, right = s.length() - 1;

        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) {
                return isPalindrome(s, left + 1, right) || isPalindrome(s, left, right - 1);
            }
            left++;
            right--;
        }

        return true;
    }

    private boolean isPalindrome(String s, int left, int right) {
        while (left < right) {
            if (s.charAt(left) != s.charAt(right)) return false;
            left++;
            right--;
        }
        return true;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(1)

------------------------------------------------------------------------

## 🔎 5. Dry Run Example

`s = "abca"`

- compare `a` and `a` → match
- compare `b` and `c` → mismatch

Now try:
- skip left: check `"ca"` → not palindrome
- skip right: check `"bc"`? actually substring `"b"` with right-1 side from original mismatch path gives `"bc"`? More precisely:
  - `isPalindrome(2,2)` for skipping left side after mismatch at indices 1 and 2?  
  Let's see carefully:
  - indices: 0='a', 1='b', 2='c', 3='a'
  - mismatch at 1 and 2
  - try skip left → substring `s[2..2] = "c"` → palindrome
  - try skip right → substring `s[1..1] = "b"` → palindrome

So answer = `true`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- palindrome check from both ends
- one mismatch is allowed
- need “at most one deletion” logic

Pattern:
- inward two pointers
- on first mismatch, branch once

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- At most one deletion, not multiple
- Only branch once on first mismatch
- Don’t try recursive branching for every mismatch; one helper check is enough

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Move inward while characters match
- On first mismatch, try skipping either left or right character
- If one of the remaining substrings is palindrome, answer is true

------------------------------------------------------------------------

# End of Notes
