package main;

public class Loader implements Runnable{
    final Monitor monitor;
    String threadName;

    public Loader(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish()){
            //System.out.println(threadName + ": Buscando imagen para cargar.");
            Image img = monitor.startLoading();

            monitor.finishLoading(img);
            //System.out.println(threadName + ": Imagen cargada exitósamente.");
        }
    }
}
