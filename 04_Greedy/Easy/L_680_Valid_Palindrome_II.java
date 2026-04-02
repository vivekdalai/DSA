package leetcode;

public class L_680_Valid_Palindrome_II {
    public static boolean validPalindrome(String s) {

        int n=s.length();

        int i=0,j=n-1;
        while(i<j){
            if(s.charAt(i)==s.charAt(j)){
                i++;j--;
            } else {
                return isPalindrome(s,i+1,j) || isPalindrome(s,i,j-1);
            }
        }

        return true;
    }

    private static boolean isPalindrome(String s, int i, int j) {
        while(i<j){
            if(s.charAt(i)!=s.charAt(j)) return false;
            i++;j--;
        }
        return true;

    }
}
