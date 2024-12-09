package application.useCases.exceptions;

public interface IErrorHandler {
    void handleError(Runnable task, Throwable error);
}
