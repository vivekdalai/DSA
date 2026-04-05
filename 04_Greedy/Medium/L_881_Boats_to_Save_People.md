# Greedy Notes

## Boats to Save People

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given an array `people`, where `people[i]` is the weight of the `i`th person, and an integer `limit`.

Each boat can carry at most `2` people with total weight at most `limit`.

Return the minimum number of boats required.

**Example 1**

    Input: people = [1,2], limit = 3
    Output: 1

**Example 2**

    Input: people = [3,2,2,1], limit = 3
    Output: 3

------------------------------------------------------------------------

## 2. Two-Pointer Greedy

Sort the people by weight.

Then always place the heaviest remaining person on a boat.

If the lightest remaining person can share that boat, pair them.

Otherwise, the heaviest person must go alone.

This is optimal because the heaviest person has the fewest pairing options.

------------------------------------------------------------------------

## 3. Pointer Rule

After sorting:

- `l` points to the lightest remaining person
- `r` points to the heaviest remaining person

At each step:

- if `people[l] + people[r] <= limit`, move `l++`
- always move `r--`
- increment boat count

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    people = [3,2,2,1], limit = 3

Sorted:

    [1,2,2,3]

Process:

- `3` goes alone
- `2` goes alone
- `1` pairs with `2`

Total boats:

    3

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public int numRescueBoats(int[] people, int limit) {
    Arrays.sort(people);

    int l = 0, r = people.length - 1, count = 0;

    while (l <= r) {
        if (people[l] + people[r] <= limit) {
            l++;
        }

        r--;
        count++;
    }

    return count;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n log n)`
- **Space:** `O(1)` extra, ignoring sort space

Pattern:

- sort
- force the heaviest item first, then try to pair it with the cheapest compatible item

------------------------------------------------------------------------

## End of Notes
