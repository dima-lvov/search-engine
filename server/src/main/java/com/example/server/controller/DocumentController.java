package com.example.server.controller;

import com.example.server.api.CreateDocumentRequestDto;
import com.example.server.api.GetDocumentResponseDto;
import com.example.server.api.SearchResponseDto;
import com.example.server.service.DocumentService;
import com.example.server.service.DocumentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createDocument(@RequestBody @Valid CreateDocumentRequestDto document) {
        documentService.saveNewDocument(document.getDocumentKey(), document.getContent());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public SearchResponseDto getDocumentsByTokens(@RequestParam("token") Set<String> tokens) {
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
