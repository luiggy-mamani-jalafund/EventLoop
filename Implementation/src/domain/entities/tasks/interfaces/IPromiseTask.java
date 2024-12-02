package domain.entities.tasks.interfaces;

import java.util.function.Supplier;

public interface IPromiseTask<T> extends ITask<Supplier<T>> {
}
