package com.example.room_service.dto.request;

import com.example.room_service.validations.CreateRoomRules;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateRoomDto {

    @NotNull
    @Length(min = CreateRoomRules.MIN_LENGTH, max = CreateRoomRules.MAX_LENGTH)
    private String title;
}
