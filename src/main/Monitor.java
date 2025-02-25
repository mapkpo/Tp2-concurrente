package main;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final Rdp rdp;
    private final ReentrantLock mutex;
    private boolean allInvariantsCompleted;
    private Policy policy;

    private final Map<Integer, Object> transitionLocks = new HashMap<>();

    public Monitor(Rdp rdp, Policy policy) {
        this.rdp = rdp;
        this.mutex = new ReentrantLock();
        allInvariantsCompleted = false;
        this.policy = policy;

        for (int i = 0; i < rdp.transitionsNo; i++){
            transitionLocks.put(i, new Object());
        }
    }

    public Boolean fireTransition(Integer transition) {
        mutex.lock();
        try {
            finish();
            if (allInvariantsCompleted){
                return false;
            }
            long timeLeft = rdp.isEnabled(transition);

            while (timeLeft != 0){
                if (allInvariantsCompleted){
                    return false;
                }
                synchronized (transitionLocks.get(transition)){
                    mutex.unlock();
                    
                        if(timeLeft>0){
                            System.out.println("Hilo " + Thread.currentThread().getName() + " esperará por: " + timeLeft);
                        } 
                            else {
                                System.out.println("Hilo " + Thread.currentThread().getName() + " esperará hasta ser notificado");
                            }
                    
                    transitionLocks.get(transition).wait(Math.max(timeLeft, 0));
                }
                mutex.lock();
                timeLeft = rdp.isEnabled(transition);
            }

            // Si la transición está sensibilizada la dispara y retorna
            System.out.println("Disparando transición: T" + transition + " por " + Thread.currentThread().getName());
            rdp.fire(transition);

            // Notificar transiciones habilitadas
            List<Integer> enabled = rdp.whichEnabled();
            for (Integer t : enabled) {
                synchronized (transitionLocks.get(t)) {
                    transitionLocks.get(t).notify();
                    System.out.println("Despertando a los hilos de las transiciones: T" + t);
                }
            }

            return true;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (mutex.isHeldByCurrentThread()) {
                mutex.unlock();
            }
        }
    }

    private void finish(){
        if(allInvariantsCompleted)
            return;

        if (rdp.completedInvariants()){
            allInvariantsCompleted = true;
            System.out.println(rdp.getSequence());
            for (int i = 0; i < transitionLocks.size(); i++) {
                synchronized (transitionLocks.get(i)){
                    transitionLocks.get(i).notifyAll();
                }
            }
        }
    }

    public boolean areInvariantsCompleted() {
        return allInvariantsCompleted;
    }

    public Rdp getRdp(){
        return rdp;
    }

}
