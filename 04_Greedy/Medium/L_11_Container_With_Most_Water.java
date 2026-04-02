package leetcode;

public class L_11_Container_With_Most_Water {
    public int maxArea(int[] height) {
        int n = height.length;
        int l = 0, r = n-1, maxArea = 0;

        while (l < r) {
            maxArea = Math.max(maxArea, Math.min(height[l], height[r]) * (r-l));
            if (height[r] > height[l])
                l++;
            else
                r--;
        }

        return maxArea;
    }
}
