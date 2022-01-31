package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * CEmailSigninFailedException
 */
public class EmployeeIdSigninFailedError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmployeeIdSigninFailedError(String message, Throwable t) {
        super(message, t);
    }

    public EmployeeIdSigninFailedError(String message) {
        super(message);
    }

    public EmployeeIdSigninFailedError() {
        super();
    }
}