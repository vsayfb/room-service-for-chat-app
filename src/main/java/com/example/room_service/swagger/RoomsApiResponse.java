package com.example.room_service.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import com.example.room_service.model.Room;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Data;

@Data
class RoomResponse {
    private List<Room> rooms;
}

@Data
class ResponseData {
    RoomResponse data;
}

@Operation(summary = "Fetch room by id")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Room found.", content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseData.class)) }),
        @ApiResponse(responseCode = "404", description = "Room not found.", content = @Content)
})
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoomsApiResponse {
}
