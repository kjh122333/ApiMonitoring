package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * SameURLError
 */
public class SameURLError extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public SameURLError() {
    }

    public SameURLError(String message) {
        super(message);
    }

    public SameURLError(Throwable cause) {
        super(cause);
    }

    public SameURLError(String message, Throwable cause) {
        super(message, cause);
    }

    public SameURLError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}