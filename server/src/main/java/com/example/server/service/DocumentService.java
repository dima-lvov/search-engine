package com.example.server.service;

import java.util.Optional;
import java.util.Set;

public interface DocumentService {

    void saveNewDocument(String key, String content);

    Optional<String> getDocumentByKey(String key);

    Set<String> findDocumentsContainingAllTokens(Set<String> tokens);

    void clearAllDocuments();
}
