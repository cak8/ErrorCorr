package BurstErrors;

/**
 * Created by 140002949 on 09/11/16.
 */
public class Channel {

    boolean state = true; //good =true , bad =false

    double Pgb; // probability of good to bad
    double Pgg; // staying in the good state = 1-Pgb
    double Pbg; // probability of bad to good
    double Pbb; // probability of staying in a bad state = 1-Pbg
    double Pflip; // probability of flipping bit
    int nrFlipped = 0;



    // random simulation
    public Channel(double p){
        Pgb = Math.random()*1.00;
        Pbg = Math.random()*1.00;
        Pflip = p; // probability of error occurring
    }

    //controlled simulation
    public Channel(double p, double pbg, double pgb){
        Pgb = pgb;
        Pbg = pbg;
        Pflip = p; // probability of error occurring
    }

    public StringBuffer transmit(StringBuffer bitStream){

        double stateProb;
        double flipProb;

        for(long i =0; i<bitStream.length(); i++){

            stateProb = Math.random()*1.00;
            flipProb = Math.random()*1.00;

            if(state == true){

                if(stateProb<Pgb){
                    state = false;
                }
            }
            else if(stateProb<Pbg){
                state = true;
            }

            if(state==false){
                //Burst error
                if(flipProb<Pflip){
                    int flipped =  (1 ^  Character.getNumericValue(bitStream.charAt((int) i))); // because xor gives you the opposite
                    bitStream.setCharAt((int) i, Character.forDigit(flipped, 2));
                    nrFlipped++;
                }
            }
        }
        System.out.println("Number of Errors occured: "+nrFlipped);
        return bitStream;
    }
}
