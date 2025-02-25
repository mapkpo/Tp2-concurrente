package main;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        
        int maxFiresForT0 = 16; // Límite máximo de disparos para la transición 0
        Policy policy = new Policy(true);   //true es equitativo, false es 8020

        int creatorThreads = 1;
        int loaderThreadsLeft = 1;
        int loaderThreadsRight = 1;
        int adjustersThreadsLeft = 1;
        int adjustersThreadsRight = 1;
        int trimmersThreads = 1;
        int exportersThreads = 1;

        List<Integer> creatorTransitions = new ArrayList<>();
        creatorTransitions.add(0);

        List<Integer> leftLoaderTransitions = new ArrayList<>();
        leftLoaderTransitions.add(1);
        leftLoaderTransitions.add(3);

        List<Integer> rightLoaderTransitions = new ArrayList<>();
        rightLoaderTransitions.add(2);
        rightLoaderTransitions.add(4);

        List<Integer> leftAdjusterTransitions = new ArrayList<>();
        leftAdjusterTransitions.add(5);
        leftAdjusterTransitions.add(7);
        leftAdjusterTransitions.add(9);

        List<Integer> rightAdjusterTransitions = new ArrayList<>();
        rightAdjusterTransitions.add(6);
        rightAdjusterTransitions.add(8);
        rightAdjusterTransitions.add(10);

        List<Integer> leftTrimmerTransitions = new ArrayList<>();
        leftTrimmerTransitions.add(11);
        leftTrimmerTransitions.add(13);

        List<Integer> rightTrimmerTransitions = new ArrayList<>();
        rightTrimmerTransitions.add(12);
        rightTrimmerTransitions.add(14);

        List<Integer> exporterTransitions = new ArrayList<>();
        exporterTransitions.add(15);
        exporterTransitions.add(16);

        Rdp rdp = new Rdp(maxFiresForT0);
        Monitor monitor = new Monitor(rdp);

        Threads[] creators = new Threads[creatorThreads];
        Threads[] loadersLeft = new Threads[loaderThreadsLeft];
        Threads[] loadersRight = new Threads[loaderThreadsRight];
        Threads[] adjustersLeft = new Threads[adjustersThreadsLeft];
        Threads[] adjustersRight = new Threads[adjustersThreadsRight];
        Threads[] trimmers = new Threads[trimmersThreads];
        Threads[] exporters = new Threads[exportersThreads];

        for (int i = 0; i < creatorThreads; i++){
            creators[i] = new Threads(creatorTransitions, monitor);
            creators[i].setName("Creator " + i);
        }

        for (int i = 0; i < loaderThreadsLeft; i++){
            loadersLeft[i] = new Threads(leftLoaderTransitions, monitor);
            loadersLeft[i].setName("Loader left " + i);
        }

        for (int i = 0; i < loaderThreadsRight; i++){
            loadersRight[i] = new Threads(rightLoaderTransitions, monitor);
            loadersRight[i].setName("Loader right " + i);
        }

        for (int i = 0; i < adjustersThreadsLeft; i++){
            adjustersLeft[i] = new Threads(leftAdjusterTransitions, monitor);
            adjustersLeft[i].setName("Adjuster left " + i);
        }

        for (int i = 0; i < adjustersThreadsRight; i++){
            adjustersRight[i] = new Threads(rightAdjusterTransitions, monitor);
            adjustersRight[i].setName("Adjuster right " + i);
        }

        for (int i = 0; i < trimmersThreads; i++){
            trimmers[i] = new Threads(leftTrimmerTransitions, monitor);
            trimmers[i].setName("Trimmer left" + i);
        }

        for (int i = 0; i < trimmersThreads; i++){
            trimmers[i] = new Threads(rightTrimmerTransitions, monitor);
            trimmers[i].setName("Trimmer right" + i);
        }

        for (int i = 0; i < exportersThreads; i++){
            exporters[i] = new Threads(exporterTransitions, monitor);
            exporters[i].setName("Exporter " + i);
        }

        Log logger = new Log(creators, loadersLeft, loadersRight, adjustersLeft, adjustersRight, trimmers, exporters, monitor);
        new Thread(logger).start();

        for (int i = 0; i < creatorThreads; i++){
            creators[i].start();
        }

        for (int i = 0; i < loaderThreadsLeft; i++){
            loadersLeft[i].start();
        }

        for (int i = 0; i < loaderThreadsRight; i++){
            loadersRight[i].start();
        }

        for (int i = 0; i < adjustersThreadsLeft; i++){
            adjustersLeft[i].start();
        }

        for (int i = 0; i < adjustersThreadsRight; i++){
            adjustersRight[i].start();
        }

        for (int i = 0; i < trimmersThreads; i++){
            trimmers[i].start();
        }

        for (int i = 0; i < exportersThreads; i++){
            exporters[i].start();
        }
    }

}