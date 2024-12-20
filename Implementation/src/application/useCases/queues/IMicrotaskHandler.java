package application.useCases.queues;

import domain.entities.tasks.concrete.promises.Promise;

import java.util.function.Supplier;

public interface IMicrotaskHandler extends IQueueHandler<Runnable> {
    <T> Promise<T> createPromise(Supplier<T> task);
    <T> Promise<T> resolvePromise(T value);
    <T> Promise<T> rejectPromise(Throwable error);
    Runnable getMicrotask();
}
