
package HammingCodes;

/**
 * Created by 140002949 on 31/10/16.
 */
public class Decoder {

    int length;//n
    int dimension;//k
    int parityBits;//r

    StringBuilder corrected;

    int sumOfFalseParBits =0; // if error checking sum is not even you know that an error has occured.
    int[] indicesOfParityBits;// the indices at which the parity bits will be located


    public Decoder(int r){
        parityBits = r;
        length = ((int) Math.pow(2, r)) - 1;        // (2^r)-1
        dimension = ((int) Math.pow(2, r)) - r - 1; // (2^r)-r-1

        fillIOPB();

    }

    public Decoder(int r, int n){
        parityBits = r;
        length = n;     // (2^r)-1
        dimension = ((int) Math.pow(2, r)) - r - 1; // (2^r)-r-1

        fillIOPB();

    }

    /**
     * fills the indicesOfParityBits array
     */
    private void fillIOPB(){// for decoding purposes- fills the field array with powers of 2
        indicesOfParityBits = new int[parityBits];
        for (int pb = 0; pb< parityBits; pb++) {
            indicesOfParityBits[pb] = (int) Math.pow(2,pb);//2^n
        }
    }

    /**
     * Decodes data received
     * @param encoded
     * @return
     */
    public String decode(String encoded){
        String correctEncoded= errCorrect(encoded);// correct errors before stripping of parity bits

        System.out.println("Encoded"+encoded);
        StringBuilder decoded = new StringBuilder(correctEncoded);

        for (int pb =0; pb<indicesOfParityBits.length; pb++) {
            // removing parity bits
            decoded.deleteCharAt((indicesOfParityBits[pb])-1-pb);// "-pb" because it becomes a substring and the indices move n forward
        }

        return decoded.toString();
    }


    /**
     * Corrects error if existing
     * @param encoded- string received through channel
     * @return
     */
    private String errCorrect(String encoded){
        for(int pb = 0; pb< parityBits; pb++) {// check for every parity bit present

            // if parity for parity bit is not correct add the parity bits value to the sum so you can find the incorrect bit at the end
            if (!isParCorr(pb, encoded)) {
                int pBit = (int) Math.pow(2, pb);
                sumOfFalseParBits = sumOfFalseParBits + pBit;
            }
        }
        if(sumOfFalseParBits>0){// if error occurred
            System.out.println("error found, correction in process");
            corrected = new StringBuilder(encoded);

            //flip incorrect bit
            int bitToFlip = Character.getNumericValue(encoded.charAt(sumOfFalseParBits-1));
            corrected.setCharAt(sumOfFalseParBits-1, Character.forDigit((1^bitToFlip), 2));

            // reset global variable
            sumOfFalseParBits=0;
            return corrected.toString();
        }
        System.out.println("No error found");
        return encoded;
    }


    /**
     * Checks parity for each parity bit by recursively using binary addition(xor)
     * @param parIndex
     * @param encoded
     * @return if the parity for parity bit is correct, hence 0
     */
    private boolean isParCorr(int parIndex, String encoded ){
        int sum =0;
        int index = (int)Math.pow(2,parIndex);

        for(int i = index-1; i<encoded.length(); i+=index*2){

            int endIndex = (i+(index) <= encoded.length()) ? i+(index) : encoded.length(); // incase string is shorter than data bits concerned
            sum = sum ^ checkDataCall(0, encoded.substring(i,endIndex), 0);

        }

        return sum == 0;
    }

    /**
     * divids up string into n chunks so parity for parity bit n can be checked for each n-block
     * example: n=2, chunks are of size 2, returns the sum for every chunk or pair of bits
     * also done recursively
     *
     * @param sum
     * @param substring
     * @param index
     * @return
     */
    private int checkDataCall(int sum, String substring, int index){
        if(index<substring.length()){
            return sum ^ checkDataCall(Character.getNumericValue(substring.charAt(index)), substring, index+1);
        }else {
            return sum;
        }
    }


}


