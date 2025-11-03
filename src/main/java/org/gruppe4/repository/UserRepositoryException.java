package org.gruppe4.repository;

public class UserRepositoryException extends RuntimeException {
    public UserRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRepositoryException(String message) {
        super(message);
    }
}
