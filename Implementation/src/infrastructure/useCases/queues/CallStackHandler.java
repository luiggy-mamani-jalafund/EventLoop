package infrastructure.useCases.queues;

import application.useCases.exceptions.IErrorHandler;
import application.useCases.queues.ICallstackHandler;
import domain.entities.tasks.concrete.immediates.ImmediateTask;

public class CallStackHandler implements ICallstackHandler {
    private final TaskQueue taskQueue;
    private final IErrorHandler errorHandler;

    public CallStackHandler(IErrorHandler errorHandler) {
        this.taskQueue = new TaskQueue();
        this.errorHandler = errorHandler;
    }

    @Override
    public void runTasks() {
        while (hasTasks()) {
            taskQueue.dequeueTask().ifPresent(task -> {
                try {
                    task.getExecutor().run();
                } catch (Throwable e) {
                    errorHandler.handleError(task.getExecutor(), e);
                }
            });
        }
    }

    @Override
    public void addTask(Runnable task) {
        taskQueue.addTask(new ImmediateTask(task));
    }

    @Override
    public boolean hasTasks() {
        return taskQueue.hasTasks();
    }
}
