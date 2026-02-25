# Dynamic Programming / Greedy Notes

## 38 - Frog Jump II (Round Trip, No Revisit) — Minimize Maximum Jump

**Generated on:** 2026-02-25 10:10:47 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You are given stone positions along a river bank (1D line). The frog starts at position 0, must reach the final bank at position `D`, and then return back to 0. The frog:
- Can jump forward to a strictly greater position and backward to a strictly smaller position,
- Cannot step on the same stone twice across the whole trip (forward + return),
- Wants to minimize the maximum single jump distance taken anywhere in the round trip.

Question: What is the minimum possible value of the maximum jump?

Example:
- stones = [0, 2, 11, 14, 17, 21] (including 0 and D=21)
- An optimal plan partitions stones into forward path and return path without overlap.
- The minimal maximum jump equals max over gaps of “skip-1” (details below).

------------------------------------------------------------------------

## 🔍 2. Key Insight (Greedy/Proof)

- Across the round trip (forward + return), each stone can be used at most once. 
- Equivalently, we must partition the stones (excluding the start/end overlaps appropriately) into two disjoint subsequences:
  - One used on the way out,
  - The other used on the way back.

Goal: Minimize the maximum “edge length” (jump) in either subsequence.

Greedy structure:
- When stones are sorted, an optimal partition is to put stones into two paths by taking them in alternating order:
  - Path A: indices 0, 2, 4, ...
  - Path B: indices 1, 3, 5, ...
  (or vice versa; either parity works)
- The maximum jump seen in these two paths is exactly the maximum difference between stones two apart in the original sorted list.

Therefore:
- Let positions be sorted: `p[0] = 0 < p[1] < ... < p[n] = D`.
- The answer is:
  
  Answer = max over i from 2 to n of (p[i] - p[i - 2])

Intuition for correctness:
- If two consecutive stones go to the same path, the alternative path will be forced to take a larger gap elsewhere.
- Alternating assignment balances the gaps across the two paths, and the critical jumps become exactly the “skip-1” gaps (distance over every other stone).
- This construction is optimal, and any deviation can only increase the maximum jump.

This is a classic result often titled “Frog Jump II” (minimax jump on a round trip with no revisits).

------------------------------------------------------------------------

## 🧱 3. Algorithm

Input: array/list of stone positions (may or may not include 0 and D)
- Ensure positions contain 0 and D (append if missing).
- Sort positions increasingly.
- Compute:
  - ans = 0
  - For i from 2 to n:
      ans = max(ans, p[i] - p[i - 2])
- Return ans

Complexity:
- Sorting: O(n log n)
- Single pass for maxima: O(n)
- Total: O(n log n), O(1) extra space (besides sorting)

------------------------------------------------------------------------

## 💻 4. Java Implementation

```java
import java.util.*;

public class FrogJumpII {
    // positions: stone coordinates; may or may not include 0 and D
    public static int minMaxJumpRoundTrip(int[] positions, int D) {
        // collect unique positions including endpoints
        TreeSet<Integer> set = new TreeSet<>();
        for (int x : positions) set.add(x);
        set.add(0);
        set.add(D);

        int n = set.size();
        int[] p = new int[n];
        int idx = 0;
        for (int x : set) p[idx++] = x;

        int ans = 0;
        for (int i = 2; i < n; i++) {
            ans = Math.max(ans, p[i] - p[i - 2]);
        }
        return ans;
    }

    // Example usage
    public static void main(String[] args) {
        int D = 21;
        int[] stones = {2, 11, 14, 17}; // 0 and D will be added automatically
        System.out.println(minMaxJumpRoundTrip(stones, D)); // prints 10
    }
}
```

Note:
- If your input already guarantees 0 and D are present and strictly increasing, you can skip the set building and just sort the given list, then apply the “skip-1 max gap” scan.

------------------------------------------------------------------------

## 🔎 5. Worked Example

stones (including endpoints): [0, 2, 11, 14, 17, 21]
- Differences over skipping one:
  - 11 - 0 = 11
  - 14 - 2 = 12
  - 17 - 11 = 6
  - 21 - 14 = 7
- max = 12

But check alternate partition:
- Path A (even indices): 0 → 11 → 17 → 21 has jumps {11, 6, 4} → max 11
- Path B (odd indices): 2 → 14 has jump {12} → max 12
Overall maximum = 12, which matches the formula.

(If you try other partitions, you’ll not beat 12.)

------------------------------------------------------------------------

## 🏷 6. Pattern Recognition

- Problem type: Minimax partitioning of edges into two disjoint paths (round trip without revisit).
- Technique: Sort + alternate assignment (greedy), answer equals max “every-other” gap.
- This is a greedy/constructive proof problem rather than a traditional DP; however, it belongs alongside “Frog Jump” themes and optimization on paths.

------------------------------------------------------------------------

## 🔄 7. Edge Cases and Pitfalls

- Ensure endpoints 0 and D are present.
- Duplicate stone positions should be removed (coordinates are unique in the standard setting).
- If there are only two stones (0 and D), answer is simply D.
- If there is only one intermediate stone x:
  - Answer = max(x - 0, D - x) (equivalent to the skip-1 rule with very small n).

------------------------------------------------------------------------

## ✅ 8. Takeaway

- Optimal strategy partitions stones into two non-overlapping alternating subsequences (forward/return).
- The minimized maximum jump is the maximum length of “skip-one” gaps in the sorted list.
- Implemented via a simple O(n log n) sort plus O(n) scan.

------------------------------------------------------------------------

# End of Notes
