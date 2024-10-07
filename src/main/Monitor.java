package main;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Monitor {
    private final Rdp petri;
    // Mutex protege el acceso a los contenedores
    final private Semaphore mutex;
    final private Politic politic;
    final private ReentrantReadWriteLock petriRWLock;

    private boolean allInvariantsCompleted = false; //bandera para parar hilos
    long startTime;
    long endTime;

    Container[] buffers;
    Semaphore[] containerSems;

    public Monitor(Politic _politic, int containersAmount){
        petri = new Rdp();
        mutex = new Semaphore(1, true);
        politic = _politic;

        petriRWLock = new ReentrantReadWriteLock(true);
        buffers = new Container[containersAmount];
        containerSems = new Semaphore[containersAmount];
        for (int i = 0; i < containersAmount; i++){
            buffers[i] = new Container();
            containerSems[i] = new Semaphore(1,true);
        }

        startTime = System.currentTimeMillis();
    }

    /* Tries to acquire the mutex. */
    private void getMutex() throws InterruptedException {
        mutex.acquire();
    }

    private boolean writeRDP(int transition){
        try {
            petriRWLock.writeLock().lock();
        } catch (Exception e){
            return false;
        }
        if (!petri.isEnabled(transition)) {
            petriRWLock.writeLock().unlock();
            return false;
        }
        petri.fire(transition);
        petriRWLock.writeLock().unlock();
        return true;
    }

    public boolean shootTransition(int transition){
        try {
            mutex.acquire();
        } catch (InterruptedException e){
            return false;
        }
        boolean toReturn = writeRDP(transition);
        mutex.release();
        return toReturn;
    }

    public boolean readRDP(int transition){
        try {
            petriRWLock.readLock().lock();
        } catch (Exception e){
            return false;
        }
        boolean toReturn = petri.isEnabled(transition);
        petriRWLock.readLock().unlock();
        return toReturn;
    }

    public boolean addImageToContainer(int containerNum, int transition, Image image){
        try {
            containerSems[containerNum].acquire();
        } catch (Exception e){
            return false;
        }

        try {
            mutex.acquire();
        } catch (InterruptedException e){
            containerSems[containerNum].release();
            return false;
        }

        if (!writeRDP(transition)) {
            containerSems[containerNum].release();
            mutex.release();
            return false;
        }

        buffers[containerNum].add(image);
        containerSems[containerNum].release();
        mutex.release();
        return true;
    }

    public Image getImageFromContainer(int containerNum, int transition){
        try {
            containerSems[containerNum].acquire();
        } catch (Exception e){
            return null;
        }

        try {
            mutex.acquire();
        } catch (InterruptedException e){
            containerSems[containerNum].release();
            return null;
        }

        // Hay realmente una imagen para tomar?
        Image toReturn = buffers[containerNum].getImage();
        if (toReturn == null){
            containerSems[containerNum].release();
            mutex.release();
            return null;
        }

        //Está realmente sensibilizada la transición?
        if (!writeRDP(transition)) {
            // Sino volvemos a guardar la imágen
            buffers[containerNum].silentAdd(toReturn);
            containerSems[containerNum].release();
            mutex.release();
            return null;
        }

        containerSems[containerNum].release();
        mutex.release();
        return toReturn;
    }

    public boolean isReadyToFinish(){
        return allInvariantsCompleted;
    }

    public void finish(){
        allInvariantsCompleted = true;
        try {
            getMutex();
        } catch (InterruptedException ignored){

        }
        System.out.println("Programa finalizado con: " + buffers[buffers.length - 1].getAdded() + " invariantes");
        petri.printCounter();
        mutex.release();
        //System.out.print(petri.getSecuencia());
    }

    public String getSequence(){
        return petri.getSequence();
    }

    public int getBufferCount(int bufferNum){
        return buffers[bufferNum].getAdded();
    }

    public String getBalanceCount(){
        return petri.counterString();
    }
}
