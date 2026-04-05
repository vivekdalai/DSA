package leetcode;

public class L_1323_Maximum_69_Number {
    public int maximum69Number (int num) {
        char[] c = String.valueOf(num).toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '6') {
                c[i] = '9'; break;
            }
        }
        return Integer.parseInt(new String(c));
    }
}
