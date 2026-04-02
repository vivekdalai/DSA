package leetcode;

import java.util.Arrays;

public class L_135_Candy {
    public static int candy(int[] ratings) {
        int n = ratings.length;

        int[] candiesLR = new int[n];
        int[] candiesRL = new int[n];

        for(int i=0;i<n;i++){
            candiesLR[i]=1;
            candiesRL[i]=1;
        }

        for(int i=1;i<n;i++){
            if(ratings[i]>ratings[i-1]){
                candiesLR[i] = candiesLR[i-1]+1;
            }
        }

        for(int i=n-2;i>=0;i--){
            if(ratings[i]>ratings[i+1]){
                candiesRL[i] = candiesRL[i+1]+1;
            }
        }

        int totalCandies = 0;
        for(int i=0;i<n;i++){
            totalCandies+=Math.max(candiesRL[i],candiesLR[i]);
        }

        return totalCandies;

    }

    //without 2 extra arrays --> in one array only
    public static int candy_v2(int[] ratings) {
        int n = ratings.length;

        int[] candies = new int[n];
        int totalCandies = 0;

        Arrays.fill(candies,1);

        for(int i=1;i<n;i++){
            if(ratings[i]>ratings[i-1]){
                candies[i] = candies[i-1]+1;
            }
        }

        for(int i=n-2;i>=0;i--){
            if(ratings[i]>ratings[i+1]){
                candies[i] = Math.max(candies[i+1]+1,candies[i]);
            }
        }

        for(int i=0;i<n;i++){
            totalCandies+=candies[i];
        }

        return totalCandies;

    }
}
