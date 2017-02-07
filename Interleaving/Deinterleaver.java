package Interleaving;

import java.util.Arrays;

/**
 * Created by 140002949 on 11/11/16.
 */
public class Deinterleaver {

    static int numberOfWords;// height of uninterleaved table
    static char[][] wordmatrix;


    private static void receiveMatrix(char[][] matr){
        numberOfWords = matr.length;
        wordmatrix = new char[matr[0].length][numberOfWords];
    }

    public static char[][] deInterleave(char[][] matr){
        System.out.println("deinterleaving");
        receiveMatrix(matr);

        for(int row = 0; row<matr[0].length; row++) { // original array row
            for (int column = 0; column < matr.length; column++) { // original array column
                wordmatrix[row][column] = matr[column][row];
            }
            System.out.println(Arrays.toString(wordmatrix[row]));
        }
        System.out.println("finish deinterleaving");
        return wordmatrix;
    }
}