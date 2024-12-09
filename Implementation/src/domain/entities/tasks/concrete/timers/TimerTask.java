package domain.entities.tasks.concrete.timers;

import domain.entities.tasks.interfaces.ITimerTask;

import java.util.UUID;

public class TimerTask implements ITimerTask {
    private final String id;
    private final Runnable executable;
    private final long scheduledTime;

    public TimerTask(final Runnable executable, final long delay) {
        this.id = UUID.randomUUID().toString();
        this.executable = executable;
        this.scheduledTime = System.currentTimeMillis() + delay;
    }

    @Override
    public Runnable getExecutor() {
        return executable;
    }

    @Override
    public long getScheduledTime() {
        return scheduledTime;
    }

    public String getId() {
        return id;
    }
}
