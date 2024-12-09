package domain.entities.tasks.concrete.promises;

import domain.entities.tasks.interfaces.IPromiseTask;

import java.util.function.Supplier;

public class PromiseTask<T> implements IPromiseTask<T> {

    private final Supplier<T> task;

    public PromiseTask(Supplier<T> task) {
        this.task = task;
    }

    @Override
    public Supplier<T> getExecutor() {
        return task;
    }
}
