package com.example.server.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class SearchResponseDto {
    private final Set<String> documentKeys;
}
