package com.example.search.api;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDocumentRequestDto {

    @NotEmpty(message = "Key of the document should not be empty.")
    private String documentKey;

    @NotEmpty(message = "Content of the document should not be empty.")
    private String content;
}
