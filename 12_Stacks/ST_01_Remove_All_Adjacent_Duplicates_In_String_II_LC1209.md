# Stack Notes

## 01 - Remove All Adjacent Duplicates in String II (LC 1209)

Problem: Given a string `s` and an integer `k`, repeatedly remove every group of `k` adjacent equal characters until no such group remains.

Return the final string.

---

## 1. Problem Understanding

We need to delete adjacent duplicate blocks of size `k`.

Example:

```text
s = "deeedbbcccbdaa", k = 3
answer = "aa"
```

Deletion flow:

```text
deeedbbcccbdaa
remove eee -> ddbbcccbdaa
remove ccc -> ddbbbdaa
remove bbb -> dddaa
remove ddd -> aa
```

Important point:

After one deletion, new adjacent duplicates may be formed, so the process must continue naturally.

---

## 2. Core Intuition

When scanning from left to right, we only need to know:

- the current character
- how many same characters are adjacent at the end

This is a stack problem because the last processed characters are the first ones that may be removed.

If the count of the latest same-character block becomes `k`, remove that block from the stack.

---

## 3. Why Stack?

The stack represents the current valid string after processing characters so far.

When a new character comes:

- if it is different from the stack top, its count starts from `1`
- if it is same as the stack top, its count becomes `top.count + 1`
- if the count becomes `k`, remove the last `k` characters

This handles cascading deletions automatically.

Example:

```text
s = "abbba", k = 3
```

After removing `"bbb"`, the two `a` characters become adjacent:

```text
abbba -> aa
```

Because the stack keeps the already processed left side, this merge happens naturally.

---

## 4. Your Current Implementation Idea

Your code stores one `Pair` per character.

Each pair contains:

```java
char ch;
int count;
```

For repeated characters, every pushed character stores the current running count.

Example for `"aaa"` with `k = 3`:

```text
push (a,1)
push (a,2)
push (a,3)
count == k, remove last 3 pairs
```

This works because when a block reaches size `k`, your helper removes exactly `k` characters from the back of the deque.

---

## 5. Java Implementation From Your Code

```java
class Solution {
    public String removeDuplicates(String s, int k) {
        Deque<Pair> deque = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            if (deque.isEmpty()) {
                deque.addLast(new Pair(c, 1));
            } else {
                if (deque.peekLast().ch != c) {
                    deque.addLast(new Pair(c, 1));
                } else {
                    int lastCount = deque.peekLast().count;
                    deque.addLast(new Pair(c, lastCount + 1));
                }
            }

            if (deque.peekLast().count == k) {
                make_K_Deletion(deque, k);
            }
        }

        StringBuilder sb = new StringBuilder();

        while (!deque.isEmpty()) {
            sb.append(deque.removeFirst().ch);
        }

        return sb.toString();
    }

    private void make_K_Deletion(Deque<Pair> deque, int k) {
        while (!deque.isEmpty() && k > 0) {
            deque.removeLast();
            k--;
        }
    }
}

class Pair {
    char ch;
    int count;

    public Pair(char ch, int count) {
        this.ch = ch;
        this.count = count;
    }
}
```

---

## 6. Dry Run

Input:

```text
s = "deeedbbcccbdaa", k = 3
```

Key stack changes:

```text
d      -> d
de     -> de
dee    -> dee
deee   -> d          remove eee
ddb    -> ddb
ddbb   -> ddbb
ddbbc  -> ddbbc
ddbbcc -> ddbbcc
ddbbccc -> ddbb      remove ccc
ddbbb  -> dd         remove bbb
ddd    -> empty      remove ddd
a      -> a
aa     -> aa
```

Answer:

```text
"aa"
```

---

## 7. More Space-Efficient Interview Version

Instead of storing one pair per character, store one pair per consecutive block.

For example:

```text
aaabb
```

Can be stored as:

```text
(a,3), (b,2)
```

Code:

```java
class Solution {
    public String removeDuplicates(String s, int k) {
        Deque<Pair> stack = new ArrayDeque<>();

        for (char ch : s.toCharArray()) {
            if (!stack.isEmpty() && stack.peekLast().ch == ch) {
                stack.peekLast().count++;
            } else {
                stack.addLast(new Pair(ch, 1));
            }

            if (stack.peekLast().count == k) {
                stack.removeLast();
            }
        }

        StringBuilder result = new StringBuilder();

        while (!stack.isEmpty()) {
            Pair pair = stack.removeFirst();

            for (int i = 0; i < pair.count; i++) {
                result.append(pair.ch);
            }
        }

        return result.toString();
    }
}

class Pair {
    char ch;
    int count;

    Pair(char ch, int count) {
        this.ch = ch;
        this.count = count;
    }
}
```

Why this version is cleaner:

- It stores compressed groups instead of every character.
- When a group reaches `k`, removing one pair removes the whole group.
- The final string is rebuilt by repeating each character `count` times.

---

## 8. Complexity

For your current version:

```text
Time:  O(n)
Space: O(n)
```

Each character is pushed once and removed at most once.

For the compressed pair version:

```text
Time:  O(n)
Space: O(n)
```

The worst case is still `O(n)` space, but it can use less space when there are long repeated blocks.

---

## 9. Common Mistakes

- Forgetting that deletion can create new adjacent duplicate groups.
- Using a simple one-pass string replacement without stack behavior.
- Removing only one character when `count == k`.
- Rebuilding the answer from the back and accidentally reversing it.
- In the compressed version, forgetting to repeat each character by its stored count.

---

## 10. Pattern Recognition

Use this pattern when:

- adjacent duplicates matter
- deletion happens at the latest processed block
- deleting a block can connect left and right parts
- the problem asks to repeatedly remove until stable

Common stack idea:

```text
store character + count of consecutive block
remove the top block when count reaches k
```

---

## 11. Takeaway

This is a stack-based string reduction problem.

The stack stores the current stable prefix. Each new character either starts a new block or increases the count of the top block. When the count reaches `k`, remove that block immediately.

Because the remaining left side is already on the stack, cascading duplicate removals are handled naturally.

---

# End of Notes
