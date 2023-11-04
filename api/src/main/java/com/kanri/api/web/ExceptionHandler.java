package com.kanri.api.web;

import com.kanri.api.dto.ErrorResponse;
import com.kanri.api.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<?> NotFoundExceptionHandler(NotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getStatus(), ex.getMessage()), ex.getStatus());
    }
}
