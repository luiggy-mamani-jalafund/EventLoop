package presentation;

import application.useCases.queues.ICallstackHandler;
import application.useCases.queues.IMicrotaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import domain.entities.tasks.interfaces.ITask;
import domain.entities.tasks.interfaces.ITimerTask;
import infrastructure.useCases.EventLoopHandler;
import infrastructure.useCases.queues.CallStackHandler;
import infrastructure.useCases.queues.MicrotaskHandler;
import infrastructure.useCases.queues.TimerTaskHandler;

public class EventLoop {
    private final EventLoopHandler eventLoopHandler;

    public EventLoop() {
        CallStackHandler callstackHandler = new CallStackHandler();
        MicrotaskHandler microtaskHandler = new MicrotaskHandler();
        TimerTaskHandler timerTaskHandler = new TimerTaskHandler();

        this.eventLoopHandler = new EventLoopHandler(callstackHandler, microtaskHandler, timerTaskHandler);
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

    public void run() {
        try {
            eventLoopHandler.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
