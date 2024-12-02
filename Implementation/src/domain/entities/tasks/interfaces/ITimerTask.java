package domain.entities.tasks.interfaces;

public interface ITimerTask extends ITask<Runnable> {
    long getScheduledTime();
}
