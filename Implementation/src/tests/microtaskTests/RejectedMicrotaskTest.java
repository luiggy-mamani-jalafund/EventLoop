package tests.microtaskTests;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class RejectedMicrotaskTest {

    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();

        eventLoop.execute(new ImmediateTask(() -> System.out.println("task 1")));

        eventLoop.reject(new Exception("promise rejected exception"))
                .catchError(e -> System.out.println(e.getMessage()))
                .finallyDo(() -> System.out.println("Finished promise"));

        eventLoop.execute(new ImmediateTask(() -> System.out.println("task 2")));

        eventLoop.run();

        /*
         * Expected:
         *
         * task 1
         * task 2
         * promise rejected exception
         * Finished promise
         * */
    }
}
