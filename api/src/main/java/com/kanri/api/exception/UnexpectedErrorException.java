package com.kanri.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnexpectedErrorException extends BaseHttpException {
    public UnexpectedErrorException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
