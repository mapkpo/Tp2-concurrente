package main;

//import java.util.concurrent.TimeUnit;

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
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish()){
            //System.out.println(threadName + ": Buscando imagen para recortar.");
            Imagen img = monitor.startCut();

            // Si no hay imagenes para recortar, terminar
            if (img == null)
                break;

            //System.out.println(threadName + ": Inciando recorte.");

            img.recortar();
            monitor.finishCut(img);
            // System.out.println(threadName + ": Imagen recortada exitosamente.");
            contador++;
        }
    }

    public int getContador(){
        return contador;
    }
}
