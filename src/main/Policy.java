package main;
import java.util.Random;
import java.util.List;

public class Policy {

    private final boolean policyTypeEquitative;

    public Policy(boolean a){
        policyTypeEquitative = a;
    }

    private final Random rand = new Random();
    public int decide(List<Integer> transitions){
        double probability = rand.nextDouble();

        if (transitions.size() == 1){
            return transitions.get(0);
        }

        if (transitions.contains(11) && transitions.contains(12)){

            if (policyTypeEquitative){
                if (probability <= 0.5){
                    return 11;
                } else return 12;
            } else {
                if (probability <= 0.8){
                    return 11;
                } else return 12;
            }
        }

        int randomIndex = rand.nextInt(transitions.size());
        return transitions.get(randomIndex);
    }
}
