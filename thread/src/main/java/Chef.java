import java.util.concurrent.TimeUnit;

public class Chef implements  Runnable {
    public Restaurant restaurant;
    public int orderNum = 0;
    public Chef(Restaurant restaurant)
    {
        this.restaurant = restaurant;
    }
    @Override
    public void run() {
        while(!Thread.interrupted()) {
            synchronized (this) {
                if (restaurant.meal != null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    restaurant.waitPerson.notify();
                }
            }


            synchronized (restaurant.waitPerson) {
                restaurant.meal = new Meal(++orderNum);
                restaurant.waitPerson.notify();
            }

            try {
                TimeUnit.MICROSECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("meal:" + orderNum  + "  has done");
        }
    }
}