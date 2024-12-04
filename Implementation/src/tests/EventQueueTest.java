package tests;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class EventQueueTest {
    public static void main(String[] args) {
        try (EventLoop eventLoop = new EventLoop()) {
            eventLoop.start();

            eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 1")));
            eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 2")));
            eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 3")));

            eventLoop.execute(new ImmediateTask(() -> {
                eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 6 (nested)")));
                System.out.println("Task 4 (complex task)");
                eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 5 (nested)")));
            }));

            Thread.sleep(100);

            eventLoop.shutdown();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /* Expected Output:
            Task 1
            Task 2
            Task 3
            Task 4 (complex task)
            Task 6 (nested)
            Task 5 (nested)
         */
    }
}