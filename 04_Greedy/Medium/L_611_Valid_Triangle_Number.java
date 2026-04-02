package leetcode;

public class L_611_Valid_Triangle_Number {

    public int triangleNumber(int[] nums) {
        int n=nums.length;
        int ans=0;
        for(int i=n-1;i>=2;i--){

            int l = 0, r=i-1;
            while(l<r){
                if(nums[l]+nums[r]>nums[i]) {
                    ans += r - l;
                    r--;
                } else l++;
            }


        }

        return ans;
    }
}
