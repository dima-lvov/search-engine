package com.example.server.api;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDocumentRequestDto {

    @NotEmpty(message = "Key of the document should not be empty.")
    private String documentKey;

    @NotEmpty(message = "Content of the document should not be empty.")
    private String content;
}
