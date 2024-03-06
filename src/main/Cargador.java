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

            //System.out.println(threadName + ": Iniciando carga.");
            //Simula tiempo de carga.
            try{
                Thread.sleep(10);
            } catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }

            monitor.finishcarga(img);
            //System.out.println(threadName + ": Imagen cargada exitósamente.");
        }
    }
}
