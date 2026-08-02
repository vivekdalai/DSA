# Dynamic Programming Notes

## 54 - Jump Game I and II (LC 55 / LC 45) — Reachability and Min Steps

**Generated on:** 2026-08-03 01:15:00 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

Jump Game I (LC 55):
- Given `nums[]` where `nums[i]` is the maximum jump length from index `i`.
- Starting at index 0, return `true` if you can reach the last index.

Jump Game II (LC 45):
- Same setup, but the input is guaranteed reachable.
- Return the **minimum number of jumps** needed to reach the last index.

Reported in an Amazon SDE-1 interview (April 2026) as "max reachable index / number
of jumps," with the interviewer explicitly walking recursive → memoized → optimized —
i.e. they want to see the DP thought process even though the final accepted answer
is usually a greedy O(n) walk.

------------------------------------------------------------------------

## 🪜 2. State Definition

Jump I:
- `dp[i]` = `true` if index `i` is reachable from index 0.

Jump II:
- `dp[i]` = minimum number of jumps needed to reach index `i` from index 0.

Greedy re-framing (the O(n) upgrade path):
- Jump I: track `maxReach` = farthest index reachable so far while scanning left to
  right; fail as soon as `i > maxReach`.
- Jump II: track `currentEnd` (farthest reachable with jumps used so far) and
  `farthest` (farthest reachable with one more jump); increment `jumps` every time
  `i` catches up to `currentEnd` — this is a level-by-level BFS in disguise.

------------------------------------------------------------------------

## 🔁 3. Recurrence Relation

Jump I:

    dp[0] = true
    dp[i] = OR over j in [0, i) of ( dp[j] AND j + nums[j] >= i )

Jump II:

    dp[0] = 0
    dp[i] = min over j in [0, i), where j + nums[j] >= i, of ( dp[j] + 1 )

Both are O(n^2) as written (for each `i`, scan all earlier `j`). The greedy versions
below collapse this to O(n) by never re-scanning — they exploit that once a farthest
reachable index is known, individual `dp[j]` values don't need to be revisited.

------------------------------------------------------------------------

## 🧱 4. Base Cases and Initialization

- Jump I: `dp[0] = true`, all others `false` until proven reachable.
- Jump II: `dp[0] = 0`, all others `+infinity` until proven reachable.
- Single-element array (`n == 1`): already at the destination → Jump I is trivially
  `true`, Jump II is trivially `0`.

------------------------------------------------------------------------

## 📦 5. Space Optimization Insight

The O(n^2) DP is the natural first pass, but the real "optimization" here isn't
about space — it's collapsing the algorithm to O(n) time / O(1) space via greedy
range-covering, because both `dp[i]` only ever need the *best* (farthest / smallest)
predecessor, and that best value only ever grows monotonically as `i` increases. This
is the insight interviewers are fishing for.

------------------------------------------------------------------------

## 💻 6A. Jump Game I — Bottom-Up O(n²) DP (starting point)

```java
class JumpGameI_DP {
    public boolean canJump(int[] nums) {
        int n = nums.length;
        boolean[] dp = new boolean[n];
        dp[0] = true;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && j + nums[j] >= i) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[n - 1];
    }
}
```

Complexity: Time O(n^2), Space O(n)

------------------------------------------------------------------------

## 💻 6B. Jump Game I — Greedy O(n) (interview-preferred)

```java
class JumpGameI_Greedy {
    public boolean canJump(int[] nums) {
        int maxReach = 0;
        for (int i = 0; i < nums.length; i++) {
            if (i > maxReach) return false; // stuck before reaching i
            maxReach = Math.max(maxReach, i + nums[i]);
        }
        return true;
    }
}
```

Complexity: Time O(n), Space O(1)

------------------------------------------------------------------------

## 💻 6C. Jump Game II — Bottom-Up O(n²) DP (starting point)

```java
import java.util.*;

class JumpGameII_DP {
    public int jump(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] != Integer.MAX_VALUE && j + nums[j] >= i) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }
        return dp[n - 1];
    }
}
```

Complexity: Time O(n^2), Space O(n)

------------------------------------------------------------------------

## 💻 6D. Jump Game II — Greedy O(n) BFS-Level Expansion (interview-preferred)

```java
class JumpGameII_Greedy {
    public int jump(int[] nums) {
        int jumps = 0, currentEnd = 0, farthest = 0;

        for (int i = 0; i < nums.length - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == currentEnd) {         // exhausted this "jump level"
                jumps++;
                currentEnd = farthest;
            }
        }
        return jumps;
    }
}
```

Complexity: Time O(n), Space O(1)

------------------------------------------------------------------------

## 🔎 7. Full Dry Run Example

Input: `nums = [2, 3, 1, 1, 4]`

Jump I (greedy):
- i=0: maxReach = max(0, 0+2) = 2
- i=1: maxReach = max(2, 1+3) = 4
- i=2: maxReach = max(4, 2+1) = 4
- i=3, i=4: within reach → `true`

Jump II (greedy):
- i=0: farthest = 2, i==currentEnd(0) → jumps=1, currentEnd=2
- i=1: farthest = max(2, 4)=4
- i=2: i==currentEnd(2) → jumps=2, currentEnd=4
- i=3: farthest = max(4,4)=4 (loop stops at n-1=4)

Answer: minimum jumps = 2 (0 → 1 → 4)

------------------------------------------------------------------------

## 🏷 8. Pattern Recognition

- Name: Reachability / Min-Steps over Jump Ranges
- Family: interval-covering greedy, expressible first as O(n²) DP for clarity
- Triggers: "max index reachable from i," "min steps/jumps to reach the end,"
  "cover a range using overlapping intervals"
- Relatives: Video Stitching, Gas Station (circular greedy), Minimum Number of Taps
  to Open to Water a Garden — all the same "extend the frontier greedily" shape.
- Interview framing: start from the DP (shows you understand *why* it works), then
  explicitly upgrade to greedy and explain the monotonic-frontier argument for why
  greedy is safe here (unlike, say, 0/1 Knapsack, where greedy fails).

------------------------------------------------------------------------

## 🔄 9. Edge Cases and Pitfalls

Edge Cases:
- `n == 1` → already at destination: Jump I `true`, Jump II `0`
- `nums[i] == 0` mid-array → can strand you; Jump I greedy catches this via
  `i > maxReach`
- All zeros except `nums[0]` → only reachable if `nums[0]` alone covers the array

Pitfalls:
- Jump II loop bound: iterate to `n - 2` (or `< n - 1`), not `n - 1` — incrementing
  `jumps` when `i` reaches the last index overcounts by one
- Assuming greedy is "obviously correct" without being able to explain the frontier
  argument — interviewers will push on this
- Off-by-one in the O(n²) DP's inner bound (`j < i`, not `j <= i`)

------------------------------------------------------------------------

## ✅ 10. Takeaway

- Both problems reduce to "extend the farthest reachable frontier as you scan left
  to right" — DP formulation is O(n²) and clarifies *why*, greedy is O(n) and is
  what you should land on.
- Jump Game II's greedy is a level-order BFS in array form: `currentEnd` marks the
  boundary of the current "jump," `farthest` previews the next one.
- Walk the interviewer through DP → greedy explicitly; that progression is what's
  being evaluated, not just the final one-liner.

------------------------------------------------------------------------

# End of Notes
