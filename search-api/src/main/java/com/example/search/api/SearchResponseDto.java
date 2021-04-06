package com.example.search.api;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SearchResponseDto {
    private final Set<String> documentKeys;
}
