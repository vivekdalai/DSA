# Greedy Notes

## 16 - Lemonade Change

**Generated on:** 2026-04-06 01:10:54 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

Each lemonade costs `$5`.

Customers arrive in order and pay with bills of:

- `5`
- `10`
- `20`

Return `true` if you can provide correct change to every customer, otherwise return `false`.

Initially, you have no change.

**Example 1**

    Input: bills = [5, 5, 5, 10, 20]
    Output: true

**Example 2**

    Input: bills = [5, 5, 10, 10, 20]
    Output: false

------------------------------------------------------------------------

## 2. What State Matters

Only two counts matter:

- how many `$5` bills we have
- how many `$10` bills we have

We never need to store `$20` bills because they are never used as change.

------------------------------------------------------------------------

## 3. Greedy Change Rule

When a customer pays:

### With `5`

- no change needed
- increase count of `$5`

### With `10`

- must give one `$5`

### With `20`

Need `$15` change.

The greedy rule is:

- prefer giving one `$10` and one `$5`
- otherwise give three `$5`

Why prefer `10 + 5`?

Because `$5` bills are more flexible for future customers.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    bills = [5, 5, 5, 10, 20]

Process:

- get `5` -> `five = 1`
- get `5` -> `five = 2`
- get `5` -> `five = 3`
- get `10` -> give one `5`, now `five = 2`, `ten = 1`
- get `20` -> give `10 + 5`, now `five = 1`, `ten = 0`

All customers are served, so return `true`.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean lemonadeChange(int[] bills) {
    int five = 0, ten = 0;

    for (int bill : bills) {
        if (bill == 5) {
            five++;
        } else if (bill == 10) {
            if (five == 0) return false;
            five--;
            ten++;
        } else {
            if (ten >= 1 && five >= 1) {
                ten--;
                five--;
            } else if (five >= 3) {
                five -= 3;
            } else {
                return false;
            }
        }
    }

    return true;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- maintain limited inventory
- for change-making, preserve the most flexible bill when possible

------------------------------------------------------------------------

## End of Notes
