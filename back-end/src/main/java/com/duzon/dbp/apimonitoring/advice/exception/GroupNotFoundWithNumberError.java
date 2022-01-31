package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * GroupNotFoundWithNumberError
 */
public class GroupNotFoundWithNumberError extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public GroupNotFoundWithNumberError() {
    }

    public GroupNotFoundWithNumberError(String message) {
        super(message);
    }

    public GroupNotFoundWithNumberError(Throwable cause) {
        super(cause);
    }

    public GroupNotFoundWithNumberError(String message, Throwable cause) {
        super(message, cause);
    }

    public GroupNotFoundWithNumberError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}