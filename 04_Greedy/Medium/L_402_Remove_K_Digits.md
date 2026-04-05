# Greedy Notes

## Remove K Digits

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Given a non-negative integer `num` represented as a string and an integer `k`, remove `k` digits from the number so that the new number is the smallest possible.

Return the answer as a string.

**Example 1**

    Input: num = "1432219", k = 3
    Output: "1219"

**Example 2**

    Input: num = "10200", k = 1
    Output: "200"

------------------------------------------------------------------------

## 2. Monotonic Stack Intuition

To make the number small, we want smaller digits as early as possible.

So while scanning:

- if the current digit is smaller than the previous chosen digit
- and we still have deletions left

then the larger previous digit should be removed.

That is exactly a monotonic increasing stack idea.

------------------------------------------------------------------------

## 3. Greedy Rule

For each digit `c`:

- while stack top is larger than `c` and `k > 0`, pop
- push `c`

After the scan:

- if deletions still remain, remove from the end

Why from the end?

Because trailing digits have the least positional impact.

Then strip leading zeros from the final number.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    num = "1432219", k = 3

Process:

- take `1`
- take `4`
- `3` is smaller than `4`, pop `4`
- later `2` pops `3`
- later `1` pops one `2`

Final smallest sequence:

    "1219"

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static String removeKdigits(String num, int k) {
    Deque<Character> st = new ArrayDeque<>();

    for (char c : num.toCharArray()) {
        while (!st.isEmpty() && k > 0 && c < st.peek()) {
            st.pop();
            k--;
        }
        st.push(c);
    }

    while (!st.isEmpty() && k > 0) {
        st.pop();
        k--;
    }

    StringBuilder sb = new StringBuilder();
    while (!st.isEmpty()) sb.append(st.pop());

    while (sb.length() > 0 && sb.charAt(sb.length() - 1) == '0') {
        sb.deleteCharAt(sb.length() - 1);
    }

    String ans = sb.reverse().toString();
    return ans.isEmpty() ? "0" : ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(n)`

Pattern:

- monotonic increasing stack
- pop expensive digits as soon as a better smaller digit arrives

------------------------------------------------------------------------

## End of Notes
