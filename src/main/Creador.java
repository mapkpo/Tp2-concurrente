package main;

//import java.util.concurrent.TimeUnit;

import java.util.concurrent.atomic.AtomicInteger;

public class Creador implements Runnable{

    final Monitor monitor;
    String threadName;
    private static final AtomicInteger contador = new AtomicInteger(0);
    private final int cantidadmaxima;

    public Creador(Monitor monitor, int cantidad) {
        this.monitor = monitor;
        cantidadmaxima = cantidad;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish() && contador.get() < cantidadmaxima){
            contador.incrementAndGet();
            //Espera a poder tomar control del mutex del monitor para agregar la imagen al contenedor P0.
            monitor.addImage(new Image());
            //System.out.println(threadName + ": Nueva imagen creada con éxito.");
        }
    }

    public int getContador(){
        return contador.get();
    }
}
