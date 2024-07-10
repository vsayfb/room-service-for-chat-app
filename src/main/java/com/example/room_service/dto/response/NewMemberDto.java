package com.example.room_service.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NewMemberDto {

    @NotNull
    private String username;

    @NotNull
    private String sessionId;

    @NotNull
    private String userId;
}
