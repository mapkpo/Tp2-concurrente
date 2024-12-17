package main;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final Rdp rdp;
    private final ReentrantLock mutex;
    private boolean allInvariantsCompleted;
    private Policy policy;

    public Monitor(Rdp rdp, Policy policy) {
        this.rdp = rdp;
        this.mutex = new ReentrantLock();
        allInvariantsCompleted = false;
        this.policy = policy;
    }

    public Boolean fireTransition(List<Integer> transitions) {
        mutex.lock();
        finish(); 
        try {
            List<Integer> toTry = rdp.whichEnabled();

            toTry.retainAll(transitions);

            if(toTry.size() == 0){
                return false;
            }

            int number = policy.decide(toTry);

            if (rdp.isEnabled(number)) {
                System.out.println("Firing transition: T" + number);
                rdp.fire(number);
                return true;
            } else {
                System.out.println("Transition T" + number + " is not enabled.");
                return false;
            }
        } finally {
            mutex.unlock(); 
        }
    }

    private void finish(){
        if (rdp.completedInvariants()){
            allInvariantsCompleted = true;
            System.out.println(rdp.getSequence());
        }
    }

    public boolean areInvariantsCompleted() {
        return allInvariantsCompleted;
    }

    public Rdp getRdp(){
        return rdp;
    }

}
