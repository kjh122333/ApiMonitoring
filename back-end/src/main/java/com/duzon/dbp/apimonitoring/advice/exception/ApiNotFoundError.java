package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ApiNotFoundError
 */
public class ApiNotFoundError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiNotFoundError() {
    }

    public ApiNotFoundError(String message) {
        super(message);
    }

    public ApiNotFoundError(Throwable cause) {
        super(cause);
    }

    public ApiNotFoundError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiNotFoundError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}