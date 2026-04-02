package leetcode;

import java.util.HashMap;
import java.util.Map;

public class L_659_Split_Array_into_Consecutive_Subsequences {
    public static boolean isPossible(int[] nums) {


        Map<Integer,Integer> freq = new HashMap<>();
        Map<Integer,Integer> vMap = new HashMap<>(); // vacancy map


        for(int x:nums){
            freq.put(x, freq.getOrDefault(x,0)+1);
        }

        for(int x:nums){

            if(freq.getOrDefault(x,0)==0) continue;

            freq.put(x, freq.getOrDefault(x,0)-1);

            if(vMap.getOrDefault(x,0)>0) { //can be added into existing subsequence

                vMap.put(x,vMap.getOrDefault(x,0)-1);
                vMap.put(x+1,vMap.getOrDefault(x+1,0)+1);

            } else if(freq.getOrDefault(x+1,0)>0 && freq.getOrDefault(x+2,0)>0){ // new sequence can be generated

                freq.put(x+1, freq.getOrDefault(x+1,0)-1);
                freq.put(x+2, freq.getOrDefault(x+2,0)-1);
                vMap.put(x+3,vMap.getOrDefault(x+3,0)+1);
            } else {
                return false;
            }
        }

        return true;

    }
}
