package infrastructure.useCases.queues;

import application.useCases.queues.ITimerTaskHandler;
import domain.entities.tasks.concrete.timers.TimerTask;
import domain.entities.tasks.interfaces.ITimerTask;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

public class TimerTaskHandler implements ITimerTaskHandler {
    private final Queue<ITimerTask> timerQueue;

    public TimerTaskHandler() {
        this.timerQueue = new ArrayDeque<>();
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
                timerTask.getExecutor().run();
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
