package com.example.search.server.controller;

import com.example.search.api.ErrorResponseDto;
import com.example.search.server.service.DocumentAlreadyExistsException;
import java.util.stream.Collectors;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ResponseBody
@ControllerAdvice
public class ErrorsHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DocumentAlreadyExistsException.class)
    public ErrorResponseDto handleDocumentAlreadyExistsException(DocumentAlreadyExistsException exception) {
        return new ErrorResponseDto(exception.getMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException exception,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto("Missing request param: " + exception.getParameterName()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponseDto(getErrorDescription(exception)));
    }

    private String getErrorDescription(MethodArgumentNotValidException exception) {
        return exception.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
    }
}
