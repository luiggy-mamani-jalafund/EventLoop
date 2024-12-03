package presentation;

import application.useCases.queues.ICallstackHandler;
import domain.entities.tasks.interfaces.ITask;
import infrastructure.useCases.EventLoopHandler;
import infrastructure.useCases.queues.CallStackHandler;

public class EventLoop {

    private final EventLoopHandler eventLoopHandler;

    public EventLoop() {
        ICallstackHandler callstackHandler = new CallStackHandler();

        eventLoopHandler = new EventLoopHandler(callstackHandler);
    }

    public void execute(ITask<Runnable> task) {
        eventLoopHandler.executeTask(task);
    }

    public void run() {
        eventLoopHandler.run();
    }

}
