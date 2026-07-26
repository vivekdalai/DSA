# Monotonic Stack Notes

## 01 - Remove K Digits (LC 402)

Problem: Given a non-negative integer `num` as a string and an integer `k`, remove exactly `k` digits so that the remaining number is as small as possible.

Return the result as a string.

---

## 1. Problem Understanding

We need to remove exactly `k` digits.

The relative order of the remaining digits must stay the same.

Example:

```text
num = "1432219", k = 3
answer = "1219"
```

One optimal removal:

```text
1432219
 remove 4, 3, 2
result = 1219
```

The goal is not just to remove large digits.

The goal is to make the earliest possible digits as small as possible, because earlier digits have higher place value.

---

## 2. Core Intuition

To make a number small, we want smaller digits toward the left.

So while scanning digits from left to right:

- if the current digit is smaller than the previous chosen digit
- and we still have removals left
- then removing the previous larger digit improves the answer

Example:

```text
num = "143..."
```

When we see `3`, the previous chosen digit is `4`.

Since:

```text
3 < 4
```

it is better to remove `4` and keep `3` earlier.

This is a monotonic increasing stack idea.

The stack should store digits in increasing order as much as possible.

---

## 3. Why Monotonic Stack?

We need to repeatedly remove the last chosen digit when a better smaller digit arrives.

That is exactly what a stack gives us:

```text
last chosen digit = stack top
```

Whenever the current digit is smaller than the stack top, we can pop the stack top if `k > 0`.

Rule:

```text
while stack is not empty
and k > 0
and current digit < stack top
    pop stack top
    k--
```

After that, push the current digit.

This keeps the answer as small as possible from left to right.

---

## 4. Stack Invariant

After processing each digit, the stack represents the best possible prefix using the digits seen so far.

The stack is mostly increasing:

```text
small digits stay before larger digits
```

It may not be perfectly sorted in every case, because we have only `k` removals.

Example:

```text
num = "765", k = 1
```

We can remove only one digit.

Process:

```text
7 -> stack [7]
6 -> pop 7, stack [6], k = 0
5 -> cannot pop anymore, stack [6,5]
```

Result:

```text
65
```

The stack is not increasing at the end, but it is still the best possible answer after exactly one removal.

---

## 5. Java Implementation

```java
class Solution {
    public String removeKdigits(String num, int k) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char digit : num.toCharArray()) {
            while (!stack.isEmpty() && k > 0 && digit < stack.peekLast()) {
                stack.pollLast();
                k--;
            }

            stack.offerLast(digit);
        }

        while (!stack.isEmpty() && k > 0) {
            stack.pollLast();
            k--;
        }

        StringBuilder result = new StringBuilder();

        while (!stack.isEmpty()) {
            char digit = stack.pollFirst();

            if (result.length() == 0 && digit == '0') {
                continue;
            }

            result.append(digit);
        }

        return result.length() == 0 ? "0" : result.toString();
    }
}
```

Why use `Deque<Character>` instead of `Stack<Character>`?

- `Stack` is old and synchronized.
- `ArrayDeque` is the preferred stack/deque implementation in modern Java.
- We use the back of the deque as the stack top.

---

## 6. Dry Run

Input:

```text
num = "1432219", k = 3
```

| digit | action | stack | k |
|---|---|---|---:|
| `1` | push `1` | `[1]` | 3 |
| `4` | push `4` | `[1,4]` | 3 |
| `3` | pop `4`, push `3` | `[1,3]` | 2 |
| `2` | pop `3`, push `2` | `[1,2]` | 1 |
| `2` | push `2` | `[1,2,2]` | 1 |
| `1` | pop `2`, push `1` | `[1,2,1]` | 0 |
| `9` | push `9` | `[1,2,1,9]` | 0 |

Final result:

```text
1219
```

Answer:

```text
"1219"
```

---

## 7. Why Remove From End If `k` Is Still Left?

Sometimes the number is already increasing.

Example:

```text
num = "12345", k = 2
```

No digit is smaller than the stack top while scanning, so nothing gets popped during the main loop.

Stack:

```text
[1,2,3,4,5]
```

We still must remove exactly `k = 2` digits.

To make the number smallest, remove from the end:

```text
12345 -> 123
```

That is why we do:

```java
while (!stack.isEmpty() && k > 0) {
    stack.pollLast();
    k--;
}
```

---

## 8. Leading Zeros

The result should not contain unnecessary leading zeros.

Example:

```text
num = "10200", k = 1
```

Remove `1`:

```text
0200
```

After removing leading zeros:

```text
200
```

Answer:

```text
"200"
```

If all digits are removed or only zeros remain, return:

```text
"0"
```

Example:

```text
num = "10", k = 2
answer = "0"
```

---

## 9. Common Mistakes

- Removing the largest digits globally instead of thinking left to right.
- Forgetting that earlier digits matter more than later digits.
- Forgetting to remove from the end if `k` is still greater than `0`.
- Forgetting to strip leading zeros.
- Returning an empty string instead of `"0"`.
- Using `digit <= stack.peekLast()` instead of `digit < stack.peekLast()`.

Why not use `<=`?

If digits are equal, keeping the earlier equal digit is better because it preserves more choices later.

Example:

```text
num = "112", k = 1
```

There is no benefit in removing the first `1` just because the next digit is also `1`.

---

## 10. Complexity

Time:

```text
O(n)
```

Each digit is pushed once and popped at most once.

Space:

```text
O(n)
```

The stack may store all digits.

---

## 11. Pattern Recognition

Use monotonic stack when:

- you are scanning left to right
- the latest chosen item may need to be removed
- a better current item can replace a worse previous item
- each element should be pushed and popped at most once

For this problem:

```text
better current item = smaller digit
worse previous item = larger digit before it
```

Stack type:

```text
monotonic increasing stack
```

---

## 12. Takeaway

To make the number smallest, make the left side as small as possible.

The stack stores the best prefix so far.

When a smaller digit arrives, remove larger previous digits while removals are available.

If removals remain after scanning, remove digits from the end.

Finally, strip leading zeros and return `"0"` if the result is empty.

---

# End of Notes
