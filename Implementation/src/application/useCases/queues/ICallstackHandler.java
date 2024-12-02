package application.useCases.queues;

public interface ICallstackHandler extends IQueueHandler<Runnable> {
    void runTasks();
}
