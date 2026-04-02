package leetcode;

public class L_376_Wiggle_Subsequence {
    public static int wiggleMaxLength(int[] nums) {
        int n=nums.length;
        if(n<2) return n;

        int prevDiff = nums[1]-nums[0];

        int ans = prevDiff!=0?2:1;
        int diff;
        for(int i=2;i<n;i++){
            diff = nums[i]-nums[i-1];
            if((diff>0 && prevDiff<=0) || diff<0 && prevDiff>=0){
                ans++;
                prevDiff=diff;
            }
        }

        return ans;
    }

    public int wiggleMaxLength_v2(int[] nums) {
        int n = nums.length;
        int up = 1;
        int down = 1;
        for(int i=1;i<n;i++){
            if(nums[i]>nums[i-1]){
                up = down+1;
            }
            else if(nums[i]<nums[i-1]){
                down = up+1;
            }
        }
        int res = Math.max(up,down);
        return res;
    }
}
