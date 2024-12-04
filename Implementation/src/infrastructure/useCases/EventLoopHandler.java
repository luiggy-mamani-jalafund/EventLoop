package infrastructure.useCases;

import application.useCases.IEventLoopHandler;
import application.useCases.queues.ICallstackHandler;
import application.useCases.queues.IMicrotaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import domain.entities.tasks.interfaces.ITask;
import infrastructure.utils.Sleeper;

public class EventLoopHandler implements IEventLoopHandler {
    private static final long SLEEP_LOOP_MILLISECONDS = 1;
    private final ICallstackHandler callstackHandler;
    private volatile boolean isRunning;
    private final IMicrotaskHandler microtaskHandler;

    public EventLoopHandler(ICallstackHandler callstackHandler, IMicrotaskHandler microtaskHandler) {
        this.callstackHandler = callstackHandler;
        this.microtaskHandler = microtaskHandler;
        this.isRunning = true;
    }

    @Override
    public void run() throws InterruptedException {
        while (isRunning) {
            if (callstackHandler.hasTasks()) {
                callstackHandler.runTasks();
            }

            while (microtaskHandler.hasTasks()) {
                Runnable microtask = microtaskHandler.getMicrotask();
                microtask.run();
            }

            if (!callstackHandler.hasTasks() && !microtaskHandler.hasTasks()) {
                Sleeper.tryToSleepOrDie(SLEEP_LOOP_MILLISECONDS);
            }

            if (Thread.interrupted()) {
                throw new InterruptedException("Event loop was interrupted");
            }
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
    }

    @Override
    public <T> Promise<T> executePromise(IPromiseTask<T> task) {
        if (!isRunning) {
            throw new IllegalStateException("Event loop is shutdown");
        }
        if (task == null) {
            throw new IllegalArgumentException("Promise task cannot be null");
        }
        return microtaskHandler.createPromise(task.getExecutor());
    }

    @Override
    public <T> Promise<T> resolvePromise(T value) {
        if (!isRunning) {
            throw new IllegalStateException("Event loop is shutdown");
        }
        return microtaskHandler.resolvePromise(value);
    }

    @Override
    public <T> Promise<T> rejectPromise(Throwable error) {
        if (!isRunning) {
            throw new IllegalStateException("Event loop is shutdown");
        }
        return microtaskHandler.rejectPromise(error);
    }
}
