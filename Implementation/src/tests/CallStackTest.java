package tests;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class CallStackTest {
    public static void main(String[] args) {
        try (EventLoop eventLoop = new EventLoop()) {
            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 1")));

            Runnable func = () -> System.out.println("task 3");
            Runnable func2 = () -> {
                eventLoop.execute(new ImmediateTask(func));
                eventLoop.execute(new ImmediateTask(() -> System.out.println("task 2")));
            };

            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 4")));
            func2.run();

            eventLoop.start();
        }

        /* Expected
         * Task 1
         * Task 4
         * Task 3
         * Task 2
         */
    }
}
