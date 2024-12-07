package tests.errorHandler;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class CallStackErrorTest {
    public static void main(String[] args) {
        try (EventLoop eventLoop = new EventLoop()) {
            eventLoop.execute(new ImmediateTask(() -> System.out.println("Task 1")));

            Runnable errorTask = () -> {
                throw new RuntimeException("Error in task 2");
            };

            Runnable safeTask = () -> System.out.println("Task 3");

            eventLoop.execute(new ImmediateTask(errorTask));
            eventLoop.execute(new ImmediateTask(safeTask));

            eventLoop.start();

            /* Expected:
             * Task 1
             * Dec 07, 2024 5:35:29 PM domain.exceptions.DefaultErrorHandler handleError
             * SEVERE: Error executing task: tests.errorHandler.CallStackErrorTest$$Lambda$21/0x00000008010048f8@33c7353a
             * java.lang.RuntimeException: Error in task 2
             * [Rest of error information]

             * Task tests.errorHandler.CallStackErrorTest$$Lambda$21/0x00000008010048f8@33c7353a failed with error: Error in task 2
             * Task 3
             */
        }
    }
}
