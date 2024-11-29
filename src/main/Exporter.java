package main;
import java.util.concurrent.atomic.AtomicInteger;

public class Exporter implements Runnable{
    
    final Monitor monitor;
    String threadName;
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int max;

    public Exporter(Monitor monitor, int cantidad) {
        this.monitor = monitor;
        max = cantidad;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (counter.get() < max && !monitor.isReadyToFinish()){
            if (!monitor.readRDP(15))
                continue;

            Image img = monitor.getImageFromContainer(3, 15);
            if(img == null)
                continue;

            // Modify Image if necessary
            while (true){
                if (monitor.addImageToContainer(4, 16, img)) {
                    break;
                }
            }
            counter.incrementAndGet();
        }
        monitor.finish();
    }

    public int getContador(){
        return counter.get();
    }
}
