package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private final Rdp rdp;
    private final Semaphore mutex;
    private boolean allInvariantsCompleted;

    private final Map<Integer, Object> transitionLocks = new HashMap<>();

    private final List<Boolean> timedQueued = new ArrayList<>();
    private final List<Integer> threadsOnQueue = new ArrayList<>();

    public Monitor(Rdp rdp) {
        this.rdp = rdp;
        this.mutex = new Semaphore(1);
        allInvariantsCompleted = false;

        for (int i = 0; i < rdp.transitionsNo; i++){
            transitionLocks.put(i, new Object());
        }
    }

    public Boolean fireTransition(Integer transition) {
        try {
            mutex.acquire();
            finish();
            if (allInvariantsCompleted){
                return false;
            }

            while (true) {
                long timeLeft = rdp.isEnabled(transition);

                // Si está sensibilizada la disparo
                if (timeLeft == 0){
                    System.out.println("Disparando transición: T" + transition + " por " + Thread.currentThread().getName());
                    rdp.fire(transition);
                    break;
                }



                // COLAS DE CONDICIÓN
                synchronized (transitionLocks.get(transition)){
                    // TODO: Si la transición no está sensibilizada por tiempo no marcar que hay un hilo en cola,
                    //  marcar que hay uno por tiempo y solo libera el mutex duerme por el tiempo que hace falta y lo vuelve a adquirir.
                    // Informo que hay un hilo más en la cola de condición
                    threadsOnQueue.set(transition, threadsOnQueue.get(transition) + 1);
                    System.out.println("Hilo " + Thread.currentThread().getName() + " esperará hasta ser notificado");

                    // Si no está sensibilizada y tengo en mutex lo libero
                    if (mutex.availablePermits() == 0) {
                        mutex.release();
                    }

                    transitionLocks.get(transition).wait();

                    // Al despertar informo que hay un hilo menos en la cola de condición
                    threadsOnQueue.set(transition, threadsOnQueue.get(transition) - 1);
                }
            }

            // Notificar transiciones habilitadas
            List<Integer> enabled = rdp.whichEnabledAfterLastFired();
            List<Integer> ready = new ArrayList<>();
            for (Integer T : enabled) {
                if (threadsOnQueue.get(T) > 0)
                    ready.add(T);
            }
            // Si no hay hilos que despertar liberar el mutex y retornar
            if (ready.isEmpty()){
                mutex.release();
                return true;
            }
            // TODO: Pedir a la política que elija entre las que están en ready y despertar SOLO UNO
            // No liberamos el mutex después de despertar un hilo
            return true;

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
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
