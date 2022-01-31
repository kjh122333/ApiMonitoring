package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * IdAndTokenNotMatchError
 */
public class IdAndTokenNotMatchError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public IdAndTokenNotMatchError() {
    }

    public IdAndTokenNotMatchError(String message) {
        super(message);
    }

    public IdAndTokenNotMatchError(Throwable cause) {
        super(cause);
    }

    public IdAndTokenNotMatchError(String message, Throwable cause) {
        super(message, cause);
    }

    public IdAndTokenNotMatchError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}