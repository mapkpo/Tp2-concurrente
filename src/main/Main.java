package main;

public class Main {
    public static void main(String[] args) {

    Monitor monitor = new Monitor();

    Creador creador = new Creador(monitor);

    Cargador cargador = new Cargador(monitor);
    Cargador cargador1 = new Cargador(monitor);
    Ajustador ajustador = new Ajustador(monitor);
    Ajustador ajustador1 = new Ajustador(monitor);
    Recortador recortador = new Recortador(monitor);
    Exportador exportador = new Exportador(monitor);

    Thread hiloCreador = new Thread(creador);
    Thread hiloCargador = new Thread(cargador);
    Thread hiloCargador1 = new Thread(cargador1);
    Thread hiloAjustador = new Thread(ajustador);
    Thread hiloAjustador1 = new Thread(ajustador1);
    Thread hiloRecortador = new Thread(recortador);
    Thread hiloExportador = new Thread(exportador);

    hiloCreador.start();
    hiloCargador.start();
    hiloCargador1.start();
    hiloAjustador.start();
    hiloAjustador1.start();
    hiloRecortador.start();
    hiloExportador.start();

    }
}