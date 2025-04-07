package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class Monitor {
    private final Rdp rdp;
    private final Semaphore mutex;
    private boolean allInvariantsCompleted;
    private final Policy policy;
    private final List<Semaphore> transitionLocks = new ArrayList<>();
    private final List<Boolean> timedQueued = new ArrayList<>();
    private final List<Integer> threadsOnQueue = new ArrayList<>();

    public Monitor(Rdp rdp, Policy policy) {
        this.rdp = rdp;
        this.mutex = new Semaphore(1);
        this.policy = policy;
        allInvariantsCompleted = false;

        for (int i = 0; i < rdp.transitionsNo; i++){
            transitionLocks.add(new Semaphore(0));
            timedQueued.add(false);
            threadsOnQueue.add(0);
        }
    }

    // COLAS DE CONDICIÓN
    private void ConditionQueue(Integer transition) throws InterruptedException {
        // Informo que hay un hilo más en la cola de condición
        threadsOnQueue.set(transition, threadsOnQueue.get(transition) + 1);
        System.out.println("Hilo " + Thread.currentThread().getName() + " esperará hasta ser notificado");

        // Si tengo el mutex lo libero
        mutex.release();

        // Espero
        transitionLocks.get(transition).acquire();

        // No hace falta tomar el mutex nuevamente ya que quien despertó el hilo no lo liberó

        // Al despertar informo que hay un hilo menos en la cola de condición
        threadsOnQueue.set(transition, threadsOnQueue.get(transition) - 1);
    }

    // COLA DE CONDICIÓN POR TIEMPO
    private void TimedQueue(Integer transition, long timeLeft) throws InterruptedException {
        // Informo que hay un hilo esperando a que se sensibilice la transición por tiempo
        timedQueued.set(transition, true);

        // Libero el mutex
        mutex.release();

        // Duermo el tiempo que hace falta
        sleep(timeLeft);

        // Adquiero el mutex
        mutex.acquire();

        // Informo que ya no hay un hilo esperando por tiempo
        timedQueued.set(transition, false);
    }

    public Boolean fireTransition(Integer transition) {
        try {
            mutex.acquire();
            finish();

            while (true) {
                // Verificamos si ya se completaron los invariantes después de despertar
                if (allInvariantsCompleted) {
                    mutex.release();
                    return false;
                }

                // Si hay un hilo durmiendo por tiempo tiene prioridad, voy directo a la cola de condición
                if (timedQueued.get(transition)){
                    ConditionQueue(transition);
                }

                // Verificamos si ya se completaron los invariantes después de despertar
                if (allInvariantsCompleted) {
                    mutex.release();
                    return false;
                }

                long timeLeft = rdp.isEnabled(transition);

                // Si la transición está sensibilizada la disparo
                if (timeLeft == 0){
                    System.out.println("Disparando transición: T" + transition + " por " + Thread.currentThread().getName());
                    rdp.fire(transition);
                    break;
                }

                if (timeLeft == -1) {
                    // Si no está sensibilizada por marcado entro a la cola de condición
                    ConditionQueue(transition);
                } else {
                    // Si no está sensibilizada por tiempo entro a la cola temporizada
                    TimedQueue(transition, timeLeft);
                }
            }

            // Adquiero las nuevas transiciones que se habilitaron
            List<Integer> enabled = rdp.whichEnabled();

            // De estas filtro las que no tienen hilos en la cola de condición o tiene un hilo en la temporizada
            List<Integer> ready = new ArrayList<>();
            for (Integer T : enabled) {
                if (threadsOnQueue.get(T) > 0 && !timedQueued.get(T)) {
                    ready.add(T);
                }
            }

            // Si no hay hilos que despertar liberar el mutex y retornar
            if (ready.isEmpty()){
                mutex.release();
                return true;
            }

            // Pero si hay, elegir uno y despertarlo sin liberar el mutex para darle prioridad
            Integer to_awake = policy.decide(ready);
            transitionLocks.get(to_awake).release();

            // No liberamos el mutex después de despertar un hilo a menos que ya se hayan completado todos los invariantes
            if (allInvariantsCompleted) {
                mutex.release();
                return false;
            }
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
                transitionLocks.get(i).release(5000);
            }
            synchronized (this){
                notifyAll();
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
