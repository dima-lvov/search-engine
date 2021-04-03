package com.example.server.service;

import com.example.server.controller.DocumentAlreadyExistsException;
import com.example.server.repository.DocumentRepository;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;

    private final Cache<String, Set<String>> cache = CacheBuilder.newBuilder().build();

    public void saveDocument(String key, String content) {
        if (!documentRepository.saveNewDocument(key, content)) {
            throw new DocumentAlreadyExistsException(String.format("Document with key: [%s] already exists.", key));
        }
    }

    public Optional<String> getDocumentByKey(String key) {
        return documentRepository.getDocumentByKey(key);
    }

    public Set<String> search(Set<String> tokens) throws ExecutionException {
        return Collections.emptySet();
    }
}
