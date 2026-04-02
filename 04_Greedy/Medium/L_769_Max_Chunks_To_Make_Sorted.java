package leetcode;

public class L_769_Max_Chunks_To_Make_Sorted {
    public int maxChunksToSorted(int[] arr) {
        int idxSum = 0, sum = 0, cnt = 0;
        for (int i = 0; i < arr.length; i++){
            idxSum += i;
            sum += arr[i];

            if (idxSum == sum) cnt++;
        }

        return cnt;
    }
}
