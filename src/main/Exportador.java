package main;

public class Exportador implements Runnable{
    final Monitor monitor;
    String threadName;

    public Exportador(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();

        while (true){
            //System.out.println(threadName + ": Buscando imagen para exportar.");
            Imagen img = monitor.startexporte();

            monitor.finishexport(img);
            //System.out.println(threadName + ": Imagen exportada exitósamente.");
        }
    }
}
