package main;

public class Cargador implements Runnable{
    final Monitor monitor;
    String threadName;

    public Cargador(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();

        while (true){
            //System.out.println(threadName + ": Buscando imagen para cargar.");
            Imagen img = monitor.startcarga();

            monitor.finishcarga(img);
            //System.out.println(threadName + ": Imagen cargada exitósamente.");
        }
    }
}
