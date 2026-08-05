# Prefix Sum Notes

## 06 - Range Flip Using Difference Array

**Updated on:** 2026-08-05

------------------------------------------------------------------------

## 1. Problem Understanding

Given an integer array `arr` and a list of queries, where each query is `[L, R]`, apply every query by flipping the sign of every element in the range `L..R` (inclusive).

A naive approach loops over `L..R` for every query and negates each element directly. If there are `q` queries each covering up to `n` elements, this costs `O(n * q)`.

We want to apply all queries and produce the final array in roughly `O(n + q)`.

------------------------------------------------------------------------

## 2. Key Insight

We do not need to know how many times each index was flipped exactly - we only need to know whether it was flipped an **odd** or **even** number of times, since two flips cancel out (`-(-x) = x`).

So instead of touching every element in every range, record just the **start** and **end+1** of each range in a difference array, then take a running prefix sum over it. The prefix sum at index `i` tells us exactly how many queries cover index `i`.

- covered an odd number of times -> flip the sign
- covered an even number of times -> leave it unchanged

This turns a range-update problem into a point-update + prefix-sum problem, which is the same trick as `Range Addition` / `Corporate Flight Bookings`, adapted to care about **parity** instead of the raw sum.

------------------------------------------------------------------------

## 3. Difference Array Meaning

For a query `[L, R]`:
- `diff[L] += 1` -> "one more active flip starts here"
- `diff[R + 1] -= 1` -> "that flip stops applying after R"

`diff` is sized `n + 1` so `R + 1` never goes out of bounds even when `R == n - 1`.

When we later take a running sum of `diff`, `running` at index `i` equals the number of queries whose range currently covers `i`.

------------------------------------------------------------------------

## 4. Step-by-Step Logic

1. Create `diff` of size `n + 1`, initialized to `0`.
2. For each query `[L, R]`:
   - `diff[L] += 1`
   - `diff[R + 1] -= 1`
3. Scan `i` from `0` to `n - 1`:
   - `running += diff[i]`
   - if `running` is odd, flip `arr[i]` (`arr[i] = -arr[i]`)
4. Return `arr`.

------------------------------------------------------------------------

## 5. Code

```java
int[] applyFlips(int[] arr, int[][] queries) {
    int n = arr.length;
    int[] diff = new int[n + 1];

    for (int[] q : queries) {
        int L = q[0], R = q[1];
        diff[L] += 1;
        diff[R + 1] -= 1;
    }

    int running = 0;
    for (int i = 0; i < n; i++) {
        running += diff[i];
        if ((running & 1) == 1) arr[i] = -arr[i];
    }
    return arr;
}
```

------------------------------------------------------------------------

## 6. Dry Run

Take:
- `arr = [1, 2, 3, 4, 5]`
- `queries = [[0, 2], [1, 3]]`

Build `diff` (size `6`):
- query `[0, 2]`: `diff[0] += 1`, `diff[3] -= 1`
- query `[1, 3]`: `diff[1] += 1`, `diff[4] -= 1`
- `diff = [1, 1, 0, -1, -1, 0]`

Scan and accumulate `running`:
- `i=0`: `running = 1` -> odd -> flip -> `arr[0] = -1`
- `i=1`: `running = 2` -> even -> `arr[1] = 2`
- `i=2`: `running = 2` -> even -> `arr[2] = 3`
- `i=3`: `running = 1` -> odd -> flip -> `arr[3] = -4`
- `i=4`: `running = 0` -> even -> `arr[4] = 5`

Result:
- `[-1, 2, 3, -4, 5]`

Check against ranges directly:
- index `0`: covered by `[0,2]` only -> 1 flip -> odd -> flipped ✓
- index `1`: covered by `[0,2]` and `[1,3]` -> 2 flips -> even -> unchanged ✓
- index `2`: covered by `[0,2]` and `[1,3]` -> 2 flips -> even -> unchanged ✓
- index `3`: covered by `[1,3]` only -> 1 flip -> odd -> flipped ✓
- index `4`: covered by nothing -> 0 flips -> even -> unchanged ✓

------------------------------------------------------------------------

## 7. Complexity

- Time: `O(n + q)` where `q` is the number of queries
- Space: `O(n)` for the difference array

------------------------------------------------------------------------

## 8. Pattern Recognition

Use this trick when:
- there are many range-update queries applied to an array
- you only care about the final state, not the intermediate states after each query
- the update is additive (increment, flip, toggle) rather than something like "set to value" (which difference arrays cannot express directly)

Signal:
- "apply these `q` range operations, then return the final array" almost always means: difference array + one prefix-sum pass, instead of applying each query directly.

For flips/toggles specifically:
- track parity (`count & 1`) instead of the raw count, since flips are self-canceling.

------------------------------------------------------------------------

## 9. Edge Cases and Pitfalls

- `diff` must be sized `n + 1`, not `n`, so `diff[R + 1]` is always valid even when `R == n - 1`.
- Order of queries does not matter - only the final coverage count (or its parity) at each index matters.
- This technique only works for range operations that are associative/cancelable (add, flip). It does not work for "assign value V to range" without extra machinery (e.g. segment tree with lazy propagation).
- Don't apply the flip while building `diff` - the flip decision only makes sense after the prefix sum (running count) is known.

------------------------------------------------------------------------

## 10. Takeaway

- Range updates followed by a "read final state" step should trigger difference array + prefix sum, not direct per-element mutation per query.
- Difference array turns `O(range length)` work per query into `O(1)` per query, deferring all the real work to one final `O(n)` pass.
- For flip/toggle-style updates, reduce the running count to parity (`& 1`) instead of using the raw count.

------------------------------------------------------------------------

# End of Notes
