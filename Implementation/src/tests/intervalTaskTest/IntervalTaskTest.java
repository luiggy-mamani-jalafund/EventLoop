package tests.intervalTaskTest;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.intervals.IntervalTask;
import domain.entities.tasks.concrete.timers.TimerTask;
import presentation.EventLoop;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class IntervalTaskTest {

    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();
        System.out.println("Test start");

        UUID intervalId = eventLoop.setInterval(new IntervalTask(
                () -> System.out.println("Interval task executing..."),
                1000
        ));

        eventLoop.execute(new ImmediateTask(() -> System.out.println("Immediate task executed")));

        eventLoop.setTimeout(new TimerTask(() -> {
            System.out.println("Cancelling interval...");
            boolean cancelled = eventLoop.clearInterval(intervalId);
            System.out.println("Interval cancelled: " + cancelled);
        }, 5000));

        AtomicInteger id = new AtomicInteger(1);
        UUID intervalId2 = eventLoop.setInterval(new IntervalTask(
                () -> {
                    System.out.println("Interval task with id... " + id);
                    id.getAndIncrement();
                }
                ,
                5000
        ));

        eventLoop.start();

        /*
         * Expected Output:
         *
         * Test start
         * Immediate task executed
         * Interval task executing...
         * Interval task executing...
         * Interval task executing...
         * Interval task executing...
         * Interval task executing...
         * Cancelling interval...
         * Interval cancelled: true
         * Interval task executing... 1
         * Interval task executing... 2
         * Interval task executing... 3
         * Interval task executing... 4
         */
    }
}

