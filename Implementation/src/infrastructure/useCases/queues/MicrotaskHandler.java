package infrastructure.useCases.queues;

import application.useCases.queues.IMicrotaskHandler;
import domain.entities.tasks.concrete.immediates.ImmediateTask;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.ITask;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class MicrotaskHandler implements IMicrotaskHandler {

    private final TaskQueue taskQueue;
    private final ExecutorService executor;

    public MicrotaskHandler() {
        this.taskQueue = new TaskQueue();
        this.executor = Executors.newCachedThreadPool();
    }

    @Override
    public <T> Promise<T> createPromise(Supplier<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();

        executor.submit(() -> {
            try {
                T result = task.get();
                addTask(() -> future.complete(result));
            } catch (Exception exception) {
                addTask(() -> future.completeExceptionally(exception));
            }
        });

        return new Promise<>(future);
    }

    @Override
    public <T> Promise<T> resolvePromise(T value) {
        CompletableFuture<T> future = new CompletableFuture<>();
        addTask(() -> future.complete(value));
        return new Promise<>(future);
    }

    @Override
    public <T> Promise<T> rejectPromise(Throwable error) {
        CompletableFuture<T> future = new CompletableFuture<>();
        addTask(() -> future.completeExceptionally(error));
        return new Promise<>(future);
    }

    @Override
    public Runnable getMicrotask() {
        return taskQueue.dequeueTask()
                .map(ITask::getExecutor)
                .orElse(null);
    }

    @Override
    public void addTask(Runnable task) {
        if (task != null) {
            taskQueue.addTask(new ImmediateTask(task));
        }
    }

    @Override
    public boolean hasTasks() {
        return taskQueue.hasTasks();
    }
}
