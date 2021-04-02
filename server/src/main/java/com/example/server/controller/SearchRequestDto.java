package com.example.server.controller;

import java.util.Set;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestDto {

    @NotEmpty
    private Set<String> tokens;
}
