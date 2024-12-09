package domain.exceptions;

import application.useCases.exceptions.ErrorNotificationStrategy;

public class DefaultErrorNotificationStrategy implements ErrorNotificationStrategy {
    @Override
    public void notify(Runnable task, Throwable error) {
        System.err.println("Task " + task + " failed with error: " + error.getMessage());
    }
}
