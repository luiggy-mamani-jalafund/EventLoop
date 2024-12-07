package application.useCases.exceptions;

public interface ErrorNotificationStrategy {
    void notify(Runnable task, Throwable error);
}
