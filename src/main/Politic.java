package main;
import java.util.Random;

public class Politic {

    int poloticType;

    public Politic(int a){
        poloticType = a;
    }

    public int transitionNumber(){

        Random rand = new Random();
        double probability = rand.nextDouble();

        if (poloticType == 1){
            if (probability < 0.5) {
                return 11;
            } 
            else return 12;            
        }

        if (poloticType == 2){
            if (probability < 0.8){
                return 11;
            } 
            else return 12;
        }   
        return 11;
    }
}
