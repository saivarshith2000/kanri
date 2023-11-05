package com.kanri.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ForbiddenException extends BaseHttpException {
    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
