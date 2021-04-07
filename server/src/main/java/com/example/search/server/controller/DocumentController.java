package com.example.search.server.controller;

import com.example.search.api.CreateDocumentRequestDto;
import com.example.search.api.GetDocumentResponseDto;
import com.example.search.api.SearchResponseDto;
import com.example.search.server.service.DocumentService;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createDocument(@RequestBody @Valid CreateDocumentRequestDto document) {
        documentService.createNewDocument(document.getDocumentKey(), document.getContent());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResponseDto getDocumentsByTokens(@NotNull @RequestParam(value = "token") Set<String> tokens) {
        return new SearchResponseDto(documentService.findDocumentsContainingAllTokens(tokens));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{documentKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetDocumentResponseDto> getDocumentByKey(@PathVariable String documentKey) {
        return documentService.getDocumentByKey(documentKey)
                .map(GetDocumentResponseDto::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
