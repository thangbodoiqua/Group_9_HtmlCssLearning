package com.se2.htmlcsslearning.exception;

/**
 * Thrown when a registration attempt is made with an email
 * that is already associated with an existing account.
 */
public class EmailAlreadyExistsException extends AppException {
    public EmailAlreadyExistsException(String email) {
        super("This email address is already registered: " + email);
    }
}
