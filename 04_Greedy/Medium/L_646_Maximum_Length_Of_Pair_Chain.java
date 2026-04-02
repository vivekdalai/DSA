package leetcode;

import java.util.Arrays;
import java.util.Comparator;

public class L_646_Maximum_Length_Of_Pair_Chain {
    public int findLongestChain(int[][] pairs) {

        Arrays.sort(pairs, Comparator.comparingInt(c->c[0]));

        int[] prev = pairs[0];
        int ans=1;
        for(int i=1;i<pairs.length;i++){
            if(pairs[i][0] > prev[1]){
                ans++;
                prev=pairs[i];
            }
        }

        return ans;

    }
}
