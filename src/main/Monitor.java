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

    //Todo: Usar una clase contenedor en lugar de listas?
    List<Imagen> BufferEntrada = new ArrayList<>();         //P0
    List<Imagen> BufferProcesamiento = new ArrayList<>();   //P6

    public Monitor(){
        petri = new Rdp();
        mutex = new Semaphore(1, true);
        s_proc = new Semaphore(3);
        //s_create = new Semaphore(1);
    }

    private void GetMutex()
    {
        try{
            mutex.acquire();
        } catch (InterruptedException e){
            System.out.println("Monitor: interrupted while trying to acquire mutex: " + e);
        }
    }

    //T0
    public void AddImagen(Imagen img)
    {
        //Todo: usar un semaforo para bloquear la adicion de mas imagenes
        //  cuando se puedan hacer otras cosas (s_create).
        GetMutex();

        //No reviso si T0 esta sensibilizada porque ya sabemos que siempre esta sensibilizada
        BufferEntrada.add(img);
        petri.disparar(0);

        mutex.release();
    }

    //T1 y T2
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
        BufferEntrada.remove(0);

        mutex.release();
        return to_process;
    }

    //T3 y T4
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
        BufferProcesamiento.add(img);

        mutex.release();
        s_proc.release();
    }
    

}
