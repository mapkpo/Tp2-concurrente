package main;

// import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Exportador implements Runnable{
    
    final Monitor monitor;
    String threadName;
    private static final AtomicInteger contador = new AtomicInteger(0);
    private final int max;

    public Exportador(Monitor monitor, int cantidad) {
        this.monitor = monitor;
        max = cantidad;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (contador.get() < max && !monitor.isReadyToFinish()){
            //System.out.println(threadName + ": Buscando imagen para exportar.");
            Imagen img = monitor.startExport();

            if(monitor.isReadyToFinish()){
                break;
            }

            monitor.finishExport(img);
            //System.out.println(threadName + ": Imagen exportada exitósamente.");
            contador.incrementAndGet();

            /*try{
                TimeUnit.MILLISECONDS.sleep(0);
            } catch(InterruptedException e){
                e.printStackTrace();
            }*/
        }
        monitor.finish();
    }

    public int getContador(){
        return contador.get();
    }
}
