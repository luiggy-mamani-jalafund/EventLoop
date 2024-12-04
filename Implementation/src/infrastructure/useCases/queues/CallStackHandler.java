package infrastructure.useCases.queues;

import application.useCases.queues.ICallstackHandler;
import domain.entities.tasks.concrete.immediates.ImmediateTask;

public class CallStackHandler implements ICallstackHandler {
    private final TaskQueue taskQueue;

    public CallStackHandler() {
        this.taskQueue = new TaskQueue();
    }

    @Override
    public void runTasks() {
        while (hasTasks()) {
            taskQueue.dequeueTask()
                    .ifPresent(task -> task.getExecutor().run());
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
