package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ServiceCategoryNotFoundWithNumberError
 */
public class ServiceCategoryNotFoundWithNumberError extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ServiceCategoryNotFoundWithNumberError() {
    }

    public ServiceCategoryNotFoundWithNumberError(String message) {
        super(message);
    }

    public ServiceCategoryNotFoundWithNumberError(Throwable cause) {
        super(cause);
    }

    public ServiceCategoryNotFoundWithNumberError(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceCategoryNotFoundWithNumberError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}