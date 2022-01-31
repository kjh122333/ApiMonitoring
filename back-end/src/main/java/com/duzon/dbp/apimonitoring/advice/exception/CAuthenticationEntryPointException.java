package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * CAuthenticationEntryPointException
 * 
 */
public class CAuthenticationEntryPointException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CAuthenticationEntryPointException(String message, Throwable t) {
        super(message, t);
    }

    public CAuthenticationEntryPointException(String message) {
        super(message);
    }

    public CAuthenticationEntryPointException() {
        super();
    }
}