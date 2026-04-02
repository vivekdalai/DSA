package leetcode;

import java.util.Arrays;

public class L_3545_Minimum_Deletions_for_At_Most_K_Distinct_Characters {
    public int minDeletion(String s, int k) {
        int[] freq = new int[26];

        for(char c: s.toCharArray()){
            freq[c-'a']++;
        }

        Arrays.sort(freq);

        int ans=0;
        for(int i=0;i<26-k;i++){
            ans+=freq[i];
        }
        return ans;
    }
}
