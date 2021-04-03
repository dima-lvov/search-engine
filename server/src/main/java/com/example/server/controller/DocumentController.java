package com.example.server.controller;

import com.example.server.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createDocument(@RequestBody @Valid CreateDocumentRequestDto document) {
        documentService.saveDocument(document.getDocumentKey(), document.getContent());
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
