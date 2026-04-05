package leetcode;

public class L_921_Minimum_Add_to_Make_Parentheses_Valid {
    public int minAddToMakeValid(String s) {
        int cnt = 0, ans = 0;

        for (char c : s.toCharArray()) {
            if (c == '(') cnt++;
            else {
                if (cnt > 0) cnt--;
                else ans++;
            }
        }
        return ans + cnt;
    }
}
