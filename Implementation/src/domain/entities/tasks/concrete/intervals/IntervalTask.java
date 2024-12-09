package domain.entities.tasks.concrete.intervals;

import domain.entities.tasks.interfaces.IIntervalTask;

import java.util.UUID;

public class IntervalTask implements IIntervalTask {
    private final UUID intervalId;
    private final Runnable runnable;
    private final long periodMilliseconds;

    public IntervalTask(final Runnable runnable, final long periodMilliseconds) {
        this.runnable = runnable;
        this.periodMilliseconds = periodMilliseconds;

        this.intervalId = UUID.randomUUID();
    }

    @Override
    public Runnable getExecutor() {
        return runnable;
    }

    @Override
    public UUID getIntervalId() {
        return intervalId;
    }

    @Override
    public long getPeriodMilliseconds() {
        return periodMilliseconds;
    }
}
