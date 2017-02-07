import BurstErrors.Channel;
import HammingCodes.Decoder;
import HammingCodes.Encoder;
import Interleaving.Deinterleaver;
import Interleaving.Interleaver;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;


/**
 * Created by 140002949 on 11/11/16.
 */
public class Loop {

    PrintWriter statsWriter;

    //CSV file header
    private static final String FILE_HEADER = "Error Probability p, P(good -> bad), P(bad->good),parity bits r, length of encode word n, number of words generated(table height), correctness of decoded text %";

    Random random = new Random();// Random bit generator
    StringBuffer input = new StringBuffer();//source
    StringBuffer output = new StringBuffer();//decoded text

    //Objects used through out class
    Encoder encoder;
    Decoder decoder;
    BurstErrors.Channel channel;

    double errorProb;
    double pgb;
    double pbg;

    int r;// nr of parity bits
    int wordlength;// length of word after encoding
    int infoBits;// nr of information bits in encoding
    int numberOfWords;// number of words in array to be transposed


    /**
     *
     * @param parBits - number of parity bits
     * @param wordlength
     * @param errorProb
     * @param pbg
     * @param pgb
     * @param fileName
     * @throws IOException
     */
    public Loop(int parBits, int wordlength, double errorProb, double pbg, double pgb, String fileName) throws IOException {
        statsWriter = new PrintWriter(new FileWriter(fileName, true));

        r = parBits;
        infoBits = wordlength - parBits;
        this.wordlength = wordlength;
        this.errorProb = errorProb;

        encoder = new Encoder(r,wordlength);
        decoder = new Decoder(r,wordlength);

        if(pbg >=0 && pgb>=0){
            channel = new Channel(errorProb,pbg,pgb);
        }
        else{
            channel = new Channel(errorProb);
        }
        this.pbg = pbg;
        this.pgb = pgb;

    }

    public void run(int numberOfWords) {
        this.numberOfWords = numberOfWords;

        while (input.length() < infoBits * numberOfWords) {
            bitsGen();
        }
        System.out.println("INPUT STRING: "+input.toString()+"\n");

        StringBuffer atReceiver = toEncode(infoBits, numberOfWords);
        toDecode(atReceiver);
        double correctness = isCorrect();
        System.out.println(correctness+"%");
        statsWriter.println(errorProb+","+ pgb+","+pbg+","+r+","+wordlength+","+numberOfWords+","+ correctness);
        statsWriter.flush();
    }

    private void bitsGen() {
        int word = random.nextInt(2);
        input.append(word);
    }

    private StringBuffer toEncode(int infoBits, int numberOfWords) {

        char[][] encoded = new char[numberOfWords][wordlength];
        System.out.println("ENCODED WORDS\n ");//testing

        for (int i = 0; i < input.length() - infoBits + 1; i += infoBits) {

            String toEncode = input.substring(i, i + infoBits);
            String result = encoder.encode2(toEncode);

            System.out.println("To encode" + toEncode);
            System.out.println("Encoded" + result + "\n");


            if (i / infoBits < numberOfWords) {
                encoded[i / infoBits] = result.toCharArray();
            } else {
                interleaveAndSend(encoded);
                break;
            }
        }
        return interleaveAndSend(encoded);
    }

    private StringBuffer interleaveAndSend(char[][] initialMatrix) {
        StringBuffer toSend = new StringBuffer();
        char[][] leaved = Interleaver.interleave(initialMatrix);

        System.out.println("interleaved words");//testing

        // flattening array to prepare for transmission through channel
        for (int i = 0; i < leaved.length; i++) {
            for(int j = 0; j < leaved[0].length; j++){
                toSend.append(leaved[i][j]);
            }
            System.out.println(Arrays.toString(leaved[i]));
        }
        System.out.println("\nTo send through channel:"+toSend);
        return channel.transmit(toSend);
    }

    private char[][] deInterleave(StringBuffer transmitted) {
        System.out.println("Received bits: "+ transmitted.toString()+"\n");
        char[][] transmittedArray = new char[wordlength][numberOfWords];
        int strIndex = 0;
        for(int i = 0; i<wordlength; i++){
            for(int j = 0; j<numberOfWords; j++) {
                transmittedArray[i][j] = transmitted.charAt(strIndex++);
            }
        }
         return Deinterleaver.deInterleave(transmittedArray);
    }

    private void toDecode(StringBuffer transmitted){
        // deinterleave first
        char[][] received = deInterleave(transmitted);

        //decode
        System.out.println("\nDECODED WORDS\n");
        String decoded;
        for(int i =0; i<received.length; i++){
            decoded = decoder.decode(String.valueOf(received[i]));
            output.append(decoded);
            System.out.println("Decoded"+decoded+"\n");
        }
    }

    private double isCorrect(){
        System.out.println();

        if(input.toString().equals(output.toString())){
            System.out.println("String has been correctly transmitted and corrected if needed");
            return 100.00;
        }
            int err = 0;
            for(int i = 0; i<input.length(); i++){
                if(input.charAt(i)!=output.charAt(i)){
                    err++;
                }
            }

            double diff = input.length() - err;
            double perc = 100 * diff/input.length();
            System.out.println("Too many errors occured to correct errors");
        return perc;
    }

}