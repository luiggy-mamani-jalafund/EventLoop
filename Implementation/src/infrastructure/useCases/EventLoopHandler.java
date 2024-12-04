package infrastructure.useCases;

import application.useCases.queues.ICallstackHandler;
import application.useCases.IEventLoopHandler;
import application.useCases.queues.IMicrotaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import domain.entities.tasks.interfaces.ITask;
import infrastructure.utils.Sleeper;

public class EventLoopHandler implements IEventLoopHandler {

    private final long SLEEP_LOOP_MILLISECONDS = 200;
    private final ICallstackHandler callstackHandler;
    private final IMicrotaskHandler microtaskHandler;

    public EventLoopHandler(ICallstackHandler callstackHandler, IMicrotaskHandler microtaskHandler) {
        this.callstackHandler = callstackHandler;
        this.microtaskHandler = microtaskHandler;
    }

    @Override
    public void run() {
        while (true) {
            callstackHandler.runTasks();

            while (microtaskHandler.hasTasks()) {
                Runnable microtask = microtaskHandler.getMicrotask();
                microtask.run();
            }

            Sleeper.tryToSleepOrDie(SLEEP_LOOP_MILLISECONDS);
        }
    }

    @Override
    public void executeTask(ITask<Runnable> task) {
        callstackHandler.addTask(task.getExecutor());
    }

    @Override
    public <T> Promise<T> executePromise(IPromiseTask<T> task) {
        return microtaskHandler.createPromise(task.getExecutor());
    }
}
