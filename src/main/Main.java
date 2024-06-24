package main;

public class Main {
    public static void main(String[] args) {

        int setpolitica = 1;     //politica 1 es 50/50, 2 es 80/20
        int numerodeimagenesaprocesar = 20;

        Politica politica = new Politica(setpolitica); 

        Monitor monitor = new Monitor(politica);

        Creador creador = new Creador(monitor,numerodeimagenesaprocesar);
        Cargador cargador = new Cargador(monitor);
        Cargador cargador1 = new Cargador(monitor);
        Ajustador ajustador = new Ajustador(monitor);
        Ajustador ajustador1 = new Ajustador(monitor);
        Recortador recortador = new Recortador(monitor);
        Recortador recortador1 = new Recortador(monitor);
        Exportador exportador = new Exportador(monitor);

        Thread hiloCreador = new Thread(creador);
        Thread hiloCargador = new Thread(cargador);
        Thread hiloCargador1 = new Thread(cargador1);
        Thread hiloAjustador = new Thread(ajustador);
        Thread hiloAjustador1 = new Thread(ajustador1);
        Thread hiloRecortador = new Thread(recortador);
        Thread hiloRecortador1 = new Thread(recortador1);
        Thread hiloExportador = new Thread(exportador);

        hiloCreador.start();
        hiloCargador.start();
        hiloCargador1.start();
        hiloAjustador.start();
        hiloAjustador1.start();
        hiloRecortador.start();
        hiloRecortador1.start();
        hiloExportador.start();
    }
}