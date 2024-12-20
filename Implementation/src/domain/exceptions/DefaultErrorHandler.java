package domain.exceptions;

import application.useCases.exceptions.ErrorNotificationStrategy;
import application.useCases.exceptions.IErrorHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultErrorHandler implements IErrorHandler {
    private static final Logger LOGGER = Logger.getLogger(DefaultErrorHandler.class.getName());
    private final ErrorNotificationStrategy notificationStrategy;

    public DefaultErrorHandler() {
        this.notificationStrategy = new DefaultErrorNotificationStrategy();
    }

    public DefaultErrorHandler(ErrorNotificationStrategy notificationStrategy) {
        this.notificationStrategy = notificationStrategy;
    }

    @Override
    public void handleError(Runnable task, Throwable error) {
        String errorMessage = String.format("Task %s failed with error: %s", task, error.getMessage());
        LOGGER.log(Level.SEVERE, errorMessage);

        try {
            notificationStrategy.notify(task, error);
        } catch (Exception notificationError) {
            LOGGER.log(Level.WARNING, "Error in error notification: " + notificationError.getMessage());
        }
    }
}
