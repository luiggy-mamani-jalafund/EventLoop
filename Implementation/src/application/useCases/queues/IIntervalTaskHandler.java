package application.useCases.queues;

import domain.entities.tasks.interfaces.IIntervalTask;

import java.util.UUID;

public interface IIntervalTaskHandler extends IQueueHandler<IIntervalTask> {
    void runTasks();

    boolean cancelInterval(UUID intervalId);
}
