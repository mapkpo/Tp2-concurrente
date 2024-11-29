package main;

public class Adjuster implements Runnable{
    final Monitor monitor;
    String threadName;
    private int counter;

    public Adjuster(Monitor monitor) {
        this.monitor = monitor;
        counter = 0;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish()){
            int firstTransition = 5;
            if (!monitor.readRDP(firstTransition))
                firstTransition = 6;
            if (!monitor.readRDP(firstTransition))
                continue;

            int midTransition = firstTransition + 2;
            int finalTransition = midTransition + 2;

            Image img = monitor.getImageFromContainer(1, firstTransition);
            if (img == null){
                continue;
            }

            img.firstAdjustment();
            while (true) {
                if (monitor.shootTransition(midTransition)){
                    break;
                }
            }
            img.finalAdjustment();
            while (true) {
                if (monitor.addImageToContainer(2, finalTransition, img)){
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
