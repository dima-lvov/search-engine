package com.example.server.service;

import com.example.server.repository.DocumentStorage;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class DocumentServiceTest {

    @Autowired
    DocumentStorage documentStorage;

    @Autowired
    DocumentService documentService;

    @Test
    void createNewDocument() {
        documentService.createNewDocument("key", "word1 WORD2 1 _8#%$ ?");

        assertThat(documentStorage).hasSize(1).containsEntry("key", List.of("word1", "WORD2", "1", "_8#%$", "?"));
    }

    @NullAndEmptySource
    @ParameterizedTest
    void createNewDocument_InvalidKey(String key) {
        assertThatThrownBy(() -> documentService.createNewDocument(key, "any content"))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("createNewDocument.documentKey: must not be empty");
    }

    @NullAndEmptySource
    @ParameterizedTest
    void createNewDocument_InvalidContent(String content) {
        assertThatThrownBy(() -> documentService.createNewDocument("someKey", content))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("createNewDocument.content: must not be empty");
    }

    @Test
    void getDocumentByKey_DocumentExist() {
        documentStorage.put("key1", List.of("content1"));
        documentStorage.put("key2", List.of("content2"));

        assertThat(documentService.getDocumentByKey("key2")).contains("content2");
    }

    @Test
    void getDocumentByKey_DocumentNotExist() {
        documentStorage.put("key", List.of("content"));

        assertThat(documentService.getDocumentByKey("otherKey")).isEmpty();
    }

    @MethodSource("findDocumentTestCases")
    @ParameterizedTest
    void findDocumentsContainingAllTokens(FindDocumentTestCase testData) {
        documentStorage.putAll(testData.forDocuments);
        assertThat(documentService.findDocumentsContainingAllTokens(testData.searchedByTokens))
                .containsExactlyInAnyOrderElementsOf(testData.expectedResult);

    }

    static Stream<FindDocumentTestCase> findDocumentTestCases() {
        return Stream.of(
                new FindDocumentTestCase("Empty document storage, 1 search token, no document keys found")
                        .forDocuments(Collections.emptyMap())
                        .searchedByTokens(Set.of("abc"))
                        .expectedResult(Collections.emptySet()),

                new FindDocumentTestCase("1 document with single token, 1 search token, 1 key found")
                        .forDocuments(Map.of("key1", List.of("abc")))
                        .searchedByTokens(Set.of("abc"))
                        .expectedResult(Set.of("key1")),

                new FindDocumentTestCase("3 documents in storage, empty search tokens, all document keys found")
                        .forDocuments(Map.of(
                                "key1", List.of("Abc", "|?@#!", "1Qwrt", "abc"),
                                "key2", List.of("Abc", "|?@#!", "1Qwrt"),
                                "key3", List.of("abc")))
                        .searchedByTokens(Collections.emptySet())
                        .expectedResult(Set.of("key1", "key2", "key3")),

                new FindDocumentTestCase("3 documents in storage, 1 search token, 2 document keys found")
                        .forDocuments(Map.of(
                                "key1", List.of("Abc", "|?@#!", "1Qwrt", "abc"),
                                "key2", List.of("Abc", "|?@#!", "1Qwrt"),
                                "key3", List.of("abc")))
                        .searchedByTokens(Set.of("abc"))
                        .expectedResult(Set.of("key1", "key3"))
        );
    }

    @AfterEach
    void tearDown() {
        documentStorage.clear();
    }

    @Setter
    @Accessors(fluent = true)
    @RequiredArgsConstructor
    static class FindDocumentTestCase {

        final String caseDescription;
        Map<String, List<String>> forDocuments;
        Set<String> searchedByTokens;
        Set<String> expectedResult;

        @Override
        public String toString() {
            return caseDescription;
        }
    }
}