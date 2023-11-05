package com.kanri.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseHttpException extends RuntimeException {
    private final HttpStatus status;
    private final String message;

    public BaseHttpException(HttpStatus status, String message) {
        super(message);
        this.message = message;
        this.status = status;
    }
}
