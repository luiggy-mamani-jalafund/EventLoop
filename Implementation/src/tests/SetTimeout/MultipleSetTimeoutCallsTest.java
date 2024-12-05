package tests.SetTimeout;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.timers.TimerTask;
import presentation.EventLoop;

public class MultipleSetTimeoutCallsTest {

    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();

        eventLoop.execute(new ImmediateTask(() -> System.out.println("Start of the program")));

        eventLoop.execute(new ImmediateTask(() -> {
            System.out.println("First function");

            eventLoop.setTimeout(new TimerTask(() -> {
                System.out.println("Timeout 1");

                eventLoop.execute(new ImmediateTask(() -> {
                    System.out.println("Second function");

                    eventLoop.setTimeout(new TimerTask(() -> {
                        System.out.println("Timeout 2");
                    }, 0));

                    eventLoop.execute(new ImmediateTask(() -> {
                        System.out.println("Third function");
                    }));
                }));
            }, 0));
        }));

        eventLoop.execute(new ImmediateTask(() -> System.out.println("End of the program")));

        eventLoop.start();
        
        // Console output:
        // Start of the program
        // First function
        // End of the program
        // Timeout 1
        // Second function
        // Third function
        // Timeout 2
    }
}
