package com.example.room_service.response_entity;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private Date timestamp;
    private final int status;
    private final HttpStatus error;
    private final String message;

    public ErrorResponse(String message, HttpStatus httpStatus) {
        this.message = message;
        this.error = httpStatus;
        this.status = httpStatus.value();
    }

    public ResponseEntity<ErrorResponse> send() {

        this.timestamp = new Date();

        return new ResponseEntity<>(this, error);
    }
}
