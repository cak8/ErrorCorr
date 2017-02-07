package Interleaving;

/**
 * Created by 140002949 on 11/11/16.
 */
public class Interleaver {

    static int wordlength;//  height of interleaved table
    static char[][] wordmatrix;


    private static void receiveMatrix(char[][] matr) {
        wordlength = matr.length;
        wordmatrix = new char[matr[0].length][wordlength];
    }

    public static char[][] interleave(char[][] matr) {
        System.out.println("interleaving");
        receiveMatrix(matr);

        for (int column = 0; column < wordmatrix[0].length; column++) { // original array row
            for (int row = 0; row < wordmatrix.length; row++) { // original array column
                wordmatrix[row][column] = matr[column][row];
                System.out.print(wordmatrix[row][column]);
            }
            System.out.println();
        }
        System.out.println("interleaving done \n");
        return wordmatrix;
    }
}