# Greedy Notes

## Minimum Add to Make Parentheses Valid

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given a parentheses string `s`, return the minimum number of parentheses that must be added to make it valid.

A valid parentheses string is one where every opening bracket is matched with a closing bracket in the correct order.

**Example 1**

    Input: s = "())"
    Output: 1

**Example 2**

    Input: s = "((("
    Output: 3

------------------------------------------------------------------------

## 2. Balance Intuition

Keep track of how many unmatched `'('` we currently have.

- `cnt` = open parentheses waiting to be closed
- `ans` = extra `'('` we had to add when a `')'` arrived too early

For every `')'`:

- if we have an open bracket, match it
- otherwise we must add a new `'('`

After the scan, any remaining unmatched `'('` need closing brackets.

------------------------------------------------------------------------

## 3. One Pass Rule

For each character:

- if `'('`, increment `cnt`
- else if `cnt > 0`, decrement `cnt`
- else increment `ans`

Final answer:

    ans + cnt

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    s = "())"

Process:

- `'('` -> `cnt = 1`
- `')'` -> match it, `cnt = 0`
- `')'` -> no open bracket available, so `ans = 1`

Final:

    ans + cnt = 1 + 0 = 1

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int minAddToMakeValid(String s) {
    int cnt = 0, ans = 0;

    for (char c : s.toCharArray()) {
        if (c == '(') {
            cnt++;
        } else {
            if (cnt > 0) cnt--;
            else ans++;
        }
    }

    return ans + cnt;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- running balance
- count unmatched closes immediately, unmatched opens at the end

------------------------------------------------------------------------

## End of Notes
