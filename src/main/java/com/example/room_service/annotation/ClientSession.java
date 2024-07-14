package com.example.room_service.annotation;

import java.lang.annotation.*;

import io.swagger.v3.oas.annotations.Hidden;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
@Hidden
public @interface ClientSession {
}
