package leetcode;

public class L_55_Jump_Game {
    public boolean canJump(int[] nums) {
        int maxReach = 0;

        for(int i=0;i<nums.length;i++){

            if(i>maxReach) return false;
            maxReach = Math.max(maxReach,i+nums[i]);
        }
        return true;
    }
}
