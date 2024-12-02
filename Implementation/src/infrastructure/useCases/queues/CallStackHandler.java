package infrastructure.useCases.queues;

import application.useCases.queues.ICallstackHandler;

import java.util.ArrayDeque;
import java.util.Optional;

public class CallStackHandler implements ICallstackHandler {

    private final ArrayDeque<Runnable> tasks;

    public CallStackHandler() {
        this.tasks = new ArrayDeque<>();
    }

    @Override
    public void runTasks() {
        while (hasTasks()) {
            Optional<Runnable> task = Optional.ofNullable(tasks.poll());
            task.ifPresent(Runnable::run);
        }
    }

    @Override
    public void addTask(Runnable task) {
        tasks.add(task);
    }

    @Override
    public boolean hasTasks() {
        return !tasks.isEmpty();
    }
}
