package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ServiceNotFoundWithNumberError
 */
public class ServiceNotFoundWithNumberError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ServiceNotFoundWithNumberError() {
    }

    public ServiceNotFoundWithNumberError(String message) {
        super(message);
    }

    public ServiceNotFoundWithNumberError(Throwable cause) {
        super(cause);
    }

    public ServiceNotFoundWithNumberError(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceNotFoundWithNumberError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}