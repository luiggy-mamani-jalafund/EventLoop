package tests.SetTimeout;

import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.timers.TimerTask;
import presentation.EventLoop;

public class ComplexFunctionTest {

    public static void main(String[] args) {
        EventLoop eventLoop = new EventLoop();

        eventLoop.execute(new ImmediateTask(() -> System.out.println("Start of program")));

        eventLoop.execute(new ImmediateTask(() -> executeComplexFunction(eventLoop, "parameter1")));

        eventLoop.execute(new ImmediateTask(() -> executeComplexFunction(eventLoop, "parameter2")));

        eventLoop.execute(new ImmediateTask(() -> System.out.println("End of program")));

        eventLoop.run();
    }

    private static void executeComplexFunction(EventLoop eventLoop, String param) {
        System.out.println("Running complexFunction with " + param);

        for (int i = 0; i < 3; i++) {
            System.out.println("Synchronous iteration " + i + " with " + param);
        }

        eventLoop.setTimeout(new TimerTask(() -> {
            eventLoop.execute(new ImmediateTask(() -> { System.out.println("Timeout 1 with " + param); }));

            eventLoop.execute(new ImmediateTask(() -> {
                System.out.println("Nested function with " + param);

                eventLoop.setTimeout(new TimerTask(() -> {
                    System.out.println("Timeout inside nested function with " + param);
                }, 100));
            }));
        }, 0));
    }
}
