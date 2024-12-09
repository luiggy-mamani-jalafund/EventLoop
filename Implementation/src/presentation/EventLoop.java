package presentation;

import application.useCases.exceptions.IErrorHandler;
import application.useCases.queues.ICallstackHandler;
import application.useCases.queues.IIntervalTaskHandler;
import application.useCases.queues.IMicrotaskHandler;
import application.useCases.queues.ITimerTaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IIntervalTask;
import domain.entities.tasks.interfaces.IPromiseTask;
import domain.entities.tasks.interfaces.ITask;
import domain.entities.tasks.interfaces.ITimerTask;
import domain.exceptions.DefaultErrorHandler;
import infrastructure.useCases.EventLoopHandler;
import infrastructure.useCases.queues.CallStackHandler;
import infrastructure.useCases.queues.IntervalTaskHandler;
import infrastructure.useCases.queues.MicrotaskHandler;
import infrastructure.useCases.queues.TimerTaskHandler;

import java.util.UUID;

public class EventLoop implements AutoCloseable {
    private final EventLoopHandler eventLoopHandler;

    public EventLoop() {
        IErrorHandler errorHandler = new DefaultErrorHandler();
        ICallstackHandler callstackHandler = new CallStackHandler(errorHandler);
        IMicrotaskHandler microtaskHandler = new MicrotaskHandler(errorHandler);
        ITimerTaskHandler timerTaskHandler = new TimerTaskHandler(errorHandler);
        IIntervalTaskHandler intervalTaskHandler = new IntervalTaskHandler(errorHandler);
        this.eventLoopHandler = new EventLoopHandler(callstackHandler, microtaskHandler, timerTaskHandler, errorHandler, intervalTaskHandler);
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

    public UUID setInterval(IIntervalTask intervalTask) {
        return eventLoopHandler.setInterval(intervalTask);
    }

    public boolean clearInterval(UUID intervalId) {
        return eventLoopHandler.clearInterval(intervalId);
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
