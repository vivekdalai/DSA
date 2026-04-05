# Greedy Notes

## Dota Senate

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given a string `senate` made of:

- `R` for Radiant
- `D` for Dire

Each senator can ban one senator from the opposite party when it is their turn.

The process continues in rounds until only one party remains.

Return the winning party.

**Example 1**

    Input: senate = "RD"
    Output: "Radiant"

**Example 2**

    Input: senate = "RDD"
    Output: "Dire"

------------------------------------------------------------------------

## 2. Pending Ban Intuition

The file models the process with:

- a queue of active senators
- `killR` = pending bans against future `R`
- `killD` = pending bans against future `D`

When a senator acts:

- if no pending ban targets them, they survive, issue one ban on the opposite party, and go back to the queue
- otherwise they are removed by consuming one pending ban

------------------------------------------------------------------------

## 3. Why This Simulation Works

The queue preserves round order.

The pending ban counters summarize all earlier surviving senators that are waiting to eliminate future opponents.

So each senator only needs to know:

- do I die now?
- or do I survive and create one future ban?

That is enough to simulate the entire game.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    senate = "RDD"

Queue starts as:

    R, D, D

`R` acts first:

- survives
- issues one ban on `D`

Next `D` arrives:

- gets removed by that pending ban

Last `D` survives, bans `R`, and eventually Dire wins.

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static String predictPartyVictory(String senate) {
    int cntD = 0, cntR = 0, killD = 0, killR = 0;

    for (char c : senate.toCharArray()) {
        if (c == 'R') cntR++;
        if (c == 'D') cntD++;
    }

    Queue<Character> queue = new LinkedList<>();
    for (char c : senate.toCharArray()) queue.add(c);

    while (!queue.isEmpty()) {
        char curr = queue.poll();

        if (curr == 'R') {
            if (killR == 0) {
                killD++;
                cntD--;
                if (cntD == 0) return "Radiant";
                queue.add(curr);
            } else {
                killR--;
            }
        } else {
            if (killD == 0) {
                killR++;
                cntR--;
                if (cntR == 0) return "Dire";
                queue.add(curr);
            } else {
                killD--;
            }
        }
    }

    return "";
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)` amortized over the simulation
- **Space:** `O(n)`

Pattern:

- queue-driven round simulation
- delayed effects stored as pending bans

------------------------------------------------------------------------

## End of Notes
