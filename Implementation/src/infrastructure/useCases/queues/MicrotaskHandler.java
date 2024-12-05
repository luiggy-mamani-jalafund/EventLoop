package infrastructure.useCases.queues;

import application.useCases.queues.IMicrotaskHandler;
import domain.entities.tasks.concrete.promises.Promise;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class MicrotaskHandler implements IMicrotaskHandler {

    private final Queue<Runnable> microtaskQueue;
    private final ExecutorService executor;

    public MicrotaskHandler() {
        this.microtaskQueue = new ArrayDeque<>();
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
        return microtaskQueue.poll();
    }

    @Override
    public void addTask(Runnable task) {
        microtaskQueue.offer(task);
    }

    @Override
    public boolean hasTasks() {
        return !microtaskQueue.isEmpty();
    }
}
