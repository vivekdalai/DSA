package leetcode;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Stack;

public class L_321_Create_Maximum_Number {
    public static int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int[] ans = new int[k];

        int len1 = nums1.length;
        int len2 = nums2.length;
        for(int i=0;i<=k;i++){

            int n1 = i;
            int n2 = k-i;

            if(n1<=len1 && n2<=len2) {
                int canMiss1 = len1 - n1;
                int canMiss2 = len2 - n2;

                // 1. find max digit number for arrays using monotonic stack
                Deque<Integer> st1 = new ArrayDeque<>();
                Deque<Integer> st2 = new ArrayDeque<>();

                for (int d : nums1) {
                    while (!st1.isEmpty() && canMiss1 > 0 && d > st1.peek()) {
                        st1.pop();
                        canMiss1--;
                    }
                    st1.push(d);
                }

                while (!st1.isEmpty() && canMiss1 > 0) {
                    st1.pop();
                    canMiss1--;
                }


                for (int d : nums2) {
                    while (!st2.isEmpty() && canMiss2 > 0 && d > st2.peek()) {
                        st2.pop();
                        canMiss2--;
                    }
                    st2.push(d);
                }

                while (!st2.isEmpty() && canMiss2 > 0) {
                    st2.pop();
                    canMiss2--;
                }

                int[] arr1 = new int[n1];
                int[] arr2 = new int[n2];

                for (int j = n1 - 1; j >= 0; j--) {
                    if (!st1.isEmpty())
                        arr1[j] = st1.pop();
                }

                for (int j = n2 - 1; j >= 0; j--) {
                    if (!st2.isEmpty())
                        arr2[j] = st2.pop();
                }

                // 2. merge two largest digits array greedily
                int[] mergedArray = mergeGreedy(arr1, n1, arr2, n2);

                // 3. compare lexicographically and find max number array
                ans = findMaxNumberArray(mergedArray,ans);
            }

        }

        return ans;
    }

    private static int[] findMaxNumberArray(int[] mergedArray, int[] maxm) {

        for(int i=0;i<mergedArray.length;i++){
            if(mergedArray[i]!=maxm[i]){
                if(mergedArray[i]>maxm[i]){
                    maxm = mergedArray.clone();
                }else return maxm;
            }
        }

        return maxm;

    }

    private static int[] mergeGreedy(int[] arr1, int n1, int[] arr2, int n2) {
        int[] mergedArray = new int[n1+n2];
        int i=0,j=0,k=0;
        while(i<n1 || j<n2){
            if(j == n2 || (i < n1 && greater(arr1,i,n1,arr2,j,n2))) mergedArray[k++]=arr1[i++];
            else mergedArray[k++]=arr2[j++];
        }
        return mergedArray;

    }

    private static boolean greater(int[] arr1, int i, int n1, int[] arr2, int j, int n2) {
        while (i<n1 && j<n2 && arr1[i]==arr2[j]){
            i++;j++;
        }

        if(j==n2) return true;

        if(i<n1 && arr1[i]>arr2[j]) return true;

        return false;

    }
}
