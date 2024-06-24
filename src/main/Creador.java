package main;

public class Creador implements Runnable{

    final Monitor monitor;
    String threadName;
    private final int max;
    private int contador;

    public Creador(Monitor monitor, int max) {
        this.monitor = monitor;
        this.max = max;
        contador = 0;
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
            contador++;
        }
    }

    public int getContador(){
        return contador;
    }
}
