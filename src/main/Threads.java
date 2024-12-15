package main;
import java.util.List;

public class Threads extends Thread {
    private final List<Integer> transitions;
    private final Monitor monitor;

    public Threads(List<Integer> transitions, Monitor monitor) {
        this.transitions = transitions;
        this.monitor = monitor;
    }

    @Override
    public void run() {
        while (true) {
            if (monitor.areInvariantsCompleted()) {
                System.out.println("Thread handling transitions " + transitions + " is terminating as invariants are completed.");
                break;
            }

            boolean anyFired = false;

            // for (int transition : transitions) {
            //     if (monitor.fireTransition(transition)) {
            //         anyFired = true;
            //         System.out.println("Fired transition: T" + transition);
            //     }
            // }

            
                if (monitor.fireTransition(transitions)) {
                    anyFired = true;
                    //System.out.println("Fired transition: T" + transition);
                }
            

            if (!anyFired) {
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
