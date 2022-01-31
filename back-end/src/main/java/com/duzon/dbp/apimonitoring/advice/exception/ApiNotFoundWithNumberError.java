package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ApiNotFoundWithNumberError
 */
public class ApiNotFoundWithNumberError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiNotFoundWithNumberError() {
    }

    public ApiNotFoundWithNumberError(String message) {
        super(message);
    }

    public ApiNotFoundWithNumberError(Throwable cause) {
        super(cause);
    }

    public ApiNotFoundWithNumberError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiNotFoundWithNumberError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}