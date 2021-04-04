package com.example.server.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GetDocumentResponseDto {
    private final String content;
}
