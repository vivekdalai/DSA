package leetcode;

import java.util.Arrays;

public class L_1024_Video_Stitching {
    public int videoStitching(int[][] clips, int time) {
        Arrays.sort(clips,(a,b)->a[0]-b[0]);

        int currEnd = 0, farthest = 0, i = 0, count = 0, n = clips.length;

        while (currEnd < time) {
            while (i < n && clips[i][0] <= currEnd) {
                farthest = Math.max(clips[i][1], farthest);
                i++;
            }

            if (currEnd == farthest) return -1;

            currEnd = farthest;
            count++;
        }
        return count;
    }
}

