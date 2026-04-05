package leetcode;

public class L_1927_Sum_Game {
    public boolean sumGame(String num) {
        int rightSum = 0, leftSum = 0, leftQ = 0, rightQ = 0,  n = num.length();
        for (int i = 0; i < n; i++) {
            char c = num.charAt(i);
            if (i < n / 2) { // first half
                if (c == '?') leftQ++;
                else leftSum += c-'0';
            } else { // second half
                if (c == '?') rightQ++;
                else rightSum += c-'0';
            }
        }
        // If total '?' is odd → Alice wins
        if ((leftQ + rightQ) % 2 != 0) return true;
        // Bob wins only if perfectly balanced
        return (rightSum - leftSum) != ((leftQ - rightQ) / 2) * 9;

    }
}
