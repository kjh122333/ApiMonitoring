package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * SqlIndexErro
 */
public class SqlIndexError extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public SqlIndexError() {
    }

    public SqlIndexError(String message) {
        super(message);
    }

    public SqlIndexError(Throwable cause) {
        super(cause);
    }

    public SqlIndexError(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlIndexError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}