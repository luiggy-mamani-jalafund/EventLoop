package infrastructure.useCases.queues;

import application.useCases.exceptions.IErrorHandler;
import application.useCases.queues.IIntervalTaskHandler;
import domain.entities.tasks.concrete.intervals.ScheduledIntervalTask;
import domain.entities.tasks.interfaces.IIntervalTask;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.UUID;

public class IntervalTaskHandler implements IIntervalTaskHandler {
    private final PriorityQueue<ScheduledIntervalTask> intervalQueue;
    private final IErrorHandler errorHandler;

    public IntervalTaskHandler(IErrorHandler errorHandler) {
        this.intervalQueue = new PriorityQueue<>(Comparator.comparingLong(ScheduledIntervalTask::getNextExecutionTime));
        this.errorHandler = errorHandler;
    }

    @Override
    public void runTasks() {
        long currentTime = System.currentTimeMillis();

        while (!intervalQueue.isEmpty() && intervalQueue.peek().getNextExecutionTime() <= currentTime) {
            ScheduledIntervalTask scheduledTask = intervalQueue.poll();

            if (scheduledTask == null || scheduledTask.getTask() == null) {
                continue;
            }

            try {
                scheduledTask.getTask().getExecutor().run();
                scheduledTask.setNextExecutionTime(currentTime + scheduledTask.getTask().getPeriodMilliseconds());
                intervalQueue.offer(scheduledTask);
            } catch (Throwable e) {
                errorHandler.handleError(scheduledTask.getTask().getExecutor(), e);
            }
        }
    }


    @Override
    public void addTask(IIntervalTask task) {
        if (task == null) {
            throw new IllegalArgumentException("Interval task cannot be null.");
        }
        intervalQueue.offer(new ScheduledIntervalTask(task));
    }

    @Override
    public boolean hasTasks() {
        return !intervalQueue.isEmpty();
    }

    @Override
    public boolean cancelInterval(UUID intervalId) {
        return intervalQueue.removeIf(scheduledTask ->
                scheduledTask.getTask().getIntervalId().equals(intervalId)
        );
    }
}
