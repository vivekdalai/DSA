# Dynamic Programming Notes

## 50 - Maximum Profit in Job Scheduling (LC 1235) — Weighted Interval Scheduling

**Generated on:** 2026-02-25 23:24:40 (IST)

------------------------------------------------------------------------

## 🧠 1. Problem Understanding

You’re given arrays `startTime[]`, `endTime[]`, and `profit[]` for `n` jobs.  
You can select non-overlapping jobs (a job `i` with end time `Ei` is compatible with job `j` with start time `Sj` if `Ei <= Sj`).  
Goal: Maximize total profit.

This is the classic Weighted Interval Scheduling problem.

------------------------------------------------------------------------

## 🪜 2. Key Insight

- Sort jobs by end time.
- For each job i, use binary search to find the rightmost job `p(i)` that doesn’t overlap with `i` (i.e., `end[p(i)] <= start[i]`).
- DP recurrence:
  - `dp[i]` = maximum profit considering jobs up to index i (0-based after sorting).
  - `dp[i] = max(dp[i-1], profit[i] + (p(i) >= 0 ? dp[p(i)] : 0))`

Answer: `dp[n-1]`.

Complexity: O(n log n) (sort + n x binary search)

------------------------------------------------------------------------

## 💻 3A. Bottom-Up with Binary Search (Java)

```java
import java.util.*;

public class MaxProfitJobScheduling {
    static class Job {
        int s, e, p;
        Job(int s, int e, int p) { this.s = s; this.e = e; this.p = p; }
    }

    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        Job[] jobs = new Job[n];
        for (int i = 0; i < n; i++) jobs[i] = new Job(startTime[i], endTime[i], profit[i]);

        Arrays.sort(jobs, Comparator.comparingInt(j -> j.e));

        int[] ends = new int[n];
        for (int i = 0; i < n; i++) ends[i] = jobs[i].e;

        int[] dp = new int[n];
        for (int i = 0; i < n; i++) {
            int include = jobs[i].p;
            // find last non-conflicting job index p(i)
            int idx = lastNonConflicting(ends, jobs[i].s);
            if (idx != -1) include += dp[idx];

            int exclude = (i > 0) ? dp[i - 1] : 0;
            dp[i] = Math.max(exclude, include);
        }
        return dp[n - 1];
    }

    // returns the largest index j such that ends[j] <= start (or -1 if none)
    private int lastNonConflicting(int[] ends, int start) {
        int lo = 0, hi = ends.length - 1, ans = -1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (ends[mid] <= start) {
                ans = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }

    // Example
    public static void main(String[] args) {
        MaxProfitJobScheduling solver = new MaxProfitJobScheduling();
        System.out.println(solver.jobScheduling(
            new int[]{1,2,3,3}, new int[]{3,4,5,6}, new int[]{50,10,40,70})); // 120
        System.out.println(solver.jobScheduling(
            new int[]{1,1,1}, new int[]{2,3,4}, new int[]{5,6,4})); // 6
    }
}
```

------------------------------------------------------------------------

## 💡 3B. Top-Down (Memo + Binary Search)

```java
import java.util.*;

class MaxProfitJobSchedulingMemo {
    static class Job {
        int s, e, p;
        Job(int s, int e, int p) { this.s = s; this.e = e; this.p = p; }
    }

    private Job[] jobs;
    private int[] memo;
    private int[] starts; // start times in sorted-by-end order
    private int[] ends;

    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        jobs = new Job[n];
        for (int i = 0; i < n; i++) jobs[i] = new Job(startTime[i], endTime[i], profit[i]);
        Arrays.sort(jobs, Comparator.comparingInt(j -> j.e));

        ends = new int[n];
        starts = new int[n];
        for (int i = 0; i < n; i++) {
            ends[i] = jobs[i].e;
            starts[i] = jobs[i].s;
        }

        memo = new int[n];
        Arrays.fill(memo, -1);
        return dfs(n - 1);
    }

    private int dfs(int i) {
        if (i < 0) return 0;
        if (memo[i] != -1) return memo[i];

        // exclude i
        int best = dfs(i - 1);

        // include i + best up to p(i)
        int idx = lastNonConflicting(ends, jobs[i].s);
        best = Math.max(best, jobs[i].p + (idx >= 0 ? dfs(idx) : 0));

        return memo[i] = best;
    }

    private int lastNonConflicting(int[] ends, int start) {
        int lo = 0, hi = ends.length - 1, ans = -1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            if (ends[mid] <= start) {
                ans = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return ans;
    }
}
```

------------------------------------------------------------------------

## 🔎 4. Dry Run Sketch

start = [1,2,3,3], end = [3,4,5,6], profit = [50,10,40,70]
- Sort by end → already sorted
- i=0: dp[0] = max(0, 50) = 50
- i=1: p(1)=0 (end[0]=3<=start[1]=2? no) → p(1)=-1 → dp[1] = max(dp[0]=50, 10) = 50
- i=2: p(2)=0 (3<=3) → include = 40 + dp[0]=90 → dp[2] = max(dp[1]=50, 90) = 90
- i=3: p(3)=2 (5<=3? no; try lower; actually 5<=3 false; 5<=3? re-eval with binary search:
      Need end <= start(3). ends=[3,4,5,6], start=3 → p=0) include=70+50=120
      dp[3]=max(dp[2]=90, 120)=120

Answer = 120.

------------------------------------------------------------------------

## 🏷 5. Pattern Recognition

- Name: Weighted Interval Scheduling
- Family: DP with binary search (after sorting by end)
- Triggers:
  - Select non-overlapping intervals with maximum weight
  - “Take or skip current” with predecessor lookup

------------------------------------------------------------------------

## 🔄 6. Edge Cases and Pitfalls

- Multiple jobs sharing same end times → still fine after sort
- Large n (≤ 10^5 typical) → prefer O(n log n) approach; avoid O(n^2)
- Definition of compatibility: end <= start (non-overlap); verify equals allowed per problem
- Use iterative bottom-up or top-down with memo; both O(n log n)

------------------------------------------------------------------------

## ✅ 7. Takeaway

- Sort by end time, precompute p(i) via binary search on ends, and apply dp[i] = max(dp[i-1], profit[i] + dp[p(i)]).
- Canonical weighted interval scheduling solution with clean O(n log n) complexity.

------------------------------------------------------------------------

# End of Notes
