package application.useCases;

import domain.entities.tasks.interfaces.ITask;

public interface IEventLoopHandler {
    /**
     * Starts the event loop
     * @throws InterruptedException if the thread is interrupted
     */
    void run() throws InterruptedException;

    /**
     * Adds a task to be executed by the event loop
     * @param task the task to execute
     */
    void executeTask(ITask<Runnable> task);

    /**
     * Gracefully shuts down the event loop
     */
    void shutDown();

    /**
     * Checks if the event loop is currently running
     * @return true if the event loop is running
     */
    boolean isRunning();
}
