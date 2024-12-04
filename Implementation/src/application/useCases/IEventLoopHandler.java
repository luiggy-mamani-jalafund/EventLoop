package application.useCases;

import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import domain.entities.tasks.interfaces.ITask;

public interface IEventLoopHandler {
    void run() throws InterruptedException;
    void executeTask(ITask<Runnable> task);

    <T> Promise<T> executePromise(IPromiseTask<T> task);
    <T> Promise<T> resolvePromise(T value);
    <T> Promise<T> rejectPromise(Throwable error);
}
