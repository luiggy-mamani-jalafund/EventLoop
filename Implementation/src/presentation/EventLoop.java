package presentation;

import application.useCases.queues.ICallstackHandler;
import application.useCases.queues.IMicrotaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import domain.entities.tasks.interfaces.ITask;
import infrastructure.useCases.EventLoopHandler;
import infrastructure.useCases.queues.CallStackHandler;
import infrastructure.useCases.queues.MicrotaskHandler;

public class EventLoop {

    private final EventLoopHandler eventLoopHandler;

    public EventLoop() {
        ICallstackHandler callstackHandler = new CallStackHandler();
        IMicrotaskHandler microtaskHandler = new MicrotaskHandler();

        eventLoopHandler = new EventLoopHandler(callstackHandler, microtaskHandler);
    }

    public void execute(ITask<Runnable> task) {
        eventLoopHandler.executeTask(task);
    }

    public <T> Promise<T> execute(IPromiseTask<T> task) {
        return eventLoopHandler.executePromise(task);
    }

    public <T> Promise<T> resolve(T value) {
        return eventLoopHandler.resolvePromise(value);
    }

    public <T> Promise<T> reject(Throwable error) {
        return eventLoopHandler.rejectPromise(error);
    }

    public void run() {
        eventLoopHandler.run();
    }

}
