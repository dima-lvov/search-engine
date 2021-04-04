package com.example.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final Map<String, String> documents = new ConcurrentHashMap<>();

    @Override
    public void saveNewDocument(String key, String content) {
        String valueBefore = documents.putIfAbsent(key, content);
        if (valueBefore != null) {
            throw new DocumentAlreadyExistsException(key);
        }
    }

    @Override
    public Optional<String> getDocumentByKey(String key) {
        return Optional.ofNullable(documents.get(key));
    }

    @Override
    public Set<String> findDocumentsContainingAllTokens(Set<String> tokens) {
        return documents.entrySet().stream()
                .filter(containsAllOf(tokens))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public void clearAllDocuments() {
        documents.clear();
    }

    protected Predicate<Map.Entry<String, String>> containsAllOf(Set<String> tokens) {
        Set<Pattern> patterns = compilePatterns(tokens);
        return entry -> patterns.stream()
                .allMatch(pattern -> pattern.matcher(entry.getValue()).find());
    }

    private Set<Pattern> compilePatterns(Set<String> tokens) {
        return tokens.stream()
                .map(this::toWordRegex)
                .map(Pattern::compile)
                .collect(Collectors.toSet());
    }

    private String toWordRegex(String word) {
        return "(^|\\s)" + word + "($|\\s)";
    }

}
