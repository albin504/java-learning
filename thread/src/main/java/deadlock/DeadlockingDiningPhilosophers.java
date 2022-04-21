package deadlock;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeadlockingDiningPhilosophers {
    public static void main(String[] args) throws IOException {
        int ponder = 5;
        if (args.length > 0) {
            ponder = Integer.parseInt(args[0]);
        }
        System.out.println("ponder:" + ponder);

        int size = 2;
        if (args.length > 1) {
            size = Integer.parseInt(args[1]);
        }
        Chopstick[] chopsticks = new Chopstick[size];
        for (int i = 0; i < size; i++) {
            chopsticks[i] = new Chopstick(i);
        }
        Philosopher[] philosophers = new Philosopher[size];
        for (int i = 0; i < size; i++) {
            philosophers[i] = new Philosopher(chopsticks[i], chopsticks[(i + 1) % size],i, ponder);
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < size; i++) {
            executorService.execute(philosophers[i]);
        }

        System.out.println("Press `enter` to quit");
        System.in.read();
        executorService.shutdownNow();
    }
}
