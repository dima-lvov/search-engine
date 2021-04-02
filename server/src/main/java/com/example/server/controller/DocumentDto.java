package com.example.server.controller;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentDto {

    @NotBlank
    private String documentKey;
    @NotBlank
    private String content;
}
