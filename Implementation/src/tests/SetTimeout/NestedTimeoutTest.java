package tests.SetTimeout;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.timers.TimerTask;
import presentation.EventLoop;

public class NestedTimeoutTest {

    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();

        eventLoop.execute(new ImmediateTask(() -> System.out.println("Start")));

        eventLoop.setTimeout(new TimerTask(() -> {
            System.out.println("First message");

            eventLoop.setTimeout(new TimerTask(() -> {
                System.out.println("Second message");

                eventLoop.setTimeout(new TimerTask(() -> {
                    System.out.println("Third message");
                }, 1000));
            }, 2000));
        }, 3000));

        eventLoop.start();

        // Console output:
        // Start
        // First message
        // Second message
        // Third message
    }
}
