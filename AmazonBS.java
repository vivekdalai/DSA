import java.util.ArrayList;
import java.util.List;

public class AmazonBS {

    // Given two integers x, y say x = 3 and y =4 form the largest number such that sum of the number formed is n, say =15
    // possible answers --> 4443 , 33333
    // return the largest one --> 33333;
    // Linear didn't work have to apply BS
    public static void main(String[] args) {

        int x = 3, y =4;
        int n = 24;

        int bestX = -1, bestY = -1;

        // Iterate over countY from max possible down to 0
        for (int countY = n / y; countY >= 0; countY--) {
            int rem = n - (countY * y);
            if (rem % x != 0) continue;
            int countX = rem / x;

            // Prefer more digits, or higher leading digits
            if (bestX == -1 || countX + countY > bestX + bestY ||
                    (countX + countY == bestX + bestY && countY > bestY)) {
                bestX = countX;
                bestY = countY;
            }
        }


        // Build result once
        StringBuilder sb = new StringBuilder(bestX + bestY);
        for (int i = 0; i < bestY; i++) sb.append(y);
        for (int i = 0; i < bestX; i++) sb.append(x);
        System.out.println(sb.toString());

    }

    private static String formNumber(int x, int y, int countX, int countY){
        StringBuilder stringBuilder = new StringBuilder();
        while(countY!=0){
            stringBuilder.append(String.valueOf(y));
            countY--;
        }

        while(countX != 0){
            stringBuilder.append(String.valueOf(x));
            countX--;
        }

        return stringBuilder.toString();
    }

}
