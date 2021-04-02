package com.example.server.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DocumentService {

    private static final Map<String, String> DOCUMENT_STORAGE = new ConcurrentHashMap<>();

    public void saveDocument(String key, String content) {
        if (DOCUMENT_STORAGE.putIfAbsent(key, content) != null) {
            throw new IllegalArgumentException(String.format("Document with key %s already exists", key));
        }
    }

    public Optional<String> getDocument(String key) {
        return Optional.ofNullable(DOCUMENT_STORAGE.get(key));
    }

    public Set<String> search(Set<String> tokens) {
        return Collections.emptySet();
    }
}
