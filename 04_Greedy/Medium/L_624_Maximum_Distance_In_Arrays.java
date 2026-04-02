package leetcode;

import java.util.List;

public class L_624_Maximum_Distance_In_Arrays {
    public int maxDistance(List<List<Integer>> arrays) {

        int n=arrays.size();
        int currMax = arrays.get(0).get(arrays.get(0).size()-1);
        int currMin = arrays.get(0).get(0);
        int diff = Integer.MIN_VALUE;
        for(int i=1;i<n;i++){
            List<Integer> currList = arrays.get(i);
            int currListSize = currList.size();

            diff = Math.max(
                    Math.max(
                            diff,Math.abs(currList.get(0)-currMax)
                    ),
                    Math.abs(currList.get(currListSize-1)-currMin)
            );

            currMax = Math.max(currMax, currList.get(currListSize-1));
            currMin = Math.min(currMin, currList.get(0));


        }

        return diff;

    }
}
