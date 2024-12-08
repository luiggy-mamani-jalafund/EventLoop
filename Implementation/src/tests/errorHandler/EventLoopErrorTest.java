package tests.errorHandler;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class EventLoopErrorTest {
    public static void main(String[] args) {
        try (EventLoop eventLoop = new EventLoop()) {
            eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 1 executed")));

            eventLoop.execute(new ImmediateTask(() -> {
                throw new NullPointerException("Simulated NPE in Event Loop");
            }));

            eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 2 executed")));

            eventLoop.start();
        }

        /* Expected:
         * Task 1 executed
         * Dec 07, 2024 5:39:45 PM domain.exceptions.DefaultErrorHandler handleError
         * SEVERE: Error executing task: tests.errorHandler.EventLoopErrorTest$$Lambda$21/0x00000008010048f8@33c7353a
         * java.lang.NullPointerException: Simulated NPE in Event Loop
         * [Rest of error information]
         *
         * Task tests.errorHandler.EventLoopErrorTest$$Lambda$21/0x00000008010048f8@33c7353a failed with error: Simulated NPE in Event Loop
         * Task 2 executed
         */
    }
}
