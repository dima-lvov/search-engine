package com.example.server.controller;

import com.example.server.api.ErrorResponseDto;
import com.example.server.service.DocumentAlreadyExistsException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

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
