# Dynamic Programming Notes

## 17 - Candy Distribution (LC 135) — Two-Pass DP

**Generated on:** 2026-02-24 20:40:10 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

There are `n` children standing in a line, each child has a rating value.  
You must distribute candies such that:
- Each child has at least one candy.
- A child with a higher rating than an immediate neighbor gets more candies than that neighbor.

Goal: Minimize the total number of candies required.

Key Insight:
- This is a local-neighbor constraint from both sides.
- A single left-to-right pass enforces only the left neighbor rule; we also need a right-to-left pass.

------------------------------------------------------------------------

## 🪜 2. State Idea

Two direction constraints:
- Left-to-right: If `ratings[i] > ratings[i-1]` then `candies[i] > candies[i-1]`
- Right-to-left: If `ratings[i] > ratings[i+1]` then `candies[i] > candies[i+1]`

We can:
- Build an increasing run from left.
- Build an increasing run from right.
- Take the max per index to satisfy both constraints simultaneously.

------------------------------------------------------------------------

## 🔁 3. Recurrence / Transitions

Left pass:
- `left[i] = left[i-1] + 1` if `ratings[i] > ratings[i-1]`
- else `left[i] = 1`

Right pass:
- `right[i] = right[i+1] + 1` if `ratings[i] > ratings[i+1]`
- else `right[i] = 1`

Final candies per child:
- `ans[i] = max(left[i], right[i])`

Total:
- `sum(ans[i])`

------------------------------------------------------------------------

## 🧱 4. Base Cases

- For both `left[]` and `right[]`, initialize each entry with 1 (minimum per child).
- Boundary comparisons handle i-1 and i+1 naturally via loop bounds.

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

You can:
- Use two arrays `left[]` and `right[]`, or
- Use one array `candies[]` for the left-to-right pass, then a single backward pass adjusts in place while summing:
  - `candies[i] = max(candies[i], candies[i+1] + 1)` when `ratings[i] > ratings[i+1]`

This yields O(1) extra beyond the `candies[]` buffer.

------------------------------------------------------------------------

## 💻 6A. Clean Interview Version — Two Arrays

```java
import java.util.*;

class SolutionTwoPass {
    public int candy(int[] ratings) {
        int n = ratings.length;
        if (n == 0) return 0;

        int[] left = new int[n];
        int[] right = new int[n];
        Arrays.fill(left, 1);
        Arrays.fill(right, 1);

        // left to right
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                left[i] = left[i - 1] + 1;
            }
        }

        // right to left
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                right[i] = right[i + 1] + 1;
            }
        }

        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += Math.max(left[i], right[i]);
        }
        return sum;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n)

------------------------------------------------------------------------

## 💻 6B. Clean Interview Version — Single Array + Backward Adjust

```java
import java.util.*;

class Solution {
    public int candy(int[] ratings) {
        int n = ratings.length;
        if (n == 0) return 0;

        int[] candies = new int[n];
        Arrays.fill(candies, 1);

        // left to right
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }

        // right to left + sum
        int sum = candies[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
            sum += candies[i];
        }
        return sum;
    }
}
```

Complexity:
- Time: O(n)
- Space: O(n) for `candies[]` (no second array)

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input:
```
ratings = [1, 0, 2]
```

- Left pass:
  - candies = [1, 1, 2] (since 2 > 0 → +1 over previous)
- Right pass adjust:
  - i=1: ratings[1]=0 <= ratings[2]=2 → no change
  - i=0: ratings[0]=1 > ratings[1]=0 → candies[0] = max(1, candies[1]+1=2) = 2
- Final candies: [2, 1, 2]
- Sum = 5

Another:
```
ratings = [1, 2, 2]
```
- Left pass: [1, 2, 1]
- Right adjust:
  - i=1: ratings[1]=2 > ratings[2]=2? no (strict >), keep [1,2,1]
  - i=0: ratings[0]=1 > ratings[1]=2? no
- Sum = 1 + 2 + 1 = 4

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Two-direction Local Constraint DP
- Family: 1D DP with “neighbor inequality”
- Triggers:
  - Each index must satisfy inequality versus both neighbors
  - Natural solution: enforce left rule and right rule via two sweeps

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

- Equal ratings neighbors: do NOT force difference; both can be 1.
- Strictness: use `>` not `>=`. With `>=`, you may over-allocate.
- All increasing: result is 1 + 2 + ... + n.
- All decreasing: result is 1 + 2 + ... + n (from the other side).
- Large n: O(n) solutions required; avoid O(n log n) or quadratic approaches.

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Two passes (left→right, right→left) enforce both neighbor constraints.
- Combine by taking max at each index.
- Single-array backward-adjust approach is compact and optimal in practice.

------------------------------------------------------------------------

# End of Notes
