package main;

import java.util.concurrent.TimeUnit;

public class Exportador implements Runnable{
    
    final Monitor monitor;
    String threadName;
    private int contador;
    private final int max;

    public Exportador(Monitor monitor, int cantidad) {
        this.monitor = monitor;
        contador = 0;
        max = cantidad;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (contador < max && !monitor.finalizarquestion()){
            //System.out.println(threadName + ": Buscando imagen para exportar.");
            Imagen img = monitor.startexporte();

            monitor.finishexport(img);
            //System.out.println(threadName + ": Imagen exportada exitósamente.");
            contador++;

             try{
                TimeUnit.MILLISECONDS.sleep(0);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        monitor.finalizar();
    }

    public int getContador(){
        return contador;
    }
}
