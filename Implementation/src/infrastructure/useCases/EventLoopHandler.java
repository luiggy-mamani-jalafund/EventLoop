package infrastructure.useCases;

import application.useCases.queues.ICallstackHandler;
import application.useCases.IEventLoopHandler;
import domain.entities.tasks.interfaces.ITask;
import infrastructure.utils.Sleeper;

public class EventLoopHandler implements IEventLoopHandler {

    private final long SLEEP_LOOP_MILLISECONDS = 1;
    private final ICallstackHandler callstackHandler;

    public EventLoopHandler(ICallstackHandler callstackHandler) {
        this.callstackHandler = callstackHandler;
    }

    @Override
    public void run() {
        while (callstackHandler.hasTasks()) {
            callstackHandler.runTasks();

            Sleeper.tryToSleepOrDie(SLEEP_LOOP_MILLISECONDS);
        }
    }

    @Override
    public void executeTask(ITask<Runnable> task) {
        callstackHandler.addTask(task.getExecutor());
    }
}
