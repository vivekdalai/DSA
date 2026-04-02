package leetcode;

public class L_334_Increasing_Triplet_Subsequence {
    public boolean increasingTriplet(int[] nums) {
        int f = Integer.MAX_VALUE;
        int s = Integer.MAX_VALUE;


        for(int curr : nums){
            if(curr<=f) f=curr;
            else if (curr<=s) s=curr;
            else return true;
        }

        return false;
    }
}
