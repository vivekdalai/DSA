# Greedy Notes

## Sum Game

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/sum-game/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given a string `num` of even length containing digits and `'?'`.

Alice and Bob take turns replacing `'?'` with digits from `0` to `9`.

After all replacements:

- if the sum of the first half equals the sum of the second half, Bob wins
- otherwise Alice wins

Assuming both play optimally, return `true` if Alice wins, otherwise return `false`.

**Example 1**

    Input: num = "5023"
    Output: false

There are no question marks, and both half sums are already equal.

**Example 2**

    Input: num = "25??"
    Output: true

------------------------------------------------------------------------

## 2. What Actually Matters

We do not need to simulate turns directly.

Only four values matter:

- `leftSum`
- `rightSum`
- `leftQ`
- `rightQ`

The whole game collapses into whether Bob can force perfect balance by the end.

------------------------------------------------------------------------

## 3. Key Result

Two facts control the answer:

1. If the total number of `'?'` is odd, Alice wins immediately.
2. If it is even, Bob wins only in one perfectly balanced situation.

The code checks that exact condition:

    (rightSum - leftSum) == ((leftQ - rightQ) / 2) * 9

If this equality fails, Alice can force inequality, so the answer is `true`.

------------------------------------------------------------------------

## 4. Why The `9` Appears

Each unmatched pair of question marks across the two halves can shift the half-sum difference by at most `9`.

So the only way Bob survives is if the existing sum difference can be exactly canceled by the available question-mark imbalance.

If the cancellation target does not match, Alice can always steer the final totals apart.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public boolean sumGame(String num) {
    int rightSum = 0, leftSum = 0, leftQ = 0, rightQ = 0, n = num.length();

    for (int i = 0; i < n; i++) {
        char c = num.charAt(i);
        if (i < n / 2) {
            if (c == '?') leftQ++;
            else leftSum += c - '0';
        } else {
            if (c == '?') rightQ++;
            else rightSum += c - '0';
        }
    }

    if ((leftQ + rightQ) % 2 != 0) return true;
    return (rightSum - leftSum) != ((leftQ - rightQ) / 2) * 9;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(1)`

Pattern:

- convert a game into arithmetic invariants instead of move-by-move simulation

------------------------------------------------------------------------

## End of Notes
