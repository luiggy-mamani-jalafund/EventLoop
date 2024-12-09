package tests.errorHandler;

import domain.entities.tasks.concrete.timers.TimerTask;
import presentation.EventLoop;

public class TimerTaskErrorTest {
    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();

        eventLoop.setTimeout(new TimerTask(() -> {
            throw new RuntimeException("Timer task error");
        }, 1000));

        eventLoop.setTimeout(new TimerTask(() -> System.out.println("Safe Timer Task"), 2000));

        eventLoop.start();

        /* Expected:
         * Dec 07, 2024 5:38:26 PM domain.exceptions.DefaultErrorHandler handleError
         * SEVERE: Error executing task: tests.errorHandler.TimerTaskErrorTest$$Lambda$20/0x0000000801004980@7a07c5b4
         * java.lang.RuntimeException: Timer task error
         * [Rest of error information]
         * Safe Timer Task
         */
    }
}
