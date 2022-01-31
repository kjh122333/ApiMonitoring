package com.duzon.dbp.apimonitoring.advice.exception;

/**
 * ChannelNotFoundError
 */
public class ChannelNotFoundError extends RuntimeException{
    private static final long serialVersionUID = 1L;
    
    public ChannelNotFoundError(String message) {
        super(message);
    }
}