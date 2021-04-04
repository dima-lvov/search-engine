package com.example.server.service;

/**
 * Thrown to indicate an attempt to create an already existing document.
 */
public class DocumentAlreadyExistsException extends RuntimeException {
    public DocumentAlreadyExistsException(String key) {
        super(String.format("Document with key: [%s] already exists.", key));
    }
}
