# Greedy Notes

## Maximum Swap

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/maximum-swap/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

Given a non-negative integer `num`, you may swap two digits at most once.

Return the maximum value you can get.

**Example 1**

    Input: num = 2736
    Output: 7236

**Example 2**

    Input: num = 9973
    Output: 9973

------------------------------------------------------------------------

## 2. Leftmost Improvement Intuition

To maximize a number, improve the earliest possible digit.

For each position, ask:

- does a larger digit appear later?

If yes, swap with the farthest-right occurrence of the best larger digit.

Using the farthest-right occurrence preserves larger digits earlier in the number.

------------------------------------------------------------------------

## 3. Cleanest Version In The File

The `v2` method is the easiest to revise.

It stores:

    last[d] = last index where digit d appears

Then for each position `i`:

- try digits from `9` down to `current + 1`
- if some larger digit appears later, swap and return immediately

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    num = 2736

Digits:

    [2,7,3,6]

At index `0`, a larger digit `7` appears later.

Swap `2` with that `7`:

    [7,2,3,6]

Answer:

    7236

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maximumSwap_v2(int num) {
    char[] digits = String.valueOf(num).toCharArray();
    int[] last = new int[10];

    for (int i = 0; i < digits.length; i++) {
        last[digits[i] - '0'] = i;
    }

    for (int i = 0; i < digits.length; i++) {
        for (int d = 9; d > digits[i] - '0'; d--) {
            if (last[d] > i) {
                char temp = digits[i];
                digits[i] = digits[last[d]];
                digits[last[d]] = temp;
                return Integer.parseInt(new String(digits));
            }
        }
    }

    return num;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(d * 10)` where `d` is number of digits
- **Space:** `O(10)`

Pattern:

- maximize earliest position first
- use last occurrence to choose the best swap target

------------------------------------------------------------------------

## End of Notes
