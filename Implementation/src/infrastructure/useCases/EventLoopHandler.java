package infrastructure.useCases;

import application.useCases.IEventLoopHandler;
import application.useCases.queues.ICallstackHandler;
import domain.entities.tasks.interfaces.ITask;
import infrastructure.utils.Sleeper;

public class EventLoopHandler implements IEventLoopHandler {
    private static final long SLEEP_LOOP_MILLISECONDS = 1;
    private final ICallstackHandler callstackHandler;
    private volatile boolean isRunning;

    public EventLoopHandler(ICallstackHandler callstackHandler) {
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
}
