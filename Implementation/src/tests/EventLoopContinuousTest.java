package tests;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class EventLoopContinuousTest {
    public static void main(String[] args) {
        try (EventLoop eventLoop = new EventLoop()) {
            eventLoop.start();
            System.out.println("Event Loop started, waiting for tasks...");

            eventLoop.execute(new ImmediateTask(() ->
                    System.out.println("Task 1: " + System.currentTimeMillis())
            ));
            eventLoop.execute(new ImmediateTask(() ->
                    System.out.println("Task 2: " + System.currentTimeMillis())
            ));

            Thread.sleep(2000);
            System.out.println("Adding more tasks after 2 seconds...");

            eventLoop.execute(new ImmediateTask(() -> {
                System.out.println("Task 3 (complex) started: " + System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                eventLoop.execute(new ImmediateTask(() ->
                        System.out.println("Task 3.1 (nested): " + System.currentTimeMillis())
                ));
            }));

            Thread.sleep(3000);
            System.out.println("Adding final task after 3 more seconds...");

            // Tarea final
            eventLoop.execute(new ImmediateTask(() ->
                    System.out.println("Final Task: " + System.currentTimeMillis())
            ));

            Thread.sleep(1000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Event Loop shut down");
    }
}