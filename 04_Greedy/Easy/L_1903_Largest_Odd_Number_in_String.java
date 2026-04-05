package leetcode;

public class L_1903_Largest_Odd_Number_in_String {
    public String largestOddNumber(String s) {
        int n = s.length(), first = 0, last = -1;
        for (int i = n - 1; i >= 0; i--) {
            if((s.charAt(i) - '0') % 2 == 1) {
                last = i; break;
            }
        }
        if (last == -1) return "";
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) != '0') {
                first = i; break;
            }
        }
        return s.substring(first, last + 1);
    }
}
