package tests.microtaskTests;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class RejectedMicrotaskTest {
    public static void main(String[] args) {
        try (EventLoop eventLoop = new EventLoop()) {
            eventLoop.start();

            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 1")));

            eventLoop.reject(new Exception("promise rejected exception"))
                    .catchError(e -> System.out.println(e.getMessage()))
                    .finallyDo(() -> System.out.println("Finished promise"));

            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 2")));

            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /*
         * Expected:
         *
         * task 1
         * promise rejected exception
         * Finished promise
         * task 2
         * */
    }
}
