package domain.entities.tasks.concrete.intervals;

import domain.entities.tasks.interfaces.IIntervalTask;

public class ScheduledIntervalTask {
    private final IIntervalTask task;
    private long nextExecutionTime;

    public ScheduledIntervalTask(IIntervalTask task) {
        this.task = task;
        this.nextExecutionTime = System.currentTimeMillis() + task.getPeriodMilliseconds();
    }

    public IIntervalTask getTask() {
        return task;
    }

    public long getNextExecutionTime() {
        return nextExecutionTime;
    }

    public void setNextExecutionTime(long nextExecutionTime) {
        this.nextExecutionTime = nextExecutionTime;
    }
}
