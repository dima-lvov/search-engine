package com.example.search.server.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Component;

@Component
public class ConcurrentDocumentStorage implements DocumentStorage {

    // I know this annotation is ugly:) Used for prototyping only.
    @Delegate
    private final Map<String, List<String>> documents = new ConcurrentHashMap<>();
}
