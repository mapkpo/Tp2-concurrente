package main;
import java.util.Random;
import java.util.List;

public class Policy {

    private boolean policyTypeEquitative;

    public Policy(boolean a){
        policyTypeEquitative = a;
    }

    public int decide(List<Integer> transitions){

        Random rand = new Random();
        double probability = rand.nextDouble();

        if (transitions.contains(11) && transitions.contains(12)){

            //System.out.println("entro aca");
            if (policyTypeEquitative == true){
                if (probability < 0.5){
                    return 11;
                } else return 12;
            }

            if (policyTypeEquitative == false){
                if (probability < 0.8){
                    return 11;
                } else return 12;
            }
        }   
        
        //Random randd = new Random();
        if (transitions.size() == 1){
            return transitions.get(0);
        }
        int randomIndex = rand.nextInt(transitions.size());
        //System.out.println(" sizeeee" + transitions.size());
        //System.out.println(" politica decide: "+transitions.get(randomIndex));
        return transitions.get(randomIndex);


    }
}
