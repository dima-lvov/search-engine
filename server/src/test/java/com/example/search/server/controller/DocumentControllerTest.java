package com.example.search.server.controller;

import com.example.search.server.service.DocumentAlreadyExistsException;
import com.example.search.server.service.DocumentService;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.blankString;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

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
        Mockito.doThrow(new DocumentAlreadyExistsException("Key"))
                .when(documentService).createNewDocument(eq("Key"), any());

        mockMvc.perform(post("/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"documentKey\":\"Key\", \"content\":\"word1 word2\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Document with key: [Key] already exists."));
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
        when(documentService.getDocumentByKey("Key")).thenReturn(Optional.of("word1 word2"));

        mockMvc.perform(get("/documents/{documentKey}", "Key"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("word1 word2"));
    }

    @Test
    void shouldRespondWithNotFoundOnGetNotExistingDocument() throws Exception {
        when(documentService.getDocumentByKey("Key")).thenReturn(Optional.empty());

        mockMvc.perform(get("/documents/{documentKey}", "Key"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(blankString()));
    }

    @Test
    void shouldRespondWithOkOnGetDocumentByTokens() throws Exception {
        when(documentService.findDocumentsContainingAllTokens(any())).thenReturn(Set.of("Key1", "Key2"));

        mockMvc.perform(get("/documents")
                .param("token", "word1", "word2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.documentKeys")
                        .value(containsInAnyOrder("Key1", "Key2")));
    }

}
