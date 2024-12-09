package domain.entities.tasks.interfaces;

import java.util.UUID;

public interface IIntervalTask extends ITask<Runnable> {
    UUID getIntervalId();
    long getPeriodMilliseconds();
}
