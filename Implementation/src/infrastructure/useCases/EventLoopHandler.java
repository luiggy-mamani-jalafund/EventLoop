package infrastructure.useCases;

import application.useCases.IEventLoopHandler;
import application.useCases.exceptions.IErrorHandler;
import application.useCases.queues.ICallstackHandler;
import application.useCases.queues.IMicrotaskHandler;
import application.useCases.queues.ITimerTaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import domain.entities.tasks.interfaces.ITask;
import domain.entities.tasks.interfaces.ITimerTask;
import infrastructure.utils.Sleeper;

public class EventLoopHandler implements IEventLoopHandler {
    private static final long SLEEP_LOOP_MILLISECONDS = 1;
    private final ICallstackHandler callstackHandler;
    private volatile boolean isRunning;
    private final IMicrotaskHandler microtaskHandler;
    private final ITimerTaskHandler timerTaskHandler;
    private final IErrorHandler errorHandler;

    public EventLoopHandler(ICallstackHandler callstackHandler, IMicrotaskHandler microtaskHandler,
                            ITimerTaskHandler timerTaskHandler, IErrorHandler errorHandler) {
        this.callstackHandler = callstackHandler;
        this.microtaskHandler = microtaskHandler;
        this.isRunning = true;
        this.timerTaskHandler = timerTaskHandler;
        this.errorHandler = errorHandler;
    }

    @Override
    public void run() throws InterruptedException {
        while (isRunning) {
            try {
                if (callstackHandler.hasTasks()) {
                    callstackHandler.runTasks();
                }

                while (microtaskHandler.hasTasks()) {
                    Runnable microtask = microtaskHandler.getMicrotask();
                    if (microtask != null) {
                        microtask.run();
                    }
                }

                if (timerTaskHandler.hasTasks()) {
                    timerTaskHandler.runTasks();
                }

                if (!callstackHandler.hasTasks() && !microtaskHandler.hasTasks() && !timerTaskHandler.hasTasks()) {
                    Sleeper.tryToSleepOrDie(SLEEP_LOOP_MILLISECONDS);
                }

            } catch (Throwable e) {
                errorHandler.handleError(null, e);
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

    public void setTimeout(ITimerTask timerTask) {
        if (!isRunning) {
            throw new IllegalStateException("Event loop is shutdown");
        }
        if (timerTask == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        timerTaskHandler.addTask(timerTask);
    }

    public boolean clearTimeout(String timerTaskId) {
        if (!isRunning) {
            throw new IllegalStateException("Event loop is shutdown");
        }
        if (timerTaskId == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        return timerTaskHandler.cancelTask(timerTaskId);
    }
}