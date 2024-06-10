package com.example.room_service.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Room {

    @Id
    private String id;

    @NotNull
    private String title;

    @CreatedDate
    private Date createdAt;
}
