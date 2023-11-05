package com.kanri.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {
    private Integer status;
    private Map<String, String> errors;
    private Instant timestamp;

    public ErrorResponse(HttpStatus status, String error) {
        this(status, Map.of("msg", error));
    }

    public ErrorResponse(HttpStatus status, Map<String, String> errors) {
        this.timestamp = Instant.now();
        this.status = status.value();
        this.errors = errors;
    }
}
