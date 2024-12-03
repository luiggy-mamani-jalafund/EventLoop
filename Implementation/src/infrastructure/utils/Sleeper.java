package infrastructure.utils;

public class Sleeper {

    public static void tryToSleepOrDie(long timeMilliseconds) {
        try {
            Thread.sleep(timeMilliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
