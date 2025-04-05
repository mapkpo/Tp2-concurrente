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
                if (policyTypeEquitative){
                    if (probability <= 0.5){
                        return transitions.get(0);
                    } else return transitions.get(1);
                } else {
                    if (probability <= 0.8){
                        return transitions.get(0);
                    } else return transitions.get(1);
                }
            default:
                int randomIndex = rand.nextInt(transitions.size());
                return transitions.get(randomIndex);
        }
    }
}
