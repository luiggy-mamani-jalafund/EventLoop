package presentation;

import application.useCases.queues.ICallstackHandler;
import application.useCases.queues.IMicrotaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import domain.entities.tasks.interfaces.ITask;
import infrastructure.useCases.EventLoopHandler;
import infrastructure.useCases.queues.CallStackHandler;
import infrastructure.useCases.queues.MicrotaskHandler;

public class EventLoop implements AutoCloseable {
    private final EventLoopHandler eventLoopHandler;
    private final Thread eventLoopThread;

    public EventLoop() {
        ICallstackHandler callstackHandler = new CallStackHandler();
        this.eventLoopHandler = new EventLoopHandler(callstackHandler);
        this.eventLoopThread = new Thread(this::runEventLoop, "EventLoop-Thread");
    }

    public void start() {
        if (!isRunning()) {
            throw new IllegalStateException("Cannot start a shutdown event loop");
        }
        eventLoopThread.start();
        IMicrotaskHandler microtaskHandler = new MicrotaskHandler();

        eventLoopHandler = new EventLoopHandler(callstackHandler, microtaskHandler);
    }

    public void execute(ITask<Runnable> task) {
        eventLoopHandler.executeTask(task);
    }

    private void runEventLoop() {
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
        try {
            eventLoopHandler.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        eventLoopHandler.shutDown();
        eventLoopThread.interrupt();
    }

    public boolean isRunning() {
        return eventLoopHandler.isRunning();
    }

    @Override
    public void close() {
        shutdown();
    }
}
