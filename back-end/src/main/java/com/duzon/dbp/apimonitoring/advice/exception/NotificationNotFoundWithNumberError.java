package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * NotificationNotFoundWithNumberError
 */
public class NotificationNotFoundWithNumberError extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public NotificationNotFoundWithNumberError() {
    }

    public NotificationNotFoundWithNumberError(String message) {
        super(message);
    }

    public NotificationNotFoundWithNumberError(Throwable cause) {
        super(cause);
    }

    public NotificationNotFoundWithNumberError(String message, Throwable cause) {
        super(message, cause);
    }

    public NotificationNotFoundWithNumberError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}