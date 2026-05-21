# Binary Search Notes

## 07 - Allocate Minimum Pages

**Generated on:** 2026-05-20 IST

------------------------------------------------------------------------

## 1. Problem Understanding

Given `n` books where `arr[i]` is the number of pages in book `i`, allocate books to `m` students such that:

- each student gets a contiguous group of books
- every book is allocated
- the maximum pages assigned to any student is minimized

If `m > n`, allocation is impossible.

Example:

```text
Input: arr = [12,34,67,90], m = 2
Output: 113
```

One optimal allocation is `[12,34,67]` and `[90]`, so max pages is `113`.

------------------------------------------------------------------------

## 2. First Intuition

This is a "minimize the maximum" problem.

Instead of trying all partitions, ask:

Can all books be allocated if no student gets more than `X` pages?

If yes, maybe we can lower `X`.
If no, `X` is too small.

That yes/no behavior is monotonic, so binary search works.

------------------------------------------------------------------------

## 3. Methodology

Search space:

- `low = max(arr)` because one student must take the largest book
- `high = sum(arr)` because one student can take all books

Check function:

- Traverse books from left to right.
- Keep assigning to current student while page sum stays `<= maxPages`.
- If adding a book exceeds `maxPages`, start a new student.
- Count how many students are required.

If required students `<= m`, the limit is possible.

------------------------------------------------------------------------

## 4. Clean Interview Version

```java
import java.util.*;

class Solution {
    public static int findPages(ArrayList<Integer> arr, int n, int m) {
        if (m > n) {
            return -1;
        }

        int low = 0;
        int high = 0;

        for (int pages : arr) {
            low = Math.max(low, pages);
            high += pages;
        }

        int ans = high;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int students = studentsRequired(arr, mid);

            if (students <= m) {
                ans = mid;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        return ans;
    }

    private static int studentsRequired(ArrayList<Integer> arr, int maxPages) {
        int students = 1;
        int currentPages = 0;

        for (int pages : arr) {
            if (currentPages + pages > maxPages) {
                students++;
                currentPages = pages;
            } else {
                currentPages += pages;
            }
        }

        return students;
    }
}
```

------------------------------------------------------------------------

## 5. Short Dry Run

For `arr = [12,34,67,90]`, `m = 2`:

- `low = 90`, `high = 203`
- Try `146`: possible with 2 students, try smaller
- Try `117`: possible with 2 students, try smaller
- Try `103`: needs 3 students, too small
- Best limit becomes `113`

------------------------------------------------------------------------

## 6. Complexity

- Time: `O(n log sum)`
- Space: `O(1)`

------------------------------------------------------------------------

## 7. Pattern Recognition and Revision Notes

- This is binary search on answer.
- Trigger words: minimize maximum, split array, allocate pages, capacity.
- The check function answers: "How many groups are needed if each group has capacity X?"

------------------------------------------------------------------------

## End of Notes
