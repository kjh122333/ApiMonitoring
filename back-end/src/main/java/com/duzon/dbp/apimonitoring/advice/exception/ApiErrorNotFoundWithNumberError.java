package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ApiErrorNotFoundWithNumberError
 */
public class ApiErrorNotFoundWithNumberError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiErrorNotFoundWithNumberError() {
    }

    public ApiErrorNotFoundWithNumberError(String message) {
        super(message);
    }

    public ApiErrorNotFoundWithNumberError(Throwable cause) {
        super(cause);
    }

    public ApiErrorNotFoundWithNumberError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiErrorNotFoundWithNumberError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}