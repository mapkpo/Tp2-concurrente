package main;
import java.util.ArrayList;
import java.util.List;

public class Threads extends Thread {
    private final List<List<Integer>> transitions;
    private final Monitor monitor;
    private final Policy policy;

    public Threads(List<List<Integer>> transitions, Monitor monitor, Policy policy) {

        this.transitions = transitions;
        this.monitor = monitor;
        this.policy = policy;
    }

    public Threads(List<Integer> transitions, Monitor monitor) {

        this.transitions = new ArrayList<>();
        this.transitions.add(transitions);
        this.monitor = monitor;
        policy = new Policy(true);
    }

    @Override
    public void run() {
        while (true) {
            /*if (monitor.areInvariantsCompleted()) {
                System.out.println("Thread handling transitions " + transitions + " is terminating as invariants are completed.");
                break;
            }*/
            List<Integer> selected = policy.decide(transitions);

            for (int transition : selected) {
                while (!monitor.fireTransition(transition))
                {
                    if (monitor.areInvariantsCompleted()) {
                        System.out.println("Thread handling transitions " + transitions + " is terminating as invariants are completed.");
                        return;
                    }

                    try {
                        Threads.sleep(100); // Espera un poco antes de volver a intentar.
                    } catch (InterruptedException e) {
                        Threads.currentThread().interrupt();
                        break;
                    }
                }
            }

        }
    }
}
