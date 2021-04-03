package com.example.server.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class CreateDocumentRequestDto {

    @NotBlank(message = "Key of the document should not be empty.")
    private String documentKey;

    @NotBlank(message = "Content of the document should not be empty.")
    private String content;
}
