package main;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log implements Runnable {
    int count;
    Threads[] threadCreator;
    Threads[] threadLoader;
    Threads[] threadAdjusters;
    Threads[] threadTrimmers;
    Threads[] threadExporters;
    Monitor monitor;
    File file;
    File file1;
    final Long INITIAL_TIME = System.currentTimeMillis();
    
    public Log(Threads[] threadCreator, Threads[] threadLoader, Threads[] threadAdjusters, Threads[] threadTrimmers, Threads[] threadExporters, Monitor monitor){
        this.threadCreator = threadCreator;
        this.threadLoader = threadLoader;
        this.threadAdjusters = threadAdjusters;
        this.threadTrimmers = threadTrimmers;
        this.threadExporters = threadExporters;
        this.monitor = monitor;
        count = 0;

        File directory = new File("./Logs");
        if(!directory.exists()){
            if (!directory.mkdirs()) {
                System.out.println("Error al crear directorio");
            }
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String fileName = "log-" + dateFormat.format(new Date()) + ".txt";
            file = new File(directory, fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        }
        catch (IOException e) {
            System.out.println("Problema al crear el archivo de LOG.");
        }

        File directory1 = new File("./Secuencia");
        if(!directory1.exists()){
            if (!directory1.mkdirs()) {
                System.out.println("Error al crear directorio");
            } //if it aint broken dont fix it
        }
        try {
            String fileName1 = ("Secuencia") + ".txt";
            file1 = new File(directory1, fileName1);
            if (!file1.exists()) {
                file1.createNewFile();
            }
        }
        catch (IOException e) {
            System.out.println("Problema al crear el archivo de LOG.");
        }
    }

    @Override
    public void run() {
        while (!monitor.areInvariantsCompleted()){
            try {
                writeFile();
                this.count++;
                Threads.sleep(500);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.count++;
        writeFile();
        logWriteSequence();
        writeSequenceOnly();
    }


    private void writeFile(){
        try {
            FileWriter writer = new FileWriter(file, true);
            try {
                Long currentTime = System.currentTimeMillis();
                writer.write("Iteración: " + count + " tiempo: " + (currentTime - INITIAL_TIME) + "ms\n");
                writer.write("Imagenes creadas: "+ monitor.getRdp().getMarking(0) +"\n");
                writer.write("Imagenes cargadas: "+ monitor.getRdp().getMarking(6) +"\n");
                writer.write("Imagenes ajustadas: "+ monitor.getRdp().getMarking(14) +"\n");
                writer.write("Imagenes recortadas: "+ monitor.getRdp().getMarking(18) +"\n");
                writer.write("Imagenes exportadas: "+ monitor.getRdp().getFiredCounter()[16] +"\n");
                //writer.write(monitor.getBalanceCount() +"\n");

                for (Threads thread: threadCreator){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Threads thread: threadLoader){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Threads thread: threadAdjusters){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Threads thread: threadTrimmers){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Threads thread: threadExporters){
                    writer.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                writer.write("\n\n");
            }
            catch (IOException e){
                System.out.println("Problema al escribir en el archivo de LOG.");
            }
            finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void logWriteSequence(){
        try {
            FileWriter writer = new FileWriter(file, true);
            try {
                writer.write("Secuencia: "+ monitor.getRdp().getSequence() +"\n");
                
                writer.write("\n\n");
            }
            catch (IOException e){
                System.out.println("Problema al escribir en el archivo de LOG.");
            }
            finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeSequenceOnly(){
        try {
            FileWriter writer = new FileWriter(file1, false);
            try {
                writer.write(monitor.getRdp().getSequence()+"\n");
                }
            catch (IOException e){
                System.out.println("Problema al escribir en el archivo de LOG.");
            }
            finally {
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
