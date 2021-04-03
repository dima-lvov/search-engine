package com.example.server.controller;

/**
 * Thrown to indicate an attempt to create an already existing document.
 */
public class DocumentAlreadyExistsException extends RuntimeException {
    public DocumentAlreadyExistsException(String message) {
        super(message);
    }
}
