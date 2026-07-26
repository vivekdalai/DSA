# Topological Sort Notes

## Topological Sort / Course Schedule

**Generated on:** 2026-07-05 IST

------------------------------------------------------------------------

## 1. What is Topological Sort?

Topological sort is an ordering of nodes in a directed graph such that every dependency comes before the thing that depends on it.

If there is an edge:

```text
A -> B
```

It means:

```text
A must come before B
```

In the course schedule problem:

```text
prerequisites[i] = [course, prerequisite]
```

Example:

```text
[1, 0]
```

This means:

```text
To take course 1, first complete course 0.
```

So the graph edge is:

```text
0 -> 1
```

------------------------------------------------------------------------

## 2. When is Topological Sort Possible?

Topological sort is possible only for a Directed Acyclic Graph.

Directed:

- edges have direction
- `0 -> 1` is different from `1 -> 0`

Acyclic:

- there must be no cycle

If there is a cycle:

```text
0 -> 1 -> 2 -> 0
```

Then no valid ordering exists.

Why?

- `0` needs `2`
- `2` needs `1`
- `1` needs `0`

Everyone is waiting for someone else, so we can never start.

------------------------------------------------------------------------

## 3. First Intuition

Think of courses as tasks with prerequisites.

You can start with courses that have no pending prerequisites.

After completing one course, it may unlock other courses.

So the process is:

- find all courses with no prerequisites
- take them first
- remove their effect from the graph
- when a new course has no remaining prerequisites, take it
- continue until no more courses can be taken

If all courses are taken, we found a valid order.

If some courses are still left, they are stuck in a cycle.

------------------------------------------------------------------------

## 4. Indegree Meaning

`indegree` means:

```text
How many incoming edges does this node have?
```

In this problem:

```text
indegree[course] = number of prerequisites still needed
```

Example:

```text
numCourses = 4
prerequisites = [[1, 0], [2, 0], [3, 1], [3, 2]]
```

Edges:

```text
0 -> 1
0 -> 2
1 -> 3
2 -> 3
```

Graph:

```text
    0
   / \
  v   v
  1   2
   \ /
    v
    3
```

Indegree:

```text
course 0: 0 prerequisites
course 1: 1 prerequisite  -> 0
course 2: 1 prerequisite  -> 0
course 3: 2 prerequisites -> 1 and 2
```

So:

```text
indegree = [0, 1, 1, 2]
```

Course `0` can be taken first because its indegree is `0`.

------------------------------------------------------------------------

## 5. Current Code

Current method:

```java
public int[] findOrder(int numCourses, int[][] prerequisites) {

    Map<Integer, List<Integer>> adjList = new HashMap<>();
    for(int i = 0; i < numCourses; i++){
        adjList.put(i, new ArrayList<>());
    }

    int[] indegree = new int[numCourses];
    for(int[] pair : prerequisites){
        adjList.get(pair[1]).add(pair[0]);
        indegree[pair[0]]++;
    }

    Deque<Integer> queue = new ArrayDeque<>();
    for(int i = 0; i < numCourses; i++){
        if(indegree[i] == 0)
            queue.addLast(i);
    }

    int completed = 0;
    int idx = 0;
    int[] orderOfCourses = new int[numCourses];
    while(!queue.isEmpty()){
        int curr = queue.pollFirst();
        orderOfCourses[idx++] = curr;
        completed++;
        for(int nei : adjList.get(curr)){
            indegree[nei]--;
            if(indegree[nei] == 0){
                queue.add(nei);
            }
        }
    }

    return completed == numCourses ? orderOfCourses : new int[0];
}
```

This is Kahn's Algorithm for topological sorting.

------------------------------------------------------------------------

## 6. Building the Graph

Code:

```java
Map<Integer, List<Integer>> adjList = new HashMap<>();
for(int i = 0; i < numCourses; i++){
    adjList.put(i, new ArrayList<>());
}
```

Methodology:

- Create an empty adjacency list for every course.
- Even courses with no outgoing edges should be present.
- This avoids null checks later when reading neighbors.

For `numCourses = 4`:

```text
0 -> []
1 -> []
2 -> []
3 -> []
```

Then:

```java
for(int[] pair : prerequisites){
    adjList.get(pair[1]).add(pair[0]);
    indegree[pair[0]]++;
}
```

For each pair:

```text
[course, prerequisite]
```

We add:

```text
prerequisite -> course
```

Because the prerequisite must be completed before the course.

Example:

```text
[3, 1]
```

Means:

```text
1 -> 3
```

And because course `3` now has one more prerequisite:

```text
indegree[3]++
```

------------------------------------------------------------------------

## 7. Queue Initialization

Code:

```java
Deque<Integer> queue = new ArrayDeque<>();
for(int i = 0; i < numCourses; i++){
    if(indegree[i] == 0)
        queue.addLast(i);
}
```

Methodology:

- Add all courses with `indegree = 0`.
- These courses have no pending prerequisites.
- They are safe starting points.

If:

```text
indegree = [0, 1, 1, 2]
```

Then:

```text
queue = [0]
```

Course `0` can be completed immediately.

------------------------------------------------------------------------

## 8. Processing the Queue

Code:

