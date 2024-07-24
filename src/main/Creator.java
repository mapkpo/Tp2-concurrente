package main;

import java.util.concurrent.atomic.AtomicInteger;

public class Creator implements Runnable{

    final Monitor monitor;
    String threadName;
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int maxAmmount;

    public Creator(Monitor monitor, int ammount) {
        this.monitor = monitor;
        maxAmmount = ammount;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish() && counter.get() < maxAmmount){
            counter.incrementAndGet();
            //Espera a poder tomar control del mutex del monitor para agregar la imagen al contenedor P0.
            monitor.addImage(new Image());
            //System.out.println(threadName + ": Nueva imagen creada con éxito.");
        }
    }

    public int getCounter(){
        return counter.get();
    }
}
