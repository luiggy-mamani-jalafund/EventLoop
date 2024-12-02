package domain.entities.tasks.concrete.immediates;

import domain.entities.tasks.interfaces.ITask;

public class ImmediateTask implements ITask<Runnable> {

    private final Runnable runnable;

    public ImmediateTask(final Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public Runnable getExecutor() {
        return runnable;
    }
}
