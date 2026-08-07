# Stack Notes

## 06 - Minimum Reversals to Make a Bracket Expression Balanced

Problem: Given a string made up only of `{` and `}`, find the minimum number of bracket **reversals** (flipping a `{` to `}` or a `}` to `{`) needed to make the expression balanced. If the string length is odd, it's impossible — return `-1` (or print "invalid").

---

## 1. Problem Understanding

Example:

```text
expression = "{}{}}}{{{{"
```

Length is `10` (even), so it's potentially fixable.

First, strip out every already-balanced adjacent `{}` pair (they cancel and can never need reversal). What remains is a sequence of leftover, structurally unbalanced brackets — some run of unmatched `}` followed by a run of unmatched `{` (after cancellation, the leftovers are always of the form `}}}...{{{...`, never mixed in complex ways).

---

## 2. Core Intuition

Use a stack to cancel valid `{...}` pairs as we scan:

```text
if current is '{': push it
else (current is '}'):
    if stack top is '{': pop (they cancel — valid pair)
    else: push '}' (can't cancel right now, this is an "unbalanced closing" leftover)
```

After the scan, whatever remains on the stack is entirely unmatched — count how many are `{` (`unbalancedOpening`) and how many are `}` (`unbalancedClosing`).

---

## 3. Why This Leftover Structure Is Always `}}}...{{{...`?

Every `{` that gets pushed and never popped means no closing bracket appeared after it (within the still-open portion) to cancel it — so all leftover `{`s must be positioned after all leftover `}`s were already accounted for. This falls out naturally from how the stack processes left to right: any `}` that finds a `{` on top cancels immediately, so unmatched `}`s can only be the ones that arrived when the stack top was **not** `{` (i.e., either empty or already a `}`), meaning they occurred before any surviving unmatched `{`.

---

## 4. Reversal Counting Logic

- A pair of two leftover `}}` can be fixed with **one** reversal: flip one of them to `{`, forming `{}`.
- A pair of two leftover `{{` can similarly be fixed with **one** reversal: flip one to `}`.
- If there's an **odd** leftover `}` (or `{`) left over after pairing them up, it takes **two** reversals to fix that single one combined with the leftover odd one from the other side (flip one `}`→`{` and one `{`→`}` to form a valid pair from what would otherwise be a single mismatched leftover).

This gives the formula:

```text
minReversals = ceil(unbalancedOpening / 2) + ceil(unbalancedClosing / 2)
             = (unbalancedOpening + 1) / 2 + (unbalancedClosing + 1) / 2   // integer division
```

Note: for this formula to work out to a whole, achievable count, the **total length must be even** — this is guaranteed by checking `expression.length() % 2 == 0` up front (an odd-length expression can never be balanced, since balanced bracket strings always have even length).

---

## 5. Java Implementation

```java
import java.util.Stack;

class Solution {
    public int minReversalsToBalance(String expression) {
        if (expression.length() % 2 != 0) {
            return -1; // impossible to balance an odd-length string
        }

        Stack<Character> st = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char ch = expression.charAt(i);

            if (ch == '{') {
                st.push(ch);
            } else {
                if (!st.isEmpty() && st.peek() == '{') {
                    st.pop(); // cancel a valid pair
                } else {
                    st.push(ch); // leftover unmatched '}'
                }
            }
        }

        int unbalancedOpening = 0, unbalancedClosing = 0;
        while (!st.isEmpty()) {
            if (st.peek() == '{') {
                unbalancedOpening++;
            } else {
                unbalancedClosing++;
            }
            st.pop();
        }

        return (unbalancedOpening + 1) / 2 + (unbalancedClosing + 1) / 2;
    }
}
```

---

## 6. Dry Run

Input:

```text
expression = "{}{}}}{{{{"
```

| char | action | stack after |
|---|---|---|
| `{` | push | `{` |
| `}` | top is `{` → pop | (empty) |
| `{` | push | `{` |
| `}` | top is `{` → pop | (empty) |
| `}` | stack empty (not `{`) → push | `}` |
| `}` | top is `}` (not `{`) → push | `} }` |
| `{` | push | `} } {` |
| `{` | push | `} } { {` |
| `{` | push | `} } { { {` |
| `{` | push | `} } { { { {` |

Leftover: `unbalancedClosing = 2`, `unbalancedOpening = 4`.

```text
minReversals = (4+1)/2 + (2+1)/2 = 2 + 1 = 3
```

---

## 7. Edge Cases

- Odd-length string: always `-1` (or "invalid") — never balanceable regardless of content.
- Already balanced (e.g. `"{}{}"`): stack empties completely, `unbalancedOpening = unbalancedClosing = 0`, answer is `0`.
- All same character (e.g. `"{{{{"`, length 4): entirely leftover `{`s, `unbalancedOpening = 4`, `unbalancedClosing = 0` → `minReversals = (4+1)/2 = 2`.
- Empty string: length `0` is even, loop does nothing, both leftovers are `0`, answer `0` (an empty string is trivially balanced).
- Single unmatched leftover on each side (e.g. after cancellation, `1` leftover `}` and `1` leftover `{`): `(1+1)/2 + (1+1)/2 = 1 + 1 = 2`, matching the "two reversals for one odd leftover on each side" rule.

---

## 8. Common Mistakes

- Forgetting the odd-length check up front — without it, the formula can produce a nonsensical answer for an unfixable string.
- Using `unbalancedOpening/2 + unbalancedClosing/2` (plain integer division, no `+1`) — this under-counts when either leftover count is odd; the `+1` before dividing implements the "round up" (ceiling) needed because a single odd leftover requires two reversals, not zero extra.
- Trying to directly count `'{' `vs `'}'` occurrences in the *original* string instead of first cancelling matched pairs via the stack — raw character counts don't capture the *positional* imbalance (e.g. `"}{"}` has equal counts of each but is still unbalanced).
- Confusing this with LeetCode 921 ("Minimum Add to Make Parentheses Valid") — that problem only allows **inserting** new brackets (not flipping existing ones), which is a different operation with a different (simpler) formula (`unbalancedOpening + unbalancedClosing`, no division).

---

## 9. Complexity

```text
Time:  O(n)
Space: O(n)
```

---

## 10. Pattern Recognition

Any "minimum operations to balance a bracket sequence" problem starts the same way: **cancel matched pairs with a stack**, then reason about the leftover residue (which always has a simple, predictable shape — all closing brackets first, then all opening brackets). The specific formula changes based on what operation is allowed (insertion vs. reversal), but the cancellation step is universal.

---

## 11. Takeaway

After stripping already-valid pairs, only leftover unmatched brackets matter, and they always separate cleanly into "all leftover closers" followed by "all leftover openers." Pairing up two leftovers of the same type costs one reversal each; an odd one out from each side costs two combined — captured neatly by `(count+1)/2` on each side.

---

# End of Notes
