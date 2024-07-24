package main;
import java.util.Random;

public class Politic {

    int politicType;

    public Politic(int a){
        politicType = a;
    }

    public int transitionNumber(){

        Random rand = new Random();
        double probability = rand.nextDouble();

        if (politicType == 1){
            if (probability < 0.5) {
                return 11;
            } 
            else return 12;            
        }

        if (politicType == 2){
            if (probability < 0.8){
                return 11;
            } 
            else return 12;
        }   
        return 11;
    }
}
