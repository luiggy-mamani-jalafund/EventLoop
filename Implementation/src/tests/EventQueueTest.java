package tests;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class EventQueueTest {
    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();

        eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 1")));
        eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 2")));
        eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 3")));

        eventLoop.execute(new ImmediateTask(() -> {
            System.out.println("Task 4 (complex task)");
            eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 5 (nested)")));
        }));

        eventLoop.run();

        /* Expected Output:
         * Task 1
         * Task 2
         * Task 3
         * Task 4 (complex task)
         * Task 5 (nested)
         */
    }
}
