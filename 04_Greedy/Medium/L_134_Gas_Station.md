# Greedy Notes

## 01 - Gas Station

**Generated on:** 2026-04-06 00:33:30

------------------------------------------------------------------------

## 📘 1. LeetCode Question Statement

There are `n` gas stations along a **circular route**, where `gas[i]` is the amount of gas available at station `i`.

You have a car with an **unlimited tank**, and it costs `cost[i]` gas to travel from station `i` to station `i + 1`.

You start with an **empty tank** at one of the stations.

Return the **starting gas station index** if you can travel around the circuit exactly once in the clockwise direction.

If it is not possible, return `-1`.

It is guaranteed that if a valid answer exists, it is **unique**.

**Example 1**

    Input: gas = [1,2,3,4,5], cost = [3,4,5,1,2]
    Output: 3

**Example 2**

    Input: gas = [2,3,4], cost = [3,4,3]
    Output: -1

**Constraints:**

- `n == gas.length == cost.length`
- `1 <= n <= 10^5`
- `0 <= gas[i], cost[i] <= 10^4`

------------------------------------------------------------------------

## 🧠 2. Problem Understanding

There are `n` gas stations in a **circular route**.

- `gas[i]` = fuel available at station `i`
- `cost[i]` = fuel needed to go from station `i` to station `i + 1`

You start with an **empty tank**.

Goal:

- Find the **starting station index** from where you can complete the full circle
- If impossible, return `-1`

This is a **Greedy** problem.

Core Observation:

- If total gas `<` total cost, answer is always `-1`
- If starting from some station fails at index `i`, then **any station between that start and `i` also fails**

------------------------------------------------------------------------

## 🪜 3. State Definition

We do not need DP here.

We only track 3 running values:

    tank = current fuel while simulating from current start
    totalGain = total gas - total cost over the whole route
    start = current candidate starting station

Meaning:

- `tank` tells whether current start is still valid
- `totalGain` tells whether any valid solution exists
- `start` stores the answer candidate

------------------------------------------------------------------------

## 🔁 4. Greedy Transition / Logic

For every station:

    currGain = gas[i] - cost[i]
    totalGain += currGain
    tank += currGain

If:

    tank < 0

That means current start cannot reach next stations.

So:

    start = i + 1
    tank = 0

Why reset?

- Because all stations from old `start` to `i` are impossible as starting points
- So the next possible candidate must be `i + 1`

------------------------------------------------------------------------

## 🧱 5. Final Decision

After full traversal:

    if totalGain >= 0 -> return start
    else return -1

Why?

- `totalGain < 0` means total fuel in the whole circle is insufficient
- `totalGain >= 0` means one valid answer exists, and greedy has already found it

------------------------------------------------------------------------

## 📦 6. Why This Works

The main greedy invariant is:

- As long as `tank >= 0`, current `start` is still possible
- Once `tank < 0`, current segment is proven invalid

Important intuition from your code:

- `totalGain` checks global feasibility
- `tank` checks local feasibility
- `start` is updated only when local feasibility breaks

### Complexity

- **Time:** O(N)
- **Space:** O(1)

------------------------------------------------------------------------

## 💻 7. Clean Interview Version (Optimal Code)

```java
public int canCompleteCircuit(int[] gas, int[] cost) {
    int start = 0;
    int tank = 0;
    int totalGain = 0;

    for (int i = 0; i < gas.length; i++) {
        int currGain = gas[i] - cost[i];
        totalGain += currGain;
        tank += currGain;

        if (tank < 0) {
            start = i + 1;
            tank = 0;
        }
    }

    return totalGain >= 0 ? start : -1;
}
```

------------------------------------------------------------------------

## 🔎 8. Full Dry Run Example

**Input:**

    gas  = [1, 2, 3, 4, 5]
    cost = [3, 4, 5, 1, 2]

Step-by-step:

  Index   gas[i]   cost[i]   currGain   tank(after)   totalGain   start
  ------- -------- --------- ---------- ------------- ----------- -----
  0       1        3         -2         -2 -> reset   -2          1
  1       2        4         -2         -2 -> reset   -4          2
  2       3        5         -2         -2 -> reset   -6          3
  3       4        1          3          3            -3          3
  4       5        2          3          6             0          3

**Final Answer:** `3`

Why station `3` works:

- Start from index `3`
- You never let fuel go negative while completing the circle

------------------------------------------------------------------------

## 🏷 9. Pattern Recognition

**Pattern Name:** Greedy Reset on Failure
**Family:** Greedy / Circular Traversal

Whenever you see:

- Circular route
- Gain/loss at each index
- Need one valid starting point
- Failure at one range eliminates many candidates

👉 Think greedy reset + total feasibility check.

------------------------------------------------------------------------

## 🔄 10. Common Variations

1. **Return whether completion is possible**
   → Only check `totalGain >= 0`

2. **Find start in similar gain/loss circular problems**
   → Track running balance and reset candidate when it becomes negative

3. **No uniqueness guarantee**
   → There may be multiple valid starts, but the same greedy feasibility idea still helps

------------------------------------------------------------------------

## End of Notes
