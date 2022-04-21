public class WaitPerson implements  Runnable {

    public Restaurant restaurant;
    public WaitPerson(Restaurant restaurant)
    {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            synchronized (this) {
                if (restaurant.meal == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("Waitperson get " + restaurant.meal);
            synchronized (restaurant.chef) {
                restaurant.meal = null;
                restaurant.chef.notifyAll();
            }
        }
    }
}
