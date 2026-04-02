package leetcode;

public class L_45_Jump_Game_II {
    public int jump(int[] nums) {
        int jumps=0,left=0,right=0;
        int n = nums.length;
        while(right<n-1){

            int farthest = 0;

            for(int i=left;i<=right;i++){
                farthest = Math.max(farthest,i+ nums[i]);
            }

            left=right+1;
            right=farthest;
            jumps++;

        }

        return jumps;
    }
}
