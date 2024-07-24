package main;
import java.util.concurrent.Semaphore;

public class Monitor {
    final private Rdp petri;
    final private Semaphore mutex;
    final private Semaphore semProcess;
    final private Semaphore semAdjust;
    final private Semaphore semTrim;
    final private Semaphore semExport;
    final private Politic politic;
    final private Semaphore semCreate;
    

    private boolean allInvariantsCompleted = false; //bandera para parar hilos
    long startTime;
    long endTime;
   
    Container bufferIn = new Container();     //P0
    Container bufferToProcess = new Container();  //P6
    Container bufferToAdjust = new Container();   //P14
    Container bufferReady = new Container();   //P18
    Container bufferExported = new Container();   //OUTPUT


    public Monitor(Politic _politic){
        petri = new Rdp();
        mutex = new Semaphore(1, true);
        semProcess = new Semaphore(3);          //P3
        semAdjust = new Semaphore(2);        //P9
        semTrim = new Semaphore(1);       //P15
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
    public void addImage(Image img) {
        while(true){
            getMutex();
            if(petri.isEnabled(0))
                break;
            mutex.release();
        }

        petri.fire(0);
        bufferIn.add(img);
        semCreate.release();
        mutex.release();
    }

    /* T1|T2: Toma una imagen del buffer de entrada. */
    public Image startLoading() {
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
        Image toProcess = bufferIn.getImage();
        mutex.release();
        return toProcess;
    }

    /* T3|T4: Carga la imagen al buffer de imagenes a procesar. */
    public void finishLoading(Image img){
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
        bufferToProcess.add(img);
        mutex.release();
        semProcess.release();
    }

    /* T5|T6: Toma una imagen del buffer a procesar. */
    public Image startAdjust(){
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
        Image toAdjust = bufferToProcess.getImage();
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
    public void finishAdjust(Image img){
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
        bufferToAdjust.add(img);
        semAdjust.release();
        mutex.release();
    }

    /* T11|T12: Toma una imagen para ser recortada. */
    public Image startTrimming(){
        int T;
        while (true){
            try{
                semTrim.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_recorte: " + e);
            }
            getMutex();
            
            // Verificacion de finalizacion
            if(isReadyToFinish()){
                semTrim.release();
                mutex.release();
                return null;
            }

            if(petri.isEnabled(11) && petri.isEnabled(12)){
                T = politic.transitionNumber();
                break;
            }

            semTrim.release();
            mutex.release();
        }
        petri.fire(T);
        Image toTrim = bufferToAdjust.getImage();
        mutex.release();
        return toTrim;
    }
    

    /* T13|T14: Carga las imagenes ya recortadas al buffer final. */
    public void finishTrim(Image img){
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
        bufferReady.add(img);
        semTrim.release();
        mutex.release();
    }

    /* T15: Toma una imagen para exportarla. */
    public Image startExport(){
        while (true){
            try{
                semExport.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_exporta: " + e);
            }
            getMutex();
            if(allInvariantsCompleted){
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
        Image toExport = bufferReady.getImage();
        mutex.release();
        return toExport;
    }

    /* T16: Carga la imagen en el buffer de exportadas. */
    public void finishExport(Image img){
        while (true){
            getMutex();
            if(petri.isEnabled(16))
                break;
            mutex.release();
        }
        endTime = System.currentTimeMillis();
        petri.fire(16);
        bufferExported.add(img);
        semExport.release();
        mutex.release();
    }

    public boolean isReadyToFinish(){
        return allInvariantsCompleted;
    }

    public void finish(){
        allInvariantsCompleted = true;
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
        return bufferIn.getAdded();
    }

    public int getBufferP6(){
        return bufferToProcess.getAdded();
    }

    public int getBufferP14(){
        return bufferToAdjust.getAdded();
    }

    public int getBufferP18(){
        return bufferReady.getAdded();
    }

    public int getBufferExported(){
        return bufferExported.getAdded();
    }

    public String getBalanceCount(){
        return petri.counterString();
    }
}
