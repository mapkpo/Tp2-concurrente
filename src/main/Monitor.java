package main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Monitor {
    final private Rdp petri;
    final private Semaphore mutex;
    //El mutex ya da toda la proteccion necesaria, los demas semaforos estan para coordinacion unicamente.
    //final private Semaphore s_create;
    final private Semaphore s_proc;
    final private Semaphore s_ajuste;

    //Todo: Usar una clase contenedor en lugar de listas?
    List<Imagen> BufferEntrada = new ArrayList<>();     //P0
    List<Imagen> BufferProcesadas = new ArrayList<>();  //P6
    List<Imagen> BufferAjustadas = new ArrayList<>();   //P14

    public Monitor(){
        petri = new Rdp();
        mutex = new Semaphore(1, true);
        s_proc = new Semaphore(3);
        s_ajuste = new Semaphore(2);
        //s_create = new Semaphore(1);
    }

    /* Trys to acquire the mutex. */
    private void GetMutex()
    {
        try{
            mutex.acquire();
        } catch (InterruptedException e){
            System.out.println("Monitor: interrupted while trying to acquire mutex: " + e);
        }
    }

    /* T0: Agrega una imagen al buffer de entrada. */
    public void AddImagen(Imagen img) {
        //Todo: usar un semaforo para bloquear la adicion de mas imagenes
        //  cuando se puedan hacer otras cosas (s_create).
        GetMutex();

        //No reviso si T0 esta sensibilizada porque ya sabemos que siempre esta sensibilizada
        BufferEntrada.add(img);
        petri.disparar(0);

        mutex.release();
    }

    /* T1|T2: Toma una imagen del buffer de entrada. */
    public Imagen StartPocessing() {
        int T = 1;
        while(true){
            //Todo: Quizas agregar otro semaforo que simule P3 para evitar que bloquee los
            // otros semaforos si no va a poder hacer nada.
            try{
                s_proc.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_proc: " + e);
            }
            GetMutex();
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

        Imagen to_process = BufferEntrada.get(0);
        BufferEntrada.remove(to_process);

        mutex.release();
        return to_process;
    }

    /* T3|T4: Agrega una imagen ya procesada al buffer de procesadas. */
    public void FinishProcessing(Imagen img){
        int T = 3;
        while (true){
            GetMutex();
            if(petri.issensibilizada(3))
                break;
            if(petri.issensibilizada(4)){
                T = 4;
                break;
            }

            mutex.release();
        }

        petri.disparar(T);
        BufferProcesadas.add(img);

        mutex.release();
        s_proc.release();
    }

    /* T5|T6: Toma una imagen del buffer de procesadas. */
    public Imagen StartAjuste(){
        int T = 5;
        while (true){
            try{
                s_ajuste.acquire();
            } catch (InterruptedException e){
                System.out.println("Monitor: interrupted while trying to acquire s_ajuste: " + e);
            }
            GetMutex();
            if(petri.issensibilizada(5))
                break;
            if(petri.issensibilizada(6)){
                T = 6;
                break;
            }
            s_ajuste.release();
            mutex.release();
        }

        Imagen to_adjust = BufferProcesadas.get(0);
        BufferProcesadas.remove(to_adjust);

        mutex.release();
        return to_adjust;
    }

    /* T7|T8: Dispara la transicion correspondiente en la RDP. */
    public void MidAjuste(){
        int T = 7;
        while (true){
            GetMutex();
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
    public void FinishAjuste(Imagen img){
        int T = 9;
        while (true){
            GetMutex();
            if(petri.issensibilizada(9))
                break;
            if(petri.issensibilizada(10)){
                T = 10;
                break;
            }
            mutex.release();
        }

        petri.disparar(T);
        BufferAjustadas.add(img);

        s_ajuste.release();
        mutex.release();
    }
}
