package infrastructure.useCases;

import application.useCases.IEventLoopHandler;
import application.useCases.queues.IMicrotaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import application.useCases.queues.ICallstackHandler;
import domain.entities.tasks.interfaces.ITask;
import infrastructure.utils.Sleeper;

public class EventLoopHandler implements IEventLoopHandler {
    private static final long SLEEP_LOOP_MILLISECONDS = 1;
    private final ICallstackHandler callstackHandler;
    private volatile boolean isRunning;
    private final IMicrotaskHandler microtaskHandler;

    public EventLoopHandler(ICallstackHandler callstackHandler, IMicrotaskHandler microtaskHandler) {
        this.callstackHandler = callstackHandler;
        this.isRunning = true;
    }

    @Override
    public void run() throws InterruptedException {
        while (isRunning) {
            if (callstackHandler.hasTasks()) {
                callstackHandler.runTasks();
            } else {
                Sleeper.tryToSleepOrDie(SLEEP_LOOP_MILLISECONDS);
            }

            if (Thread.interrupted()) {
                throw new InterruptedException("Event loop was interrupted");
            }
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
        if (!isRunning) {
            throw new IllegalStateException("Event loop is shutdown");
        }
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        callstackHandler.addTask(task.getExecutor());
    }

    @Override
    public void shutDown() {
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    public <T> Promise<T> executePromise(IPromiseTask<T> task) {
        return microtaskHandler.createPromise(task.getExecutor());
    }

    @Override
    public <T> Promise<T> resolvePromise(T value) {
        return microtaskHandler.resolvePromise(value);
    }

    @Override
    public <T> Promise<T> rejectPromise(Throwable error) {
        return microtaskHandler.rejectPromise(error);
    }
}
