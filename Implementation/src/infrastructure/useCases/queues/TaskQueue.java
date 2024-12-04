package infrastructure.useCases.queues;

import application.useCases.queues.IQueueHandler;
import domain.entities.tasks.interfaces.ITask;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskQueue implements IQueueHandler<ITask<Runnable>> {
    private final ConcurrentLinkedQueue<ITask<Runnable>> taskQueue;

    public TaskQueue() {
        this.taskQueue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void addTask(ITask<Runnable> task) {
        if (task != null) {
            taskQueue.offer(task);
        }
    }

    @Override
    public boolean hasTasks() {
        return !taskQueue.isEmpty();
    }

    public Optional<ITask<Runnable>> dequeueTask() {
        return Optional.ofNullable(taskQueue.poll());
    }

    public int size() {
        return taskQueue.size();
    }
}
