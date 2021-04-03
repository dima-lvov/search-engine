package com.example.server.repository;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;


public interface DocumentRepository {

    Optional<String> getDocumentByKey(String key);

    Map<String,String> getDocuments();

    boolean saveNewDocument(String key, String content);

    void clearStorage();
}
