package tests.microtaskTests;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import presentation.EventLoop;

public class ResolvedMicrotaskTest {

    public static void main(String[] args) {
        try (EventLoop eventLoop = new EventLoop()) {
            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 1")));

            eventLoop.resolve("Hello word")
                    .then(value -> value + "!")
                    .then(value -> {
                        System.out.println(value);
                        return value.length();
                    })
                    .finallyDo(() -> System.out.println("Finished promise"));

            eventLoop.execute(new ImmediateTask(() -> System.out.println("task 2")));

            Thread.sleep(500);

            eventLoop.start();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        /*
        * Expected:
        *
        * task 1
        * task 2
        * Hello word!
        * Finished promise
        * */
    }
}
