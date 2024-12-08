package tests.errorHandler;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.promises.PromiseTask;
import presentation.EventLoop;

public class MicrotaskErrorTest {
    public static void main(String[] args) {
        try (EventLoop eventLoop = new EventLoop()) {
            eventLoop.execute(new PromiseTask<>(() -> {
                        throw new IllegalStateException("Error fetching data");
                    }))
                    .thenAccept(res -> System.out.println("Result: " + res))
                    .catchError(error -> System.out.println("Handled error: " + error.getMessage()));

            eventLoop.execute(new ImmediateTask(() -> System.out.println("Safe task executed")));

            eventLoop.start();

            /* Expected:
             * Safe task executed
             * Handled error: java.lang.IllegalStateException: Error fetching data
             * */
        }
    }
}
