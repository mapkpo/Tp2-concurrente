package main;
import java.util.concurrent.Semaphore;

public class Monitor {
    final private Rdp petri;
    final private Semaphore mutex;
    //El mutex ya da toda la proteccion necesaria, los demas semaforos estan para coordinacion unicamente.
    //final private Semaphore s_create;
    final private Semaphore s_proc;
    final private Semaphore s_ajuste;
    final private Semaphore s_recorte;
    final private Semaphore s_exporta;
    final private Politica politica;
    final private Semaphore s_create;
    

    private boolean invariantescompletados = false; //bandera para parar hilos
    long starttime;
    long endtime;
   
    Contenedor bufferentrada = new Contenedor();     //P0
    Contenedor bufferaprocesar = new Contenedor();  //P6
    Contenedor bufferajustadas = new Contenedor();   //P14
    Contenedor bufferlistas = new Contenedor();   //P18
    Contenedor bufferexportadas = new Contenedor();   //OUTPUT


    public Monitor(Politica _politica){
        petri = new Rdp();
        mutex = new Semaphore(1, true);
        s_proc = new Semaphore(3);          //P3
        s_ajuste = new Semaphore(2);        //P9
        s_recorte = new Semaphore(1);       //P15
        s_exporta = new Semaphore(1);       //P20
        politica = _politica;
        s_create = new Semaphore(1); //creo que es redundante

        starttime = System.currentTimeMillis();
    }

    /* Trys to acquire the mutex. */
    private void getmutex() {
        try{
            mutex.acquire();
        } catch (InterruptedException e){
            System.out.println("Monitor: interrupted while trying to acquire mutex: " + e);
        }
    }

    /* T0: Agrega una imagen al buffer de entrada. */
    public void addimagen(Imagen img) {
        while(true){
            /*try{
                s_create.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire mutex: " + e);
            }*/
            getmutex();
            if(petri.issensibilizada(0))
                break;
            //s_create.release();
            mutex.release();
        }

        petri.disparar(0);
        bufferentrada.agregar(img);
        s_create.release();
        mutex.release();
    }

    /* T1|T2: Toma una imagen del buffer de entrada. */
    public Imagen startcarga() {
        int T = 1;
        while(true){
            try{
                s_proc.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_proc: " + e);
            }
            getmutex();
            if(petri.issensibilizada(1))
                break;
            else if(petri.issensibilizada(2)){
                T = 2;
                break;
            }
            mutex.release();
            s_proc.release();
        }
        petri.disparar(T);
        Imagen to_process = bufferentrada.getImagen();
        mutex.release();
        return to_process;
    }

    /* T3|T4: Carga la imagen al buffer de imagenes a procesar. */
    public void finishcarga(Imagen img){
        int T = 3;
        while (true){
            getmutex();
            if(petri.issensibilizada(3))
                break;
            if(petri.issensibilizada(4)){
                T = 4;
                break;
            }
            mutex.release();
        }
        petri.disparar(T);
        bufferaprocesar.agregar(img);
        mutex.release();
        s_proc.release();
    }

    /* T5|T6: Toma una imagen del buffer a procesar. */
    public Imagen startajuste(){
        int T = 5;
        while (true){
            try{
                s_ajuste.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_ajuste: " + e);
            }
            getmutex();
            if(petri.issensibilizada(5))
                break;
            if(petri.issensibilizada(6)){
                T = 6;
                break;
            }
            s_ajuste.release();
            mutex.release();
        }
        petri.disparar(T);
        Imagen to_adjust = bufferaprocesar.getImagen();
        mutex.release();
        return to_adjust;
    }

    /* T7|T8: Dispara la transicion correspondiente en la RDP. */
    public void midajuste(){
        int T = 7;
        while (true){
            getmutex();
            if(petri.issensibilizada(7))
                break;
            if(petri.issensibilizada(8)){
                T = 8;
                break;
            }
            mutex.release();
        }
        petri.disparar(T);
        mutex.release();
    }

    /* T9|T10: Agrega una imagen ya ajustada al bufffer de ajustadas. */
    public void finishajuste(Imagen img){
        int T = 9;
        while (true){
            getmutex();
            if(petri.issensibilizada(9))
                break;
            if(petri.issensibilizada(10)){
                T = 10;
                break;
            }
            mutex.release();
        }
        petri.disparar(T);
        bufferajustadas.agregar(img);
        s_ajuste.release();
        mutex.release();
    }

    /* T11|T12: Toma una imagen para ser recortada. */
    public Imagen startrecorte(){
        int T;
        while (true){
            try{
                s_recorte.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_recorte: " + e);
            }
            getmutex();
            
            if(petri.issensibilizada(11) && petri.issensibilizada(12)){
                T = politica.numerodetransicion();
                break;
            }

            s_recorte.release();
            mutex.release();
        }
        petri.disparar(T);
        Imagen to_cut = bufferajustadas.getImagen();
        mutex.release();
        return to_cut;
    }

    /* T13|T14: Carga las imagenes ya recortadas al buffer final. */
    public void finishrecorte(Imagen img){
        int T = 13;
        while (true){
            getmutex();
            if(petri.issensibilizada(13))
                break;
            if(petri.issensibilizada(14)){
                T = 14;
                break;
            }
            mutex.release();
        }
        petri.disparar(T);
        bufferlistas.agregar(img);
        s_recorte.release();
        mutex.release();
    }

    /* T15: Toma una imagen para exportarla. */
    public Imagen startexporte(){
        while (true){
            try{
                s_exporta.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_exporta: " + e);
            }
            getmutex();
            if(petri.issensibilizada(15))
                break;
            s_exporta.release();
            mutex.release();
        }
        petri.disparar(15);
        Imagen to_export = bufferlistas.getImagen();
        mutex.release();
        return to_export;
    }

    /* T16: Carga la imagen en el buffer de exportadas. */
    public void finishexport(Imagen img){
        while (true){
            getmutex();
            if(petri.issensibilizada(16))
                break;
            mutex.release();
        }
        endtime = System.currentTimeMillis();
        petri.disparar(16);
        bufferexportadas.agregar(img);
        s_exporta.release();
        mutex.release();
    }

    public boolean finalizarquestion(){
        return invariantescompletados;
    }

    public void finalizar(){
        invariantescompletados = true;
        System.out.println("Programa finalizado con: " + getBufferExportadas() + " invariantes");
        petri.imprimircontador();
        //System.out.print(petri.getSecuencia());
    }

    public String getSecuencia(){
        return petri.getSecuencia();
    }

    public int getBufferP0(){
        return bufferentrada.getAgregadas();
    }

    public int getBufferP6(){
        return bufferaprocesar.getAgregadas();
    }

    public int getBufferP14(){
        return bufferajustadas.getAgregadas();
    }

    public int getBufferP18(){
        return bufferlistas.getAgregadas();
    }

    public int getBufferExportadas(){
        return bufferexportadas.getAgregadas();
    }

    public String getContadorBalanceo(){
        return petri.contadorString();
    }
}
