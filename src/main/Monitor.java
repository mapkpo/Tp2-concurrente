package main;
import java.util.concurrent.Semaphore;

public class Monitor {
    final private Rdp petri;
    final private Semaphore mutex;
    final private Semaphore semProcess;
    final private Semaphore semAdjust;
    final private Semaphore semCut;
    final private Semaphore semExport;
    final private Politic politic;
    final private Semaphore semCreate;
    

    private boolean allInvariatesCompleted = false; //bandera para parar hilos
    long startTime;
    long endTime;
   
    Contenedor bufferIn = new Contenedor();     //P0
    Contenedor bufferToProcess = new Contenedor();  //P6
    Contenedor bufferToAdjust = new Contenedor();   //P14
    Contenedor bufferReady = new Contenedor();   //P18
    Contenedor bufferExported = new Contenedor();   //OUTPUT


    public Monitor(Politic _politic){
        petri = new Rdp();
        mutex = new Semaphore(1, true);
        semProcess = new Semaphore(3);          //P3
        semAdjust = new Semaphore(2);        //P9
        semCut = new Semaphore(1);       //P15
        semExport = new Semaphore(1);       //P20
        politic = _politic;
        semCreate = new Semaphore(1); //creo que es redundante

        startTime = System.currentTimeMillis();
    }

    /* Tries to acquire the mutex. */
    private void getMutex() {
        try{
            mutex.acquire();
        } catch (InterruptedException e){
            System.out.println("Monitor: interrupted while trying to acquire mutex: " + e);
        }
    }

    /* T0: Agrega una imagen al buffer de entrada. */
    public void addImage(Imagen img) {
        while(true){
            getMutex();
            if(petri.isEnabled(0))
                break;
            mutex.release();
        }

        petri.fire(0);
        bufferIn.agregar(img);
        semCreate.release();
        mutex.release();
    }

    /* T1|T2: Toma una imagen del buffer de entrada. */
    public Imagen startLoading() {
        int T = 1;
        while(true){
            try{
                semProcess.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_proc: " + e);
            }
            getMutex();

            if(isReadyToFinish()){
                mutex.release();
                semProcess.release();
                return null;
            }

            if(petri.isEnabled(1))
                break;
            else if(petri.isEnabled(2)){
                T = 2;
                break;
            }
            mutex.release();
            semProcess.release();
        }
        petri.fire(T);
        Imagen toProcess = bufferIn.getImagen();
        mutex.release();
        return toProcess;
    }

    /* T3|T4: Carga la imagen al buffer de imagenes a procesar. */
    public void finishLoading(Imagen img){
        int T = 3;
        while (true){
            getMutex();
            if(isReadyToFinish()){
                mutex.release();
                return;
            }
            if(petri.isEnabled(3))
                break;
            if(petri.isEnabled(4)){
                T = 4;
                break;
            }
            mutex.release();
        }
        petri.fire(T);
        bufferToProcess.agregar(img);
        mutex.release();
        semProcess.release();
    }

    /* T5|T6: Toma una imagen del buffer a procesar. */
    public Imagen startAdjust(){
        int T = 5;
        while (true){
            try{
                semAdjust.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_ajuste: " + e);
            }
            getMutex();

            if(isReadyToFinish()){
                mutex.release();
                semAdjust.release();
                return null;
            }

            if(petri.isEnabled(5))
                break;
            if(petri.isEnabled(6)){
                T = 6;
                break;
            }
            semAdjust.release();
            mutex.release();
        }
        petri.fire(T);
        Imagen toAdjust = bufferToProcess.getImagen();
        mutex.release();
        return toAdjust;
    }

    /* T7|T8: Dispara la transicion correspondiente en la RDP. */
    public void midAdjust(){
        int T = 7;
        while (true){
            getMutex();

            if(isReadyToFinish()){
                mutex.release();
                semAdjust.release();
                return;
            }

            if(petri.isEnabled(7))
                break;
            if(petri.isEnabled(8)){
                T = 8;
                break;
            }
            mutex.release();
        }
        petri.fire(T);
        mutex.release();
    }

    /* T9|T10: Agrega una imagen ya ajustada al bufffer de ajustadas. */
    public void finishAdjust(Imagen img){
        int T = 9;
        while (true){
            getMutex();

            if(isReadyToFinish()){
                mutex.release();
                semAdjust.release();
                return;
            }

            if(petri.isEnabled(9))
                break;
            if(petri.isEnabled(10)){
                T = 10;
                break;
            }
            mutex.release();
        }
        petri.fire(T);
        bufferToAdjust.agregar(img);
        semAdjust.release();
        mutex.release();
    }

    /* T11|T12: Toma una imagen para ser recortada. */
    public Imagen startCut(){
        int T;
        while (true){
            try{
                semCut.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_recorte: " + e);
            }
            getMutex();
            
            // Verificacion de finalizacion
            if(isReadyToFinish()){
                semCut.release();
                mutex.release();
                return null;
            }

            if(petri.isEnabled(11) && petri.isEnabled(12)){
                T = politic.transitionNumber();
                break;
            }

            semCut.release();
            mutex.release();
        }
        petri.fire(T);
        Imagen toCut = bufferToAdjust.getImagen();
        mutex.release();
        return toCut;
    }
    

    /* T13|T14: Carga las imagenes ya recortadas al buffer final. */
    public void finishCut(Imagen img){
        int T = 13;
        while (true){
            getMutex();
            if(petri.isEnabled(13))
                break;
            if(petri.isEnabled(14)){
                T = 14;
                break;
            }
            mutex.release();
        }
        petri.fire(T);
        bufferReady.agregar(img);
        semCut.release();
        mutex.release();
    }

    /* T15: Toma una imagen para exportarla. */
    public Imagen startExport(){
        while (true){
            try{
                semExport.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_exporta: " + e);
            }
            getMutex();
            if(allInvariatesCompleted){
                semExport.release();
                mutex.release();
                return null;
            }
            if(petri.isEnabled(15))
                break;
            semExport.release();
            mutex.release();
        }
        petri.fire(15);
        Imagen toExport = bufferReady.getImagen();
        mutex.release();
        return toExport;
    }

    /* T16: Carga la imagen en el buffer de exportadas. */
    public void finishExport(Imagen img){
        while (true){
            getMutex();
            if(petri.isEnabled(16))
                break;
            mutex.release();
        }
        endTime = System.currentTimeMillis();
        petri.fire(16);
        bufferExported.agregar(img);
        semExport.release();
        mutex.release();
    }

    public boolean isReadyToFinish(){
        return allInvariatesCompleted;
    }

    public void finish(){
        allInvariatesCompleted = true;
        getMutex();
        System.out.println("Programa finalizado con: " + getBufferExported() + " invariantes");
        petri.printCounter();
        mutex.release();
        //System.out.print(petri.getSecuencia());
    }

    public String getSecuence(){
        return petri.getSequence();
    }

    public int getBufferP0(){
        return bufferIn.getAgregadas();
    }

    public int getBufferP6(){
        return bufferToProcess.getAgregadas();
    }

    public int getBufferP14(){
        return bufferToAdjust.getAgregadas();
    }

    public int getBufferP18(){
        return bufferReady.getAgregadas();
    }

    public int getBufferExported(){
        return bufferExported.getAgregadas();
    }

    public String getBalanceCount(){
        return petri.counterString();
    }
}
