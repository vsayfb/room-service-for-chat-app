package com.example.room_service.dto.response;

import java.util.Date;
import java.util.UUID;

import lombok.Data;

@Data
public class NewRoomDto {

    private UUID id;
    private String title;
    private Date createdAt;
}
