package deadlock;

/**
 * 筷子类
 */
public class Chopstick {
    public boolean isHold;
    public int key;
    public Chopstick(int key) {
        this.key = key;
    }

    public synchronized void pick() throws InterruptedException {
        while (isHold) {
            System.out.println(key + " wait lock");
            wait();
        }
        this.isHold = true;
        System.out.println(key + " get lock");
    }

    public synchronized void drop() {
        this.isHold = false;
        System.out.println(key + " drop lock");
        notifyAll();
    }
}
