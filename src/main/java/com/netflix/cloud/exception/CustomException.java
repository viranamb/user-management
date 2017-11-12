package com.netflix.cloud.exception;

/**
 * Custom exception class
 */
public class CustomException extends Exception {

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

}
