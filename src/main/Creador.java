package main;

public class Creador implements Runnable{

    final Monitor monitor;
    String threadName;
    public Creador(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        while (true){
            //Duerme para representar el tiempo que le tomaría crear una nueva imagen.
            try{
                System.out.println(threadName + ": Intentando crear una nueva imagen.");
                Thread.sleep(1000);
            } catch (InterruptedException ex){
                System.out.println(ex.getMessage());
            }

            System.out.println(threadName + ": Nueva imagen creada con éxito.");
            System.out.println(threadName + ": Intentando pasar nueva imagen al sistema.");

            //Espera a poder tomar control del mutex del monitor para agregar la imagen al contenedor P0.
            monitor.addimagen(new Imagen());

            System.out.println(threadName + ": Nueva imagen agregada con éxito.");
        }
    }
}
