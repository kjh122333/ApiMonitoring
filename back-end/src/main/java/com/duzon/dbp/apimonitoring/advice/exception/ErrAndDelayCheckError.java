package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ErrAndDelayCheckErrorException
 */
public class ErrAndDelayCheckError extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ErrAndDelayCheckError() {
    }

    public ErrAndDelayCheckError(String message) {
        super(message);
    }

    public ErrAndDelayCheckError(Throwable cause) {
        super(cause);
    }

    public ErrAndDelayCheckError(String message, Throwable cause) {
        super(message, cause);
    }

    public ErrAndDelayCheckError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}