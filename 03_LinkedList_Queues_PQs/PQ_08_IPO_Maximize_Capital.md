# Priority Queue Notes

## 08 - IPO / Maximize Capital

Problem: [LeetCode 502 - IPO](https://leetcode.com/problems/ipo/description/)

---

## 1. Problem, In One Line

Starting with capital `w`, pick up to `k` projects (each with a required `capital[i]` and a `profits[i]`) to maximize final capital — you can only start a project if your *current* capital covers its requirement, and each project can be picked at most once.

**Example**

    Input:  w = 0, k = 2, capital = [0,1,1], profits = [1,2,3]
    Output: 4

    Input:  w = 0, k = 3, capital = [0,1,2], profits = [1,2,3]
    Output: 6

---

## 2. Core Idea

At each of the `k` steps, the right move is: among every project you can currently *afford*, take the one with the highest profit. Capital only grows over time, so a project that's affordable now stays affordable forever — you never need to "reconsider" one you skipped.

That means the natural two-phase filter is:
1. **Unlock** — move every project whose capital requirement is `<=` current capital out of a "not yet affordable" pool and into an "affordable" pool.
2. **Choose** — take the max-profit project from the affordable pool.

Two different orderings are needed for those two questions ("what's affordable" vs. "what's most profitable"), which is exactly what two heaps buy you.

---

## 3. Why This Is Greedy, Not DP

At a glance this looks like it could need DP — "pick a limited number of items to maximize a sum" is the same shape as 0/1 Knapsack (`DP_18_0_1_Knapsack.md`). It doesn't, and the reason is what the *capital* constraint actually does.

In knapsack, weight is a **depleting** resource: every item you take consumes some of a fixed budget, and that budget never comes back. Greedily taking the best value/weight ratio can lock you out of a better combination later, because the budget spent on one item can't be un-spent — that's exactly why knapsack needs to explore the trade-off space via DP.

In IPO, capital is a **threshold gate**, not a budget you spend. Choosing a project doesn't consume capital — it only *adds* profit to it. The requirement `capital[i]` is a one-time bar to clear, not a cost paid out of a shrinking pool. Once a project is affordable, it stays affordable forever, no matter what else gets picked first.

That monotonicity is what makes the local greedy choice provably optimal (an exchange argument):

- Suppose, at some step, projects `p1` and `p2` are both affordable, `profit(p2) > profit(p1)`, and an optimal plan picks `p1` here instead of `p2`.
- Swap them: pick `p2` now instead. This is always legal — `p2` was affordable at this exact point, same as `p1` was.
- After the swap, capital at every later step is **at least as large** as before (you gained `profit(p2) - profit(p1) >= 0` extra), so every project that was affordable down the line under the old plan is still affordable under the new one — nothing gets locked out.
- Total profit strictly does not decrease. So swapping toward "always take the max-profit affordable project" never hurts, and repeating the argument shows it's optimal at every step.

Since capital never shrinks and the constraint only ever gets *easier* to satisfy, there's no trade-off left to search over — the greedy choice is safe, and no DP table over "which subset" or "how much capital" is needed.

---

## 4. What Goes In The Heaps

- `byCapital` — a min-heap of `{capital, profit}`, ordered by `capital` ascending. This answers "what's the cheapest still-locked project?" so unlocking can stop as soon as the cheapest remaining one is still too expensive.
- `byProfit` — a max-heap of plain profit values, holding every project that's currently affordable. This answers "what's the best thing I can do right now?" in `O(log n)`.

A project moves **one-way**: `byCapital` → `byProfit` when unlocked, then out of `byProfit` when chosen. It never needs to go back.

---

## 5. The Algorithm, Step By Step

1. Push every project onto `byCapital` as `{capital, profit}`.
2. Repeat up to `k` times:
   - While the cheapest project in `byCapital` costs `<=` current capital, pop it and push its profit onto `byProfit`.
   - If `byProfit` is empty, no affordable project remains — stop early (fewer than `k` projects may end up chosen).
   - Otherwise pop the max profit off `byProfit` and add it to current capital.
3. Return the final capital.

---

## 6. Code

This is the actual LeetCode submission signature — `class Solution`, method `findMaximizedCapital(k, w, profits, capital)` — so it's directly pasteable, not just illustrative:

```java
import java.util.*;

class Solution {
    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        int n = profits.length;
        PriorityQueue<int[]> byCapital = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0])); // {capital_required, profit_gained}

        for (int i = 0; i < n; i++) {
            byCapital.offer(new int[]{capital[i], profits[i]});
        }

        int currentCapital = w;
        PriorityQueue<Integer> byProfit = new PriorityQueue<>(Collections.reverseOrder());
        for (int i = 0; i < k; i++) {
            // for every choice, check available investment options and take the max profit achievable
            while (!byCapital.isEmpty() && byCapital.peek()[0] <= currentCapital) {
                byProfit.offer(byCapital.poll()[1]); // move every newly affordable profit into byProfit
            }

            if (byProfit.isEmpty())
                break; // no profit to add

            currentCapital += byProfit.poll();
        }

        return currentCapital;
    }
}
```

Complexity:
- Time: `O(n log n)` — every project is pushed/popped from `byCapital` at most once, and from `byProfit` at most once; the outer loop runs at most `k` times.
- Space: `O(n)` across both heaps.

---

## 7. Dry Run

Input: `w = 0, k = 2, capital = [0,1,1], profits = [1,2,3]`

`byCapital` initially: `{(0,1), (1,2), (1,3)}` (by capital ascending)

| Step | Unlocked this round | `byProfit` before pick | Picked | `currentCapital` after |
|---|---|---|---|---|
| 1 | `(0,1)` (capital `0 <= 0`) | `[1]` | `1` | `0 + 1 = 1` |
| 2 | `(1,2)` and `(1,3)` (both `<= 1` now) | `[2,3]` | `3` | `1 + 3 = 4` |

`k = 2` steps used, final capital `= 4` — matches expected output. (Note project with profit `2` is left on the table — only `k` picks are allowed, and profit `3` was strictly better.)

---

## 8. Edge Cases and Pitfalls

- `byProfit` can legitimately run dry before `k` picks are used up — that's not an error, it means fewer than `k` projects were ever affordable; break out rather than looping further.
- The `while` unlock loop must run **every** iteration of the outer loop, not just once at the start — capital changes after each pick, which can unlock new projects that were too expensive a step earlier.
- Comparator for `byCapital` sorts ascending (min-heap, cheapest first) while `byProfit` needs `Collections.reverseOrder()` (max-heap, most profitable first) — mixing these up is the most common transcription bug in this problem.
- `currentCapital` is accumulated as a plain `int` here, which is safe **only** because LeetCode's stated constraints for this problem keep the maximum possible sum (`w + k * max(profits[i])`, up to roughly `10^9 + 10^5 * 10^4 = 2*10^9`) just under `Integer.MAX_VALUE` (`~2.147*10^9`). It's a tight margin — if you're reusing this pattern for a variant with looser constraints, accumulate in `long` and narrow only at the return.
- All projects with `capital[i] == 0` are affordable from the very first step regardless of starting capital `w` — don't special-case this, the `<=` comparison already covers it.

---

## 9. Complexity And Pattern

Pattern:

- greedy "unlock what's newly affordable, then take the best available" — same two-heap shape as `PQ_08` itself (this file) and conceptually related to `PQ_02_Minimum_Interval_To_Include_Each_Query.md`'s "grow the eligible set, then answer from it" structure.
- capital is monotonically non-decreasing here, which is what makes the greedy provably correct — once a project is affordable it stays affordable, so there's never a reason to hold off on unlocking it.
- One-way flow between two heaps (`byCapital` → `byProfit`, never backward) is the tell that this is a two-heap greedy rather than needing a more general search.

Trigger words:
- "at most k projects", "each project requires capital X and yields profit Y", "maximize final capital"
- "unlock" / "become eligible" framing combined with "pick the best of what's currently eligible"

---

## End of Notes
