package infrastructure.useCases.queues;

import application.useCases.exceptions.IErrorHandler;
import application.useCases.queues.ITimerTaskHandler;
import domain.entities.tasks.concrete.timers.TimerTask;
import domain.entities.tasks.interfaces.ITimerTask;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class TimerTaskHandler implements ITimerTaskHandler {
    private final Queue<ITimerTask> timerQueue;
    private final IErrorHandler errorHandler;

    public TimerTaskHandler(IErrorHandler errorHandler) {
        this.timerQueue = new ArrayDeque<>();
        this.errorHandler = errorHandler;
    }

    @Override
    public void addTask(ITimerTask task) {
        timerQueue.offer(task);
    }

    @Override
    public boolean hasTasks() {
        return !timerQueue.isEmpty();
    }

    @Override
    public void runTasks() {
        long currentTime = System.currentTimeMillis();

        timerQueue.removeIf(timerTask -> {
            if (timerTask.getScheduledTime() <= currentTime) {
                try {
                    timerTask.getExecutor().run();
                } catch (Throwable e) {
                    errorHandler.handleError(timerTask.getExecutor(), e);
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean cancelTask(String taskId) {
        Iterator<ITimerTask> iterator = timerQueue.iterator();

        while (iterator.hasNext()) {
            ITimerTask task = iterator.next();
            if (task instanceof TimerTask && ((TimerTask) task).getId().equals(taskId)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
}
