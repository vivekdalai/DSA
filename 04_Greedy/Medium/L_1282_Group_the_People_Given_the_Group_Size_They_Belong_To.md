# Greedy Notes

## Group the People Given the Group Size They Belong To

**Generated on:** 2026-04-06 01:20:58 IST

------------------------------------------------------------------------

## 1. LeetCode Question Statement

You are given an array `groupSizes`, where `groupSizes[i]` is the required group size for person `i`.

Return a grouping of people such that every person `i` belongs to a group of size `groupSizes[i]`.

Any valid answer is accepted.

**Example 1**

    Input: groupSizes = [3,3,3,3,3,1,3]
    Output: [[5],[0,1,2],[3,4,6]]

**Example 2**

    Input: groupSizes = [2,1,3,3,3,2]
    Output: [[1],[0,5],[2,3,4]]

------------------------------------------------------------------------

## 2. Main Intuition

People with different required sizes can never mix.

So the problem becomes:

- gather indices by required group size
- cut each bucket into chunks of that size

That is exactly what the map-based solution does.

------------------------------------------------------------------------

## 3. Two Styles In The File

### Bucket then slice

- map each size to all indices wanting that size
- split each stored list into groups of length `size`

### Streaming version

- keep building the current group for each size
- once that temporary group reaches the required size, push it to the answer and reset it

The second version is cleaner for revision.

------------------------------------------------------------------------

## 4. Walkthrough

**Input:**

    groupSizes = [3,3,3,3,3,1,3]

Buckets:

- size `1` -> `[5]`
- size `3` -> `[0,1,2,3,4,6]`

Now split the size-`3` bucket into chunks of `3`:

    [0,1,2]
    [3,4,6]

Final answer:

    [[5],[0,1,2],[3,4,6]]

------------------------------------------------------------------------

## 5. Clean Interview Version

```java
public static List<List<Integer>> groupThePeople_v2(int[] groupSizes) {
    Map<Integer, List<Integer>> map = new HashMap<>();
    List<List<Integer>> ans = new ArrayList<>();

    for (int i = 0; i < groupSizes.length; i++) {
        int size = groupSizes[i];
        map.putIfAbsent(size, new ArrayList<>());

        List<Integer> group = map.get(size);
        group.add(i);

        if (group.size() == size) {
            ans.add(group);
            map.put(size, new ArrayList<>());
        }
    }

    return ans;
}
```

------------------------------------------------------------------------

## 6. Complexity And Pattern

- **Time:** `O(n)`
- **Space:** `O(n)`

Pattern:

- bucket by required size
- flush a bucket whenever it reaches capacity

------------------------------------------------------------------------

## End of Notes
