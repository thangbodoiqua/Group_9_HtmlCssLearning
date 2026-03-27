package com.se2.htmlcsslearning.exception;

/**
 * Base runtime exception for application-level errors.
 * All custom exceptions should extend this class.
 */
public class AppException extends RuntimeException {
    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }
}
