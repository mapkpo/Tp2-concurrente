package main;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final Rdp rdp;
    private final ReentrantLock mutex;
    private boolean allInvariantsCompleted;
    private Policy policy;
    private Semaphore[] transitionSems;

    public Monitor(Rdp rdp, Policy policy) {
        this.rdp = rdp;
        this.mutex = new ReentrantLock();
        allInvariantsCompleted = false;
        this.policy = policy;
        transitionSems = new Semaphore[rdp.transitionsNo];
        for (int i = 0; i < transitionSems.length; i++){
            transitionSems[i] = new Semaphore(1, true);
        }
    }

    public Boolean fireTransition(List<Integer> transitions) {
        int number = -1;
        mutex.lock();
        finish(); 
        try {
            List<Integer> toTry = rdp.whichEnabled();

            toTry.retainAll(transitions);

            if(toTry.isEmpty()){
                return false;
            }

            number = policy.decide(toTry);

            // Verifico si el recurso esta disponible
            if(!transitionSems[number].tryAcquire()){
                mutex.unlock();
                // Sino ingreso a la cola
                try {
                    transitionSems[number].acquire();
                } catch (InterruptedException e) {
                    return false;
                }
                mutex.lock();
            }


            if (rdp.isEnabled(number)) {
                System.out.println("Firing transition: T" + number);
                rdp.fire(number);
                return true;
            } else {
                System.out.println("Transition T" + number + " is not enabled.");
                return false;
            }
        } finally {
            if (number != -1) {
                transitionSems[number].release();
            }
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
