# Two Pointers Notes

## 06 - Valid Palindrome (LC 125)

**Generated on:** 2026-03-23 01:08:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Given a string `s`, return true if it is a palindrome after converting all uppercase letters into lowercase letters and removing all non-alphanumeric characters.

Examples:
- `s = "A man, a plan, a canal: Panama"` → `true`
- `s = "race a car"` → `false`

------------------------------------------------------------------------

## 🪜 2. Core Two-Pointer Idea

Use two pointers:
- `left` from start
- `right` from end

At each step:
1. Skip non-alphanumeric characters
2. Compare lowercase versions of valid characters
3. If mismatch → false
4. Else move both inward

------------------------------------------------------------------------

## 🔁 3. Steps

1. Set `left = 0`, `right = s.length() - 1`
2. While `left < right`:
   - skip left if not alphanumeric
   - skip right if not alphanumeric
   - compare lowercase chars
   - if mismatch, return false
   - move inward
3. Return true

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
class ValidPalindrome {
    public boolean isPalindrome(String s) {
        int left = 0, right = s.length() - 1;

        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            }

            char a = Character.toLowerCase(s.charAt(left));
            char b = Character.toLowerCase(s.charAt(right));

            if (a != b) return false;

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

`s = "A man, a plan, a canal: Panama"`

- compare `a` and `a`
- compare `m` and `m`
- compare `a` and `a`
- continue while skipping spaces/comma/colon
- all match

Answer = `true`

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

Use this when:
- need to compare from both ends
- ignore certain characters
- perform normalized comparison

This is a standard:
- **inward two pointers**

Related:
- Reverse String
- Palindrome checking variants
- Valid Palindrome II

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Empty string becomes palindrome → true
- Only non-alphanumeric chars → true
- Don’t forget case normalization
- Must skip punctuation and spaces

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Start from both ends
- Skip irrelevant chars
- Compare normalized characters
- Simple but frequently asked two-pointer problem

------------------------------------------------------------------------

# End of Notes
