package domain.entities.tasks.concrete.promises;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Promise<T> {

    private final CompletableFuture<T> future;

    public Promise(CompletableFuture<T> future) {
        this.future = future;
    }

    public Promise<T> then(Consumer<T> thenAction) {
        future.thenAccept(thenAction);

        return this;
    }

    public void catchException(Consumer<Throwable> catchAction) {
        future.exceptionally((ex) -> {
            catchAction.accept(ex);
            return null;
        });
    }
}
