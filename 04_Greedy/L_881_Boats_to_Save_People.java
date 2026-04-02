package leetcode;

import java.util.Arrays;

public class L_881_Boats_to_Save_People {
    public int numRescueBoats(int[] people, int limit) {

        Arrays.sort(people);
        int n = people.length;
        int l = 0, r = n-1, count = 0;
        while (l <= r)  {
            if (people[l] + people[r] <= limit){
                l++;
            }
            r--;
            count++;
        }
        return count;
    }
}
