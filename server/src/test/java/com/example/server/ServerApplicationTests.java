package com.example.server;

import com.example.server.service.DocumentServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ServerApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentServiceImpl documentService;

    @Test
    void shouldRespondWithCreatedOnCreateNewDocument() throws Exception {
        String document = "{\"documentKey\":\"Test document\", \"content\":\"word1 word2\"}";
        mockMvc.perform(post("/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(document))
                .andExpect(status().isCreated())
                .andExpect(content().string(blankString()));
    }

    @Test
    void shouldRespondWithConflictOnCreateDocumentWithExistingKey() throws Exception {
        documentService.saveNewDocument("TestDocumentKey", "word1 word2");
        String documentWithExistingKey = "{\"documentKey\":\"TestDocumentKey\", \"content\":\"whatever\"}";

        mockMvc.perform(post("/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(documentWithExistingKey))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Document with key: [TestDocumentKey] already exists."));
    }

    @Test
    void shouldRespondWithBadRequestOnCreateDocumentWithEmptyContent() throws Exception {
        String document = "{\"documentKey\":\"TestDocumentKey\"}";
        mockMvc.perform(post("/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(document))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Content of the document should not be empty."));
    }

    @Test
    void shouldRespondWithBadRequestOnCreateDocumentWithEmptyKey() throws Exception {
        String document = "{\"content\":\"whatever\"}";
        mockMvc.perform(post("/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(document))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Key of the document should not be empty."));
    }

    @Test
    void shouldRespondWithOkOnGetDocumentByKey() throws Exception {
        documentService.saveNewDocument("TestDocumentKey", "word1 word2");
        mockMvc.perform(get("/documents/{documentKey}", "TestDocumentKey"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("word1 word2"));
    }

    @Test
    void shouldRespondWithNotFoundOnGetNotExistingDocument() throws Exception {
        mockMvc.perform(get("/documents/{documentKey}", "TestDocumentKey"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(blankString()));
    }

    @Test
    void shouldRespondWithOkOnGetDocumentByTokens() throws Exception {
        documentService.saveNewDocument("TestDocumentKey1", "word1 word2 word3");
        documentService.saveNewDocument("TestDocumentKey2", "word1 word2");
        documentService.saveNewDocument("TestDocumentKey3", "word1");
        mockMvc.perform(get("/documents")
                .param("token", "word1", "word2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentKeys")
                        .value(containsInAnyOrder("TestDocumentKey1","TestDocumentKey2")));
    }

    @AfterEach
    void tearDown() {
        documentService.clearAllDocuments();
    }

}
