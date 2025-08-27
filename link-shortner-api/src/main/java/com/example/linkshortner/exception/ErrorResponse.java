package com.example.linkshortner.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private String errorMessage;
    private HttpStatus status;
    private int statusCode;
    private LocalDateTime timeStamp;

    public ErrorResponse(String errorMessage, HttpStatus status, int code) {
        this.errorMessage = errorMessage;
        this.status = status;
        this.statusCode = code;
        this.timeStamp = LocalDateTime.now();
    }
}
