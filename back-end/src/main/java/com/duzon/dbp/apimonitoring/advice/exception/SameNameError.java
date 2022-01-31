package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ServiceCategorySameNameError
 */
public class SameNameError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SameNameError() {
    }

    public SameNameError(String message) {
        super(message);
    }

    public SameNameError(Throwable cause) {
        super(cause);
    }

    public SameNameError(String message, Throwable cause) {
        super(message, cause);
    }

    public SameNameError(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}