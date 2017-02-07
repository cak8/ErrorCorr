import java.io.IOException;

/**
 * Created by 140002949 on 03/11/16.
 */
public class Main {

    public static void main(String args[]) throws IOException {

            Loop loop;
            loop = new Loop(4, 8, 0.1, 0.1, 0.1, "Err_corr.csv");//(8,4) Hamming Code
            loop.run(4);

    }

}
