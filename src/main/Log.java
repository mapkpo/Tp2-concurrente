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
    File archivo1;
    final Long TIEMPO_INICIAL = System.currentTimeMillis();
    
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
            if (!directorio.mkdirs()) {
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

        File directorio1 = new File("./Secuencia");
        if(!directorio1.exists()){
            if (!directorio1.mkdirs()) {
                System.out.println("Error al crear directorio");
            } //if it aint broken dont fix it
        }
        try {
            String nombreArchivo1 = ("Secuencia") + ".txt";
            archivo1 = new File(directorio1, nombreArchivo1);
            if (!archivo1.exists()) {
                archivo1.createNewFile();
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
        escribir_secuencialog();
        escribir_secuenciasola();
    }


    private void escribir_archivo(){
        try {
            FileWriter escribir = new FileWriter(archivo, true);
            try {
                Long tiempo_actual = System.currentTimeMillis();
                escribir.write("Iteración: " + contador + " tiempo: " + (tiempo_actual - TIEMPO_INICIAL) + "ms\n"); //cambiarlo a tiempo del sistema
                escribir.write("Imagenes creadas: " + monitor.getBufferP0() +"\n");
                escribir.write("Imagenes cargadas: "+ monitor.getBufferP6() +"\n");
                escribir.write("Imagenes ajustadas: "+ monitor.getBufferP14() +"\n");
                escribir.write("Imagenes recortadas: "+ monitor.getBufferP18() +"\n");
                escribir.write("Imagenes exportadas: "+ monitor.getBufferExportadas() +"\n");
                escribir.write(monitor.getContadorBalanceo() +"\n");

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

    private void escribir_secuencialog(){
        try {
            FileWriter escribir = new FileWriter(archivo, true);
            try {
                escribir.write("Secuencia: "+ monitor.getSecuencia() +"\n");
                
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

    private void escribir_secuenciasola(){
        try {
            FileWriter escribir = new FileWriter(archivo1, false);
            try {
                escribir.write(monitor.getSecuencia()+"\n");
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
