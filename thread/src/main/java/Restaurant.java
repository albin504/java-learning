import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Restaurant {
    Meal meal;
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);
    public ExecutorService executor = Executors.newCachedThreadPool();
    public Restaurant()
    {
        executor.execute(waitPerson);
        executor.execute(chef);
    }

    public static void main(String[] args) {
        new Restaurant();
    }
}
