# Binary Search Notes

## 06 - Aggressive Cows / Magnetic Force Between Balls

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

<!-- leetcode-link-start -->
**LeetCode Link:** https://leetcode.com/problems/magnetic-force-between-two-balls/description/
<!-- leetcode-link-end -->

## 1. Problem Understanding

Given positions of baskets/stalls and `m` balls/cows, place them so that the minimum distance between any two placed items is as large as possible.

Example:

```text
Input: position = [1,2,3,4,7], m = 3
Output: 3
```

One valid placement is `[1,4,7]`, where the minimum distance is `3`.

------------------------------------------------------------------------

## 2. First Intuition

This is a "maximize the minimum" problem.

Instead of directly constructing the best placement, ask:

Can I place all `m` balls if every pair of neighboring placed balls is at least `d` apart?

If yes, try a bigger distance.
If no, try a smaller distance.

That yes/no answer is monotonic, so binary search works.

------------------------------------------------------------------------

## 3. Methodology

1. Sort positions.
2. Binary search the answer distance:
   - smallest possible distance: `1`
   - largest possible distance: `max(position) - min(position)`
3. For a chosen distance `d`, greedily place balls:
   - place first ball at the first position
   - place next ball whenever current position is at least `d` away from last placed position
4. If we can place at least `m`, distance `d` is possible.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
import java.util.*;

class Solution {
    public int maxDistance(int[] position, int m) {
        Arrays.sort(position);

        int low = 1;
        int high = position[position.length - 1] - position[0];
        int ans = 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (canPlace(position, m, mid)) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return ans;
    }

    private boolean canPlace(int[] position, int requiredBalls, int minDistance) {
        int placed = 1;
        int lastPosition = position[0];

        for (int i = 1; i < position.length; i++) {
            if (position[i] - lastPosition >= minDistance) {
                placed++;
                lastPosition = position[i];
            }
        }

        return placed >= requiredBalls;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `position = [1,2,3,4,7]`, `m = 3`:

- Try distance `3`: place at `1`, `4`, `7`, possible.
- Since `3` works, try larger.
- Try distance `4`: place at `1`, `7`, only 2 balls, not possible.
- Best possible distance is `3`.

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n log n + n log range)`
- Space: `O(1)` apart from sorting

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This belongs to binary search on answer.
- Trigger words: maximize minimum, largest minimum distance, aggressive cows, magnetic force.
- The check function should be greedy and monotonic.

------------------------------------------------------------------------

## End of Notes
