package main;

public class Ajustador implements Runnable{
    final Monitor monitor;
    String threadName;
    private int contador;

    public Ajustador(Monitor monitor) {
        this.monitor = monitor;
        contador = 0;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();

        while (true){
            //System.out.println(threadName + ": Buscando imagen para ajustar.");
            Imagen img = monitor.startajuste();

            monitor.midajuste();
            //System.out.println(threadName + ": Ajuste inicial finalizado exitosamente.");
            //System.out.println(threadName + ": Iniciando ajuste final.");

            monitor.finishajuste(img);
            //System.out.println(threadName + ": Ajuste final finalizado exitosamente.");
            contador++;
        }
    }

    public int getContador(){
        return contador;
    }
}
