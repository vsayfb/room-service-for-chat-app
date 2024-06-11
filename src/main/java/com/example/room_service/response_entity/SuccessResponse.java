package com.example.room_service.response_entity;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Getter
public class SuccessResponse<T> {

    private Date timestamp;
    private final int status;
    private final String message;
    private final T data;

    @JsonIgnore
    private final HttpStatus httpStatus;

    public SuccessResponse(T data, String message, HttpStatus httpStatus) {
        this.data = data;
        this.message = message;
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
    }

    public ResponseEntity<SuccessResponse<T>> send() {

        this.timestamp = new Date();

        return new ResponseEntity<>(this, httpStatus);
    }
}
