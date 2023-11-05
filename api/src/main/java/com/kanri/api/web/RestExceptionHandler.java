package com.kanri.api.web;

import com.kanri.api.dto.ErrorResponse;
import com.kanri.api.exception.BadRequestException;
import com.kanri.api.exception.BaseHttpException;
import com.kanri.api.exception.ForbiddenException;
import com.kanri.api.exception.NotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(BaseHttpException.class)
    public ResponseEntity<?> HttpExceptionHandler(BaseHttpException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getStatus(), ex.getMessage()), ex.getStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleDTOValidationExceptions(MethodArgumentNotValidException ex ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(new ErrorResponse(status, errors), status);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<?> handleURLVariableValidationExceptions(ConstraintViolationException ex ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach((error) -> {
            String field = getFieldFromPropertyPath(error.getPropertyPath());
            String errorMessage = error.getMessage();
            errors.put(field, errorMessage);
        });
        return new ResponseEntity<>(new ErrorResponse(status, errors), status);
    }

    private String getFieldFromPropertyPath(Path path) {
        String field = "msg";           // in case the path is empty
        for (Path.Node node : path) {
            field = node.getName();
        }
        return field;
    }
}
