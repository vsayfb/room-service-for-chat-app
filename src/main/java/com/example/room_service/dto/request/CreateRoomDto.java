package com.example.room_service.dto.request;

import com.example.room_service.validations.CreateRoomRules;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRoomDto {

    @NotBlank
    @Size(min = CreateRoomRules.MIN_LENGTH, max = CreateRoomRules.MAX_LENGTH)
    private String title;
}
