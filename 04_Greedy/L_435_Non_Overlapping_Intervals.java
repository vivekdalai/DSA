package leetcode;

import java.util.Arrays;

public class L_435_Non_Overlapping_Intervals {
    public int eraseOverlapIntervals(int[][] intervals) {

        Arrays.sort(intervals,(a,b)->a[1]-b[1]);

        int[] prevInterval = intervals[0];
        int skip=0;
        for(int i=1;i<intervals.length;i++){

            if(intervals[i][0]<prevInterval[1]){
                skip++;
            }else {
                prevInterval=intervals[i];
            }
        }
        return skip;
    }

}
