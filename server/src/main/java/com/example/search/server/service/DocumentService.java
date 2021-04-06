package com.example.search.server.service;

import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

public interface DocumentService {

    /**
     * Creates new document with given key and content.
     *
     * @param documentKey unique document key
     * @param content     series of tokens (words) separated by spaces.
     * @throws DocumentAlreadyExistsException if document with given key already exists
     */
    void createNewDocument(@NotEmpty String documentKey, @NotEmpty String content);

    /**
     * Finds keys of all documents that contain all tokens in the set.
     *
     * @param tokens set of tokens
     * @return matching document keys
     */
    Set<String> findDocumentsContainingAllTokens(Set<String> tokens);

    Optional<String> getDocumentByKey(@NotBlank String key);
}
