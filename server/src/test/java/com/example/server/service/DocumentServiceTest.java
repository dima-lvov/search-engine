package com.example.server.service;

import com.example.server.repository.DocumentStorage;
import com.example.server.service.DocumentServiceTest.FindDocumentTestData.FindDocumentTestDataBuilder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import javax.validation.ConstraintViolationException;
import lombok.Builder;
import lombok.Singular;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.server.service.DocumentServiceTest.FindDocumentTestData.builder;
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

    @MethodSource("findDocumentTestData")
    @ParameterizedTest
    void findDocumentsContainingAllTokens(FindDocumentTestData testData) {
        assertThat(documentService.findDocumentsContainingAllTokens(testData.documents.keySet()))
                .containsExactlyInAnyOrderElementsOf(testData.expectedResult);

    }

    @AfterEach
    void tearDown() {
        documentStorage.clear();
    }

    static Stream<FindDocumentTestData> findDocumentTestData() {
        return Stream.of(
                builder()
                        .document("key1", List.of("Abc", "|?@#!", "1Qwrt", "abc"))
                        .document("key2", List.of("Abc", "|?@#!", "1Qwrt"))
                        .document("key3", List.of("abc"))
                        .searchByTokens(Set.of("abc"))
                        .expectedResult(Set.of("key1", "key3"))
        ).map(FindDocumentTestDataBuilder::build);
    }

    @Builder
    static class FindDocumentTestData {

        @Singular
        final Map<String, List<String>> documents;
        final Set<String> searchByTokens;
        final Set<String> expectedResult;
    }
}