package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * EmailSendEmployeeError
 */
public class EmailSendEmployeeError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmailSendEmployeeError() {
    }

    public EmailSendEmployeeError(String message) {
        super(message);
    }

    public EmailSendEmployeeError(Throwable cause) {
        super(cause);
    }

    public EmailSendEmployeeError(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailSendEmployeeError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}