package leetcode;

public class L_991_Broken_Calculator {
    public int brokenCalc(int startValue, int target) {
        int cnt = 0;
        while (target > startValue) {
            cnt++;
            if (target % 2 == 0) {
                target /= 2;
            } else {
                target++;
            }
        }

        return cnt + startValue - target;
    }
}
