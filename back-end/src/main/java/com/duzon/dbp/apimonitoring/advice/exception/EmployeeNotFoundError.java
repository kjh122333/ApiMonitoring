package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * CEmployeeNotFoundException
 */
public class EmployeeNotFoundError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmployeeNotFoundError() {
    }

    public EmployeeNotFoundError(String message) {
        super(message);
    }

    public EmployeeNotFoundError(Throwable cause) {
        super(cause);
    }

    public EmployeeNotFoundError(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeNotFoundError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}