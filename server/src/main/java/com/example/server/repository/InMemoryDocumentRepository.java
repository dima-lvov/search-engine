package com.example.server.repository;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class InMemoryDocumentRepository implements DocumentRepository {

    private final Map<String, String> documents = new HashMap<>();

    @Override
    public Map<String, String> getDocuments() {
        return new HashMap<>(documents);
    }

    @Override
    public Optional<String> getDocumentByKey(String key) {
        return Optional.ofNullable(documents.get(key));
    }

    @Override
    public boolean saveNewDocument(String key, String content) {
        return documents.putIfAbsent(key, content) == null;
    }

    @Override
    public void clearStorage() {
        documents.clear();
    }
}
