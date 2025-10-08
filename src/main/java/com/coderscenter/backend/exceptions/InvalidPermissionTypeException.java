package com.coderscenter.backend.exceptions;

public class InvalidPermissionTypeException extends RuntimeException {
    public InvalidPermissionTypeException(String message) {
        super(message);
    }
}
