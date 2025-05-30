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
            for (int transition : transitions) {
                while (!monitor.fireTransition(transition))
                {
                    if (monitor.areInvariantsCompleted()) {
                        System.out.println("Hilo manejando las transiciones " + transitions + " termina dado invariantes completadas.");
                        return;
                    }
                }
            }

        }
    }
}
