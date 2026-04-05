# Greedy Notes

## Remove Duplicate Letters

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/remove-duplicate-letters/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a string `s`, remove duplicate letters so that every letter appears once and only once.

Among all valid results, return the smallest lexicographical one.

**Example 1**

    Input: s = "bcabc"
    Output: "abc"

**Example 2**

    Input: s = "cbacdcbc"
    Output: "acdb"

------------------------------------------------------------------------

## 2. Monotonic Stack Intuition

We want two things at once:

- every letter only once
- lexicographically smallest order

So while scanning the string, if the current character is smaller than the stack top, we would like to pop the larger top.

But we can do that only if the popped character appears again later.

That is why the last occurrence array matters.

------------------------------------------------------------------------

## 3. Greedy Rule

Precompute:

- `lastSeen[ch]` = last index where `ch` appears

Track:

- `visited[ch]` = whether `ch` is already in the answer
- a stack of chosen characters

For each character:

- if already visited, skip it
- while the stack top is larger than the current character and also appears later, pop it
- push the current character and mark it visited

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    s = "cbacdcbc"

Process early characters:

- take `c`
- `b` is smaller than `c`, and `c` appears later, so pop `c`, take `b`
- `a` is smaller than `b`, and `b` appears later, so pop `b`, take `a`
- then continue building the smallest valid unique sequence

Final answer:

    "acdb"

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static String removeDuplicateLetters(String s) {
    int[] lastSeen = new int[26];
    boolean[] visited = new boolean[26];
    Arrays.fill(lastSeen, -1);

    for (int i = s.length() - 1; i >= 0; i--) {
        if (lastSeen[s.charAt(i) - 'a'] == -1) {
            lastSeen[s.charAt(i) - 'a'] = i;
        }
    }

    Stack<Integer> st = new Stack<>();

    for (int i = 0; i < s.length(); i++) {
        int idx = s.charAt(i) - 'a';
        if (visited[idx]) continue;

        while (!st.empty()
                && s.charAt(st.peek()) > s.charAt(i)
                && lastSeen[s.charAt(st.peek()) - 'a'] > i) {
            visited[s.charAt(st.pop()) - 'a'] = false;
        }

        st.push(i);
        visited[idx] = true;
    }

    StringBuilder sb = new StringBuilder();
    while (!st.empty()) sb.append(s.charAt(st.pop()));
    return sb.reverse().toString();
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)` extra for fixed alphabet, plus stack

Pattern:

- monotonic stack
- pop only when the removed character can be recovered later

------------------------------------------------------------------------

## End of Notes
