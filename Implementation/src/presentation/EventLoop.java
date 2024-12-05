package presentation;

import application.useCases.queues.ICallstackHandler;
import application.useCases.queues.IMicrotaskHandler;
import application.useCases.queues.ITimerTaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import domain.entities.tasks.interfaces.ITask;
import domain.entities.tasks.interfaces.ITimerTask;
import infrastructure.useCases.EventLoopHandler;
import infrastructure.useCases.queues.CallStackHandler;
import infrastructure.useCases.queues.MicrotaskHandler;
import infrastructure.useCases.queues.TimerTaskHandler;

public class EventLoop implements AutoCloseable {
    private final EventLoopHandler eventLoopHandler;

    public EventLoop() {
        ICallstackHandler callstackHandler = new CallStackHandler();
        IMicrotaskHandler microtaskHandler = new MicrotaskHandler();
        ITimerTaskHandler timerTaskHandler = new TimerTaskHandler();
        this.eventLoopHandler = new EventLoopHandler(callstackHandler, microtaskHandler, timerTaskHandler);
    }

    public void start() {
        runEventLoop();
    }

    public void execute(ITask<Runnable> task) {
        eventLoopHandler.executeTask(task);
    }

    public <T> Promise<T> execute(IPromiseTask<T> task) {
        return eventLoopHandler.executePromise(task);
    }

    public void setTimeout(ITimerTask timerTask) {
        eventLoopHandler.setTimeout(timerTask);
    }

    public boolean cancelTimeout(String timerTaskId) {
        return eventLoopHandler.clearTimeout(timerTaskId);
    }

    public <T> Promise<T> resolve(T value) {
        return eventLoopHandler.resolvePromise(value);
    }

    public <T> Promise<T> reject(Throwable error) {
        return eventLoopHandler.rejectPromise(error);
    }

    private void runEventLoop() {
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
    }

    public boolean isRunning() {
        return eventLoopHandler.isRunning();
    }

    @Override
    public void close() {
        shutdown();
    }
}
