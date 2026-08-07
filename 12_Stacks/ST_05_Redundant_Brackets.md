# Stack Notes

## 05 - Check if an Expression Contains Redundant Brackets

Problem: Given a valid arithmetic expression as a string (containing `+`, `-`, `*`, `/`, `(`, `)`, and operands), determine whether it contains any **redundant** parentheses — a pair of parentheses that wraps a sub-expression with no operator directly inside it at that level (e.g., `((a+b))` has a redundant outer pair around `(a+b)`).

---

## 1. Problem Understanding

Example:

```text
expression = "(((a*b))+(c+d))"
```

The innermost `(a*b)` is fine (it has an operator `*` inside). But it is wrapped by two more pairs of parentheses `((...))` that add nothing — those are redundant.

Contrast with:

```text
expression = "(a+b*(c-d))"
```

No redundant brackets — every pair of parentheses directly contains at least one operator.

---

## 2. Core Intuition

A pair of parentheses is **redundant** exactly when, scanning from its matching `(` up to its `)`, we find **no operator** (`+`, `-`, `*`, `/`) directly between them (only a single operand or a single nested parenthesized group).

Equivalently: if the character immediately after popping `(` characters from the stack (upon seeing a matching `)`) turns out to be `(` itself — i.e., nothing was popped except the `(` — then that pair wrapped nothing but a bare operand or a single sub-expression with no operator at this level.

---

## 3. Why Stack?

We need to know, at the moment we see a `)`, what was pushed since the most recent unmatched `(`. A stack naturally exposes exactly that "since the last open bracket" window via repeated `peek()`/`pop()`.

```text
push '(' and any operator onto the stack
on ')':
    if stack.peek() == '('   -> the pair is empty of operators -> REDUNDANT
    else -> pop operators until '(' is found, then pop that '(' too
```

---

## 4. Java Implementation

```java
import java.util.Stack;

class Solution {
    public boolean findRedundantBrackets(String expression) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char curr = expression.charAt(i);

            if (curr == '(' || isOperator(curr)) {
                stack.push(curr);
            } else if (curr == ')') {
                if (stack.peek() == '(') {
                    return true; // redundant: nothing but the matching '(' was between them
                }

                // pop operators until the matching '(' is found and removed
                while (stack.peek() != '(') {
                    stack.pop();
                }
                stack.pop(); // remove '('
            }
            // operands (letters/digits) are simply skipped — they aren't pushed
        }

        return false;
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
}
```

---

## 5. Dry Run

Input:

```text
expression = "(((a*b))+(c+d))"
```

Key steps:

| char | action | stack (top rightmost) |
|---|---|---|
| `(` | push | `(` |
| `(` | push | `( (` |
| `(` | push | `( ( (` |
| `a` | skip | `( ( (` |
| `*` | push | `( ( ( *` |
| `b` | skip | `( ( ( *` |
| `)` | top is `*`, not `(` → pop `*`, pop `(` | `( (` |
| `)` | top is `(` → **REDUNDANT found**, return `true` | - |

Answer: the expression **contains** a redundant pair (position of the 2nd `)`), matching the PDF's original worked example.

---

## 6. Edge Cases

- Expression with no parentheses at all (e.g. `"a+b*c"`): loop finds no `(`/`)`, returns `false` — no redundancy possible.
- Single pair with an operator (e.g. `"(a+b)"`): the `)` finds a non-`(` top (the `+`), pops correctly, no redundancy.
- Single pair with only an operand (e.g. `"(a)"`): the `)` immediately sees `(` on top → redundant, returns `true`.
- Deeply nested but all meaningful (e.g. `"(a+(b*c))"`): each `)` pops through an operator before hitting `(`, so no redundancy is flagged.
- Malformed input (unbalanced parentheses): this algorithm assumes a **valid** expression per the problem statement; calling `stack.peek()`/`pop()` on an empty stack for malformed input will throw — add a validity check first if the input isn't guaranteed well-formed.

---

## 7. Common Mistakes

- Pushing operands onto the stack — only `(` and operators need to go on the stack; pushing operands would break the `stack.peek() == '('` redundancy check.
- Forgetting to pop the matching `(` itself after popping all operators inside a non-redundant pair — leaving stale `(` characters would corrupt future redundancy checks.
- Checking `stack.isEmpty()` instead of `stack.peek() == '('` to detect redundancy — the check must specifically be "was the top exactly the unmatched `(`," not "is the stack empty" (the stack is rarely empty mid-expression).
- Not handling multi-character operands (e.g., multi-digit numbers or multi-letter variable names) — the skip-logic works fine as long as none of those characters coincide with `(`, `)`, or an operator symbol.

---

## 8. Complexity

```text
Time:  O(n)
Space: O(n)
```

Each character is pushed at most once and popped at most once.

---

## 9. Pattern Recognition

Whenever a problem needs to know "is there any operator/content between the most recent unmatched opening symbol and now," push the opening symbols (and relevant content markers) and use `peek()` immediately after a closing symbol to answer in O(1). This complements the balance-checking style seen in [[ST_06_Minimum_Reversals_To_Balance_Brackets]], but here we care about *content* between matched pairs, not just *balance*.

---

## 10. Takeaway

Redundant-bracket detection reduces to one question per closing bracket: "did anything (an operator) get pushed since the most recent open bracket?" A stack answers this instantly via `peek()`, since the most recent unmatched `(` is always exactly at the point where the "since-then" window begins.

---

# End of Notes
