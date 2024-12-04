package tests.microtaskTests;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.promises.PromiseTask;
import infrastructure.utils.Sleeper;
import presentation.EventLoop;

public class PromiseMicrotaskTest {
    public static void main(String[] args) {
        try (EventLoop eventLoop = new EventLoop()) {
            eventLoop.start();

            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 1")));
            eventLoop.execute(new PromiseTask<>(() -> {
                        Sleeper.tryToSleepOrDie(100);
                        return "data fetched";
                    }))
                    .then(res -> "processed data -> " + res)
                    .then(res -> "final processed -> " + res)
                    .thenAccept(System.out::println);

            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 2")));
            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 3")));
            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 4")));
            eventLoop.execute(new ImmediateTask(() -> {
                Sleeper.tryToSleepOrDie(600);
                System.out.println("task 5");
            }));

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /*
        * Expected:
        *
        * task 1
        * task 2
        * task 3
        * task 4
        * task 5
        *
        * final processed -> processed data -> data fetched
         * */
    }
}
