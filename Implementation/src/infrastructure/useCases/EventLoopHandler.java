package infrastructure.useCases;

import application.useCases.IEventLoopHandler;
import application.useCases.queues.IMicrotaskHandler;
import application.useCases.queues.ITimerTaskHandler;
import domain.entities.tasks.concrete.promises.Promise;
import domain.entities.tasks.interfaces.IPromiseTask;
import application.useCases.queues.ICallstackHandler;
import domain.entities.tasks.interfaces.ITask;
import domain.entities.tasks.interfaces.ITimerTask;
import infrastructure.utils.Sleeper;

public class EventLoopHandler implements IEventLoopHandler {
    private final long SLEEP_LOOP_MILLISECONDS = 1;
    private final ICallstackHandler callstackHandler;
    private final IMicrotaskHandler microtaskHandler;
    private final ITimerTaskHandler timerTaskHandler;

    public EventLoopHandler(ICallstackHandler callstackHandler, IMicrotaskHandler microtaskHandler, ITimerTaskHandler timerTaskHandler) {
        this.callstackHandler = callstackHandler;
        this.microtaskHandler = microtaskHandler;
        this.timerTaskHandler = timerTaskHandler;
    }

    @Override
    public void run() {
        while (true) {
            callstackHandler.runTasks();

            while (microtaskHandler.hasTasks()) {
                Runnable microtask = microtaskHandler.getMicrotask();
                microtask.run();
            }

            timerTaskHandler.runTasks();

            Sleeper.tryToSleepOrDie(SLEEP_LOOP_MILLISECONDS);
        }
    }

    @Override
    public void executeTask(ITask<Runnable> task) {
        callstackHandler.addTask(task.getExecutor());
    }

    @Override
    public <T> Promise<T> executePromise(IPromiseTask<T> task) {
        return microtaskHandler.createPromise(task.getExecutor());
    }

    @Override
    public <T> Promise<T> resolvePromise(T value) {
        return microtaskHandler.resolvePromise(value);
    }

    @Override
    public <T> Promise<T> rejectPromise(Throwable error) {
        return microtaskHandler.rejectPromise(error);
    }

    public void setTimeout(ITimerTask timerTask) {
        timerTaskHandler.addTask(timerTask);
    }

    public boolean clearTimeout(String timerTaskId) {
        return timerTaskHandler.cancelTask(timerTaskId);
    }
}