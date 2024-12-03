package application.useCases;

import domain.entities.tasks.interfaces.ITask;

public interface IEventLoopHandler {
    void run() throws InterruptedException;
    void executeTask(ITask<Runnable> task);
}
