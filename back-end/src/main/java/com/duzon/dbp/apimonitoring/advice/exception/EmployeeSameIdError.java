package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * EmployeeNameError
 */
public class EmployeeSameIdError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmployeeSameIdError() {
    }

    public EmployeeSameIdError(String message) {
        super(message);
    }

    public EmployeeSameIdError(Throwable cause) {
        super(cause);
    }

    public EmployeeSameIdError(String message, Throwable cause) {
        super(message, cause);
    }

    public EmployeeSameIdError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}