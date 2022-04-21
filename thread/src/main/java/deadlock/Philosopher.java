package deadlock;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 哲学家类
 */
public class Philosopher implements Runnable {
    public Chopstick left;
    public Chopstick right;
    public int ponderFactor;
    public int key;
    public Random random = new Random();

    /**
     * @throws InterruptedException
     */
    protected void pause() throws InterruptedException {
//        int sleepTime = random.nextInt(ponderFactor * 250);
//        System.out.println("sleep:" + sleepTime);
//        TimeUnit.MILLISECONDS.sleep(30);
    }

    /**
     * @param left
     * @param right
     * @param ponder
     */
    public Philosopher(Chopstick left, Chopstick right, int key,int ponder) {
        this.left = left;
        this.right = right;
        this.ponderFactor = ponder;
        this.key = key;
    }

    @Override
    public void run() {
        System.out.println(key + " begin thinking:");
        try {
            pause();
            System.out.println(key + " end thinking.");
            right.pick();
            left.pick();
            TimeUnit.MILLISECONDS.sleep(10);
//            pause();
            right.drop();
            left.drop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
