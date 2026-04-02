package leetcode;

import java.lang.reflect.Array;
import java.util.Arrays;

public class L_452_Minimum_Number_Of_Arrows_To_Burst_Balloons {
    public static int findMinArrowShots(int[][] points) {
        Arrays.sort(points,(a,b)-> Integer.compare(a[1], b[1]));

//        for (int[] row : points) {
//            System.out.println(Arrays.toString(row));
//        }

        int ans=1;
        for(int i=1;i<points.length;i++){
            if(points[i][0]<points[i-1][1]){
                int maxStart = Math.max(points[i][0],points[i-1][0]);
                int minEnd = Math.min(points[i][1],points[i-1][1]);

                points[i][0] = maxStart;
                points[i][1] = minEnd;
            } else{
                ans++;
            }
        }
        return ans;

    }
}
