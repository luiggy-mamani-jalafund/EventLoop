package domain.entities.tasks.concrete.promises;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class Promise<T> {

    private final CompletableFuture<T> future;

    public Promise(CompletableFuture<T> future) {
        this.future = future;
    }

    public <U> Promise<U> then(Function<T, U> onFulfilled) {
        return new Promise<>(future.thenApplyAsync(onFulfilled));
    }

    public Promise<T> catchError(Consumer<Throwable> onRejected) {
        CompletableFuture<T> catchFuture = future.exceptionally((exception) -> {
            onRejected.accept(exception);
            return null;
        });

        return new Promise<>(catchFuture);
    }

    public void finallyDo(Runnable onFinally) {
        future.whenComplete((result, error) ->
                onFinally.run());
    }

    public Promise<T> thenAccept(Consumer<? super T> action) {
        return new Promise<>(future.thenApply(value -> {
            action.accept(value);
            return value;
        }));
    }
}
