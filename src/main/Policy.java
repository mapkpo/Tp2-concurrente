package main;
import java.util.Random;
import java.util.List;

public class Policy {

    private final boolean policyTypeEquitative;

    public Policy(boolean a){
        policyTypeEquitative = a;
    }

    private final Random rand = new Random();
    public Integer decide(List<Integer> transitions){
        double probability = rand.nextDouble();

        switch (transitions.size()){
            case 0:
                return null;
            case 1:
                return transitions.get(0);
            case 2:
                if (transitions.contains(11) && transitions.contains(12)) {
                    if (policyTypeEquitative){
                        if (probability <= 0.5){
                            return 11;
                        } else return 12;

                    } 
                        else {
                            if (probability <= 0.8){
                                return 11;
                            } else return 12;
                        }
                }
            default:
                int randomIndex = rand.nextInt(transitions.size());
                //System.err.println(transitions);
                return transitions.get(randomIndex);
        }
    }
}
