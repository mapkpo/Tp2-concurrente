package main;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Límite máximo de disparos para la transición 0
        int maxFiresForT0 = 200;

        // Número de threads que ejecutan cada tarea. Idealmente todos son 1.
        int creatorThreads = 100;
        int loaderThreadsLeft = 100;
        int loaderThreadsRight = 100;
        int adjustersThreadsLeft = 100;
        int adjustersThreadsRight = 100;
        int trimmersThreadsLeft = 100;
        int trimmersThreadsRight = 100;
        int exportersThreads = 100;

        // Declaramos las transiciones que dispara cada hilo en orden

        /// CREADOR = T0
        List<Integer> creatorTransitions = new ArrayList<>();
        creatorTransitions.add(0);

        /// LOADER LEFT = T1 -> T3
        List<Integer> leftLoaderTransitions = new ArrayList<>();
        leftLoaderTransitions.add(1);
        leftLoaderTransitions.add(3);

        /// LOADER RIGHT = T3 -> T4
        List<Integer> rightLoaderTransitions = new ArrayList<>();
        rightLoaderTransitions.add(2);
        rightLoaderTransitions.add(4);

        /// ADJUSTER LEFT = T5 -> T7 -> T9
        List<Integer> leftAdjusterTransitions = new ArrayList<>();
        leftAdjusterTransitions.add(5);
        leftAdjusterTransitions.add(7);
        leftAdjusterTransitions.add(9);

        /// ADJUSTER RIGHT = T6 -> T8 -> T10
        List<Integer> rightAdjusterTransitions = new ArrayList<>();
        rightAdjusterTransitions.add(6);
        rightAdjusterTransitions.add(8);
        rightAdjusterTransitions.add(10);

        /// TRIMMER LEFT = T11 -> T13
        List<Integer> leftTrimmerTransitions = new ArrayList<>();
        leftTrimmerTransitions.add(11);
        leftTrimmerTransitions.add(13);

        /// TRIMMER RIGHT = T12 -> T14
        List<Integer> rightTrimmerTransitions = new ArrayList<>();
        rightTrimmerTransitions.add(12);
        rightTrimmerTransitions.add(14);

        /// EXPORTER = T15 -> T16
        List<Integer> exporterTransitions = new ArrayList<>();
        exporterTransitions.add(15);
        exporterTransitions.add(16);

        // Inicializamos el monitor con la política y la RDP
        Rdp rdp = new Rdp(maxFiresForT0);
        Policy policy = new Policy(true);   // true es equitativo, false es 8020
        Monitor monitor = new Monitor(rdp, policy);

        // Creamos arreglos para cada tipo de thread
        Threads[] creators = new Threads[creatorThreads];
        Threads[] loadersLeft = new Threads[loaderThreadsLeft];
        Threads[] loadersRight = new Threads[loaderThreadsRight];
        Threads[] adjustersLeft = new Threads[adjustersThreadsLeft];
        Threads[] adjustersRight = new Threads[adjustersThreadsRight];
        Threads[] trimmersLeft = new Threads[trimmersThreadsRight];
        Threads[] trimmersRight = new Threads[trimmersThreadsRight];
        Threads[] exporters = new Threads[exportersThreads];

        // Creamos los threads

        /// CREATORS
        for (int i = 0; i < creatorThreads; i++){
            creators[i] = new Threads(creatorTransitions, monitor);
            creators[i].setName("Creator " + i);
        }

        /// LOADERS LEFT
        for (int i = 0; i < loaderThreadsLeft; i++){
            loadersLeft[i] = new Threads(leftLoaderTransitions, monitor);
            loadersLeft[i].setName("Loader left " + i);
        }

        /// LOADERS RIGHT
        for (int i = 0; i < loaderThreadsRight; i++){
            loadersRight[i] = new Threads(rightLoaderTransitions, monitor);
            loadersRight[i].setName("Loader right " + i);
        }

        /// ADJUSTERS LEFT
        for (int i = 0; i < adjustersThreadsLeft; i++){
            adjustersLeft[i] = new Threads(leftAdjusterTransitions, monitor);
            adjustersLeft[i].setName("Adjuster left " + i);
        }

        /// ADJUSTERS RIGHT
        for (int i = 0; i < adjustersThreadsRight; i++){
            adjustersRight[i] = new Threads(rightAdjusterTransitions, monitor);
            adjustersRight[i].setName("Adjuster right " + i);
        }

        /// TRIMMERS LEFT
        for (int i = 0; i < trimmersThreadsLeft; i++){
            trimmersLeft[i] = new Threads(leftTrimmerTransitions, monitor);
            trimmersLeft[i].setName("Trimmer left" + i);
        }

        /// TRIMMERS RIGHT
        for (int i = 0; i < trimmersThreadsRight; i++){
            trimmersRight[i] = new Threads(rightTrimmerTransitions, monitor);
            trimmersRight[i].setName("Trimmer right" + i);
        }

        /// EXPORTERS
        for (int i = 0; i < exportersThreads; i++){
            exporters[i] = new Threads(exporterTransitions, monitor);
            exporters[i].setName("Exporter " + i);
        }

        // Inicializamos el hilo logger
        Log logger = new Log(creators, loadersLeft, loadersRight, adjustersLeft, adjustersRight, trimmersLeft, trimmersRight, exporters, monitor);
        new Thread(logger).start();

        // Los hilos inician sus tareas

        /// CREATORS
        for (int i = 0; i < creatorThreads; i++){
            creators[i].start();
        }

        /// LOADERS LEFT
        for (int i = 0; i < loaderThreadsLeft; i++){
            loadersLeft[i].start();
        }

        /// LOADERS RIGHT
        for (int i = 0; i < loaderThreadsRight; i++){
            loadersRight[i].start();
        }

        /// ADJUSTERS LEFT
        for (int i = 0; i < adjustersThreadsLeft; i++){
            adjustersLeft[i].start();
        }

        /// ADJUSTERS RIGHT
        for (int i = 0; i < adjustersThreadsRight; i++){
            adjustersRight[i].start();
        }

        /// TRIMMERS LEFT
        for (int i = 0; i < trimmersThreadsLeft; i++){
            trimmersLeft[i].start();
        }

        /// TRIMMERS RIGHT
        for (int i = 0; i < trimmersThreadsRight; i++){
            trimmersRight[i].start();
        }

        /// EXPORTERS
        for (int i = 0; i < exportersThreads; i++){
            exporters[i].start();
        }
    }

}