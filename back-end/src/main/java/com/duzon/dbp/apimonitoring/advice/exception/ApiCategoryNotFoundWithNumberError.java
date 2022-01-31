package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ApiCategoryNotFoundWithNumberError
 */
public class ApiCategoryNotFoundWithNumberError extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ApiCategoryNotFoundWithNumberError() {
    }

    public ApiCategoryNotFoundWithNumberError(String message) {
        super(message);
    }

    public ApiCategoryNotFoundWithNumberError(Throwable cause) {
        super(cause);
    }

    public ApiCategoryNotFoundWithNumberError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiCategoryNotFoundWithNumberError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}