# Greedy Notes

## 04 - Maximum 69 Number

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given a positive integer containing only digits `6` and `9`.

You may change at most one digit:

- `6` to `9`
- or `9` to `6`

Return the maximum number possible.

**Example 1**

    Input: num = 9669
    Output: 9969

**Example 2**

    Input: num = 9996
    Output: 9999

------------------------------------------------------------------------

## 2. Why The First `6` Matters

To maximize a number, earlier digits matter more than later digits.

So if we are allowed only one change:

- changing the leftmost `6` to `9` gives the biggest increase

There is never a reason to flip a `9` into `6` when the goal is maximum value.

------------------------------------------------------------------------

## 3. Greedy Strategy

Convert the number to a character array.

Scan from left to right:

- if the current digit is `6`, change it to `9`
- stop immediately

If no `6` exists, the number is already optimal.

------------------------------------------------------------------------

## 4. Tiny Dry Run

**Input:**

    num = 9669

Digits:

    [9, 6, 6, 9]

The first `6` appears at index `1`.

After one flip:

    [9, 9, 6, 9]

Result:

    9969

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maximum69Number(int num) {
    char[] c = String.valueOf(num).toCharArray();

    for (int i = 0; i < c.length; i++) {
        if (c[i] == '6') {
            c[i] = '9';
            break;
        }
    }

    return Integer.parseInt(new String(c));
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(d)` where `d` is the number of digits
- **Space:** `O(d)`

Pattern:

- maximize a number by improving the earliest possible digit

------------------------------------------------------------------------

## End of Notes
