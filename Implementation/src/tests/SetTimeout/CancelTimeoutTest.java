package tests.SetTimeout;

import domain.entities.tasks.concrete.timers.TimerTask;
import presentation.EventLoop;

public class CancelTimeoutTest {
    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();

        TimerTask timerTask = new TimerTask(() -> System.out.println("This message will not be displayed"), 5000);

        eventLoop.setTimeout(timerTask);

        System.out.println("Scheduled timeout");

        boolean isCancelled = eventLoop.cancelTimeout(timerTask.getId());
        if (isCancelled) {
            System.out.println("Canceled timeout");
        }

        eventLoop.run();

        // Console output:
        // Scheduled timeout
        // Canceled timeout
    }
}
