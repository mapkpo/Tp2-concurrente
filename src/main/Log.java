package main;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log implements Runnable {
    int contador;
    Thread[] threadCreador;
    Thread[] threadCargadores;
    Thread[] threadAjustadores;
    Thread[] threadRecortadores;
    Thread[] threadExportador;
    Monitor monitor;
    File archivo;
    
    public Log(Thread[] threadCreador,Thread[] threadCargadores,Thread[] threadAjustadores,Thread[] threadRecortadores,Thread[] threadExportador, Monitor monitor){
        this.threadCreador = threadCreador;
        this.threadCargadores = threadCargadores;
        this.threadAjustadores = threadAjustadores;
        this.threadRecortadores = threadRecortadores;
        this.threadExportador = threadExportador;
        this.monitor = monitor;
        contador = 0;

        File directorio = new File("./Logs");
        if(!directorio.exists()){
            if (directorio.mkdirs()) {
            }
            else {
                System.out.println("Error al crear directorio");
            }
        }
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            String nombreArchivo = "log-" + dateFormat.format(new Date()) + ".txt";
            archivo = new File(directorio, nombreArchivo);
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
        }
        catch (IOException e) {
            System.out.println("Problema al crear el archivo de LOG.");
        }
    }

    @Override
    public void run() {
        while (!monitor.finalizarquestion()){
            try {
                escribir_archivo();
                this.contador++;
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        this.contador++;
        escribir_archivo();

    }


    private void escribir_archivo(){
        try {
            FileWriter escribir = new FileWriter(archivo, true);
            try {
                escribir.write("Iteración: " + contador + " tiempo: " + contador*500 + "ms\n");
                escribir.write("Imagenes creadas: " + monitor.getBufferP0() +"\n");
                escribir.write("Imagenes cargadas: "+ monitor.getBufferP6() +"\n");
                escribir.write("Imagenes ajustadas: "+ monitor.getBufferP14() +"\n");
                escribir.write("Imagenes recortadas: "+ monitor.getBufferP18() +"\n");
                escribir.write("Imagenes exportadas: "+ monitor.getBufferExportadas() +"\n");

                for (Thread thread:threadCreador){
                    escribir.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Thread thread:threadCargadores){
                    escribir.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Thread thread:threadAjustadores){
                    escribir.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Thread thread:threadRecortadores){
                    escribir.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                for (Thread thread:threadExportador){
                    escribir.write("Hilo: "+thread.getName() +". Estado: "+thread.getState()+"\n");
                }
                escribir.write("\n\n");
            }
            catch (IOException e){
                System.out.println("Problema al escribir en el archivo de LOG.");
            }
            finally {
                escribir.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    
}
