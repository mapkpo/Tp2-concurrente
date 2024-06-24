package main;

public class Recortador implements Runnable{
    final Monitor monitor;
    String threadName;
    private int contador;

    public Recortador(Monitor monitor) {
        this.monitor = monitor;
        contador = 0;
    }

    @Override
    public void run() {

        threadName = Thread.currentThread().getName();

        while (true){
            //System.out.println(threadName + ": Buscando imagen para recortar.");
            Imagen img = monitor.startrecorte();

            //System.out.println(threadName + ": Inciando recorte.");

            img.recortar();
            monitor.finishrecorte(img);
           // System.out.println(threadName + ": Imagen recortada exitosamente.");
            contador++;
        }
    }

    public int getContador(){
        return contador;
    }
}
