package com.example.room_service.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
public class Member {
    @Id
    private ObjectId id;

    @NotNull
    private String username;

    @NotNull
    private String userId;

    private ObjectId roomId;

    @CreatedDate
    private Date joinedAt;
}
