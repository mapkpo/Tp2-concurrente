package main;

public class Creador implements Runnable{

    final Monitor monitor;
    String threadName;
    final int max;
    public Creador(Monitor monitor, int max) {
        this.monitor = monitor;
        this.max = max;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        int count = 0;
        while (count != max){
            //Espera a poder tomar control del mutex del monitor para agregar la imagen al contenedor P0.
            monitor.addimagen(new Imagen());
            //System.out.println(threadName + ": Nueva imagen creada con éxito.");
            count++;
        }
    }
}
