package main;

//import java.util.concurrent.TimeUnit;

public class Trimmer implements Runnable{
    final Monitor monitor;
    final Politic politic;
    String threadName;
    private int counter;

    public Trimmer(Monitor monitor, Politic politic) {
        this.monitor = monitor;
        this.politic = politic;
        counter = 0;
    }

    @Override
    public void run() {

        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish()){
            int firstTransition = politic.transitionNumber();
            int lastTransition = firstTransition + 2;
            if (!monitor.readRDP(firstTransition)){
                continue;
            }

            Image img = monitor.getImageFromContainer(2, firstTransition);
            if (img == null){
                continue;
            }

            img.trim();
            while (true) {
                if (monitor.addImageToContainer(3, lastTransition, img)){
                    break;
                }
            }
            counter++;
        }
    }

    public int getCounter(){
        return counter;
    }
}
