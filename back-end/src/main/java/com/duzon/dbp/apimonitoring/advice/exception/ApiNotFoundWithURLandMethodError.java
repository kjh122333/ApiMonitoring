package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ApiNotFoundWithURLandMethod
 */
public class ApiNotFoundWithURLandMethodError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiNotFoundWithURLandMethodError() {
    }

    public ApiNotFoundWithURLandMethodError(String message) {
        super(message);
    }

    public ApiNotFoundWithURLandMethodError(Throwable cause) {
        super(cause);
    }

    public ApiNotFoundWithURLandMethodError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiNotFoundWithURLandMethodError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}