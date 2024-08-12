package main;

public class Loader implements Runnable{
    final Monitor monitor;
    String threadName;

    public Loader(Monitor monitor) {
        this.monitor = monitor;
    }

    @Override
    public void run() {
        threadName = Thread.currentThread().getName();
        System.out.printf("%s inicializado\n", threadName);

        while (!monitor.isReadyToFinish()){
            int firstTransition = 1;
            if (!monitor.readRDP(firstTransition))
                firstTransition = 2;
            if (!monitor.readRDP(firstTransition))
                continue;

            int nextTransition = firstTransition + 2;
            Image img = monitor.getImageFromContainer(0, firstTransition);
            if (img == null){
                continue;
            }

            // Can modify Image if necessary
            while (true){
                if (monitor.addImageToContainer(1, nextTransition, img))
                    break;
            }
        }
    }
}
