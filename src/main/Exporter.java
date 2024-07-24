package main;

// import java.util.concurrent.TimeUnit;
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
            //System.out.println(threadName + ": Buscando imagen para exportar.");
            Image img = monitor.startExport();

            if(monitor.isReadyToFinish()){
                break;
            }

            monitor.finishExport(img);
            //System.out.println(threadName + ": Imagen exportada exitósamente.");
            counter.incrementAndGet();
        }
        monitor.finish();
    }

    public int getContador(){
        return counter.get();
    }
}
