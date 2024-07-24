package main;

public class Adjuster implements Runnable{
    final Monitor monitor;
    String threadName;
    private int counter;

    public Adjuster(Monitor monitor) {
        this.monitor = monitor;
        counter = 0;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish()){
            //System.out.println(threadName + ": Buscando imagen para ajustar.");
            Image img = monitor.startAdjust();

            monitor.midAdjust();
            //System.out.println(threadName + ": Ajuste inicial finalizado exitosamente.");
            //System.out.println(threadName + ": Iniciando ajuste final.");

            monitor.finishAdjust(img);
            //System.out.println(threadName + ": Ajuste final finalizado exitosamente.");
            counter++;
        }
    }

    public int getCounter(){
        return counter;
    }
}
