package application.useCases.queues;

public interface IQueueHandler<T> {
    void addTask(T task);
    boolean hasTasks();
}
