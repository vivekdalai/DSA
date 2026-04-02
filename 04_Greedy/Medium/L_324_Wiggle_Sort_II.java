package leetcode;

import java.util.HashMap;

public class L_324_Wiggle_Sort_II {

    public static void wiggleSort(int[] nums) {
        int median = findMedian(nums,0,nums.length-1, nums.length/2);
        int n = nums.length;
        int left = 0, i = 0, right = n - 1;

        while (i <= right) {

            int mapped = newIndex(i, n);

            if (nums[mapped] > median) {
                swap(nums, newIndex(left++, n), mapped);
                i++;
            }
            else if (nums[mapped] < median) {
                swap(nums, newIndex(right--, n), mapped);
            }
            else {
                i++;
            }
        }
    }

    private static int newIndex(int index, int n) {
        return (1 + 2 * index) % (n | 1);
    }

    private static int findMedian(int[] nums, int left, int right, int medianIdx) {
        int pivot = findPivot(nums,left,right);

        if(pivot==medianIdx) return nums[pivot];
        else if (pivot>medianIdx) return findMedian(nums,left,pivot-1, medianIdx);
        else return findMedian(nums,pivot+1,right, medianIdx);
    }

    private static int findPivot(int[] nums, int left, int right) {
        int pivotElement = nums[right];
        int l = left, r=right-1;
        while(l<r){
            while (l<right && nums[l]<pivotElement) l++;
            while (left<r && nums[r]>=pivotElement) r--;
            if(l<r){
                swap(nums,l,r);
            }
        }
        swap(nums,l,right);
        return l;
    }

    private static void swap(int[] nums, int i, int j){
        int temp = nums[j];
        nums[j] = nums[i];
        nums[i] = temp;
    }
}
