package com.example.search.server.repository;

import java.util.List;
import java.util.Map;

// Simple key/value repository, where the key is a document key and value is a list of tokens in a document.
public interface DocumentStorage extends Map<String, List<String>> {

}
