import java.util.ArrayList;
import java.util.List;

public class Program2 {
    public static List<Integer> calculate_Orchard_Sizes(char[][] matrix) {
        //creating a list of integers to store orchard sizes
        List<Integer> orchardSizes = new ArrayList<>();
        //Iterating over matrix
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 'T') {
                    int orchardSize = exploreOrchard(matrix, i, j);
                    orchardSizes.add(orchardSize);
                }
            }
        }

        return orchardSizes;
    }
    //Applying DFS to explore orchard in all directions
    private static int exploreOrchard(char[][] matrix, int i, int j) {
        //base case : boundary check
        if (i < 0 || i >= matrix.length || j < 0 || j >= matrix[0].length || matrix[i][j] != 'T') {
            return 0;
        }

        int size = 1;
        matrix[i][j] = 'O'; // Mark the current tree as visited

        // Explore the neighbors: (up, down, left, right, and diagonals)
        size += exploreOrchard(matrix, i - 1, j); // Up
        size += exploreOrchard(matrix, i + 1, j); // Down
        size += exploreOrchard(matrix, i, j - 1); // Left
        size += exploreOrchard(matrix, i, j + 1); // Right
        size += exploreOrchard(matrix, i - 1, j - 1); // Diagonal: Top left
        size += exploreOrchard(matrix, i - 1, j + 1); // Diagonal: Top right
        size += exploreOrchard(matrix, i + 1, j - 1); // Diagonal: Bottom left
        size += exploreOrchard(matrix, i + 1, j + 1); // Diagonal: Bottom right

        return size;
    }
    public static void main(String[] args) {
        char[][] matrix1 = {
                {'O', 'T', 'O', 'O'},
                {'O', 'T', 'O', 'T'},
                {'T', 'T', 'O', 'T'},
                {'O', 'T', 'O', 'T'}
        };

        char[][] matrix2 = {
                {'T','O','O'},
                {'O','T','T'},
                {'O','O','T'}
        };

        //Calling calculate_Orchard_Sizes to return orchard size for each matrix
        List<Integer> orchardSizes1 = calculate_Orchard_Sizes(matrix1);
        List<Integer> orchardSizes2 = calculate_Orchard_Sizes(matrix2);

        //printing solution for both matrices
        System.out.println(orchardSizes1);
        System.out.println(orchardSizes2);
    }
}
