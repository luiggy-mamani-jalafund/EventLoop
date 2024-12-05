package tests.SetTimeout;

import domain.entities.tasks.concrete.timers.TimerTask;
import presentation.EventLoop;

public class TimerTaskSimpleTest {
    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();

        eventLoop.setTimeout(new TimerTask(() -> System.out.println("Task 1 executed after 1000ms"), 1000));
        eventLoop.setTimeout(new TimerTask(() -> System.out.println("Task 2 executed after 2000ms"), 2000));

        eventLoop.start();

        // Console output:
        // Task 1 executed after 1000ms
        // Task 2 executed after 2000ms
    }
}
