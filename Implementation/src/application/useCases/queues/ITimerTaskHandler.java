package application.useCases.queues;

import domain.entities.tasks.interfaces.ITimerTask;

public interface ITimerTaskHandler extends IQueueHandler<ITimerTask> {
    void runTasks();
    boolean cancelTask(String taskId);
}
