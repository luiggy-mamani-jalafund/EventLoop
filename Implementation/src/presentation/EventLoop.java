package presentation;

import application.useCases.queues.ICallstackHandler;
import domain.entities.tasks.interfaces.ITask;
import infrastructure.useCases.EventLoopHandler;
import infrastructure.useCases.queues.CallStackHandler;

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
    }

    public void execute(ITask<Runnable> task) {
        eventLoopHandler.executeTask(task);
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
