public class SpiralMatrix {
    public static void main(String[] args) {
        int[][] matrix = {{1,2,3,4,5},{6,7,8,9,10},{11,12,13,14,15},{16,17,18,19,20}};


        int n = matrix.length;
        int m = matrix[0].length;

        for(int i=0; i<n; i++){
            for(int j = 0 ;j < m ; j++){
                System.out.print(matrix[i][j] + " ");  
            }
            System.out.println();
        }
        System.out.println("\n\n");
        printSpiralMatrix(matrix);
    }

    private static void printSpiralMatrix(int[][] matrix){
        int n = matrix.length;
        int m = matrix[0].length;

        boolean[][] visited = new boolean[n][m]; //initialized with false
        int i = 0, j = m-1;
        while(i < n && j >= 0){
            printRow(i, 0, n, m ,matrix, visited);
            printColumn(j, 0, n, m , matrix, visited);
            printRow(n- i - 1, 1, n, m,matrix,  visited);
            printColumn(m - j - 1,1, n, m, matrix, visited);
            i++;
            j--;
        }
    }

    private static void printRow(int row, int direction, int n, int m, int[][] matrix, boolean[][] visited){
        if(direction == 0){
            for(int j = 0; j < m ; j++){
                if(visited[row][j] == false){
                    System.out.print(matrix[row][j] + ", ");
                    visited[row][j] = true;
                }
            }
        } else {
            for(int j = m-1; j >= 0 ;j--){
                if(visited[row][j] == false){
                    System.out.print(matrix[row][j] + ", ");
                    visited[row][j] = true;
                }
            }
        }
    }

    private static void printColumn(int col, int direction, int n, int m, int[][] matrix, boolean[][] visited){
        if(direction == 0){
            for(int i = 0 ; i < n ; i++){
                if(visited[i][col] == false){
                    System.out.print(matrix[i][col] + ", ");
                    visited[i][col] = true;
                }
            }
        } else {
            for(int i = n-1 ; i >=0 ; i-- ){
                if(visited[i][col] == false){
                    System.out.print(matrix[i][col] + ", ");
                    visited[i][col] = true;
                }
            }
        }
    }
}