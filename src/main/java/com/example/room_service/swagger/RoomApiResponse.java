package com.example.room_service.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.room_service.model.Room;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;

@Data
class Response {
    private Room data;
}

@Operation(summary = "Fetch room by id")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room created.", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = Response.class)) }),
        @ApiResponse(responseCode = "404", description = "Room not found.", content = @Content)
})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoomApiResponse {
}
