package main;

public class Recortador implements Runnable{
    final Monitor monitor;
    String threadName;

    public Recortador(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {

        threadName = Thread.currentThread().getName();

        while (true){
            //System.out.println(threadName + ": Buscando imagen para recortar.");
            Imagen img = monitor.startrecorte();

            //System.out.println(threadName + ": Inciando recorte.");
            try{
                Thread.sleep(1);
            } catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }

            img.recortar();
            monitor.finishrecorte(img);
           // System.out.println(threadName + ": Imagen recortada exitosamente.");
        }
    }
}
