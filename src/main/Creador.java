package main;

//import java.util.concurrent.TimeUnit;

public class Creador implements Runnable{

    final Monitor monitor;
    String threadName;
    private int contador;

    public Creador(Monitor monitor) {
        this.monitor = monitor;
        contador = 0;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.finalizarquestion()){
            //Espera a poder tomar control del mutex del monitor para agregar la imagen al contenedor P0.
            monitor.addimagen(new Imagen());
            //System.out.println(threadName + ": Nueva imagen creada con éxito.");
            contador++;

            /*try{
                TimeUnit.MILLISECONDS.sleep(1);
            } catch(InterruptedException e){
                e.printStackTrace();
            }*/
        }
    }

    public int getContador(){
        return contador;
    }
}
