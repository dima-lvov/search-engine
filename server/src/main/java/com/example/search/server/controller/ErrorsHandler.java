package com.example.search.server.controller;

import com.example.search.api.ErrorResponseDto;
import com.example.search.server.service.DocumentAlreadyExistsException;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class ErrorsHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return new ErrorResponseDto(getErrorDescription(exception));
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ErrorResponseDto handleDocumentAlreadyExistsException(DocumentAlreadyExistsException exception) {
        return new ErrorResponseDto(exception.getMessage());
    }

    private String getErrorDescription(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
    }
}
