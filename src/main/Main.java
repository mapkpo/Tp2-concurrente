package main;

public class Main {
    public static void main(String[] args) {

        final int setpolitica = 1;     //politica 1 es 50/50, 2 es 80/20
        final int numerodeimagenesaprocesar = 20;  //numero de invariantes que buscamos, 
        final int numhilos1 = 2;    //cargador, ajustador, recortador
        final int numhilos2 = 1;    //creador, exportador

        Politica politica = new Politica(setpolitica); 
        Monitor monitor = new Monitor(politica);

        //usando el patron factory thread así ludemman puede romper el codigo mas fácil jaja
        Creador[] creador = new Creador[numhilos2];
        Thread[] threadCreador = new Thread[numhilos2];

        Cargador[] cargadores = new Cargador[numhilos1];
        Thread[] threadCargadores = new Thread[numhilos1];

        Ajustador[] ajustadores = new Ajustador[numhilos1];
        Thread[] threadAjustadores = new Thread[numhilos1];

        Recortador[] recortadores = new Recortador[numhilos1];
        Thread[] threadRecortadores = new Thread[numhilos1];

        Exportador[] exportador = new Exportador[numhilos2];
        Thread[] threadExportador = new Thread[numhilos2];

        for(int i = 0; i < numhilos1; i++){
            cargadores[i] = new Cargador(monitor);
            threadCargadores[i] = new Thread(cargadores[i]);
            threadCargadores[i].setName("Cargador: " + i);

            ajustadores[i] = new Ajustador(monitor);
            threadAjustadores[i] = new Thread(ajustadores[i]);
            threadAjustadores[i].setName("Ajustador: " + i);

            recortadores[i] = new Recortador(monitor);
            threadRecortadores[i] = new Thread(recortadores[i]);
            threadRecortadores[i].setName("Recortador: " + i);
        }

        for(int i = 0; i < numhilos2; i++){
            creador[i] = new Creador(monitor);
            threadCreador[i] = new Thread(creador[i]);
            threadCreador[i].setName("Creador: " + i);

            exportador[i] = new Exportador(monitor, numerodeimagenesaprocesar);
            threadExportador[i] = new Thread(exportador[i]);
            threadExportador[i].setName("Exportador: " + i);
        }

        Log log = new Log(threadCreador, threadCargadores, threadAjustadores, threadRecortadores, threadExportador, monitor);
        new Thread(log).start();

        for(int i = 0; i < numhilos1; i++){
            threadCargadores[i].start();
            threadAjustadores[i].start();
            threadRecortadores[i].start();
        }

        for(int i = 0; i < numhilos2; i++){
            threadCreador[i].start();
            threadExportador[i].start();
        }
    }
}