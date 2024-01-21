package main;

public class Ajustador implements Runnable{
    final Monitor monitor;
    String threadName;

    public Ajustador(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();

        while (true){
            System.out.println(threadName + ": Buscando imagen para ajustar.");
            Imagen img = monitor.startajuste();

            System.out.println(threadName + ": Iniciando ajuste inicial.");
            try{
                Thread.sleep(1000);
            } catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }
            img.ajustarinicio();

            monitor.midajuste();
            System.out.println(threadName + ": Ajuste inicial finalizado exitosamente.");
            System.out.println(threadName + ": Iniciando ajuste final.");
            try{
                Thread.sleep(1000);
            } catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }
            img.ajustarfinal();

            monitor.finishajuste(img);
            System.out.println(threadName + ": Ajuste final finalizado exitosamente.");
        }
    }
}
