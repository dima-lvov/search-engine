package com.example.search.server.service;

import com.example.search.server.repository.DocumentStorage;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintViolationException;
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

        assertThat(documentStorage).isEmpty();
    }

    @NullAndEmptySource
    @ParameterizedTest
    void createNewDocument_InvalidContent(String content) {
        assertThatThrownBy(() -> documentService.createNewDocument("someKey", content))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("createNewDocument.content: must not be empty");

        assertThat(documentStorage).isEmpty();
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
                new FindDocumentTestCase("Empty document storage, 1 search token, no document keys expected")
                        .forDocuments(Collections.emptyMap())
                        .searchedByTokens(Set.of("abc"))
                        .expectedResult(Collections.emptySet()),

                new FindDocumentTestCase("1 document with no matching tokens, 1 search token, no document keys expected")
                        .forDocuments(Map.of("key1", List.of("Abc", "ABC", "abcd", "dabc")))
                        .searchedByTokens(Set.of("abc"))
                        .expectedResult(Collections.emptySet()),

                new FindDocumentTestCase("1 document with single token, 1 search token, 1 key expected")
                        .forDocuments(Map.of("key1", List.of("abc")))
                        .searchedByTokens(Set.of("abc"))
                        .expectedResult(Set.of("key1")),

                new FindDocumentTestCase("3 documents in storage, empty search tokens, all document keys expected")
                        .forDocuments(Map.of(
                                "key1", List.of("Abc", "|?@#!", "1Qwrt", "abc"),
                                "key2", List.of("Abc", "|?@#!", "1Qwrt"),
                                "key3", List.of("abc")))
                        .searchedByTokens(Collections.emptySet())
                        .expectedResult(Set.of("key1", "key2", "key3")),

                new FindDocumentTestCase("Multiple documents in storage, 1 search token, 2 document keys expected")
                        .forDocuments(Map.of(
                                "key1", List.of("Abc", "|?@#!", "1Qwrt", "abc"),
                                "key2", List.of("Abc", "|?@#!", "1Qwrt"),
                                "key3", List.of("abc")))
                        .searchedByTokens(Set.of("abc"))
                        .expectedResult(Set.of("key1", "key3")),

                new FindDocumentTestCase("Multiple documents in storage, 3 search tokens, 2 document keys expected")
                        .forDocuments(Map.of(
                                "key1", List.of("Abc", "|?@#!", "1Qwrt", "1Qwrt", "abc"),
                                "key2", List.of("Abc", "|?@#!", "1Qwrt"),
                                "key3", List.of("abc", "|?@#!", "1Qwrt"),
                                "key4", List.of("Abc", "|?@#!")))
                        .searchedByTokens(Set.of("Abc", "|?@#!", "1Qwrt"))
                        .expectedResult(Set.of("key1", "key2"))
        );
    }

    @AfterEach
    void tearDown() {
        documentStorage.clear();
    }

    @Accessors(fluent = true)
    static class FindDocumentTestCase {

        final String caseDescription;
        Map<String, List<String>> forDocuments;
        Set<String> searchedByTokens;
        Set<String> expectedResult;

        public FindDocumentTestCase(String caseDescription) {
            this.caseDescription = caseDescription;
        }

        @Override
        public String toString() {
            return caseDescription;
        }

        public FindDocumentTestCase forDocuments(Map<String, List<String>> forDocuments) {
            this.forDocuments = forDocuments;
            return this;
        }

        public FindDocumentTestCase searchedByTokens(Set<String> searchedByTokens) {
            this.searchedByTokens = searchedByTokens;
            return this;
        }

        public FindDocumentTestCase expectedResult(Set<String> expectedResult) {
            this.expectedResult = expectedResult;
            return this;
        }
    }
}