package leetcode;

public class L_409_LongestPalindrome {
    public static int longestPalindrome(String s) {

        int[] freq = new int[200];

        for (char ch : s.toCharArray()) {
            freq[ch]++;
        }

        int ans=0;
        boolean oddFound=false;

        for(int cnt : freq){
            if(cnt%2==0){
                ans+=cnt;
            } else {
                ans+=cnt-1;
                oddFound=true;
            }
        }

        if (oddFound) ans++;


        return ans;
    }
}
