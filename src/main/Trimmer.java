package main;

//import java.util.concurrent.TimeUnit;

public class Trimmer implements Runnable{
    final Monitor monitor;
    String threadName;
    private int counter;

    public Trimmer(Monitor monitor) {
        this.monitor = monitor;
        counter = 0;
    }

    @Override
    public void run() {

        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish()){
            //System.out.println(threadName + ": Buscando imagen para recortar.");
            Image img = monitor.startTrimming();

            // Si no hay imagenes para recortar, terminar
            if (img == null)
                break;

            //System.out.println(threadName + ": Inciando recorte.");

            img.trim();
            monitor.finishTrim(img);
            // System.out.println(threadName + ": Imagen recortada exitosamente.");
            counter++;
        }
    }

    public int getCounter(){
        return counter;
    }
}
