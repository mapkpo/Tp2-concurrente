package main;
import java.util.Random;

public class Politica {

    int tipodepolitica;

    public Politica(int a){
        tipodepolitica = a;
    }

    public int numerodetransicion(){

        Random rand = new Random();
        double probabilidad = rand.nextDouble();

        if (tipodepolitica == 1){
            if (probabilidad < 0.5) {
                return 11;
            } 
            else return 12;            
        }

        if (tipodepolitica == 2){
            if (probabilidad < 0.8){
                return 11;
            } 
            else return 12;
        }   
        return 11;
    }
}
