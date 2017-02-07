package HammingCodes;

/**
 * Created by 140002949 on 31/10/16.
 */
public class Encoder {

    private final int[] indicesOfParityBits;

    int length;//n
    int dimension;//k
    int parityBits;//r


    public Encoder(int r) {
        parityBits = r;
        length = ((int) Math.pow(2, r)) - 1;        // (2^r)-1
        dimension = ((int) Math.pow(2, r)) - r - 1;   // (2^r)-r-1

        indicesOfParityBits = new int[parityBits];
        fillIOPB();

    }
    public Encoder(int r, int n) {
        parityBits = r;
        length = n;    // (2^r)-1
        dimension = ((int) Math.pow(2, r)) - r - 1;   // (2^r)-r-1

        indicesOfParityBits = new int[parityBits];
        fillIOPB();

    }

    private void fillIOPB(){// for decoding purposes- fills array with parity bits indices
        for (int pb = 0; pb< parityBits; pb++) {
            indicesOfParityBits[pb] = (int) Math.pow(2,pb);
        }
    }

    /**
     * Helps skipping indices in string that are reserved for parity bits and not code bits
     * @param nr
     * @param arr
     * @return
     */
    private boolean arrContains(int nr, int[] arr){
        for(int i =0; i< arr.length; i++){
            if(arr[i]==nr){
                return true;
            }
        }
        return false;
    }

    /**
     * Fills in info bits int to the String that is going to represent encoded word
     * @param code
     * @return
     */
    private StringBuffer fillInfoBits(String code){
        StringBuffer buffer;
        char[] bufarr = new char[length];
        int j = 0;
        for(int i =0; i<length; i++){
            if(arrContains(i+1,indicesOfParityBits)== false){

                bufarr[i] = code.charAt(j);
                j++;
            }
        }
        buffer = new StringBuffer(String.valueOf(bufarr));
        return buffer;
    }

    /**
     *Encodes info bits
     * @param code - string of k information bits
     * @return
     */
    public String encode2(String code){
        StringBuffer toEncode = fillInfoBits(code);// fills in info bits

        for(int i = 0; i<parityBits; i++){
            // fill in parity bits after calculating in addPar
            int pb = addPar(i,toEncode.toString());
            int pbIndex = indicesOfParityBits[i]-1;

            toEncode.setCharAt(pbIndex, Character.forDigit(pb,2));
        }

        return toEncode.toString();
    }

    /**
     * This function looks at the parity index , hence 1,2,4.. and calculates what bit has to be added into the empty spaces in "code"
     * @param parIndex
     * @param code - String filled in with info bits and empty spaces for parity bits
     * @return
     */
    private int addPar(int parIndex, String code ) {
        int sum = 0;
        int index = (int) Math.pow(2, parIndex);// actual parity index-->not(1->0 2->1 4->3 due to indexing in java( and programming in general))
        int endIndex;// index to keep track of where the end of the "code chunk" you are looking at is.

        int i  = 0;
        while(i<code.length()){

            if (index != 1) {// because can skip every other bit, whereas other parity bits can't
                if(i==0){
                    i = index;// always start at parity bit index, because it is on next to the empty space in which the actual parity bit is going to be places
                }
                // keep updating sum by binary adding the sum of the chunck that has been looked at
                if (i == index) {
                    endIndex = (index + i - 1 <= code.length()) ? index + i - 1 : code.length();
                    sum = sum ^ even(sum, code.substring(i, endIndex));
                    i = i+index+1;
                } else {
                    endIndex = (index + i  <= code.length()) ? index + i  : code.length();
                    sum = even(sum, code.substring(i, endIndex));
                    i = i + index*2;
                }
            }
            else{
                if (i == 0) {
                    i = index * 2 + i;
                }
                int bit = Character.getNumericValue(code.charAt(i));
                i = i+2;

                sum = sum ^ bit;
             }
        }
        return sum;
    }

    /**
     * used in addpar to return the binary sum of the block that has been analysed
     *
     * @param sum
     * @param substring
     * @return
     */
    private int even(int sum, String substring){
        for(int i =0 ; i<substring.length(); i++){
            int bit = Character.getNumericValue(substring.charAt(i));
            sum = sum ^ bit;
        }
        return sum;
    }



}