```java
while(!queue.isEmpty()){
    int curr = queue.pollFirst();
    orderOfCourses[idx++] = curr;
    completed++;

    for(int nei : adjList.get(curr)){
        indegree[nei]--;
        if(indegree[nei] == 0){
            queue.add(nei);
        }
    }
}
```

Methodology:

- Remove one available course from the queue.
- Put it into the final order.
- Count it as completed.
- Visit all courses unlocked by this course.
- Reduce their indegree because one prerequisite is now completed.
- If any neighbor's indegree becomes `0`, add it to the queue.

The key idea:

```text
Completing a node removes its outgoing edges.
```

When all incoming edges of a neighbor are removed, that neighbor becomes available.

------------------------------------------------------------------------

## 9. Dry Run

Input:

```text
numCourses = 4
prerequisites = [[1, 0], [2, 0], [3, 1], [3, 2]]
```

Graph:

```text
0 -> [1, 2]
1 -> [3]
2 -> [3]
3 -> []
```

Indegree:

```text
[0, 1, 1, 2]
```

Initial queue:

```text
[0]
```

Step 1:

```text
take 0
order = [0]

0 unlocks 1 and 2
indegree[1] becomes 0 -> add 1
indegree[2] becomes 0 -> add 2

queue = [1, 2]
```

Step 2:

```text
take 1
order = [0, 1]

1 unlocks 3
indegree[3] becomes 1

queue = [2]
```

Course `3` is not ready yet because course `2` is still pending.

Step 3:

```text
take 2
order = [0, 1, 2]

2 unlocks 3
indegree[3] becomes 0 -> add 3

queue = [3]
```

Step 4:

```text
take 3
order = [0, 1, 2, 3]

3 unlocks nobody

queue = []
```

All courses are completed.

Result:

```text
[0, 1, 2, 3]
```

Another valid result could be:

```text
[0, 2, 1, 3]
```

Both are correct because `1` and `2` can be taken in any order after `0`.

------------------------------------------------------------------------

## 10. Cycle Detection

The code does not explicitly search for a cycle.

Instead, it detects a cycle by counting how many courses were completed.

Code:

```java
return completed == numCourses ? orderOfCourses : new int[0];
```

Example:

```text
numCourses = 2
prerequisites = [[0, 1], [1, 0]]
```

Edges:

```text
1 -> 0
0 -> 1
```

Indegree:

```text
[1, 1]
```

Initial queue:

```text
[]
```

No course has indegree `0`, so we cannot start.

Completed:

```text
0
```

Because:

```text
completed != numCourses
```

Return:

```text
[]
```

This means no valid course order exists.

------------------------------------------------------------------------

## 11. Why This Works

At any point, a node with indegree `0` has no unresolved dependency.

So it is always safe to place it next in the answer.

After placing it in the answer, we simulate removing it from the graph.

That may reduce the indegree of its neighbors.

If the graph has no cycle, eventually every node becomes indegree `0` and enters the queue.

If the graph has a cycle, nodes inside the cycle never become indegree `0`.

So the algorithm gets stuck before completing all nodes.

------------------------------------------------------------------------

## 12. Important Direction Detail

For course schedule problems, this line is very important:

```java
adjList.get(pair[1]).add(pair[0]);
```

Given:

```text
pair = [course, prerequisite]
```

The edge must be:

```text
prerequisite -> course
```

So:

```text
pair[1] -> pair[0]
```

If this direction is reversed, the generated ordering will be wrong for this problem.

------------------------------------------------------------------------

## 13. Clean Interview Version

```java
import java.util.*;

class Solution {
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }

        int[] indegree = new int[numCourses];

        for (int[] pre : prerequisites) {
            int course = pre[0];
            int prerequisite = pre[1];

            graph.get(prerequisite).add(course);
            indegree[course]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int course = 0; course < numCourses; course++) {
            if (indegree[course] == 0) {
                queue.offer(course);
            }
        }

        int[] order = new int[numCourses];
        int index = 0;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            order[index++] = current;

            for (int next : graph.get(current)) {
                indegree[next]--;
                if (indegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        return index == numCourses ? order : new int[0];
    }
}
```

This version uses `List<List<Integer>>` because course numbers are from `0` to `numCourses - 1`.

So an array/list based graph is enough.

------------------------------------------------------------------------

## 14. Complexity

Let:

- `V` = number of courses
- `E` = number of prerequisite pairs

Complexities:

- Build graph: `O(V + E)`
- Queue processing: `O(V + E)`
- Total time: `O(V + E)`
- Space: `O(V + E)`

Why queue processing is `O(V + E)`:

- each course enters the queue at most once
- each edge is processed once when its source course is completed

------------------------------------------------------------------------

## 15. Pattern Recognition and Revision Notes

- Use topological sort when the problem talks about ordering with dependencies.
- Common keywords: prerequisites, dependency order, build order, task scheduling, before/after constraints.
- Use Kahn's Algorithm when you want an iterative BFS-like approach.
- `indegree = 0` means the node is currently available.
- Reducing indegree means one dependency has been completed.
- If completed nodes are fewer than total nodes, there is a cycle.
- For Course Schedule II, return the order if possible, else return an empty array.
- Multiple valid topological orders can exist.

------------------------------------------------------------------------

## End of Notes
