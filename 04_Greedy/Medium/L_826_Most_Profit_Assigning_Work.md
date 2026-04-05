# Greedy Notes

## Most Profit Assigning Work

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------


<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/most-profit-assigning-work/description/
<!-- leetcode-link-end -->
## 1. LeetCode Question Statement

You are given:

- `difficulty[i]` = difficulty of job `i`
- `profit[i]` = profit of job `i`
- `worker[j]` = ability of worker `j`

A worker can do any job whose difficulty is at most their ability.

Each worker may complete at most one job, but jobs can be repeated by multiple workers.

Return the maximum total profit.

**Example 1**

    Input: difficulty = [2,4,6,8,10], profit = [10,20,30,40,50], worker = [4,5,6,7]
    Output: 100

**Example 2**

    Input: difficulty = [13,37,58], profit = [4,90,96], worker = [34,73,45]
    Output: 190

------------------------------------------------------------------------

## 2. Sweep Line Intuition

Sort jobs by difficulty and workers by ability.

As worker ability increases, more jobs become available.

So while sweeping workers from weakest to strongest, we only need one running value:

    currMaxProfit = best profit among jobs this worker can do

------------------------------------------------------------------------

## 3. One Pass After Sorting

Build pairs:

    [difficulty, profit]

Sort jobs by difficulty.

Sort workers.

Then for each worker `w`:

- include every job with `difficulty <= w`
- update `currMaxProfit`
- add `currMaxProfit` to the answer

This works because stronger workers can inherit everything weaker workers could do.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    difficulty = [2,4,6,8,10]
    profit = [10,20,30,40,50]
    worker = [4,5,6,7]

Sorted workers:

    [4,5,6,7]

Best profits they can access:

- ability `4` -> `20`
- ability `5` -> `20`
- ability `6` -> `30`
- ability `7` -> `30`

Total:

    100

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int maxProfitAssignment(int[] difficulty, int[] profit, int[] worker) {
    int n = difficulty.length;
    int[][] jobs = new int[n][2];

    for (int i = 0; i < n; i++) {
        jobs[i][0] = difficulty[i];
        jobs[i][1] = profit[i];
    }

    Arrays.sort(jobs, (a, b) -> a[0] - b[0]);
    Arrays.sort(worker);

    int i = 0, currMaxProfit = 0, maxProfit = 0;
    for (int w : worker) {
        while (i < n && jobs[i][0] <= w) {
            currMaxProfit = Math.max(currMaxProfit, jobs[i][1]);
            i++;
        }
        maxProfit += currMaxProfit;
    }

    return maxProfit;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n + m log m)`
- **Space:** `O(n)`

Pattern:

- sort jobs and queries
- sweep once while maintaining the best available reward

------------------------------------------------------------------------

## End of Notes
