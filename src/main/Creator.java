package main;

import java.util.concurrent.atomic.AtomicInteger;

public class Creator implements Runnable{

    final Monitor monitor;
    String threadName;
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int maxAmmount;

    public Creator(Monitor monitor, int amount) {
        this.monitor = monitor;
        maxAmmount = amount;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish() && counter.get() < maxAmmount){
            // la transición 0 siempre está sensibilizada, no hace falta revisar
            if (!monitor.addImageToContainer(0, 0, new Image())){
                continue;
            }
            counter.incrementAndGet();
        }
    }

    public int getCounter(){
        return counter.get();
    }
}
