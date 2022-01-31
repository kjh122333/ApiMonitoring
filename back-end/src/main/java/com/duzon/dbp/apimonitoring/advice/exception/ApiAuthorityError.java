package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ApiAuthorityError
 */
public class ApiAuthorityError extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ApiAuthorityError() {
    }

    public ApiAuthorityError(String message) {
        super(message);
    }

    public ApiAuthorityError(Throwable cause) {
        super(cause);
    }

    public ApiAuthorityError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiAuthorityError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}