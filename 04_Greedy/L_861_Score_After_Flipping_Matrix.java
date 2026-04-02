package leetcode;

public class L_861_Score_After_Flipping_Matrix {
    public int matrixScore(int[][] grid) {
        int n = grid.length;
        int m=grid[0].length;
        int score = n * (1<<(m-1)); // 1st column score (assuming all 1s)
        for(int j=1;j<m;j++){

            int countOnes = 0;

            for(int i=0;i<n;i++){
                if(grid[i][0]==1){
                    countOnes+=grid[i][j]; // no flips
                } else{
                    countOnes+=1-grid[i][j];
                }
            }

            int maxOnes = Math.max(countOnes,n-countOnes);
            score+= maxOnes * (1<<(m-1-j));

        }
        return score;
    }
}
