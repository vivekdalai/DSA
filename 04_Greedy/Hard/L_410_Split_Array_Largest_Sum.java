package leetcode;

public class L_410_Split_Array_Largest_Sum {
    public int splitArray(int[] nums, int k) {
        int n = nums.length, l = 0, r = 0, ans = 0;
        for (int i=0; i < n; i++) {
            l = Math.max(l, nums[i]);
            r += nums[i];
        }
        while (l <= r){
            int mid = (l + r) / 2;
            if (func(nums, n, mid, k) <= k){
                ans = mid;
                r = mid - 1;
            } else l = mid + 1;
        }
        return ans;
    }

    public int func(int[] nums, int n,  int mid, int k) {
        int sum = 0, cnt = 1;
        for (int i=0; i < n; i++) {
            sum += nums[i];
            if (sum > mid) {
                sum = nums[i];
                cnt++;
            }
        }
        return cnt;
    }
}
