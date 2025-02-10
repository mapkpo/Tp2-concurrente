package main;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final Rdp rdp;
    private final ReentrantLock mutex;
    private boolean allInvariantsCompleted;
    private Semaphore[] transitionSems;

    public Monitor(Rdp rdp) {
        this.rdp = rdp;
        this.mutex = new ReentrantLock();
        allInvariantsCompleted = false;
        transitionSems = new Semaphore[rdp.transitionsNo];
        for (int i = 0; i < transitionSems.length; i++){
            transitionSems[i] = new Semaphore(1, true);
        }
    }

    public Boolean fireTransition(Integer transition) {
        mutex.lock();
        finish(); 
        try {
            // Verifico si el recurso esta disponible
            if(!transitionSems[transition].tryAcquire()){
                mutex.unlock();
                // Sino ingreso a la cola
                try {
                    transitionSems[transition].acquire();
                } catch (InterruptedException e) {
                    return false;
                }
                mutex.lock();
            }


            if (rdp.isEnabled(transition)) {
                System.out.println("Firing transition: T" + transition);
                rdp.fire(transition);
                return true;
            } else {
                System.out.println("Transition T" + transition + " is not enabled.");
                return false;
            }
        } finally {
            if (transition != -1) {
                transitionSems[transition].release();
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
