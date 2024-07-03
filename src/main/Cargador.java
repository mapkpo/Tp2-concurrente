package main;

//import java.util.concurrent.TimeUnit;

public class Cargador implements Runnable{
    final Monitor monitor;
    String threadName;

    public Cargador(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.finalizarquestion()){
            //System.out.println(threadName + ": Buscando imagen para cargar.");
            Imagen img = monitor.startcarga();

            monitor.finishcarga(img);
            //System.out.println(threadName + ": Imagen cargada exitósamente.");


             /*try{
                TimeUnit.MILLISECONDS.sleep(1);
            } catch(InterruptedException e){
                e.printStackTrace();
            }*/
        }
    }
}
