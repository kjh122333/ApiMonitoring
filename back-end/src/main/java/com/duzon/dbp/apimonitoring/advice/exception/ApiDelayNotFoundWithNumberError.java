package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ApiDelayNotFoundWithNumberError
 */
public class ApiDelayNotFoundWithNumberError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiDelayNotFoundWithNumberError() {
    }

    public ApiDelayNotFoundWithNumberError(String message) {
        super(message);
    }

    public ApiDelayNotFoundWithNumberError(Throwable cause) {
        super(cause);
    }

    public ApiDelayNotFoundWithNumberError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiDelayNotFoundWithNumberError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}