package com.example.search.server.service;

import com.example.search.server.repository.ConcurrentDocumentStorage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
public class DocumentServiceImpl implements DocumentService {

    private final ConcurrentDocumentStorage documents;

    public DocumentServiceImpl(ConcurrentDocumentStorage documents) {
        this.documents = documents;
    }

    @Override
    public void createNewDocument(String documentKey, String content) {
        List<String> valueBefore = documents.putIfAbsent(documentKey, List.of(StringUtils.split(content, " ")));
        if (valueBefore != null) {
            throw new DocumentAlreadyExistsException(documentKey);
        }
    }

    @Override
    public Optional<String> getDocumentByKey(String key) {
        return Optional.ofNullable(documents.get(key))
                .map(tokens -> String.join(" ", tokens));
    }

    @Override
    public Set<String> findDocumentsContainingAllTokens(Set<String> tokens) {
        return documents.entrySet().stream()
                .filter(containsAllOf(tokens))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    private Predicate<Map.Entry<String, List<String>>> containsAllOf(Set<String> tokens) {
        return entry -> entry.getValue().containsAll(tokens);
    }

}
